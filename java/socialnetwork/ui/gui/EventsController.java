package socialnetwork.ui.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import socialnetwork.domain.Event;
import socialnetwork.domain.TimeNotification;
import socialnetwork.service.*;
import socialnetwork.service.datatrasfer.EventDTO;
import socialnetwork.utils.events.EventChangeEvent;
import socialnetwork.utils.observer.Observer;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class EventsController implements Observer<EventChangeEvent> {
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
        eventService.addObserver(this);
        displayList();
    }

    @Override
    public void update(EventChangeEvent eventChangeEvent) {
        displayList();
    }

    class Cell extends ListCell<EventDTO> {
        HBox hBox = new HBox();
        Label name = new Label();
        Label time = new Label();
        Button btt = new Button();
        Button bttNot = new Button();
        Pane pane = new Pane();

        public Cell() {
            super();
            hBox.setSpacing(15);
            hBox.getChildren().addAll(name, time, btt, bttNot);
            hBox.setHgrow(pane, Priority.ALWAYS);
        }

        public void updateItem(EventDTO event, boolean empty){
            super.updateItem(event, empty);
            setText(null);
            setGraphic(null);

            if(event!=null && !empty) {
                name.setText(event.getName());
                time.setText(event.getTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)));
                if(event.isJoined()){
                    bttNot.setDisable(false);
                    btt.setText("Unjoin");
                    btt.setOnAction(event1 -> eventService.noParticipate(user, event.getId()));
                    if(eventService.notificationsTurnOn(user, event.getId(), event.getTime())){
                        bttNot.setText("Disable Notifications");
                        bttNot.setOnAction(event1 -> eventService.deleteNotifications(user, event.getId(), TimeNotification.NOW));
                    }
                    else{
                        bttNot.setText("Enable Notifications");
                        bttNot.setOnAction(event1 -> eventService.addNotifications(user, new Event(event.getId(), event.getName(), event.getDescription(), event.getTime())));
                    }
                }
                else{
                    btt.setText("Join");
                    btt.setOnAction(event1 -> eventService.participate(user, event.getId()));
                    bttNot.setText("Disable Notifications");
                    bttNot.setDisable(true);
                }
                setGraphic(hBox);
            }
        }
    }

    @FXML
    ListView lstEvents;

    @FXML
    public void initialize(){
        lstEvents.setCellFactory(e->new Cell());
    }

    public void displayList(){
        ObservableList<EventDTO> events = FXCollections.observableArrayList();
        eventService.userEvents(user).forEach(e->events.add(e));
        lstEvents.getItems().clear();
        lstEvents.getItems().addAll(events);
    }

    @FXML
    public void createNewEvent(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/newEvent.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("New Event");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            NewEventController newEventController = loader.getController();
            newEventController.setServices(user, userService, friendshipService, requestService, messageService, eventService);
            newEventController.setWindow(dialogStage);
            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBack() throws IOException {
        eventService.removeObserver(this);
        FXMLLoader messageLoader = new FXMLLoader();
        messageLoader.setLocation(getClass().getResource("/views/userPage.fxml"));
        AnchorPane login = messageLoader.load();
        window.setScene(new Scene(login));

        UserPageController userPageController = messageLoader.getController();
        userPageController.setWindow(window);
        userPageController.setServices(user, userService, friendshipService, requestService, messageService, eventService);
    }

}
