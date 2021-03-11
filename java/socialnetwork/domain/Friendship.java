package socialnetwork.domain;

import java.time.LocalDateTime;

public class Friendship extends Entity<Tuple<String, String>> {
    private LocalDateTime date;

    public Friendship(LocalDateTime date, String user1, String user2) {
        this.date = date;
        super.setId(new Tuple<>(user1, user2));
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "date=" + date +
                '}';
    }
}
