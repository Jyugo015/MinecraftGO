package minecraft;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class EnderBackpackImplementation{
    // public final int initialCapacity = 50;
    public int capacity, currentStock;
    public ArrayList<EnderBackpackItem> list = new ArrayList<EnderBackpackItem>();
    public String username;
       
    // database_item1.setCapacity(initialCapacity,"defaultUser");
    //to be put in login page when new user registered
        
    public EnderBackpackImplementation(String username) throws SQLException{
        this.capacity = database_item1.getCapacity("defaultUser");
        this.currentStock = 0;
        ArrayList<String> itemNamelist = database_item1.retrieveItem("defaultUser");
        for (int i=0;i<itemNamelist.size();i++){
            this.addItem(new EnderBackpackItem(itemNamelist.get(i),"backpack"),0);
        }
        // this.getItemsName().forEach(e->System.out.println(e));//testing 
        // System.out.println(this.getCurrentStock());//testing 
        this.username = username;
    }
    
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
    
    public void removeItem(EnderBackpackItem item, int quantity){
        if (this.list.contains(item)){
            item.quantity-=quantity;
            this.currentStock-=quantity;
            if(item.quantity==0) list.remove(item);
        }
        // else 
        //     System.out.println("The item is not in the backpack.");
    }

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

    public void addCapacity(int amount) throws SQLException{//amount = 5/10/20/30
        this.capacity += amount;
        database_item1.setCapacity(this.capacity,"defaultUser");
    }

    public void reduceCapacity(int amount) throws SQLException{
        this.capacity -= amount;
        database_item1.setCapacity(this.capacity, "defaultUser");
    }

    public int getCurrentStock(){
        return this.currentStock;
    }

    public int getNumOfItem(){
        return this.list.size();
    }

    public ArrayList<String> getItemsName(){
        ArrayList<String> itemNameList = new ArrayList<String>(this.
                                                                list.stream().map(n -> n.getName())
                                                                .collect(Collectors.toList()));
        return itemNameList;
    }

    public int getCapacity(){
        return this.capacity;
    }
}
