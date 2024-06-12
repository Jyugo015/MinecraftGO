package minecraft;

import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import minecraft.AutofarmerblockController.cropToAdd;

public class AutofarmerblockController implements Initializable {

    @FXML
    private Label cartLabel;
    @FXML
    private Label addCropQuantityLabel;
    @FXML
    private Button addCropButton;
    @FXML
    private TableView<cropToAdd> cartTable;
    @FXML
    private CheckBox checkBoxFertiliser;
    @FXML
    private MenuButton chooseFertiliser;
    @FXML
    private Button confirmAddButton;
    @FXML
    private AnchorPane anchorUrFarm;
    @FXML
    private GridPane cropGrid;
    @FXML
    private Label durationRemaining;
    @FXML
    private MenuItem fertiliser1;
    @FXML
    private MenuItem fertiliser2;
    @FXML
    private Label growthStage;
    @FXML
    private ImageView imageGrowthStage;
    @FXML
    private ListView<Task> listPendingTask;
    @FXML
    private Label ongoingTask;
    @FXML
    private TextField quantityField;
    @FXML
    private ScrollPane scrollPaneSelectCrop;
    @FXML
    private Label seedName;
    @FXML
    private Label taskName;
    @FXML
    private Label taskStatus;
    @FXML
    private Label alertlabel;
    @FXML
    private Label warningLabel;
    private AutoFarmerBlock farm;
    private Crop selectedCrop;
    private Task selectedTask;
    private int quantityAdd = 0;
    @FXML
    private TableColumn<cropToAdd, String> cropNameColumn;
    @FXML
    private TableColumn<cropToAdd, Integer> quantityColumn;
    private ObservableList<cropToAdd> cropsToAdd = FXCollections.observableArrayList();
    public Queue<Task> taskManager;
    private String fertiliser;
    private Task ongoingtask;
    Thread backgroundThread;
    private Map<Float, String> taskFertilised;
    private String fertiliserSet;
    public static String username;

    public static class cropToAdd {

        private final SimpleStringProperty name;
        private final SimpleIntegerProperty quantityCrop;

        public cropToAdd(String name, int quantityCrop) {
            this.name = new SimpleStringProperty(name);
            this.quantityCrop = new SimpleIntegerProperty(quantityCrop);
        }

        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public int getQuantityCrop() {
            return quantityCrop.get();
        }

        public SimpleIntegerProperty quantityCropProperty() {
            return quantityCrop;
        }
    }

