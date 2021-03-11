package socialnetwork.ui.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import socialnetwork.service.*;
import socialnetwork.service.datatrasfer.GroupDTO;
import socialnetwork.service.datatrasfer.MessageDTO;
import socialnetwork.utils.events.MessageChangeEvent;
import socialnetwork.utils.observer.Observer;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class MessagesController implements Observer<MessageChangeEvent> {
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
    Long group;
    public void setServices(String user, UserService userService, FriendshipService friendshipService, FriendshipRequestService requestService, MessageManagementService messageService, EventService eventService){
        this.user = user;
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.requestService = requestService;
        this.messageService = messageService;
        this.eventService = eventService;
        messageService.addObserver(this);

        group = -1L;
        loadGroups();
    }

    public void handleKeyPressed(javafx.scene.input.KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ESCAPE)
            handleBack();
    }

    class CellGroup extends ListCell<GroupDTO>{
        public Long groupID;
        VBox vBox = new VBox();
        HBox hBox = new HBox();
        Label name = new Label();
        Label userName = new Label();
        Label message = new Label();
        Pane pane = new Pane();

        public CellGroup(){
            name.setFont(new Font("System", 15));
            userName.setFont(new Font("System", 13));
            message.setFont(new Font("System", 13));
            setPrefHeight(40);
            vBox.setSpacing(2);
            hBox.setSpacing(5);
            hBox.getChildren().addAll(userName, message);
            vBox.getChildren().addAll(name, hBox);
            hBox.setHgrow(pane, Priority.ALWAYS);
        }

        public void updateItem(GroupDTO groupDTO, boolean empty){
            super.updateItem(groupDTO, empty);
            setText(null);
            setGraphic(null);

            if(groupDTO!=null && !empty){
                name.setText(groupDTO.getGroupName());
                userName.setText(groupDTO.getUserName()+":");
                String mes = groupDTO.getLastMessage();
                if(mes.length()>20)
                    mes = mes.substring(0, 16) + "...";
                message.setText(mes);
                groupID = groupDTO.getGroupID();
                setGraphic(vBox);
            }
        }
    }

    class CellMessage extends ListCell<MessageDTO>{
        Label name = new Label();
        Label message = new Label();
        Label time = new Label();
        VBox vBox = new VBox();
        Pane pane = new Pane();

        public CellMessage() {
            name.setFont(new Font("System", 13));
            message.setFont(new Font("System", 13));
            time.setFont(new Font("System", 7));
            vBox.setSpacing(1);
            vBox.getChildren().addAll(name, message, time);
            vBox.setVgrow(pane, Priority.ALWAYS);
        }

        public void updateItem(MessageDTO messageDTO, boolean empty){
            super.updateItem(messageDTO, empty);
            setText(null);
            setGraphic(null);
            if(messageDTO!=null && !empty){
                name.setText(messageDTO.getName());
                message.setText(messageDTO.getMessage());
                time.setText(messageDTO.getTimestamp().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)));
                setGraphic(vBox);
            }
        }
    }


    @FXML private ListView lstGroups;
    @FXML private ListView lstGroupMessages;
    @FXML private TextField txtMessage;
    @FXML private Button btBack;

    public void initialize(){
        lstGroups.setCellFactory(e->{
            CellGroup cell = new CellGroup();
            cell.setOnMouseClicked(c->{
                if(!cell.isEmpty()) {
                    loadMessages(cell.groupID);
                    group = cell.groupID;
                }
                c.consume();
            });
            return cell;
        });
        lstGroups.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE){
                    handleBack();
                }
            }
        });

        lstGroupMessages.setCellFactory(e->new CellMessage());

        lstGroupMessages.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE){
                    handleBack();
                }
            }
        });
        txtMessage.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE){
                    handleBack();
                }
                if(event.getCode() == KeyCode.ENTER){
                    sendMessage();
                }
            }
        });
    }

    public void loadGroups(){
        ObservableList<GroupDTO> groups = FXCollections.observableArrayList();
        messageService.seeMessages(user).forEach(e->groups.add(e));
        lstGroups.getItems().clear();
        lstGroups.getItems().addAll(groups);
    }

    public void loadMessages(Long groupID){
        ObservableList<MessageDTO> messages = FXCollections.observableArrayList();
        messageService.seeGroupMessages(groupID).forEach(e->messages.add(e));
        lstGroupMessages.getItems().clear();
        lstGroupMessages.getItems().addAll(messages);
        lstGroupMessages.scrollTo(lstGroupMessages.getItems().size());
    }

    public void sendMessage(){
        String message = txtMessage.getText();
        if(group!=-1){
            if(!message.isEmpty()){
                messageService.addMessageToGroup(group, user, message);
                txtMessage.clear();
            }
            else{
                Alert mes = new Alert(Alert.AlertType.INFORMATION);
                mes.setHeight(200);
                mes.setWidth(200);
                mes.getDialogPane().setContent(new ScrollPane(new Label(new String("Write a message before pressing send"))));
                mes.showAndWait();
            }
        }
        else{
            Alert mes = new Alert(Alert.AlertType.INFORMATION);
            mes.setHeight(200);
            mes.setWidth(200);
            mes.getDialogPane().setContent(new ScrollPane(new Label(new String("Select a group"))));
            mes.showAndWait();
        }
    }

    public void handleNewChat(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/newChat.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Message");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            NewChatController newChatController = loader.getController();
            newChatController.setServices(user, userService, friendshipService, requestService, messageService, eventService);
            newChatController.setWindow(dialogStage);
            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void handleBack() {
        messageService.removeObserver(this);
        try{
            FXMLLoader messageLoader = new FXMLLoader();
            messageLoader.setLocation(getClass().getResource("/views/userPage.fxml"));
            AnchorPane login = messageLoader.load();
            window.setScene(new Scene(login));

            UserPageController userPageController = messageLoader.getController();
            userPageController.setWindow(window);
            userPageController.setServices(user, userService, friendshipService, requestService, messageService, eventService);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(MessageChangeEvent messageChangeEvent) {
        loadGroups();
        loadMessages(group);
    }
}
