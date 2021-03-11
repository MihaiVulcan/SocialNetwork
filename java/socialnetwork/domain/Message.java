package socialnetwork.domain;

import java.time.LocalDateTime;

public class Message extends Entity<Long> {
    private String sender;
    private long groupId;
    private LocalDateTime timeStamp;
    private String text;

    public Message(String sender, long groupId, String text) {
        this.sender = sender;
        this.groupId = groupId;
        this.text = text;
        timeStamp = LocalDateTime.now();
    }

    public Message(Long id, String sender, long groupId, String text, LocalDateTime timeStamp) {
        super.setId(id);
        this.sender = sender;
        this.groupId = groupId;
        this.text = text;
        this.timeStamp = timeStamp;
    }

    public String getSender() {
        return sender;
    }

    public long getGroupId() {
        return groupId;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
