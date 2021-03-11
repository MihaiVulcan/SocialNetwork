package socialnetwork.service;

import socialnetwork.domain.User;
import socialnetwork.repository.Repository;
import socialnetwork.repository.database.PasswordManager;

public class UserService {
    private Repository<String, User> repo;
    private PasswordManager passwordManager;

    public UserService(Repository<String, User> repo, PasswordManager passwordManager) {
        this.repo = repo;
        this.passwordManager = passwordManager;
    }

    /**
     * creats and user and saves it repository
     * @param email
     * @param nume
     * @param breed
     * @param favefood
     * @return null if new user is saved, other user if email repeats
     */
    public User addUser(String email, String nume, String breed, String favefood, String password){
        User user = repo.save(new User(email, nume, breed, favefood));
        if(user==null){
            passwordManager.add(email, password);
        }
        return user;
    }

    /**
     * deletes user
     * @param id
     * @return null if id does not exist or user that was deleted
     */
    public User delete(String id) {
        return repo.delete(id);
    }

    /**
     * returns all users
     * @return returns all users
     */
    public Iterable<User> getAll() {
        return repo.findAll();
    }

    /**
     * return the id of an user email
     * @param email
     * @return id or -1 if email is not registered
     */
    public User getOne(String email){
        User tempUser = repo.findOne(email);
        return tempUser;
    }

    /**
     * check if a user is signed up
     * @param email
     * @param password
     * @return true if password and email are correct or false otherwise
     */
    public boolean logIn(String email, String password){
        return passwordManager.check(email, password);
    }

}
