package socialnetwork.ui.gui;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import socialnetwork.notification.Notificator;
import socialnetwork.service.*;

import java.io.IOException;

public class LoginController {

    UserService userService;
    FriendshipService friendshipService;
    FriendshipRequestService requestService;
    MessageManagementService messageService;
    EventService eventService;
    public void setServices(UserService userService, FriendshipService friendshipService, FriendshipRequestService requestService, MessageManagementService messageService, EventService eventService){
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.requestService = requestService;
        this.messageService = messageService;
        this.eventService = eventService;
    }

    Stage window;
    public void setWindow(Stage window){
        this.window = window;
    }

    @FXML
    private TextField textEmail;
    @FXML
    private PasswordField textPassword;
    @FXML
    private Button btLogin;
    @FXML
    private Button btSignup;
    @FXML
    private void initialize(){
        textEmail.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER)
                    handleLogin();
            }
        });
        textPassword.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER)
                    handleLogin();
            }
        });
    }

    @FXML
    private void handleLogin(){
        String email = textEmail.getText().trim().toLowerCase();
        String password = textPassword.getText();
        if(userService.logIn(email, password)){
            try{
                FXMLLoader tabPaneLoader = new FXMLLoader();
                tabPaneLoader.setLocation(getClass().getResource("/views/tabpane.fxml"));
                AnchorPane tabpane = tabPaneLoader.load();
                window.setScene(new Scene(tabpane));

                TabpaneController tabpaneController = tabPaneLoader.getController();
                tabpaneController.setWindow(window);
                tabpaneController.setServices(userService.getOne(email), userService, friendshipService, requestService, messageService, eventService);
            }catch (IOException e){
                e.printStackTrace();
            }
            new Notificator(email, eventService, 2).start();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeight(200);
            alert.setWidth(200);
            alert.getDialogPane().setContent(new ScrollPane(new Label(new String("Wrong email or password"))));
            alert.showAndWait();
        }
    }

    @FXML
    private void handleSignUp() throws IOException {
        FXMLLoader messageLoader = new FXMLLoader();
        messageLoader.setLocation(getClass().getResource("/views/signUp.fxml"));
        AnchorPane login = messageLoader.load();
        SignUpController signUpController = messageLoader.getController();
        signUpController.setServices(userService, friendshipService, requestService, messageService, eventService);
        signUpController.setWindow(window);
        window.setScene(new Scene(login));
    }


}
