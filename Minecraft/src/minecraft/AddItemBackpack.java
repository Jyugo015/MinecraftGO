package minecraft;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class AddItemBackpack extends EnderBackpackController implements Initializable{
    @FXML
    private Button addConfirm;

    @FXML
    private GridPane gridItemBox = new GridPane();

    @FXML
    private ScrollPane scrollPaneItem;
    
    private int colCount = 0;

    private EnderBackpackItem selected;

    public static EnderBackpackImplementation backpack;
    
    public static ItemBox box;
            
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        scrollPaneItem.setContent(gridItemBox);
        // Set the horizontal scrollbar policy to NEVER to disable horizontal scrolling
        // scrollPaneItem.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        // Set preferred size for the content (optional)
        // gridItemBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
        // gridItemBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
        gridItemBox.getChildren().clear();
        gridItemBox.setHgap(0);
        gridItemBox.setVgap(0);
        gridItemBox.setPadding(new Insets(0,0,0,0));
        display();
    }

    public void display() {
        gridItemBox.getChildren().clear();
        int rowCount=0;
        colCount =0;
        for (int i=0;i<box.list.size();i++) {
            if (box.list.get(i).quantity!=0){          
                VBox itemVBox = new VBox(-7);
                ImageView imageView = new ImageView(new Image(getClass()
                                                .getResourceAsStream("/minecraft/icon/" + 
                                                 box.list.get(i).getName() + ".PNG")));
                imageView.setFitWidth(60);
                imageView.setFitHeight(60);
                imageView.setPreserveRatio(true);
                Button button = new Button();
                button.setGraphic(imageView);
                button.setTooltip(new Tooltip(box.list.get(i).getName() + "\nType: " + 
                                            box.list.get(i).getType() + "\nFunction: " + 
                                            box.list.get(i).getFunction() + "\nQuantity: " + 
                                            box.list.get(i).getQuantity()));
                button.setMaxSize(60,60);
                button.setMinSize(60,60);
                button.setPrefSize(60,60);
                button.setStyle("-fx-background-color: transparent;");

                Label quantitylabel = new Label(String.valueOf(box.list.get(i).getQuantity()));
                // quantitylabel.getStyleClass().add("text-subtitle");
                itemVBox.getChildren().addAll(button, quantitylabel);

                
                if (box.list.get(i).equals(selected)) {
                    itemVBox.getStyleClass().removeAll();
                    itemVBox.getStyleClass().add("griditempressed");
                    quantitylabel.setStyle("-fx-text-fill: white");
                    quantitylabel.getStyleClass().add("text-body");
                } else {
                    itemVBox.getStyleClass().removeAll("griditempressed");
                    itemVBox.getStyleClass().add("griditem");
                    quantitylabel.getStyleClass().add("label-in-griditem");
                }
                EnderBackpackItem item = box.list.get(i);
                button.setOnAction(e-> selectItem(item));

                gridItemBox.add(itemVBox, colCount, rowCount);
                // gridItemBox.add(itemVBox, colCount, gridItemBox.getRowCount());
        
                // Increment column count
                colCount++;

                if(colCount==6){
                    rowCount++;
                    colCount=0;
                }

                // // If we've reached the second column, move to the next row
                // if (colCount == 2) {
                //     colCount = 0;
                //     // Add a new row constraint
                //     RowConstraints rowConstraints = new RowConstraints();
                //     rowConstraints.setVgrow(Priority.ALWAYS);
                //     gridItemBox.getRowConstraints().add(rowConstraints);
                // }
                
                // // Adjust column constraints
                // ColumnConstraints column1 = new ColumnConstraints();
                // column1.setPercentWidth(50);
                // ColumnConstraints column2 = new ColumnConstraints();
                // column2.setPercentWidth(50);
                // gridItemBox.getColumnConstraints().setAll(column1, column2);
                addConfirm.setText("Add");
                if (selected==null) addConfirm.setDisable(true);
                else addConfirm.setDisable(false);
                addConfirm.setOnAction(e->handleAddItemAction(selected));
            }
        }
    }

    private void selectItem(EnderBackpackItem item) {
        selected = item;
        display();
    }

    private void handleAddItemAction(EnderBackpackItem item){
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Add Item");
        dialog.setHeaderText("How much do you want to add for this item?");
        String cssFilePath = getClass().getResource("minecraft-style.css").toExternalForm();
        dialog.getDialogPane().getStylesheets().add(cssFilePath);
        dialog.getDialogPane().getStyleClass().add("dialog-pane");

        ButtonType okButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
        dialog.getDialogPane().lookupButton(okButtonType).setDisable(true);

        dialog.getDialogPane().lookupButton(okButtonType).getStyleClass().add("button");
        dialog.getDialogPane().lookupButton(ButtonType.CANCEL).getStyleClass().add("button");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField addItemField = new TextField();
        addItemField.setPromptText("Quantity");

        Label warningLabel = new Label(); // Create a label for warning messages
        warningLabel.setTextFill(Color.RED); // Set the text color to red

        grid.add(new Label("Quantity:"), 0, 0);
        grid.add(addItemField, 1, 0);
        grid.add(warningLabel, 0, 1, 2, 1); // Add warning label below the capacity field, spanning 2 columns

        dialog.getDialogPane().setContent(grid);

        // Request focus on the field by default
        Platform.runLater(addItemField::requestFocus);

        // Add a listener to the text property of the TextField
        addItemField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                // If the new value is not a number, show the warning message
                warningLabel.setText("Warning: Please enter a valid number!");
                dialog.getDialogPane().lookupButton(okButtonType).setDisable(true);
            }
            else if (newValue.trim().isEmpty()&&!oldValue.trim().isEmpty()){
                warningLabel.setText("");
                dialog.getDialogPane().lookupButton(okButtonType).setDisable(true);
            }
            else if (backpack.capacity<backpack.currentStock+Integer.parseInt(newValue)){
                warningLabel.setText("Quantity of addition of item > capacity");
                dialog.getDialogPane().lookupButton(okButtonType).setDisable(true);
            }
            else if (Integer.parseInt(newValue)>item.quantity){
                warningLabel.setText("Quantity of addition of item > quantity of items in box.");
                dialog.getDialogPane().lookupButton(okButtonType).setDisable(true);
            } else {
                // If the new value is a number, clear the warning message
                warningLabel.setText("");
                dialog.getDialogPane().lookupButton(okButtonType).setDisable(false);
            }
        });

        // Convert the result when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                try {
                    int itemAddition = Integer.parseInt(addItemField.getText());
                    return itemAddition;
                }
                catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });

        Optional<Integer> result = dialog.showAndWait();
        result.ifPresent(quantitytoadd -> {
            try {
                backpack.addItem(item.getName(), quantitytoadd);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            box.removeItem(item, quantitytoadd);
            display();
            try {
                database_itemBox.removeItem(item.getName(), username, quantitytoadd);
                database_item1.addItem(username, item.getName(), quantitytoadd);
                if (item.getType().equals("Potion"))
                    database_item3.removePotion(username, item.getName());
                    else if (item.getType().equals("Food")){
                        String[] crop = {"Carrot", "Wheat", "Potato", "Beetroot", "Melon Slice", "Pumpkin", "Sweet Berries"};
                        for (String cropname:crop){
                            if (cropname.equals(item.getName())){
                                Crop cropToAdd = new Crop(item.getName());
                                database_item6.removeCrop(username, cropToAdd.getName(), quantitytoadd);
                            }
                        }
                    }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (item.equals(selected)) {
                selected = null;
            }
            
            display();
        });
    }
}