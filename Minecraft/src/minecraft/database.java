package minecraft;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
// import java.util.Map;
// import java.util.ArrayList;
// import java.util.LinkedHashMap;

public class database<E> {
    public static void main(String[] args){
        try {
            // Establish a JDBC connection
            // getConnection();
//            deleteTable();
            createTable();
            // deleteResult("defaultUser");
            // resetIndex();
            // alterTable();

            // // Close the connection
            // connection.close();
            // deleteTable();
            } 
        catch (Exception e) {
            e.printStackTrace();
        }    
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
        System.out.println("Connected");
        return connection;
    }

    public static void createTable() throws SQLException{
            Connection connection = getConnection();
            
            String statement = "CREATE TABLE IF NOT EXISTS ItemBox"
            + "(ItemID INT PRIMARY KEY AUTO_INCREMENT, Username VARCHAR(255) COLLATE utf8_bin,"
            + "Name VARCHAR(255), Type VARCHAR(255), Functions VARCHAR(255), Quantity INT)";
            PreparedStatement create = connection.prepareStatement(statement);
            
            statement = "CREATE TABLE IF NOT EXISTS ItemBackpack"
                    + "(ItemID INT PRIMARY KEY AUTO_INCREMENT, Username VARCHAR(255) COLLATE utf8_bin,"
                    + "Name VARCHAR(255), Type VARCHAR(255), Functions VARCHAR(255), Quantity INT)";
            PreparedStatement create2 = connection.prepareStatement(statement);
            
            statement = "CREATE TABLE IF NOT EXISTS ToolList"
                    + "(ToolID INT PRIMARY KEY AUTO_INCREMENT, Username VARCHAR(255) COLLATE utf8_bin,"
                    + "Name VARCHAR(255), Type VARCHAR(255), Functions VARCHAR(255), Grade INT)";
            PreparedStatement create3 = connection.prepareStatement(statement);
            
            statement = "CREATE TABLE IF NOT EXISTS MultiTool"
                    + "(ToolID INT PRIMARY KEY AUTO_INCREMENT, Username VARCHAR(255) COLLATE utf8_bin," 
                    + "Name VARCHAR(255), Type VARCHAR(255), Functions VARCHAR(255), Grade INT)";
            PreparedStatement create4 = connection.prepareStatement(statement);
            
            statement = "CREATE TABLE IF NOT EXISTS Potion"
                    + "(PotionID INT PRIMARY KEY AUTO_INCREMENT, Username VARCHAR(255) COLLATE utf8_bin,"
                    + "Name VARCHAR(255), Potency INT, Effect VARCHAR(255))";
            PreparedStatement create5 = connection.prepareStatement(statement);
            
            statement = "CREATE TABLE IF NOT EXISTS PotionSatchel"
                    + "(PotionID INT PRIMARY KEY AUTO_INCREMENT, Username VARCHAR(255) COLLATE utf8_bin,"
                    + "Name VARCHAR(255), Effect VARCHAR(255), NextPotion VARCHAR(255))";
            PreparedStatement create6 = connection.prepareStatement(statement);
            
            statement = "CREATE TABLE IF NOT EXISTS AutoFarmScheduling"
                    + "(TaskID INT PRIMARY KEY AUTO_INCREMENT, Username VARCHAR(255) COLLATE utf8_bin, "
                    + "Task VARCHAR(255), CropUsed VARCHAR(255), Duration INT, " 
                    + "GrowthStage INT, Status FLOAT)";
            PreparedStatement create7 = connection.prepareStatement(statement);

            statement = "CREATE TABLE IF NOT EXISTS crop"
                    + "(CropID INT PRIMARY KEY AUTO_INCREMENT, Username VARCHAR(255) COLLATE utf8_bin," 
                    + "CropName VARCHAR(255), ToolNeeded VARCHAR(255), QuantityCrop INT, QuantitySeed INT, NumGrowthStages INT, MinSeedYield INT, "  
                    + "MaxSeedYield INT, MinCropYield INT, MaxCropYield INT)";
            PreparedStatement create8 = connection.prepareStatement(statement);

            statement = "CREATE TABLE IF NOT EXISTS SecureChestSetting"
                    + "(ChestID INT PRIMARY KEY AUTO_INCREMENT, ChestName VARCHAR(255),"
                    + "SecurityLevel VARCHAR(255), Owner VARCHAR(255) COLLATE utf8_bin," 
                    + "ApprovedUser VARCHAR(255) COLLATE utf8_bin, PermissionType INT)";
            PreparedStatement create9 = connection.prepareStatement(statement);

            statement = "CREATE TABLE IF NOT EXISTS SecureChestRequest"
                    + "(ChestID INT PRIMARY KEY AUTO_INCREMENT, ChestName VARCHAR(255)," 
                    + "Owner VARCHAR(255) COLLATE utf8_bin, Requestor VARCHAR(255) COLLATE utf8_bin," 
                    + "Groupp VARCHAR(255) COLLATE utf8_bin, RequestStatus VARCHAR(255),"
                    + "Purpose VARCHAR(255))";
                    
            PreparedStatement create10 = connection.prepareStatement(statement);

            statement = "CREATE TABLE IF NOT EXISTS BackpackCapacity"
                    + "(BackpackID INT PRIMARY KEY AUTO_INCREMENT, Username VARCHAR(255)," 
                    + "Capacity INT)";
                    
            PreparedStatement create11 = connection.prepareStatement(statement);

            statement = "CREATE TABLE IF NOT EXISTS userlist"
            + "(UserID INT PRIMARY KEY AUTO_INCREMENT, Username VARCHAR(255) COLLATE utf8_bin," 
            + "Passcode VARCHAR(255) COLLATE utf8_bin)";
                    
            PreparedStatement create12 = connection.prepareStatement(statement);

            
            statement = "CREATE TABLE IF NOT EXISTS teleportationPoint"
                    + "(PointID INT PRIMARY KEY AUTO_INCREMENT, Username VARCHAR(255) COLLATE utf8_bin,"
                    + " Name VARCHAR(255), Neighbour VARCHAR(255), CurrentPoint VARCHAR(255), " 
                    + "x_coordinate FLOAT, y_coordinate FLOAT, imageFilePath VARCHAR(255), RequestSent VARCHAR(255), RequestGet VARCHAR(255))";        
            PreparedStatement create13 = connection.prepareStatement(statement);

            statement = "CREATE TABLE IF NOT EXISTS automatedSortingChest"
                + "(ItemID INT PRIMARY KEY AUTO_INCREMENT, Username VARCHAR(255) COLLATE utf8_bin,"
                + "Name VARCHAR(255), Type VARCHAR(255), Functions VARCHAR(255), Quantity INT)";      
            PreparedStatement create14 = connection.prepareStatement(statement);

            statement = "CREATE TABLE IF NOT EXISTS secureChest"
                + "(ItemID INT PRIMARY KEY AUTO_INCREMENT, Username VARCHAR(255) COLLATE utf8_bin,"
                + "Name VARCHAR(255), Type VARCHAR(255), Functions VARCHAR(255), Quantity INT)";      
            PreparedStatement create15 = connection.prepareStatement(statement);

            create.executeUpdate();
            create2.executeUpdate();
            create3.executeUpdate();
            create4.executeUpdate();
            create5.executeUpdate();
            create6.executeUpdate();
            create7.executeUpdate();
            create8.executeUpdate();
            create9.executeUpdate();
            create10.executeUpdate();
            create11.executeUpdate();
            create12.executeUpdate();
            create13.executeUpdate();
            create14.executeUpdate();
            create15.executeUpdate();
    }

