package minecraft;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class AutomatedSortingChest extends Application {
    private static BSTs<EnderBackpackItem> bst = new BSTs<>();;
    private final static String imageFilePath = "/minecraft/icon/";
    
    private static boolean isSelected = false;
    private static SingleEnderBackpackItemPane selected;
    private static String categoryChosen = "(Include ALL)";
    private static List<EnderBackpackItem> allSortedItems;
    private static Image backgroundImage;
    private static Stage stage = new Stage();
     
    private static Text reminder = new Text();
    private static Button backToMainPageButton = new Button("Back to main page");
    private static Label totalNoOfEnderBackpackItemInChest = new Label();
    private static Label totalNoOfEnderBackpackItemOfCategory = new Label();
    private static TextField searchEnderBackpackItemTextField = new TextField(categoryChosen);
    private static TextField quantityToAddOrRemove = new TextField();
    private static BorderPane pane1 = new BorderPane();
    
    
    private static ArrayList<String> unsortedEnderBackpackItemNameArrayList = new ArrayList<>();
    private static ArrayList<Integer> unsortedEnderBackpackItemQuantityArrayList = new ArrayList<>();
    private static ItemBox unsortedBox;
    private static String username;
    
    @Override
    public void start(Stage primaryStage) throws SQLException, FileNotFoundException {
        username = "defaultUser";
        unsortedBox = new ItemBox(username);
        unsortedEnderBackpackItemNameArrayList.clear();
        unsortedEnderBackpackItemQuantityArrayList.clear();
        try {
            unsortedBox = new ItemBox(username);
            // loading unsorted item in itemBox
            for (EnderBackpackItem item: unsortedBox.list) {
                if (database_itemBox.retrieveQuantity(item.name , username) > 0) {
                    unsortedEnderBackpackItemNameArrayList.add(item.name);
                    unsortedEnderBackpackItemQuantityArrayList.add(database_itemBox.retrieveQuantity(item.name , username)); 
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AutomatedSortingChest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // sorted item in Automated sorting chest
        for (String itemName : database_item4.retrieveItem(username)) {
            EnderBackpackItem item = database_item4.getEnderBackpackItem(username, itemName);
            bst.add(itemName, item.quantity);
        }
        allSortedItems= bst.getParticularCategory(categoryChosen).retriveAllItems();
        
        backToMainPageButton.setOnAction(e->{
            try {
                MainPage mainPage = new MainPage();
                mainPage.start((Stage) ((Button) e.getSource()).getScene().getWindow());
//                stage.close();
            } catch (IOException ex) {
                Logger.getLogger(AutomatedSortingChest.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        
        stage.setOnCloseRequest((WindowEvent t) -> {
            try {
                MainPage mainpage = new MainPage();
                mainpage.start(new Stage());
            } catch (IOException ex) {
                System.out.println("Failed to go to the main page");
            }
        });

        totalNoOfEnderBackpackItemInChest = labelSetting(totalNoOfEnderBackpackItemInChest);
        totalNoOfEnderBackpackItemOfCategory = labelSetting(totalNoOfEnderBackpackItemOfCategory);
        
        pane1.setPadding(new Insets(10,10,10,10));
        backgroundImage = new Image(getClass().getResourceAsStream("/minecraft/icon/background.jpg"));
        stage = primaryStage;
        Image icon1 = new Image(getClass().getResourceAsStream("/minecraft/icon/AutomatedSortingChest.png"));
        stage.getIcons().clear();
        stage.getIcons().add(icon1);
        stage.setTitle("Automated Sorting Chest");
        stage.setScene(scene1());
        stage.show();
    }

    // Scene 1: main scene
    private Scene scene1() {
        pane1 = new BorderPane();
        updateAll();
        Label categoriesLabel = new Label("Categories: ");
        categoriesLabel.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        
        ChoiceBox<String> categoriesChoiceBox = new ChoiceBox<>();
        categoriesChoiceBox.setValue("(Include ALL)");
        categoriesChoiceBox.getItems().addAll("Armor", "Arrow", "Dye", "Food", "Material", "Mob Egg", "Potion", "Record", "Transportation", "Tool", "Weapon", "(Include ALL)");        
        
        Button addNewEnderBackpackItemButton = new Button("Add New Item");
        Button addcurrentEnderBackpackItemButton = new Button("Add");
        Button removeCurrentEnderBackpackItemButton = new Button("Remove");
        
        // Background
        BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
        Background background = new Background(new BackgroundImage(backgroundImage,BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            bSize));
        pane1.setBackground(background);
        
        // Top pane
        VBox topPane = new VBox(); 
        topPane.getChildren().addAll(totalNoOfEnderBackpackItemInChest, categoriesLabel,reminder,categoriesChoiceBox,searchEnderBackpackItemTextField,totalNoOfEnderBackpackItemOfCategory);
        
        // Bottom pane
        HBox bottomPane = new HBox();
        Label quantityText = new Label("Quantity: ");
        quantityText.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        bottomPane.getChildren().addAll(quantityText,quantityToAddOrRemove,addcurrentEnderBackpackItemButton,removeCurrentEnderBackpackItemButton,backToMainPageButton);
        
        // Right pane
        pane1.setRight(addNewEnderBackpackItemButton);
        
        // Main pane
        pane1.setCenter(new ScrollPane(new AutomatedSortingItemsGridPane(categoryChosen)));
        pane1.setTop(topPane);
        pane1.setBottom(bottomPane);

        // Set on action
        // Select a new category
        categoriesChoiceBox.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            categoryChosen = categoriesChoiceBox.getItems().get((Integer)newValue);
            searchEnderBackpackItemTextField.setText(categoryChosen);
            updateAll();
        });
        
        // search EnderBackpackItem to display the possible EnderBackpackItem
        searchEnderBackpackItemTextField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            pane1.setCenter(new ScrollPane(new AutomatedSortingItemsGridPane(newValue)));
        });
        
        addcurrentEnderBackpackItemButton.setOnAction(e -> {
            if (isSelected && quantityToAddOrRemove.getText() != null && ! quantityToAddOrRemove.getText().equals("") && Integer.parseInt(quantityToAddOrRemove.getText()) != 0) {
                try {
                    String itemName = selected.getEnderBackpackItemName();
                    int quantity = Integer.parseInt(quantityToAddOrRemove.getText());
                    int quantityInDatabaseItembox = database_itemBox.retrieveQuantity(itemName, username);
                    quantity = quantityInDatabaseItembox>=quantity ? quantity : quantityInDatabaseItembox;
                    bst.add(itemName, quantity);
                    database_item4.addItem(username, itemName, quantity);
                    reminder.setText("You have inserted " + (quantityInDatabaseItembox>=quantity ? quantity : quantityInDatabaseItembox) + " " + selected);
                } catch (SQLException ex) {
                    Logger.getLogger(AutomatedSortingChest.class.getName()).log(Level.SEVERE, null, ex);
                }
                unsortedBox.clearItem(); // clear database itembox
                updateAll();
            } else if (! isSelected) {
                reminder.setText("Please select a EnderBackpackItem to add.");
            } else {
                reminder.setText("Please enter the amount to add.");
            }
        });
        
        // Remove amount of current EnderBackpackItem
        removeCurrentEnderBackpackItemButton.setOnAction(e -> {
            if (isSelected && quantityToAddOrRemove.getText() != null && ! quantityToAddOrRemove.getText().equals("") && Integer.parseInt(quantityToAddOrRemove.getText()) != 0) {
                try {
                    String itemName = selected.getEnderBackpackItemName();
                    int quantity = Integer.parseInt(quantityToAddOrRemove.getText());
                    int quantityInDatabaseSortingChest = database_item4.retrieveQuantity(itemName, username);
                    quantity = quantityInDatabaseSortingChest>=quantity ? quantity : quantityInDatabaseSortingChest;
                    bst.remove(itemName, quantity);
                    database_item4.removeItem(itemName, username, quantity);
                    reminder.setText("You have removed " + quantity + " " + selected);
                } catch (SQLException ex) {
                    Logger.getLogger(AutomatedSortingChest.class.getName()).log(Level.SEVERE, null, ex);
                }
                updateAll();
            } else if (! isSelected) {
                reminder.setText("Please select a EnderBackpackItem to remove.");
            } else {
                reminder.setText("Please enter the amount to remove.");
            }
        });
        
        // enter the amount of EnderBackpackItem to add or remove
        quantityToAddOrRemove.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null && ! newValue.equals("")){
                    if (newValue.charAt(newValue.length()-1) <'0' || newValue.charAt(newValue.length()-1) >'9'){
                        quantityToAddOrRemove.setText(oldValue);
                    }
                } 
            } 
        });
        
        // swtich scene
        addNewEnderBackpackItemButton.setOnAction(e->{stage.setScene(scene2());});
        
        Scene scene = new Scene(pane1, 600,400);
        scene.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());

        return scene;
    }
    
    // Scene2: Add new EnderBackpackItem
    private Scene scene2 () {
        unsortedEnderBackpackItemNameArrayList.clear();
        unsortedEnderBackpackItemQuantityArrayList.clear();
        try {
            unsortedBox = new ItemBox(username);
            // loading unsorted item in itemBox
            for (EnderBackpackItem item: unsortedBox.list) {
                if (database_itemBox.retrieveQuantity(item.name , username) > 0) {
                    unsortedEnderBackpackItemNameArrayList.add(item.name);
                    unsortedEnderBackpackItemQuantityArrayList.add(database_itemBox.retrieveQuantity(item.name , username));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AutomatedSortingChest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        pane1 = new BorderPane();
        Text unsortedEnderBackpackItemText = new Text("Name of new EnderBackpackItem to add: ");
        ObservableList<String> EnderBackpackItemsObservableValue = FXCollections.observableArrayList();
        EnderBackpackItemsObservableValue.clear();
        for (int i = 0; i < unsortedEnderBackpackItemNameArrayList.size(); i++) {
            EnderBackpackItemsObservableValue.add(unsortedEnderBackpackItemNameArrayList.get(i) + " (" + unsortedEnderBackpackItemQuantityArrayList.get(i) + ")");
        }
        ListView<String> EnderBackpackItemsListView = new ListView<>();
        
        EnderBackpackItemsListView.getItems().addAll(EnderBackpackItemsObservableValue);
        EnderBackpackItemsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        Button addButton = new Button("Add");
        Button addAllButton = new Button("Add ALL");
        Button backButton = new Button("Back");
        
        // Background
        BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
        Background background = new Background(new BackgroundImage(backgroundImage,BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            bSize));
        pane1.setBackground(background);
        
        // Bottom pane
        HBox bottomPane = new HBox(addButton, addAllButton, backButton,backToMainPageButton);
        bottomPane.setPadding(new Insets(20,20,20,20));
        
        // Center pane
        ScrollPane centerPane = new ScrollPane();
        centerPane.setContent(EnderBackpackItemsListView);
        centerPane.setStyle("-fx-background-color: transparent");
        
        // Main pane
        pane1.setLeft(new HBox(unsortedEnderBackpackItemText, centerPane));
        pane1.setBottom(bottomPane);

        // Set on action
        // Add EnderBackpackItem(s)
        addButton.disableProperty().bind(EnderBackpackItemsListView.getSelectionModel().selectedItemProperty().isNull());
        addButton.setOnAction(e -> {
            ArrayList<String> EnderBackpackItemsToBeRemoved = new ArrayList<>(EnderBackpackItemsListView.getSelectionModel().getSelectedItems());
            for (int i = 0; i < EnderBackpackItemsToBeRemoved.size();) {
                String string = EnderBackpackItemsToBeRemoved.get(0);
                String EnderBackpackItemName = string.split("[(]")[0].trim();
                try {
                    int quantity = database_itemBox.retrieveQuantity(EnderBackpackItemName, username);
                    bst.add(EnderBackpackItemName, quantity);
                    database_item4.addItem(username, EnderBackpackItemName, quantity);
                } catch (SQLException ex) {
                    Logger.getLogger(AutomatedSortingChest.class.getName()).log(Level.SEVERE, null, ex);
                }
                EnderBackpackItemsListView.getItems().remove(string);
                EnderBackpackItemsToBeRemoved.remove(string);
                int index = getIndexInUnsorted(EnderBackpackItemName);
                unsortedEnderBackpackItemNameArrayList.remove(index);
                unsortedEnderBackpackItemQuantityArrayList.remove(index);
            }
            centerPane.setContent(EnderBackpackItemsListView);
            pane1.setCenter(centerPane);
        });
        
        // Add all EnderBackpackItems
        addAllButton.setOnAction(e -> {
            for (int i = 0; i < unsortedEnderBackpackItemNameArrayList.size(); i++) {
                String itemName = unsortedEnderBackpackItemNameArrayList.get(i);
                try {
                    int quantity = database_itemBox.retrieveQuantity(itemName, username);
                    bst.add(itemName, quantity);
                    database_item4.addItem(username, itemName, quantity);
                } catch (SQLException ex) {
                    Logger.getLogger(AutomatedSortingChest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            unsortedBox.clearItem(); // clear database itembox
            unsortedEnderBackpackItemNameArrayList.clear();
            unsortedEnderBackpackItemQuantityArrayList.clear();
            EnderBackpackItemsListView.getItems().clear();
            centerPane.setContent(EnderBackpackItemsListView);
            pane1.setCenter(centerPane);
        });
        
        // Back to previous scene
        backButton.setOnAction(e->{stage.setScene(scene1());});
        
        Scene scene = new Scene(pane1, 600,400);
        scene.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
        return scene;
    }
    
    public int getIndexInUnsorted(String EnderBackpackItemName) {
        for (int i = 0; i < unsortedEnderBackpackItemNameArrayList.size(); i++) {
            if (unsortedEnderBackpackItemNameArrayList.get(i).equals(EnderBackpackItemName)) {
                return i;
            }
        }
        return -1;
    }
    
    public void updateAll() {
        reminder.setText("");
        totalNoOfEnderBackpackItemInChest.setText("Total number of Item in the automated sorting chest is: " + bst.getTotalQuantity());
        totalNoOfEnderBackpackItemOfCategory.setText("Total number of Item for the category is: " + bst.getQuantityOfCateogory(categoryChosen));
        pane1.setCenter(new ScrollPane(new AutomatedSortingItemsGridPane(categoryChosen)));
        quantityToAddOrRemove.clear();
        if (selected != null) {
            selected.setStyle("-fx-border-color: rgba(0,0,0,0)"); 
            isSelected = false;
            selected = null; 
        }
        
    }
    
    private class SingleEnderBackpackItemPane extends BorderPane{
        private Image EnderBackpackItemImage = backgroundImage;
        private Text quantityOfEnderBackpackItem = new Text("0");
        private String category;
        private String EnderBackpackItemName;
        
//        private Text EnderBackpackItemName = new Text();

        public SingleEnderBackpackItemPane(String EnderBackpackItem, int quantity, String category) {
            this.category = category;
            this.EnderBackpackItemName = EnderBackpackItem;
            EnderBackpackItemImage = new Image(getClass().getResourceAsStream(imageFilePath+ EnderBackpackItem +".png"));
            
            quantityOfEnderBackpackItem.setFill(Color.BLACK);
            quantityOfEnderBackpackItem.setFont(Font.font("Verdana", 10));
            quantityOfEnderBackpackItem.setText(quantity + "");
            
            ImageView imageEnderBackpackItem = new ImageView(EnderBackpackItemImage);
            imageEnderBackpackItem.setFitWidth(40);
            imageEnderBackpackItem.setFitHeight(40);
            this.setCenter(imageEnderBackpackItem);
            
            Tooltip tooltip = new Tooltip(EnderBackpackItem);
            Tooltip.install(imageEnderBackpackItem, tooltip);
            
            HBox HBbottom = new HBox();
//            HBbottom.getChildren().add(EnderBackpackItemName);
            HBbottom.getChildren().add(quantityOfEnderBackpackItem);
            HBbottom.setAlignment(Pos.BOTTOM_RIGHT);
            this.setBottom(HBbottom);
            this.setPrefWidth(40);
            this.setPrefHeight(50);
            setOnMouseClicked(e->{
                if (isSelected) {
                    selected.setStyle("-fx-border-color: rgba(0,0,0,0)"); 
                    if (! selected.equals(this)) {
                        this.setStyle("-fx-border-color: red");
                        selected = this;
                    }
                } else {
                    this.setStyle("-fx-border-color: red");
                    selected = this;
                    isSelected = true;
                    }
                }
            );
        }

        public String getCategory() {
            return category;
        }

        public String getEnderBackpackItemName() {
            return EnderBackpackItemName;
        }
    }
    
    public Label labelSetting(Label label) {
        label.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        return label;
    }

    private class AutomatedSortingItemsGridPane extends GridPane{
        
        public AutomatedSortingItemsGridPane(String input) {
            allSortedItems = input.equals(categoryChosen) ? bst.getParticularCategory(input).retriveAllItems() : bst.getParticularCategory(categoryChosen).retrivePossibleEnderBackpackItemsAfterwards(input);
            this.getChildren().clear();
            int noOfColumn = 0;
            if (allSortedItems != null) {
                int j = 0;
                for (int i = 0; i < allSortedItems.size(); i++, noOfColumn++) {
                    String EnderBackpackItemName  = allSortedItems.get(i).getName();
                    SingleEnderBackpackItemPane pane = new SingleEnderBackpackItemPane(EnderBackpackItemName, bst.getParticularCategory(categoryChosen).getQuantity(EnderBackpackItemName), allSortedItems.get(i).getType());
                    add(pane, noOfColumn,j);
                    if ((i+1)%10 == 0) {
                        j++;
                        noOfColumn = -1;
                    }
                }
            } 
            setHgap(2);
            setVgap(2);
            setPadding(new Insets(5,5,5,5));
            setPrefHeight(560);
            setPrefHeight(470);
        }
    }
}