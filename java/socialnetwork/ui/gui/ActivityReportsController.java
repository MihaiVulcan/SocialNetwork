package socialnetwork.ui.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import socialnetwork.reports.ActivityReport;
import socialnetwork.reports.ActivityUserReport;
import socialnetwork.service.*;
import socialnetwork.service.datatrasfer.FriendshipDTO;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;

public class ActivityReportsController {
    Stage window;
    public void setWindow(Stage window) {
        this.window = window;
    }

    UserService userService;
    FriendshipService friendshipService;
    FriendshipRequestService requestService;
    MessageManagementService messageService;
    String user;
    String selectedFriend = new String();
    ActivityReport activityReport;
    ActivityUserReport activityUserReport;
    EventService eventService;
    public void setServices(String user, UserService userService, FriendshipService friendshipService, FriendshipRequestService requestService, MessageManagementService messageService, EventService eventService){
        this.user = user;
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.requestService = requestService;
        this.messageService = messageService;
        activityReport = new ActivityReport(messageService, friendshipService, userService);
        activityUserReport = new ActivityUserReport(messageService, friendshipService, userService);
        cmbFriends.getItems().addAll((Collection<? extends FriendshipDTO>) friendshipService.getFriends(user));
        this.eventService = eventService;
    }

    class CmbCell extends ListCell<FriendshipDTO>{
        @Override
        public void updateItem(FriendshipDTO item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                selectedFriend = item.getEmailUser();
                setText(item.getName());
            }
            else
                setText(null);
        }
    }

    Callback<ListView<FriendshipDTO>, ListCell<FriendshipDTO>> cellFactory = new Callback<ListView<FriendshipDTO>, ListCell<FriendshipDTO>>() {

        @Override
        public ListCell<FriendshipDTO> call(ListView<FriendshipDTO> l) {
            return new ListCell<FriendshipDTO>() {

                @Override
                protected void updateItem(FriendshipDTO item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setGraphic(null);
                    } else {
                        selectedFriend = item.getName();
                        setText(item.getName());
                    }
                }
            } ;
        }
    };


    @FXML
    ComboBox<FriendshipDTO> cmbFriends;
    @FXML
    ComboBox cmbReport;
    @FXML
    DatePicker dateFrom;
    @FXML
    DatePicker dateTo;

    @FXML
    public void initialize(){
        cmbReport.getItems().addAll("Activity Report", "Activity with friend");
        cmbFriends.setCellFactory(cellFactory);
        cmbFriends.setButtonCell(cellFactory.call(null));
        cmbReport.getSelectionModel().selectFirst();
        cmbFriends.getSelectionModel().selectFirst();
    }

    @FXML
    public void handleBack() throws IOException {

        FXMLLoader messageLoader = new FXMLLoader();
        messageLoader.setLocation(getClass().getResource("/views/userPage.fxml"));
        AnchorPane login = messageLoader.load();
        window.setWidth(600);
        window.setScene(new Scene(login));

        UserPageController userPageController = messageLoader.getController();
        userPageController.setWindow(window);
        userPageController.setServices(user, userService, friendshipService, requestService, messageService, eventService);
    }

    @FXML
    public void handleSave(){
        LocalDate from = dateFrom.getValue();
        LocalDate to = dateTo.getValue();
        if(from == null)
            from = LocalDate.of(2000, 1, 1);
        if(to == null)
            to = LocalDate.now();
        if(to.isAfter(LocalDate.now()))
            to = LocalDate.now();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.setInitialFileName("myActivity");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("pdf", "*.pdf"));
        File file = fileChooser.showSaveDialog(window);
        if(file != null) {
            if(cmbReport.getSelectionModel().isSelected(0))
                activityReport.save(user, file.getAbsolutePath(), from, to);
            else
                activityUserReport.save(user, file.getAbsolutePath(), from, to, selectedFriend);
        }

    }

    @FXML
    public void changeCombo(){
        if(cmbReport.getSelectionModel().isSelected(0)){
            cmbFriends.setVisible(false);
        }
        else{
            cmbFriends.setVisible(true);
            cmbFriends.getSelectionModel().selectFirst();
        }
    }

}