    // private class readDatabase{
    //     public String statement;
    //     Connection connection;
    //     readDatabase(String user, String password) throws Exception{
    //         try{
    //             connection = getConnection();
    //             statement = "SELECT * FROM userlist WHERE Username = '"+user+"' AND Password = '"+password+"'";
    //         }
    //         catch(Exception e){
    //             System.out.println(e);
    //         }
    //     }

    //     public boolean read() throws Exception{
    //         try{
    //             PreparedStatement search = this.connection.prepareStatement(this.statement);
    //             ResultSet result = search.executeQuery();
    //             return result.next();
    //         }
    //         catch(Exception e){
    //             System.out.println(e);
    //         }
    //         return false;    
    //     }
    // }
    public boolean readDatabase(String user, String password) throws SQLException{
            Connection connection = getConnection();
            String statement;
            //readDatabase(username, password)
            if (user!=null && password!=null)
                statement = "SELECT * FROM userlist WHERE Username = '"+user+"' AND Password = '"+password+"'";
            //readDatabase(username, null)
            else if(password==null)
                statement = "SELECT * FROM userList WHERE Username = '"+user+"'";
            //readDatabase(null, password)
            else 
                statement = "SELECT * FROM userList WHERE Password = '"+password+"'";
            PreparedStatement search = connection.prepareStatement(statement);
            ResultSet result = search.executeQuery();
            return result.next();
    }