    public void initialize(URL url, ResourceBundle rb) {
        // AnchorPane.setTopAnchor(taskName, 0.0);
        // AnchorPane.setBottomAnchor(taskName, 0.0);
        // AnchorPane.setLeftAnchor(taskName, 0.0);
        // AnchorPane.setRightAnchor(taskName, 0.0);

        // // Use bindings to center the label
        // taskName.layoutXProperty().bind(Bindings.createDoubleBinding(
        //             () -> (anchorUrFarm.getWidth() - taskName.getWidth()) / 2,
        //             anchorUrFarm.widthProperty(), taskName.widthProperty()));
        // taskName.layoutYProperty().bind(Bindings.createDoubleBinding(
        //             () -> (anchorUrFarm.getHeight() - taskName.getHeight()) / 2,
        //             anchorUrFarm.heightProperty(), taskName.heightProperty()));
        try {
            farm = new AutoFarmerBlock(username);
            System.out.println("run");
            for (Crop crop : farm.cropInBox) {
                System.out.println(crop.getName() + " " + crop.getQuantitySeed());
            }
            taskManager = farm.getTaskManager(username);
            taskManager.forEach(e-> System.out.println(e.getTask() + " " + e.getTaskID()));
            taskFertilised = database_item6.retrieveTaskFertilisedID(username);
            taskFertilised.entrySet().forEach(e-> System.out.println(e.getValue() + " " + e.getKey()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        checkBoxFertiliser.setDisable(true);
        chooseFertiliser.setDisable(true);
        addCropButton.setDisable(true);
        selectedCrop = null;
        initializeCart();
        updateDisplay();
        try {
            displayPendingTask();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // startAutoScheduledTask();
        startBackgroundThread();
    }

    public void startBackgroundThread() {
        backgroundThread = new Thread(() -> {
            try {
                farm.runTask(this);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        backgroundThread.setDaemon(true);//low priority thread running behind the main program
        backgroundThread.start();
    }

    // public void startAutoScheduledTask(){
    //     AutofarmBackgroundTask task = new AutofarmBackgroundTask(this.farm, this);
    //     Thread thread = new Thread(task);
    //     thread.setDaemon(true);//thread stop when exit from the program
    //     thread.start();
    // }
    public void updateDisplay() {
        // checkBoxFertiliser.setDisable(true);
        // chooseFertiliser.setDisable(true);
        displayCrop();
        displayCart();
        taskManager = farm.getTaskManager(username);
        try {
            displayOngoingTask();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // while(true) updateDisplay();
    }

    public void displayCrop() {
        cropGrid.getChildren().clear();
        int col = 0;
        int row = 0;
        cropGrid.setHgap(5);
        cropGrid.setVgap(-45);
        cropGrid.setPadding(new Insets(0, 0, 0, 0));
        if (selectedCrop == null) {
            addCropButton.setDisable(true);
            addCropQuantityLabel.setVisible(false);
            quantityField.setVisible(false);
            warningLabel.setVisible(false);
            // cartLabel.setVisible(false);
            // confirmAddButton.setVisible(false);
            // cartTable.setVisible(false);
        }
        if (cropsToAdd.isEmpty()) {
            confirmAddButton.setVisible(false);
            alertlabel.setVisible(false);
        }

        for (Crop crop : farm.cropInBox) {
            InputStream imageStream = getClass().getResourceAsStream("/minecraft/icon/" + crop.getName() + ".png");
            VBox box = new VBox(-10);
            // if (imageStream == null) {
            //     System.out.println("Image resource not found for: " + crop.getName());
            // } else {
            //     System.out.println("Image resource found for: " + crop.getName());
            // }
            ImageView imageView = new ImageView(new Image(imageStream));
            // System.out.println(crop.getName());
            imageView.setFitWidth(40);
            imageView.setFitHeight(40);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);
            Button button = new Button();
            button.setGraphic(imageView);
            button.setMaxSize(50, 50);
            button.setMinSize(50, 50);
            button.setPrefSize(50, 50);
            // button.getStyleClass().add("griditem");
            Label quantitylabel = new Label(" "+String.valueOf(crop.getQuantitySeed()));
            if (crop.equals(selectedCrop)) {
                box.getStyleClass().clear();
                box.getStyleClass().add("griditempressed");
                quantitylabel.getStyleClass().clear();
                quantitylabel.setStyle("-fx-text-fill: white");
                quantitylabel.getStyleClass().add("text-body");
            } else {
                box.getStyleClass().clear();
                box.getStyleClass().add("griditem");
                quantitylabel.getStyleClass().clear();
                quantitylabel.getStyleClass().add("label-in-griditem");
            }
            Tooltip tooltip = new Tooltip("Name: " + crop.getName()
                                        + "\nQuantity of Crops: " + crop.getQuantityCrop()
                                        + "\nQuantity of Seeds: " + crop.getQuantitySeed()
                                        + "\nNumber of Growth Stages: " + crop.getNumGrowthStages()
                                        + "\nMinimum Number of Seeds Yield: " + crop.getMinSeedYield()
                                        + "\nMaximum Number of Seeds Yield: " + crop.getMaxSeedYield()
                                        + "\nMinimum Number of Crops Yield: " + crop.getMinCropYield()
                                        + "\nMaximum Number of Crops Yield: " + crop.getMaxCropYield());
            tooltip.getStyleClass().add("text-body");
            button.setOnMouseEntered(event -> {
                if (!tooltip.isShowing()) {
                    tooltip.show(button, event.getScreenX(), event.getScreenY() + 10);
                }
            });
            button.setOnMouseExited(event -> {
                if (tooltip.isShowing()) {
                    tooltip.hide();
                }
            });
            //tooltip not working cuz the background thread is continuously executing the updatedisplay method
            //that the button is recreated and lose its properties
            //when u place cursor on the button, u can see the button is blinking, means that it is updated 
            // by the background thread running the autofarming scheudled task
            
            button.setTooltip(tooltip);
            button.setStyle("-fx-background-color: transparent");
            box.getChildren().addAll(button, quantitylabel);
            cropGrid.add(box, col++, row);
            if (crop.getQuantitySeed() == 0) {
                button.setDisable(true);
            }

            button.setOnAction(e -> selectCrop(crop));

            quantityField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.trim().isEmpty()&&!oldValue.trim().isEmpty()) {
                    // If the input is empty, clear the warning message
                    warningLabel.setText("");
                    addCropButton.setDisable(true);
                } else if (!newValue.matches("\\d*")) {
                    // If the new value is not a number, show the warning message
                    warningLabel.setText("Warning: Please enter a valid number!");
                    addCropButton.setDisable(true);
                } else if (Integer.parseInt(newValue) > selectedCrop.getQuantitySeed()) {
                    warningLabel.setText("Warning: Quantity > Number of Seeds \nAcquired");
                    // System.out.println(selectedCrop.getName() + selectedCrop.getQuantitySeed());
                    addCropButton.setDisable(true);
                } else {
                    // If the new value is a number, clear the warning message
                    warningLabel.setText("");
                    addCropButton.setDisable(false);
                    quantityAdd = Integer.parseInt(newValue);
                    addCropButton.setOnAction(e -> {
                        try {
                            cartLabel.setVisible(true);
                            confirmAddButton.setVisible(true);
                            alertlabel.setVisible(true);
                            cartTable.setVisible(true);
                            String message = farm.addCropToPlant(selectedCrop, quantityAdd);
                            if (message.equals("Crop successfully add to cart")) {
                                cropsToAdd.add(new cropToAdd(selectedCrop.getName(), quantityAdd));
                            }
                            selectedCrop = null;
                            showAlert(message);
                            quantityField.clear();
                            updateDisplay();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    });
                }
            });
        };
        confirmAddButton.setOnAction(e -> {
            try {
                handleConfirmAddCrop(e);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
    }


    public void initializeCart() {
        cropNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantityCrop"));
        // cartTable.getColumns().add(cropNameColumn);
        // cartTable.getColumns().add(quantityColumn);
        cartTable.setItems(cropsToAdd);
    }

    public void displayCart() {
        cartTable.refresh();
    }

    public void selectCrop(Crop crop) {
        selectedCrop = crop;
        updateDisplay();
        addCropButton.setDisable(false);
        addCropQuantityLabel.setVisible(true);
        quantityField.setVisible(true);
        warningLabel.setVisible(true);
        warningLabel.setText("");
        warningLabel.setTextFill(Color.RED);
    }

    @FXML
    public void handleConfirmAddCrop(ActionEvent event) throws SQLException {
        farm.addTask();
        cropsToAdd.clear();
        displayCart();
        updateDisplay();
        listPendingTask.refresh();
        displayPendingTask();
        if (backgroundThread == null || !backgroundThread.isAlive()) {
            startBackgroundThread();
        }
    }

    public void displayOngoingTask() throws SQLException {
        ongoingtask = taskManager.peek();
        if (ongoingtask != null) {
            ongoingTask.setText("\u25CF " + ongoingtask.getTask() + " "
                    + ongoingtask.getCropUsed().getName() + " Growth Stage: "
                    + String.valueOf(ongoingtask.getGrowthStage()));
            imageGrowthStage.setImage((new Image(getClass()
                    .getResourceAsStream("/minecraft/icon/"
                            + ongoingtask.getCropUsed().getName()
                            + String.valueOf(ongoingtask.getGrowthStage()) + ".png"))));
            //minecraft -> ongoingTask.getCropUsed().getName() + ongoingtask.getGrowthStage()
            durationRemaining.setText(String.valueOf(ongoingtask.getDuration() / 1000) + " s");
            seedName.setText("");
            taskName.setText(ongoingtask.getTask() + " " + ongoingtask.getCropUsed().getName());
            taskStatus.setText(String.valueOf(ongoingtask.getStatus()) + " %");
            growthStage.setText(String.valueOf(ongoingtask.getGrowthStage()));
        } else {
            ongoingTask.setText("");
            durationRemaining.setText("");
            seedName.setText("");
            taskName.setText("");
            taskStatus.setText("");
            growthStage.setText("");
        }
    }

    public void displayPendingTask() throws SQLException {
        ObservableList<Task> observableTools = FXCollections.observableArrayList(taskManager);
        listPendingTask.setItems(observableTools);
        System.out.println("update pending task");
    
        // Add a listener to handle task selection changes
        listPendingTask.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedTask = newValue;
            updateUIForSelectedTask();
        });
    
        // Set initial state for fertiliser options
        setupFertiliserOptions();
    }
    
    // Setup listeners for fertiliser options 
    private void setupFertiliserOptions() {
        checkBoxFertiliser.setDisable(true);
        chooseFertiliser.setDisable(true);
        checkBoxFertiliser.setSelected(false);
        chooseFertiliser.setText("Choose Fertiliser");
    
        checkBoxFertiliser.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                chooseFertiliser.setDisable(false);
                fertiliser1.setOnAction(e -> handleFertiliserSelection("Bone Meal"));
                fertiliser2.setOnAction(e -> handleFertiliserSelection("Super Fertiliser"));
            } else {
                chooseFertiliser.setDisable(true);
                chooseFertiliser.setText("Choose Fertiliser");
            }
        });
    }
    
    // Update the UI based on the selected task
    private void updateUIForSelectedTask() {
        if (selectedTask != null) {
            fertiliserSet = null;
            taskFertilised.entrySet().stream()
                .filter(entry -> selectedTask.getTaskID() == entry.getKey())
                .findFirst().ifPresent(e -> fertiliserSet = e.getValue());
    
            if (fertiliserSet != null) {
                System.out.println("yes");
                System.out.println(selectedTask.taskID);
                checkBoxFertiliser.setDisable(true);
                checkBoxFertiliser.setSelected(true);
                chooseFertiliser.setDisable(true);
                chooseFertiliser.setText(fertiliserSet);
            } else if (selectedTask.getTask().equals("Watering")) {
                checkBoxFertiliser.setDisable(false);
                checkBoxFertiliser.setSelected(false);
                chooseFertiliser.setDisable(true);
                chooseFertiliser.setText("Choose Fertiliser");
                System.out.println("runselectwatering");
            } else {
                resetFertiliserOptions();
            }
        } else {
            resetFertiliserOptions();
        }
    }
    
    // Handle the selection of fertiliser and apply it to the task
    private void handleFertiliserSelection(String fertiliser) {
        chooseFertiliser.setText(fertiliser);
        try {
            farm.applyFertiliser(selectedTask, fertiliser, this);
            taskFertilised.put(selectedTask.getTaskID(), fertiliser);
            displayPendingTask(); // Refresh the task list
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Reset fertiliser-related UI elements
    private void resetFertiliserOptions() {
        checkBoxFertiliser.setDisable(true);
        checkBoxFertiliser.setSelected(false);
        chooseFertiliser.setDisable(true);
        chooseFertiliser.setText("Choose Fertiliser");
    }

        // Set actions for fertiliser options if checkbox is selected and the task is not already fertilised
        // checkBoxFertiliser.selectedProperty().addListener((observable, oldValue, newValue) -> {
        //     if (newValue && !taskFertilised.containsKey(selectedTask.getTaskID())) {
        //         chooseFertiliser.setDisable(false);

        //         // Set actions for fertiliser options
        //         fertiliser1.setOnAction(e -> {
        //             chooseFertiliser.setText("Bone Meal");
        //             fertiliser = "Bone Meal";
        //             try {
        //                 farm.applyFertiliser(selectedTask, fertiliser);
        //                 taskFertilised.put(selectedTask.getTaskID(), fertiliser);
        //             } catch (SQLException e1) {
        //                 e1.printStackTrace();
        //             }
        //         });

        //         fertiliser2.setOnAction(e -> {
        //             chooseFertiliser.setText("Super Fertiliser");
        //             fertiliser = "Super Fertiliser";
        //             try {
        //                 farm.applyFertiliser(selectedTask, fertiliser);
        //                 taskFertilised.put(selectedTask.getTaskID(), fertiliser);
        //             } catch (SQLException e1) {
        //                 e1.printStackTrace();
        //             }
        //         });
        //     } else {
        //         chooseFertiliser.setDisable(true);
        //     }
        // });

    public void viewCrop() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Crop Information");
        alert.setHeaderText(null);
        String cssFilePath = getClass().getResource("minecraft-style.css").toExternalForm();
        alert.getDialogPane().getStylesheets().add(cssFilePath);
        alert.getDialogPane().getStyleClass().add("dialog-pane");

        // Create a TextArea for displaying the message
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(false);
        textArea.setStyle("-fx-font-family: 'monospaced'");  // Set monospaced font

        // Build the message string
        StringBuilder message = new StringBuilder();
        for (Crop crop : farm.cropInBox) {
            message.append(String.format("%-25s %d%n", crop.getName(), crop.getQuantityCrop()));
        }
        textArea.setText(message.toString());

        // Set the content of the alert to the TextArea
        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }

    public void showAlert(String message){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        String cssFilePath = getClass().getResource("minecraft-style.css").toExternalForm();
        alert.getDialogPane().getStylesheets().add(cssFilePath);
        alert.getDialogPane().getStyleClass().add("dialog-pane");
        alert.show();
    }
}
