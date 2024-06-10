package minecraft;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class database_item8 {
    // public static void main(String[] args) throws SQLException {
    //     initialize("defaultUser");
    // }
    
    public static Connection getConnection() throws SQLException{
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/minecraft";
        String username = "root";
        String password = "dbqLb1234!";
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found for driver", e);
        }
        Connection connection = DriverManager.getConnection(url, username, password);
        return connection;
    }
    
    public static void initialize(String username) throws SQLException{
        AdventurerDiary diary = new AdventurerDiary(username);
        diary.logEvent("Player joined the game");
        diary.logEvent("Achievement earned: DIAMONDS!");
        diary.logEvent("Discovered a village");
        diary.logEvent("Completed the Ender Dragon challenge");
    }

    public static void initializeSignup (String username) throws SQLException{
        AdventurerDiary diary = new AdventurerDiary(username);
        diary.logEvent("Player joined the game");
    }

    public static Map<Integer, String> retrieveEntries(String username) throws SQLException {
        Map<Integer, String> entries = new HashMap<Integer, String>();
        Connection connection = getConnection();
        String statement = "SELECT * FROM adventurerDiary WHERE username =?";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1, username);
        ResultSet result = select.executeQuery();
        while (result.next()){
            entries.put(result.getInt("entryId"), result.getString("Event"));
        }
        return entries;
    }

    public static void addEntry(String username, int entryID, String entry) throws SQLException {
        Connection connection = getConnection();
        String statement = "INSERT INTO adventurerdiary (username, entryID, event) VALUES (?,?,?)";
        PreparedStatement insert = connection.prepareStatement(statement);
        insert.setString(1, username);
        insert.setInt(2, entryID);
        insert.setString(3, entry);
        insert.executeUpdate();
    }

    public static void editEntry(String username, int entryID, String newEntry) throws SQLException{
        Connection connection= getConnection();
        String statement = "UPDATE adventurerdiary SET Event =? WHERE username = ? AND entryID = ?";
        PreparedStatement update = connection.prepareStatement(statement);
        update.setString(1, newEntry);
        update.setString(2, username);
        update.setInt(3, entryID);
        update.executeUpdate();
    }

    public static void deleteEntry(String username, int entryID) throws SQLException{
        Connection connection = getConnection();
        String statement = "DELETE FROM adventurerdiary WHERE username =? and entryID = ?";
        PreparedStatement delete = connection.prepareStatement(statement);
        delete.setString(1, username);
        delete.setInt(2, entryID);
        delete.executeUpdate();
    }
}
