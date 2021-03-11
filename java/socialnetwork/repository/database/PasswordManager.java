package socialnetwork.repository.database;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class PasswordManager {
    private String url;
    private String name;
    private String password;

    public PasswordManager(String url, String name, String password) {
        this.url = url;
        this.name = name;
        this.password = password;
    }

    private String hash(String password){
        String hashed = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(password.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            hashed = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return hashed;
    }

    public boolean check(String user, String pass){
        String hashed = hash(pass);
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(url, name, password);
            String sql = "SELECT * FROM \"Passwords\" WHERE user_id='"+user+"' and password='"+hashed+"'";
            Statement statement = c.createStatement();
            ResultSet result = statement.executeQuery(sql);

            if(result.next()) {//daca exista o inregistrare
                return true;
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return false;
    }

    public void add(String user, String pass){
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(url, name, password);

            String sql = "INSERT INTO \"Passwords\"(user_id, password) VALUES(?, ?)";
            PreparedStatement preparedStatement = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, user);
            preparedStatement.setString(2, hash(pass));
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            //TODO de modificat
            exception.printStackTrace();
        }
    }
}
