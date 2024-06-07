/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minecraft;

/**
 
 * @author elysi
 */
import java.util.Hashtable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Collections;
import java.util.Comparator;

public class CreatureEncyclopedia {
    public Hashtable<String, Creature> encyclopedia; 
    public static String username;

    // Method to search for creatures based on a search term
    public void searchCreatures(String searchTerm) {
        boolean found = false;
        for (String creatureName : encyclopedia.keySet()) {
            Creature creatureInfo = getCreatureInfo(creatureName);
            if (creatureName.toLowerCase().contains(searchTerm) ||
                creatureInfo.getType().toLowerCase().contains(searchTerm) ||
                creatureInfo.getSpecies().toLowerCase().contains(searchTerm) ||
                creatureInfo.getBehavior().toLowerCase().contains(searchTerm) ||
                creatureInfo.getHabitat().toLowerCase().contains(searchTerm) ||
                creatureInfo.getDrops().toLowerCase().contains(searchTerm) ||
                creatureInfo.getWeakness().toLowerCase().contains(searchTerm)) {

                System.out.println(creatureName + ":");
                System.out.println("Type: " + creatureInfo.getType());
                System.out.println("Species: " + creatureInfo.getSpecies());
                System.out.println("Behavior: " + creatureInfo.getBehavior());
                System.out.println("Habitat: " + creatureInfo.getHabitat());
                System.out.println("Drops: " + creatureInfo.getDrops());
                System.out.println("Weakness: " + creatureInfo.getWeakness());
                List<String> contributions = creatureInfo.getCommunityContributions();
                if (!contributions.isEmpty()) {
                    System.out.println("Community Contributions: ");
                    for (String contribution : contributions) {
                        System.out.println("- " + contribution);
                    }
                }
                System.out.println();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No results found for '" + searchTerm + "'.");
        }
    }
    
     // Method to filter creatures by type
    public List<Creature> filterByType(String type) {
        List<Creature> filteredCreatures = new ArrayList<>();
        for (Creature creature : encyclopedia.values()) {
            if (creature.getType().equalsIgnoreCase(type)) {
                filteredCreatures.add(creature);
            }
        }
        return filteredCreatures;
    }

    // Method to sort creatures alphabetically by name (A-Z)
    public List<Creature> sortCreaturesAZ() {
        List<Creature> sortedCreatures = new ArrayList<>(encyclopedia.values());
        Collections.sort(sortedCreatures, Comparator.comparing(Creature::getSpecies));
        return sortedCreatures;
    }

    // Method to sort creatures alphabetically by name (Z-A)
    public List<Creature> sortCreaturesZA() {
        List<Creature> sortedCreatures = sortCreaturesAZ();
        Collections.reverse(sortedCreatures);
        return sortedCreatures;
    }
    
    public static boolean askToSearchAgain(Scanner scanner) {
        System.out.println("\nDo you want to search again? (yes/no)");
        String choice = scanner.nextLine().toLowerCase();
        return choice.equals("yes");
    }


    public CreatureEncyclopedia() throws SQLException {
        // encyclopedia = new Hashtable<>();
        System.out.println("testing");
        this.encyclopedia = database_item9.retrieveEncyclopedia();
    }

    public Creature getCreatureInfo(String creatureName) {
        return encyclopedia.getOrDefault(creatureName, new Creature("", "", "", "", "", "", "", new ArrayList<String>()));
    }

    public void addNoteToCreature(String creatureName, String note) throws SQLException {
        Creature creature = encyclopedia.get(creatureName);
        if (creature != null) {
            creature.addCommunityContributions(note);
            database_item9.addNote(creatureName, note);
            System.out.println("Note added successfully for " + creatureName);
        } else {
            System.out.println("Creature " + creatureName + " not found in the encyclopedia.");
        }

    }
    
    public static class Creature {
        private String type;
        private String species;
        private String behavior;
        private String habitat;
        private String drops;
        private String weakness;
        private String category;
        private List<String> communityContributions;

        public Creature(String type, String species, String behavior, String habitat, String drops, String weakness, String category, ArrayList<String> notes) {
            this.type = type;
            this.species = species;
            this.behavior = behavior;
            this.habitat = habitat;
            this.drops = drops;
            this.weakness = weakness;
            this.category = category;
            this.communityContributions = notes;

        }

        // Getter methods
        public String getType() {
            return type;
        }

        public String getSpecies() {
            return species;
        }

        public String getBehavior() {
            return behavior;
        }

        public String getHabitat() {
            return habitat;
        }

        public String getDrops() {
            return drops;
        }

        public String getWeakness() {
            return weakness;
        }

        public String getCategory() {
            return category;
        }

        public List<String> getCommunityContributions() {
            return communityContributions;
        }

        public void addCommunityContributions(String contribution) {
            communityContributions.add(contribution);
        }
    }

    
//     public static void main(String[] args) {
//         CreatureEncyclopedia encyclopedia = new CreatureEncyclopedia();
//         Scanner scanner = new Scanner(System.in);
            

//         // Display information for all creatures
//         System.out.println("Creature Encyclopedia:");
//         String[] categories = {"Undead Mobs", "Passive Mobs", "Neutral Mobs", "Arthropods", "Illagers"};

//         for (String category : categories) {
//             System.out.println("\n" + category + ":");
//             for (String creatureName : encyclopedia.encyclopedia.keySet()) {
//                 Creature creatureInfo = encyclopedia.getCreatureInfo(creatureName);

//                 if (creatureInfo.getCategory().equals(category)) {
//                     System.out.println(creatureName + ":");
//                     System.out.println("Type: " + creatureInfo.getType());
//                     System.out.println("Species: " + creatureInfo.getSpecies());
//                     System.out.println("Behavior: " + creatureInfo.getBehavior());
//                     System.out.println("Habitat: " + creatureInfo.getHabitat());
//                     System.out.println("Drops: " + creatureInfo.getDrops());
//                     System.out.println("Weakness: " + creatureInfo.getWeakness());
//                     List<String> contributions = creatureInfo.getCommunityContributions();
//                     if (!contributions.isEmpty()) {
//                         System.out.println("Community Contributions: ");
//                         for (String contribution : contributions) {
//                             System.out.println("- " + contribution);
//                         }
//                     }
//                     System.out.println();
//                 }
//             }
//         }
        
//         // Ask the user if they want to add notes for any creatures
//         System.out.println("Do you want to add a note for any creatures? (yes/no)");
//         String addNoteChoice = scanner.nextLine().toLowerCase();

//         if (addNoteChoice.equals("yes")) {
//         // Allow the user to add notes for multiple creatures
//         while (true) {
//             System.out.println("\nEnter the name of the creature you want to add a note to (or 'exit' to finish):");
//             String creatureName = scanner.nextLine();
//         if (creatureName.equalsIgnoreCase("exit")) {
//             break;
//         }

//         // Check if the creature exists in the encyclopedia
//         if (!encyclopedia.encyclopedia.containsKey(creatureName)) {
//             System.out.println("Creature " + creatureName + " not found in the encyclopedia.");
//             continue;
//         }

//         // Prompt the user to add notes for the creature
//         System.out.println("Enter your note for " + creatureName + ":");
//         while (true) {
//             String note = scanner.nextLine();
//             if (note.equalsIgnoreCase("exit")) {
//                 break;
//             }
//             encyclopedia.addNoteToCreature(creatureName, note);
//         }
//     }
//         } else {
//             System.out.println("No notes will be added.");
// }


//         // Print the information for all creatures
//         System.out.println("\nCreature Encyclopedia After Adding Notes:");
//         for (String category : categories) {
//             System.out.println("\n" + category + ":");
//             for (String creatureName : encyclopedia.encyclopedia.keySet()) {
//                 Creature creatureInfo = encyclopedia.getCreatureInfo(creatureName);

//                 if (creatureInfo.getCategory().equals(category)) {
//                     System.out.println(creatureName + ":");
//                     System.out.println("Type: " + creatureInfo.getType());
//                     System.out.println("Species: " + creatureInfo.getSpecies());
//                     System.out.println("Behavior: " + creatureInfo.getBehavior());
//                     System.out.println("Habitat: " + creatureInfo.getHabitat());
//                     System.out.println("Drops: " + creatureInfo.getDrops());
//                     System.out.println("Weakness: " + creatureInfo.getWeakness());
//                     List<String> contributions = creatureInfo.getCommunityContributions();
//                     if (!contributions.isEmpty()) {
//                         System.out.println("Community Contributions: ");
//                         for (String contribution : contributions) {
//                             System.out.println("- " + contribution);
//                         }
//                     }
//                     System.out.println();
//                 }
//             }
//         }
//              // Search functionality
//             // Search functionality
//         System.out.println("\n=== Search Tool ===");
//     do {
//         System.out.println("Enter your search term:");
//         String searchTerm = scanner.nextLine().toLowerCase();

//         System.out.println("\nSearch Results:");
//         encyclopedia.searchCreatures(searchTerm);
//     } while (askToSearchAgain(scanner));

//         // Filter by type
//         System.out.println("\n=== Filter by Type ===");
//         System.out.println("Enter the type to filter (e.g., 'Mammals', 'Aquatic Mobs'):");
//         String typeFilter = scanner.nextLine();
//         List<Creature> filteredByType = encyclopedia.filterByType(typeFilter);
//         printCreatures(filteredByType);

//         // Sort alphabetically (A-Z)
//         System.out.println("\n=== Sorted Alphabetically (A-Z) ===");
//         List<Creature> sortedAZ = encyclopedia.sortCreaturesAZ();
//         printCreatures(sortedAZ);

//         // Sort alphabetically (Z-A)
//         System.out.println("\n=== Sorted Alphabetically (Z-A) ===");
//         List<Creature> sortedZA = encyclopedia.sortCreaturesZA();
//         printCreatures(sortedZA);

//         scanner.close();
//     }

    // Method to print creatures
    private static void printCreatures(List<Creature> creatures) {
        if (creatures.isEmpty()) {
            System.out.println("No creatures found.");
        } else {
            for (Creature creature : creatures) {
                System.out.println(creature.getSpecies() + ":");
                System.out.println("Type: " + creature.getType());
                System.out.println("Behavior: " + creature.getBehavior());
                System.out.println("Habitat: " + creature.getHabitat());
                System.out.println("Drops: " + creature.getDrops());
                System.out.println("Weakness: " + creature.getWeakness());
                List<String> contributions = creature.getCommunityContributions();
                if (!contributions.isEmpty()) {
                    System.out.println("Community Contributions: ");
                    for (String contribution : contributions) {
                        System.out.println("- " + contribution);
                    }
                }
                System.out.println();
            }
        }
    }
}

    


