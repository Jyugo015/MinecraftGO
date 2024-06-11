package minecraft;

import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
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

public class withdrawChest implements Initializable{
    @FXML
    private Button withdrawButton;

    @FXML
    private ScrollPane scrollPaneItem;
    private int colCount = 0;

    private Map.Entry<EnderBackpackItem, Integer> selected;

    public ItemBox box;
    public static SecureChest chest;
    public static String username;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            box = new ItemBox(username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Set the horizontal scrollbar policy to NEVER to disable horizontal scrolling
        // scrollPaneItem.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        // Set preferred size for the content (optional)
        // gridItemBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
        // gridItemBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
        // gridChest.getChildren().clear();
        // gridChest.setHgap(10);
        // gridChest.setVgap(10);
        // gridChest.setPadding(new Insets(10,10,10,10));
        display();
    }

    public void display() {
        GridPane gridChest = new GridPane(0, 0);
        int rowCount=0;
        colCount =0;
        // System.out.println(box.list.size());
        for (Map.Entry<EnderBackpackItem, Integer> item: chest.getItemChest().entrySet()) {
            if (item.getValue()!=0){
                VBox itemVBox = new VBox(-10);
                ImageView imageView = new ImageView(new Image(getClass()
                                                .getResourceAsStream("/minecraft/icon/" + 
                                                item.getKey().getName() + ".PNG")));
                imageView.setFitWidth(45);
                imageView.setFitHeight(45);
                imageView.setPreserveRatio(true);
                Button button = new Button();
                button.setGraphic(imageView);
                button.setTooltip(new Tooltip(item.getKey().getName() + "\nType: " + 
                                            item.getKey().getType() + "\nFunction: " + 
                                            item.getKey().getFunction() + "\nQuantity: " + 
                                            item.getValue()));
                button.setMaxSize(47, 47);
                button.setMinSize(47,47);
                button.setPrefSize(47,47);

                Label quantitylabel = new Label(String.valueOf(item.getValue()));
                quantitylabel.getStyleClass().add("text-body");

                if (item.equals(selected)) {
                    itemVBox.setStyle("-fx-background-color: #6c2929;");
                    quantitylabel.setStyle("-fx-text-fill: white");
                } else {
                    quantitylabel.setStyle("-fx-text-fill: black");
                    itemVBox.setStyle("-fx-background-color: lightgrey;");
                }

                button.setStyle("-fx-background-color: transparent; ");
                
                itemVBox.getChildren().addAll(button, quantitylabel);
                button.setOnAction(e-> selectItem(item));

                itemVBox.getStyleClass().add("griditem");
                
                gridChest.add(itemVBox, colCount, rowCount);
                // gridItemBox.add(itemVBox, colCount, gridItemBox.getRowCount());
        
                // Increment column count
                colCount++;

                if(colCount==5){
                    rowCount++;
                    colCount=0;
                }

                scrollPaneItem.setContent(gridChest);

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
                if (selected==null) withdrawButton.setDisable(true);
                else withdrawButton.setDisable(false);
                withdrawButton.setOnAction(e->handleWithdrawItemAction(selected));
            }
        }
    }

    private void selectItem(Map.Entry<EnderBackpackItem, Integer> item) {
        selected = item;
        display();
    }

    private void handleWithdrawItemAction(Map.Entry<EnderBackpackItem, Integer> item){
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Withdraw Item");
        dialog.setHeaderText("How much do you want to withdraw this item?");
        String cssFilePath = getClass().getResource("minecraft-style.css").toExternalForm();
        dialog.getDialogPane().getStylesheets().add(cssFilePath);
        dialog.getDialogPane().getStyleClass().add("dialog-pane");

        // 设置按钮
        ButtonType okButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
        dialog.getDialogPane().lookupButton(okButtonType).getStyleClass().add("button");
        dialog.getDialogPane().lookupButton(ButtonType.CANCEL).getStyleClass().add("button");

        
        // 创建一个输入区域
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField addItemField = new TextField();
        addItemField.setPromptText("Quantity");
        addItemField.getStyleClass().add("textfield");
        Label warningLabel = new Label(); // Create a label for warning messages
        warningLabel.setTextFill(Color.RED); // Set the text color to red
        warningLabel.getStyleClass().add("text-custom");
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
                // If the new value is a number, clear the warning message
                warningLabel.setText("");
                dialog.getDialogPane().lookupButton(okButtonType).setDisable(true);
            }
            else if (Integer.parseInt(newValue)>item.getValue()){
                warningLabel.setText("Quantity of withdrawal of item > quantity of items in secure chest.");
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
        result.ifPresent(quantitytoremove -> {
            try {
                chest.withdraw(username, item.getKey(), quantitytoremove);
                box.addItem(item.getKey(), quantitytoremove);
                if (item.getKey().getType().equals("Potion")){
                    Potions potions = new Potions();
                    potions.getPotionsMap().entrySet().stream().filter(entry->entry.getKey()
                                         .equals(item.getKey().getName())).findFirst().ifPresent(entry->{
                        try {
                            database_item3.addPotion(username, item.getKey().getName(), 
                                                        entry.getValue().getPotency(), 
                                                        entry.getValue().getEffect());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });        
                }
                else if (item.getKey().getType().equals("Food")){
                    String[] crop = {"Carrot", "Wheat", "Potato", "Beetroot", "Melon Slice", "Pumpkin", "Sweet Berries"};
                    for (String cropname:crop){
                        if (cropname.equals(item.getKey().getName())){
                            Crop cropToAdd = new Crop(item.getKey().getName());
                            database_item6.addCrop(username, cropToAdd, quantitytoremove);
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
