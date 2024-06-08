/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package minecraft;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import minecraft.Potions.Potion;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class PotionSatchelController extends database_item3 implements Initializable {

    @FXML
    private GridPane potionGrid;
    @FXML
    private GridPane selectedPotionGrid;
    @FXML
    private Button addButton;
    @FXML
    private Button doneButton;
    @FXML
    private Button usePotionButton;
    @FXML
    private Button removeButton;
    // @FXML
    // private Button usePotionButton;
    @FXML
    private Button clearPotionButton;

    private String styleSelected;
    private int totalPotionsAdded = 0;
    private final int MAX_POTIONS = 9;
    private int potionsToAdd = 0;

    private TreeMap<String, Potion> potionMap;
    private List<Potion> selectedPotions;
    private List<Potion> tempSelectedPotions = new ArrayList<>();
    private Map<Button, Potion> buttonPotionMap = new HashMap<>();
    private Map<Button, Potion> selectedPotionMap = new HashMap<>();
    private Potion currentlySelectedPotion = null;
    
    private newPotionSatchel satchel = new newPotionSatchel();
    public ArrayList<String> potionBag;
    public static Thread backgroundThread;

    //each time removing or adding potion into satchel will change the list of potion
    //(remove potion from list of potion if adding potion to satchel)

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Potions potions = new Potions();//initializing the potions
        try {
            potionBag = database_item3.retrievePotion("defaultUser");
            selectedPotions = database_item3.retrievePotionSatchel("defaultUser");
            selectedPotions.forEach(e->satchel.addPotionToSatchel(e));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        potionMap = new Potions().getSortedPotionMap();
        // usePotionButton.setDisable(selectedPotions==null);
        addButton.setOnAction(e->handleAddButtonClick(e));
        removeButton.setOnAction(e->{
            try {
                handleRemoveButtonClick(e);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        usePotionButton.setDisable(selectedPotions==null||selectedPotions.isEmpty());
        populatePotionGrid();
        updateSelectedPotionsGrid();
        usePotionButton.setOnAction(e->{
            addButton.setDisable(true);
            doneButton.setDisable(true);
            removeButton.setDisable(true);
            clearPotionButton.setDisable(true);
            usePotionButton.setDisable(true);
            startBackgroundThread();
        });
    }

    private void startBackgroundThread(){
        int currentNum = satchel.getSize();
        backgroundThread = new Thread(() -> {
            try {
                try {
                    for (int i=0;i<currentNum;i++)
                        satchel.useFirstPotionAutomatically(this);
                    addButton.setDisable(false);
                    doneButton.setDisable(false);
                    removeButton.setDisable(false);
                    clearPotionButton.setDisable(false);
                    displayUsePotion();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    //printing list of potions can be chosen into potionsatchel
    public void populatePotionGrid() {
        potionGrid.setHgap(5);
        potionGrid.setVgap(5);
        // potionGrid.setPadding(new Insets(10, 10, 10, 10));
        int row = 0, col = 0;
        buttonPotionMap.clear();
        for (Map.Entry<String, Potion> entry : potionMap.entrySet()) {
            Potion potion = entry.getValue();
            String fileName = "/minecraft/icon/" + potion.getName() + ".PNG";
            // System.out.println(fileName);
            // System.out.println(potion.getName());
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(fileName)));
            imageView.setFitWidth(30);
            imageView.setFitHeight(30);
            imageView.setPreserveRatio(true);
            Button button = new Button();
            button.setGraphic(imageView);
            button.setTooltip(new Tooltip(potion.getName() + "\nPotency: " + potion.getPotency() + "\nEffect: " + potion.getEffect()));
            button.setMaxSize(35,35);
            button.setPrefSize(35,35);
            buttonPotionMap.put(button, potion);
            
            boolean found = false;
            for (String potionName: potionBag){
                if (potionName.equals(entry.getKey())){
                    found= true;
                }
            }
            if (!found){
                button.setDisable(true);
                button.getStyleClass().add("griditempressed");
            }
            else 
                button.getStyleClass().add("griditem");
            button.setOnAction(e -> handlePotionSelection(button));
            //select the potion and save as reference  
            potionGrid.add(button, col++, row);
            if (col > 8) {
                col = 0;
                row++;
            }
        }
    }

    @FXML
    private void handleAddButtonClick(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Add Potion");
        dialog.setHeaderText("Select number of potions to add");
        dialog.setContentText("Enter number of potions:");
        String cssFilePath = getClass().getResource("minecraft-style.css").toExternalForm();
        dialog.getDialogPane().getStylesheets().add(cssFilePath);
        dialog.getDialogPane().getStyleClass().add("dialog-pane");

        Text warningText = new Text();
        warningText.setFill(Color.RED);

        dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);

        dialog.getDialogPane().lookupButton(ButtonType.OK).getStyleClass().add("button");
        dialog.getDialogPane().lookupButton(ButtonType.CANCEL).getStyleClass().add("button");

        // Add a listener to the text input field to validate the input
        TextField inputField = dialog.getEditor();
        // Add the input field and warning text to the GridPane
        
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(inputField, 0, 0);
        gridPane.add(warningText, 0, 1);

        // Set the GridPane as the content of the dialog pane
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContent(gridPane);

        // textinputdialog.getEditor return textfield used within the dialog
        inputField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Parse the input value as an integer
            try {
                // Check if the value is negative
                if (newValue.matches("\\D*")) {
                    warningText.setText("Please enter a valid number");
                    dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
                }
                else if (newValue.trim().isEmpty()&&!oldValue.trim().isEmpty()){
                    // If the new value is a number, clear the warning message
                    warningText.setText("");
                    dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
                }
                else if (Integer.parseInt(newValue) >9){
                    warningText.setText("Number of potions cannot be mroe than 9");
                    dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
                }
                else if (Integer.parseInt(newValue) < 0) {
                    warningText.setText("Number of potions cannot be negative");
                    dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
                }
                else {
                    // Clear the warning message if the value is valid
                    warningText.setText("");
                    dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
                }
            } catch (NumberFormatException e) {
                // Clear the warning message if the input is not a valid integer
                warningText.setText("");
                dialog.getDialogPane();
            }
            
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(number -> {
            int numToAdd = Integer.parseInt(number);
            if (numToAdd > 0 && (totalPotionsAdded + numToAdd <= MAX_POTIONS)) {
                potionsToAdd = numToAdd;
                System.out.println(String.valueOf(potionsToAdd));
                tempSelectedPotions.clear();
            } else {
                showAlert("Limit Reached", "Adding this amount would exceed the maximum number of potions.");
            }
        });

    }

    private void handlePotionSelection(Button button) {
        //tempSelectedPotions.size() < potionsToAdd, means potion selected 
        //!tempSelectedPotions.contains(buttonPotionMap.get(button), means tempselectedPotions does not contain
        //the potion that maps to the button, this is to avoid adding duplicate potion(of the same kind)
        if (tempSelectedPotions.size() < potionsToAdd && !tempSelectedPotions.contains(buttonPotionMap.get(button))) {
            System.out.println("got selected");
            tempSelectedPotions.add(buttonPotionMap.get(button));
            button.setDisable(true);  // Disable the button immediately to prevent re-selection
            System.out.println(button.isDisable());
        }
        else{
            System.out.println("not selected");
            if (tempSelectedPotions.size() >= potionsToAdd) System.out.println("potionsToAdd/tempSelectedPotions wrong");
            if (tempSelectedPotions.contains(buttonPotionMap.get(button))) System.out.println("potionMap wrong");
        }
    }

    @FXML
    private void handleDoneButtonClick(ActionEvent event){
        if (tempSelectedPotions.size() != potionsToAdd) {
            showAlert("Selection Incomplete", "Please select the number of potions you specified.");
            return;
        }
        selectedPotions.addAll(tempSelectedPotions);
        tempSelectedPotions.forEach(e->
        {
            satchel.addPotionToSatchel(e);
            potionBag.remove(e.getName());
            try {
                database_item3.addPotionSatchel("defaultUser", 
                                                e.getName(),e.getPotency(), e.getEffect());
                database_itemBox.removeItem(e.getName(), "defaultUser", 1);
                System.out.println(e.getName() + " " +  String.valueOf(e.getPotency())+ " " + e.getEffect());
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            // if (backgroundThread == null || !backgroundThread.isAlive()) {
            //     startBackgroundThread();
            // }
        });
        // totalPotionsAdded += tempSelectedPotions.size();
        totalPotionsAdded =satchel.getSize();
        // usePotionButton.setDisable(selectedPotions==null);
        updateSelectedPotionsGrid();
        populatePotionGrid();
        tempSelectedPotions.clear();
        potionsToAdd = 0;  // Reset the count after adding potions
        displayUsePotion();
    }

    public void displayUsePotion(){
        usePotionButton.setDisable(selectedPotions==null||selectedPotions.isEmpty());
    }

    public newPotionSatchel getPotionSatchel(){
        return satchel;
    }

    public void setTotalPotionsAdded(int total){
        this.totalPotionsAdded = total;
    }

    public void updateSelectedPotionsGrid() {
        selectedPotionGrid.getChildren().clear();
        int col = 0;
        if (selectedPotions!=null){
            for (Potion potion : selectedPotions) {
                System.out.println(potion.getName());
                ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/minecraft/icon/" + potion.getName() + ".PNG")));
                imageView.setFitWidth(40);
                imageView.setFitHeight(40);
                imageView.setPreserveRatio(true);
                Button button = new Button();
                if (potion.equals(currentlySelectedPotion)) {
                    System.out.println("chosen" + potion.getName());
                    styleSelected = "griditempressed";
                    button.getStyleClass().add("griditempressed");
                } else {
                    styleSelected = "griditem";
                    button.getStyleClass().add("griditem");
                }
                button.setGraphic(imageView);
                button.setMaxSize(50, 50);
                button.setPrefSize(50, 50);
                button.setMinSize(50, 50);
                button.setTooltip(new Tooltip("Selected: " + potion.getName()));
                button.setOnAction(e -> selectPotionForRemoval(button, potion));
                selectedPotionGrid.add(button, col++, 0);
                selectedPotionMap.put(button, potion);
            }
        }
    }

    private void selectPotionForRemoval(Button button, Potion potion) {
        // if (currentlySelectedButton == null) {
        //     currentlySelectedButton.setStyle("");
        //     //deselect the currentselectedbutton if another button is selected
        // }
        System.out.println("got choose" + potion.getName());
        currentlySelectedPotion = potion;
        button.getStyleClass().remove(styleSelected);
        updateSelectedPotionsGrid();
    }

    @FXML
    private void handleRemoveButtonClick(ActionEvent event) throws SQLException {
        if (currentlySelectedPotion == null) {
            showAlert("No Selection", "Please select a potion to remove.");
            return;
        }
        Potion potion = currentlySelectedPotion;
        if (potion != null) {
            selectedPotions.remove(potion);
            satchel.removePotion(potion);
            potionBag.add(potion.getName());
            // selectedPotionMap.remove(currentlySelectedButton);
            database_item3.removePotionSatchel("defaultUser", potion.getName(), 
                                                potion.getPotency(), potion.getEffect());
            database_itemBox.addItem("defaultUser", potion.getName(),1);
            // totalPotionsAdded--;
            totalPotionsAdded = satchel.getSize();
            buttonPotionMap.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(potion))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .ifPresent(button -> button.setDisable(false));
            selectedPotions.remove(currentlySelectedPotion);
            currentlySelectedPotion = null;
            updateSelectedPotionsGrid();
            populatePotionGrid();
            displayUsePotion();
        }
    }

    public int getTotalPotionsAdded(){
        return totalPotionsAdded;
    }

    public List<Potion> getSelectedPotions(){
        return selectedPotions;
    }

    @FXML
    void handleClearPotionButton(ActionEvent event) throws SQLException {
        if (!selectedPotions.isEmpty()||selectedPotions!=null){
            selectedPotions.forEach(potion-> {
                try {
                    potionBag.add(potion.getName());
                    database_itemBox.addItem("defaultUser", 
                                            potion.getName(), 1);
                    database_item3.addPotion("defaultUser", potion.getName(), 
                                            potion.getPotency(), potion.getEffect());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            database_item3.clearPotionSatchel("defaultUser");
            selectedPotions.clear();
            satchel.clearSatchel();
            totalPotionsAdded =satchel.getSize();
            updateSelectedPotionsGrid();
            populatePotionGrid();
            displayUsePotion();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        String cssFilePath = getClass().getResource("minecraft-style.css").toExternalForm();
        alert.getDialogPane().getStylesheets().add(cssFilePath);
        alert.getDialogPane().getStyleClass().add("dialog-pane");
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // private void resetPotionButtons() {
    //     potionGrid.getChildren().forEach(node -> {
    //         if (node instanceof Button) {
    //             node.setDisable(false);
    //         }
    //     });
    // }
}
