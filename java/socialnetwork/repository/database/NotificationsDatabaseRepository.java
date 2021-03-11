package socialnetwork.repository.database;

import socialnetwork.domain.Notification;
import socialnetwork.domain.TimeNotification;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.validators.Validator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NotificationsDatabaseRepository extends AbstractDatabaseRepository<Tuple<Tuple<String, Long>, TimeNotification>, Notification> {
    public NotificationsDatabaseRepository(String url, String name, String password, Validator<Notification> validator) {
        super(url, name, password, validator);
    }
    @Override
    public Notification findOne(Tuple<Tuple<String, Long>, TimeNotification> id) {
        Connection c =null;
        try{
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

            String sql = "SELECT userid, event, times FROM \"Notifications\" WHERE userid=? AND event=? AND times=?";
            PreparedStatement preparedStatement =c.prepareStatement(sql);
            preparedStatement.setString(1, id.getLeft().getLeft());
            preparedStatement.setLong(2, id.getLeft().getRight());
            preparedStatement.setString(3, id.getRight().name());
            ResultSet result = preparedStatement.executeQuery();
            if(result.next()){
                TimeNotification time = TimeNotification.valueOf(result.getString("times"));
                return new Notification(id.getLeft().getLeft(), id.getLeft().getRight(), id.getRight());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Notification> findAll() {
        Connection c =null;
        List<Notification> groupUserList = new ArrayList<>();
        try{
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

            String sql = "SELECT userid, event, times FROM \"Notifications\"";
            PreparedStatement preparedStatement =c.prepareStatement(sql);
            ResultSet result = preparedStatement.executeQuery();
            while(result.next()){
                String idUser = result.getString("userid");
                Long idEvent= result.getLong("event");
                TimeNotification time = TimeNotification.valueOf(result.getString("times"));
                groupUserList.add( new Notification(idUser, idEvent, time));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //System.out.println(groupUserList);
        return groupUserList;
    }

    @Override
    public Notification save(Notification notification) {
        Notification temp = findOne(notification.getId());
        if(temp==null) {
            Connection c =null;
            try{
                Class.forName("org.postgresql.Driver");
                c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

                String sql = "INSERT INTO \"Notifications\"(userid, event, times) VALUES(?, ?, ?)";
                PreparedStatement preparedStatement =c.prepareStatement(sql);
                preparedStatement.setString(1, notification.getId().getLeft().getLeft());
                preparedStatement.setLong(2, notification.getId().getLeft().getRight());
                preparedStatement.setString(3,notification.getId().getRight().name());
                preparedStatement.executeUpdate();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return temp;
    }

    @Override
    public Notification delete(Tuple<Tuple<String, Long>, TimeNotification> id) {
        Notification notification = findOne(id);
        if(notification!=null){
            Connection c =null;
            try{
                Class.forName("org.postgresql.Driver");
                c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

                String sql = "DELETE FROM \"Notifications\" WHERE userid=? AND event=? AND times=?";
                PreparedStatement preparedStatement =c.prepareStatement(sql);
                preparedStatement.setString(1, id.getLeft().getLeft());
                preparedStatement.setLong(2, id.getLeft().getRight());
                preparedStatement.setString(3, id.getRight().name());
                preparedStatement.executeUpdate();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return notification;
    }
}
