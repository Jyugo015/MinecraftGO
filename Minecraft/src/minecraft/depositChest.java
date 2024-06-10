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

public class depositChest  implements Initializable{
    @FXML
    private Button deposit;
    @FXML
    private ScrollPane scrollPaneItem;
    private int colCount = 0;

    private EnderBackpackItem selected;

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
        display();
    }

    public void display() {
        GridPane gridItemBox = new GridPane(0, 0);
        int rowCount=0;
        colCount =0;
        // System.out.println(box.list.size());
        for (int i=0;i<box.list.size();i++) {
            if (box.list.get(i).quantity!=0){ 
                VBox itemVBox = new VBox(-10);
                ImageView imageView = new ImageView(new Image(getClass()
                                                .getResourceAsStream("/minecraft/icon/" + 
                                                box.list.get(i).getName() + ".PNG")));
                imageView.setFitWidth(45);
                imageView.setFitHeight(45);
                imageView.setPreserveRatio(true);
                Button button = new Button();
                button.setGraphic(imageView);
                button.setTooltip(new Tooltip(box.list.get(i).getName() + "\nType: " + 
                                            box.list.get(i).getType() + "\nFunction: " + 
                                            box.list.get(i).getFunction() + "\nQuantity: " + 
                                            box.list.get(i).getQuantity()));
                button.setMaxSize(47, 47);
                button.setMinSize(47,47);
                button.setPrefSize(47,47);

                Label quantitylabel = new Label(String.valueOf(box.list.get(i).getQuantity()));
                quantitylabel.getStyleClass().add("text-body");
                if (box.list.get(i).equals(selected)) {
                    itemVBox.setStyle("-fx-background-color: #6c2929;");
                    quantitylabel.setStyle("-fx-text-fill: white");
                } else {
                    itemVBox.setStyle("-fx-background-color: lightgrey;");
                    quantitylabel.setStyle("-fx-text-fill: black");
                }

                button.setStyle("-fx-background-color: transparent;");
                
                itemVBox.getChildren().addAll(button, quantitylabel);
                EnderBackpackItem item = box.list.get(i);
                button.setOnAction(e-> selectItem(item));

                itemVBox.getStyleClass().add("griditem");
                // Handle hover state
                
                gridItemBox.add(itemVBox, colCount, rowCount);
                // gridItemBox.add(itemVBox, colCount, gridItemBox.getRowCount());
        
                // Increment column count
                colCount++;

                if(colCount==5){
                    rowCount++;
                    colCount=0;
                }
                scrollPaneItem.setContent(gridItemBox);
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
                if (selected==null) deposit.setDisable(true);
                else deposit.setDisable(false);
                deposit.setOnAction(e->{
                    handleDepositItemAction(selected);
                });
            }
        }
    }

    private void selectItem(EnderBackpackItem item) {
        selected = item;
        display();
    }

    private void handleDepositItemAction(EnderBackpackItem item){
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Deposit Item");
        dialog.setHeaderText("How much do you want to deposit for this item?");
        String cssFilePath = getClass().getResource("minecraft-style.css").toExternalForm();
        dialog.getDialogPane().getStylesheets().add(cssFilePath);
        dialog.getDialogPane().getStyleClass().add("dialog-pane");
        
        // 设置按钮
        ButtonType okButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // Apply styles to the actual buttons
        dialog.getDialogPane().lookupButton(okButtonType).getStyleClass().add("button");
        dialog.getDialogPane().lookupButton(ButtonType.CANCEL).getStyleClass().add("button");
        
        // 创建一个输入区域
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
            }else if (newValue.trim().isEmpty()&&!oldValue.trim().isEmpty()){
                // If the new value is a number, clear the warning message
                warningLabel.setText("");
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
                chest.deposit(username, item, quantitytoadd);
                box.removeItem(item, quantitytoadd);
                if (item.getType().equals("Potion"))
                    database_item3.removePotion(username, item.getName());
                else if (item.getType().equals("Food")){
                    String[] crop = {"Carrot", "Wheat", "Potato", "Beetroot", "Melon Slice", "Pumpkin", "Sweet Berries"};
                    for (String cropname:crop){
                        if (cropname.equals(item.getName())){
                            Crop cropToAdd = new Crop(item.getName());
                            database_item6.addCrop(username, cropToAdd, quantitytoadd);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            selected = null;
            display();
        });
    }
}
