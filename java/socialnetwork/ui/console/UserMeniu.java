//package socialnetwork.ui.console;
//
//import socialnetwork.service.FriendshipRequestService;
//import socialnetwork.service.FriendshipService;
//import socialnetwork.service.MessageManagementService;
//import socialnetwork.service.UserService;
//
//import java.util.InputMismatchException;
//import java.util.Scanner;
//
//public class UserMeniu {
//    private Long id;
//    private UserService userService;
//    private FriendshipService friendshipService;
//    private FriendshipRequestService friendshipRequestService;
//    private MessageManagementService messageService;
//    private Scanner scanner = new Scanner(System.in);
//
//
//    public UserMeniu(MessageManagementService messageService, UserService userService, FriendshipService friendshipService, FriendshipRequestService friendshipRequestService, Long id) {
//        this.id = id;
//        this.userService = userService;
//        this.friendshipService = friendshipService;
//        this.friendshipRequestService = friendshipRequestService;
//        this.messageService = messageService;
//    }
//
//    private void meniu() {
//        System.out.println("11.Message Meniu");
//        System.out.println("1.View Friends List");
//        System.out.println("2.View Friends List from a month");
//        System.out.println("3.Add friend");
//        System.out.println("4.View Friend Requests");
//        System.out.println("5.Remove friend");
//        System.out.println("6.Delete account");
//        System.out.println("7.Log out");
//    }
//
//    public void run() {
//        meniu();
//        System.out.print(">>>");
//        String cmd = scanner.nextLine().trim();
//        while(!cmd.equals("7")){
//            switch(cmd){
//                case "11": messageMeniu();
//                    break;
//                case "1": viewFriends();
//                    break;
//                case "2": viewFriendsMonth();
//                    break;
//                case "3":addFriendRequest();
//                    break;
//                case "4":viewFriendRequests();
//                    break;
//                case "5":removeFriend();
//                    break;
//                case "6":deleteAccount();
//                    return;
//                default:
//                    System.out.println("Invalid command. Try again");
//            }
//            if(!cmd.equals("11")){
//                System.out.println("\nPress a enter to continue");
//                scanner.nextLine();
//            }
//            meniu();
//            System.out.print(">>>");
//            cmd = scanner.nextLine().trim();
//        }
//
//    }
//
//    private void messageMeniu() {
//        MessageMeniu messageMeniu = new MessageMeniu(id, userService, messageService);
//        messageMeniu.run();
//    }
//
//    private void viewFriendsMonth() {
//        System.out.print("Enter a month(from 1 to 12): ");
//        try{
//            int month = scanner.nextInt();
//            scanner.nextLine();
//            friendshipService.getFriendsByMonth(id, month).forEach(System.out::println);
//        }catch (InputMismatchException e){
//            System.out.println("Month is not valid");;
//        }
//    }
//
//    private void viewFriends() {
//        Iterable f = friendshipService.getFriends(id);
//        if(f.spliterator().getExactSizeIfKnown()!=0)
//            f.forEach(System.out::println);
//        else
//            System.out.println("You have no friends added yet ");
//    }
//
//    private void viewFriendRequests() {
//        friendshipRequestService.getUserRequests(id).forEach(System.out::println);
//        System.out.println("If you want to accept or decline a reques type useremail_accept/decline");
//        String[] input = scanner.nextLine().trim().split(" ");
//        String email = input[0];
//        String message = input [1];
//        Long idSender = userService.getIdbyEmail(email);
//        if(idSender == -1)
//            System.out.println("Email does not exist");
//        else {
//            switch (message) {
//                case "accept":
//                    if (friendshipRequestService.acceptRequest(idSender, id) == null)
//                        System.out.println("Request doesn't exist");
//                    else
//                        System.out.println("Request accepted");
//                    break;
//                case "decline":
//                    if (friendshipRequestService.declineRequest(idSender, id) == null)
//                        System.out.println("Request doesn't exist");
//                    else
//                        System.out.println("Request declined");
//                    break;
//                default:
//                    System.out.println("Invalid command");
//            }
//        }
//    }
//
//    private void deleteAccount() {
//        userService.delete(id);
//    }
//
//    private void addFriendRequest() {
//        System.out.print("Enter your friends email: ");
//        String email = scanner.nextLine().trim().toLowerCase();
//        Long idFriend = userService.getIdbyEmail(email);
//        if (idFriend == -1)
//            System.out.println("No account is registered with this email");
//        else {
//            if(friendshipRequestService.addRequest(id, idFriend)==null)
//                System.out.println("Request was sent");
//            else
//                System.out.println("You are already friends");
//        }
//    }
//
//    private void removeFriend() {
//        System.out.print("Enter your friends email: ");
//        String email = scanner.nextLine().trim().toLowerCase();
//        Long idFriend = userService.getIdbyEmail(email);
//        if (idFriend == -1)
//            System.out.println("No account is registered with this email");
//        else {
//            friendshipService.deleteFriendship(id, idFriend);
//            System.out.println("Friend was deleted");
//        }
//    }
//}
