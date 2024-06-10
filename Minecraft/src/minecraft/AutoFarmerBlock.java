package minecraft;

import java.util.Queue;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
// import java.util.Optional;
import java.sql.SQLException;


public class AutoFarmerBlock extends AutofarmerblockController{
    private Queue<Task> taskManager;
    private Map<Crop, Integer> cropToPlant;
    ArrayList<Crop> cropInBox;

    public AutoFarmerBlock(String username) throws SQLException{
        cropInBox = database_item6.retrieveCrop(username);
        taskManager = database_item6.retrieveTaskManager(username);
        cropToPlant = new HashMap<Crop, Integer>();
    }
   
    public Queue<Task> getTaskManager(String username){
        return taskManager;
    }

    //when addCrop button is clicked, add task into the queue.
    //if resources needed is insufficient, print out suitable message
    /**
     * Add the selected crop and check the database whether the quantity of the seed and tool to plant is enough
     * @param crop the crop to add to the task schedule 
     * @param quantityToAdd the quantity to add to the task schedule 
     * @return suitable message to display
     * @throws SQLException 
     */
    public String addCropToPlant(Crop crop, int quantityToAdd) throws SQLException {
        if (crop.checkSeed(quantityToAdd)){
            if (crop.getToolNeeded().getName().equals("None")
                ||(!(crop.getToolNeeded().getName().equals("None"))
                &&database_item6.checkTool(username, crop, quantityToAdd))){
                cropToPlant.put(crop, quantityToAdd);
                database_item6.removeSeed(username, crop, quantityToAdd);
                crop.setQuantitySeed(crop.getQuantitySeed()-quantityToAdd);
                if (!crop.getToolNeeded().getName().equals("None")){
                    database_itemBox.removeItem("Axes", username, quantityToAdd);
                }
                return "Crop successfully add to cart";
            }
            else return "Tool is not enough to perform the operation.";
        }
        else return"Insufficient seeds to perform the operation";
    }

