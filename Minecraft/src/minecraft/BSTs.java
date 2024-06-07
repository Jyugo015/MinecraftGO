package minecraft;

import java.sql.SQLException;

public class BSTs<E extends EnderBackpackItem>{
    String username = "defaultUser";
    private BinarySearchTree<EnderBackpackItem> ALLTree = new BinarySearchTree<>(username);
    private BinarySearchTree<EnderBackpackItem> toolsTree = new BinarySearchTree<>(username);
    private BinarySearchTree<EnderBackpackItem> foodTree = new BinarySearchTree<>(username);
    private BinarySearchTree<EnderBackpackItem> arrrorsTree = new BinarySearchTree<>(username);
    private BinarySearchTree<EnderBackpackItem> mobEggsTree = new BinarySearchTree<>(username);
    private BinarySearchTree<EnderBackpackItem> weaponsTree = new BinarySearchTree<>(username);
    private BinarySearchTree<EnderBackpackItem> materialTree = new BinarySearchTree<>(username);
    private BinarySearchTree<EnderBackpackItem> potionsTree = new BinarySearchTree<>(username);
    private BinarySearchTree<EnderBackpackItem> recordsTree = new BinarySearchTree<>(username);
    private BinarySearchTree<EnderBackpackItem> armorTree = new BinarySearchTree<>(username);
    private BinarySearchTree<EnderBackpackItem> transportationsTree = new BinarySearchTree<>(username);
    private BinarySearchTree<EnderBackpackItem> dyesTree = new BinarySearchTree<>(username);
//    private static BinarySearchTree[] trees = {toolsTree,foodTree,armorTree,arrrorsTree,decorationsTree,mobEggsTree,weaponsTree,materialTree,potionsTree,recordsTree,transportationsTree,dyesTree,ALLTree};
    
    public void add(String EnderBackpackItemName, int quantity) throws SQLException {
        EnderBackpackItem item = database_itemBox.getEnderBackpackItem(username, EnderBackpackItemName);
        if (item.type == null) { // the item is not inside the itemBox
            System.out.println("Type is null");
            item = database_item4.getEnderBackpackItem(username, EnderBackpackItemName);
        }
        ALLTree.add(EnderBackpackItemName,quantity);
        switch (item.type) {
            case "Tool" :toolsTree.add(EnderBackpackItemName, quantity); break;
            case "Food" :foodTree.add(EnderBackpackItemName, quantity); break;
            case "Arrow" :arrrorsTree.add(EnderBackpackItemName, quantity); break;
            case "Mob Egg" :mobEggsTree.add(EnderBackpackItemName, quantity); break;
            case "Weapon" :weaponsTree.add(EnderBackpackItemName, quantity); break;
            case "Armor" :armorTree.add(EnderBackpackItemName, quantity); break;
            case "Material" :materialTree.add(EnderBackpackItemName, quantity); break;
            case "Transportation" :transportationsTree.add(EnderBackpackItemName, quantity); break;
            case "Potion" :potionsTree.add(EnderBackpackItemName, quantity); break;
            case "Record" :recordsTree.add(EnderBackpackItemName, quantity); break;
            case "Dye" :dyesTree.add(EnderBackpackItemName, quantity); break;
            default:
                System.out.println("Type not found");
        }
    }
    
    public void remove(String EnderBackpackItemName, int quantity) throws SQLException {
        EnderBackpackItem EnderBackpackItem = database_item4.getEnderBackpackItem(username, EnderBackpackItemName);
        ALLTree.remove(EnderBackpackItemName, quantity);
        switch (EnderBackpackItem.type) {
            case "Tool" :toolsTree.remove(EnderBackpackItemName, quantity);  break;
            case "Food" :foodTree.remove(EnderBackpackItemName, quantity); break;
            case "Arrow" :arrrorsTree.remove(EnderBackpackItemName, quantity); break;
            case "Mob Egg" :mobEggsTree.remove(EnderBackpackItemName, quantity); break;
            case "Weapon" :weaponsTree.remove(EnderBackpackItemName, quantity); break;
            case "Armor" :armorTree.remove(EnderBackpackItemName, quantity); break;
            case "Material" :materialTree.remove(EnderBackpackItemName, quantity); break;
            case "Transportation" :transportationsTree.remove(EnderBackpackItemName, quantity); break;
            case "Potion" :potionsTree.remove(EnderBackpackItemName, quantity); break;
            case "Record" :recordsTree.remove(EnderBackpackItemName, quantity); break;
            case "Dye" :dyesTree.remove(EnderBackpackItemName, quantity); break;
            default:
                throw new AssertionError();
        }
    }
    
    public void removeAll(String EnderBackpackItemName) throws SQLException {
        EnderBackpackItem EnderBackpackItem = database_item4.getEnderBackpackItem(username, EnderBackpackItemName);
        ALLTree.removeAll(EnderBackpackItemName);
        switch (EnderBackpackItem.getType()) {
            case "Tool" :toolsTree.removeAll(EnderBackpackItemName);  break;
            case "Food" :foodTree.removeAll(EnderBackpackItemName); break;
            case "Arrow" :arrrorsTree.removeAll(EnderBackpackItemName); break;
            case "Mob Egg" :mobEggsTree.removeAll(EnderBackpackItemName); break;
            case "Weapon" :weaponsTree.removeAll(EnderBackpackItemName); break;
            case "Armor" :armorTree.removeAll(EnderBackpackItemName); break;
            case "Material" :materialTree.removeAll(EnderBackpackItemName); break;
            case "Transportation" :transportationsTree.removeAll(EnderBackpackItemName); break;
            case "Potion" :potionsTree.removeAll(EnderBackpackItemName); break;
            case "Record" :recordsTree.removeAll(EnderBackpackItemName); break;
            case "Dye" :dyesTree.removeAll(EnderBackpackItemName); break;
            default:
                throw new AssertionError();
        }
    }
    
    public int getTotalQuantity(){
        return ALLTree.getQuantity();
    }
    
    public int getQuantityOfCateogory(String category){
        switch (category) {
            case "Tool" : return toolsTree.getQuantity();
            case "Food" : return foodTree.getQuantity();
            case "Arrow" : return arrrorsTree.getQuantity();
            case "Mob Egg" : return mobEggsTree.getQuantity();
            case "Weapon" : return weaponsTree.getQuantity();
            case "Armor" : return armorTree.getQuantity();
            case "Material" : return materialTree.getQuantity();
            case "Transportation" : return transportationsTree.getQuantity();
            case "Potion" : return potionsTree.getQuantity();
            case "Record" : return recordsTree.getQuantity();
            case "Dye" : return dyesTree.getQuantity();
            case "(Include ALL)" : return ALLTree.getQuantity();
            default:
                throw new AssertionError();
        }
    }
    
    public BinarySearchTree<EnderBackpackItem> getParticularCategory(String category) {
        switch (category) {
            case "Tool" : return toolsTree;
            case "Food" : return foodTree;
            case "Arrow" : return arrrorsTree;
            case "Mob Egg" : return mobEggsTree;
            case "Weapon" : return weaponsTree;
            case "Armor" : return armorTree;
            case "Material" : return materialTree;
            case "Transportation" : return transportationsTree;
            case "Potion" : return potionsTree;
            case "Record" : return recordsTree;
            case "Dye" : return dyesTree;
            case "(Include ALL)" : return ALLTree;
            default:
                throw new AssertionError();
        }
    }

}
