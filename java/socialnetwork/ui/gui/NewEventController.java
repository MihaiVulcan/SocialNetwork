package socialnetwork.ui.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import socialnetwork.service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class NewEventController {
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
    }

    @FXML
    TextField txtName;
    @FXML
    TextArea txtDesc;
    @FXML
    DatePicker datePicker;
    @FXML
    ComboBox cmbHour;
    @FXML
    ComboBox cmbMin;


    @FXML
    public void initialize(){
        List<Integer> hours = new ArrayList<>();
        for(int i=0; i<24; i++)
            hours.add(i);
        cmbHour.getItems().addAll(hours);

        List<Integer> minutes = new ArrayList<>();
        for(int i=0; i<60; i+=1)
            minutes.add(i);
        cmbMin.getItems().addAll(minutes);
    }

    @FXML
    public void newEvent(){
        String name = txtName.getText();
        String desc = txtDesc.getText();
        String error = "";
        if (name == null)
            error += "Add a name\n";
        if (desc == null)
            error += "Add a description\n";

        LocalDate date = datePicker.getValue();
        if (date == null)
            error += "Select a date";
        int hour = 0, min = 0;
        try {
            hour = (int) cmbHour.getSelectionModel().getSelectedItem();
        } catch (NullPointerException e) {
            error += "Select an hour\n";
        }

        try {
            min = (int) cmbMin.getSelectionModel().getSelectedItem();
        } catch (NullPointerException e) {
            error += "Select minutes\n";
        }
        if(date == null){
            error+="Select a date";
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeight(200);
            alert.setWidth(200);
            alert.getDialogPane().setContent(new ScrollPane(new Label(error)));
            alert.showAndWait();
        }
        else{
            LocalDateTime time = LocalDateTime.of(date, LocalTime.of(hour, min, 0, 0));
            if (time == null)
                error += "Add time\n";
            if (time.isBefore(LocalDateTime.now()))
                error += "Cannot create a event in the past";

            if (error.equals("")) {
                eventService.addEvent(name, desc, time);
                window.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeight(200);
                alert.setWidth(200);
                alert.getDialogPane().setContent(new ScrollPane(new Label(error)));
                alert.showAndWait();
            }
        }

    }

    @FXML
    public void handleBack(){
        window.close();
    }
}
