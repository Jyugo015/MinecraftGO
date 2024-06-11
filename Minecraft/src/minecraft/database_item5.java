package minecraft;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

import java.sql.SQLException;
import java.util.Base64;

public class database_item5 {
//node : add, remove, retrieve
//neighbour: add, remove, retrieve

//item teleportation
//public static void main(String[] args) throws SQLException {
//    createTable();
//}    
//
//    public static void createTable() throws SQLException{
//        Connection connection = getConnection();
//        String statement = "CREATE TABLE IF NOT EXISTS secureChest"
//                + "(ItemID INT PRIMARY KEY AUTO_INCREMENT, Username VARCHAR(255) COLLATE utf8_bin,"
//                + "Name VARCHAR(255), Type VARCHAR(255), Functions VARCHAR(255), Quantity INT)";      
//        PreparedStatement create = connection.prepareStatement(statement);
//        create.executeUpdate();
//    }

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
    //i will set the imagefilepath null first when each of the teleportation point is created

    public static void addteleportationPoint(String teleportationPointName, String Username, float x, float y) throws SQLException{
        Connection connection = getConnection();
        String statement = "INSERT INTO teleportationPoint (Name, Username, x_coordinate, y_coordinate) VALUES (?,?,?,?)";
        PreparedStatement insert = connection.prepareStatement(statement);
        insert.setString(1, teleportationPointName);
        insert.setString(2, Username);
        insert.setFloat(3, x);
        insert.setFloat(4, y);
        insert.executeUpdate();
    }
    
    public static void removeteleportationPoint(String getPointName) throws SQLException{
        Connection connection = getConnection();
        String statement = "DELETE FROM teleportationPoint WHERE Name =?";
        PreparedStatement delete = connection.prepareStatement(statement);
        delete.setString(1, getPointName);
        delete.executeUpdate();
    }

