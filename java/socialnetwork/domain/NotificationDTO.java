package socialnetwork.domain;

import java.time.LocalDateTime;

public class NotificationDTO {
    private Long id;
    private String name;
    private LocalDateTime time;
    private String when;

    public NotificationDTO(Long id, String name, LocalDateTime time, String when) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.when = when;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getWhen() {
        return when;
    }
}
