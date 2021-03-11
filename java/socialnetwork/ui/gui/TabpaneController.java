package socialnetwork.ui.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.service.*;

import java.io.IOException;

public class TabpaneController {

    Stage window;
    public void setWindow(Stage window) {
        this.window = window;
    }
    UserService userService;
    FriendshipService friendshipService;
    FriendshipRequestService requestService;
    MessageManagementService messageService;
    EventService eventService;
    User user;
    public void setServices(User user, UserService userService, FriendshipService friendshipService, FriendshipRequestService requestService, MessageManagementService messageService, EventService eventService){
        this.user = user;
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.requestService = requestService;
        this.messageService = messageService;
        this.eventService = eventService;
        load();
    }

    @FXML
    Tab tabMessages;
    @FXML
    Tab tabFriends;
    @FXML
    Tab tabFriendRequests;
    @FXML
    Tab tabEvents;
    @FXML
    Tab tabReports;
    @FXML
    Tab tabWelcome;

    public void load() {
        try{
            FXMLLoader messageLoader = new FXMLLoader();
            messageLoader.setLocation(getClass().getResource("/views/welcomePage.fxml"));
            AnchorPane welcome = messageLoader.load();
            WelcomePageController welcomePageController = messageLoader.getController();
            welcomePageController.setServices(user, userService, friendshipService, requestService, messageService, eventService);
            welcomePageController.setWindow(window);
            tabWelcome.setContent(welcome);
        }catch(IOException e){
            e.printStackTrace();
        }

        try {
            FXMLLoader messageLoader = new FXMLLoader();
            messageLoader.setLocation(getClass().getResource("/views/friendList.fxml"));
            AnchorPane friends = messageLoader.load();
            FriendListController friendListController = messageLoader.getController();
            friendListController.setServices(user.getEmail(), userService, friendshipService, requestService, messageService, eventService);
            friendListController.setWindow(window);
            tabFriends.setContent(friends);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            FXMLLoader messageLoader = new FXMLLoader();
            messageLoader.setLocation(getClass().getResource("/views/messages.fxml"));
            AnchorPane messages = messageLoader.load();
            MessagesController messagesController = messageLoader.getController();
            messagesController.setServices(user.getEmail(), userService, friendshipService, requestService, messageService, eventService);
            messagesController.setWindow(window);
            tabMessages.setContent(messages);
        }catch (IOException e){
            e.printStackTrace();
        }

        try{
            FXMLLoader messageLoader = new FXMLLoader();
            messageLoader.setLocation(getClass().getResource("/views/activityReports.fxml"));
            AnchorPane activity = messageLoader.load();
            ActivityReportsController activityReportsController = messageLoader.getController();
            activityReportsController.setServices(user.getEmail(), userService, friendshipService, requestService, messageService, eventService);
            activityReportsController.setWindow(window);
            tabReports.setContent(activity);
        }catch (IOException e){
            e.printStackTrace();
        }


        try{
            FXMLLoader messageLoader = new FXMLLoader();
            messageLoader.setLocation(getClass().getResource("/views/events.fxml"));
            AnchorPane events = messageLoader.load();
            EventsController eventsController = messageLoader.getController();
            eventsController.setServices(user.getEmail(), userService, friendshipService, requestService, messageService, eventService);
            eventsController.setWindow(window);
            tabEvents.setContent(events);
        }catch (IOException e){
            e.printStackTrace();
        }

        try{
            FXMLLoader messageLoader = new FXMLLoader();
            messageLoader.setLocation(getClass().getResource("/views/friendRequest.fxml"));
            AnchorPane request = messageLoader.load();
            FriendRequestController friendRequestController = messageLoader.getController();
            friendRequestController.setServices(user.getEmail(), userService, friendshipService, requestService, messageService, eventService);
            friendRequestController.setWindow(window);
            tabFriendRequests.setContent(request);
        }catch(IOException e){
            e.printStackTrace();
        }



    }
}
