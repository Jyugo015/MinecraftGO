package minecraft;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
// import java.util.Optional;
import java.util.Map;

public class database_item7 {

    //item secureChest
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

    public static void testing() throws SQLException{
        createNewChest("defaultName", "Self-defined", "defaultUser", "defaultUser", 2);
        addApproved("defaultUser", "defaultUser2", 2);
        addApproved("defaultUser", "defaultUser3", 1);
        createNewChest("defaultName2", "Self-defined", "defaultUser2", "defaultUser2", 2);
        addApproved("defaultUser2", "defaultUser", 1);
        createNewChest("defaultName3", "Self-defined", "defaultUser3", "defaultUser3", 2);
        addApproved("defaultUser3", "defaultUser", 2);
        createNewChest("defaultName4", "Private", "defaultUser4", "defaultUser4", 2);
        createNewChest("defaultName5", "Self-defined", "defaultUser5", "defaultUser5", 2);
        addRequest("defaultUser", "defaultUser5", 1);
        addRequest("defaultUser5", "defaultUser", 2);
        createNewChest("defaultName6", "Public", "defaultUser6", "defaultUser6", 2);
        createNewChest("defaultName7", "Self-defined", "defaultUser7", "defaultUser7", 2);
        addRequest("defaultUser", "defaultUser7", 1);
        addRequest("defaultUser7", "defaultUser", 2);
        createNewChest("defaultName8", "Self-defined", "defaultUser8", "defaultUser8", 2);
        addItemManually("defaultUser2", "Apple", "Food", "To be eaten by the player as food items ", 3);
        addItemManually("defaultUser2", "Trident", "Weapon", "To summon a lightning bolt if there is a thunderstorm", 4);
        addItemManually("defaultUser2", "Music Disc Far", "Record", "Play the song with the jukebox", 5);
        addItemManually("defaultUser3", "Map", "Tool", "To display any and all players in the world and their locations", 10);
        addItemManually("defaultUser3", "Melon Slice", "Food", "To craft recipe of glistering melons; 50% chance to raise the compost level of new composter by 1", 8);
        addItemManually("defaultUser3", "Elytra", "Transportation", "Use arrows or fireworks as ammunition", 15);
        addItemManually("defaultUser6", "Map", "Tool", "To display any and all players in the world and their locations", 12);
        addItemManually("defaultUser6", "Melon Slice", "Food", "To craft recipe of glistering melons; 50% chance to raise the compost level of new composter by 1", 9);
        addItemManually("defaultUser6", "Elytra", "Transportation", "Use arrows or fireworks as ammunition", 15);
        addItemManually("defaultUser6", "Hoe", "Tool", "To turn dirt, grass blocks, and dirt paths into farmland", 13);
        addItemManually("defaultUser6", "Swords", "Weapon", "To deal damage to entities or for breaking certain blocks faster than by hand", 7);
        addItemManually("defaultUser6", "Redstone", "Material", "To carry power signals between items", 11);
        addItemManually("defaultUser5", "Hoe", "Tool", "To turn dirt, grass blocks, and dirt paths into farmland", 13);
        addItemManually("defaultUser5", "Swords", "Weapon", "To deal damage to entities or for breaking certain blocks faster than by hand", 7);
        addItemManually("defaultUser5", "Redstone", "Material", "To carry power signals between items", 11);
        addItemManually("defaultUser7", "Hoe", "Tool", "To turn dirt, grass blocks, and dirt paths into farmland", 13);
        addItemManually("defaultUser7", "Swords", "Weapon", "To deal damage to entities or for breaking certain blocks faster than by hand", 7);
        addItemManually("defaultUser7", "Redstone", "Material", "To carry power signals between items", 11);
        addItemManually("defaultUser8", "Hoe", "Tool", "To turn dirt, grass blocks, and dirt paths into farmland", 13);
        addItemManually("defaultUser8", "Swords", "Weapon", "To deal damage to entities or for breaking certain blocks faster than by hand", 7);
        addItemManually("defaultUser8", "Redstone", "Material", "To carry power signals between items", 11);
    }

    public static void initialize(String username) throws SQLException{
        createNewChest(username);
    }
    
    public static void createNewChest(String username) throws SQLException{
        createNewChest(username+"\'s Secure Chest", "Private", username, username, 2);
    }

