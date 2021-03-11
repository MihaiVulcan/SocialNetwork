//package socialnetwork.ui.console;
//
//import socialnetwork.service.FriendshipRequestService;
//import socialnetwork.service.FriendshipService;
//import socialnetwork.service.MessageManagementService;
//import socialnetwork.service.UserService;
//
//import java.util.Scanner;
//
//public class LogInMeniu {
//    private UserService userService;
//    private FriendshipService friendshipService;
//    private FriendshipRequestService friendshipRequestService;
//    private MessageManagementService messageService;
//
//    public LogInMeniu(MessageManagementService messageService, UserService userService, FriendshipService friendshipService, FriendshipRequestService friendshipRequestService) {
//        this.userService = userService;
//        this.friendshipService = friendshipService;
//        this.friendshipRequestService = friendshipRequestService;
//        this.messageService = messageService;
//    }
//
//    private Scanner scanner = new Scanner(System.in);
//
//    private void meniu(){
//        System.out.println("1.Log in");
//        System.out.println("2.Sign up your awesome pet");
//        System.out.println("3.Exit");
//    }
//
//    public void run(){
//        meniu();
//        System.out.print(">>>");
//        String cmd = scanner.nextLine().trim();
//        while(!cmd.equals("3")) {
//            switch (cmd) {
//                case "1":
//                    logIn();
//                    break;
//                case "2":
//                    signUp();
//                    break;
//                default:
//                    System.out.println("Invalid Command");
//                    break;
//            }
//            meniu();
//            System.out.print(">>>");
//            cmd = scanner.nextLine().trim();
//        }
//    }
//
//    private void logIn() {
//        System.out.println("Enter your email: ");
//        String email = scanner.nextLine().trim();
//        Long id = userService.getIdbyEmail(email);
//        if(id == -1)
//            System.out.println("No account for this email is registered");
//        else{
//            UserMeniu userMeniu = new UserMeniu(messageService, userService, friendshipService, friendshipRequestService, id);
//            userMeniu.run();
//        }
//    }
//
//    private void signUp(){
//        System.out.print("Pet's name: ");
//        String name = scanner.nextLine().trim();
//
//        System.out.print("Email: ");
//        String email = scanner.nextLine().trim().toLowerCase();
//
//        System.out.print("Breed: ");
//        String breed = scanner.nextLine().trim();
//
//        System.out.print("Favourite food: ");
//        String favefood = scanner.nextLine().trim();
//
//        if(userService.addUser(email, name, breed, favefood)==null)
//            System.out.println("Your pet's " + name + " account was created");
//        else
//            System.out.println("There is another account registered with this email");
//    }
//}
