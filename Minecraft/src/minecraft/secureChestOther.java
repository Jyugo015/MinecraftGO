package minecraft;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class secureChestOther implements Initializable{
    @FXML
    private Label chestNameOther;
    @FXML
    private Button depositOther;
    @FXML
    private ImageView imageChestOther;
    @FXML
    private Label securityLevelOtherLabel;
    @FXML
    private Button viewChestOther;
    @FXML
    private Button withdrawOther;

    public static SecureChest chestToShow;

    public static String username;

    public void initialize(URL url, ResourceBundle rb) {
        display();
    }

    public void display(){
        chestNameOther.setText("Chest Name: "+chestToShow.getName());
        imageChestOther.setImage(new Image(getClass()
                        .getResourceAsStream("/minecraft/icon/" + "SecureChest" + ".PNG")));
        securityLevelOtherLabel.setText("Security Level: "+chestToShow.getSecurityLevel());
        withdrawOther.setDisable(true);
        depositOther.setDisable(true);
        chestToShow.getAccessPermissions().entrySet().stream()
        .filter(entry->entry.getKey().equals(username)).map(entry->entry.getValue())
        .filter(entry->entry==2).findFirst().ifPresent(e->{
            withdrawOther.setDisable(false);
            depositOther.setDisable(false);
        });
        viewChestOther.setOnAction(e-> handleViewChest());
        withdrawOther.setOnAction(e->{
            try {
                handleWithdrawOther();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        depositOther.setOnAction(e->{
            try {
                handleDepositOther();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    public void handleViewChest() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("View Chest");
        String cssFilePath = getClass().getResource("minecraft-style.css").toExternalForm();
        dialog.getDialogPane().getStylesheets().add(cssFilePath);
        dialog.getDialogPane().getStyleClass().add("dialog-pane");

        dialog.getDialogPane().setPrefWidth(600);
        dialog.getDialogPane().setPrefHeight(400);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefWidth(500);
        scrollPane.setPrefHeight(200);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        int rowCount=0, colCount =0;

        for (Map.Entry<EnderBackpackItem, Integer> item: chestToShow.getItemChest().entrySet()){
            if (item.getValue()!=0){
                Button button = new Button();
                VBox box  = new VBox(-10);
                ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/Minecraft/icon/" + item.getKey().getName() + ".PNG")));
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                imageView.setPreserveRatio(true);

                // Create a Button and set the ImageView as its graphic
                button.setGraphic(imageView);
                button.setMinSize(60, 60);
                button.setMaxSize(60, 60);
                button.setStyle("-fx-background-color: transparent;");
                box.getStyleClass().add("griditem");
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                imageView.setPreserveRatio(true);
                button.setTooltip(new Tooltip(item.getKey().getName() + "\nType: " + 
                                                item.getKey().getType() + "\nFunction: " + 
                                                item.getKey().getFunction() + "\nQuantity: " + 
                                                item.getValue()));
                Label quantitylabel = new Label(String.valueOf(item.getValue()));
                quantitylabel.getStyleClass().add("text-body");
                box.getChildren().addAll(button, quantitylabel);
                // Create a Button and set the ImageView as its graphic
                gridPane.add(box, colCount, rowCount);

                colCount++;

                if(colCount==5){
                    rowCount++;
                    colCount=0;
                }
            } 
        }
        // Add GridPane to ScrollPane
        scrollPane.setContent(gridPane);

        // Create an AnchorPane and add the GridPane to it
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().add(scrollPane);

        // Set the anchors for the GridPane
        AnchorPane.setTopAnchor(scrollPane, 55.0);
        AnchorPane.setLeftAnchor(scrollPane, 48.0);
        AnchorPane.setRightAnchor(scrollPane, 48.0);
        
        // Create the DialogPane
        dialog.getDialogPane().setContent(anchorPane);
       
        // Add buttons to the dialog
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().lookupButton(ButtonType.OK).getStyleClass().add("button");
        dialog.getDialogPane().lookupButton(ButtonType.CANCEL).getStyleClass().add("button");

        // Show the dialog
        dialog.showAndWait();
    }

    public void handleDepositOther() throws IOException{
        depositChest.chest = chestToShow;
        depositChest.username = username;
        Parent root = FXMLLoader.load(getClass().getResource("depositChest.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) viewChestOther.getScene().getWindow();
        scene.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Deposit Item To " + chestToShow.getOwner() + "\'s Secure Chest");
        stage.setOnCloseRequest(e->{
            chestToShow = depositChest.chest;
            try {
                Parent root1 = FXMLLoader.load(getClass().getResource("secureChestOther.fxml"));
                Scene scene1 = new Scene(root1);
                scene1.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
                Stage stage1= new Stage();
                stage1.setTitle("Minecraft");
                stage1.setScene(scene1);
                Image icon1 = new Image(getClass().getResourceAsStream("/minecraft/icon/SecureChest.png"));
                stage1.getIcons().clear();
                stage1.getIcons().add(icon1);
                stage1.setOnCloseRequest(e1->{
                    try {
                        SecureChestController.chest = chestToShow;
                        SecureChestController.username = username;
                        Parent root2 = FXMLLoader.load(getClass().getResource("SecureChest.fxml"));
                        Scene scene2 = new Scene(root2);
                        scene2.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
                        Stage stage2= new Stage();
                        stage2.setTitle("Scure Chest");
                        stage2.setScene(scene1);
                        Image icon2 = new Image(getClass().getResourceAsStream("/minecraft/icon/SecureChest.png"));
                        stage2.getIcons().clear();
                        stage2.getIcons().add(icon2);
                        stage2.show();
                        stage1.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                });
                stage1.show();
                stage.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    public void handleWithdrawOther() throws IOException{
        withdrawChest.chest = chestToShow;
        withdrawChest.username = username;
        Parent root = FXMLLoader.load(getClass().getResource("withdrawChest.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
        Stage stage = (Stage) viewChestOther.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Withdraw Item From " + chestToShow.getOwner() + "\'s Secure Chest");
        stage.setOnCloseRequest(e->{
            chestToShow = withdrawChest.chest;
            try {
                Parent root1 = FXMLLoader.load(getClass().getResource("secureChestOther.fxml"));
                Scene scene1 = new Scene(root1);
                scene1.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
                Stage stage1= new Stage();
                stage1.setTitle("Minecraft");
                stage1.setScene(scene1);
                Image icon1 = new Image(getClass().getResourceAsStream("/minecraft/icon/SecureChest.png"));
                stage1.getIcons().clear();
                stage1.getIcons().add(icon1);
                stage1.setOnCloseRequest(e1->{
                    try {
                        SecureChestController.chest = chestToShow;
                        SecureChestController.username = username;
                        Parent root2 = FXMLLoader.load(getClass().getResource("SecureChest.fxml"));
                        Scene scene2 = new Scene(root2);
                        scene2.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
                        Stage stage2= new Stage();
                        stage2.setTitle("Secure Chest");
                        stage2.setScene(scene2);
                        Image icon2 = new Image(getClass().getResourceAsStream("/minecraft/icon/SecureChest.png"));
                        stage2.getIcons().clear();
                        stage2.getIcons().add(icon2);
                        stage2.show();
                        stage1.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                });
                stage1.show();
                stage.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }
}
