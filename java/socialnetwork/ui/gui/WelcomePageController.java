package socialnetwork.ui.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.service.*;

import java.io.IOException;
import java.util.Set;

public class WelcomePageController {
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
        lblWelcome.setText("Welcome back " + user.getName());
    }
    @FXML
    Label lblWelcome;

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
}
