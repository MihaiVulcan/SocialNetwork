package socialnetwork.service.datatrasfer;

import java.time.LocalDateTime;

public class EventDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime time;
    private boolean joined;

    public EventDTO(Long id, String name, String description, LocalDateTime time, boolean joined) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.time = time;
        this.joined = joined;
    }

    public Long getId() {
        return id;
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

    public boolean isJoined() {
        return joined;
    }
}
