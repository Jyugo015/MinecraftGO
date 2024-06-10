package minecraft;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemBox{
    public ArrayList<EnderBackpackItem> list = new ArrayList<EnderBackpackItem>();

    public ItemBox(String username) throws SQLException{
        ArrayList<String> itemNamelist = new ArrayList<String>();
        itemNamelist = database_itemBox.retrieveItem(username);
        for (int i=0;i<itemNamelist.size();i++){
            this.addItem(new EnderBackpackItem(itemNamelist.get(i), "itemBox"),0);
        }
    }
    
    public void addItem(EnderBackpackItem item, int quantity){
        if (this.list.contains(item)){
            item.quantity+=quantity;
        }
        else
            this.list.add(item);
    }

    public void addItem (String itemName, int quantity) throws SQLException{
        for (int i=0;i<this.list.size();i++){
            if (list.get(i).getName().equals(itemName)){
                addItem(list.get(i), quantity);
                return;
            }
        }
        this.addItem(new EnderBackpackItem(itemName, database_itemBox.retrieveType(itemName),
                     database_itemBox.retrieveFunction(itemName), quantity), 0);
    }

    // public void addItem(String itemName, String type, String function, int quantity){
    //     this.addItem(new EnderBackpackItem(itemName, type, function), quantity);
    // }
    
    public void removeItem(EnderBackpackItem item, int quantity){
        if (this.list.contains(item)){
            item.quantity-=quantity;
            if(item.quantity==0) list.remove(item);
        }
    }

    public void removeItem(String itemName, int quantity){
        for (int i=0;i<this.list.size();i++){
            if (list.get(i).getName().equals(itemName))
                removeItem(list.get(i), quantity);
        }
    }

    public void clearItem(){
        this.list.clear();
    }

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

    public ArrayList<String> getItemsName(){
        ArrayList<String> itemNameList = new ArrayList<String>(this.
                                                                list.stream().map(n -> n.getName())
                                                                .collect(Collectors.toList()));
        return itemNameList;
    }
}
