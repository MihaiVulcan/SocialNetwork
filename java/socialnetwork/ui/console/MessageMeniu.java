//package socialnetwork.ui.console;
//
//import socialnetwork.service.MessageManagementService;
//import socialnetwork.service.UserService;
//import socialnetwork.service.datatrasfer.GroupDTO;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//
//public class MessageMeniu {
//    private Long id;
//    private UserService userService;
//    private MessageManagementService messageService;
//    private Scanner scanner = new Scanner(System.in);
//
//    public MessageMeniu(Long id, UserService userService, MessageManagementService messageService) {
//        this.id = id;
//        this.userService = userService;
//        this.messageService = messageService;
//    }
//
//    private void meniu() {
//        System.out.println("1. See existing messages and reply to them");
//        System.out.println("2. Send a private message");
//        System.out.println("3. Create a group and send a message");
//        System.out.println("4. Back");
//    }
//
//    public void run(){
//        meniu();
//        System.out.println(">>>");
//        String cmd = scanner.nextLine().trim();
//        while(!cmd.equals("4")){
//            switch (cmd){
//                case "1": seeMessages();
//                    break;
//                case "2": sendPrivateMessage();
//                    break;
//                case "3": sendGroupMessage();
//                    break;
//                default:
//                    System.out.println("Invalid command");
//            }
//            meniu();
//            System.out.println(">>>");
//            cmd = scanner.nextLine().trim();
//        }
//
//    }
//
//    private void seeMessages() {
//        List<GroupDTO> lastMessageList =  messageService.seeMessages(id);
//        lastMessageList.forEach(System.out::println);
//        System.out.print("Type the index of the group or back: ");
//        String input = scanner.nextLine().trim();
//        if(input.equals("back"))
//            return;
//        else
//            try{
//                int index = Integer.parseInt(input)-1;
//                if(index>lastMessageList.size() || index < 0)
//                    System.out.println("Enter a valid number");
//                else {
//                    messageService.seeGroupMessages(lastMessageList.get(index).getGroupID()).forEach(System.out::println);
//                    System.out.println("Type replay or back");
//                    input = scanner.nextLine().trim().toLowerCase();
//                    if(input.equals("back")){
//                        return;
//                    }
//                    else{
//                        System.out.println("Type your message");
//                        String message = scanner.nextLine().trim();
//                        messageService.addMessageToGroup(lastMessageList.get(index).getGroupID(), id, message);
//                    }
//                }
//            }catch (Exception e){
//                System.out.println("Invalid command");
//            }
//
//    }
//
//    private void sendPrivateMessage(){
//        System.out.print("(Enter Email)To: ");
//        String reciver = scanner.nextLine().trim();
//        Long idReciver = userService.getIdbyEmail(reciver);
//        if(idReciver == -1)
//            System.out.println("email does not exists");
//        else {
//            System.out.println("Message:");
//            String text = scanner.nextLine().trim();
//            messageService.sendPrivateMessage(id, idReciver, text);
//        }
//
//
//    }
//
//    private void sendGroupMessage(){
//        System.out.print("Enter Group Name: ");
//        String groupName = scanner.nextLine().trim();
//
//        System.out.println("Enter Group Description: ");
//        String groupDescription = scanner.nextLine().trim();
//
//        System.out.println("Enter the participants(emails), press enter after each participant");
//        System.out.println("After you finished adding participants type finish");
//        List<Long> ids = new ArrayList<>();
//        String input = null;
//        do{
//            System.out.print("Email: ");
//            input = scanner.nextLine().trim().toLowerCase();
//            if(!input.equals("finish")){
//                Long idUser = userService.getIdbyEmail(input);
//                if(idUser == -1)
//                    System.out.println("Email not registered");
//                else{
//                    ids.add(idUser);
//                }
//            }
//        }while(!input.equals("finish"));
//
//        System.out.println("Message: ");
//        String message = scanner.nextLine().trim();
//
//        messageService.sendMessageGroup(id, ids, groupName, groupDescription, message);
//    }
//
//}
