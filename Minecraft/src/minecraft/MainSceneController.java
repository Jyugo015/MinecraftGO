/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package minecraft;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class MainSceneController implements Initializable {

    @FXML
    private Button btn;

    @FXML
    private Button ender;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    // private void switchToMainScene() {
    //     try {
    //         // Load the MainScene
    //         Parent root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
    //         Scene scene = new Scene(root);
            
    //         // Apply the stylesheet
    //         scene.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
            
    //         // Set the main scene back to the primary stage
    //         stage.setScene(scene);
    //         stage.setTitle("Minecraft");
    //         stage.show();
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    @FXML
    private void switchToMultitool(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MultitoolGUI.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Multi Tool");
        scene.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
        Image icon = new Image(getClass().getResourceAsStream("/minecraft/icon/Multitool.png"));
        stage.getIcons().clear();
        stage.getIcons().add(icon);
        stage.setOnCloseRequest(e->{
            try {
                System.out.println("In");
                Parent root1 = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
                Scene scene1 = new Scene(root1);
                scene1.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
                Stage stage1= new Stage();
                stage1.setTitle("Minecraft");
                stage1.setScene(scene1);
                Image icon1 = new Image(getClass().getResourceAsStream("/minecraft/icon/Minecraft.png"));
                stage.getIcons().clear();
                stage.getIcons().add(icon1);
                stage1.show();
                stage.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        stage.show();
    }

    @FXML
    public void switchToEnderBackpack(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("EnderBackpack.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Ender Backpack");
        scene.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
        Image icon = new Image(getClass().getResourceAsStream("/minecraft/icon/Ender Backpack.png"));
        stage.getIcons().clear();
        stage.getIcons().add(icon);
        stage.setOnCloseRequest(e->{
            try {
                System.out.println("In");
                Parent root1 = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
                Scene scene1 = new Scene(root1);
                scene1.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
                Stage stage1= new Stage();
                stage1.setTitle("Minecraft");
                stage1.setScene(scene1);
                Image icon1 = new Image(getClass().getResourceAsStream("/minecraft/icon/Minecraft.png"));
                stage.getIcons().clear();
                stage.getIcons().add(icon1);
                stage1.show();
                stage.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        stage.show();
    }


    @FXML
    private void switchToPotionSatchel(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("PotionSatchel.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Potion Stachel");
        scene.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
        stage.getIcons().clear();
        Image icon = new Image(getClass().getResourceAsStream("/minecraft/icon/Potion.png"));
        stage.getIcons().add(icon);
        stage.setOnCloseRequest(e->{
            try {
                System.out.println("In");
                Parent root1 = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
                Scene scene1 = new Scene(root1);
                scene1.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
                Stage stage1= new Stage();
                stage1.setTitle("Minecraft");
                stage1.setScene(scene1);
                Image icon1 = new Image(getClass().getResourceAsStream("/minecraft/icon/Minecraft.png"));
                stage.getIcons().clear();
                stage.getIcons().add(icon1);
                stage1.show();
                stage.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        stage.show();
    }

    @FXML void switchToSecureChest(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("SecureChest.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Secure Chest");
        scene.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
        stage.getIcons().clear();
        Image icon = new Image(getClass().getResourceAsStream("/minecraft/icon/SecureChest.png"));
        stage.getIcons().add(icon);
        stage.setOnCloseRequest(e->{
            try {
                System.out.println("In");
                Parent root1 = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
                Scene scene1 = new Scene(root1);
                scene1.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
                Stage stage1= new Stage();
                stage1.setTitle("Minecraft");
                stage1.setScene(scene1);
                Image icon1 = new Image(getClass().getResourceAsStream("/minecraft/icon/Minecraft.png"));
                stage.getIcons().clear();
                stage.getIcons().add(icon1);
                stage1.show();
                stage.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        stage.show();
    }

    @FXML
    private void switchToAutoFarmerBlock(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Autofarmerblock.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Autofarmer Block");
        scene.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
        stage.getIcons().clear();
        Image icon = new Image(getClass().getResourceAsStream("/minecraft/icon/AutofarmerBlock.png"));
        stage.getIcons().add(icon);
        stage.setOnCloseRequest(e->{
            try {
                System.out.println("In");
                Parent root1 = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
                Scene scene1 = new Scene(root1);
                scene1.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
                Stage stage1= new Stage();
                stage1.setTitle("Minecraft");
                stage1.setScene(scene1);
                Image icon1 = new Image(getClass().getResourceAsStream("/minecraft/icon/Minecraft.png"));
                stage.getIcons().clear();
                stage.getIcons().add(icon1);
                stage1.show();
                stage.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        stage.show();
    }
    @FXML
    private void switchToAdventurerDiary(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AdventurerDiary.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Adventurer Diary");
        scene.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
        stage.getIcons().clear();
        Image icon = new Image(getClass().getResourceAsStream("/minecraft/icon/AdventurerDiary.png"));
        stage.getIcons().add(icon);
        stage.setOnCloseRequest(e->{
            try {
                System.out.println("In");
                Parent root1 = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
                Scene scene1 = new Scene(root1);
                scene1.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
                Stage stage1= new Stage();
                stage1.setTitle("Minecraft");
                stage1.setScene(scene1);
                Image icon1 = new Image(getClass().getResourceAsStream("/minecraft/icon/Minecraft.png"));
                stage.getIcons().clear();
                stage.getIcons().add(icon1);
                stage1.show();
                stage.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        stage.show();
    }

    @FXML
    private void switchToCretureEncyclopedia(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("CreatureEncyclopedia.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Creature Encyclopedia");
        scene.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
        stage.getIcons().clear();
        Image icon = new Image(getClass().getResourceAsStream("/minecraft/icon/CreatureEncyclopedia.png"));
        stage.getIcons().add(icon);
        stage.setOnCloseRequest(e->{
            try {
                System.out.println("In");
                Parent root1 = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
                Scene scene1 = new Scene(root1);
                scene1.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
                Stage stage1= new Stage();
                stage1.setTitle("Minecraft");
                stage1.setScene(scene1);
                Image icon1 = new Image(getClass().getResourceAsStream("/minecraft/icon/Minecraft.png"));
                stage.getIcons().clear();
                stage.getIcons().add(icon1);
                stage1.show();
                stage.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        stage.show();
    }

    @FXML
    private void switchToTeleportationNetwork(ActionEvent event) {
         TeleportationNetworkController_GUI teleportationNetwork = new TeleportationNetworkController_GUI();
        try {
            teleportationNetwork.start((Stage) ((Button) event.getSource()).getScene().getWindow());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void switchToAutomatedSortingChest(ActionEvent event) {
        AutomatedSortingChest sortedChest = new AutomatedSortingChest();
        try{
            sortedChest.start((Stage)((Button)event.getSource()).getScene().getWindow());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void switchToLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Login");
        scene.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
        stage.getIcons().clear();
        Image icon = new Image(getClass().getResourceAsStream("/minecraft/icon/Minecraft.png"));
        stage.getIcons().add(icon);
        // stage.setOnCloseRequest(e->{
        //     try {
        //         System.out.println("In");
        //         Parent root1 = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
        //         Scene scene1 = new Scene(root1);
        //         scene1.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
        //         Stage stage1= new Stage();
        //         stage1.setTitle("Minecraft");
        //         stage1.setScene(scene1);
        //         Image icon1 = new Image(getClass().getResourceAsStream("/minecraft/icon/Minecraft.png"));
        //         stage.getIcons().clear();
        //         stage.getIcons().add(icon1);
        //         stage1.show();
        //         stage.close();
        //     } catch (IOException e1) {
        //         e1.printStackTrace();
        //     }
        // });
        stage.show();
    }
}
