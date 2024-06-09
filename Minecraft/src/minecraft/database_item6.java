package minecraft;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
// import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
// import java.util.Map;
import java.util.Map;

public class database_item6 {

    // item autofarmerblock
    // public static void main(String[] args) throws SQLException {
    //     initialize("defaultUser");
    //     // addSeedManually("defaultUser", "Wheat", 15);
    //     // addSeedManually("defaultUser", "Carrot", 15);
    //     // addSeedManually("defaultUser", "Potato", 15);
    //     // addSeedManually("defaultUser", "Beetroot", 15);
    //     // addSeedManually("defaultUser", "Melon", 15);
    //     // addSeedManually("defaultUser", "Pumpkin", 15);
    //     // addSeedManually("defaultUser", "Sweet Berry Bushes", 15);
    //     // addSeedManually method is meant for testing purpose only
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

    //to be called when new user is registered
    public static void initialize(String username) throws SQLException{
        addCrop(username, "Wheat", 15, 15, "None", 
                8, 1, 4, 1, 1);
        addCrop(username,"Carrot",15, 15, "None", 
                8, 0, 0, 1, 4);
        addCrop(username, "Potato", 15,15, "None", 
                8, 0, 0, 1, 4);
        addCrop(username, "Beetroot", 15,15, "None", 
                4, 1, 4, 1, 1);
        addCrop(username, "Melon Slice", 15,15, "Axes", 
                8, 0, 0, 3, 7);
        addCrop(username, "Pumpkin", 15,15, "Axes", 
                8, 0, 0, 1, 11);
        addCrop(username, "Sweet Berries", 15,15, "None", 
                4, 0, 0, 2, 3);
        //crop is saved into itemBox as crop only, the seed is not taken into account
    }

    public static ArrayList<Crop> retrieveCrop(String username) throws SQLException{
        ArrayList<Crop> record = new ArrayList<Crop>();;
        Connection connection = getConnection();
        String statement = "SELECT * FROM crop WHERE Username = ?";
        PreparedStatement retrieve  = connection.prepareStatement(statement);
        retrieve.setString(1, username);
        ResultSet result = retrieve.executeQuery();
        while (result.next()){
            String cropName = result.getString("cropName");
            int quantityCrop = result.getInt("quantityCrop");
            int quantitySeed = result.getInt("quantitySeed");
            Tool toolNeeded = new Tool(result.getString("toolneeded"));
            int numGrowthStages = result.getInt("numGrowthStages");
            int minSeedYield = result.getInt("minSeedYield");
            int maxSeedYield = result.getInt("maxSeedYield");
            int minCropYield = result.getInt("minCropYield");
            int maxCropYield = result.getInt("maxCropYield");
            record.add(new Crop(cropName, quantityCrop, quantitySeed, toolNeeded, numGrowthStages, 
                                minSeedYield, maxSeedYield, minCropYield, maxCropYield));
        }   
        return record;
    }

    public static LinkedList<Task> retrieveTaskManager(String username) throws SQLException{
        LinkedList<Task> record = new LinkedList<Task>();
        Connection connection = getConnection();
        String statement = "SELECT * FROM autofarmscheduling WHERE Username = ?";
        PreparedStatement retrieve  = connection.prepareStatement(statement);
        retrieve.setString(1, username);
        ResultSet result = retrieve.executeQuery();
        while (result.next()){
            float taskID = result.getFloat("taskID");
            String task = result.getString("task");
            String cropUsed = result.getString("cropused");
            int duration  = result.getInt("duration");
            int growthStage = result.getInt("growthstage");
            float status = result.getFloat("status");
            record.add(new Task(taskID, task, new Crop(cropUsed), duration, growthStage, status));
        }   
        return record;
    }

