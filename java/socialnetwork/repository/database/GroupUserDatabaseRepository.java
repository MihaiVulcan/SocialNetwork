package socialnetwork.repository.database;

import socialnetwork.domain.GroupUser;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.validators.Validator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GroupUserDatabaseRepository extends AbstractDatabaseRepository<Tuple<String, Long>, GroupUser> {

    public GroupUserDatabaseRepository(String url, String name, String password, Validator<GroupUser> validator) {
        super(url, name, password, validator);
    }

    @Override
    public GroupUser findOne(Tuple<String, Long> id) {
        Connection c =null;
        try{
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

            String sql = "SELECT iduser, idgroup FROM \"GroupUser\" WHERE iduser=? AND idgroup=?";
            PreparedStatement preparedStatement =c.prepareStatement(sql);
            preparedStatement.setString(1, id.getLeft());
            preparedStatement.setLong(2, id.getRight());
            ResultSet result = preparedStatement.executeQuery();
            if(result.next()){
                return new GroupUser(id.getLeft(), id.getRight());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<GroupUser> findAll() {
        Connection c =null;
        List<GroupUser> groupUserList = new ArrayList<>();
        try{
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

            String sql = "SELECT iduser, idgroup FROM \"GroupUser\"";
            PreparedStatement preparedStatement =c.prepareStatement(sql);
            ResultSet result = preparedStatement.executeQuery();
            while(result.next()){
                String idUser = result.getString("iduser");
                Long idGroup = result.getLong("idgroup");
                groupUserList.add( new GroupUser(idUser, idGroup));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //System.out.println(groupUserList);
        return groupUserList;
    }

    public Iterable<GroupUser> findAll(String user) {
        Connection c =null;
        List<GroupUser> groupUserList = new ArrayList<>();
        try{
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

            String sql = "SELECT iduser, idgroup FROM \"GroupUser\" where iduser = ?";
            PreparedStatement preparedStatement =c.prepareStatement(sql);
            preparedStatement.setString(1, user);
            ResultSet result = preparedStatement.executeQuery();
            while(result.next()){
                String idUser = result.getString("iduser");
                Long idGroup = result.getLong("idgroup");
                groupUserList.add( new GroupUser(idUser, idGroup));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //System.out.println(groupUserList);
        return groupUserList;
    }

    @Override
    public GroupUser save(GroupUser groupUser) {
        GroupUser temp = findOne(groupUser.getId());
        if(temp==null) {
            Connection c =null;
            try{
                Class.forName("org.postgresql.Driver");
                c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

                String sql = "INSERT INTO \"GroupUser\"(iduser, idgroup) VALUES(?, ?)";
                PreparedStatement preparedStatement =c.prepareStatement(sql);
                preparedStatement.setString(1, groupUser.getId().getLeft());
                preparedStatement.setLong(2, groupUser.getId().getRight());
                preparedStatement.executeUpdate();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return temp;
    }

    @Override
    public GroupUser delete(Tuple<String, Long> id) {
        GroupUser groupUser = findOne(id);
        if(groupUser!=null){
            Connection c =null;
            try{
                Class.forName("org.postgresql.Driver");
                c = DriverManager.getConnection(super.getUrl(), super.getName(), super.getPassword());

                String sql = "DELETE FROM \"GroupUser\" WHERE iduser=? AND idgroup=?";
                PreparedStatement preparedStatement =c.prepareStatement(sql);
                preparedStatement.setString(1, id.getLeft());
                preparedStatement.setLong(2, id.getRight());
                preparedStatement.executeUpdate();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return groupUser;
    }
}
