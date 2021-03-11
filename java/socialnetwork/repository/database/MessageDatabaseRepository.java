package socialnetwork.repository.database;

import socialnetwork.domain.Message;
import socialnetwork.domain.validators.Validator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageDatabaseRepository extends AbstractDatabaseRepository<Long, Message>{
    private Validator<Message> validator;

    public MessageDatabaseRepository(String url, String name, String password, Validator<Message> validator) {
        super(url, name, password, validator);
        this.validator = validator;
    }

    @Override
    public Message findOne(Long aLong) {
        return null;
    }

    @Override
    public Iterable<Message> findAll() {
        Connection c = null;
        List<Message> messageList = new ArrayList<>();

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

            String sql = "SELECT id, sender, idgroup, text, timestamp FROM \"Messages\" ORDER BY timestamp";
            PreparedStatement preparedStatement = c.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                Long id = resultSet.getLong("id");
                String sender = resultSet.getString("sender");
                Long idGroup = resultSet.getLong("idgroup");
                String text = resultSet.getString("text");
                LocalDateTime timestamp = resultSet.getObject("timestamp", LocalDateTime.class);
                messageList.add(new Message(id, sender, idGroup, text, timestamp));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return messageList;
    }

    public Iterable<Message> findAll(Long group) {
        Connection c = null;
        List<Message> messageList = new ArrayList<>();

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

            String sql = "SELECT id, sender, idgroup, text, timestamp FROM \"Messages\" where idgroup = ? ORDER BY timestamp";
            PreparedStatement preparedStatement = c.prepareStatement(sql);
            preparedStatement.setLong(1, group);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                Long id = resultSet.getLong("id");
                String sender = resultSet.getString("sender");
                Long idGroup = resultSet.getLong("idgroup");
                String text = resultSet.getString("text");
                LocalDateTime timestamp = resultSet.getObject("timestamp", LocalDateTime.class);
                messageList.add(new Message(id, sender, idGroup, text, timestamp));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return messageList;
    }


    @Override
    public Message save(Message message) {
        Connection c = null;
        try{
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

            String sql = "INSERT INTO \"Messages\"(sender, idgroup, text, timestamp) VALUES(?,?,?,?) RETURNING id";
            PreparedStatement preparedStatement = c.prepareStatement(sql);
            preparedStatement.setString(1, message.getSender());
            preparedStatement.setLong(2, message.getGroupId());
            preparedStatement.setString(3, message.getText());
            preparedStatement.setObject(4, message.getTimeStamp());
            ResultSet result =  preparedStatement.executeQuery();
            if(result.next())
                return message;

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message delete(Long aLong) {
        return null;
    }
}
