package socialnetwork.domain;

public class User extends Entity<String> {
    private String name;
    private String breed;
    private String favouriteFood;
//    private LocalDate birthdate;
//    private String gender;

    public User(String email, String name, String breed, String favouriteFood) {
        super.setId(email);
        this.name = name;
        this.breed = breed;
        this.favouriteFood = favouriteFood;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getFavouriteFood() {
        return favouriteFood;
    }

    public void setFavouriteFood(String favouriteFood) {
        this.favouriteFood = favouriteFood;
    }

    public String getEmail(){
        return super.getId();
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", breed='" + breed + '\'' +
                ", email='" + super.getId() + '\'' +
                ", favouriteFood='" + favouriteFood + '\'' +
                '}';
    }
}

