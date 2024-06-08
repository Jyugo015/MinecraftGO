package minecraft;

public class EnderBackpackItem implements Comparable<EnderBackpackItem>{
    public int quantity;
    public String name, type, function;

    public EnderBackpackItem(String name, String table) {
        this.name = name;
        if (table.equalsIgnoreCase("backpack"))
            this.setSpecificationBackpack(this.name);
        else if (table.equalsIgnoreCase("itemBox"))
            this.setSpecificationItemBox(this.name);
    }

    public EnderBackpackItem(String itemName, String type, String function, int quantity){
        this.name = itemName;
        this.type = type;
        this.function = function;
        this.quantity = quantity;
    }

    public EnderBackpackItem(String itemName, String type, String function){
        this.name = itemName;
        this.type = type;
        this.function = function;
        this.quantity = 0;
    }

    public void setSpecificationBackpack(String itemname){
        try{
            this.setFunction(database_item1.retrieveFunction(itemname));
            this.setQuantity(database_item1.retrieveQuantity(itemname, "defaultUser"));
            this.setType(database_item1.retrieveType(itemname));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setSpecificationItemBox(String itemname){
        try{
            this.setFunction(database_itemBox.retrieveFunction(itemname));
            this.setQuantity(database_itemBox.retrieveQuantity(itemname, "defaultUser"));
            this.setType(database_itemBox.retrieveType(itemname));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + " " + type + "Grade:" + "2";
    }

    @Override
    public int compareTo(EnderBackpackItem o) {
        return name.compareToIgnoreCase(o.name);
    }
}
