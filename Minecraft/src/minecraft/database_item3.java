package minecraft;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import minecraft.Potions.Potion;

public class database_item3 {
    //item potionsatchel

    // public static void main(String[] args) throws SQLException {
    //     initialize("defaultUser");
    // }
    
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

    public static void initialize(String username) throws SQLException{
        for (Map.Entry<String, Potion> entry : new Potions().getPotionsMap().entrySet()){
            Potion potion = entry.getValue();
            addPotion(username, potion.getName(), potion.getPotency(),potion.getEffect());
            database_itemBox.addItem(username, potion.getName(), "Potion", potion.getEffect(), 1);
        }
    }

    public static void addPotion(String username, String potionName, int potency, String effect) throws SQLException{
        Connection connection = getConnection();
        String statement = "INSERT INTO potion (Username, Name, Potency, Effect) VALUES (?,?,?,?)";
        PreparedStatement insert = connection.prepareStatement(statement);
        insert.setString(1, username);
        insert.setString(2, potionName);
        insert.setInt(3, potency);
        insert.setString(4, effect);
        insert.executeUpdate();
    }

    public static void addPotionSatchel(String username, String potionName, int potency, String effect) throws SQLException{
        Connection connection = getConnection();
        String statement = "INSERT INTO potionsatchel (Username, Name, Potency, Effect) VALUES (?,?,?,?)";
        PreparedStatement insert = connection.prepareStatement(statement);
        insert.setString(1, username);
        insert.setString(2, potionName);
        insert.setInt(3, potency);
        insert.setString(4, effect);
        insert.executeUpdate();
        removePotion(username, potionName);
    }

    public static void removePotion(String username, String potionName) throws SQLException{
        try {
            Connection connection = getConnection();
            String statement = "DELETE FROM potion WHERE username = ? AND name = ?";
            PreparedStatement delete = connection.prepareStatement(statement);
            delete.setString(1, username);
            delete.setString(2, potionName);
            delete.executeUpdate();
            statement = "SELECT * FROM potion WHERE username = ? AND name =?";
            PreparedStatement check = connection.prepareStatement(statement);
            check.setString(1, username);
            check.setString(2, potionName);
            ResultSet result = check.executeQuery();
            if (result.next())
                System.out.println("not removed");
            else
                System.out.println("removed " + potionName);
        } catch (Exception e) {
            System.out.println("Failed in removePotion" + e);
        }
    }

    public static void removePotionSatchel(String username, String potionName,int potency, String effect) {
        try {
            Connection connection = getConnection();
            String statement = "DELETE FROM potionsatchel WHERE username = ? AND name = ?";
            PreparedStatement delete = connection.prepareStatement(statement);
            delete.setString(1, username);
            delete.setString(2, potionName);
            delete.executeUpdate();
            System.out.println("removed potion satchel " + potionName);
            addPotion(username, potionName, potency, effect);
            
        } catch (Exception e) {
            System.out.println("Failed in removePotionSatchel");
        }
    }

    public static void clearPotionSatchel(String username) throws SQLException{
        Connection connection = getConnection();
        String statement = "DELETE FROM potionsatchel WHERE username =?";
        PreparedStatement delete = connection.prepareStatement(statement);
        delete.setString(1, username);
        delete.executeUpdate();
    }

    public static ArrayList<Potion> retrievePotionSatchel(String username) throws SQLException{
        ArrayList<Potion> satchelPotionName = new ArrayList<Potion>();
        Connection connection = getConnection();
        String statement = "SELECT name FROM potionsatchel WHERE username =?";
        PreparedStatement retrieve = connection.prepareStatement(statement);
        retrieve.setString(1, username);
        ResultSet result = retrieve.executeQuery();
        while(result.next()){
            satchelPotionName.add(new Potion(result.getString("name")));
        }
        return satchelPotionName;
    }

    public static ArrayList<String> retrievePotion(String username) throws SQLException{
        ArrayList<String> potionName = new ArrayList<String>();
        Connection connection = getConnection();
        String statement = "SELECT name FROM potion WHERE username =?";
        PreparedStatement retrieve = connection.prepareStatement(statement);
        retrieve.setString(1, username);
        ResultSet result = retrieve.executeQuery();
        while(result.next()){
            potionName.add(result.getString("name"));
        }
        return potionName;
    }
}
