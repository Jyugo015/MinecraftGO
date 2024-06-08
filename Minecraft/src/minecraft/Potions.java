/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minecraft;

/**
 *
 * @author elysi
 */

import java.util.TreeMap;
import java.util.Map;


public class Potions {
    // Define Potion class
    public static class Potion {
        String name;
        int potency;
        String effect;
        Potion nextPotion;

        // Constructor
        public Potion(String name, int potency, String effect) {
            this.name = name;
            this.potency = potency;
            this.effect = effect;
            this.nextPotion = null; // initialize nextPotion to null by default
        }

        public Potion(String name){
            this.name = name;
            Map<String, Potion> map = new Potions().getPotionsMap();
            for (Map.Entry<String, Potion> entry: map.entrySet()){
                if (entry.getKey().equals(name)){
                    this.potency = entry.getValue().getPotency();
                    this.effect = entry.getValue().getEffect();
                    this.nextPotion = null;
                }
            }
        }

        // Getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPotency() {
            return potency;
        }

        public void setPotency(int potency) {
            this.potency = potency;
        }

        public String getEffect() {
            return effect;
        }

        public void setEffect(String effect) {
            this.effect = effect;
        }

        // Override toString() method for better output representation
        @Override
        public String toString() {
            return "Potion{" +
                    "name='" + name + '\'' +
                    ", potency=" + potency +
                    ", effect='" + effect + '\'' +
                    '}';
        }
    }

    public Map<String, Potion> potionsMap;

    // Constructor
    public Potions() {
        potionsMap = new TreeMap<>();
        initializePotions();
    }

    // Method to initialize potions
    public void initializePotions() {
               
        // 15 types of potions
        // total - 45 potions
        
        potionsMap.put("Healing Potion I", new Potion("Healing Potion I", 20, "Instantly heal the user"));
        potionsMap.put("Healing Potion II", new Potion("Healing Potion II", 50, "Instantly heal the user"));
        potionsMap.put("Healing Potion III", new Potion("Healing Potion III", 100, "Instantly heal the user"));
              
        potionsMap.put("Swiftness Potion I", new Potion("Swiftness Potion I", 20, "Increase movement speed"));
        potionsMap.put("Swiftness Potion II", new Potion("Swiftness Potion II", 50, "Increase movement speed"));
        potionsMap.put("Swiftness Potion III", new Potion("Swiftness Potion III", 100, "Increase movement speed"));
        
        potionsMap.put("Strength Potion I", new Potion("Strength Potion I", 20, "Make the damage dealt by direct combat higher"));
        potionsMap.put("Strength Potion II", new Potion("Strength Potion II", 50, "Make the damage dealt by direct combat higher"));
        potionsMap.put("Strength Potion III", new Potion("Strength Potion III", 100, "Make the damage dealt by direct combat higher"));

        potionsMap.put("Leaping Potion I", new Potion("Leaping Potion I", 20, "Raise the jump height"));
        potionsMap.put("Leaping Potion II", new Potion("Leaping Potion II", 50, "Raise the jump height"));
        potionsMap.put("Leaping Potion III", new Potion("Leaping Potion III", 100, "Raise the jump height"));
    
        potionsMap.put("Fire Resistance Potion I", new Potion("Fire Resistance Potion I", 20, "Grant fire resistance"));
        potionsMap.put("Fire Resistance Potion II", new Potion("Fire Resistance Potion II", 50, "Grant fire resistance"));
        potionsMap.put("Fire Resistance Potion III", new Potion("Fire Resistance Potion III", 100, "Grant fire resistance"));
        
        potionsMap.put("Regeneration Potion I", new Potion("Regeneration Potion I", 20, "Regenerate health of the user over time"));
        potionsMap.put("Regeneration Potion II", new Potion("Regeneration Potion II", 50, "Regenerate health of the user over time"));
        potionsMap.put("Regeneration Potion III", new Potion("Regeneration Potion III", 100, "Regenerate health of the user over time"));
        
        potionsMap.put("Water Breathing Potion I", new Potion("Water Breathing Potion I", 20, "Grant ability to breathe underwater"));
        potionsMap.put("Water Breathing Potion II", new Potion("Water Breathing Potion II", 50, "Grant ability to breathe underwater"));
        potionsMap.put("Water Breathing Potion III", new Potion("Water Breathing Potion III", 100, "Grant ability to breathe underwater"));
        
        potionsMap.put("Slow Falling Potion I", new Potion("Slow Falling Potion I", 20, "Allow safer descents from high places"));
        potionsMap.put("Slow Falling Potion II", new Potion("Slow Falling Potion II", 50, "Allow safer descents from high places"));
        potionsMap.put("Slow Falling Potion III", new Potion("Slow Falling Potion III", 100, "Allow safer descents from high places"));
 
        potionsMap.put("Invisibility Potion I", new Potion("Invisibility Potion I", 20, "Invisible to enemies and other players"));
        potionsMap.put("Invisibility Potion II", new Potion("Invisibility Potion II", 50, "Invisible to enemies and other players"));
        potionsMap.put("Invisibility Potion III", new Potion("Invisibility Potion III", 100, "Invisible to enemies and other players"));
        
        potionsMap.put("Night Vision Potion I", new Potion("Night Vision Potion I", 20, "Ability to see in low-light conditions or darkness"));
        potionsMap.put("Night Vision Potion II", new Potion("Night Vision Potion II", 50, "Ability to see in low-light conditions or darkness"));
        potionsMap.put("Night Vision Potion III", new Potion("Night Vision Potion III", 100, "Ability to see in low-light conditions or darkness"));
        
        potionsMap.put("Potion of Slowness I", new Potion("Potion of Slowness I", 20, "Decrease movement speed"));
        potionsMap.put("Potion of Slowness II", new Potion("Potion of Slowness II", 50, "Decrease movement speed"));
        potionsMap.put("Potion of Slowness III", new Potion("Potion of Slowness III", 100, "Decrease movement speed"));
        
        potionsMap.put("Potion of Luck I", new Potion("Potion of Luck I", 20, "Increase luck for finding rare items"));
        potionsMap.put("Potion of Luck II", new Potion("Potion of Luck II", 50, "Increase luck for finding rare items"));
        potionsMap.put("Potion of Luck III", new Potion("Potion of Luck III", 100, "Increase luck for finding rare items"));
        
        potionsMap.put("Potion of Weakness I", new Potion("Potion of Weakness I", 20, "Decrease the damage dealt by direct combat"));
        potionsMap.put("Potion of Weakness II", new Potion("Potion of Weakness II", 50, "Decrease the damage dealt by direct combats"));
        potionsMap.put("Potion of Weakness III", new Potion("Potion of Weakness III", 100, "Decrease the damage dealt by direct combat"));
        
        potionsMap.put("Potion of Poison I", new Potion("Potion of Poison I", 20, "Reduce health but does not kill"));
        potionsMap.put("Potion of Poison II", new Potion("Potion of Poison II", 50, "Reduce health but does not kill"));
        potionsMap.put("Potion of Poison III", new Potion("Potion of Poison III", 100, "Reduce health but does not kill"));
        
        potionsMap.put("Potion of Harming I", new Potion("Potion of Harming I", 20, "Cause instant damage and can even kill the target"));
        potionsMap.put("Potion of Harming II", new Potion("Potion of Harming II", 50, "Cause instant damage and can even kill the target"));
        potionsMap.put("Potion of Harming III", new Potion("Potion of Harming III", 100, "Cause instant damage and can even kill the target"));
               
    }

    public TreeMap<String, Potion> getSortedPotionMap(){
        int index = 1;
       
        TreeMap<String, Potion> sortedPotionsMap = new TreeMap<>();

        for(Map.Entry<String, Potion> entry : potionsMap.entrySet()){
            String potionName = entry.getKey();
            Potion potion = entry.getValue();
            // System.out.println(index + ". " + potionName + " - " + potion);
            sortedPotionsMap.put(potionName, potion);
            index++;
        }
       
       return sortedPotionsMap;
    }

    // Method to get potions map
    public Map<String, Potion> getPotionsMap() {
        return potionsMap;
    }
}

