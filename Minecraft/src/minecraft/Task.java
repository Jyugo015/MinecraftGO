package minecraft;

public class Task {
    String task;
    Crop cropUsed;
    int duration, growthStage;
    float status, taskID;
    
    public Task(float taskID, String task, Crop cropUsed, int duration, int growthStage, float status) {
        this.taskID = taskID;
        this.task = task;
        this.cropUsed = cropUsed;
        this.duration = duration;
        this.growthStage = growthStage;
        this.status = status;
    }


    public Task(String task, Crop cropUsed, int growthStage){
        this.task = task;
        this.cropUsed = cropUsed;
        this.growthStage = growthStage;
        this.status = 0;
        if (this.task.equals("Planting")||this.task.equals("Watering")
            ||this.task.equals("Applying")||this.task.equals("Harvesting"))
            this.duration = 40000;
        else 
            this.duration = 100000;
    }
    
    
    public String getTask() {
        return task;
    }
    
    public void setTask(String task) {
        this.task = task;
    }
    
    public Crop getCropUsed() {
        return cropUsed;
    }
    
    public void setCropUsed(Crop cropUsed) {
        this.cropUsed = cropUsed;
    }
    
    public float getStatus() {
        return status;
    }
    
    public void setStatus(float status) {
        this.status = status;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public void setDuration(String fertiliser) {
        if (fertiliser.equals("Bone Meal"))
            this.duration = (int)(this.duration*0.8);//reduce by 20% for bone meal applied 
        else if (fertiliser.equals("Super Fertiliser"))
            this.duration = (int)(this.duration*0.6);//reduce by 40% for super fertiliser applied
    }

    public void setDuration(int duration){
        this.duration = duration;
    }
    
    public int getGrowthStage() {
        return growthStage;
    }
    
    public void setGrowthStage(int growthStage) {
        this.growthStage = growthStage;
    }
    
    public float getTaskID(){
        return taskID;
    }

    public void setTaskID(float taskID){
        this.taskID = taskID;
    }

    public String toString(){
        return "\u25CF " + String.format("%-30s", this.task + " " + this.getCropUsed().getName()) 
                + String.format("%20s", " ") + "Growth Stage: " 
                + String.valueOf(this.getGrowthStage());
    }
}
