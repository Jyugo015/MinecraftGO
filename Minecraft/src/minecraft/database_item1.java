package minecraft;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
public class database_item1 {

    //item enderbackpack
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
        setCapacity(50, username);//initial backpack capacity when new user registered = 50
    }

    public static ArrayList<String> retrieveItem(String user) throws SQLException{
        ArrayList<String> record = new ArrayList<String>();
        Connection connection = getConnection();
        String statement = "SELECT * FROM itemBackpack WHERE Username = ?";
        PreparedStatement retrieve  = connection.prepareStatement(statement);
        retrieve.setString(1, user);
        ResultSet result = retrieve.executeQuery();
        while (result.next()){
            record.add(result.getString("Name"));
        }   
        return record;
    }

    public static String retrieveType(String itemName) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT Type from itemBackpack WHERE Name = ?";
        PreparedStatement retrieve = connection.prepareStatement(statement);
        retrieve.setString(1, itemName);
        ResultSet result = retrieve.executeQuery();
        if (result.next())
            return result.getString("Type");
        return null;
    }

    public static String retrieveFunction(String itemName) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT Functions from itemBackpack WHERE Name = ?";
        PreparedStatement retrieve = connection.prepareStatement(statement);
        retrieve.setString(1, itemName);
        ResultSet result = retrieve.executeQuery();
        if (result.next())
            return result.getString("Functions");
        return null;
    }

    public static int retrieveQuantity(String itemName, String username) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT Quantity from itemBackpack WHERE Name = ? AND Username = ?";
        PreparedStatement retrieve = connection.prepareStatement(statement);
        retrieve.setString(1, itemName);
        retrieve.setString(2, username);
        ResultSet result = retrieve.executeQuery();
        if (result.next())
            return result.getInt("Quantity");
        return 0;
    }

    public static void addItem(String username, String itemName, int quantitytoadd) throws SQLException{
        addItem(username, itemName, database_itemBox.retrieveType(itemName), 
                database_itemBox.retrieveFunction(itemName), quantitytoadd);
    }

    public static void addItem(String username, String itemname, String type, String function, 
    int quantitytoadd) throws SQLException{   
        Connection connection = getConnection();
        String statement = "SELECT Quantity FROM itemBackpack WHERE Username = ? AND Name = ?";
        PreparedStatement check = connection.prepareStatement(statement);
        check.setString(1, username);
        check.setString(2,itemname);
        ResultSet result = check.executeQuery();
        int Itemquantity;
        if (result.next()){
            Itemquantity = result.getInt("Quantity") + quantitytoadd;
            statement = "UPDATE itemBackpack SET Quantity = ? WHERE Username = ? AND Name =?";
            PreparedStatement update = connection.prepareStatement(statement);
            update.setInt(1, Itemquantity);
            update.setString(2,username);
            update.setString(3, itemname);
            update.executeUpdate();
        }
        else{
            Itemquantity = quantitytoadd;
            statement = "INSERT INTO itemBackpack(username, name, type, functions, quantity) VALUES (?,?,?,?,?)";
            PreparedStatement insert = connection.prepareStatement(statement);
            insert.setString(1,username);
            insert.setString(2,itemname);
            insert.setString(3,type);
            insert.setString(4, function);
            insert.setInt(5,quantitytoadd);
            insert.executeUpdate();
        }
    }

    public static void removeItem(String itemName, String username, int quantitytoreduce) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT Quantity FROM itemBackpack WHERE Username = ? AND Name = ?";
        PreparedStatement check = connection.prepareStatement(statement);
        check.setString(1, username);
        check.setString(2,itemName);
        ResultSet result = check.executeQuery();
        int quantity;
        if (result.next()){
            quantity = result.getInt("Quantity") - quantitytoreduce;
            if (quantity==0){
                statement = "DELETE FROM itemBackpack WHERE Username = ? AND Name = ? ";
                PreparedStatement delete = connection.prepareStatement(statement);
                delete.setString(1, username);
                delete.setString(2,itemName);
                delete.executeUpdate();
            }
            else{
                statement = "UPDATE itemBackpack SET Quantity = ? WHERE Username = ? AND Name =?";
                PreparedStatement update = connection.prepareStatement(statement);
                update.setInt(1, quantity);
                update.setString(2,username);
                update.setString(3, itemName);
                update.executeUpdate();
            }
        }
    }

    public static int getCapacity(String username) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT capacity FROM backpackcapacity WHERE Username = ?";
        PreparedStatement search = connection.prepareStatement(statement);
        search.setString(1, username);
        ResultSet result = search.executeQuery();
        if (result.next()){
            return result.getInt("Capacity");
        }
        return 0;
    }

    public static void setCapacity(int capacity, String username) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT capacity FROM backpackcapacity WHERE Username = ?";
        PreparedStatement search = connection.prepareStatement(statement);
        search.setString(1, username);
        ResultSet result = search.executeQuery();
        if (result.next()){
            statement = "UPDATE backpackcapacity SET Capacity = ? WHERE Username = ?";
            PreparedStatement update = connection.prepareStatement(statement);
            update.setInt(1, capacity);
            update.setString(2,username);
            update.executeUpdate();
        }
        else{
            statement = "INSERT INTO backpackcapacity(Username, Capacity) VALUES (?,?) ";
            PreparedStatement insert = connection.prepareStatement(statement);
            insert.setString(1,username);
            insert.setInt(2,capacity);
            insert.executeUpdate();
        }
    }
}
