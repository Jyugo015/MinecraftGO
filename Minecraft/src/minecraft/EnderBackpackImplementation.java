package minecraft;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class EnderBackpackImplementation extends EnderBackpackController{
    // public final int initialCapacity = 50;
    public int capacity, currentStock;
    public ArrayList<EnderBackpackItem> list = new ArrayList<EnderBackpackItem>();
        
    public EnderBackpackImplementation(String username) throws SQLException{
        this.capacity = database_item1.getCapacity(username);
        this.currentStock = 0;
        ArrayList<String> itemNamelist = database_item1.retrieveItem(username);
        for (int i=0;i<itemNamelist.size();i++){
            this.addItem(new EnderBackpackItem(itemNamelist.get(i),"backpack"),0);
        }
        // this.getItemsName().forEach(e->System.out.println(e));//testing 
        // System.out.println(this.getCurrentStock());//testing 
    }
    
    /**
     * Add item into the backpack using the item instance.
     * If the item already exists in the backpack, update the quantity of the item in the backpack.
     * Else, insert the item with the quantity to be added into the backpack.
     * @param item item instance to be added to the backpack
     * @param quantity the quantity to be added to the backpack
     */
    public void addItem(EnderBackpackItem item, int quantity){
        if ((this.currentStock + quantity) <= this.capacity){
            if (this.list.contains(item)){
                item.quantity+=quantity;
                this.currentStock+=item.quantity;
            }
            else{
                this.list.add(item);
                this.currentStock += item.quantity;
            }
        }
        else 
            System.out.println("The backpack is full.");
    }

    /**
     * Add item into the backpack using the name of item.
     * Call the addItem(EnderbackpackItem item, int quantity) method to perform the addition of item into the backpack.
     * @param itemName the name of the item
     * @param quantity the quantity of item to be added to the backpack
     * @throws SQLException 
     */
    public void addItem(String itemName, int quantity) throws SQLException{
        for (int i=0;i<this.list.size();i++){
            if (list.get(i).getName().equals(itemName)){
                addItem(list.get(i), quantity);
                return;
            }
        }
        this.addItem(new EnderBackpackItem(itemName, database_itemBox.retrieveType(itemName), 
                                            database_itemBox.retrieveFunction(itemName), 
                                            quantity),0);
    }
    
    /**
     * Remove item from the backpack using the item instance.
     * Update the quantity of the item in the backpack if the amount left after deducting the current quantity of the item in the backpack is more than 0.
     * Else remove the item from the list.
     * @param item the item instance to be removed from the backpack
     * @param quantity the quantity to be removed from the backpack
     */
    public void removeItem(EnderBackpackItem item, int quantity){
        if (this.list.contains(item)){
            item.quantity-=quantity;
            this.currentStock-=quantity;
            if(item.quantity==0) list.remove(item);
        }
        // else 
        //     System.out.println("The item is not in the backpack.");
    }

    /**
     * Remove item from the backpack using the name of the item.
     * Call the removeItem(ENDerbackpackItem item, int quantity) method to perform removal of item from the backpack.
     * @param itemName the name of the item to be removed from the backpack
     * @param quantity the quantity of the item to be removed from the backpack
     */
    public void removeItem(String itemName, int quantity){
        for (int i=0;i<this.list.size();i++){
            if (list.get(i).getName().equals(itemName))
                removeItem(list.get(i), quantity);
        }
    }

    // public void clearItem(){
    //     this.list.clear();
    //     this.currentStock = 0;
    // }

    /**
     * Obtain the details of the item(instance variables) such as the name, type, function and the quantity of the item in the backpack 
     * @param item the item instance that requires specification 
     * @return an ArrayList containing the name, type, function and quantity stored in String data type
     */
    public ArrayList<String> getItemSpecification(EnderBackpackItem item){
        if (this.list.contains(item)){
            ArrayList<String> specification = new ArrayList<String>();
            specification.add(item.name);
            specification.add(item.type);
            specification.add(item.function);
            specification.add(Integer.toString(item.quantity));
            return specification;
        }
        return null;
    }

    // public void setInitialCapacity() throws SQLException{
    //     this.capacity = this.initialCapacity;
    //     database_item1.setCapacity(this.initialCapacity, "defaultUser");
    // }

    /**
     * Add capacity of the backpack.
     * @param amount the amount to increase the capacity of backpack by.
     * @throws SQLException
     */
    public void addCapacity(int amount) throws SQLException{//amount = 5/10/20/30
        this.capacity += amount;
        database_item1.setCapacity(this.capacity,username);
    }

    /**
     * Reduce the capacity of the backpack.
     * @param amount the amount to decrease the capacity of backpack by.
     * @throws SQLException
     */
    public void reduceCapacity(int amount) throws SQLException{
        this.capacity -= amount;
        database_item1.setCapacity(this.capacity, username);
    }

    /**
     * Obtain the current amount of item in the backpack.
     * @return the current amount of item in the backpack
     */
    public int getCurrentStock(){
        return this.currentStock;
    }

    /**
     * Obtain the number of distinct item in the backpack
     * @return the number of distinct item in the backpack
     */
    public int getNumOfItem(){
        return this.list.size();
    }

    /**
     * Obtain the name of items in the backpack.
     * @return an ArrayList containing the list of each distinct item in the backpack
     */
    public ArrayList<String> getItemsName(){
        ArrayList<String> itemNameList = new ArrayList<String>(this.
                                                                list.stream().map(n -> n.getName())
                                                                .collect(Collectors.toList()));
        return itemNameList;
    }

    /**
     * Obtain the current capacity of the backpack
     * @return the ciurrent capacity of the backpack
     */
    public int getCapacity(){
        return this.capacity;
    }
}
