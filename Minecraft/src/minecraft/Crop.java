package minecraft;

import java.sql.SQLException;

public class Crop {
    private String name;
    private Tool toolNeeded;
    private int quantityCrop, quantitySeed, numGrowthStages, minSeedYield, maxSeedYield, 
                    minCropYield, maxCropYield;
    //the number of crop to plant is equal to the number of seed needed

    public Crop(String name, int quantityCrop, int quantitySeed, Tool toolNeeded, int numGrowthStages, int minSeedYield,
                int maxSeedYield, int minCropYield, int maxCropYield) {
        this.name = name;
        this.quantityCrop = quantityCrop;
        this.quantitySeed = quantitySeed;
        this.toolNeeded = toolNeeded;
        this.numGrowthStages = numGrowthStages;
        this.minSeedYield = minSeedYield;
        this.maxSeedYield = maxSeedYield;
        this.minCropYield = minCropYield;
        this.maxCropYield = maxCropYield;
    }

    public Crop(String name) throws SQLException{
        this.name = name;
        this.setSpecification(this.name);
    }

    public void setSpecification(String name) throws SQLException{
        this.quantityCrop = database_item6.retrieveQuantityCrop("defaultUser", name);
        this.quantitySeed = database_item6.retrieveQuantitySeed("defaultUser", name);
        this.toolNeeded = database_item6.retrievetoolNeeded("defaultUser", name);
        this.numGrowthStages = database_item6.retrievenumGrowthStages("defaultUser", name);
        this.minSeedYield = database_item6.retrieveminSeedYield("defaultUser", name);
        this.maxSeedYield = database_item6.retrievemaxSeedYield("defaultUser", name);
        this.minCropYield = database_item6.retrieveminCropYield("defaultUser", name);
        this.maxCropYield = database_item6.retrievemaxCropYield("defaultUser", name);
    }

    public String getName() {
        return name;
    }

    public Tool getToolNeeded() {
        return toolNeeded;
    }

    public int getNumGrowthStages() {
        return numGrowthStages;
    }

    public int getMinSeedYield() {
        return minSeedYield;
    }

    public int getMaxSeedYield() {
        return maxSeedYield;
    }

    public int getMinCropYield() {
        return minCropYield;
    }

    public int getMaxCropYield() {
        return maxCropYield;
    }

    public int getQuantityCrop() {
        return quantityCrop;
    }

    public void setQuantityCrop(int quantityCrop) {
        this.quantityCrop = quantityCrop;
    }

    public int getQuantitySeed() {
        return quantitySeed;
    }

    public void setQuantitySeed(int quantitySeed) {
        this.quantitySeed = quantitySeed;
    }

    public boolean checkSeed(int quantityToAdd){
        if (quantityToAdd> this.quantitySeed) return false;
        else return true;
    }
}
