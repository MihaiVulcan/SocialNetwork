package socialnetwork.notification;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import socialnetwork.service.EventService;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class Notificator extends Service<String>  {
    private String user;
    private EventService eventService;
    private int sleepTime;

    public Notificator(String user, EventService eventService, int sleepTime){
        this.user = user;
        this.eventService = eventService;
        this.sleepTime = sleepTime;
        setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                String not = getNotifications();
                if(!not.equals("")){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.getDialogPane().setContent(new Pane(new Label(not)));
                    alert.showAndWait();
                }
                new Notificator(user, eventService, 10*1000).start();
            }
        });
        setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                Thread.currentThread().interrupt();
            }
        });
    }

    private String getNotifications(){
        String rez = "";
        for (var not : eventService.notificationsToShow(user)){
            rez+=not.getName()+" "+not.getWhen()+"\n"+not.getTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))+"\n";
        }
        return rez;
    }

    @Override
    protected Task<String> createTask() {
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                Thread.currentThread().setName("Notificator");
                Thread.sleep(sleepTime);
                return null;
            }
        };
    }
}
