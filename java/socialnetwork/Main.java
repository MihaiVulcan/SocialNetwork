package socialnetwork;


public class Main {
    public static void main(String[] args) {
//        UserDatabase userDatabase = new UserDatabase("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres", new UserValidator());
//        UserService userService = new UserService(userDatabase);
//
//        FriendshipDatabase friendshipDatabase = new FriendshipDatabase("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres", new FriendshipValidator());
//        FriendshipService friendshipService = new FriendshipService(friendshipDatabase, userDatabase);
//
//        FriendshipRequestDatabase friendshipRequestDatabase = new FriendshipRequestDatabase("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres", new FriendshipRequestValidator());
//        FriendshipRequestService friendshipRequestService = new FriendshipRequestService(friendshipRequestDatabase, friendshipDatabase, userDatabase);
//
//        GroupDatabase groupDatabase = new GroupDatabase("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres", new GroupValidator());
//        GroupUserDatabase groupUserDatabase = new GroupUserDatabase("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres", new GroupUserValidator());
//        MessageDatabase messageDatabase = new MessageDatabase("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres", new MessageValidator());
//
//        MessageManagementService messageManagementService = new MessageManagementService(userDatabase, groupDatabase, groupUserDatabase, messageDatabase);
//
//        LogInMeniu logInMeniu = new LogInMeniu(messageManagementService, userService, friendshipService, friendshipRequestService);
//        logInMeniu.run();

        MainApp.main(args);
    }
}
