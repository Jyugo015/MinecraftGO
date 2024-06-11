/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minecraft;

/**
 *
 * @author elysi
 */
import java.util.LinkedHashMap;
import java.util.Map;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

class AdventurerDiary extends AdventurerDiaryController{

    private Map<Integer, String> entries;
    private int entryCounter;
    private String user;

    /**
     * Constructs an AdventurerDiary with the specified username and initializes the diary entries.
     *
     * @param username the username of the adventurer
     * @throws SQLException if there is an error retrieving entries from the database
     */
    public AdventurerDiary(String username) throws SQLException {
        this.entries = database_item8.retrieveEntries(username);
        this.entryCounter = entries.size() + 1;
        this.user = username;
    }

     /**
     * Logs an event in the diary with the current timestamp.
     *
     * @param eventDescription a description of the event
     * @throws SQLException if there is an error adding the entry to the database
     */
    public void logEvent(String eventDescription) throws SQLException {
        LocalDateTime timestamp = LocalDateTime.now();
        String formattedTimestamp = timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String entry = formattedTimestamp + " - " + eventDescription;
        database_item8.addEntry(user, this.entryCounter, entry);
        this.entries.put(this.entryCounter++, entry);
        System.out.println("Event logged: " + entry);
    }

    /**
     * Displays all entries in the adventurer's diary.
     */
    public void displayEntries() {
        System.out.println("Adventurer's Diary Entries:");
        for (Map.Entry<Integer, String> entry : this.entries.entrySet()) {
            System.out.println("[entry " + entry.getKey() + "]");
            System.out.println("timestamp: " + entry.getValue().split(" - ")[0]);
            System.out.println("description: " + entry.getValue().split(" - ")[1]+ "\n");
        }
    }

    
    public Map<Integer, String> getEntries() {
        return entries;
    }

    /**
     * Retrieves a specific entry from the diary.
     *
     * @param entryNumber the number of the entry to retrieve
     * @return the entry with the specified number, or "Invalid entry number" if not found
     */
    public String shareEntry(int entryNumber) {
        return entries.getOrDefault(entryNumber, "Invalid entry number");
    }

    
    /**
     * Allows the adventurer to share one or more diary entries with others.
     *
     * @param toShare a list of entries to share
     */
    public void shareEvent(ArrayList<String> toShare) {
        Scanner scanner = new Scanner(System.in);
        boolean continueSharing = true;
        List<String> sharedEvents = new ArrayList<>();
        //controller class straight away pass the list to the method

        while (continueSharing) {
            System.out.println("\nDo you want to share an event? (Enter the number of the entry or 'exit' to stop)");
            String input = scanner.next();

            if (input.equals("exit")) {
                continueSharing = false;
                continue;
            }

            int entryToShare = Integer.parseInt(input);//select one event to share 
            if (entryToShare != 0) {
                String sharedEvent = shareEntry(entryToShare);
                sharedEvents.add(sharedEvent);
                System.out.println("Shared Entry:");
                System.out.println(sharedEvent);
            }

            // Ask if the user wants to share another event
            System.out.println("\nDo you want to share another event? (yes/no)");
            String shareAgainChoice = scanner.next().toLowerCase();
            if (shareAgainChoice.equals("no")) {
                continueSharing = false;
            }
        }

        // Display all shared events
        System.out.println("\nShared Events:");
        for (String sharedEvent : sharedEvents) {
            System.out.println(sharedEvent);
        }
    }
    
     /**
     * Searches for diary entries containing a specific keyword.
     */
 
    public void searchEvent() {
        Scanner scanner = new Scanner(System.in);
        boolean searchAgain = true;

        while (searchAgain) {
            System.out.println("\nDo you want to search an event? (Enter the keyword of the entry or 'exit' to stop)");
            String keyword = scanner.next().toLowerCase();

            if (keyword.equals("exit")) {
                break;
            }

            boolean found = false;
            System.out.println("Search result:");
            for (Map.Entry<Integer, String> entry : this.entries.entrySet()) {
                String description = entry.getValue().split(" - ")[1];
                if (description.toLowerCase().contains(keyword)) {
                    found = true;
                    System.out.println(entry.getValue());
                    break; // Exit loop after finding the first match
                }
            }

            if (!found) {
                System.out.println("No matching events found.");
            }

            // Ask if the user wants to search another event
            System.out.println("\nDo you want to search for another event? (yes/no)");
            String searchAgainChoice = scanner.next().toLowerCase();
            if (searchAgainChoice.equals("no")) {
                searchAgain = false;
            }
        }
    }

    
    /**
     * Verifies all events in the diary.
     */
    public void verifyAllEvents() {
        System.out.println("Verifying all events:");
        for (String entry : this.entries.values()) {
            System.out.println(entry);
            // Extract event description
            String eventDescription = entry.split(" - ")[1];
            // Verify event
            verifyEvent(eventDescription);
            System.out.println(); // Print a blank line for separation
        }
    }

    /**
     * Verifies a specific event in the diary.
     *
     * @param eventDescription the description of the event to verify
     */
    public void verifyEvent(String eventDescription) {
        boolean found = false;
        for (String entry : this.entries.values()) {
            if (entry.contains(eventDescription)) {
                found = true;
                break;
            }
        }
        if (found) {
            System.out.println("Event verified: true");
        } else {
            System.out.println("Event verified: false");
        }
    }


    // public static void main(String[] args) {

    //     AdventurerDiary diary = new AdventurerDiary();
    //     diary.logEvent("Player joined the game");
    //     diary.logEvent("Achievement earned: DIAMONDS!");
    //     diary.logEvent("Discovered a village");
    //     diary.logEvent("Completed the Ender Dragon challenge");
    //     diary.displayEntries();

    //     diary.shareEvent();
    //     diary.searchEvent();

    //     // Verify all events
    //     diary.verifyAllEvents();
    // }
}
