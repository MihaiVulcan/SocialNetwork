package socialnetwork.repository.database;

import socialnetwork.domain.Group;
import socialnetwork.domain.validators.Validator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GroupDatabaseRepository extends AbstractDatabaseRepository<Long, Group>{

    public GroupDatabaseRepository(String url, String name, String password, Validator<Group> validator) {
        super(url, name, password, validator);
    }

    @Override
    public Group findOne(Long id) {
        Connection c = null;
        try{
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

            String sql = "SELECT name, description From \"Groups\" WHERE id=?";
            PreparedStatement preparedStatement = c.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet result = preparedStatement.executeQuery();
            if(result.next()){
                String name = result.getString("name");
                String description = result.getString("description");
                Group group = new Group(id, name, description);
                return group;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Group> findAll() {
        Connection c = null;
        try{
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

            String sql = "SELECT id, name, description From \"Groups\"";
            PreparedStatement preparedStatement = c.prepareStatement(sql);

            ResultSet result = preparedStatement.executeQuery();
            List<Group> groups = new ArrayList<>();
            while(result.next()){
                Long id = result.getLong("id");
                String name = result.getString("name");
                String description = result.getString("description");
                Group group = new Group(id, name, description);
                groups.add(group);
            }
            return groups;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Group save(Group group) {
        Connection c = null;
        try{
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

            String sql = "INSERT INTO \"Groups\"(name, description) VALUES(?, ?) RETURNING id";
            PreparedStatement preparedStatement = c.prepareStatement(sql);

            preparedStatement.setString(1, group.getName());
            preparedStatement.setString(2, group.getDescription());

            ResultSet result = preparedStatement.executeQuery();
            if(result.next()){
                Long id = result.getLong("id");
                group.setId(id);
                return group;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Group delete(Long id) {
        Group temp = findOne(id);
        if(temp != null){
            Connection c = null;
            try{
                Class.forName("org.postgresql.Driver");
                c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

                String sql = "DELETE FROM \"Groups\" WHERE id=?";
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