    public void insertData(String a, String b, String c, String d) throws SQLException{
            Connection connection = getConnection();
            String statement ="";
            int frequency=1;
            switch(a){
                //insertData("userlist", username, password, null)
                case "userlist":
                    statement = "SELECT * FROM userlist WHERE Username ='"+b+"'";
                    PreparedStatement check = connection.prepareStatement(statement);
                    ResultSet result = check.executeQuery();
                    if (result.next())
                        return;
                    else 
                        statement = "INSERT INTO userlist (Username, Password) VALUES ('"+b+"','"+c+"')";
                    break;
                
                //insertData("result", username, Double.toString(wpm), Double.toString(accuracy))
                case "result":
                    statement="INSERT INTO result (Username,Wpm, Accuracy) VALUES ('"+b+"',?,?)";
                    break;
                    
                //insertData("misspelled", username, word, null)
                case "misspelled":
                    statement = "SELECT * FROM misspelled WHERE Username ='"+b+"' AND Word ='"+c+"'";
                    PreparedStatement check1 = connection.prepareStatement(statement);
                    ResultSet result1 = check1.executeQuery();
                    if (result1.next()){
                        frequency = result1.getInt("Frequency") +1;
                        statement = "UPDATE misspelled SET Frequency= ? WHERE Username ='"+b+"' AND Word ='"+c+"'";
                    }
                    else
                        statement="INSERT INTO misspelled (Username,Word, Frequency) VALUES ('"+b+"','"+c+"', ?)";
                    break;
                
                //insertData("suddenDeath", username, Double.toString(suddenDeathScore), null)
                case "suddenDeath":
                    statement = "INSERT INTO suddenDeath (Username,Score) VALUES (?,?)";
                    break;
            }            
            PreparedStatement insert = connection.prepareStatement(statement);
            if (a.equals("result")){
                insert.setFloat(1,Float.parseFloat(c));
                insert.setFloat(2,Float.parseFloat(d));
            }
            else if (a.equals("suddenDeath")){
                insert.setString(1,b);
                insert.setFloat(2,Float.parseFloat(c));
            }
            else if(a.equals("misspelled"))
                insert.setInt(1,frequency);
            insert.executeUpdate();
        
    }
    
    public int NumOfRecord(String tableName, String username, String columnName) throws SQLException{
        int record=0;
            Connection connection = getConnection();
            String statement = "SELECT COUNT(?) AS count FROM ? WHERE Username =?";
            PreparedStatement count = connection.prepareStatement(statement);
            count.setString(1, columnName);
            count.setString(2, tableName);
            count.setString(3, username);
            ResultSet result = count.executeQuery();
            if (result.next()){
                return result.getInt("count");
            }
        return record;
    }

    public int NumOfUniqueRecord(String tableName, String username, String columnName) throws SQLException{
        int record=0;
            Connection connection = getConnection();
            String statement = "SELECT COUNT(DISTINCT ?) AS count FROM ? WHERE Username =? ";
            PreparedStatement count = connection.prepareStatement(statement);
            count.setString(1, columnName);
            count.setString(2, tableName);
            count.setString(3, username);
            ResultSet result = count.executeQuery();
            if (result.next()){
                return result.getInt("count");
            }
        return record;
    }

    public int maxValue(String tableName, String username, String columnName) throws SQLException{
        int max = 0;
            Connection connection = getConnection();
            String statement = "SELECT MAX(?) AS max FROM ? WHERE Username =? ";
            PreparedStatement count = connection.prepareStatement(statement);
            count.setString(1, columnName);
            count.setString(2, tableName);
            count.setString(3, username);
            ResultSet result = count.executeQuery();
            if (result.next()){
                return result.getInt("max");
            }
        return max;
    }

    public int minValue(String tableName, String username, String columnName) throws SQLException{
        int min = 0;//cincai give one datatype one, later will change according to each database 
            Connection connection = getConnection();
            String statement = "SELECT MIN(?) AS min FROM ? WHERE Username =? ";
            PreparedStatement count = connection.prepareStatement(statement);
            count.setString(1, columnName);
            count.setString(2, tableName);
            count.setString(3, username);
            ResultSet result = count.executeQuery();
            if (result.next()){
                return result.getInt("min");
            }
        return min;
    }

