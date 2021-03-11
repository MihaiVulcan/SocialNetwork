package socialnetwork.ui.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import socialnetwork.service.*;
import socialnetwork.service.datatrasfer.FriendshipDTO;
import socialnetwork.utils.events.FriendshipChangeEvent;
import socialnetwork.utils.observer.Observer;

import java.io.IOException;

public class FriendRequestController implements Observer<FriendshipChangeEvent> {
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
        requestService.addObserver(this);
        friendshipService.addObserver(this);
        initModel();
    }

    public void initModel(){
        ObservableList<FriendshipDTO> receviedRequest = FXCollections.observableArrayList();
        requestService.getUserRequests(user).forEach(e->receviedRequest.add(e));
        lstRequestReceived.getItems().clear();
        lstRequestReceived.getItems().addAll(receviedRequest);

        ObservableList<FriendshipDTO> sentRequest = FXCollections.observableArrayList();
        requestService.getUserSentRequests(user).forEach(e->sentRequest.add(e));
        lstRequestSent.getItems().clear();
        lstRequestSent.getItems().addAll(sentRequest);
    }

    @Override
    public void update(FriendshipChangeEvent friendRequestChangeEvent) {
        initModel();
    }

    class CellRecevied extends ListCell<FriendshipDTO> {
        HBox hBox = new HBox();
        Button bttAdd = new Button("Add");
        Button bttDel = new Button("Delete");
        String user;
        Label timestamp = new Label();
        Label lbName = new Label();
        Pane pane = new Pane();

        public CellRecevied() {
            super();
            hBox.setSpacing(20);
            bttAdd.setOnAction(event -> requestService.acceptRequest(user, FriendRequestController.this.user));
            bttDel.setOnAction(event -> requestService.declineRequest(user, FriendRequestController.this.user));
            hBox.getChildren().addAll(lbName, timestamp, bttAdd, bttDel);
            hBox.setHgrow(pane, Priority.ALWAYS);
        }


        public void updateItem(FriendshipDTO friendshipDTO, boolean empty){
            super.updateItem(friendshipDTO, empty);
            setText(null);
            setGraphic(null);

            if(friendshipDTO!=null && !empty) {
                lbName.setText(friendshipDTO.getName());
                user = friendshipDTO.getEmailUser();
                timestamp.setText(friendshipDTO.getLocalDateTime().getMonth().toString());
                setGraphic(hBox);
            }
        }
    }
    class CellSent extends  ListCell<FriendshipDTO>{
        HBox hBox = new HBox();
        Button bttDel = new Button("Delete");
        String user;
        Label timestamp = new Label();
        Label lbName = new Label();
        Pane pane = new Pane();

        public CellSent() {
            super();
            hBox.setSpacing(20);
            bttDel.setOnAction(event -> requestService.declineRequest(user, FriendRequestController.this.user));
            hBox.getChildren().addAll(lbName, timestamp, bttDel);
            hBox.setHgrow(pane, Priority.ALWAYS);
        }


        public void updateItem(FriendshipDTO friendshipDTO, boolean empty){
            super.updateItem(friendshipDTO, empty);
            setText(null);
            setGraphic(null);

            if(friendshipDTO!=null && !empty) {
                lbName.setText(friendshipDTO.getName());
                user = friendshipDTO.getEmailUser();
                timestamp.setText(friendshipDTO.getLocalDateTime().getMonth().toString());
                setGraphic(hBox);
            }
        }
    }

    @FXML private ListView lstRequestReceived;
    @FXML private ListView lstRequestSent;

    public void initialize(){
        lstRequestReceived.setCellFactory(e->new FriendRequestController.CellRecevied());
        lstRequestSent.setCellFactory(e->new FriendRequestController.CellSent());
        lstRequestReceived.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE)
                    handleBack();
            }
        });
        lstRequestSent.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE)
                    handleBack();
            }
        });
    }

    @FXML
    public void handleBack() {
        try {
            requestService.removeObserver(this);

            FXMLLoader messageLoader = new FXMLLoader();
            messageLoader.setLocation(getClass().getResource("/views/userPage.fxml"));
            AnchorPane login = messageLoader.load();
            window.setScene(new Scene(login));

            UserPageController userPageController = messageLoader.getController();
            userPageController.setWindow(window);
            userPageController.setServices(user, userService, friendshipService, requestService, messageService, eventService);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
