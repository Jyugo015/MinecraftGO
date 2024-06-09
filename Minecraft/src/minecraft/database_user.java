package minecraft;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class database_user {
    public static Connection getConnection() throws SQLException{
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/minecraft";
        String username = "root";
        String password = "urpw";
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found for driver", e);
        }
        Connection connection = DriverManager.getConnection(url, username, password);
        return connection;
    }

    public static boolean addUser(User newUser){
        try {
            Connection connection = getConnection();
            String statement = "INSERT INTO userlist (username, passcode, email) VALUES (?,?,?)";
            PreparedStatement insert = connection.prepareStatement(statement);
            insert.setString(1, newUser.getUsername());
            insert.setString(2, newUser.getHashedPassword());
            insert.setString(3, newUser.getEmail());
            insert.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static User getUserByEmail(String email) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT * FROM userlist WHERE email = ?";
        PreparedStatement check = connection.prepareStatement(statement);
        check.setString(1, email);
        ResultSet result = check.executeQuery();
        if (result.next()){
            return new User(result.getString("username"), 
                            result.getString("email"), result.getString("passcode"));
        }
        return null;
    }

    public static User getUserByUsername(String username) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT * FROM userlist WHERE username =?";
        PreparedStatement check = connection.prepareStatement(statement);
        check.setString(1, username);
        ResultSet result = check.executeQuery();
        if (result.next()){
            return new User(result.getString("username"), 
                            result.getString("email"), result.getString("passcode"));
        }
        return null;
    }

    public static void updateUserPassword(String email, String newHashedPassword) throws SQLException {
        System.out.println("update");
        Connection connection = getConnection();
        String statement = "UPDATE userlist SET passcode =? WHERE email=?";
        PreparedStatement update = connection.prepareStatement(statement);
        update.setString(1, newHashedPassword);
        update.setString(2, email);
        update.executeUpdate();
    }
}
