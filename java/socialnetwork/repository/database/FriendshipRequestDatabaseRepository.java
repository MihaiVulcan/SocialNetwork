package socialnetwork.repository.database;

import socialnetwork.domain.FriendshipRequest;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class FriendshipRequestDatabaseRepository extends AbstractDatabaseRepository<Tuple<String, String>, FriendshipRequest> {


    public FriendshipRequestDatabaseRepository(String url, String name, String password, Validator<FriendshipRequest> validator) {
        super(url, name, password, validator);
    }

    @Override
    public FriendshipRequest findOne(Tuple<String, String> id) {
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

            String sql = "SELECT user1, user2, timestamp FROM \"FriendshipRequests\" WHERE (user1=? and user2=?) or (user1=? and user2=?)";
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
                return new FriendshipRequest(date, id1, id2);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<FriendshipRequest> findAll() {
        Connection c = null;
        ArrayList<FriendshipRequest> friendships = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

            String sql = "SELECT user1, user2, timestamp FROM \"FriendshipRequests\"";
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                String id1 = resultSet.getString("user1");
                String id2 = resultSet.getString("user2");
                LocalDateTime date = resultSet.getObject(3, LocalDateTime.class);
                FriendshipRequest friendship = new FriendshipRequest(date, id1, id2);
                friendships.add(friendship);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return friendships;
    }

    @Override
    public FriendshipRequest save(FriendshipRequest friendship) {
        FriendshipRequest tempFriendship = findOne(friendship.getId());
        if(tempFriendship == null) {
            Connection c = null;
            try {
                Class.forName("org.postgresql.Driver");
                c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

                String sql = "INSERT INTO \"FriendshipRequests\" (user1, user2, timestamp) VALUES(?, ?, ?)";
                PreparedStatement preparedStatement = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                preparedStatement.setString(1, friendship.getId().getLeft());
                preparedStatement.setString(2, friendship.getId().getRight());
                preparedStatement.setObject(3, friendship.getDateTime());

                preparedStatement.executeUpdate();
            } catch (Exception exception) {
                //de modificat
                exception.printStackTrace();
            }
        }
        return tempFriendship;
    }

    @Override
    public FriendshipRequest delete(Tuple<String, String> id) {
        FriendshipRequest tempFriendship = findOne(id);
        if(tempFriendship != null) {
            Connection c;
            try {
                Class.forName("org.postgresql.Driver");
                c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

                String sql = "DELETE FROM \"FriendshipRequests\" WHERE (user1=? AND user2=? )OR (user1=? AND user2=?) ";
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

}
