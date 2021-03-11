package socialnetwork.domain;

public class GroupUser extends Entity<Tuple<String, Long>>{
    public GroupUser(String userId, Long groupId) {
        super.setId(new Tuple<>(userId, groupId));
    }

    @Override
    public String toString() {
        return super.getId().getLeft() + " " + super.getId().getRight();
    }
}
