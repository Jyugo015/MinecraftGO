package minecraft;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class database_itemBox {
    //item in user's box
    public static void main(String[] args) throws SQLException{
        // addItem("defaultUser", "None", "None", "None",0);
        // addItem("defaultUser", "axe", "Tool", "Planting Melon and Pumpkin" ,5);
        // Connection connection = getConnection();
        // String statement = "CREATE TABLE IF NOT EXISTS ItemBox"
        //     + "(ItemID INT PRIMARY KEY AUTO_INCREMENT, Username VARCHAR(255) COLLATE utf8_bin,"
        //     + "Name VARCHAR(255), Type VARCHAR(255), Functions VARCHAR(255), Quantity INT)";
        // PreparedStatement create = connection.prepareStatement(statement);
        // create.executeUpdate();
        //call when a new user is registered
    }

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

    public static ArrayList<String> retrieveItem(String user) throws SQLException{
        ArrayList<String> record = new ArrayList<String>();
        Connection connection = getConnection();
        String statement = "SELECT * FROM itemBox WHERE Username = ?";
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
        String statement = "SELECT Type from itemBox WHERE Name = ?";
        PreparedStatement retrieve = connection.prepareStatement(statement);
        retrieve.setString(1, itemName);
        ResultSet result = retrieve.executeQuery();
        if (result.next())
            return result.getString("Type");
        return null;
    }

    public static String retrieveFunction(String itemName) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT Functions from itemBox WHERE Name = ?";
        PreparedStatement retrieve = connection.prepareStatement(statement);
        retrieve.setString(1, itemName);
        ResultSet result = retrieve.executeQuery();
        if (result.next())
            return result.getString("Functions");
        return null;
    }

    public static int retrieveQuantity(String itemName, String username) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT Quantity from itemBox WHERE Name = ? AND Username = ?";
        PreparedStatement retrieve = connection.prepareStatement(statement);
        retrieve.setString(1, itemName);
        retrieve.setString(2, username);
        ResultSet result = retrieve.executeQuery();
        if (result.next())
            return result.getInt("Quantity");
        return 0;
    }

    public static void addItem(String username, String itemname, int quantitytoadd) throws SQLException{
        addItem(username, itemname, retrieveType(itemname), retrieveFunction(itemname), quantitytoadd);
    }

    public static void addItem(String username, String itemname, String type, String function, 
    int quantitytoadd) throws SQLException{   

        Connection connection = getConnection();
        String statement = "SELECT Quantity FROM itemBox WHERE Username = ? AND Name = ?";
        PreparedStatement check = connection.prepareStatement(statement);
        check.setString(1, username);
        check.setString(2,itemname);
        ResultSet result = check.executeQuery();
        int Itemquantity;
        if (result.next() && quantitytoadd>0){
            System.out.println("quantity initially: " + result.getInt("Quantity") );
            Itemquantity = result.getInt("Quantity") + quantitytoadd;
            statement = "UPDATE itemBox SET Quantity = ? WHERE Username = ? AND Name =?";
            PreparedStatement update = connection.prepareStatement(statement);
            update.setInt(1, Itemquantity);
            update.setString(2,username);
            update.setString(3, itemname);
            update.executeUpdate();
            System.out.println("databaseItemboxadditem" + database_itemBox.retrieveQuantity(itemname, username));
        }
        else if (quantitytoadd>0){
            Itemquantity = quantitytoadd;
            statement = "INSERT INTO itemBox(username, name, type, functions, quantity) VALUES (?,?,?,?,?)";
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
        String statement = "SELECT Quantity FROM itemBox WHERE Username = ? AND Name = ?";
        PreparedStatement check = connection.prepareStatement(statement);
        check.setString(1, username);
        check.setString(2,itemName);
        ResultSet result = check.executeQuery();
        int quantity;
        if (result.next()){
            quantity = result.getInt("Quantity") - quantitytoreduce;
            if (quantity<=0){
                statement = "UPDATE itemBox SET Quantity =? WHERE Username = ? AND Name = ? ";
                PreparedStatement update = connection.prepareStatement(statement);
                update.setInt(1, 0);
                update.setString(2, username);
                update.setString(3,itemName);
                update.executeUpdate();
                System.out.println("itemBoxRemoveItem " + database_itemBox.retrieveQuantity(itemName, username));
            }
            else{
                statement = "UPDATE itemBox SET Quantity = ? WHERE Username = ? AND Name =?";
                PreparedStatement update = connection.prepareStatement(statement);
                update.setInt(1, quantity);
                update.setString(2,username);
                update.setString(3, itemName);
                update.executeUpdate();
            }
        }
    }

    public static EnderBackpackItem getEnderBackpackItem(String username, String itemName) throws SQLException {
        return new EnderBackpackItem(itemName, retrieveType(itemName), retrieveFunction(itemName), retrieveQuantity(itemName, username));
    }
}
