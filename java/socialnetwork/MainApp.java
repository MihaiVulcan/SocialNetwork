package socialnetwork;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import socialnetwork.domain.*;
import socialnetwork.domain.validators.*;
import socialnetwork.repository.Repository;
import socialnetwork.repository.database.*;
import socialnetwork.service.*;
import socialnetwork.ui.gui.LoginController;

import java.io.IOException;

public class MainApp extends Application {

    Repository<String, User> userDatabase;
    UserService userService;
    Repository<Tuple<String, String>, Friendship> friendshipDatabase;
    FriendshipService friendshipService;
    Repository<Tuple<String, String>, FriendshipRequest> friendshipRequestDatabase;
    FriendshipRequestService friendshipRequestService;
    Repository<Long, Group> groupDatabase;
    GroupUserDatabaseRepository groupUserDatabase;
    MessageDatabaseRepository messageDatabase;
    MessageManagementService messageManagementService;
    PasswordManager passwordManager;
    Repository<Long, Event> eventRepository;
    Repository<Tuple<String, Long>, EventUser> participateRepository;
    EventService eventService;
    Repository<Tuple<Tuple<String, Long>, TimeNotification>, Notification> notificationRepository;

    FriendshipDatabaseRepository frrepo = new FriendshipDatabaseRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres", new FriendshipValidator());


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        passwordManager = new PasswordManager("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres");
        userDatabase = new UserDatabaseRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres", new UserValidator());
        userService = new UserService(userDatabase, passwordManager);
        friendshipDatabase = new FriendshipDatabaseRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres", new FriendshipValidator());
        friendshipService = new FriendshipService(frrepo, userDatabase);

        friendshipRequestDatabase = new FriendshipRequestDatabaseRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres", new FriendshipRequestValidator());
        friendshipRequestService = new FriendshipRequestService(friendshipRequestDatabase, friendshipDatabase, userDatabase);

        groupDatabase = new GroupDatabaseRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres", new GroupValidator());
        groupUserDatabase = new GroupUserDatabaseRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres", new GroupUserValidator());
        messageDatabase = new MessageDatabaseRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres", new MessageValidator());
        messageManagementService = new MessageManagementService(userDatabase, groupDatabase, groupUserDatabase, messageDatabase);

        eventRepository = new EventDatabaseRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres", new EventValidator());
        participateRepository = new EventUserDatabaseRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres", new EventUserValidator());
        notificationRepository = new NotificationsDatabaseRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres", new NotificationValidator());
        eventService = new EventService(userDatabase, eventRepository, participateRepository, notificationRepository);

        initView(primaryStage);
        primaryStage.setWidth(600);
        primaryStage.show();

    }

    private void initView(Stage primaryStage) throws IOException {

        FXMLLoader messageLoader = new FXMLLoader();
        messageLoader.setLocation(getClass().getResource("/views/login.fxml"));
        AnchorPane login = messageLoader.load();
        primaryStage.setScene(new Scene(login));

        LoginController loginController = messageLoader.getController();
        loginController.setWindow(primaryStage);
        loginController.setServices(userService, friendshipService, friendshipRequestService, messageManagementService, eventService);

    }

}
