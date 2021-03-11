package socialnetwork.repository.database;

import socialnetwork.domain.Event;
import socialnetwork.domain.validators.Validator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventDatabaseRepository extends AbstractDatabaseRepository<Long, Event> {

    public EventDatabaseRepository(String url, String name, String password, Validator<Event> validator) {
        super(url, name, password, validator);
    }

    @Override
    public Event findOne(Long id) {
        Connection c = null;
        try{
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

            String sql = "SELECT name, description, time From \"Event\" WHERE id=?";
            PreparedStatement preparedStatement = c.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet result = preparedStatement.executeQuery();
            if(result.next()){
                String name = result.getString("name");
                String description = result.getString("description");
                LocalDateTime time = result.getObject("time", LocalDateTime.class);
                return new Event(id, name, description, time);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Event> findAll() {
        Connection c = null;
        try{
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

            String sql = "SELECT id, name, description, time From \"Event\" where time>current_timestamp ";
            PreparedStatement preparedStatement = c.prepareStatement(sql);

            ResultSet result = preparedStatement.executeQuery();
            List<Event> events = new ArrayList<>();
            while(result.next()){
                Long id = result.getLong("id");
                String name = result.getString("name");
                String description = result.getString("description");
                LocalDateTime time = result.getObject("time", LocalDateTime.class);
                Event event = new Event(id, name, description, time);
                events.add(event);
            }
            return events;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Event save(Event event) {
        Connection c = null;
        try{
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

            String sql = "INSERT INTO \"Event\"(name, description, time) VALUES(?, ?, ?) RETURNING id";
            PreparedStatement preparedStatement = c.prepareStatement(sql);

            preparedStatement.setString(1, event.getName());
            preparedStatement.setString(2, event.getDescription());
            preparedStatement.setObject(3, event.getTime());

            ResultSet result = preparedStatement.executeQuery();
            if(result.next()){
                Long id = result.getLong("id");
                event.setId(id);
                return event;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Event delete(Long id) {
        Event temp = findOne(id);
        if(temp != null){
            Connection c = null;
            try{
                Class.forName("org.postgresql.Driver");
                c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

                String sql = "DELETE FROM \"Event\" WHERE id=?";
                PreparedStatement preparedStatement = c.prepareStatement(sql);
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return temp;
    }
}
