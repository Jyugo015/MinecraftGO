/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minecraft;

/**
 *
 * @author USER
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.beans.binding.IntegerBinding;

public class SecureChest {
    private String owner;
    private String name;
    private Map<String, Integer> accessPermissions; 
    // Map to store access permissions for other user to this securechest
    private String securityLevel; 
    // private Contents content;
    private Map<String, Integer> accessRequestsFromOther; 
    // Map to store pending access requests for this securechest
    
    //access level 
    // NO_ACCESS = 0;
    // VIEW_ONLY = 1;
    // FULL_ACCESS = 2;

    //access level
    //View Only = 1
    //Full Access = 2

    //security level
    //Public, Private, Self-defined
    //default security level (when new user sign up) = Private

    private Map<EnderBackpackItem, Integer> itemChest;
    //map to store the item in the securechest
    //map to store the securechest that is requested by this owner and the permission type requested
    
    public SecureChest(String owner) throws SQLException{
        this(owner, database_item7.retrieveChestName(owner));
    }

    public SecureChest(String owner, String name) throws SQLException {
        // accessPermissions.put(owner,2);
        // securityLevel = SecurityLevel.PRIVATE; // Default security level
        // to be set when new user registered
        this(owner, name, database_item7.retrieveSecurityLevel(owner));
    }

    public SecureChest(String owner, String name, String securityLevel) throws SQLException {
        this.owner = owner;
        this.name = name;
        this.securityLevel = securityLevel;
        accessRequestsFromOther = database_item7.retrieveAccessRequestMy(owner, "Pending");
        accessPermissions = database_item7.retrieveAccessPermission(owner);
        //if public, retrieve all users and set with permission type full access
        itemChest = database_item7.retrieveItemChest(owner);
        System.out.println("run");
    }
    
    //cus all the attibutes are in private modifier so have to have getter method for obtaining the attibutes
    public Map<EnderBackpackItem, Integer> getItemChest(){
        return this.itemChest;
    }

    public Map<String, Integer> getAccessRequests(){
        return this.accessRequestsFromOther;
    }

    public Map<String, Integer> getAccessPermissions(){
        return this.accessPermissions;
    }

    public String getOwner() {
        return this.owner;
    }

    public String getName(){
        return this.name;
    }

    // Method to get the security level of the chest (in String dt)
    public String getSecurityLevel() {
        return this.securityLevel;
    }

    // Get the list of players with access to the chest
    public List<String> getAuthorizedPlayers() {
        return accessPermissions.keySet().stream().map(e -> String.format("%-50s", e) +
        (this.getAccessPermissions().get(e) == 1 ? "View Only" : "Full Access")).collect(Collectors.toList());
    }

    // Enum to represent different security levels
    // public enum SecurityLevel {
    //     PUBLIC, // Accessible to all players
    //     PRIVATE, // Accessible only to the owner 
    //     SELFDEFINED; // Accessible to authorized players

    //     public static Optional<SecurityLevel> fromString(String value) {
    //         try {
    //             return Optional.of(SecurityLevel.valueOf(value));
    //         } catch (IllegalArgumentException e) {
    //             // If the string does not match any enum constant, return empty Optional
    //             return Optional.empty();
    //         }
    //     }
    // }

    // Method to set the security level of the chest
    /**
     * Edit security level of the secure chest (public/private/self-defined)
     * @param level 
     * @param username 
     * @throws SQLException 
     */
    public void editSecurityLevel(String level) throws SQLException {
        securityLevel = level;
        if (level.equals("Private"))
            accessPermissions.clear();
        database_item7.updateChestSecurity(owner, this.securityLevel);
    }

    // Method to grant access to a player: owner wants to add / remove someone to access this chest
    /**
     * Edit access level of a user
     * @param username
     * @param type 0(NO_ACCESS),1(VIEW_ONLY),2(FULL_ACCESS)
     * @throws SQLException 
     */
    public void editAccess(String username, int type) throws SQLException {
        // set at controller if (securityLevel == "Public" || securityLevel == "Self-Defined") {
            accessPermissions.put(username, type);
            database_item7.updateChestPermission(owner, username, type);//type is the permission
            // return "Access modified";
        // }
        // else return "This is a private chest";
    }

    /**
     * Edit the Secure Chest's name
     * @param newName
     * @throws SQLException 
     */
    public void editChestName(String newName) throws SQLException{
        this.name = newName;
        database_item7.editChestName(owner, newName);
    }

     // Method to check if a player has access to the chest
    /**
     * Check the access level of the user
     * @param username
     * @return 0 if the security level is PRIVATE, entry.getValue() is the security level is PUBLIC/SELF_DEFINED
     */
     public int hasAccess(String username) {
        // if (securityLevel == "Public" || securityLevel == "Self-Defined") {
            for (Map.Entry<String, Integer> entry: accessPermissions.entrySet()){
                if (entry.getKey().equals(username))
                    return entry.getValue();
            }
            return 0;
        // } else {
        //     return 0; // If security level is PRIVATE, only the owner has access
        // }
    }
    
     /**
      * Remove authorised user from the permission list
      * @param username
      * @throws SQLException 
      */
    public void removeAuthorisedUser(String username) throws SQLException{
        database_item7.removeApprovedUser(owner,username);
        accessPermissions.remove(username);
        accessPermissions.entrySet().stream().forEach(e->System.out.println(e.getKey()+ " " + e.getValue()));
    }

    // Method to approve access request
    /**
     * Approve the user to access the chest
     * @param username 
     * @param type the user request's access level
     * @throws SQLException 
     */
    public void approveRequest(String username, int type) throws SQLException {
        // if (accessRequestsFromOther.keySet().contains(username)) {
            accessPermissions.put(username, type);
            database_item7.addApproved(owner, username, type);
            accessRequestsFromOther.remove(username);
            database_item7.updateRequest(owner, username, "Approved");
            // updateAccessLog(); // Update access log after approval
            // System.out.println("Access request approved for " + username);
        // } else {
        //     System.out.println("Access request not found for " + username);
        // }
    }

    // Method to reject access request
    /**
     * Reject access request
     * @param username
     * @throws SQLException 
     */
    public void rejectRequest(String username) throws SQLException {
        // if (accessRequestsFromOther.keySet().contains(username)) {
            accessRequestsFromOther.remove(username);
            database_item7.updateRequest(owner, username, "Rejected");
            // updateAccessLog(); // Update access log after rejection
        //     System.out.println("Access request rejected for " + username);
        // } else {
        //     System.out.println("Access request not found for " + username);
        // }
    }

    // Method to send an access request, with the permission type requested
    public void acceptRequest(String username, int type) throws SQLException {
        // if (!accessPermissions.containsKey(username)) { // Check if the player is not already authorized
            // if (securityLevel == SecurityLevel.PUBLIC || securityLevel == SecurityLevel.SELFDEFINED) {
                accessRequestsFromOther.put(username, type);
                database_item7.addRequest(owner, username, type);
            // }
        // }
    }

    // public void sendRequest(SecureChest otherChest, int type) throws SQLException{
    //     database_item7.addRequest(otherChest.getOwner(), owner, type);
    // }
    
    // public Set<EnderBackpackItem> view (String username) {
    //     if (hasAccess(username)>=1) {
    //         //show all content
    //         return itemChest.keySet();
    //     } else {
    //         return null;
    //         // System.out.println("You are not able to view this chest.");
    //         //this message print out as an alert if return = null
    //     }
    // }
    
    /**
     * Deposit items into the secure chest
     * @param username
     * @param item 
     * @param quantity quantity of the item
     * @throws SQLException 
     */
    public void deposit (String username, EnderBackpackItem item, int quantity) throws SQLException {
        // if (hasAccess(username)==2) {
            database_item7.deposit(owner, username, item, quantity);
            //username can be either the owner or other user, depending on which page the user are in 
            // (My Chest -> username = owner, other Chest -> username = otherUser)
            if (itemChest.keySet().contains(item)){
                quantity+=itemChest.get(item);
                itemChest.remove(item);
                itemChest.put(item,quantity);
            }
            else
                itemChest.put(item,quantity);
        //     return "Item successfully deposited";
        // } else {
        //     return "You are not able to deposit items to this chest.";
        // }
    }
    
    /**
     * Withdraw items from the secure chest
     * @param username
     * @param item
     * @param quantity quantity of the item
     * @throws SQLException 
     */
    public void withdraw (String username, EnderBackpackItem item, int quantity) throws SQLException {
        // if (hasAccess(username)==2) {
            database_item7.withdraw(owner, username, item, quantity);
            int oriquantity = itemChest.get(item);
            itemChest.remove(item);
            if (oriquantity-quantity !=0)
                itemChest.put(item, oriquantity-quantity);
        //     return "Item successfully withdrawed.";
        // } else {
        //     return "You are not able to withdraw items from this chest.";
        // }
    }

    public String toString(){
        return this.name + "   Owner: " + this.owner;
    }
}
