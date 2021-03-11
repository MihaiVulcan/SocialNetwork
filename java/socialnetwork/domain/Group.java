package socialnetwork.domain;

public class Group extends Entity<Long> {
    private String name;
    private String description;

    public Group(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Group(Long id, String name, String description) {
        super.setId(id);
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
