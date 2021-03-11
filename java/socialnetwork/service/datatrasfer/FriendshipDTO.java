package socialnetwork.service.datatrasfer;

import java.time.LocalDateTime;

public class FriendshipDTO {
    private String emailUser;
    private String name;
    private String breed;

    public FriendshipDTO(String emailUser, String name, String breed, LocalDateTime localDateTime) {
        this.emailUser = emailUser;
        this.name = name;
        this.breed = breed;
        this.localDateTime = localDateTime;
    }

    public String getBreed() {
        return breed;
    }

    private LocalDateTime localDateTime;

    public FriendshipDTO(String emailUser, String name, LocalDateTime localDateTime) {
        this.emailUser = emailUser;
        this.name = name;
        this.localDateTime = localDateTime;
    }

    @Override
    public String toString() {
        return "Name " + name + "\tEmail " + emailUser + " \tDate " + localDateTime.toString();
    }

    public String getEmailUser() {
        return emailUser;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }
}
