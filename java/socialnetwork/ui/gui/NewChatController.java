package socialnetwork.ui.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.service.*;

import java.util.ArrayList;
import java.util.List;

public class NewChatController {
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
    List<String> chatMembers = new ArrayList<>();
    public void setServices(String user, UserService userService, FriendshipService friendshipService, FriendshipRequestService requestService, MessageManagementService messageService, EventService eventService){
        this.user = user;
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.requestService = requestService;
        this.messageService = messageService;
        this.eventService = eventService;
        loadUsers();
    }

    class UserCell extends ListCell<User>{
        String id;
        Pane pane = new Pane();
        HBox hBox = new HBox();
        Label name = new Label();
        Label breed = new Label();
        Button btAdd = new Button("add");
        Button btDel = new Button("remove");

        public UserCell(){
            hBox.setSpacing(10);
            btDel.setDisable(true);
            btAdd.setOnMouseClicked(e->{
                chatMembers.add(id);
                btAdd.setDisable(true);
                btDel.setDisable(false);
            });
            btDel.setOnMouseClicked(e->{
                chatMembers.remove(id);
                btDel.setDisable(true);
                btAdd.setDisable(false);
            });
            hBox.getChildren().addAll(name, breed, btAdd, btDel);
            hBox.setHgrow(pane, Priority.ALWAYS);
        }

        public void updateItem(User user, boolean empty){
            super.updateItem(user, empty);
            setText(null);
            setGraphic(null);
            if(user!=null && !empty){
                id=user.getId();
                name.setText(user.getName());
                breed.setText(user.getBreed());
                setGraphic(hBox);
            }
        }
    }

    @FXML private ListView lstUsers;
    @FXML private TextField txtMessage;
    @FXML private TextField txtGroup;

    @FXML
    public void initialize(){
        lstUsers.setCellFactory(e -> new UserCell());
    }

    public void createNewChat(){
        if(chatMembers.size() < 1){
            //TODO eroare
        }
        else{
            if(txtGroup.getText().isEmpty()){
                //TODO eroare
            }
            else
                messageService.sendMessageGroup(user, chatMembers, txtGroup.getText(), "", txtMessage.getText());
        }
        window.close();
    }

    private void loadUsers(){
        ObservableList<User> list = FXCollections.observableArrayList();
        userService.getAll().forEach(e->{
            if(!e.getEmail().equals(user))
                list.add(e);

        });
        lstUsers.getItems().clear();
        lstUsers.getItems().addAll(list);
    }
}