    /**
     * Add Planting, Watering, Harvesting tasks and Growth Stages into taskManager
     * @throws SQLException 
     */
    public void addTask() throws SQLException{
        for (Map.Entry<Crop, Integer> entryCrop : cropToPlant.entrySet()){
            Crop crop = entryCrop.getKey();
            for (int i =0;i<entryCrop.getValue();i++){
                taskManager.add(new Task("Planting", crop, 1));
                database_item6.addTask(username, "Planting", crop.getName(), 
                                        40000, 1, 0);
                ((LinkedList<Task>) taskManager).getLast()
                                                .setTaskID(database_item6.retrieveLastTaskID(username));
                for (int j=2;j<crop.getNumGrowthStages();j++){
                    taskManager.add(new Task("Watering", crop, j));
                    database_item6.addTask(username,"Watering", crop.getName(), 
                                        40000, j, 0);
                    ((LinkedList<Task>) taskManager).getLast()
                                                    .setTaskID(database_item6.retrieveLastTaskID(username));
                    taskManager.add(new Task("Growth", crop, j));
                    database_item6.addTask(username,"Growth", crop.getName(), 
                                        100000, j, 0);
                    ((LinkedList<Task>) taskManager).getLast()
                                                    .setTaskID(database_item6.retrieveLastTaskID(username));
                }
                taskManager.add(new Task("Harvesting", crop, crop.getNumGrowthStages()));
                database_item6.addTask(username,"Harvesting", crop.getName(), 
                                        40000, crop.getNumGrowthStages(), 0);
                ((LinkedList<Task>) taskManager).getLast()
                                                .setTaskID(database_item6.retrieveLastTaskID(username));
            }
        }
        cropToPlant.clear();
    }

    
    /**
     * Add fertiliser if user checks the box
     * @param task task selected, and the applying fertiliser task will be added to the next
     * @param fertiliser Type of fertilisers: Bone meal, Super fertiliser
     * @param controller the controller class instance that call the method 
     * @throws SQLException 
     */
    public void applyFertiliser(Task task, String fertiliser, AutofarmerblockController controller) throws SQLException{
        //fertiliser can only be applied when the task chosen to be execute before the applying fertiliser 
        //operation is "watering" 
        ListIterator<Task> iterator = ((LinkedList<Task>)taskManager).listIterator();
        while(iterator.hasNext()){
            Task currentTask = iterator.next();
            if (currentTask.equals(task)){
                iterator.add(new Task("Applying "+fertiliser, task.getCropUsed(), task.getGrowthStage()));
                Task apply = iterator.previous();
                Task growthAfter = iterator.next();
                growthAfter.setDuration(fertiliser);
                database_item6.updateTask(username, growthAfter.getTask(), 
                                            growthAfter.getCropUsed().getName(), growthAfter.getDuration(), 
                                            growthAfter.getStatus(), growthAfter.getTaskID());
                database_item6.addTaskAfter(username, "Applying " + fertiliser, 
                                            task.getCropUsed().getName(), 
                                            4000, task.getGrowthStage(), (float)0.0, 
                                            (float)(task.getTaskID()+0.5));
                System.out.println(apply.getTask());
                apply.setTaskID((float)(task.getTaskID()+0.5));
                System.out.println(apply.getTaskID());
                Platform.runLater(()->{
                    try {
                        controller.updateDisplay();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } 
        }
    }
    
    //run through all the task inside the queue
    //after the addCrop button is clicked and all tasks are added into the queue, starts running the task 
    //one by one
    //complete one task, dequeue -> poll()
    
    /**
     * Run all the tasks in the taskManager
     * @param controller the controller class instance that call the method 
     * @throws SQLException 
     */
    public void runTask(AutofarmerblockController controller) throws SQLException {
        while (!taskManager.isEmpty()){
            Task taskToRun = taskManager.peek();
            System.out.println("Running " + taskToRun.getTask());
            try{
                int timeInterval = 1000;
                int duration = taskToRun.getDuration();
                System.out.println(duration);
                float statusAdd = 100/(duration/1000);
                float status = statusAdd;
                for (int i = duration - timeInterval; i>=0 ; i -= timeInterval, status += statusAdd){
                    Thread.sleep(timeInterval);
                    database_item6.updateTask(username,taskToRun.getTask(),
                                                taskToRun.getCropUsed().getName(), 
                                                i, status, taskToRun.getTaskID());
                    taskToRun.setDuration(i);
                    taskToRun.setStatus(status);
                    System.out.println(status);
                    Platform.runLater(() -> {
                        try {
                            controller.updateDisplay();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
                database_item6.removeTask(username,taskToRun.getTask(),
                                            taskToRun.getCropUsed().getName());
                taskManager.poll();
                Platform.runLater(()->{
                    try {
                        controller.displayPendingTask();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                System.out.println("Done " + taskToRun.getTask());
            } catch (InterruptedException e) {
                e.printStackTrace();    
            }
            if (taskToRun.getTask().equals("Harvesting")){
                // Optional<Crop> crop = cropInBox.stream().filter(e -> e.getName()
                //                             .equals(taskToRun.getCropUsed())).findFirst();
                // if (crop.isPresent()){
                    // Crop cropUsed = crop.get();
                    String message="Harvested\n";
                    Crop cropUsed = taskToRun.getCropUsed();
                    if (cropUsed.getMaxSeedYield()!=0){
                        int numSeedYield = (int)(Math.random() * 
                                            (cropUsed.getMaxSeedYield() - cropUsed.getMinSeedYield() + 1)) 
                                            + cropUsed.getMinSeedYield();
                        System.out.println(numSeedYield + " seeds of " + cropUsed.getName() + " Harvested!");
                        message+= numSeedYield + " seeds of " + cropUsed.getName() + "\n";
                        database_item6.addSeed(username, cropUsed, numSeedYield);
                        cropUsed.setQuantitySeed(cropUsed.getQuantitySeed() + numSeedYield);
                    }
                    int numCropYield = (int)(Math.random() * 
                                    (cropUsed.getMaxCropYield() - cropUsed.getMinCropYield() + 1)) 
                                    + cropUsed.getMinCropYield();
                    System.out.println(numCropYield + " crops of" + cropUsed.getName() + " Harvested!");
                    message+= numCropYield + " crops of " + cropUsed.getName();
                    database_item6.addCrop(username, cropUsed, numCropYield);
                    cropUsed.setQuantityCrop(cropUsed.getQuantityCrop() + numCropYield);
                    final String finalMessage = message;
                    Platform.runLater(()->{
                        controller.showAlert(finalMessage);
                    });
                // }
            }
        }
    }
}