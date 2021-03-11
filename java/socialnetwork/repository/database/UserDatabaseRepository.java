package socialnetwork.repository.database;

import socialnetwork.domain.User;
import socialnetwork.domain.validators.Validator;

import java.sql.*;
import java.util.ArrayList;

public class UserDatabaseRepository extends AbstractDatabaseRepository<String, User>{

    public UserDatabaseRepository(String url, String name, String password, Validator<User> validator) {
        super(url, name, password, validator);
    }

    public User findOne(String email){
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

            String sql = "SELECT nume, breed, favefood FROM \"User\" WHERE email='"+email+"'";
            Statement statement = c.createStatement();
            ResultSet result = statement.executeQuery(sql);

            if(result.next()) {//daca exista o inregistrare
                String nume = result.getString("nume");
                String breed = result.getString("breed");
                String favefood = result.getString("favefood");

                return new User(email, nume, breed, favefood);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return null;
    }
    @Override
    public Iterable<User> findAll() {
        Connection c = null;
        ArrayList<User> users = new ArrayList<User>();
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

            String sql = "SELECT nume, breed, email, favefood FROM \"User\"";
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                String nume = resultSet.getString("nume");
                String breed = resultSet.getString("breed");
                String favefood = resultSet.getString("favefood");
                String email = resultSet.getString("email");

                User user = new User(email, nume, breed, favefood);
                users.add(user);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return users;
    }

    @Override
    public User save(User user) {
        super.save(user);
        User tempUser = findOne(user.getEmail());
        if(tempUser == null){ //daca mai exista un utilizator cu acest email returnam utilizatorl cu acest email
            Connection c = null;
            try {
                Class.forName("org.postgresql.Driver");
                c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

                String sql = "INSERT INTO \"User\"(nume, breed, email, favefood) VALUES(?, ?, ?, ?)";
                PreparedStatement preparedStatement = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getBreed());
//            preparedStatement.setObject(3, user.getBirthdate());
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.setString(4, user.getFavouriteFood());
//            preparedStatement.setString(6, user.getGender());

                preparedStatement.executeUpdate();


            } catch (Exception exception) {
                //TODO de modificat
                exception.printStackTrace();
            }
        }
        return tempUser;
    }

    @Override
    public User delete(String email) {
        User tempUser = findOne(email);
        Connection c;
        try{
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

            String sql = "DELETE FROM \"User\" WHERE email=?";
            PreparedStatement preparedStatement = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, email);
            preparedStatement.executeUpdate();

        }catch (Exception exception){
            //TODO de modificat
            exception.printStackTrace();
        }
        return tempUser;
    }
}
