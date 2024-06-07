package minecraft;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import minecraft.TeleportationNetworkController.Edge;
import minecraft.TeleportationNetworkController.Point;
import static minecraft.database_item5.getConnection;


public class TeleportationNetworkController_GUI extends Application {
    private static String imageFilePath = "/minecraft/icon/";
    private static Image defaultImage;
    private static Image mapImage;
    private static float r = 20;
    private static ArrayList<SingleTeleportationPoint> points = new ArrayList<>();

    private static Stage stage = new Stage(); 
    private static BorderPane pane1; 
    private static String[] nodesName = {"A","B","C","D"};
//    private static String[] nodesImages = {"C:/Users/PC/Documents/UMHomework/y1s2/WIA1002 DS/Assignment/DS-assignment/Minecraft/src/minecraft/icon/A.jpg","C:/Users/PC/Documents/UMHomework/y1s2/WIA1002 DS/Assignment/DS-assignment/Minecraft/src/minecraft/icon/B.jpg","C:/Users/PC/Documents/UMHomework/y1s2/WIA1002 DS/Assignment/DS-assignment/Minecraft/src/minecraft/icon/C.jpg","C:/Users/PC/Documents/UMHomework/y1s2/WIA1002 DS/Assignment/DS-assignment/Minecraft/src/minecraft/icon/D.jpg"};
    private static boolean isSelected = false;
    private static boolean selectionMode = false;
    private static boolean addingNewNode = false;
    private static ArrayList<SingleTeleportationPoint> selected = new ArrayList<>();
    private static ArrayList<Line> edges = new ArrayList<>();
    private static Label reminder = new Label();
    private static String username;
    private static String css_hover = "-fx-background-color: #6c2929";
    private static String css_original ="-fx-background-color: black;-fx-background-insets: 0;-fx-background-radius: 0; -fx-border-color: transparent;"
                        + "-fx-border-width: 0;-fx-padding: 5px 10px;-fx-font-family: 'Unifont';"
                        + "-fx-font-size: 14px;-fx-text-fill: white;";
    private static Circle imaginaryCircle = new Circle(r);
    private static SingleTeleportationPoint currentPoint;
    private static SingleTeleportationPoint currentlySelected;
    private static Button backToMainPageButton = new Button("Back to main page");
    
    
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException, SQLException {
        defaultImage = new Image(getClass().getResourceAsStream(imageFilePath +"background.jpeg"));
        mapImage = new Image(getClass().getResourceAsStream(imageFilePath +"Map.png"));
        pane1 = new BorderPane();
        try {
            Connection connection = getConnection();
            String statement = "SELECT * FROM teleportationPoint";
            PreparedStatement select = connection.prepareStatement(statement);
            ResultSet result = select.executeQuery();
            ArrayList<Point> pointsFromDB = new ArrayList<>();
            while (result.next()){
                String nodeName = result.getString("name");
                pointsFromDB.add(new TeleportationNetworkController.Point(result.getString("name"), result.getString("username"), result.getFloat("x_coordinate"),result.getFloat("y_coordinate"), database_item5.retrieveNeighbour(nodeName), database_item5.retrieveRequestGet(nodeName), database_item5.retrieveRequestSent(nodeName)));
            }
            TeleportationNetworkController.restoreNode(pointsFromDB);
           
        } catch (SQLException ex) {
            Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
        Background background = new Background(new BackgroundImage(mapImage,BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            bSize));
        pane1.setBackground(background);
        
        // Initiate the current point if any ------------------------------------------------------get username ----------------------------------
        username = "defaultUser";
        // set tthe button style
        backToMainPageButton.setStyle("-fx-background-color: rgb(144, 206, 227);"
                + "-fx-background-insets: 0;-fx-background-radius: 0;"
                + "-fx-border-color: rgb(144, 206, 227) white white rgb(144, 206, 227);"
                + "-fx-border-width: 2;-fx-padding: 0px 0px;-fx-font-family: 'Unifont';"
                + "-fx-font-size: 13px;-fx-text-fill: black;");
        backToMainPageButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            backToMainPageButton.setStyle("-fx-background-color: linear-gradient(#d2d2d2, #bfbfbf); -fx-background-color: rgb(17, 56, 77);"
                    + "-fx-text-fill: white;-fx-border-color: rgb(17, 56, 77) white white rgb(17, 56, 77);-fx-border-color: #6c2929;");
        });
        backToMainPageButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            backToMainPageButton.setStyle("-fx-background-color: rgb(144, 206, 227);"
                + "-fx-background-insets: 0;-fx-background-radius: 0;"
                + "-fx-border-color: rgb(144, 206, 227) white white rgb(144, 206, 227);"
                + "-fx-border-width: 2;-fx-padding: 0px 0px;-fx-font-family: 'Unifont';"
                + "-fx-font-size: 13px;-fx-text-fill: black;");
        });
        backToMainPageButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            backToMainPageButton.setStyle("-fx-background-color: rgb(17, 56, 77); -fx-border-color: rgb(17, 56, 77) white white rgb(17, 56, 77); "
                    + "-fx-border-color:#6c2929; -fx-text-fill: white;");
        });
        backToMainPageButton.setOnAction(e->{
            try {
                MainPage mainPage = new MainPage();
                mainPage.start((Stage) ((Button) e.getSource()).getScene().getWindow());
//                stage.close();
            } catch (IOException ex) {
                Logger.getLogger(AutomatedSortingChest.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        stage = primaryStage;
        stage.setTitle("Teleportation Control Network");
        Scene scene = new Scene(pane1, 1500,800);
        scene.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
        Image icon1 = new Image(getClass().getResourceAsStream("/minecraft/icon/teleportation.png"));
        stage.getIcons().clear();
        stage.getIcons().add(icon1);
        stage.setScene(scene);
        stage.show();
        
        reminder.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        
        mainScene();
        
    }

    public static void setButtonCSS(Button button) {
        button.setStyle(css_original);
        
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler() {
            @Override
            public void handle(Event t) {
                button.setStyle(css_hover);
            }
        });
        button.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler() {
            @Override
            public void handle(Event t) {
                button.setStyle(css_original);
            }
        });
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler() {
            @Override
            public void handle(Event t) {
                button.setStyle(css_hover);
            }
        });
    }
    
    // Scene 1: main scene
    public static void mainScene() {
         
        selectionMode = false;
        currentlySelected = null;
        Button addNewPointButton = new Button("Add a new teleportation point");
        Button removePointButton = new Button("Remove a teleportation point");
        reset();
        
        // Button css
        setButtonCSS(addNewPointButton);
        setButtonCSS(removePointButton);
        
        // Set on action
        addNewPointButton.setOnAction(e->{
            try {
                selectNewNodeLocation();
            } catch (SQLException ex) {
                Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        removePointButton.setOnAction(e->{
            try {
                removePoint();
            } catch (SQLException ex) {
                Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        // Bottom pane
        HBox bottomPane = new HBox(addNewPointButton, removePointButton);
        bottomPane.setPadding(new Insets(10,10,10,10));
        pane1.setBottom(bottomPane);
        
    }
    
    
    public static void removePoint() throws SQLException{
        reminder.setText("Choose the teleportation point you wish to remove.");
        reset();
        Button cancelButton = new Button("Cancel");
        Button confirmRemoveButton = new Button("Confirm");        
        canAllBeSelected(false);
        selectionMode = true;
        
        // dim the node that cannot be removed as not belong to the user
        for (SingleTeleportationPoint p : points) {
            if (p.getOwnerName().equals(username)) {
                p.canSelect = true;
            } else {
                p.circleFrame.setOpacity(0.5);
            }
        }
        
        HBox bottomPane = new HBox(cancelButton, confirmRemoveButton);
        pane1.setBottom(bottomPane); // make user choose to cancel or confirm

        // Button css
        setButtonCSS(cancelButton);
        setButtonCSS(confirmRemoveButton);
        
        // Set on action
        confirmRemoveButton.setOnAction(e->{
            if (isSelected) {
                for (SingleTeleportationPoint point : selected) {
                    try {
                        TeleportationNetworkController.removeNode(point.getNodeName());
                    } catch (SQLException ex) {
                        Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    points.remove(point);
                }
                reminder.setText("You have remove " + selected.size() + " nodes.");
                if (! points.contains(currentPoint))  
                    currentPoint = null;
                
                ArrayList<String> points = new ArrayList<>();
                boolean hasAWayToGo = false;
                try {
                    points = database_item5.retrieveteleportationPoints(username);
                } catch (SQLException ex) {
                    Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                for (int i = 0; i < points.size(); i++) {
                    if (TeleportationNetworkController.shortestPath(points.get(i), currentPoint.point.getNameOfTeleportationPoint()) != null) {
                        hasAWayToGo = true;
                        break;
                    }
                }
                if (! hasAWayToGo) {
                    currentPoint = null;
                    try {
                        database_item5.setCurrentPoint(username, null);
                    } catch (SQLException ex) {
                        Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                mainScene();
            } else {
                reminder.setText("Please select the point you wish to remove.");
            }
            
        });

        cancelButton.setOnAction(e->{
            reminder.setText("");
            mainScene();
        });
    }
    
    public static void neighbourSelection () throws SQLException{
        reminder.setText("Who do you want to be neighbour with?");
        reset();
        if (addingNewNode) {
            currentlySelected = points.get(points.size()-1);
            addingNewNode = false;
        }
        selectionMode = true;
        canAllBeSelected(true); // default all can be selected
        Button confirmButton = new Button("Let's become neighbours");
        Button cancelButton = new Button("Cancel");
        // check if the point is current point OR neighbour already, cannot be chosen to add anymore
        for (SingleTeleportationPoint point : points) {
            // Highlight the current point
            if (point.getNodeName().equals(currentlySelected.getNodeName())) {
                point.canSelect = false;
                point.circleFrame.setOpacity(0.8);
                point.filter.setFill(Color.rgb(255,0,0,0.3));
            } else if (currentPoint != null && point.getNodeName().equals(currentPoint.getNodeName())) {
                point.filter.setFill(Color.TRANSPARENT);
                point.circleFrame.setOpacity(1);
            }
            for (int i = 0; i < currentlySelected.getNeighbours().size(); i++) {
                if (point.getNodeName().equals(currentlySelected.getNeighbours().get(i)) || point.getNodeName().equals(currentlySelected.getNodeName())) {
                    point.canSelect = false;
                    point.circleFrame.setOpacity(0.5);
                    break;
                }
            }
        }
        
        // Button css
        setButtonCSS(confirmButton);
        setButtonCSS(cancelButton);
        // Set on action
        confirmButton.setOnAction(e->{
            if (isSelected) {
                int size = selected.size();
                String text1 = "Yeah! you have sent the request to ";
                String text2 = "You have resent the request to ";
                boolean isSent = false;
                boolean isResent = false;
                for (int i = 0; i< size; i++) {
                    try {
                        if (TeleportationNetworkController.getNode(currentlySelected.getNodeName()).sendFriendRequest(selected.get(i).getNodeName())) {
                            isSent = true;
                            text1 += "[" + selected.get(i).getNodeName() + "] ";
                            try {
                                database_item5.addRequestSent(currentlySelected.getNodeName(), selected.get(i).getNodeName());
                                
                            } catch (SQLException ex) {
                                System.out.println("Unable to save friend request sent");
                            }
                        } else {
                            isResent = true;
                            text2 += "[" + selected.get(i).getNodeName() + "] ";
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                selected.clear();
                isSelected = false;
                reminder.setText((isSent? text1 : "") + (isResent ? text2 : ""));
                mainScene();
            } else {
                reminder.setText("Please select the new neighbour");
            }
            
        });
        
        cancelButton.setOnAction(e->{
            reminder.setText("");
            mainScene();
        });
        
        HBox bottomPane = new HBox(cancelButton, confirmButton);
        bottomPane.setPadding(new Insets(10,10, 10,10));
        pane1.setBottom(bottomPane);
        
    }
    
    public static void removeNeighbourSelection () {
        reminder.setText("Which neighbour do you wish to remove? (Argue ? lmao!)");
        reset();
        isSelected = false;
        selectionMode = true;
        canAllBeSelected(false);
        Button confirmButton = new Button("REMOVE!");
        Button cancelButton = new Button("Cancel. i have cooled down");
        for (SingleTeleportationPoint point : points) {
            // Highlight the current point
            if (point.getNodeName().equals(currentlySelected.getNodeName())) {
                point.circleFrame.setOpacity(0.8);
                point.filter.setFill(Color.rgb(255,0,0,0.3));
            } else if (currentPoint != null && point.getNodeName().equals(currentPoint.getNodeName())) {
                point.circleFrame.setOpacity(1);
                point.filter.setFill(Color.TRANSPARENT);
            }
            for (int i = 0; i < currentlySelected.getNeighbours().size(); i++) {
                if (point.getNodeName().equals(currentlySelected.getNeighbours().get(i))) {
                    point.canSelect = true;
                    break;
                }
            }
            
            if (! point.canSelect) {
                point.circleFrame.setOpacity(0.5);
            }
        }
        
        // Button css
        setButtonCSS(confirmButton);
        setButtonCSS(cancelButton);
        // Set on action
        confirmButton.setOnAction(e->{
            if (isSelected) {
                String text = "Wooohh! You are now no longer connected to ";
                int size = selected.size();
                for (int i = 0; i< size-1; i++) {
                    try {
                        System.out.println("remove neighbour with " + selected.get(i) + "? " + TeleportationNetworkController.getNode(currentlySelected.getNodeName()).removeNeighbour(selected.get(i).getNodeName()));
                    } catch (SQLException ex) {
                        Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    text += selected.get(i).getNodeName() + ", ";
                }
                try {
                    System.out.println("remove neighbour with " + selected.get(size-1) + "? " + TeleportationNetworkController.getNode(currentlySelected.getNodeName()).removeNeighbour(selected.get(size-1).getNodeName()));
                } catch (SQLException ex) {
                    Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                text += (size>1) ? " and " + selected.get(size-1) : "" + selected.get(size-1);
                selected.clear();
                isSelected = false;
                reminder.setText(text);
                
                ArrayList<String> points = new ArrayList<>();
                boolean hasAWayToGo = false;
                try {
                    points = database_item5.retrieveteleportationPoints(username);
                } catch (SQLException ex) {
                    Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                for (int i = 0; i < points.size(); i++) {
                    if (TeleportationNetworkController.shortestPath(points.get(i), currentPoint.point.getNameOfTeleportationPoint()) != null) {
                        hasAWayToGo = true;
                        break;
                    }
                }
                if (! hasAWayToGo) {
                    currentPoint = null;
                    try {
                        database_item5.setCurrentPoint(username, null);
                    } catch (SQLException ex) {
                        Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                mainScene();
            } else {
                reminder.setText("Please select the neighbour(s) to remove (sad");
            }
            
        });
        
        cancelButton.setOnAction(e->{
            reminder.setText("");
            mainScene();
        });
        
        HBox bottomPane = new HBox(cancelButton, confirmButton);
        bottomPane.setPadding(new Insets(10,10, 10,10));
        pane1.setBottom(bottomPane);
    }    
    
    // Popout: to insert the information of the new node
    public static void newPointInformationWindow() throws SQLException{
        pane1.setDisable(true);
        Button cancelButton = new Button("Cancel");
        Button confirmAddButton = new Button("Confirm");
        Text text = new Text("Your new teleportation point's name: ");
        Text warningText = new Text("");
        TextField TPnameTextField = new TextField();
        Stage stage = new Stage();
        stage.setWidth(400);
        stage.setHeight(200);
        BorderPane pane = new BorderPane();
        
        // Center pane
        HBox centerPane = new HBox(text, TPnameTextField);
        centerPane.setPadding(new Insets(10,10,10,10));

        //Bottom pane
        HBox bottomPane = new HBox(cancelButton, confirmAddButton);
        bottomPane.setPadding(new Insets(10,10,10,10));

        pane.setCenter(centerPane);
        pane.setBottom(bottomPane);
        pane.setTop(warningText);
        
        // Button css
        setButtonCSS(confirmAddButton);
        setButtonCSS(cancelButton);
        
        // Set on action
        confirmAddButton.setOnAction(e->{
            pane1.setDisable(false);
            if (TPnameTextField.getText()!=null && ! TPnameTextField.getText().equals("")) {
                try {
                    if (TeleportationNetworkController.addNewNode(TPnameTextField.getText(), username, (float)imaginaryCircle.getLayoutX(), (float)imaginaryCircle.getLayoutY(),null,null,null)) {
                        stage.close();
                        addingNewNode = true;
                        pane.getChildren().remove(imaginaryCircle);
                        enquiryAddNeighoursWindow();
                    } else {
                        warningText.setText("Sorry, the teleportation point with same name exist already. Please use anthor name.");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                warningText.setText("Please give a name to your teleportation point");
            }
            
        });
        
        TPnameTextField.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == null || newValue.equals("")){
                    reminder.setText("Let's give your teleportation point a new name");
                    TPnameTextField.setStyle("-fx-border-color : blue");
                } else {
                    reminder.setText("");
                    TPnameTextField.setStyle("-fx-border-color : rgba(0,0,0,0)");
                }
            }

        });
        
        cancelButton.setOnAction(e->{
            pane1.setDisable(false);
            stage.close();
            reminder.setText("Opps! You failed in creating a new point!");
            mainScene();
        });
        
        stage.setOnCloseRequest((WindowEvent t) -> {
            pane1.setDisable(false);
            stage.close();
            reminder.setText("Opps! You failed in creating a new point!");
            mainScene();
        });
        
        stage.setScene(new Scene(pane));
        stage.setTitle("Construct your new teleportation point");
        stage.show();
    }
    
    // Popout: to ask if the new node want to add a new neighbour
    public static void enquiryAddNeighoursWindow() throws SQLException{
        Stage stage = new Stage();
        BorderPane pane = new BorderPane();
        Text text = new Text("Do you want to add neighbours? ");
        Button yesButton = new Button("Yes! Let's join!");
        Button noButton = new Button("Nope! I want to be alone!");
        
        // Bottom pane 
        HBox bottomPane = new HBox(yesButton, noButton);
        
        pane.setCenter(text);
        pane.setBottom(bottomPane);
        
        
        // Button css
        setButtonCSS(yesButton);
        setButtonCSS(noButton);
        
        noButton.setOnAction(e->{
            stage.close();
            reminder.setText("Congratulation " + username + "! You have sucessfully create a new point!");
            mainScene();
        });
        
        yesButton.setOnAction(e->{
            stage.close();
            try {
                neighbourSelection();
            } catch (SQLException ex) {
                Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        stage.setOnCloseRequest(new EventHandler<WindowEvent>(){
            @Override
            public void handle(WindowEvent t) {
                stage.close();
                reminder.setText("Congratulation " + username + "! You have sucessfully create a new point!");
                mainScene();
            }
        });
        
        stage.setScene(new Scene(pane));
        stage.show();
        
    }
    
    // Set all node to be can be selected or not at one time
    public static void canAllBeSelected(Boolean value) {
        for (SingleTeleportationPoint p : points) {
            p.canSelect = value;
        }
    }
    
    public static void selectNewNodeLocation() throws SQLException{
        selectionMode = true; // when set to true, only the permitted points can be selected, but all are disabled in the next line
        canAllBeSelected(false);
        Button cancelButton = new Button("Cancel");
        Button confirmAddButton = new Button("Confirm");
        HBox bottomPane = new HBox(cancelButton, confirmAddButton);
        bottomPane.setPadding(new Insets(10,10,10,10));
        
        reminder.setText("Choose a new location for your teleportation point. ");
        pane1.setBottom(bottomPane);      
        // Click action to add a new node
        Pane pane = new Network();
        pane.setOnMouseClicked(e->{
            float x = (float)e.getX();
            float y = (float)e.getY();
            if (x>= r && x <= pane.getWidth()-r && y>= r && y <= pane.getHeight()-r) {
                boolean canBeAdded = true;
                
                for (SingleTeleportationPoint child : points) {
                    double distance = Math.pow(Math.pow((child.getLayoutX()-x), 2) + Math.pow((child.getLayoutY()-y), 2), 0.5);
                    if ( distance < r*2) {
                        canBeAdded = false;
                        break;
                    }
                }
                if (canBeAdded && pane.getChildren().contains(imaginaryCircle)) {
                    imaginaryCircle.setLayoutX(e.getX());
                    imaginaryCircle.setLayoutY(e.getY());
                } else if (canBeAdded) {
                    pane.getChildren().add(imaginaryCircle);
                    imaginaryCircle.setLayoutX(e.getX());
                    imaginaryCircle.setLayoutY(e.getY());
                }
            }
        });
        
        // Button css
        setButtonCSS(confirmAddButton);
        setButtonCSS(cancelButton);
        
        // Set on action
        confirmAddButton.setOnAction(e->{
            reminder.setText("Wow! Fill in the information to create it instantly!");
            pane1.setBottom(null); // remove the options button
            try {
                newPointInformationWindow();
            } catch (SQLException ex) {
                Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        cancelButton.setOnAction(e->{
            reminder.setText("");
            mainScene();
        });
        
        pane1.setCenter(pane);
    }
    
    private static class SingleTeleportationPoint extends Pane{
        ImagePattern image = new ImagePattern(defaultImage);
        Point point;
        boolean canSelect;
        Button goButton = new Button("GO!!!");
        Button noButton = new Button("Nope, just take a look"); 
        Circle circleFrame = new Circle(r);
        Circle filter = new Circle(r);
        ObjectProperty<ImagePattern> imagePatternProperty = new SimpleObjectProperty<>();
        public SingleTeleportationPoint(Point point) {
            this.point = point;
            // Bind the circle fill with the imageview
            circleFrame.fillProperty().bind(imagePatternProperty);
//            circleFrame.setFill(new ImagePattern(image));
            filter.setFill(Color.rgb(255, 0, 0, this.equals(currentlySelected)? 0.3 : 0));
            
            try {
                String filePath = database_item5.getImageFilePath(point.getNameOfTeleportationPoint());
                if (filePath != null) {
                    image = new ImagePattern(new Image(new FileInputStream(new File(filePath))));
                }imagePatternProperty.set(image);
            } catch (SQLException | FileNotFoundException ex) {
                Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            getChildren().addAll(circleFrame, filter);
            
            setOnMouseClicked ( e -> {
                if (selectionMode) {
                    if (canSelect) {
                        // if previously selected, unselect it
                        if (selected.contains(this)) {
                            filter.setFill(Color.TRANSPARENT); 
                            selected.remove(this);
                            isSelected = ! selected.isEmpty();
                        } else {
                            // if haven't selected yet, select it
                            filter.setFill(Color.rgb(0, 0,255, 0.3));
                            selected.add(this);
                            isSelected = true;
                        }    
                    }
                } else {
                    pane1.setRight(new VBox());
                    pane1.setBottom(new HBox());
                    if (currentlySelected != null) {
                        currentlySelected.filter.setFill(Color.TRANSPARENT); 
                        currentlySelected.circleFrame.setOpacity(1);
                    }
                    currentlySelected = this;
                    
                    filter.setFill(Color.rgb(255, 0, 0,0.3));
                    if (username.equals(getOwnerName())) { try {
                        // this node is owned by the current user
                        reviewNode();
                        } catch (SQLException ex) {
                            Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (currentPoint == null) { // user haven't create any nodes
                        reminder.setText("Wanna go to other points? Create/select one teleportation point & join them now!");
                        mainScene();
                    } else if (! getNeighbours().isEmpty()) {
                        reset();
                        ArrayList<Point> shortestPath = TeleportationNetworkController.shortestPath(currentPoint.getNodeName(), getNodeName());
                        if (shortestPath != null) {
                            if (currentPoint != null && currentPoint.getNodeName().equals(this.getNodeName())) {
                                reminder.setText("You are at this point already!");
                            } else {
                                String text = "The shortest path from your current point " + currentPoint  + " to " + this + " is " ;
                                for (int i = (shortestPath.size()-1); i>0; i--) {
                                    text += (shortestPath.get(i)+" -> ");
                                }
                                text += shortestPath.get(0) + " with length of " + String.format("%.2f", TeleportationNetworkController.shortestDistance(currentPoint.getNodeName(), getNodeName()))+ ". Do you want to go? ";
                                reminder.setText(text);
                                pane1.setTop(new HBox(backToMainPageButton, reminder));
                                HBox bottomPane = new HBox(goButton, noButton);
                                bottomPane.setPadding(new Insets(10,10,10,10));
                                pane1.setBottom(bottomPane);    
                            }
                        } else {
                            reminder.setText("There is no way to go from " + currentPoint + " to " + this);
                            mainScene();
                        }
                    } else {
                        reminder.setText("There is no way to go from " + currentPoint + " to " + this);
                        mainScene();
                    }
                }
                }
            );
            
            // Button css
            setButtonCSS(goButton);
            setButtonCSS(noButton);
            
            // Set on action
            goButton.setOnAction(e->{
                try {
                    goNewPoint();
                } catch (SQLException ex) {
                    Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            noButton.setOnAction(e->{
                reminder.setText("");
                mainScene();
            });
            
            Tooltip tooltip = new Tooltip(point.getNameOfTeleportationPoint());
            Tooltip.install(filter, tooltip);
        }
        
        public void goNewPoint() throws SQLException {
            currentPoint = this;
            reminder.setText("You are now at " + currentPoint + "! Wow!");
            database_item5.setCurrentPoint(username, this.getNodeName());
            mainScene();
        }
        
        private final Button addNeigbourButton = new Button("Add neighbour");
        public void reviewNode() throws SQLException{
            reminder.setText("");
            reset();
            ImageView notificatonImageView = new ImageView(new Image(getClass().getResourceAsStream("/minecraft/icon/redDot.jpg")));
            notificatonImageView.setFitHeight(20);
            notificatonImageView.setFitWidth(20);
            Label nameText = new Label("Name: " + getNodeName());
            Label neighbourText = new Label("Neighbour: " + getNeighbours().toString());
            nameText.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            neighbourText.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            Button removeNeigbourButton = new Button("Remove neighbour");
            Button friendRequestReceivedButton;
            Button friendRequestSentButton = new Button("Friend requests sent");
            friendRequestReceivedButton = new Button("Friend requests received", point.getFriendRequestsReceived()!= null && !point.getFriendRequestsReceived().isEmpty() ? notificatonImageView:null);
            Button updateImageButton = new Button("Update image of teleportation point ");
            Button goButton = new Button("GO!!!");
            Button backMainSceneButton = new Button("Review done!");
           
            
            VBox pane = new VBox(nameText, neighbourText,addNeigbourButton,removeNeigbourButton,goButton,friendRequestReceivedButton,friendRequestSentButton,updateImageButton, backMainSceneButton);

            updateImageButton.setDisable(false);
            pane1.setRight(pane);
            
            // Button css
            setButtonCSS(addNeigbourButton);
            setButtonCSS(removeNeigbourButton);
            setButtonCSS(friendRequestReceivedButton);
            setButtonCSS(friendRequestSentButton);
            setButtonCSS(updateImageButton);
            setButtonCSS(goButton);
            setButtonCSS(backMainSceneButton);
            
            // Set on action
            addNeigbourButton.setOnAction(e->{
                try {
                    neighbourSelection();
                } catch (SQLException ex) {
                    Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            removeNeigbourButton.setOnAction(e->{
                removeNeighbourSelection();
            });
            
            backMainSceneButton.setOnAction(e->{
                reminder.setText("");
                mainScene();
            });
            
            goButton.setOnAction(e->{
                try {
                    goNewPoint();
                } catch (SQLException ex) {
                    Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            FileChooser.ExtensionFilter filter1 = new FileChooser.ExtensionFilter("Image files", Arrays.asList(new String[]{"*.png","*.jpg","*.jpeg"}));
            //---------------------------------------------------------------------------
            updateImageButton.setOnAction(e->{
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose the image for your node.");
                fileChooser.setInitialDirectory(new File("C:"));
                fileChooser.getExtensionFilters().add(filter1);
                File selectedFile = fileChooser.showOpenDialog(null);
                
                if (selectedFile != null) {
                    String filePath = selectedFile.getAbsolutePath();
                    try {
                        database_item5.addImage(currentlySelected.getNodeName(), filePath);
                        image = new ImagePattern(new Image(new FileInputStream(new File(database_item5.getImageFilePath(currentlySelected.getNodeName())))));
                        currentlySelected.circleFrame.fillProperty().bind(imagePatternProperty);
                        reviewNode();
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            
            friendRequestReceivedButton.setOnAction(e->{
                pane1.setRight(new PaneReviewRequestReceived());
            });
            
            friendRequestSentButton.setOnAction(e->{
                pane1.setRight(new PaneReviewRequestSent());
            });
        }
        
        public class PaneReviewRequestReceived extends VBox{

            public PaneReviewRequestReceived(){
                
                ObservableList<String> receivedObservableValue = FXCollections.observableArrayList();
                receivedObservableValue.clear();
                for (int i = 0; i < point.getFriendRequestsReceived().size(); i++) {
                    receivedObservableValue.add(point.getFriendRequestsReceived().get(i));
                }
                ListView<String> waitingListView = new ListView<>();

                waitingListView.getItems().addAll(receivedObservableValue);
                waitingListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

                Button acceptButton = new Button("Accept");
                Button acceptAllButton = new Button("Accept ALL");
                Button rejectButton = new Button("Reject");
                Button rejectAllButton = new Button("Reject ALL");
                Button backButton = new Button("Back");
                // Bottom pane
                HBox bottomPane = new HBox(acceptButton, acceptAllButton, rejectButton, rejectAllButton, backButton);
                bottomPane.setPadding(new Insets(20,20,20,20));

                // Center pane
                ScrollPane centerPane = new ScrollPane();
                centerPane.setContent(waitingListView);
                centerPane.setStyle("-fx-background-color: transparent");
                
                
                // Button css
                setButtonCSS(acceptButton);
                setButtonCSS(acceptAllButton);
                setButtonCSS(rejectButton);
                setButtonCSS(rejectAllButton);
                setButtonCSS(backButton);
                
                // Set on action
                // Accept selected invitation
                acceptButton.disableProperty().bind(waitingListView.getSelectionModel().selectedItemProperty().isNull());
                acceptButton.setOnAction(e -> {
                    ArrayList<String> newNeighbourToAdd = new ArrayList<>(waitingListView.getSelectionModel().getSelectedItems());
                    
                    String text  = "Yeah! you have become neighbour with  ";
                    for (int i = 0; i < newNeighbourToAdd.size()-1;) {
                        String newNeigh = newNeighbourToAdd.get(0);
                        text += newNeigh + ", ";
                        waitingListView.getItems().remove(newNeigh);
                        try {
                            point.acceptFriendRequest(newNeigh);;
                        } catch (SQLException ex) {
                            System.out.println("Failed");
                        }
                    }
                    String newNeigh = newNeighbourToAdd.get(newNeighbourToAdd.size()-1);
                    text += (newNeighbourToAdd.size()>1 ? "and " : "" ) + newNeigh;
                    try {
                        point.acceptFriendRequest(newNeigh);
                    } catch (SQLException ex) {
                        Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    waitingListView.getItems().remove(newNeigh);
                    centerPane.setContent(waitingListView);
                    reminder.setText(text);
                    reset();
                    pane1.setRight(new PaneReviewRequestReceived());
                });

                // Accpet all invitation
                SimpleBooleanProperty isEmptyBooleanProperty = new SimpleBooleanProperty(receivedObservableValue.isEmpty());
                acceptAllButton.disableProperty().bind(isEmptyBooleanProperty);
                acceptAllButton.setOnAction(e -> {
                        ArrayList<String> requests = point.getFriendRequestsReceived();
                        String text  = "Yeah! you have become neighbour with  "; 
                        for (int i = 0; i < requests.size()-1; i++) {
                            String newNeigh = requests.get(i);
                            text += newNeigh + ", ";
                            try {
                                point.acceptFriendRequest(newNeigh);
                            } catch (SQLException ex) {
                                Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        String newNeigh = requests.get(requests.size()-1);
                        text += (requests.size()>1 ?  "and " : "") + newNeigh;
                        try {
                            point.acceptFriendRequest(newNeigh);
                        } catch (SQLException ex) {
                            Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        waitingListView.getItems().clear();
                        centerPane.setContent(waitingListView);
                        reminder.setText(text);
                        reset();
                        pane1.setRight(new PaneReviewRequestReceived());
                });

                // Reject selected invitation(s)
                rejectButton.disableProperty().bind(waitingListView.getSelectionModel().selectedItemProperty().isNull());
                rejectButton.setOnAction(e -> {
                    ArrayList<String> invitationToReject = new ArrayList<>(waitingListView.getSelectionModel().getSelectedItems());
                    String text  = "Opps! you have rejected  ";
                    for (int i = 0; i < invitationToReject.size()-1;) {
                        String newNeigh = invitationToReject.get(0);
                        text += newNeigh + ", ";
                        try {
                            point.rejectFriendRequest(newNeigh);
                        } catch (SQLException ex) {
                            Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        waitingListView.getItems().remove(newNeigh);
                    }
                    String newNeigh = invitationToReject.get(invitationToReject.size()-1);
                    text += (invitationToReject.size()>1 ? "and " : "" ) + newNeigh;
                    try {
                        point.rejectFriendRequest(newNeigh);
                    } catch (SQLException ex) {
                        Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    waitingListView.getItems().remove(newNeigh);
                    centerPane.setContent(waitingListView);
                    reminder.setText(text);
                    reset();
                    pane1.setRight(new PaneReviewRequestReceived());
                });

                // Reject all invitation
                rejectAllButton.disableProperty().bind(isEmptyBooleanProperty);
                rejectAllButton.setOnAction(e -> {
                        ArrayList<String> requests = point.getFriendRequestsReceived();
                        String text  = "Yeah! you have become neighbour with  "; 
                        for (int i = 0; i < requests.size()-1; i++) {
                            String newNeigh = requests.get(i);
                            text += newNeigh + ", ";
                            try {
                                point.rejectFriendRequest(newNeigh);
                            } catch (SQLException ex) {
                                Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        String newNeigh = requests.get(requests.size()-1);
                        text += (requests.size()>1 ?  "and " : "") + newNeigh;
                        try {
                            point.rejectFriendRequest(newNeigh);
                        } catch (SQLException ex) {
                            Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        waitingListView.getItems().clear();
                        centerPane.setContent(waitingListView);
                        reminder.setText(text);
                        reset();
                        pane1.setRight(new PaneReviewRequestReceived());
                });
                
                // Back to previous scene
                backButton.setOnAction(e->{try {
                    reviewNode();
                    } catch (SQLException ex) {
                        Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
});
                reminder.setText(receivedObservableValue.isEmpty()? "You dont have any friend request for now." : "Wow! Someone invited you to become neighbour! Do you want to join?");
                this.getChildren().addAll(centerPane, bottomPane);
            }
        }
        
        public class PaneReviewRequestSent extends VBox{

            public PaneReviewRequestSent() {
                
                ObservableList<String> sentObservableValue = FXCollections.observableArrayList();
                sentObservableValue.clear();
                ArrayList<String> sent = point.getFriendWaitingAcceptance();
                for (int i = 0; i < sent.size(); i++) {
                    sentObservableValue.add(sent.get(i));
                }
                ListView<String> waitingListView = new ListView<>();

                waitingListView.getItems().addAll(sentObservableValue);
                waitingListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

                Button backButton = new Button("Back");

                // Bottom pane
                HBox bottomPane = new HBox();
                bottomPane.getChildren().add(backButton);
                if (sent.isEmpty()) {
                    bottomPane.getChildren().add(addNeigbourButton);
                    reminder.setText("You haven't request for any neighbour. Do you want to invite others?");
                }else{
                    reminder.setText("Opps! Thay haven't accpet your request. Please wait patiently");
                }
                // Center pane
                ScrollPane centerPane = new ScrollPane();
                centerPane.setContent(waitingListView);
                centerPane.setStyle("-fx-background-color: transparent");
                
                
                // Button css
                setButtonCSS(backButton);
                
                // Back to previous scene
                backButton.setOnAction(e->{try {
                    reviewNode();
                    } catch (SQLException ex) {
                        Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
});
                
                this.getChildren().addAll(centerPane, bottomPane);
                
            }
        }
        
        public static int getIndex(String nodeName) {
            for (int i = 0; i < nodesName.length; i++) {
                if (nodesName[i].equals(nodeName)) {
                    return i;
                }
            }
            return -1;
        }

        public String getNodeName() {
            return point.getNameOfTeleportationPoint();
        }

        public ArrayList<String> getNeighbours() {
            return point.getNeighboursInString();
        }

        public String getOwnerName() {
            return point.getOwner();
        }

        @Override
        public String toString() {
            return point.getNameOfTeleportationPoint();
        }
        
    }
    
    private static class Network extends Pane{

        public Network() {
            canAllBeSelected(false);
            this.getChildren().clear();
            points.clear();
            imaginaryCircle.setFill(Color.rgb(230, 150, 0));
            
            // Add edges
            List<Edge> edges = TeleportationNetworkController.getEdges();
            for (Edge edge : edges) {
                Line line = new Line(edge.getN1().getX(), edge.getN1().getY(), edge.getN2().getX(), edge.getN2().getY());
                getChildren().add(line);
            }
            
            // Add nodes
            List<Point> nodesList = TeleportationNetworkController.getNodes();
            for (Point node : nodesList) {
                SingleTeleportationPoint point = new SingleTeleportationPoint(node);
                getChildren().add(point);
                point.setLayoutX(node.getX());
                point.setLayoutY(node.getY());
                points.add(point);
                if (currentPoint != null && point.getNodeName().equals(currentPoint.getNodeName())) {
                    point.filter.setFill(Color.rgb(0, 255, 0,0.5));
                    point.circleFrame.setOpacity(0.8);
                } else if (currentlySelected != null && point.getNodeName().equals(currentlySelected.getNodeName())) {
                    point.filter.setFill(Color.rgb(255,0, 0, 0.5));
                    point.canSelect  = false;
                    point.circleFrame.setOpacity(0.8);
                }
            }
            
            try {
                String currentPointName = database_item5.getCurrentPoint(username);
                for (SingleTeleportationPoint p : points) {
                    if (p.getNodeName().equals(currentPointName)) {
                        currentPoint = p;
                        p.filter.setFill(Color.rgb(0, 255, 0,0.5));
                        p.circleFrame.setOpacity(0.8);
                        break;
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(TeleportationNetworkController_GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    public static void reset() {
        pane1.setCenter(new Network());
        pane1.setTop(new HBox(backToMainPageButton, reminder));
        pane1.setBottom(new HBox());
        pane1.setRight(new VBox());
        selectionMode = false;
        isSelected = false;
        selected.clear();
    }
    
    public Label labelSetting(Label label) {
        label.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        label.setText(username);
        return label;
    }

}