    public static LinkedHashMap<String,String> sort(String mode, String username) throws SQLException{
        LinkedHashMap <String, String> selected = new LinkedHashMap<>();
        String key="", value="", statement="";
            Connection connection = getConnection();
            switch(mode) {
                //sort("leaderboard", null)
                case "leaderboard":
                    statement = "SELECT * FROM leaderboard ORDER BY AvWpmLast10 DESC LIMIT 10";
                    key = "Username";
                    value = "AvWpmLast10";
                    break;
                
                //sort("misspelled", username)
                case "misspelled":
                    statement = "SELECT * FROM misspelled WHERE Username = '"+username+"' ORDER BY Frequency DESC LIMIT 10";
                    key = "Word";
                    value = "Frequency";
                    break;
                
                //sort("suddenDeath", username)
                case "suddenDeath":
                    statement = "CREATE TEMPORARY TABLE temp AS SELECT Username, Score FROM suddenDeath WHERE Username = '"+username+"'";
                    PreparedStatement select = connection.prepareStatement(statement);
                    select.executeUpdate();
                    statement = "ALTER TABLE temp ADD id INT PRIMARY KEY AUTO_INCREMENT";
                    PreparedStatement add = connection.prepareStatement(statement);
                    add.executeUpdate();
                    key = "id";
                    value = "Score";
                    statement = "SELECT * FROM temp ORDER BY id DESC";
                    break;                
            }
            PreparedStatement sort = connection.prepareStatement(statement);
            ResultSet result = sort.executeQuery();
            while (result.next()){
                selected.put(result.getString(key),
                        String.valueOf(mode.equals("misspelled")?result.getInt(value):result.getFloat(value)));
            }                            
        return selected;
    }

    public static void resetIndex() throws SQLException{
           Connection connection = getConnection();
           String statement ="ALTER TABLE potionsatchel AUTO_INCREMENT =1";
//            ALTER TABLE `table` AUTO_INCREMENT = number
           PreparedStatement change = connection.prepareStatement(statement);
           change.executeUpdate();
   }
   
   public static void deleteUser(String username) throws SQLException{
           Connection connection = getConnection();
           String statement ="DELETE FROM userlist WHERE Username =?";
           PreparedStatement delete = connection.prepareStatement(statement);
           delete.setString(1,username);
           delete.executeUpdate();
//            String statement ="SELECT username, password FROM userlist AS a,b";
//            PreparedStatement delete = connection.prepareStatement(statement);
//            delete.setString(1,username);
//            delete.executeUpdate();
//            if (row>0){
//                System.out.println("Deleted");
//            }
   }
   
   public static void deleteResult(String username) throws SQLException{
           Connection connection = getConnection();
           String statement ="DELETE FROM crop";
           PreparedStatement delete = connection.prepareStatement(statement);
        //    delete.setString(1,username);
           delete.executeUpdate();
//            String statement ="SELECT username, password FROM userlist AS a,b";
//            PreparedStatement delete = connection.prepareStatement(statement);
//            delete.setString(1,username);
//            delete.executeUpdate();
//            if (row>0){
//                System.out.println("Deleted");
//            }
   }
    
   public static void deleteTable() throws SQLException{
           Connection connection = getConnection();
//            String statement ="DROP TABLE misspelled";
//            PreparedStatement drop = connection.prepareStatement(statement);
//            String statement ="DROP TABLE ItemBox";
//            PreparedStatement drop1 = connection.prepareStatement(statement);
            String statement ="DROP TABLE teleportationpoint";
            PreparedStatement drop2 = connection.prepareStatement(statement);
            statement ="DROP TABLE automatedSortingChest";
            PreparedStatement drop3 = connection.prepareStatement(statement);
             statement ="DROP TABLE securechest";
            PreparedStatement drop4 = connection.prepareStatement(statement);
//            drop.executeUpdate();
//            drop1.executeUpdate();
            drop2.executeUpdate();
            drop3.executeUpdate();
            drop4.executeUpdate();
   }

   public static void alterTable() throws SQLException{
        Connection connection = getConnection();
        // String statement = "ALTER TABLE teleportationpoint ADD imagefilePath VARCHAR(255)";
        // // String statement = "ALTER TABLE potionsatchel DROP COLUMN effect, DROP COLUMN nextpotion";
        // PreparedStatement addColumn = connection.prepareStatement(statement);
        // addColumn.executeUpdate();
        // // String statement = "ALTER TABLE multitool DROP COLUMN quantity";
        // String statement = "ALTER TABLE autofarmscheduling ADD (Potency INT, "
        //                     + "Effect VARCHAR(255))";
        String statement = "ALTER TABLE teleportationpoint RENAME COLUMN distanceWithNeighbour to CurrentPoint";
        // statement = "ALTER TABLE itemBox ALTER COLUMN/MODIFY COLUMN/MODIFY column_name datatype";
        PreparedStatement addcolumn = connection.prepareStatement(statement);
        addcolumn.executeUpdate();
        // statement = "ALTER TABLE autofarmresource RENAME TO Crop";
        // PreparedStatement changename = connection.prepareStatement(statement);
        // changename.executeUpdate();
   }
}
