package socialnetwork.service.datatrasfer;

public class GroupDTO {
    private int index;
    private Long groupID;
    private String groupName;
    private String userName;
    private String lastMessage;

    public GroupDTO(int index, Long groupID, String groupName, String userName, String lastMessage) {
        this.index = index;
        this.groupID = groupID;
        this.groupName = groupName;
        this.userName = userName;
        this.lastMessage = lastMessage;
    }

    public Long getGroupID() {
        return groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getUserName() {
        return userName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    @Override
    public String toString() {
        return index + " " + groupName + " " + userName + ": " + lastMessage;
    }
}