    public static ArrayList<String> retrieveteleportationPoints(String Username) throws SQLException{
        Connection connection = getConnection();
        ArrayList<String> record = new ArrayList<String>();
        String statement = "SELECT name FROM teleportationPoint WHERE Username = ?";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1,Username);
        ResultSet result = select.executeQuery();
        while (result.next()){
            record.add(result.getString("name"));
        }
        return record;
    }

    public static void addNeighbour(String teleportationPointName, String neighbourPointName) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT Neighbour FROM teleportationPoint WHERE Name = ?";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1 , teleportationPointName);
        ResultSet result = select.executeQuery();
        if (result.next()){
            String neighbour = result.getString("neighbour");
            if (neighbour==null || neighbour.equals("")) {
                neighbour = neighbourPointName;
            } else {
                ArrayList<String> arralist = new ArrayList<>(Arrays.asList(neighbour.split(",")));
                if (! arralist.contains(neighbourPointName)) {
                    neighbour += ("," + neighbourPointName);
                }
            }
            
            System.out.println("Neighbour of " + teleportationPointName + ": " + neighbour);
            statement = "UPDATE teleportationPoint SET neighbour = ? WHERE Name = ?";
            PreparedStatement update = connection.prepareStatement(statement);
            update.setString(1, neighbour);
            update.setString(2, teleportationPointName);
            update.executeUpdate();
        }
        System.out.println(retrieveNeighbour(teleportationPointName));
    }

    public static void removeNeighbour(String teleportationPointName, String neighbourPointName) throws SQLException{
        Connection connection= getConnection();
        String statement = "SELECT Neighbour FROM teleportationPoint WHERE Name = ?";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1 , teleportationPointName);
        ResultSet result = select.executeQuery();
        if (result.next()){
            String[] neighbour = result.getString("neighbour").split(",");
            ArrayList<String> neighbourlist = new ArrayList<String>(Arrays.asList(neighbour));
            neighbourlist.remove(neighbourPointName);
            
            String newNeighbours = "";
            for (String i:neighbourlist){
                if (newNeighbours.equals("")) {
                    newNeighbours = i;
                } else
                    newNeighbours += "," + i ;
            }
            newNeighbours = newNeighbours.substring(0,newNeighbours.length());
            statement = "UPDATE teleportationPoint SET neighbour = ? WHERE Name = ?";
            PreparedStatement update = connection.prepareStatement(statement);
            update.setString(1, newNeighbours);
            update.setString(2, teleportationPointName);
            update.executeUpdate();
        }
    }

    public static ArrayList<String> retrieveNeighbour(String teleportationPointName) throws SQLException{
        Connection connection= getConnection();
        String statement = "SELECT Neighbour FROM teleportationPoint WHERE Name = ?";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1 , teleportationPointName);
        ResultSet result = select.executeQuery();
        if  (result.next()){
            String r= result.getString("neighbour");
            if (r != null&& ! r.equals("")) {
                String[] neighbour = r.split(",");
                ArrayList<String> neighbourlist = new ArrayList<String>(Arrays.asList(neighbour));
                return neighbourlist;
            }
        }
        return null;
    }

    public static void addRequestSent(String teleportationPointName, String sentPointName) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT RequestSent FROM teleportationPoint WHERE Name = ?";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1 , teleportationPointName);
        ResultSet result = select.executeQuery();
        if (result.next()){
            String sent = result.getString("RequestSent");
            if (sent == null || sent.equals("")) {
                sent = sentPointName;
            } else{
                String[] list = sent.split(",");
                ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(list));
                if (! arrayList.contains(sentPointName)) {
                    sent += "," + sentPointName;
                }
            }
            
            statement = "UPDATE teleportationPoint SET RequestSent = ? WHERE Name = ?";
            PreparedStatement update = connection.prepareStatement(statement);
            System.out.println("Remainign waiting accpet: " + sent);
            update.setString(1, sent);
            update.setString(2, teleportationPointName);
            update.executeUpdate();
            System.out.println(retrieveRequestSent(teleportationPointName));
            System.out.println("Request sent");
        }
    }

    public static void setRequestSent(String teleportationPointName, ArrayList<String> sentPointName) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT RequestSent FROM teleportationPoint WHERE Name = ?";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1 , teleportationPointName);
        System.out.println("telepoint" + teleportationPointName);
        ResultSet result = select.executeQuery();
        if (result.next()){
            String sent = "";
            for (String sentPointName1 : sentPointName) {
                if (sent.equals("")) {
                    sent =sentPointName1;
                } else {
                    sent += "," + sentPointName1;
                }
            }
            System.out.println("remianing waiting accpect(after removed) : " + sent);
//            sent = sent.substring(0,sent.length());
            statement = "UPDATE teleportationPoint SET RequestSent = ? WHERE Name = ?";
            PreparedStatement update = connection.prepareStatement(statement);
            update.setString(1, sent);
            update.setString(2, teleportationPointName);
            update.executeUpdate();
//            System.out.println(retrieveRequestGet(teleportationPointName));
            System.out.println("Request removed");
        }
    }

    public static ArrayList<String> retrieveRequestSent(String teleportationPointName) throws SQLException{
        Connection connection= getConnection();
        String statement = "SELECT RequestSent FROM teleportationPoint WHERE Name = ?";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1 , teleportationPointName);
        ResultSet result = select.executeQuery();
        if  (result.next()){
            String r = result.getString("RequestSent");
            if (r != null&& ! r.equals("")) {
                String[] sent = r.split(",");
                ArrayList<String> sentlist = new ArrayList<String>(Arrays.asList(sent));
                return sentlist; 
            }
        }
        return null;
    }

    public static void addRequestGet(String teleportationPointName, String getPointName) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT RequestGet FROM teleportationPoint WHERE Name = ?";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1 , teleportationPointName);
        ResultSet result = select.executeQuery();
        if (result.next()){
            String get = result.getString("RequestGet");
            if (get == null || get.equals("")) {
                get = getPointName;
            } else{
                String[] list = get.split(",");
                ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(list));
                if (! arrayList.contains(getPointName)) {
                    get += "," + getPointName;
                }
            }
            
            statement = "UPDATE teleportationPoint SET RequestGet = ? WHERE Name = ?";
            PreparedStatement update = connection.prepareStatement(statement);
            System.out.println("Request get: " + teleportationPointName + " from " + get);
            update.setString(1, get);
            update.setString(2, teleportationPointName);
            update.executeUpdate();
            System.out.println(retrieveRequestGet(teleportationPointName));
        }
    }

    public static void setRequestGet(String teleportationPointName, ArrayList<String> getPointName) throws SQLException{
        Connection connection= getConnection();
        String statement = "SELECT RequestGet FROM teleportationPoint WHERE Name = ?";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1 , teleportationPointName);
        ResultSet result = select.executeQuery();
        if (result.next()){
            String get = "";
            for (String pointName : getPointName) {
                if (get.equals("")) {
                    get = pointName;
                } else {
                    get += "," + pointName;
                }
            }
//            newGets = newGets.substring(0,newGets.length());
            statement = "UPDATE teleportationPoint SET RequestGet = ? WHERE Name = ?";
            PreparedStatement update = connection.prepareStatement(statement);
            System.out.println("Request get remainded: " + get + " from " + teleportationPointName);
            update.setString(1, get);
            update.setString(2, teleportationPointName);
            update.executeUpdate();
//            System.out.println(retrieveRequestGet(teleportationPointName));
        }
    }

    public static ArrayList<String> retrieveRequestGet(String teleportationPointName) throws SQLException{
        Connection connection= getConnection();
        String statement = "SELECT RequestGet FROM teleportationPoint WHERE Name = ?";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1 , teleportationPointName);
        ResultSet result = select.executeQuery();
        if  (result.next()){
            String r = result.getString("RequestGet");
            if (r!=null && ! r.equals("")) {
                String[] get = r.split(",");
                ArrayList<String> getlist = new ArrayList<String>(Arrays.asList(get));
                return getlist;
            }
        }
        return null;
    }
    
    public static String getCurrentPoint(String Username) throws SQLException{
        Connection connection= getConnection();
        String statement = "SELECT currentPoint FROM teleportationPoint WHERE Username = ?";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1 , Username);
        ResultSet result = select.executeQuery();
        if  (result.next()){
            return result.getString("currentPoint");
        }
        return null;
    }

    public static void setCurrentPoint(String Username, String teleportationPoint) throws SQLException{
        Connection connection= getConnection();
        String statement = "UPDATE teleportationPoint SET currentpoint =? WHERE Username = ?";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1 , teleportationPoint);
        select.setString(2, Username);
        select.executeUpdate();
    }

    public static void addImage(String teleportationPointName, String filePath) throws SQLException, IOException{
        Connection connection= getConnection();
        String statement = "UPDATE teleportationPoint SET imageFilePath =? WHERE Name = ?";
        PreparedStatement select = connection.prepareStatement(statement);
        System.out.println("File path is database: " + filePath);
        String[] token = filePath.split("\\\\");
        String processedFilePath = "";
        for (String s : token) {
            if (!processedFilePath.equals("")) {
                processedFilePath += "/" +s ;
            } else {
                processedFilePath = s;
            }
        }
        System.out.println("processed: " + processedFilePath);
        select.setString(1 , processedFilePath);
        select.setString(2, teleportationPointName);
        select.executeUpdate();
    }

    public static String getImageFilePath(String teleportationPointName) throws SQLException{
        System.out.println("teleport name: " + teleportationPointName);
        Connection connection = getConnection();
        String statement = "SELECT imageFilePath FROM teleportationPoint WHERE Name =?";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1,teleportationPointName);
        ResultSet result = select.executeQuery();
        
        if (result.next()){
            if (result.getString("imageFilePath") != null) {
//                byte[] imageBytes = Base64.getDecoder().decode(result.getBytes("imageFilePath"));
                System.out.println("File image retrived for " + teleportationPointName + " successfully with path " + result.getString("imageFilePath"));
                return result.getString("imageFilePath");
            } else{
                System.out.println("image in database is null");
            }
        }    
        return null;
    }

    public static float getX(String teleportationPointName) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT x_coordinate FROM teleportationPoint WHERE Name =?";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1,teleportationPointName);
        ResultSet result = select.executeQuery();
        if (result.next()){
            System.out.println("get x: " + result.getFloat("x_coordinate"));
            return result.getFloat("x_coordinate");
        }    
        return -1;
    }
    
    public static float getY(String teleportationPointName) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT y_coordinate FROM teleportationPoint WHERE Name =?";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1,teleportationPointName);
        ResultSet result = select.executeQuery();
        if (result.next()){
            System.out.println("get x: " + result.getFloat("y_coordinate"));
            return result.getFloat("y_coordinate");
        }    
        return -1;
    }
        
}
