package socialnetwork.domain;

import java.time.LocalDateTime;

public class FriendshipRequest extends Entity<Tuple<String, String>> {
    private LocalDateTime dateTime;

    public FriendshipRequest(LocalDateTime localDateTime, String user1, String user2) {
        dateTime = localDateTime;
        super.setId(new Tuple(user1,user2));
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
