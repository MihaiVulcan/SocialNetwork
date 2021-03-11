package socialnetwork.ui.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.service.*;

import java.io.IOException;

public class SignUpController {
    Stage window;
    public void setWindow(Stage window) {
        this.window = window;
    }

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

    @FXML
    public void initialize(){
        lblEmail.setVisible(false);
        lblName.setVisible(false);
        lblPassword.setVisible(false);

        txtEmail.textProperty().addListener((obs, oldText, newText)->checkEmptyEmail());
        txtName.textProperty().addListener((obs, oldText, newText)->checkEmptyName());
        txtPassword.textProperty().addListener((obs, oldText, newText)->checkEmptyPassword());
        txtPassword1.textProperty().addListener((obs, oldText, newText)->checkEmptyPassword1());
    }

    @FXML
    TextField txtEmail;
    @FXML
    TextField txtName;
    @FXML
    TextField txtBreed;
    @FXML
    TextField txtFood;
    @FXML
    PasswordField txtPassword;
    @FXML
    PasswordField txtPassword1;
    @FXML
    Label lblEmail;
    @FXML
    Label lblName;
    @FXML
    Label lblPassword;
    @FXML
    Label lblPassword1;


    @FXML
    public void handleSignUp(){
        String email = txtEmail.getText().toLowerCase().trim();
        String name = txtName.getText().trim();
        String breed = txtBreed.getText().trim();
        String food = txtFood.getText().trim();
        String password = txtPassword.getText();
        String password1 = txtPassword1.getText();

        if(email.isEmpty() || name.isEmpty() || password.isEmpty() || password1.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.getDialogPane().setContent(new Pane(new Label(new String("Email, Name and Password fields cannot be empty "))));
            alert.showAndWait();
        }
        else if(!password.equals(password1)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.getDialogPane().setContent(new Pane(new Label(new String("Passwords do not match"))));
            alert.showAndWait();
        }
        else
            {
            try{
                User user = userService.addUser(email, name, breed, food, password);
                if(user == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.getDialogPane().setContent(new Pane(new Label(new String("New user added"))));
                    alert.showAndWait();
                    handleBack();
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.getDialogPane().setContent(new Pane(new Label(new String("Existing user"))));
                    alert.showAndWait();
                }
            }catch(ValidationException e){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.getDialogPane().setContent(new ScrollPane(new Label(e.getMessage())));
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void checkEmptyEmail(){
        if(txtEmail.getText().isEmpty())
            lblEmail.setVisible(true);
        else
            lblEmail.setVisible(false);
    }

    @FXML
    public void checkEmptyName(){
        if(txtName.getText().isEmpty())
            lblName.setVisible(true);
        else
            lblName.setVisible(false);
    }

    @FXML
    public void checkEmptyPassword(){
        if(txtPassword.getText().isEmpty())
            lblPassword.setVisible(true);
        else
            lblPassword.setVisible(false);
    }

    @FXML
    public void checkEmptyPassword1(){
        if(txtPassword1.getText().isEmpty())
            lblPassword1.setVisible(true);
        else
            lblPassword1.setVisible(false);
    }

    @FXML
    public void handleBack() {
        try {
            FXMLLoader messageLoader = new FXMLLoader();
            messageLoader.setLocation(getClass().getResource("/views/login.fxml"));
            AnchorPane login = messageLoader.load();
            LoginController loginController = messageLoader.getController();
            loginController.setWindow(window);
            loginController.setServices(userService, friendshipService, requestService, messageService, eventService);
            window.setScene(new Scene(login));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
