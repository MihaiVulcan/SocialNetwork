package socialnetwork.service;

import socialnetwork.domain.*;
import socialnetwork.repository.Repository;
import socialnetwork.repository.database.GroupUserDatabaseRepository;
import socialnetwork.repository.database.MessageDatabaseRepository;
import socialnetwork.service.datatrasfer.GroupDTO;
import socialnetwork.service.datatrasfer.MessageDTO;
import socialnetwork.utils.events.ChangeEventType;
import socialnetwork.utils.events.MessageChangeEvent;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MessageManagementService implements Observable<MessageChangeEvent> {
    private Repository<String, User> userDatabase;
    private Repository<Long, Group> groupDatabase;
    private GroupUserDatabaseRepository belongDatabase;
    private MessageDatabaseRepository messageDatabase;

    public MessageManagementService(Repository<String, User> userDatabase, Repository<Long, Group> groupDatabase, GroupUserDatabaseRepository belongDatabase, MessageDatabaseRepository messageDatabase) {
        this.userDatabase = userDatabase;
        this.groupDatabase = groupDatabase;
        this.belongDatabase = belongDatabase;
        this.messageDatabase = messageDatabase;
    }

    /**
     * Creats a private group chat and adds the 2 participants to it
     *      after that creates the first message in the private group chat and saves it
     * @param idSender
     * @param idReciver
     * @param text
     */
    public void sendPrivateMessage(String idSender, String idReciver, String text) {
        //create a private group chat
        Group group = groupDatabase.save(new Group("Private Group", "Private Group"));
        //add id and id to the private group chat
        if(group != null){
            belongDatabase.save(new GroupUser(idSender, group.getId()));
            belongDatabase.save(new GroupUser(idReciver, group.getId()));

            //add the message to the group chat
            messageDatabase.save(new Message(idSender, group.getId(), text));
        }
        notifyObservers(new MessageChangeEvent(ChangeEventType.ADD, null));
    }

    /**
     * returns the list with all the groups for the user and the last message from that group
     * @param idUser
     * @return
     */
    public List<GroupDTO> seeMessages(String idUser){
        List<GroupDTO> messageList = new ArrayList<>();
        Iterable<GroupUser> temp = belongDatabase.findAll();
        //System.out.println(temp);
        List<Long> groupList = StreamSupport.stream( belongDatabase.findAll(idUser).spliterator(), false)
                .map(groupUser -> groupUser.getId().getRight())
                .collect(Collectors.toList());
        int i = 1;
        for(Long idGroup : groupList){
            Message lastMessage = StreamSupport.stream(messageDatabase.findAll(idGroup).spliterator(), false)
                    .max(Comparator.comparing(Message::getTimeStamp))
                    .orElseThrow();
            User senderLastMessage = userDatabase.findOne(lastMessage.getSender());
            Group group = groupDatabase.findOne(idGroup);
            messageList.add(new GroupDTO(i++, idGroup, group.getName(),senderLastMessage.getName() , lastMessage.getText()));
        }
        return messageList;
    }

    /**
     * returns all the messages from a group
     * @param idGroup
     * @return
     */
    public Iterable<MessageDTO> seeGroupMessages(Long idGroup){
        return StreamSupport.stream(messageDatabase.findAll(idGroup).spliterator(), false)
                .map(message -> new MessageDTO(userDatabase.findOne(message.getSender()).getName(), message.getText(), message.getTimeStamp()))
                .collect(Collectors.toList());
    }

    /**
     * adds a message to an existing group
     * @param idGroup
     * @param idUser
     * @param message
     */
    public void addMessageToGroup(Long idGroup, String idUser, String message){
        messageDatabase.save(new Message(idUser, idGroup, message));
        notifyObservers(new MessageChangeEvent(ChangeEventType.ADD, null));
    }

    /**
     * creates a new group and sends a message
     * @param idSender who creates the group
     * @param idReceviers group participants
     * @param groupName
     * @param groupDescription
     * @param text mesage
     */
    public void sendMessageGroup(String idSender, List<String> idReceviers, String groupName, String groupDescription, String text){
        //create new group
        Group group = groupDatabase.save(new Group(groupName, groupDescription));
        Long idGroup = group.getId();
        //add participants
        belongDatabase.save(new GroupUser(idSender, idGroup));
        for(String id : idReceviers)
            belongDatabase.save(new GroupUser(id, idGroup));
        //send message
        addMessageToGroup(idGroup, idSender, text);
        notifyObservers(new MessageChangeEvent(ChangeEventType.ADD, null));
    }

    public List<MessageDTO> getMessagesReceivedByUser(String user){
         List<MessageDTO> messages = new ArrayList<>();
         List<Long> groups = seeMessages(user).stream().map(e->e.getGroupID()).collect(Collectors.toList());
         Iterable<Message> allMessages = messageDatabase.findAll();
         messages = StreamSupport.stream(allMessages.spliterator(), false)
         .filter(e-> groups.contains(e.getGroupId()) && e.getSender()!=user)
         .map(e-> new MessageDTO(userDatabase.findOne(e.getSender()).getName(), e.getText(), e.getTimeStamp()))
         .collect(Collectors.toList());
         return messages;
    }

    private List<Observer<MessageChangeEvent>> observers = new ArrayList<>();
    @Override
    public void addObserver(Observer<MessageChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<MessageChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(MessageChangeEvent t) {
        observers.forEach(e->e.update(t));
    }
}
