package socialnetwork.domain;

public class EventUser extends Entity<Tuple<String, Long>>{
    public EventUser(String user, Long event) {
        super.setId(new Tuple<>(user, event));
    }
}
