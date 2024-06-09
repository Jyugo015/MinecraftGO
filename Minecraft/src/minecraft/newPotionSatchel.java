/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minecraft;

import java.util.Map;
import java.util.Scanner;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;

/**
 *
 * @author elysi
 */
public class newPotionSatchel extends Potions{
    private Potion head;
    private int size;
    private Potion currentPotion;
    
      
    /**
     * Constructs a new PotionSatchel with default values.
     */
    public newPotionSatchel() {
        Potions potions = new Potions();
        
        this.head = null;
        this.size = 0;
        this.currentPotion = null;
    }
     
     /**
     * Gets the size of the satchel.
     *
     * @return the size of the satchel
     */
    public int getSize(){
        return size;
    }

     /**
     * Gets the head potion of the satchel.
     *
     * @return the head potion of the satchel
     */
    public Potion getHead(){
        return this.head;
    }
     
    /**
     * Adds a potion to the satchel based on its index in the potion map.
     *
     * @param index the index of the potion in the potion map
     */
    public void addPotionByIndex(int index) {
        if (index >= 1 && index <= getPotionsMap().size()) {
            Scanner scanner = new Scanner(System.in);
            
            int count = 1;
            for (Map.Entry<String, Potion> entry : getPotionsMap().entrySet()) {
                if (count == index) {
                    Potion potionToAdd = entry.getValue();
                    // Add potion to the satchel (you need to implement this)
                    addPotionToSatchel(potionToAdd);
                    System.out.println(potionToAdd.getName() + " added to the potion satchel.");
                    return;
                }
                count++;
            }
        } else {
            System.out.println("Invalid index. Potion could not be added to the satchel.");
        }
    }
    
    
    /**
     * Adds a potion to the satchel.
     *
     * @param potion the potion to add
     */
    public void addPotionToSatchel(Potion potion){
        Potion newNode = new Potion(potion.getName(), potion.getPotency(), potion.getEffect());
        
        //if the satchel is empty, set the new node as the head
        if(head==null){
            head= newNode;
        }else{
            Potion last = head;
            while(last.nextPotion != null){
                last = last.nextPotion;
            }
            last.nextPotion = newNode;
        }
        size++;
    }
    
    
    /**
     * Displays the contents of the potion satchel.
     */
    public void displaySatchelContents(){
        //Start from the head of the Satchel
        Potion temp = head;
        //Iterate through the satchel
        while(temp!= null){
            System.out.println(temp);
            temp = temp.nextPotion;
        }
    }

    /**
     * Removes a potion from the satchel.
     *
     * @param potion the potion to remove
     * @throws SQLException if there is an error during database interaction
     */
    public void removePotion(Potion potion) throws SQLException{
        int index = -1, tempIndex=1;
        Potion current = head;
        while (current.nextPotion !=null){
            if (current.equals(potion)){
                index = tempIndex;
                break;
            }
            current = current.nextPotion;
            tempIndex++;
        }
        removePotionByIndex(index);
    }
    
    /**
     * Removes a potion from the satchel based on its index.
     *
     * @param index the index of the potion to remove
     * @throws SQLException if there is an error during database interaction
     */
    public void removePotionByIndex(int index) throws SQLException {
    if (index >= 1 && index <= size) {
        if (index == 1) { 
            head = head.nextPotion; 
            
        } else {
           
            Potion previous = head;
            
            for (int i = 1; i < index - 1; i++) {
                previous = previous.nextPotion;
            }
            
            previous.nextPotion = previous.nextPotion.nextPotion;
        }
        size--;
        System.out.println("Potion at index " + index + " removed from the potion satchel.");
        } else {
            System.out.println("Invalid index. No potion removed from the satchel.");
        }
    }
      
    public void displayPotionsForPvP() {
        System.out.println("\nPotions for PvP Battle:");
        Potion temp = head;
        while (temp != null) {
            if (temp.getPotency() < 50) {
                System.out.println("- " + temp.getName());
            }
            temp = temp.nextPotion;
        }
    }

    public void displayPotionsForBossFight() {
        System.out.println("\nPotions for Boss Fight:");
        Potion temp = head;
        while (temp != null) {
            if (temp.getPotency() == 50) {
                System.out.println("- " + temp.getName());
            }
            temp = temp.nextPotion;
        }
    }
    
    
    public void displayPotionsForHazardousAdventure() {
        System.out.println("\nPotions for Hazardous Adventure:");
        Potion temp = head;
        while (temp != null) {
            if (temp.getPotency() > 50) {
                System.out.println("- " + temp.getName());
            }
            temp = temp.nextPotion;
        }
    }
    
