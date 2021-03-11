package socialnetwork.domain;

import java.time.LocalDateTime;

public class Event extends Entity<Long> {
    private String name;
    private String description;
    private LocalDateTime time;

    public Event(String name, String description, LocalDateTime time) {
        this.name = name;
        this.description = description;
        this.time = time;
    }

    public Event(Long id, String name, String description, LocalDateTime time) {
        super.setId(id);
        this.name = name;
        this.description = description;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
