package socialnetwork.repository.database;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.paging.Pageable;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class FriendshipDatabaseRepository extends AbstractDatabaseRepository<Tuple<String, String>, Friendship>{

    public FriendshipDatabaseRepository(String url, String name, String password, Validator<Friendship> validator) {
        super(url, name, password, validator);
    }

    @Override
    public Friendship findOne(Tuple<String, String> id) {
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

            String sql = "SELECT user1, user2, date FROM \"Friendship\" WHERE (user1=? and user2=?) or (user1=? and user2=?)";
            PreparedStatement preparedStatement = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, id.getLeft());
            preparedStatement.setString(2, id.getRight());
            preparedStatement.setString(3, id.getRight());
            preparedStatement.setString(4, id.getLeft());
            ResultSet result = preparedStatement.executeQuery();
            if(result.next()){
                String id1 = result.getString("user1");
                String id2 = result.getString("user2");
                LocalDateTime date = result.getObject(3, LocalDateTime.class);
                return new Friendship(date, id1, id2);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return null;
    }

    /**
     * returns all friends of a user
     * @param email
     * @return
     */
    public Iterable<Friendship> findAll(String email){
        Connection c = null;
        ArrayList<Friendship> friendships = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

            String sql = "SELECT user1, user2, date FROM \"Friendship\"  WHERE user1=? or user2=?";
            PreparedStatement preparedStatement = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String id1 = resultSet.getString("user1");
                String id2 = resultSet.getString("user2");
                LocalDateTime date = resultSet.getObject(3, LocalDateTime.class);
                Friendship friendship = new Friendship(date, id1, id2);
                friendships.add(friendship);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return friendships;
    }

    @Override
    public Iterable<Friendship> findAll() {
        Connection c = null;
        ArrayList<Friendship> friendships = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

            String sql = "SELECT user1, user2, date FROM \"Friendship\"";

            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                String id1 = resultSet.getString("user1");
                String id2 = resultSet.getString("user2");
                LocalDateTime date = resultSet.getObject(3, LocalDateTime.class);
                Friendship friendship = new Friendship(date, id1, id2);
                friendships.add(friendship);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return friendships;
    }

    @Override
    public Friendship save(Friendship friendship) {
        Friendship tempFriendship = findOne(friendship.getId());
        if(tempFriendship == null) {
            Connection c = null;
            try {
                Class.forName("org.postgresql.Driver");
                c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

                String sql = "INSERT INTO \"Friendship\" (user1, user2, date) VALUES(?, ?, ?)";
                PreparedStatement preparedStatement = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                preparedStatement.setString(1, friendship.getId().getLeft());
                preparedStatement.setString(2, friendship.getId().getRight());
                preparedStatement.setObject(3, friendship.getDate());

                preparedStatement.executeUpdate();
            } catch (Exception exception) {
                //de modificat
                exception.printStackTrace();
            }
        }
        return tempFriendship;
    }

    @Override
    public Friendship delete(Tuple<String, String> id) {
        Friendship tempFriendship = findOne(id);
        if(tempFriendship != null) {
            Connection c;
            try {
                Class.forName("org.postgresql.Driver");
                c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

                String sql = "DELETE FROM \"Friendship\" WHERE (user1=? AND user2=? )OR (user1=? AND user2=?) ";
                PreparedStatement preparedStatement = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                preparedStatement.setString(1, tempFriendship.getId().getLeft());
                preparedStatement.setString(2, tempFriendship.getId().getRight());
                preparedStatement.setString(3, tempFriendship.getId().getRight());
                preparedStatement.setString(4, tempFriendship.getId().getLeft());

                preparedStatement.executeUpdate();

            } catch (Exception exception) {
                //de modificat
                exception.printStackTrace();
            }
        }
        return tempFriendship;
    }

    public Iterable<Friendship> findByPage(String email, Pageable pageable) {
        Connection c = null;
        ArrayList<Friendship> friendships = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

            String sql = "SELECT user1, user2, date FROM \"Friendship\"  WHERE user1=? or user2=? LIMIT ? OFFSET ? ";


            PreparedStatement statement = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, email);
            statement.setString(2, email);
            statement.setInt(3, pageable.getPageSize());
            statement.setInt(4, pageable.getPageNumber());

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                String id1 = resultSet.getString("user1");
                String id2 = resultSet.getString("user2");
                LocalDateTime date = resultSet.getObject(3, LocalDateTime.class);
                Friendship friendship = new Friendship(date, id1, id2);
                friendships.add(friendship);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return friendships;
    }

    public int nrOfEntries(String user) {
        int entries =0;
        Connection c = null;
        ArrayList<Friendship> friendships = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

            String sql = "SELECT user1, user2, date FROM \"Friendship\"  WHERE user1=? or user2=?";
            PreparedStatement preparedStatement = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, user);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                entries++;
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return entries;
    }
}
