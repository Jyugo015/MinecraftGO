package minecraft;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import minecraft.CreatureEncyclopedia.Creature;

public class database_item9 {

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

    public static void initialize() throws SQLException{
        //undead mobs
        addCreature("Undead mobs", "Skeletons", "Hostile, armed with bows, shoot arrows at players and other mobs.", "Spawn at night or in dark areas.", "Arrows, bones, and rarely, bows or enchanted bows.", "Vulnerable to close-range attacks due to lack of melee capabilities. They are also susceptible to sunlight, which causes them to burn during the day.", "Undead Mobs");
        addCreature("Undead mobs", "Drowned", "Hostile underwater zombies, wield tridents.", "Spawn underwater in oceans, rivers, and underwater ruins.", "Rotten flesh, occasionally tridents, and fishing loot.", "Vulnerable to attacks from weapons, particularly those with sweeping or knockback effects. They are also slow on land compared to water.", "Undead Mobs");
        addCreature("Undead mobs", "Husks", "Hostile zombies found in desert biomes, immune to sunlight.", "Spawn in desert biomes, desert temples, and desert villages.", "Rotten flesh, occasionally iron ingots, carrots, or potatoes.", "Vulnerable to water and fire damage. Their melee attacks do not inflict any additional effects, making them relatively straightforward to handle.", "Undead Mobs");
        addCreature("Undead mobs", "Skeleton horses", "Passive mobs that spawn from lightning strikes, ridden by skeleton horsemen.", "Rarely spawn in the Overworld during thunderstorms.", "None, but can be tamed and ridden by players.", "Vulnerable to attacks, but they do not actively attack players unless ridden by a skeleton horseman.", "Undead Mobs");
        addCreature("Undead mobs", "Withers", "Powerful boss mobs, shoot explosive skulls, regenerate health over time.", "Created by players using soul sand and wither skeleton skulls.", "Nether star, used to craft beacons.", "Vulnerable to attacks from players using ranged or melee weapons. They can also be slowed down by the effects of potions such as slowness.", "Undead Mobs");
        addCreature("Undead mobs", "Wither Skeletons", "Hostile skeletons found in Nether fortresses.", "Spawn in Nether fortresses, particularly in fortress corridors.", "Bones, coal, and rarely, wither skeleton skulls.", "Vulnerable to attacks, particularly from weapons with sweeping or knockback effects. Susceptible to sunlight and fire damage.", "Undead Mobs");
        addCreature("Undead mobs", "Zombie villagers", "Hostile variants of villagers.", "Spawn when a villager is killed by a zombie.", "Rotten flesh, occasionally carrots, potatoes, or iron ingots.", "Vulnerable to attacks, particularly from weapons with sweeping or knockback effects. Can be cured by using a potion of weakness and a golden apple.", "Undead Mobs");
        addCreature("Flying mobs", "Phantom", "Hostile flying mobs that spawn if players haven't slept for several in-game days.", "Spawn in the Overworld, particularly in the sky.", "Phantom membrane, used to repair elytra.", "Vulnerable to ranged attacks, particularly arrows or projectiles. Takes increased damage from sweeping or knockback attacks.", "Undead Mobs");
        addCreature("Undead mobs", "Strays", "Hostile skeletons found in snowy biomes, shoot arrows of slowness.", "Spawn in snowy biomes, particularly during snowstorms.", "Bones, arrows, and occasionally, tipped arrows of slowness.", "Vulnerable to attacks, particularly from weapons with sweeping or knockback effects. Susceptible to fire and sunlight damage.", "Undead Mobs");
        addCreature("Neutral mobs", "Zombie Piglins", "Neutral mobs found in the Nether, become aggressive if attacked.", "Spawn in the Nether, particularly in Nether wastes and Crimson forests.", "Gold nuggets, rotten flesh, and occasionally, gold ingots or enchanted gold items.", "Vulnerable to attacks, particularly from weapons with sweeping or knockback effects. Can be distracted by gold items.", "Undead Mobs");
        addCreature("Undead mobs", "Zoglins", "Hostile mobs resulting from zombifying hoglins, aggressive towards players and other mobs.", "Spawn when a hoglin enters the Overworld or when a hoglin is zombified.", "Rotten flesh, occasionally leather.", "Vulnerable to attacks, particularly from weapons with sweeping or knockback effects. Susceptible to fire and sunlight damage.", "Undead Mobs");

        //passive mobs: do not attack player
        addCreature("Birds", "Chickens", "Walk around and lay eggs.", "Found in grassy areas, particularly in villages and farms.", "Raw chicken, feathers, and occasionally eggs.", "Vulnerable to attacks, particularly from hostile mobs or players. Low health.", "Passive Mobs");
        addCreature("Mammals", "Cows", "Graze and can be milked.", "Found in grassy areas, particularly in plains and villages.", "Raw beef, leather, and occasionally milk.", "Vulnerable to attacks, particularly from hostile mobs or players. Low health.", "Passive Mobs");
        addCreature("Mammals", "Horses", "Can be tamed and ridden.", "Found in plains and savannas, often in herds.", "None.", "Vulnerable to attacks, particularly from hostile mobs or players. May buck off inexperienced riders.", "Passive Mobs");
        addCreature("Mammals", "Mooshrooms", "Similar to cows but with mushroom growth.", "Found in mushroom fields and mushroom biomes.", "Raw beef, leather, and occasionally mushrooms.", "Vulnerable to attacks, particularly from hostile mobs or players. Low health.", "Passive Mobs");
        addCreature("Mammals", "Rabbits", "Hop around and can be bred with carrots.", "Found in grassy areas, particularly in forests and plains.", "Rabbit hide, raw rabbit, and occasionally rabbit's foot.", "Vulnerable to attacks, particularly from hostile mobs or players. Low health.", "Passive Mobs");
        addCreature("Mammals", "Sheep", "Graze and can be sheared for wool.", "Found in grassy areas, particularly in plains and mountains.", "Wool (when sheared), raw mutton, and occasionally lamb chops.", "Vulnerable to attacks, particularly from hostile mobs or players. Low health.", "Passive Mobs");

        //neutral mobs: attack players if provoked or under certain conditions
        addCreature("Neutral Mobs", "Wolves", "Neutral, become hostile when attacked or when a nearby wolf is attacked.", "Found in forests and taiga biomes, often in packs.", "Bones and occasionally wolf pelts.", "Vulnerable to attacks, particularly from ranged weapons. Can be tamed with bones.", "Neutral Mobs");
        addCreature("Neutral Mobs", "Bats", "Passive mobs that fly around in dark areas.", "Spawn in dark areas, particularly in caves and underground structures.", "None.", "Vulnerable to attacks, but they rarely pose a threat to players due to their passive nature.", "Neutral Mobs");

        //aquatic mobs
        addCreature("Aquatic Mobs", "Axolotls", "Passive aquatic mobs found in underground water sources, can regenerate health.", "Spawn in lush caves in underground water sources.", "None.", "Vulnerable to attacks, particularly from hostile mobs. Cannot survive out of water for extended periods.", "Aquatic Mobs");
        addCreature("Aquatic Mobs", "Dolphins", "Passive aquatic mobs, swim in groups, can lead players to shipwrecks and ruins.", "Spawn in ocean biomes, particularly in groups near shorelines.", "Raw cod or raw salmon when killed.", "Vulnerable to attacks, particularly from hostile mobs or players. Cannot survive out of water for extended periods.", "Aquatic Mobs");
        addCreature("Aquatic Mobs", "Squids", "Passive aquatic mobs, swim in groups, ink clouds when attacked.", "Spawn in ocean biomes, particularly in deep ocean biomes.", "Ink sacs used for crafting dyes.", "Vulnerable to attacks, particularly from hostile mobs or players. Cannot survive out of water for extended periods.", "Aquatic Mobs");
        addCreature("Aquatic Mobs", "Glow squid", "Passive aquatic mobs that emit light underwater.", "Spawn in ocean biomes, particularly in deep ocean biomes.", "Glow ink sacs, which emit light when used to craft signs and item frames.", "Vulnerable to attacks, particularly from hostile mobs or players. Cannot survive out of water for extended periods.", "Aquatic Mobs");
        addCreature("Aquatic Mobs", "Guardians", "Hostile aquatic mobs found in ocean monuments, shoot laser beams.", "Spawn in ocean monuments, particularly in water chambers.", "Prismarine shards, prismarine crystals, fish, and rarely, sponge blocks.", "Vulnerable to attacks, particularly from ranged weapons. Can be temporarily distracted by blocks or entities placed between them and their target.", "Aquatic Mobs");
        addCreature("Aquatic Mobs", "Elder guardians", "Hostile aquatic mobs, more powerful variants of guardians.", "Spawn in ocean monuments, particularly near treasure rooms.", "Same as regular guardians.", "Vulnerable to attacks, particularly from ranged weapons. Can be temporarily distracted by blocks or entities placed between them and their target.", "Aquatic Mobs");
        addCreature("Aquatic Mobs", "Turtles", "Passive aquatic mobs, lay eggs on beaches, can be bred with seagrass.", "Spawn on beaches, particularly in warm ocean biomes.", "Seagrass, scute (when baby turtles grow into adults).", "Vulnerable to attacks, particularly from hostile mobs or players. Slow movement on land makes them susceptible to predators.", "Aquatic Mobs");
        addCreature("Aquatic Mobs", "Cod", "Passive aquatic mobs, swim in schools, different varieties found in different biomes.", "Spawn in ocean biomes, particularly in specific biome types.", "Raw fish of their respective types when killed.", "Vulnerable to attacks, particularly from predators or hostile mobs. Limited mobility out of water makes them easy targets for land-based predators.", "Aquatic Mobs");

        //arthropods
        addCreature("Neutral Mobs", "Bees", "Passive mobs that pollinate flowers and attack when provoked.", "Found in flower-filled biomes, particularly near beehives.", "Honeycomb and honey bottles.", "Vulnerable to attacks when provoked, but they retaliate in groups. Smoke from a campfire or a beehive calms them, making them less aggressive.", "Arthropods");
        addCreature("Hostile Mobs", "Cave spiders", "Hostile spiders found in cave systems, poison players with their attacks.", "Spawn in abandoned mineshafts and cave systems.", "Spider eyes and occasionally, spider webs.", "Vulnerable to attacks, particularly from weapons with sweeping or knockback effects. Susceptible to sunlight and fire damage.", "Arthropods");
        addCreature("Hostile Mobs", "Endermites", "Hostile mobs that spawn when an ender pearl is used.", "Spawn in the End, particularly near teleporting players or ender pearls.", "None.", "Vulnerable to attacks, particularly from weapons with sweeping or knockback effects. Limited mobility makes them easy to defeat.", "Arthropods");
        addCreature("Hostile Mobs", "Silverfish", "Hostile mobs found in strongholds, hide in blocks and swarm when attacked.", "Spawn in strongholds, particularly in stone bricks.", "None.", "Vulnerable to attacks, particularly from weapons with sweeping or knockback effects. Susceptible to sunlight and fire damage.", "Arthropods");
        addCreature("Hostile Mobs", "Spiders", "Hostile mobs found in various biomes, capable of climbing walls.", "Spawn in dark areas, particularly in forests and caves.", "String and spider eyes.", "Vulnerable to attacks, particularly from weapons with sweeping or knockback effects. Susceptible to sunlight and fire damage.", "Arthropods");

        //illagers -evil counterparts to the peaceful villagers found in villages
        addCreature("Hostile Mobs", "Pillagers", "Hostile mobs that attack players and villagers, often found in patrols or pillager outposts.", "Spawn in patrols and pillager outposts in various biomes.", "Crossbows, arrows, emeralds, and occasionally, ominous banners.", "Vulnerable to attacks, particularly from ranged weapons. Can be distracted by other mobs or players.", "Illagers");
        addCreature("Hostile Mobs", "Illusioners", "Hostile illusion-casting mobs, rare and difficult to find.", "Rarely spawn in woodland mansions or through commands.", "None.", "Vulnerable to attacks, particularly from ranged weapons. Can be countered by focusing on the real illusioner rather than its illusions.", "Illagers");
        addCreature("Hostile Mobs", "Ravagers", "Hostile mobs that charge and attack players and villagers, often accompanied by pillagers.", "Found in patrols, pillager outposts, and raids.", "Saddle, and occasionally, emeralds or crossbows.", "Vulnerable to attacks, particularly from ranged weapons. Can be distracted by other mobs or players.", "Illagers");
        addCreature("Hostile Mobs", "Evokers", "Hostile spell-casting mobs found in woodland mansions, summon vexes and attack players.", "Spawn in woodland mansions, particularly in evoker rooms.", "Totem of undying, emeralds, and occasionally, spell books.", "Vulnerable to attacks, particularly from ranged weapons. Can be countered by quickly defeating them to prevent them from summoning vexes.", "Illagers");
        addCreature("Hostile Mobs", "Vindicators", "Hostile mobs that wield axes and attack players and villagers.", "Spawn in raids, particularly during waves of illagers.", "Emeralds and occasionally, axes or enchanted books.", "Vulnerable to attacks, particularly from ranged weapons. Can be distracted by other mobs or players.", "Illagers");
        addCreature("Passive Mobs", "Villagers", "Passive mobs found in villages, trade with players and perform various professions.", "Spawn in villages, particularly in houses and buildings.", "None.", "Vulnerable to attacks, particularly from hostile mobs. They have limited ability to defend themselves.", "Illagers");
        addCreature("Passive Mobs", "Wandering Traders", "Passive mobs that spawn randomly in the world, trade various items with players.", "Roam around the world, often found in open areas or near player-built structures.", "None.", "Vulnerable to attacks, particularly from hostile mobs. They have limited ability to defend themselves.", "Illagers");
        addCreature("Neutral Mobs", "Iron Golems", "Neutral mobs that protect villagers and attack hostile mobs.", "Spawn in villages, particularly when a sufficient number of villagers are present.", "Iron ingots and poppies.", "Vulnerable to attacks, particularly from mobs with high damage output. Can be lured away from villages to weaken their defense.", "Illagers");
        addCreature("Hostile Mobs", "Witches", "Hostile spell-casting mobs that attack players with harmful potions.", "Spawn in witch huts and during raids.", "Redstone, glowstone dust, sticks, sugar, spider eyes, potions, and occasionally, glass bottles.", "Vulnerable to attacks, particularly from ranged weapons. Can be countered by avoiding their potion attacks and using cover to approach them safely.", "Illagers");
        addCreature("Hostile Mobs", "Vexes", "Hostile flying mobs summoned by evokers during raids, attack players.", "Spawn during raids summoned by evokers.", "None.", "Vulnerable to attacks, particularly from ranged weapons. Can be difficult to hit due to their flying behavior.", "Illagers");
    }

