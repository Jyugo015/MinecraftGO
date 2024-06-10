package minecraft;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class database_item4 {
    public static void main(String[] args) throws SQLException{
        // database_itemBox.addItem(String username, String itemname, String type, String function, int quantitytoadd);
        //listOfType
        // "Tool"
        // "Food" 
        // "Arrows"
        // "Decorations"
        // "MobEggs"
        // "Weapons" 
        // "Armor"
        // "Materials"
        // "Transportations"
        // "Potions" nonid choose use elysia de example i add it later
        // "Records"
        // "Dyes"
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

    //return arraylist of string storing the name of items in the automated sorting chest
    //call the constructor EnderBackpackItem(itemName, "itemBox") to create instance of item to instiate with all the attribute
    public static ArrayList<String> retrieveItem(String username) throws SQLException{
        ArrayList<String> record = new ArrayList<String>();
        Connection connection = getConnection();
        String statement = "SELECT * FROM automatedSortingChest WHERE username = ?";
        PreparedStatement retrieve  = connection.prepareStatement(statement);
        retrieve.setString(1, username);
        ResultSet result = retrieve.executeQuery();
        while (result.next()){
            String name = result.getString("Name");
            record.add(name);
            System.out.println("name: " + name);
            System.out.println("type: " + retrieveType(name));
            System.out.println("function: " + retrieveFunction(name));
            System.out.println("quantity: " + retrieveQuantity(name, username));
        }   
        return record;
    }

    // can use this to retrieve the type of the item (before that, must have the record in the database table)
    public static String retrieveType(String itemName) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT Type from automatedSortingChest WHERE Name = ?";
        PreparedStatement retrieve = connection.prepareStatement(statement);
        retrieve.setString(1, itemName);
        ResultSet result = retrieve.executeQuery();
        if (result.next()){
            System.out.println("Type: " + result.getString("Type"));
            return result.getString("Type");
        }
            
        return null;
    }

    //get the function of item 
    public static String retrieveFunction(String itemName) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT Functions from automatedSortingChest WHERE Name = ?";
        PreparedStatement retrieve = connection.prepareStatement(statement);
        retrieve.setString(1, itemName);
        ResultSet result = retrieve.executeQuery();
        if (result.next())
            return result.getString("Functions");
        return null;
    }

    //get the quantity of item 
    public static int retrieveQuantity(String itemName, String username) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT Quantity from automatedSortingChest WHERE Name = ? AND username = ?";
        PreparedStatement retrieve = connection.prepareStatement(statement);
        retrieve.setString(1, itemName);
        retrieve.setString(2, username);
        ResultSet result = retrieve.executeQuery();
        if (result.next())
            return result.getInt("Quantity");
        return 0;
    }

    //check quantity in itemBox enough to put into sorting chest 
    //database_itemBox.retrieveQuantity(String itemName, String username)
    public static void addItem(String username, String itemName, int quantityToAdd) throws SQLException{
        addItem(username, itemName, database_itemBox.retrieveType(itemName), database_itemBox.retrieveFunction(itemName), quantityToAdd);
    }

    public static void addItem(String username, String itemname, String type, String function, 
    int quantitytoadd) throws SQLException{   
        Connection connection = getConnection();
        String statement = "SELECT Quantity FROM automatedSortingChest WHERE username = ? AND Name = ?";
        PreparedStatement check = connection.prepareStatement(statement);
        check.setString(1, username);
        check.setString(2,itemname);
        ResultSet result = check.executeQuery();
        if (result.next()){
            int itemquantity = result.getInt("Quantity") + quantitytoadd;
            statement = "UPDATE automatedSortingChest SET Quantity = ? WHERE username = ? AND Name =?";
            PreparedStatement update = connection.prepareStatement(statement);
            update.setInt(1, itemquantity);
            update.setString(2,username);
            update.setString(3, itemname);
            update.executeUpdate();
        }
        else{
            statement = "INSERT INTO automatedSortingChest(username, name, type, Functions, quantity)" 
                        + "VALUES (?,?,?,?,?)";
            PreparedStatement insert = connection.prepareStatement(statement);
            insert.setString(1,username);
            insert.setString(2,itemname);
            insert.setString(3,type);
            insert.setString(4, function);
            insert.setInt(5,quantitytoadd);
            insert.executeUpdate();
        }
        database_itemBox.removeItem(itemname, username, quantitytoadd);
        //update the database of itemBox oso, as the item is removed from itemBox then added to auotmatedsortingchest
    }    
    
    // check quantity in sorting chest enough 
    //database_item4.retrieveQuantity(itemname, username)
    public static void removeItem(String itemName, String username, int quantitytoreduce) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT Quantity FROM automatedSortingChest WHERE username = ? AND Name = ?";
        PreparedStatement check = connection.prepareStatement(statement);
        check.setString(1, username);
        check.setString(2,itemName);
        ResultSet result = check.executeQuery();
        int quantity;
        if (result.next()){
            quantity = result.getInt("Quantity") - quantitytoreduce;
            database_itemBox.addItem(username, itemName, retrieveType(itemName), retrieveFunction(itemName), quantitytoreduce);
            if (quantity<=0){
                statement = "DELETE FROM automatedSortingChest WHERE username = ? AND Name = ? ";
                PreparedStatement delete = connection.prepareStatement(statement);
                delete.setString(1, username);
                delete.setString(2,itemName);
                delete.executeUpdate();
            }
            else{
                statement = "UPDATE automatedSortingChest SET Quantity = ? WHERE username = ? AND Name =?";
                PreparedStatement update = connection.prepareStatement(statement);
                update.setInt(1, quantity);
                update.setString(2,username);
                update.setString(3, itemName);
                update.executeUpdate();
            }
        }
        else
            System.out.println("The automated sorting chest does not containg this particular item");
    }
    
    public static EnderBackpackItem getEnderBackpackItem(String username, String itemName) throws SQLException {
        return new EnderBackpackItem(itemName, retrieveType(itemName), retrieveFunction(itemName), retrieveQuantity(itemName, username));
    }
}
  