/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minecraft;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static javafx.scene.input.MouseButton.PRIMARY;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author PC
 */
public class MultiToolScene2 extends Application{
    private MultipleTool multipleTool;
    private Tool currentTool;
    private Image toolImage;
    private ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/minecraft/icon/background.jpeg")));
    private Label notification = new Label();
    private Rectangle rectangle;
    private StackPane notificationBox = new StackPane();
    private FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.7), notificationBox);
    private FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.7), notificationBox);
    public static String username;
    @Override
    public void start(Stage stage) throws Exception {
        // Get data from the database
        multipleTool = new MultipleTool(username);
        currentTool = multipleTool.getTool(0);
        imageView.setFitHeight(300);
        imageView.setFitWidth(300);
        imageView.setRotate(290);
        if (currentTool != null) {
            toolImage = new Image(getClass().getResourceAsStream("/minecraft/icon/" + currentTool.getName() + ".png"));
            imageView.setImage(toolImage);
        }
        
        Pane pane = new Pane();
        Button leftButton = new Button("Left");
        Button rightButton = new Button("Right");
        Button mainPageButton = new Button("Back To Main Page");
        Button selectMultiToolButton = new Button("Select Multitool");
        leftButton.getStyleClass().add("button");
        rightButton.getStyleClass().add("button");
        mainPageButton.getStyleClass().add("button");
        selectMultiToolButton.getStyleClass().add("button");
        pane.getChildren().addAll(notificationBox, imageView, leftButton, rightButton, mainPageButton, selectMultiToolButton);
        imageView.layoutXProperty().bind(pane.widthProperty().subtract(imageView.fitWidthProperty().multiply(0.8)));
        imageView.layoutYProperty().bind(pane.heightProperty().subtract(imageView.fitHeightProperty().multiply(0.8)));
        leftButton.layoutXProperty().bind(pane.widthProperty().subtract(150));
        leftButton.layoutYProperty().bind(pane.heightProperty().subtract(50));
        rightButton.layoutXProperty().bind(pane.widthProperty().subtract(100));
        rightButton.layoutYProperty().bind(pane.heightProperty().subtract(50));
        mainPageButton.setLayoutX(50);
        mainPageButton.setLayoutY(50);
        selectMultiToolButton.setLayoutX(50);
        selectMultiToolButton.layoutYProperty().bind(pane.heightProperty().subtract(50));
        notificationBox.layoutXProperty().bind(pane.widthProperty().divide(2).subtract(notificationBox.widthProperty().divide(2)));
        notificationBox.layoutYProperty().bind(pane.heightProperty().divide(2).subtract(notificationBox.heightProperty().divide(2)));
        
        // Fade out notification
        // Create a Rectangle
        rectangle = new Rectangle(300, 40);
        rectangle.setFill(Color.BLACK);
        rectangle.setArcWidth(10); // Rounded corners
        rectangle.setArcHeight(10); // Rounded corners

        // Create a Label
        notification.setTextFill(Color.WHITE);
        notification.getStyleClass().add("text-body");
        
        // Create a stackpane as holder of the notification
        notificationBox.getChildren().addAll(rectangle, notification);
        notificationBox.setOpacity(0);
        
        // Background
        Image backgroundImage = new Image(getClass().getResourceAsStream("/minecraft/icon/MainPage.png"));
        BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, false);
        Background background = new Background(new BackgroundImage(backgroundImage,BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            bSize));
        pane.setBackground(background);
        
        // Button on the screen presses
        leftButton.setOnAction(e->{
            leftEvent();
            fading();
        });
        
        rightButton.setOnAction(e->{
            rightEvent();
            fading();
        });
        
        mainPageButton.setOnAction(e->{
            stage.close();
//             try {
//                 MainPage mainPage = new MainPage();
//                 mainPage.start((Stage) ((Button) e.getSource()).getScene().getWindow());
// //                stage.close();
//             } catch (IOException ex) {
//                 Logger.getLogger(AutomatedSortingChest.class.getName()).log(Level.SEVERE, null, ex);
//             }
            Parent root;
            try {
                root = FXMLLoader.load(getClass().getResource("MultitoolGUI.fxml"));
                Scene scene = new Scene(root);
                Stage stage2 = (Stage) leftButton.getScene().getWindow();
                stage2.setScene(scene);
                stage2.setTitle("Multi Tool");
                scene.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
                Image icon = new Image(getClass().getResourceAsStream("/minecraft/icon/Multitool.png"));
                stage2.getIcons().clear();
                stage2.getIcons().add(icon);
                stage.setOnCloseRequest(e1->{
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
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            });
            stage.show();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        
        selectMultiToolButton.setOnAction(e->{
            stage.close();
            Parent root;
            try {
                root = FXMLLoader.load(getClass().getResource("MultitoolGUI.fxml"));
                Scene scene = new Scene (root);
                scene.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
                stage.setTitle("Multitool");
//                Image icon = new Image(getClass().getResourceAsStream("/minecraft/icon/Minecraft.png"));
//                stage.getIcons().add(icon);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(MultiToolScene2.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        });
        
        pane.setOnMouseClicked(e->{
            switch (e.getButton()) {
                case SECONDARY:
                    rightEvent();
                    fading();
                    break;
                default:
                    break;
            }
        });
        
        pane.setOnScroll(e->{
            if (e.getDeltaY() <0) {
                leftEvent();fading();
            } else if (e.getDeltaY( )> 0) {
                rightEvent();fading();
            }
        });
//        pane.setFocusTraversable(true);
        Scene scene1 = new Scene(pane, 700, 400);
        scene1.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
        stage.setTitle("Using Multi Tool");
        stage.setScene(scene1);
        stage.show();
    }
    
    public void fading() {
        fadeIn.stop();
        fadeOut.stop();
        
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.setCycleCount(1);
        fadeIn.setAutoReverse(false);
        fadeIn.play();

        // After fade in, fade out the notification
        fadeIn.setOnFinished(ev -> {
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setCycleCount(1);
            fadeOut.setAutoReverse(false);
            fadeOut.setDelay(Duration.seconds(2)); // Delay before fade out
            fadeOut.play();
        });
    }
    
    private void leftEvent () {
        currentTool = multipleTool.switchToolUp(currentTool);
        toolImage = new Image(getClass().getResourceAsStream("/minecraft/icon/" + currentTool.getName() + ".png"));
        imageView.setImage(toolImage);
        notification.setText("You have switched to " + currentTool.getName());
    }
    
    private void rightEvent () {
        currentTool = multipleTool.switchToolDown(currentTool);
        toolImage = new Image(getClass().getResourceAsStream("/minecraft/icon/" + currentTool.getName() + ".png"));
        imageView.setImage(toolImage);
        notification.setText("You have switched to " + currentTool.getName());
    }
}
