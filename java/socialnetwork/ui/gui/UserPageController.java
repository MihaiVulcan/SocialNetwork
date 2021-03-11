package socialnetwork.ui.gui;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import socialnetwork.service.*;

import java.io.IOException;
import java.util.Set;

public class UserPageController {

    Stage window;
    public void setWindow(Stage window) {
        this.window = window;
    }
    UserService userService;
    FriendshipService friendshipService;
    FriendshipRequestService requestService;
    MessageManagementService messageService;
    EventService eventService;
    String user;
    public void setServices(String user, UserService userService, FriendshipService friendshipService, FriendshipRequestService requestService, MessageManagementService messageService, EventService eventService){
        this.user = user;
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.requestService = requestService;
        this.messageService = messageService;
        this.eventService = eventService;
    }

    @FXML
    private Button btLogout;
    @FXML
    private Button btMessages;
    @FXML
    private Button btFriends;
    @FXML
    private Button btFriendRequsts;
    @FXML
    private Button btEvents;
    @FXML
    private Button btActivity;

    @FXML
    public void initialize(){
        btLogout.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER){
                    handleLogout();
                }
            }
        });
        btFriends.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER){
                    handleFriends();
                }
            }
        });
        btFriendRequsts.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER){
                    handleRequests();
                }
            }
        });
        btActivity.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER){
                    handleReports();
                }
            }
        });
        btMessages.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER){
                    handleMessages();
                }
            }
        });
        btEvents.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER){
                    handleEvents();
                }
            }
        });

    }

    @FXML
    private void handleLogout(){
        try{
            FXMLLoader messageLoader = new FXMLLoader();
            messageLoader.setLocation(getClass().getResource("/views/login.fxml"));
            AnchorPane login = messageLoader.load();
            window.setScene(new Scene(login));

            LoginController loginController = messageLoader.getController();
            loginController.setWindow(window);
            loginController.setServices(userService, friendshipService, requestService, messageService, eventService);
        }catch(IOException e){
            e.printStackTrace();
        }
        Set<Thread> threads = Thread.getAllStackTraces().keySet();
        threads.forEach(t->{
            if(t.getName().equals("Notificator"))
                t.interrupt();
        });

    }

    @FXML
    private void handleFriends(){
        try{
            FXMLLoader messageLoader = new FXMLLoader();
            messageLoader.setLocation(getClass().getResource("/views/friendList.fxml"));
            AnchorPane login = messageLoader.load();
            FriendListController friendListController = messageLoader.getController();
            friendListController.setServices(user, userService, friendshipService, requestService, messageService, eventService);
            friendListController.setWindow(window);
            window.setScene(new Scene(login));
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @FXML
    private void handleRequests() {
        try{
            FXMLLoader messageLoader = new FXMLLoader();
            messageLoader.setLocation(getClass().getResource("/views/friendRequest.fxml"));
            AnchorPane login = messageLoader.load();
            FriendRequestController friendRequestController = messageLoader.getController();
            friendRequestController.setServices(user, userService, friendshipService, requestService, messageService, eventService);
            friendRequestController.setWindow(window);
            window.setScene(new Scene(login));
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    @FXML
    private void handleMessages() {
        try{
            FXMLLoader messageLoader = new FXMLLoader();
            messageLoader.setLocation(getClass().getResource("/views/messages.fxml"));
            AnchorPane login = messageLoader.load();
            MessagesController messagesController = messageLoader.getController();
            messagesController.setServices(user, userService, friendshipService, requestService, messageService, eventService);
            messagesController.setWindow(window);
            window.setScene(new Scene(login));
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @FXML
    public void handleReports() {
        try{
            FXMLLoader messageLoader = new FXMLLoader();
            messageLoader.setLocation(getClass().getResource("/views/activityReports.fxml"));
            AnchorPane activity = messageLoader.load();
            ActivityReportsController activityReportsController = messageLoader.getController();
            activityReportsController.setServices(user, userService, friendshipService, requestService, messageService, eventService);
            activityReportsController.setWindow(window);
            window.setWidth(333);
            window.setScene(new Scene(activity));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleEvents() {
        try{
            FXMLLoader messageLoader = new FXMLLoader();
            messageLoader.setLocation(getClass().getResource("/views/events.fxml"));
            AnchorPane events = messageLoader.load();
            EventsController eventsController = messageLoader.getController();
            eventsController.setServices(user, userService, friendshipService, requestService, messageService, eventService);
            eventsController.setWindow(window);
            window.setScene(new Scene(events));
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}