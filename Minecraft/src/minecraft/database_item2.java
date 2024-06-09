package minecraft;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class database_item2 {
    //item multitool
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
        database_item2.addIntoTable(username, "Axes", "Tool", "To quickly chop down trees or destroy various other wooden items", 2);
        database_item2.addIntoTable(username, "Bucket of Salmon", "Tool", "A bucket of fish places a water source block, and spawns the corresponding fish back into the world", 2);
        database_item2.addIntoTable(username, "Bucket of Water", "Tool", "A bucket to carry water", 2);
        database_item2.addIntoTable(username, "Clock", "Tool", "The dial spins clockwise slowly to indicate the time of day, corresponding to the sun or moon's actual position in the sky", 2);
        database_item2.addIntoTable(username, "Compass", "Tool", "Point toward the world spawn point", 2);
        database_item2.addIntoTable(username, "Hoe", "Tool", "To turn dirt, grass blocks, and dirt paths into farmland", 2);
        database_item2.addIntoTable(username, "Iron Pickaxe", "Tool", "To mine ores, rocks, rock-based blocks and metal-based blocks quickly and obtain them as items", 2);
        database_item2.addIntoTable(username, "Map", "Tool", "To display any and all players in the world and their locations", 2);
        database_item2.addIntoTable(username, "Powder Snow Bucket", "Tool", "To places a powder snow block", 2);
        database_item2.addIntoTable(username, "Shovels", "Tool", "To hasten the process of breaking dirt, sand, gravel and other soil blocks, as well as to convert dirt blocks into dirt paths", 2);
    }
    
    // public int getSize(String username) throws SQLException{
    //     int record=0;
    //     Connection connection = getConnection();
    //     String statement = "SELECT COUNT(?) AS count FROM ? WHERE Username =?";
    //     PreparedStatement count = connection.prepareStatement(statement);
    //     count.setString(1, "Name");
    //     count.setString(2, "multitool");
    //     count.setString(3, username);
    //     ResultSet result = count.executeQuery();
    //     if (result.next())
    //         return result.getInt("count");
    //     return record;
    // }

    public static void addIntoTable(String username, String name, String type, String function, int grade) throws SQLException{
        Connection connection = getConnection();
        String statement = "INSERT INTO toollist (Username, Name, Type, Functions, Grade)"+
                            "VALUES (?,?,?,?,?)";
        PreparedStatement insert = connection.prepareStatement(statement);
        insert.setString(1, username);
        insert.setString(2, name);
        insert.setString(3, type);
        insert.setString(4, function);
        insert.setInt(5, grade);
        insert.executeUpdate();
    }

    public static List<Tool> retrieveTool(String user) throws SQLException{
        Connection connection = getConnection();
        List<Tool> list = new ArrayList<Tool>();
        String statement = "SELECT * FROM toollist WHERE Username = ? and Type = ?";
        PreparedStatement filter = connection.prepareStatement(statement);
        filter.setString(1, user);
        filter.setString(2, "Tool");
        ResultSet result = filter.executeQuery();
        String name, type, function;
        int grade, toolid;//initialize the grade of each tool =2
        while(result.next()){
            name = result.getString("Name");
            type = result.getString("Type");
            function = result.getString("Functions");
            grade = result.getInt("Grade");
            toolid= result.getInt("toolid");
            list.add(new Tool(name, type, function, grade, toolid));
        }
        list.forEach(e-> System.out.println(e));
        return list;
    }

    public static int retrieveLastToolID(String username) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT * FROM multitool WHERE username = ? ORDER BY toolid DESC LIMIT 1";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1, username);
        ResultSet result = select.executeQuery();
        if (result.next()){
            return result.getInt("toolId");
        }
        return 0;
    }

    public static ArrayList<Tool> retrieveMultitool(String user) throws SQLException{
        Connection connection = getConnection();
        ArrayList<Tool> list = new ArrayList<Tool>();
        String statement = "SELECT * FROM multitool WHERE Username = ?";
        PreparedStatement retrieve = connection.prepareStatement(statement);
        retrieve.setString(1, user);
        ResultSet result = retrieve.executeQuery();
        String name, type, function;
        int grade, toolId;
        while (result.next()){
            name = result.getString("Name");
            type = result.getString("Type");
            function = result.getString("Functions");
            grade = result.getInt("Grade");
            toolId = result.getInt("toolid");
            list.add(new Tool(name, type, function, grade, toolId));
        }
        return list;
    }

    public static void addMultiTool(String username, String toolname, String type, String function, 
    int grade) throws SQLException{   
        //int quantitytoadd - parameter removed, back up for future use if needed
        Connection connection = getConnection();
        // String statement = "SELECT * FROM multitool WHERE Username = ? AND Name = ?";
        // PreparedStatement check = connection.prepareStatement(statement);
        // check.setString(1, username);
        // check.setString(2,toolname);
        // ResultSet result = check.executeQuery();
        // int Itemquantity;
        // if (result.next()){
        //     Itemquantity = result.getInt("Quantity") + quantitytoadd;
        //     statement = "UPDATE itemBackpack SET Quantity = ? WHERE Username = ? AND Name =?";
        //     PreparedStatement update = connection.prepareStatement(statement);
        //     update.setInt(1, Itemquantity);
        //     update.setString(2,username);
        //     update.setString(3, itemname);
        //     update.executeUpdate();
        // }
        // else{
        //     Itemquantity = quantitytoadd;
            String statement = "INSERT INTO multitool(username, name, type, functions, grade)"
                                + " VALUES (?,?,?,?,?)";
            PreparedStatement insert = connection.prepareStatement(statement);
            insert.setString(1,username);
            insert.setString(2,toolname);
            insert.setString(3,type);
            insert.setString(4, function);
            insert.setInt(5, grade);
            // insert.setInt(5,quantitytoadd);
            insert.executeUpdate();
        // }
    }

    public static void removeMultiTool(String toolName, int grade, int toolId, String username) throws SQLException{
        // int quantitytoreduce - parameter removed, back up for future use 
        Connection connection = getConnection();
        // String statement = "SELECT Quantity FROM itemBackpack WHERE Username = ? AND Name = ?";
        // PreparedStatement check = connection.prepareStatement(statement);
        // check.setString(1, username);
        // check.setString(2,itemName);
        // ResultSet result = check.executeQuery();
        // // int quantity;
        // // if (result.next()){
        //     quantity = result.getInt("Quantity") - quantitytoreduce;
        //     if (quantity==0){
                String statement = "DELETE FROM multitool WHERE Username = ? AND Name = ? AND grade = ? AND toolid = ?";
                PreparedStatement delete = connection.prepareStatement(statement);
                delete.setString(1, username);
                delete.setString(2,toolName);
                delete.setInt(3, grade);
                delete.setInt(4, toolId);
                delete.executeUpdate();
            // }
            // else{
            //     statement = "UPDATE itemBackpack SET Quantity = ? WHERE Username = ? AND Name =?";
            //     PreparedStatement update = connection.prepareStatement(statement);
            //     update.setInt(1, quantity);
            //     update.setString(2,username);
            //     update.setString(3, itemName);
            //     update.executeUpdate();
            // }
        // }
    }

    // public static void removeToolByGrade(String username, String toolName) throws SQLException{
    //     Connection connection = getConnection();
    //     String statement = "SELECT * FROM toollist WHERE username=? AND name =? ORDER BY grade ASC LIMIT 1";
    //     PreparedStatement check = connection.prepareStatement(statement);
    //     check.setString(1, username);
    //     check.setString(2,toolName);
    //     ResultSet result = check.executeQuery();
    //     if(result.next()){
    //         int toolId = result.getInt("toolid");
    //         statement = "DELETE FROM toollist WHERE toolid = ?";
    //         PreparedStatement delete = connection.prepareStatement(statement);
    //         delete.setInt(1, toolId);
    //         delete.executeUpdate();
    //     }
    // }

    public static void removeTool(String toolName, int grade, int toolId, String username) throws SQLException{
        // int quantitytoreduce - parameter removed, back up for future use 
        Connection connection = getConnection();
        // String statement = "SELECT Quantity FROM itemBackpack WHERE Username = ? AND Name = ?";
        // PreparedStatement check = connection.prepareStatement(statement);
        // check.setString(1, username);
        // check.setString(2,itemName);
        // ResultSet result = check.executeQuery();
        // // int quantity;
        // // if (result.next()){
        //     quantity = result.getInt("Quantity") - quantitytoreduce;
        //     if (quantity==0){
                String statement = "DELETE FROM toollist WHERE Username = ? AND Name = ? AND grade = ? AND toolid = ?";
                PreparedStatement delete = connection.prepareStatement(statement);
                delete.setString(1, username);
                delete.setString(2,toolName);
                delete.setInt(3, grade);
                delete.setInt(4, toolId);
                delete.executeUpdate();
                // database_itemBox.removeItem(username, toolName, 1);
            // }
            // else{
            //     statement = "UPDATE itemBackpack SET Quantity = ? WHERE Username = ? AND Name =?";
            //     PreparedStatement update = connection.prepareStatement(statement);
            //     update.setInt(1, quantity);
            //     update.setString(2,username);
            //     update.setString(3, itemName);
            //     update.executeUpdate();
            // }
        // }
    }

    public static void addTool(String username, String toolname, String type, String function, 
    int grade) throws SQLException{   
        //int quantitytoadd - parameter removed, back up for future use if needed
        Connection connection = getConnection();
        // String statement = "SELECT * FROM multitool WHERE Username = ? AND Name = ?";
        // PreparedStatement check = connection.prepareStatement(statement);
        // check.setString(1, username);
        // check.setString(2,toolname);
        // ResultSet result = check.executeQuery();
        // int Itemquantity;
        // if (result.next()){
        //     Itemquantity = result.getInt("Quantity") + quantitytoadd;
        //     statement = "UPDATE itemBackpack SET Quantity = ? WHERE Username = ? AND Name =?";
        //     PreparedStatement update = connection.prepareStatement(statement);
        //     update.setInt(1, Itemquantity);
        //     update.setString(2,username);
        //     update.setString(3, itemname);
        //     update.executeUpdate();
        // }
        // else{
        //     Itemquantity = quantitytoadd;
            String statement = "INSERT INTO toollist(username, name, type, functions, grade)"
                                + " VALUES (?,?,?,?,?)";
            PreparedStatement insert = connection.prepareStatement(statement);
            insert.setString(1,username);
            insert.setString(2,toolname);
            insert.setString(3,type);
            insert.setString(4, function);
            insert.setInt(5, grade);
            // insert.setInt(5,quantitytoadd);
            insert.executeUpdate();
            // database_itemBox.addItem(username, toolname, 1);
        // }
    }

    
    public static void clearMultiTool(String username) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT * FROM multitool WHERE Username = ?";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1, username);
        ResultSet result = select.executeQuery();
        while(result.next()){
            String toolname = result.getString("name");
            String type = result.getString("Type");
            String function = result.getString("Functions");
            int grade = result.getInt("Grade");
            addTool(username, toolname, type, function, grade);
            // database_itemBox.addItem(username, toolname, 1);
        }
        statement = "DELETE FROM multitool WHERE Username = ?";
        PreparedStatement delete = connection.prepareStatement(statement);
        delete.setString(1, username);
        delete.executeUpdate();
    }

    public static void upgradeTool(String username, String toolName, int toolId) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT * FROM multitool WHERE Username = ? AND Name = ? AND toolId = ?";
        PreparedStatement filter = connection.prepareStatement(statement);
        filter.setString(1, username);
        filter.setString(2,toolName);
        filter.setInt(3, toolId);
        ResultSet result = filter.executeQuery();
        int grade = 0;
        if(result.next()){
            grade = result.getInt("grade");
            statement = "UPDATE multitool SET grade= ? WHERE Username =? AND Name = ? AND toolId = ?";
            PreparedStatement update = connection.prepareStatement(statement);
            update.setInt(1,grade+1);
            update.setString(2, username);
            update.setString(3,toolName);
            update.setInt(4, toolId);
            update.executeUpdate();
        }
    }

    public static void downgradeTool(String username, String toolName, int toolId) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT * FROM multitool WHERE Username = ? AND Name = ? AND toolid =?";
        PreparedStatement filter = connection.prepareStatement(statement);
        filter.setString(1, username);
        filter.setString(2,toolName);
        filter.setInt(3, toolId);
        ResultSet result = filter.executeQuery();
        int grade = 0;
        if(result.next()){
            grade = result.getInt("grade");
            statement = "UPDATE multitool SET grade= ? WHERE Username =? AND Name = ? AND toolid =?";
            PreparedStatement update = connection.prepareStatement(statement);
            update.setInt(1,grade-1);
            update.setString(2, username);
            update.setString(3,toolName);
            update.setInt(4, toolId);
            update.executeUpdate();
        }
    }
}