    public static Hashtable<String, Creature> retrieveEncyclopedia() throws SQLException{
        Hashtable<String, Creature> encyclopedia = new Hashtable<String, Creature>();
        Connection connection = getConnection();
        String statement = "SELECT * FROM creatureEncyclopedia";
        PreparedStatement retrieve = connection.prepareStatement(statement);
        ResultSet result = retrieve.executeQuery();
        while (result.next()){
            String species = result.getString("Species");
            String type = result.getString("Type");
            String behavior = result.getString("behavior");
            String habitat = result.getString("habitat");
            String drops = result.getString("drops");
            String weakness = result.getString("Weakness");
            String category = result.getString("category");
            ArrayList<String> communitycontributions = new ArrayList<String>();
            statement = "SELECT notes FROM creaturecommunitycontributions WHERE species =?";
            PreparedStatement query = connection.prepareStatement(statement);
            query.setString(1, species);
            ResultSet result1 = query.executeQuery();
            while (result1.next()){
                communitycontributions.add(result1.getString("Notes"));
            }
            encyclopedia.put(species, new Creature(type, species, behavior, habitat, 
                                                   drops, weakness, category, communitycontributions));
        }
        return encyclopedia;
    }

    public static void addNote(String creatureName, String note) throws SQLException{
        Connection connection = getConnection();
        String statement  = "INSERT INTO creaturecommunitycontributions (species, notes) VALUES (?,?)";
        PreparedStatement insert = connection.prepareStatement(statement);
        insert.setString(1, creatureName);
        insert.setString(2,note);
        insert.executeUpdate();
    }

    public static void addCreature
    (String type, String species, String behavior, String habitat, String drops, String weakness, String category)
    throws SQLException{
        Connection connection = getConnection();
        String statement = "INSERT INTO creatureencyclopedia "
                            + "(type, species, behavior, habitat, drops, weakness, category) "
                            + "VALUES (?,?,?,?,?,?,?)";
        PreparedStatement insert = connection.prepareStatement(statement);
        insert.setString(1, type);
        insert.setString(2, species);
        insert.setString(3, behavior);
        insert.setString(4, habitat);
        insert.setString(5, drops);
        insert.setString(6, weakness);
        insert.setString(7, category);
        insert.executeUpdate();
    }
}