    public void useFirstPotionAutomatically(PotionSatchelController controller) throws SQLException, InterruptedException {
        // Timer timer = new Timer();
        // timer.schedule(new TimerTask() {
            // @Override
            // public void run() {
                newPotionSatchel satchel = controller.getPotionSatchel();
                List<Potion> selectedPotions = controller.getSelectedPotions();
                int totalPotionsAdded = controller.getTotalPotionsAdded();
                if (satchel.getHead() != null) {
                    System.out.println("\nUsing potion automatically: " + satchel.getHead().getName());
                    // Remove the used potion from the satchel
                    database_item3.removePotionSatchel("defaultUser", satchel.getHead().getName(), 
                                                satchel.getHead().getPotency(), satchel.getHead().getEffect());
                    database_item3.removePotion("defaultUser", satchel.getHead().getName());
                    
                    if (size == 0) {
                        System.out.println("\nNo more potions in the satchel. Automatic usage stopped.");
                        // timer.cancel();
                    }
                    Thread.sleep(5000);
                    Platform.runLater(() -> {
                        try {
                            selectedPotions.stream().filter(e->satchel.getHead().getName().equals(e.getName()))
                                                    .findFirst().ifPresent(e->selectedPotions.remove(e));
                            satchel.head = satchel.head.nextPotion;
                            satchel.size--;
                            controller.setTotalPotionsAdded(satchel.getSize());
                            controller.updateSelectedPotionsGrid();
                            controller.displayUsePotion();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                } 
                // else {
                //     System.out.println("\nNo more potions in the satchel.");
                    // timer.cancel();
                // }
            // }
        // }, 0, 15000); // 15 seconds interval
    }
    
    //method to clear all potion in the satchel
    public void clearSatchel() {
        head = null; //set head to null to clear all potion
        size = 0; //reset the size to 0
    }

    //  public static void main(String[] args) throws SQLException {
    //     newPotionSatchel satchel = new newPotionSatchel();
    //     Scanner scanner = new Scanner(System.in);
        
    //     // Show all potions
    //     System.out.println("Potions:");
    //     int index=1;
    //     for (Map.Entry<String, Potion> entry : satchel.getPotionsMap().entrySet()) {
    //          System.out.println(index + ". " + entry.getKey() + ": " + entry.getValue());
    //             index++;
    //     }
    //     int totalPotions = 0;
    //     while (totalPotions <= 0 || totalPotions > 10) {
    //         System.out.println("Enter the number of potions you want to add to the satchel (max 10): ");
    //         totalPotions = scanner.nextInt();
    //         if (totalPotions <= 0 || totalPotions > 10) {
    //             System.out.println("Invalid number of potions. Please enter a number between 1 and 10.");
    //         }
    //     }
        
    //     // Track the number of potions selected by the user
    //     int selectedPotions = 0;
    //     for (int i = 0; i < totalPotions; i++) {
    //         System.out.println("Enter the index of potion " + (i + 1) + " you want to add to the satchel: ");
    //         int potionIndex = scanner.nextInt();
    //         satchel.addPotionByIndex(potionIndex);
    //         selectedPotions++; //increment the count of selected potions
    //     }
            
    //      // Display the done button if the selected and total potions are equal
    //     if (selectedPotions == totalPotions) {
    //         System.out.println("\nPress 'done' to proceed.");
    //         String done = scanner.next();
    //         if (!done.equalsIgnoreCase("done")) {
    //             System.out.println("Invalid input. Exiting.");
    //             return;
    //         }
    //     } else {
    //         System.out.println("You haven't selected the desired number of potions. Exiting.");
    //         return;
    //     }


    //     System.out.println("\nPotion Satchel Contents:");
    //     satchel.displaySatchelContents();
        
        
    //     // Ask player if they want to remove potions by index
    //     System.out.println("\nDo you want to remove potions by index? (yes/no)");
    //     String removeChoice = scanner.next();

    //     if (removeChoice.equalsIgnoreCase("yes")) {
    //         int maxPotionsToRemove = satchel.getSize();
    //         // Ask player how many potions they want to remove from the potion satchel
    //         System.out.println("\nEnter the number of potions you want to remove from the satchel: " + maxPotionsToRemove + "): ");
    //         int removeCount = scanner.nextInt();
            
    //     // Remove potions
    //         if(removeCount <= maxPotionsToRemove){
    //             for (int i = 0; i < removeCount; i++) {
    //                 System.out.println("Enter the index of potion " + (i + 1) + " you want to remove from the satchel: ");
    //                 int potionIndexToRemove = scanner.nextInt();
    //                 satchel.removePotionByIndex(potionIndexToRemove);
    //             }
                
    //             System.out.println("\nPress 'done' to proceed. ");
    //             String doneAfterRemove = scanner.next();
    //             if(!doneAfterRemove.equalsIgnoreCase("done")){
    //                 System.out.println("Invalid Input. Exiting");
    //             }   

    //             System.out.println("\nPotion Satchel Contents after removal:");
    //             satchel.displaySatchelContents();
                
    //         }else{
    //             System.out.println("Invalid number of potions to remove. The number of potions you want to remove excess the total number of potions in the potion satchel. ");
    //         }
        
    //     } else if (removeChoice.equalsIgnoreCase("no")) {
    //         System.out.println("Quit.");
            
    //     } else {
    //     System.out.println("Invalid choice. Quitting.");
    //     }

    //     satchel.displayPotionsForPvP();
    //     satchel.displayPotionsForBossFight();
    //     satchel.displayPotionsForHazardousAdventure(); 
    //     satchel.useFirstPotionAutomatically();
    // }
}
