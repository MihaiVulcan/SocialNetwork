package socialnetwork.domain;

public class Notification extends Entity<Tuple<Tuple<String, Long>, TimeNotification>>{
    public Notification(String user, Long event, TimeNotification time) {
        super.setId(new Tuple(new Tuple(user, event), time));
    }
}
