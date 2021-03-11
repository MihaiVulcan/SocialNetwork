package socialnetwork.repository.database;


import socialnetwork.domain.EventUser;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.validators.Validator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EventUserDatabaseRepository extends AbstractDatabaseRepository<Tuple<String, Long>, EventUser> {
    public EventUserDatabaseRepository(String url, String name, String password, Validator<EventUser> validator) {
        super(url, name, password, validator);
    }

    @Override
    public EventUser findOne(Tuple<String, Long> id) {
        Connection c =null;
        try{
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

            String sql = "SELECT userid, event FROM \"EventUser\" WHERE userid=? AND event=?";
            PreparedStatement preparedStatement =c.prepareStatement(sql);
            preparedStatement.setString(1, id.getLeft());
            preparedStatement.setLong(2, id.getRight());
            ResultSet result = preparedStatement.executeQuery();
            if(result.next()){
                return new EventUser(id.getLeft(), id.getRight());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<EventUser> findAll() {
        Connection c =null;
        List<EventUser> groupUserList = new ArrayList<>();
        try{
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

            String sql = "SELECT userid, event FROM \"EventUser\"";
            PreparedStatement preparedStatement =c.prepareStatement(sql);
            ResultSet result = preparedStatement.executeQuery();
            while(result.next()){
                String idUser = result.getString("user");
                Long idEvent= result.getLong("event");
                groupUserList.add( new EventUser(idUser, idEvent));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //System.out.println(groupUserList);
        return groupUserList;
    }

    @Override
    public EventUser save(EventUser eventUser) {
        EventUser temp = findOne(eventUser.getId());
        if(temp==null) {
            Connection c =null;
            try{
                Class.forName("org.postgresql.Driver");
                c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

                String sql = "INSERT INTO \"EventUser\"(userid, event) VALUES(?, ?)";
                PreparedStatement preparedStatement =c.prepareStatement(sql);
                preparedStatement.setString(1, eventUser.getId().getLeft());
                preparedStatement.setLong(2, eventUser.getId().getRight());
                preparedStatement.executeUpdate();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return temp;
    }

    @Override
    public EventUser delete(Tuple<String, Long> id) {
        EventUser eventUser = findOne(id);
        if(eventUser!=null){
            Connection c =null;
            try{
                Class.forName("org.postgresql.Driver");
                c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

                String sql = "DELETE FROM \"EventUser\" WHERE userid=? AND event=?";
                PreparedStatement preparedStatement =c.prepareStatement(sql);
                preparedStatement.setString(1, id.getLeft());
                preparedStatement.setLong(2, id.getRight());
                preparedStatement.executeUpdate();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return eventUser;
    }
}
