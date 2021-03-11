package socialnetwork.ui.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import socialnetwork.domain.FriendshipRequest;
import socialnetwork.domain.User;
import socialnetwork.service.*;
import socialnetwork.service.datatrasfer.FriendshipDTO;
import socialnetwork.utils.events.FriendshipChangeEvent;
import socialnetwork.utils.observer.Observer;

import java.io.IOException;

public class FriendListController implements Observer<FriendshipChangeEvent> {
    Stage window;
    public void setWindow(Stage window) {
        this.window = window;
    }

    UserService userService;
    FriendshipService friendshipService;
    FriendshipRequestService requestService;
    MessageManagementService messageService;
    String user;
    EventService eventService;
    public void setServices(String user, UserService userService, FriendshipService friendshipService, FriendshipRequestService requestService, MessageManagementService messageService, EventService eventService){
        this.user = user;
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.requestService = requestService;
        this.messageService = messageService;
        this.eventService = eventService;
        friendshipService.addObserver(this);
        requestService.addObserver(this);
//        pagination.setPageCount(friendshipService.pageCount(user));
//        pagination.setCurrentPageIndex(0);
        displayListPage(0);

    }

    void displayList (){
        ObservableList<FriendshipDTO> friends = FXCollections.observableArrayList();
        friendshipService.getFriends(user).forEach(e->friends.add(e));
        lstFriends.getItems().clear();
        lstFriends.getItems().addAll(friends);
    }

    @FXML Button btAddFriend;
    @FXML TextField textAddFriend;
    @FXML Button btBack;
    @FXML ListView lstFriends;
    @FXML Pagination pagination;

    @Override
    public void update(FriendshipChangeEvent friendshipChangeEvent) {
        displayListPage(0);
    }

    class Cell extends ListCell<FriendshipDTO>{
        HBox hBox = new HBox();
        Button btt = new Button("delete");
        String user;
        Label timestamp = new Label();
        Label lbName = new Label();
        Label lbBreed = new Label();
        Pane pane = new Pane();

        public Cell() {
            super();
            hBox.setSpacing(20);
            btt.setOnAction(event -> friendshipService.deleteFriendship(this.user, FriendListController.this.user));
            hBox.getChildren().addAll(lbName, lbBreed, timestamp, btt);
            hBox.setHgrow(pane, Priority.ALWAYS);
        }

        public void updateItem(FriendshipDTO friendshipDTO, boolean empty){
            super.updateItem(friendshipDTO, empty);
            setText(null);
            setGraphic(null);

            if(friendshipDTO!=null && !empty) {
                lbName.setText(friendshipDTO.getName());
                lbBreed.setText(friendshipDTO.getBreed());
                user = friendshipDTO.getEmailUser();
                timestamp.setText(friendshipDTO.getLocalDateTime().getMonth().toString());
                setGraphic(hBox);
            }
        }
    }

    @FXML
    public void initialize(){
        lstFriends.setCellFactory(e->new Cell());
        pagination.setPageFactory(this::displayListPage);
        lstFriends.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()){
                    case LEFT:
                        if(pagination.getCurrentPageIndex()!=0)
                            pagination.setCurrentPageIndex(pagination.getCurrentPageIndex()-1);
                        break;
                    case RIGHT:
                        if(pagination.getCurrentPageIndex()!=pagination.getPageCount())
                            pagination.setCurrentPageIndex(pagination.getCurrentPageIndex()+1);
                        break;
                    case ESCAPE:
                        handleBack();
                        break;
                    case TAB:
                        textAddFriend.requestFocus();
                }
            }
        });
        textAddFriend.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE)
                    handleBack();
                if(event.getCode() == KeyCode.ENTER)
                    handleAddFriend();
            }
        });
        btAddFriend.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()){
                    case ENTER:
                        handleAddFriend();
                        break;
                    case ESCAPE:
                        handleBack();
                        break;
                }
            }
        });
    }

    public Node displayListPage(int page){
        ObservableList<FriendshipDTO> friends = FXCollections.observableArrayList();
        friendshipService.getFriendPage(page, user).forEach(e->friends.add(e));
        lstFriends.getItems().clear();
        lstFriends.getItems().addAll(friends);
        pagination.setPageCount(friendshipService.pageCount(user));
        return new BorderPane();
    }

    @FXML
    public void updatePage(){
        displayListPage(pagination.getCurrentPageIndex());
    }

    @FXML
    public void handleBack() {
        try{
            friendshipService.removeObserver(this);

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

    @FXML
    public void handleAddFriend(){
        String email = textAddFriend.getText();
        User temp = userService.getOne(email);
        if(temp != null){
            FriendshipRequest frReq =  requestService.addRequest(user, email);
            if(frReq != null){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeight(200);
                alert.setWidth(200);
                alert.getDialogPane().setContent(new ScrollPane(new Label(new String("You are already friends"))));
                alert.showAndWait();
            }
            textAddFriend.setText("");
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeight(200);
            alert.setWidth(200);
            alert.getDialogPane().setContent(new ScrollPane(new Label(new String("This user dosen't exist"))));
            alert.showAndWait();
        }

    }
}
