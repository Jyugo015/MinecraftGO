package minecraft;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class database_summary {

    public static String username = "defaultUser";
    //initialize itemBox(potion based on potion, food based on crop, and + xy eh), item1, item3(potion),
    // item6(crop), item7, item8, item9

    //for demonstration purpose, we use "defaultUser" as the user 
    //n we manually add the user into the userlist so defaultUser can straightaway log in
    //as for multiple user in secureChest and teleportation point, we use defaultUser2,3,4 respectively for 
    //each functionality
    public static void main(String[] args) throws SQLException {
        resetAll();

        demonstrate();
    }

    public static void demonstrate() throws SQLException {
        createTable();
        initializeItemBox();
        database_user.addUser(new User(username, "jiaqitan1006@gmail.com",
                PasswordHash.hashPassword("MinecraftGO")));
        database_item1.initialize(username);
        database_item2.initialize(username);
        database_item3.initialize(username);
        database_item6.initialize(username);
        database_item7.testing();
        database_item8.initialize(username);
        database_item9.initialize();
        initializeTeleportationPoint();
    }

    public static Connection getConnection() throws SQLException {
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
        System.out.println("Connected");
        return connection;
    }

    public static void createTable() throws SQLException {
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
                + "Name VARCHAR(255), Potency INT, Effect VARCHAR(255))";
        PreparedStatement create6 = connection.prepareStatement(statement);

        statement = "CREATE TABLE IF NOT EXISTS AutoFarmScheduling"
                + "(TaskID FLOAT PRIMARY KEY AUTO_INCREMENT, Username VARCHAR(255) COLLATE utf8_bin, "
                + "Task VARCHAR(255), CropUsed VARCHAR(255), Duration INT, "
                + "GrowthStage INT, Status FLOAT)";
        PreparedStatement create7 = connection.prepareStatement(statement);

        statement = "CREATE TABLE IF NOT EXISTS crop"
                + "(CropID INT PRIMARY KEY AUTO_INCREMENT, Username VARCHAR(255) COLLATE utf8_bin,"
                + "CropName VARCHAR(255), ToolNeeded VARCHAR(255), QuantityCrop INT, QuantitySeed INT, "
                + "NumGrowthStages INT, MinSeedYield INT, MaxSeedYield INT, "
                + "MinCropYield INT, MaxCropYield INT)";
        PreparedStatement create8 = connection.prepareStatement(statement);

        statement = "CREATE TABLE IF NOT EXISTS SecureChestSetting"
                + "(ChestID INT PRIMARY KEY AUTO_INCREMENT, ChestName VARCHAR(255),"
                + "SecurityLevel VARCHAR(255), Owner VARCHAR(255) COLLATE utf8_bin,"
                + "ApprovedUser VARCHAR(255) COLLATE utf8_bin, PermissionType INT)";
        PreparedStatement create9 = connection.prepareStatement(statement);

        statement = "CREATE TABLE IF NOT EXISTS SecureChestRequest"
                + "(ChestID INT PRIMARY KEY AUTO_INCREMENT, ChestName VARCHAR(255),"
                + "Owner VARCHAR(255) COLLATE utf8_bin, Requestor VARCHAR(255) COLLATE utf8_bin,"
                + "RequestStatus VARCHAR(255),"
                + "PermissionRequested VARCHAR(255))";
        PreparedStatement create10 = connection.prepareStatement(statement);

        statement = "CREATE TABLE IF NOT EXISTS BackpackCapacity"
                + "(BackpackID INT PRIMARY KEY AUTO_INCREMENT, Username VARCHAR(255),"
                + "Capacity INT)";
        PreparedStatement create11 = connection.prepareStatement(statement);

        statement = "CREATE TABLE IF NOT EXISTS userlist"
                + "(UserID INT PRIMARY KEY AUTO_INCREMENT, Username VARCHAR(255) COLLATE utf8_bin,"
                + "Passcode VARCHAR(255) COLLATE utf8_bin, Email VARCHAR(255) COLLATE utf8_bin)";
        PreparedStatement create12 = connection.prepareStatement(statement);

//        statement = "CREATE TABLE IF NOT EXISTS teleportationPoint"
//                + "(PointID INT PRIMARY KEY AUTO_INCREMENT, Username VARCHAR(255) COLLATE utf8_bin,"
//                + " Name VARCHAR(255), Neighbour VARCHAR(255), CurrentPoint VARCHAR(255), "
//                + "x_coordinate FLOAT, y_coordinate FLOAT, imageFilePath VARCHAR(255),"
//                + "RequestSent VARCHAR(255), RequestGet VARCHAR(255))";
//        PreparedStatement create13 = connection.prepareStatement(statement);
        statement = "CREATE TABLE IF NOT EXISTS teleportationPoint"
                + "(PointID INT PRIMARY KEY AUTO_INCREMENT, Username VARCHAR(255) COLLATE utf8_bin,"
                + " Name VARCHAR(255) COLLATE utf8_bin, Neighbour VARCHAR(255), CurrentPoint VARCHAR(255), "
                + "x_coordinate FLOAT, y_coordinate FLOAT, imageFilePath VARCHAR(255),"
                + "RequestSent VARCHAR(255), RequestGet VARCHAR(255))";
        PreparedStatement create13 = connection.prepareStatement(statement);

        statement = "CREATE TABLE IF NOT EXISTS automatedSortingChest"
                + "(ItemID INT PRIMARY KEY AUTO_INCREMENT, Username VARCHAR(255) COLLATE utf8_bin,"
                + "Name VARCHAR(255), Type VARCHAR(255), Functions VARCHAR(255), Quantity INT)";
        PreparedStatement create14 = connection.prepareStatement(statement);

        statement = "CREATE TABLE IF NOT EXISTS secureChest"
                + "(ItemID INT PRIMARY KEY AUTO_INCREMENT, Username VARCHAR(255) COLLATE utf8_bin,"
                + "Name VARCHAR(255), Type VARCHAR(255), Functions VARCHAR(255), Quantity INT)";
        PreparedStatement create15 = connection.prepareStatement(statement);

        statement = "CREATE TABLE IF NOT EXISTS AdventurerDiary"
                + "(Username VARCHAR(255) COLLATE utf8_bin,"
                + "EntryId INT, Event VARCHAR(255))";
        PreparedStatement create16 = connection.prepareStatement(statement);

        statement = "CREATE TABLE IF NOT EXISTS CreatureEncyclopedia"
                + "(CreatureID INT PRIMARY KEY AUTO_INCREMENT, Species VARCHAR(255) COLLATE utf8_bin,"
                + "Type  VARCHAR(255), Behavior VARCHAR(255), Habitat VARCHAR(255),"
                + "Drops VARCHAR(255), Weakness VARCHAR(255), Category VARCHAR(255))";
        PreparedStatement create17 = connection.prepareStatement(statement);

        statement = "CREATE TABLE IF NOT EXISTS CreatureCommunityContributions"
                + "(CreatureID INT PRIMARY KEY AUTO_INCREMENT, Species VARCHAR(255) COLLATE utf8_bin,"
                + "Notes VARCHAR(255))";
        PreparedStatement create18 = connection.prepareStatement(statement);

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
        create16.executeUpdate();
        create17.executeUpdate();
        create18.executeUpdate();
    }

    public static void initializeTeleportationPoint() throws SQLException {
            TeleportationNetworkController.Point n1 =  new TeleportationNetworkController.Point("A", "defaultUser", 50,60,null,null,null);
            TeleportationNetworkController.Point n2 =  new TeleportationNetworkController.Point("B", "user1", 200, 100,null,null,null);
            TeleportationNetworkController.Point n3 =  new TeleportationNetworkController.Point("C", "user2", 400, 50,null,null,null);
            TeleportationNetworkController.Point n4 =  new TeleportationNetworkController.Point("D", "user3", 300, 300,null,null,null);
            TeleportationNetworkController.Point[] points = {n1,n2,n3,n4};
            for (TeleportationNetworkController.Point p : points) {
                TeleportationNetworkController.addNewNode(p.getNameOfTeleportationPoint(),p.getOwner(), p.getX(), p.getY(), p.getNeighbours(), p.getFriendRequestsReceived(), p.getFriendWaitingAcceptance());
            }
            n1.sendFriendRequest("B");
            n3.sendFriendRequest("B");
            n4.sendFriendRequest("B");
            n2.acceptFriendRequest("A");
            n2.acceptFriendRequest("C");
            n2.acceptFriendRequest("D");
    }

    public static void initializeItemBox() throws SQLException {
        // database_itemBox.addItem(String username, String itemname, String type, String function, int quantitytoadd)
        //Tool
        //for tool need to add to the toollist table oso 
        database_itemBox.addItem(username, "Blaze Rod", "Weapon", "Act as a fuel for both brewing and smelting", 15);
        database_itemBox.addItem(username, "Bread", "Food", "To be eaten as food or breed Villagers and level up Composters", 15);
        database_itemBox.addItem(username, "Arrow of Slow Falling", "Arrow", "To allow the player to jump across a gap of 5 blocks (6 blocks with enough momentum), compared to 4 (5 blocks with enough momentum) blocks normally", 15);
        database_itemBox.addItem(username, "Elytra", "Transportation", "Use arrows or fireworks as ammunition", 15);
        database_itemBox.addItem(username, "Zombie Villager Spawn Egg", "Mob Egg", "To instantly spawn a zombie villager", 15);
        database_itemBox.addItem(username, "Minecart with Chest", "Transportation", "To be able to interact with hoppers", 15);
        database_itemBox.addItem(username, "Axes", "Tool", "To quickly chop down trees or destroy various other wooden items", 15);
        database_itemBox.addItem(username, "Bucket of Salmon", "Tool", "A bucket of fish places a water source block, and spawns the corresponding fish back into the world", 15);
        database_itemBox.addItem(username, "Arrow of Slowness", "Arrow", "To reduce speed by 15% for 11 seconds", 15);
        database_itemBox.addItem(username, "Melon Slice", "Food", "To craft recipe of glistering melons; 50% chance to raise the compost level of new composter by 1;", 15);
        database_itemBox.addItem(username, "Minecart", "Transportation", "To allow players to travel in them", 15);
        database_itemBox.addItem(username, "Arrow of Fire Resistance", "Arrow", "To grant immunity to damage from fire, blaze fireballs, fire charges, magma blocks, and lava", 15);
        database_itemBox.addItem(username, "Hoe", "Tool", "To turn dirt, grass blocks, and dirt paths into farmland", 15);
        database_itemBox.addItem(username, "Bucket of Water", "Tool", "A bucket to carry water", 15);
        database_itemBox.addItem(username, "Dye Lime", "Dye", "To apply a lime color to objects", 15);
        database_itemBox.addItem(username, "Arrow of Harming", "Arrow", "To shot players or mobs to give instant damage effect and immediately suffer damage to their health", 15);
        database_itemBox.addItem(username, "Dye Orange", "Dye", "To apply an orange color to objects", 15);
        database_itemBox.addItem(username, "Wheat", "Food", "To breed cows, sheep, goats and mooshrooms; craft cake, cookies, packed mud, bread, hay bales; fill up composters", 15);
        database_itemBox.addItem(username, "Blaze Powder", "Weapon", "To fuel brewing stands, to brew strength potions, and to make eyes of ender to take the player to the end", 15);
        database_itemBox.addItem(username, "Diamond", "Material", "To make the best weapons and armour in the game", 15);
        database_itemBox.addItem(username, "Potato", "Food", "To breed Pigs and Villagers", 15);
        database_itemBox.addItem(username, "Brick","Material", "To craft brick blocks, flower pots, and decorated pots",15);
        database_itemBox.addItem(username, "Clay", "Material", "To be smelted in a furnace to create clay bricks which can further be crafted into brick blocks; craft blocks of clay", 15);
        database_itemBox.addItem(username, "Music Disc Wait", "Record", "Play the song with the jukebox", 15);
        database_itemBox.addItem(username, "Golden Horse Armor", "Armor", "To provides protection to the player", 15);
        database_itemBox.addItem(username, "Trident", "Weapon", "To summon a lightning bolt if there is a thunderstorm", 15);
        database_itemBox.addItem(username, "Dye Pink", "Dye", "To apply a pink color to objects", 15);
        database_itemBox.addItem(username, "Apple", "Food", "To be eaten by the player as food items", 15);
        database_itemBox.addItem(username, "Minecart with Furnace", "Transportation", "To climb up steep inclines while pushing other minecarts as long as they have fuel", 15);
        database_itemBox.addItem(username, "Map", "Tool", "To display any and all players in the world and their locations", 15);
        database_itemBox.addItem(username, "Allay Spawn Egg", "Mob Egg", "To instantly spawn an allay", 15);
        database_itemBox.addItem(username, "Swords", "Weapon", "To deal damage to entities or for breaking certain blocks faster than by hand", 15);
        database_itemBox.addItem(username, "Dye Black", "Dye", "To apply a black color to objects", 15);
        database_itemBox.addItem(username, "Sheep Spawn Egg", "Mob Egg", "To instantly spawn a sheep", 15);
        database_itemBox.addItem(username, "Sweet Berries", "Food", "To breed foxes and bought off of Butcher Villagers", 15);
        database_itemBox.addItem(username, "Music Disc Far", "Record", "Play the song with the jukebox", 15);
        database_itemBox.addItem(username, "Cow Spawn Egg", "Mob Egg", "To instantly spawn a cow", 15);
        database_itemBox.addItem(username, "Beetroot", "Food", "Used as food and to dye items", 15);
        database_itemBox.addItem(username, "Raw Salmon", "Food", "To gain ocelot trust, breed ocelots, and make baby ocelots grow up by 10%", 15);
        database_itemBox.addItem(username, "Firework Rocket", "Weapon", "Used as ammunition for crossbows", 15);
        database_itemBox.addItem(username, "Leggings", "Armor", "To covers the lower body of the player", 15);
        database_itemBox.addItem(username, "Crossbow Pull", "Weapon", "Used as fuel in furnaces, smelting 1.5 items per crossbow", 15);
        database_itemBox.addItem(username, "Music Disc Otherside", "Record", "Play the song with the jukebox", 15);
        database_itemBox.addItem(username, "Dye Light Blue", "Dye", "To apply a light blue color to objects", 15);
        database_itemBox.addItem(username, "Redstone", "Material", "To carry power signals between items", 15);
        database_itemBox.addItem(username, "Iron Pickaxe", "Tool", "To mine ores, rocks, rock-based blocks and metal-based blocks quickly and obtain them as items", 15);
        database_itemBox.addItem(username, "Shovels", "Tool", "To hasten the process of breaking dirt, sand, gravel and other soil blocks, as well as to convert dirt blocks into dirt paths", 15);
        database_itemBox.addItem(username, "Chorus Fruit", "Food", "To be eaten as food, or smelted into popped chorus fruit", 15);
        database_itemBox.addItem(username, "Pumpkin", "Food", "To play the didgeridoo and fill up composters", 15);
        database_itemBox.addItem(username, "Clock", "Tool", "The dial spins clockwise slowly to indicate the time of day, corresponding to the sun or moon's actual position in the sky", 15);
        database_itemBox.addItem(username, "Pork Chop", "Food", "To breed and heal tamed wolves, lead them around, and make baby tamed wolves grow up faster by 10% of the remaining time", 15);
        database_itemBox.addItem(username, "Arrow of Luck", "Arrow", "To increase the probability of finding high-quality loot from fishing or naturally-generated chests", 15);
        database_itemBox.addItem(username, "Carrot", "Food", "To breed or attract pigs and rabbits, or to trade with farmer villagers for emerald", 15);
        database_itemBox.addItem(username, "Arrow of Decay", "Arrow", "To apply the wither status effect to whoever is shot", 15);
        database_itemBox.addItem(username, "Stone", "Material", "Used as an ingredient in crafting redstone repeater, or trading for emeralds with stonemasons", 15);
        database_itemBox.addItem(username, "Raw Chicken", "Food", "To be cooked in a Furnace to make a Cooked Chicken", 15);
        database_itemBox.addItem(username, "Dye Cyan", "Dye", "To apply a cyan color to objects", 15);
        database_itemBox.addItem(username, "Dye Purple", "Dye", "To apply a purple color to objects", 15);
        database_itemBox.addItem(username, "Raw Mutton", "Food", "To breed and heal tamed wolves, lead them around, and make baby tamed wolves grow up faster by 10% of the remaining time", 15);
        database_itemBox.addItem(username, "Boots", "Armor", "To safely cross powder snow without sinking in it", 15);
        database_itemBox.addItem(username, "Brick", "Material", "To craft brick blocks, flower pots, and decorated pots", 15);
        database_itemBox.addItem(username, "Dye Yellow", "Dye", "To apply a yellow color to objects", 15);
        database_itemBox.addItem(username, "Oak Wood", "Material", "To turn it all into planks for crafting", 15);
        database_itemBox.addItem(username, "Charcoal", "Material", "Used as fuel, or for crafting torches and campfires", 15);
        database_itemBox.addItem(username, "Compass", "Tool", "Point toward the world spawn point", 15);
        database_itemBox.addItem(username, "Dye Magenta", "Dye", "To apply a magenta color to objects", 15);
        database_itemBox.addItem(username, "Raw Beef", "Food", "To breed and heal tamed wolves, lead them around, and make baby tamed wolves grow up faster by 10% of the remaining time", 15);
        database_itemBox.addItem(username, "Arrow of Poisoned", "Arrow", "To poison any living creature that it hits", 15);
        database_itemBox.addItem(username, "Clay", "Material", "To be smelted in a furnace to create clay bricks which can further be crafted into brick blocks;craft blocks of clay", 15);
        database_itemBox.addItem(username, "Strider Spawn Egg", "Mob Egg", "To instantly spawn a strider", 15);
        database_itemBox.addItem(username, "Arrow of Slow Falling", "Arrow", "To allow the player to jump across a gap of 5 blocks (6 blocks with enough momentum), compared to 4 (5 blocks with enough momentum) blocks normally", 15);
        database_itemBox.addItem(username, "Minecart", "Transportation", "To allow players to travel in them", 15);
        database_itemBox.addItem(username, "Bucket of Water", "Tool", "A bucket to carry water", 15);
        database_itemBox.addItem(username, "Music Disc Cat", "Record", "To play the song when inserted in a jukebox", 15);
        database_itemBox.addItem(username, "Bow", "Weapon", "To take on mobs at a distance, or where sword attacks could endanger a player", 15);
        database_itemBox.addItem(username, "Chestplate", "Armor", "To protect players from damage", 15);
        database_itemBox.addItem(username, "Powder Snow Bucket", "Tool", "To places a powder snow block", 15);
        database_itemBox.addItem(username, "Dye Brown", "Dye", "To apply a brown color to objects", 15);
        database_itemBox.addItem(username, "Clownfish", "Food", "To heal one bar of hunger", 15);
        database_itemBox.addItem(username, "Dye Blue", "Dye", "To apply a blue color to objects", 15);
        
        database_itemBox.removeItem("Carrot", username, 15);
        System.out.println(database_itemBox.retrieveQuantity("Carrot", username));
        database_itemBox.removeItem("Beetroot", username, 15);
        database_itemBox.removeItem("Wheat", username, 15);
        database_itemBox.removeItem("Potato", username, 15);
        database_itemBox.removeItem("Melon Slice", username, 15);
        database_itemBox.removeItem("Pumpkin", username, 15);
        database_itemBox.removeItem("Sweet Berries", username, 15);
    }

    public static void resetAll() throws SQLException {
        Connection connection = getConnection();
        String statement = "DROP TABLE adventurerdiary";
        PreparedStatement drop = connection.prepareStatement(statement);
        statement = "DROP TABLE autofarmscheduling";
        PreparedStatement drop1 = connection.prepareStatement(statement);
        statement = "DROP TABLE automatedsortingchest";
        PreparedStatement drop2 = connection.prepareStatement(statement);
        statement = "DROP TABLE backpackcapacity";
        PreparedStatement drop3 = connection.prepareStatement(statement);
        statement = "DROP TABLE creaturecommunitycontributions";
        PreparedStatement drop4 = connection.prepareStatement(statement);
        statement = "DROP TABLE creatureencyclopedia";
        PreparedStatement drop5 = connection.prepareStatement(statement);
        statement = "DROP TABLE crop";
        PreparedStatement drop6 = connection.prepareStatement(statement);
        statement = "DROP TABLE itembackpack";
        PreparedStatement drop7 = connection.prepareStatement(statement);
        statement = "DROP TABLE itembox";
        PreparedStatement drop8 = connection.prepareStatement(statement);
        statement = "DROP TABLE multitool";
        PreparedStatement drop9 = connection.prepareStatement(statement);
        statement = "DROP TABLE potion";
        PreparedStatement drop10 = connection.prepareStatement(statement);
        statement = "DROP TABLE potionsatchel";
        PreparedStatement drop11 = connection.prepareStatement(statement);
        statement = "DROP TABLE securechest";
        PreparedStatement drop12 = connection.prepareStatement(statement);
        statement = "DROP TABLE securechestsetting";
        PreparedStatement drop13 = connection.prepareStatement(statement);
        statement = "DROP TABLE securechestrequest";
        PreparedStatement drop14 = connection.prepareStatement(statement);
        statement = "DROP TABLE teleportationpoint";
        PreparedStatement drop15 = connection.prepareStatement(statement);
        statement = "DROP TABLE toollist";
        PreparedStatement drop16 = connection.prepareStatement(statement);
        statement = "DROP TABLE userlist";
        PreparedStatement drop17 = connection.prepareStatement(statement);
        drop.executeUpdate();
        drop1.executeUpdate();
        drop2.executeUpdate();
        drop3.executeUpdate();
        drop4.executeUpdate();
        drop5.executeUpdate();
        drop6.executeUpdate();
        drop7.executeUpdate();
        drop8.executeUpdate();
        drop9.executeUpdate();
        drop10.executeUpdate();
        drop11.executeUpdate();
        drop12.executeUpdate();
        drop13.executeUpdate();
        drop14.executeUpdate();
        drop15.executeUpdate();
        drop16.executeUpdate();
        drop17.executeUpdate();
    }
}
