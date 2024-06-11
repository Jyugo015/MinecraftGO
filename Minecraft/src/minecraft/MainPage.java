/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package minecraft;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Asus
 */
public class MainPage extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));//Login.fxml
        Scene scene = new Scene (root);
        stage.setTitle("Login");
        Image icon = new Image(getClass().getResourceAsStream("/minecraft/icon/Minecraft.png"));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
        // Parent root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
        // Scene scene = new Scene (root);
        // stage.setTitle("Main");
        // Image icon = new Image(getClass().getResourceAsStream("/minecraft/icon/Minecraft.png"));
        // stage.getIcons().add(icon);
        // stage.setScene(scene);
        // stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