    public static void createNewChest
    (String chestname, String securitylevel, String username, String approveduser, int permissiontype)
    throws SQLException{
        Connection connection = getConnection();
        String statement = "INSERT INTO securechestSetting (chestname, securitylevel, owner, approveduser, permissiontype) "
                            + "VALUES (?,?,?,?,?)";
        PreparedStatement insert = connection.prepareStatement(statement);
        insert.setString(1, chestname);
        insert.setString(2, securitylevel);
        insert.setString(3, username);
        insert.setString(4, approveduser);
        insert.setInt(5, permissiontype);
        insert.executeUpdate();
    }

    public static void addItemManually(String owner, String itemname, String type, String function, int quantity) throws SQLException{
        Connection connection = getConnection();
        String statement = "INSERT INTO securechest (username, name, type, functions, quantity) "
                            + "VALUES (?,?,?,?,?)";
        PreparedStatement insert = connection.prepareStatement(statement);
        insert.setString(1, owner);
        insert.setString(2, itemname);
        insert.setString(3, type);
        insert.setString(4, function);
        insert.setInt(5, quantity);
        insert.executeUpdate();
    }

    public static Map<SecureChest, Integer> retrieveChestWithAccess(String username) throws SQLException{
        Map<SecureChest, Integer> chestWithAccess = new HashMap<SecureChest, Integer>();
        Connection connection = getConnection();
        String statement = "SELECT * FROM securechestSetting WHERE approveduser =? AND owner !=?";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1, username);
        select.setString(2, username);
        ResultSet result = select.executeQuery();
        while(result.next()){
            chestWithAccess.put(new SecureChest(result.getString("owner")), result.getInt("PermissionType"));
        }
        statement = "SELECT * FROM securechestSetting WHERE securitylevel = ?";
        PreparedStatement select2 = connection.prepareStatement(statement);
        select2.setString(1, "Public");
        ResultSet result2 = select2.executeQuery();
        while(result2.next()){
            chestWithAccess.put(new SecureChest(result2.getString("owner")), 2);
        }
        return chestWithAccess;
    }

    public static ArrayList<SecureChest> retrieveChestNoAccess(String username) throws SQLException{
        ArrayList<SecureChest> chestNoAccess = new ArrayList<SecureChest>();
        Connection connection = getConnection();
        String statement = "SELECT DISTINCT owner FROM securechestSetting WHERE securitylevel = ? AND "
                            + "approvedUser = owner EXCEPT SELECT DISTINCT owner FROM securechestsetting " 
                            + "WHERE approvedUser =?";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1, "Self-defined");
        select.setString(2, username);
        ResultSet result  = select.executeQuery();
        while (result.next()){
            chestNoAccess.add(new SecureChest(result.getString("owner")));
        }
        chestNoAccess.forEach(e->System.out.println(e));
        return chestNoAccess;
    }

    public static String retrieveChestName(String owner) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT ChestName FROM securechestSetting WHERE owner = ?";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1, owner);
        ResultSet result = select.executeQuery();
        if(result.next())
            return result.getString("chestname");
        return null;
    }

    public static String retrieveSecurityLevel(String owner) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT Securitylevel FROM securechestSetting WHERE Owner = ?";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1, owner);
        ResultSet result = select.executeQuery();
        if (result.next()){
            return result.getString("securityLevel");
            // Optional<SecurityLevel> securityLevel = SecurityLevel.fromString(securityLevelString);
            // if (securityLevel.isPresent()) {
            //     SecurityLevel level = securityLevel.get();
            //     return level;
            // } else {
            //     return null;
            // }
        }
        return null;
    }

    public static HashMap<String, Integer> retrieveAccessRequestMy(String owner, String status) throws SQLException{
        Connection connection = getConnection();
        HashMap<String, Integer> request = new HashMap<String, Integer>();
        String statement = "SELECT * FROM securechestRequest WHERE Owner =? AND requeststatus =?";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1, owner);
        select.setString(2, status);
        ResultSet result = select.executeQuery();
        while (result.next()){
            String username = result.getString("requestor");
            int type = result.getInt("PermissionRequested");
            request.put(username, type);
        }
        return request;
    }

    public static Map<String, String> retrieveRequestSent(String username) throws SQLException{
        Map<String, String> requestSent = new HashMap<String, String>();
        Connection connection = getConnection();
        String statement = "SELECT * FROM securechestrequest WHERE requestor =?";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1, username);
        ResultSet result = select.executeQuery();
        while (result.next()){
            requestSent.put("Request for "+ 
                            (result.getInt("PermissionRequested")==1?
                            "View Only Permission of ":"Full Access Permission of ") 
                            + result.getString("chestName"), 
                            result.getString("requestStatus"));
        }
        return requestSent;
    }

    public static HashMap<SecureChest, Integer> retrieveAccessRequestOther(String owner) throws SQLException{
        Connection connection = getConnection();
        HashMap<SecureChest, Integer> request = new HashMap<SecureChest, Integer>();
        String statement = "SELECT * FROM securechestRequest WHERE requestor =? AND requeststatus =?";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1, owner);
        select.setString(2, "Pending");
        ResultSet result = select.executeQuery();
        while (result.next()){
            String username = result.getString("owner");
            int type = result.getInt("PermissionRequested");
            request.put(new SecureChest(username), type);
        }
        return request;
    }

    public static HashMap<String, Integer> retrieveAccessPermission(String owner) throws SQLException{
        Connection connection = getConnection();
        HashMap<String, Integer> access = new HashMap<String, Integer>();
        String statement = "SELECT * FROM securechestsetting WHERE owner =?";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1, owner);
        ResultSet result = select.executeQuery();
        String securitylevel= null;
        if (result.next()){
            securitylevel = result.getString("securitylevel");
            if (securitylevel.equals("Public")){
                statement = "SELECT DISTINCT owner FROM securechestSetting  WHERE owner !=?";
                PreparedStatement select1 = connection.prepareStatement(statement);
                select1.setString(1, owner);
                ResultSet result1 = select1.executeQuery();
                while (result1.next())
                    access.put(result1.getString("owner"), 2);
            }
            else{
                statement = "SELECT * FROM securechestSetting WHERE Owner =? AND approvedUser !=? ";
                PreparedStatement select2 = connection.prepareStatement(statement);
                select2.setString(1, owner);
                select2.setString(2, owner);
                ResultSet result2 = select2.executeQuery();
                while (result2.next()){
                    String username = result2.getString("approvedUser");
                    int type = result2.getInt("PermissionType");
                    access.put(username, type);
                }
            }
        } 
        return access;
    }

    public static HashMap<EnderBackpackItem, Integer> retrieveItemChest(String owner) throws SQLException{
        Connection connection = getConnection();
        HashMap<EnderBackpackItem, Integer> itemList = new HashMap<EnderBackpackItem, Integer>();
        String statement = "SELECT * FROM securechest WHERE Username =?";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1, owner);
        ResultSet result = select.executeQuery();
        while (result.next()){
            EnderBackpackItem item = new EnderBackpackItem(result.getString("Name"), 
                                     result.getString("Type"), 
                                     result.getString("Functions"));
            int quantity = result.getInt("quantity");
            itemList.put(item, quantity);
        }
        return itemList;
    }

    public static void updateChestSecurity(String owner, String security) throws SQLException{
        Connection connection = getConnection();
        String statement = "UPDATE securechestSetting SET SecurityLevel = ? WHERE owner =?";
        PreparedStatement update = connection.prepareStatement(statement);
        update.setString(1, security);
        update.setString(2, owner);
        update.executeUpdate();
        if (security.equals("Private")){
            statement = "DELETE FROM securechestSetting WHERE owner != approvedUser";
            PreparedStatement delete = connection.prepareStatement(statement);
            delete.executeUpdate();
        }
    }

    public static void updateChestPermission(String owner, String username, int type) throws SQLException{
        Connection connection = getConnection();
        String statement = "UPDATE securechestSetting SET PermissionType = ? "
                           + "WHERE owner =? AND approvedUser =?";
        PreparedStatement update = connection.prepareStatement(statement);
        update.setInt(1, type);
        update.setString(2, owner);
        update.setString(3, username);
        update.executeUpdate();
    }

    public static void editChestName(String username, String newName) throws SQLException{
        Connection connection = getConnection();
        String statement = "UPDATE securechestSetting SET chestName = ? WHERE Owner =?";
        PreparedStatement update = connection.prepareStatement(statement);
        update.setString(1, newName);
        update.setString(2, username);
        update.executeUpdate();
    }

    public static void addApproved(String owner, String username, int type) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT * FROM secureChestSetting WHERE owner = ?";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1, owner);
        ResultSet result = select.executeQuery();
        if (result.next()){
            String chestname = result.getString("chestName");
            String securityLevel = result.getString("securitylevel");
            statement = "INSERT INTO secureChestSetting "
                        + "(chestname, securityLevel, owner, ApprovedUser, PermissionType)"
                        + "VALUES(?,?,?,?,?)";
            PreparedStatement insert  = connection.prepareStatement(statement);
            insert.setString(1,chestname);
            insert.setString(2, securityLevel);
            insert.setString(3, owner);
            insert.setString(4, username);
            insert.setInt(5,type);
            insert.executeUpdate();
        }
    }

    public static void removeApprovedUser(String owner, String username) throws SQLException{
        Connection connection = getConnection();
        String statement = "DELETE FROM secureChestSetting WHERE owner = ? AND approvedUser =?";
        PreparedStatement delete  = connection.prepareStatement(statement);
        delete.setString(1, owner);
        delete.setString(2, username);
        delete.executeUpdate();
    }

    public static void updateRequest(String owner, String username, String status) throws SQLException{
        Connection connection = getConnection();
        String statement = "UPDATE securechestrequest SET requeststatus =? WHERE owner =? AND requestor =?";
        PreparedStatement update = connection.prepareStatement(statement);
        update.setString(1, status);
        update.setString(2, owner);
        update.setString(3, username);
        update.executeUpdate();
    }

    public static void addRequest(String owner, String username, int type) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT * FROM secureChestsetting WHERE owner = ?";
        PreparedStatement select = connection.prepareStatement(statement);
        select.setString(1, owner);
        ResultSet result = select.executeQuery();
        if (result.next()){
            String chestname = result.getString("chestName");
            statement = "INSERT INTO secureChestrequest "
                        + "(chestname, owner, requestor, requeststatus, Permissionrequested)"
                        + "VALUES(?,?,?,?,?)";
            PreparedStatement insert  = connection.prepareStatement(statement);
            insert.setString(1,chestname);
            insert.setString(2, owner);
            insert.setString(3, username);
            insert.setString(4, "Pending");
            insert.setInt(5,type);
            insert.executeUpdate();
        }
    }

    public static void deposit(String owner, String username, EnderBackpackItem item, int quantityAdd) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT quantity FROM securechest WHERE Username = ? AND Name = ?";
        PreparedStatement check = connection.prepareStatement(statement);
        check.setString(1, owner);
        check.setString(2, item.getName());
        ResultSet result = check.executeQuery();
        if (result.next()){
            int quantity = result.getInt("quantity");
            statement = "UPDATE securechest SET quantity =? WHERE Username = ? AND Name = ?";
            PreparedStatement update = connection.prepareStatement(statement);
            update.setInt(1, quantity+quantityAdd);
            update.setString(2, owner);
            update.setString(3, item.getName());
            update.executeUpdate();
        }
        else{
            statement = "INSERT INTO secureChest "
                            + "(Username, Name, Type, Functions, Quantity)"
                            + "VALUES (?,?,?,?,?)";
            PreparedStatement insert  = connection.prepareStatement(statement);
            insert.setString(1, owner);
            insert.setString(2, item.getName());
            insert.setString(3, item.getType());
            insert.setString(4, item.getFunction());
            insert.setInt(5,quantityAdd);
            insert.executeUpdate();
        }
        database_itemBox.removeItem(item.getName(), username, quantityAdd);
    }

    public static void withdraw(String owner, String username, EnderBackpackItem item, int quantityToRemove) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT quantity FROM securechest WHERE Username = ? AND Name = ?";
        PreparedStatement check = connection.prepareStatement(statement);
        check.setString(1, owner);
        check.setString(2, item.getName());
        ResultSet result = check.executeQuery();
        if (result.next()){
            int quantity = result.getInt("quantity");
            statement = "UPDATE securechest SET quantity =? WHERE Username = ? AND Name = ?";
            PreparedStatement update = connection.prepareStatement(statement);
            update.setInt(1, quantity-quantityToRemove);
            update.setString(2, owner);
            update.setString(3, item.getName());
            update.executeUpdate();
        }
        database_itemBox.addItem(username,item.getName(), quantityToRemove);
    }
}