    public static Task retrieveOngoingTask(String username) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT * from autofarmscheduling WHERE username = ? ORDER BY taskID LIMIT 1";
        PreparedStatement retrieve = connection.prepareStatement(statement);
        retrieve.setString(1, username);
        ResultSet result = retrieve.executeQuery();
        if (result.next()){
            float taskID = result.getInt("taskID");
            String task= result.getString ("task");
            Crop cropUsed = new Crop (result.getString("cropUsed"));
            int duration = result.getInt("duration");
            int growthStage= result.getInt("growthStage");
            float status = result.getFloat("status");
            return new Task(taskID, task, cropUsed, duration, growthStage, status);
        }
        return null;
    }

    public static int retrieveQuantityCrop(String username, String cropName) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT quantityCrop from crop WHERE username = ? AND cropName = ?";
        PreparedStatement retrieve = connection.prepareStatement(statement);
        retrieve.setString(1, username);
        retrieve.setString(2, cropName);
        ResultSet result = retrieve.executeQuery();
        if (result.next())
            return result.getInt("quantitycrop");
        return 0;
    } 

    public static int retrieveQuantitySeed(String username, String cropName) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT quantitySeed from crop WHERE username = ? AND cropName = ?";
        PreparedStatement retrieve = connection.prepareStatement(statement);
        retrieve.setString(1, username);
        retrieve.setString(2, cropName);
        ResultSet result = retrieve.executeQuery();
        if (result.next())
            return result.getInt("quantitySeed");
        return 0;
    }

    public static Tool retrievetoolNeeded(String username, String cropName) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT toolneeded from crop WHERE username = ? AND cropName = ?";
        PreparedStatement retrieve = connection.prepareStatement(statement);
        retrieve.setString(1, username);
        retrieve.setString(2, cropName);
        ResultSet result = retrieve.executeQuery();
        if (result.next())
            return new Tool(result.getString("toolneeded"));
        return null;
    }
    
    public static int retrievenumGrowthStages(String username, String cropName) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT numgrowthstages from crop WHERE username = ? AND cropName = ?";
        PreparedStatement retrieve = connection.prepareStatement(statement);
        retrieve.setString(1, username);
        retrieve.setString(2, cropName);
        ResultSet result = retrieve.executeQuery();
        if (result.next())
            return result.getInt("numgrowthstages");
        return 0;
    }

    public static int retrieveminSeedYield(String username, String cropName) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT minSeedYield from crop WHERE username = ? AND cropName = ?";
        PreparedStatement retrieve = connection.prepareStatement(statement);
        retrieve.setString(1, username);
        retrieve.setString(2, cropName);
        ResultSet result = retrieve.executeQuery();
        if (result.next())
            return result.getInt("minSeedYield");
        return 0;
    }

    public static int retrievemaxSeedYield(String username, String cropName) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT maxSeedYield from crop WHERE username = ? AND cropName = ?";
        PreparedStatement retrieve = connection.prepareStatement(statement);
        retrieve.setString(1, username);
        retrieve.setString(2, cropName);
        ResultSet result = retrieve.executeQuery();
        if (result.next())
            return result.getInt("maxSeedYield");
        return 0;
    }
    
    public static int retrieveminCropYield(String username, String cropName) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT minCropYield from crop WHERE username = ? AND cropName = ?";
        PreparedStatement retrieve = connection.prepareStatement(statement);
        retrieve.setString(1, username);
        retrieve.setString(2, cropName);
        ResultSet result = retrieve.executeQuery();
        if (result.next())
            return result.getInt("minCropYield");
        return 0;
    }
    
    public static int retrievemaxCropYield(String username, String cropName) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT maxCropYield from crop WHERE username = ? AND cropName = ?";
        PreparedStatement retrieve = connection.prepareStatement(statement);
        retrieve.setString(1, username);
        retrieve.setString(2, cropName);
        ResultSet result = retrieve.executeQuery();
        if (result.next())
            return result.getInt("maxCropYield");
        return 0;
    }

    public static Map<Float, String> retrieveTaskFertilisedID(String username) throws SQLException{
        Map<Float, String> retrieved = new HashMap<Float, String>();
        Connection connection = getConnection();
        String statement = "SELECT taskID, task from autofarmscheduling WHERE username = ? AND Task=? OR Task=?";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1, username);
        select.setString(2, "Applying Super Fertiliser");
        select.setString(3, "Applying Bone Meal");
        ResultSet result = select.executeQuery();
        while (result.next()){
            retrieved.put((float)(result.getFloat("TaskID") - 0.5),
            result.getString("Task").split(" ")[1] + " " 
            + result.getString("Task").split(" ")[2]);
        }
        return retrieved;
    }

    public static float retrieveLastTaskID(String username) throws SQLException{
        Connection connection =  getConnection();
        String statement = "SELECT taskID FROM autofarmscheduling WHERE username = ? ORDER BY taskID DESC LIMIT 1";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1, username);
        ResultSet result = select.executeQuery();
        if (result.next())
            return result.getFloat("taskID");
        return -1;
    }

    public static boolean checkTool(String username, Crop crop, int quantityToAdd) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT quantity FROM itemBox WHERE Username = ? AND name =? AND Type =?";
        PreparedStatement check  = connection.prepareStatement(statement);
        check.setString(1, username);
        check.setString(2, crop.getToolNeeded().getName());
        check.setString(3, "Tool");
        ResultSet result = check.executeQuery();
        if(result.next()){
            if (result.getInt("Quantity")<quantityToAdd)
                return false;
            return true;
        }
        return false;
    }

    public static void addSeed(String username, Crop crop, int quantityToAdd) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT * FROM crop WHERE Username = ? AND cropName = ?";
        PreparedStatement check = connection.prepareStatement(statement);
        check.setString(1, username);
        check.setString(2, crop.getName());
        ResultSet result = check.executeQuery();
        if(result.next()){
            int quantity = result.getInt("quantityseed");
            quantity += quantityToAdd;
            statement = "UPDATE crop SET quantityseed =? WHERE Username =? AND CropName =?";
            PreparedStatement update = connection.prepareStatement(statement);
            update.setInt(1, quantity);
            update.setString(2, username);
            update.setString(3, crop.getName());
            update.executeUpdate();
        }
        connection.close();
    }

    public static void addSeedManually(String username, String crop, int quantityToAdd) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT * FROM crop WHERE Username = ? AND cropName = ?";
        PreparedStatement check = connection.prepareStatement(statement);
        check.setString(1, username);
        check.setString(2, crop);
        ResultSet result = check.executeQuery();
        if(result.next()){
            int quantity = result.getInt("quantityseed");
            quantity += quantityToAdd;
            statement = "UPDATE crop SET quantityseed =? WHERE Username =? AND CropName =?";
            PreparedStatement update = connection.prepareStatement(statement);
            update.setInt(1, quantity);
            update.setString(2, username);
            update.setString(3, crop);
            update.executeUpdate();
        }
        connection.close();
    }


    public static void removeSeed(String username, Crop crop, int quantityToRemove) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT * FROM crop WHERE Username = ? AND cropName = ?";
        PreparedStatement check = connection.prepareStatement(statement);
        check.setString(1, username);
        check.setString(2, crop.getName());
        ResultSet result = check.executeQuery();
        if(result.next()){
            int quantity = result.getInt("quantityseed");
            quantity -= quantityToRemove;
            statement = "UPDATE crop SET quantityseed =? WHERE Username =? AND CropName =?";
            PreparedStatement update = connection.prepareStatement(statement);
            update.setInt(1, quantity);
            update.setString(2, username);
            update.setString(3, crop.getName());
            update.executeUpdate();
        }
        connection.close();
    }

    //no removeCrop method cuz u will never ever need to use a crop(as per autofarmerblock implementation)
    public static void addCrop(String username, Crop crop, int quantityToAdd) throws SQLException{
       addCrop(username, crop.getName(), quantityToAdd, 0,crop.getToolNeeded().getName(), crop.getNumGrowthStages(),
                crop.getMinSeedYield(), crop.getMaxSeedYield(), crop.getMinCropYield(), crop.getMaxCropYield());
    }
    
    public static void addCrop(String username, String cropName, int cropQuantityToAdd, int seedQuantityToAdd, 
                               String toolNeeded, int numGrowthStages, int minSeedYield, int maxSeedYield, 
                               int minCropYield, int maxCropYield) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT * FROM crop WHERE Username = ? AND cropName = ?";
        PreparedStatement check = connection.prepareStatement(statement);
        check.setString(1, username);
        check.setString(2, cropName);
        ResultSet result = check.executeQuery();
        if(result.next()){
            //to be applied when yield
            int quantityCrop = result.getInt("quantityCrop");
            quantityCrop += cropQuantityToAdd;
            statement = "UPDATE crop SET quantityCrop =? WHERE Username =? AND CropName =?";
            PreparedStatement update = connection.prepareStatement(statement);
            update.setInt(1, quantityCrop);
            update.setString(2, username);
            update.setString(3, cropName);
            update.executeUpdate();
        }
        else{
            //for initialization purpose
            statement = "INSERT INTO crop (username, cropName, quantitycrop, quantitySeed, toolNeeded, "
                        + "numGrowthStages,minSeedYield,maxSeedYield, minCropYield, "
                        + "maxCropYield) VALUES (?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement insert = connection.prepareStatement(statement);
            insert.setString(1, username);
            insert.setString(2, cropName);
            insert.setInt(3, cropQuantityToAdd);
            insert.setInt(4, seedQuantityToAdd);
            insert.setString(5, toolNeeded);
            insert.setInt(6, numGrowthStages);
            insert.setInt(7, minSeedYield);
            insert.setInt(8, maxSeedYield);
            insert.setInt(9, minCropYield);
            insert.setInt(10, maxCropYield);
            insert.executeUpdate();
        }
        System.out.println("item6addcrop"+database_itemBox.retrieveQuantity(cropName,"defaultUser"));
        database_itemBox.addItem(username, cropName, cropQuantityToAdd);
        System.out.println("item6addcrop"+database_itemBox.retrieveQuantity(cropName,"defaultUser"));
        connection.close();
    }

    public static void removeCrop(String username, String cropName, int quantityToRemove) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT * FROM Crop WHERE username = ? AND cropname=?";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1, username);
        select.setString(2, cropName);
        ResultSet result = select.executeQuery();
        if (result.next()){
            statement = "UPDATE crop SET cropQuantity =? WHERE username =? AND cropName =?";
            PreparedStatement update = connection.prepareStatement(statement);
            update.setInt(1, result.getInt("cropquantity")- quantityToRemove);
            update.setString(2, username);
            update.setString(3, cropName);
            update.executeUpdate();
        }
    }

    public static void addTask(String username, String task, String cropUsed, int duration, int growthStage, 
    float status) throws SQLException{
        Connection connection = getConnection();
        String statement = "INSERT INTO autofarmscheduling "
                           + "(username, task, cropUsed, duration, GrowthStage, status)"
                           + "VALUES (?,?,?,?,?,?)";
        PreparedStatement insert = connection.prepareStatement(statement);
        insert.setString(1, username);
        insert.setString(2, task);
        insert.setString(3, cropUsed);
        insert.setInt(4, duration);
        insert.setInt(5, growthStage);
        insert.setFloat(6, status);
        insert.executeUpdate();
    }

    public static void addTaskAfter(String username, String task, String cropUsed, int duration, int growthStage, 
    float status, float taskID) throws SQLException{
        Connection connection = getConnection();
        String statement = "INSERT INTO autofarmscheduling " 
                            + "(taskID, username, task, cropUsed, duration, GrowthStage, status) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement insert = connection.prepareStatement(statement);
        insert.setFloat(1, taskID);
        insert.setString(2, username);
        insert.setString(3, task);
        insert.setString(4, cropUsed);
        insert.setInt(5, duration);
        insert.setInt(6, growthStage);
        insert.setFloat(7, status);
        insert.executeUpdate();
    }

    public static void updateTask(String username, String task, String cropUsed, 
                                  int durationRemaining, float status, float taskID) throws SQLException{
        Connection connection = getConnection();
        String statement = "UPDATE autofarmscheduling SET duration = ?, status = ? "
                        + "WHERE Username =? AND task =? AND cropUsed =? AND taskID =?";
        PreparedStatement update = connection.prepareStatement(statement);
        update.setInt(1, durationRemaining);
        update.setFloat(2, status);
        update.setString(3, username);
        update.setString(4, task);
        update.setString(5, cropUsed);     
        update.setFloat(6, taskID); 
        update.executeUpdate();
        connection.close();                      
    }

    public static void removeTask(String username, String task, String cropUsed) throws SQLException{
        Connection connection = getConnection();
        String statement = "DELETE FROM autofarmscheduling "
                           + "WHERE username = ? AND task = ? AND cropUsed =? ORDER BY taskID ASC LIMIT 1";
        PreparedStatement remove = connection.prepareStatement(statement);
        remove.setString(1, username);
        remove.setString(2, task);
        remove.setString(3, cropUsed);
        remove.executeUpdate();
    }
}
