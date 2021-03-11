package socialnetwork.service;


import socialnetwork.domain.Friendship;
import socialnetwork.domain.FriendshipRequest;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.repository.Repository;
import socialnetwork.service.datatrasfer.FriendshipDTO;
import socialnetwork.utils.events.ChangeEventType;
import socialnetwork.utils.events.FriendshipChangeEvent;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendshipRequestService implements Observable<FriendshipChangeEvent> {
    private Repository<Tuple<String, String>, FriendshipRequest> friendshipRequestDatabase;
    private Repository<Tuple<String, String>, Friendship> friendshipDatabase;
    private Repository<String, User> userDatabase;

    public FriendshipRequestService(Repository<Tuple<String, String>, FriendshipRequest> friendshipRequestDatabase, Repository<Tuple<String, String>, Friendship> friendshipDatabase, Repository<String, User> userDatabase) {
        this.friendshipRequestDatabase = friendshipRequestDatabase;
        this.friendshipDatabase = friendshipDatabase;
        this.userDatabase = userDatabase;
    }

    /**
     * adds a new friendship request
     * @param sender
     * @param reciver
     * @return null if friendship request is send, or friendship or friendship request if it already exists
     */
    public FriendshipRequest addRequest(String sender, String reciver){
        Friendship temp = friendshipDatabase.findOne(new Tuple(sender, reciver));
        if(temp == null){
            FriendshipRequest friendshipRequest = new FriendshipRequest(LocalDateTime.now(),sender, reciver);
            FriendshipRequest sent = friendshipRequestDatabase.save(friendshipRequest);
            notifyObservers(new FriendshipChangeEvent(ChangeEventType.ADD, null));
            return sent;
        }

        return new FriendshipRequest(LocalDateTime.now(),sender, reciver);
    }

    /**
     * accepts a friend request
     * @param sender
     * @param reciver
     * @return null if request doesn't exist or the accepted friendship request
     */
    public FriendshipRequest acceptRequest(String sender, String reciver) {
        FriendshipRequest temp = friendshipRequestDatabase.delete(new Tuple(sender, reciver));
        if (temp != null) {
            notifyObservers(new FriendshipChangeEvent(ChangeEventType.DELETE, null));
            Friendship friendship = new Friendship(LocalDateTime.now(), sender, reciver);
            friendshipDatabase.save(friendship);
            notifyObservers(new FriendshipChangeEvent(ChangeEventType.ADD, null));
        }
        return temp;
    }

    /**
     * deletes a friendship request
     * @param sender
     * @param reciver
     * @return null if friendship request doesn't exist or the deleted friendship request
     */
    public FriendshipRequest declineRequest(String sender, String reciver){
        FriendshipRequest friendshipRequest = friendshipRequestDatabase.delete(new Tuple(sender, reciver));
        if(friendshipRequest != null)
            notifyObservers(new FriendshipChangeEvent(ChangeEventType.DELETE, null));
        return friendshipRequest;
    }

    /**
     * returns the list of friend requests
     * @param id
     * @return list of friend requests
     */
    public Iterable<FriendshipDTO> getUserRequests(String id){
        return  StreamSupport.stream(friendshipRequestDatabase.findAll().spliterator(), false)
                .filter(friendshipRequest -> friendshipRequest.getId().getRight().equals(id))//numai daca este in dreapta este reciever
                .map(fr -> new FriendshipDTO(userDatabase.findOne(fr.getId().getLeft()).getEmail(), userDatabase.findOne(fr.getId().getLeft()).getName(), fr.getDateTime()))
                .collect(Collectors.toList());
    }

    public Iterable<FriendshipDTO> getUserSentRequests(String id){
        return  StreamSupport.stream(friendshipRequestDatabase.findAll().spliterator(), false)
                .filter(friendshipRequest -> friendshipRequest.getId().getLeft().equals(id))//numai daca este in dreapta este reciever
                .map(fr -> new FriendshipDTO(userDatabase.findOne(fr.getId().getRight()).getEmail(), userDatabase.findOne(fr.getId().getRight()).getName(), fr.getDateTime()))
                .collect(Collectors.toList());
    }

    private List<Observer<FriendshipChangeEvent>> observers = new ArrayList<>();
    @Override
    public void addObserver(Observer<FriendshipChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<FriendshipChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(FriendshipChangeEvent t) {
        observers.forEach(x->x.update(t));
    }
}
