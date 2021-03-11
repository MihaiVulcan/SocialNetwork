package socialnetwork.service.datatrasfer;

import java.time.LocalDateTime;

public class MessageDTO {
    private String name;
    private String message;
    private LocalDateTime timestamp;

    public MessageDTO(String name, String message, LocalDateTime timestamp) {
        this.name = name;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return timestamp + " " + name + ": " + message;
    }
}
