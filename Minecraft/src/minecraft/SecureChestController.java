package minecraft;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SecureChestController implements Initializable{
    @FXML
    private ScrollPane scrollPaneRequest;
    @FXML
    private AnchorPane anchorSecureChestOthers;
    @FXML
    private ScrollPane scrollPaneOtherChest;
    @FXML
    private ListView<String> authorisedUserList;
    @FXML
    private Label chestNameLabel1;
    @FXML
    private Label chestNameLabel2;
    @FXML
    private Button depositUser;
    @FXML
    private Button editAuthorisedUserButton;
    @FXML
    private Button editChestNameButton;
    @FXML
    private Button editSecurityLevelButton;
    @FXML
    private ImageView imageChestUser;
    @FXML
    private Label securityLevelLabel;
    @FXML
    private Label securityLevelLabel2;
    @FXML
    private Button sendRequestButton;
    @FXML
    private TableView<Request> tableRequestSent;
    @FXML
    private Button viewChestUser;
    @FXML
    private Button withdrawUser;
    @FXML
    private TableColumn<Request, String> chestNameColumn;
    @FXML
    private TableColumn<Request, String> requestStatusColumn;

    public static String username;
    public static SecureChest chest;
    private List<String> authorisedUser;
    private Map<SecureChest, Integer> otherChest;
    ObservableList<Request> requestSent = FXCollections.observableArrayList();
    public Map<String, String> requestSentMap;
    public ArrayList<SecureChest> chestNoAccess;
    public int selectedPermission;
    public String selectedUser;
    public SecureChest selectedChestRequest;

    public static class Request {
        private final SimpleStringProperty ChestName;
        private final SimpleStringProperty status;

        public Request(String ChestName, String status) {
            this.ChestName = new SimpleStringProperty(ChestName);
            this.status = new SimpleStringProperty(status);
        }

        public String getChestName() {
            return ChestName.get();
        }

        public SimpleStringProperty ChestNameProperty() {
            return ChestName;
        }

        public String getStatus() {
            return status.get();
        }

        public SimpleStringProperty statusProperty() {
            return status;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            chest = new SecureChest("defaultUser");
            otherChest = database_item7.retrieveChestWithAccess("defaultUser");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("initialised with database");
        viewChestUser.setOnAction(e->{
            handleViewChest(chest);
            System.out.println("working");
        });
        depositUser.setOnAction(e-> {
            try {
                handleDeposit(e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        withdrawUser.setOnAction(e->{
            try {
                handleWithdraw(e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        editChestNameButton.setOnAction(e-> {
            try {
                handleEditChestNameAction();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        editSecurityLevelButton.setOnAction(e->{
            try {
                handleEditSecurityLevel();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        sendRequestButton.setOnAction(e->{
            try {
                handleSendRequestButton();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        editAuthorisedUserButton.setOnAction(e-> {
            try {
                handleEditAuthorisedUser(selectedUser);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        try {
            display();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void display() throws SQLException{
        // if (chest.getSecurityLevel().equals("Public")||chest.getSecurityLevel().equals("Private"))
        editAuthorisedUserButton.setDisable(true);
        // else editAuthorisedUserButton.setDisable(false);
        chestNameLabel1.setText("Chest Name: " + chest.getName());
        chestNameLabel2.setText(chest.getName());
        securityLevelLabel.setText("Security Level: " + chest.getSecurityLevel());
        securityLevelLabel2.setText(chest.getSecurityLevel());
        imageChestUser.setImage(new Image(getClass()
                        .getResourceAsStream("/minecraft/icon/" + "SecureChest" + ".png")));
        displayRequestFromOther();
        editSecurityLevelButton.setVisible(true);
        if (chest.getSecurityLevel().equals("Private")){
            authorisedUserList.setVisible(false);
        }
        else{
            authorisedUserList.setVisible(true);
        }
        displayEditAuthorisedUser();
        displayOtherChest();
        displayRequestSent();
    }

    public void displayRequestFromOther() throws SQLException{
        VBox requestbox = new VBox(0);
        for (Map.Entry<String, Integer> entry : chest.getAccessRequests().entrySet()){
            HBox request = new HBox();
            Label requestor = new Label();
            // requestor.setStyle("-fx-text-fill: white");
            // requestor.getStyleClass().add("text-grid");
            requestor.getStyleClass().add("label-in-griditem");
            int width = 127, height= 40;
            requestor.setMinWidth(width);
            requestor.setMaxWidth(width);
            requestor.setPrefWidth(width);
            requestor.setMinHeight(height);
            requestor.setMaxHeight(height);
            requestor.setPrefHeight(height);
            
            Label permissionRequested = new Label();
            // permissionRequested.setStyle("-fx-text-fill: white");
            permissionRequested.getStyleClass().add("label-in-griditem");
            // permissionRequested.getStyleClass().add("text-grid");
            width = 212;
            height= 40;
            permissionRequested.setMinWidth(width);
            permissionRequested.setMaxWidth(width);
            permissionRequested.setPrefWidth(width);
            permissionRequested.setMinHeight(height);
            permissionRequested.setMaxHeight(height);
            permissionRequested.setPrefHeight(height);
            Label yeslabel = new Label("\u2713");
            yeslabel.setStyle("-fx-font-size: 15px;");
            yeslabel.getStyleClass().add("text-yesno");
            Button yesButton = new Button();
            yesButton.setGraphic(yeslabel);
            yesButton.getStyleClass().add("yesnobutton");
            Label noLabel = new Label("❌");
            noLabel.getStyleClass().add("text-yesno");
            Button noButton = new Button();
            noButton.setGraphic(noLabel);
            noButton.getStyleClass().add("yesnobutton");
            
            requestor.setText(entry.getKey());
            permissionRequested.setText("Requests for " + ((entry.getValue()==1)?"View Only":"Full Access"));
            request.getChildren().addAll(requestor, permissionRequested, yesButton, noButton);
            requestbox.getChildren().add(request);
            request.getStyleClass().add("griditem");
            yesButton.setOnAction(e->{
                try {
                    System.out.println("approved");
                    chest.approveRequest(entry.getKey(), entry.getValue());
                    display();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            });
            noButton.setOnAction(e->{
                try {
                    chest.rejectRequest(entry.getKey());
                    display();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            });
        }
        scrollPaneRequest.setContent(requestbox);
    }

    public void handleEditChestNameAction() throws SQLException{
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Edit Chest Name: ");
        String cssFilePath = getClass().getResource("minecraft-style.css").toExternalForm();
        dialog.getDialogPane().getStylesheets().add(cssFilePath);
        dialog.getDialogPane().getStyleClass().add("dialog-pane");

        ButtonType confirm = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirm, ButtonType.CANCEL);
        dialog.getDialogPane().lookupButton(confirm).getStyleClass().add("button");
        dialog.getDialogPane().lookupButton(ButtonType.CANCEL).getStyleClass().add("button");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        nameField.setPromptText("New Name");


        grid.add(new Label("New Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(nameField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirm) {
                return nameField.getText(); // Return null to indicate invalid input
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newName -> {
            dialog.close();
            String chestname = chest.getName();
            try {
                chest.editChestName(newName);
                otherChest.entrySet().forEach(e->
                                    System.out.println("Edit Chest Name then" + 
                                                        e.getKey() +  " " + e.getValue()));
                display();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Dialog<ButtonType> doneDialog = new Dialog<>();
            doneDialog.setTitle("Message");
            doneDialog.setContentText("Chest Name changed from " + chestname + " to " + newName);
            doneDialog.getDialogPane().getStylesheets().add(cssFilePath);
            doneDialog.getDialogPane().getStyleClass().add("dialog-pane");
        
            ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            doneDialog.getDialogPane().getButtonTypes().add(ok);
            doneDialog.getDialogPane().lookupButton(ok).getStyleClass().add("button");
            
            doneDialog.showAndWait();
        });
    }

    public void handleEditSecurityLevel() throws SQLException {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Chest Security Level: ");
        String cssFilePath = getClass().getResource("minecraft-style.css").toExternalForm();
        dialog.getDialogPane().getStylesheets().add(cssFilePath);
        dialog.getDialogPane().getStyleClass().add("dialog-pane");

        
        ButtonType confirm = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirm, ButtonType.CANCEL);
        dialog.getDialogPane().lookupButton(confirm).getStyleClass().add("button");
        dialog.getDialogPane().lookupButton(ButtonType.CANCEL).getStyleClass().add("button");

        MenuButton permissionMenuButton = new MenuButton("Select Security Level");
        permissionMenuButton.getStyleClass().add("menu-button");
        
        MenuItem publicItem = new MenuItem("Public");
        MenuItem privateItem = new MenuItem("Private");
        MenuItem selfDefinedItem = new MenuItem("Self-defined");

        permissionMenuButton.getItems().addAll(publicItem, privateItem, selfDefinedItem);
        publicItem.setOnAction(event -> permissionMenuButton.setText("Public"));
        privateItem.setOnAction(event -> permissionMenuButton.setText("Private"));
        selfDefinedItem.setOnAction(event -> permissionMenuButton.setText("Self-defined"));
        publicItem.getStyleClass().add("menu-item");
        privateItem.getStyleClass().add("menu-item");
        selfDefinedItem.getStyleClass().add("menu-item");
                        
        VBox content = new VBox();
        content.setSpacing(10);
        content.setPadding(new Insets(20, 150, 10, 10));
        content.getChildren().add(permissionMenuButton);

        dialog.getDialogPane().setContent(content);

        dialog.showAndWait().ifPresent(response -> {
            if (response == confirm) {
                String selectedPermission = permissionMenuButton.getText();
                try {
                    if (!selectedPermission.equals("Select Security Level")){
                        chest.editSecurityLevel(selectedPermission, chest.getOwner());
                        display();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void displayEditAuthorisedUser() throws SQLException {
        authorisedUser = chest.getAuthorizedPlayers();
        ObservableList<String> users = FXCollections.observableArrayList(authorisedUser);
        authorisedUserList.setItems(users);
        System.out.println("set the userlistView");
        editAuthorisedUserButton.setOnAction(e-> {
            try {
                handleEditAuthorisedUser(selectedUser);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
    
        // Add a selection listener to the list view
        authorisedUserList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedUser = newValue;
            System.out.println(selectedUser);
            if (selectedUser != null && chest.getSecurityLevel().equals("Self-defined")) {
                System.out.println("got run");
                editAuthorisedUserButton.setDisable(false);
                System.out.println("enabled the button");
            } else {
                System.out.println("wrong");
                editAuthorisedUserButton.setDisable(true);
            }
        });
    }
    

    public void handleEditAuthorisedUser(String selectedUser) throws SQLException{
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Edit Authorised User ");
            dialog.setHeaderText("Select Operation:");
            String cssFilePath = getClass().getResource("minecraft-style.css").toExternalForm();
            dialog.getDialogPane().getStylesheets().add(cssFilePath);
            dialog.getDialogPane().getStyleClass().add("dialog-pane");
            
            ButtonType removeButton = new ButtonType("Remove Current User", ButtonBar.ButtonData.OK_DONE);
            ButtonType editAccessButton = new ButtonType("Edit Access of Current User", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().clear();
            dialog.getDialogPane().getButtonTypes().addAll(removeButton, editAccessButton, ButtonType.CANCEL);
            dialog.getDialogPane().lookupButton(removeButton).getStyleClass().add("button");
            dialog.getDialogPane().lookupButton(editAccessButton).getStyleClass().add("button");
            dialog.getDialogPane().lookupButton(ButtonType.CANCEL).getStyleClass().add("button");
            

            dialog.showAndWait().ifPresent(response -> {
                dialog.close();
                if (response == removeButton) {
                    Dialog<ButtonType> dialog2 = new Dialog<>();
                    dialog2.setTitle("Remove Current User");
                    dialog2.setHeaderText("Confirm to remove " + selectedUser.split(" ")[0] +" from accessing your secure chest?");
                    dialog2.getDialogPane().getStylesheets().add(cssFilePath);
                    dialog2.getDialogPane().getStyleClass().add("dialog-pane");


                    ButtonType confirm = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
                    // dialog2.getDialogPane().getButtonTypes().clear();
                    dialog2.getDialogPane().getButtonTypes().addAll(confirm, ButtonType.CANCEL);
                    dialog2.getDialogPane().lookupButton(confirm).getStyleClass().add("button");
                    dialog2.getDialogPane().lookupButton(ButtonType.CANCEL).getStyleClass().add("button");
                    
                    dialog2.showAndWait().ifPresent(response2->{
                        if (response2==confirm){
                            try {
                                chest.removeAuthorisedUser(selectedUser.split(" ")[0]);
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
                }
                else if (response == editAccessButton){
                    Dialog<ButtonType> dialog3 = new Dialog<>();
                    System.out.println(selectedUser);
                    // chest.getAccessPermissions().entrySet().forEach(e-> System.out.println(String.valueOf(e.getKey() + " "+ e.getValue())));
                    // System.out.println(chest.getAccessPermissions().get(editAccessButton));
                    dialog3.setContentText("Permission Type for Current User: " + (chest.getAccessPermissions().get(selectedUser.split(" ")[0])==1?"View Only":"Full Access"));
                    dialog3.setHeaderText("Select the Permission Type for " + selectedUser.split(" ")[0]);
                    dialog3.getDialogPane().getStylesheets().add(cssFilePath);
                    dialog3.getDialogPane().getStyleClass().add("dialog-pane");

                    ButtonType confirm = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
                    // dialog3.getDialogPane().getButtonTypes().clear();
                    dialog3.getDialogPane().getButtonTypes().addAll(confirm, ButtonType.CANCEL);
                    // Apply styles to the actual buttons
                    dialog3.getDialogPane().lookupButton(confirm).getStyleClass().add("button");
                    dialog3.getDialogPane().lookupButton(ButtonType.CANCEL).getStyleClass().add("button");

                    MenuButton permissionMenuButton = new MenuButton("Select Permission Type");
                    permissionMenuButton.getStyleClass().add("menu-button");

                    MenuItem no = new MenuItem("No Access");
                    MenuItem view = new MenuItem("View Only");
                    MenuItem full = new MenuItem("Full Access");
                    no.getStyleClass().add("menu-item");
                    view.getStyleClass().add("menu-item");
                    full.getStyleClass().add("menu-item");
                    
                    permissionMenuButton.getItems().addAll(no, view, full);

                    no.setOnAction(event -> permissionMenuButton.setText("No Access"));
                    view.setOnAction(event -> permissionMenuButton.setText("View Only"));
                    full.setOnAction(event -> permissionMenuButton.setText("Full Access"));
                    
                    VBox content = new VBox();
                    content.getChildren().add(permissionMenuButton);

                    dialog3.getDialogPane().setContent(content);

                    dialog3.showAndWait().ifPresent(response2 -> {
                        if (response2 == confirm) {
                            String selectedPermission = permissionMenuButton.getText();
                            switch (selectedPermission) {
                                case "No Access":
                                    try {
                                        chest.removeAuthorisedUser(selectedUser.split(" ")[0]);
                                    } catch (SQLException e1) {
                                        e1.printStackTrace();
                                    }
                                    break;
                                case "View Only":
                                    try {
                                        chest.editAccess(selectedUser.split(" ")[0], 1);
                                    } catch (SQLException e1) {
                                        e1.printStackTrace();
                                    }
                                    break;
                                case "Full Access":
                                    try {
                                        chest.editAccess(selectedUser.split(" ")[0], 2);
                                        database_item7.updateChestPermission("defaultUser", username, 2);
                                    } catch (SQLException e1) {
                                        e1.printStackTrace();
                                    }    
                                    break;
                            }
                        }
                    });
                }
            });
            authorisedUserList.refresh();
            display();
            editAuthorisedUserButton.setDisable(true);
    }

    public void displayRequestSent() throws SQLException{
        requestSent.clear();
        requestSentMap = database_item7.retrieveRequestSent("defaultUser");
        for (Map.Entry<String, String> entry : requestSentMap.entrySet()){
            System.out.println(entry.getKey() + " " + entry.getValue());
            requestSent.add(new Request(entry.getKey(), entry.getValue()));
        }
        chestNameColumn.setCellValueFactory(new PropertyValueFactory<>("ChestName"));
        requestStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        // cartTable.getColumns().add(cropNameColumn);
        // cartTable.getColumns().add(quantityColumn);
        tableRequestSent.setItems(requestSent);
        tableRequestSent.refresh();
    }

    public void handleSendRequestButton() throws SQLException{
        Dialog<SecureChest> dialog = new Dialog<>();
        dialog.setTitle("Send Request");
        dialog.setHeaderText("Select a secure chest to send request: ");
        String cssFilePath = getClass().getResource("minecraft-style.css").toExternalForm();
        dialog.getDialogPane().getStylesheets().add(cssFilePath);
        dialog.getDialogPane().getStyleClass().add("dialog-pane");

        chestNoAccess = database_item7.retrieveChestNoAccess("defaultUser");
        ObservableList<SecureChest> chestList = FXCollections.observableArrayList(chestNoAccess);
        ListView<SecureChest> chestListView = new ListView<>();
        chestListView.setItems(chestList);
        // chestListView.setFixedCellSize(30); // Set this to the height of your ListView cells
        chestListView.setPrefHeight(30*chestListView.getItems().size()+10*chestListView.getItems().size());
        // SimpleDoubleProperty itemsProperty = new SimpleDoubleProperty(chestListView.getItems().size());
        // chestListView.prefHeightProperty().bind(Bindings.createDoubleBinding(() ->
        // chestListView.getFixedCellSize() * Math.min(10, chestListView.getItems().size()) + 2, chestListView.fixedCellSizeProperty(), chestListView.getItems()));
        // The prefHeightProperty of the ListView is then bound to the product of fixedCellSize and the minimum of maxItems and the current number of items (listView.getItems().size()). The 1.05 added to the calculation accounts for padding or borders around the cells.
        // The setFixedCellSize method is used to specify the fixed height of each cell. This value needs to be the height of your ListView cells.
        // The "maximum items" in the context of setting the size of a ListView to fit its content refers to the upper limit on the number of items that can be displayed without requiring scrolling. If the number of items exceeds this limit, the ListView will show a scroll bar. 
        
        ButtonType okButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
        dialog.getDialogPane().lookupButton(okButtonType).getStyleClass().add("button");
        dialog.getDialogPane().lookupButton(ButtonType.CANCEL).getStyleClass().add("button");

        // dialog.getDialogPane().lookupButton(okButtonType).setDisable(selectedForRequest==null);
        
        dialog.getDialogPane().lookupButton(okButtonType).setDisable(true);
        dialog.getDialogPane().setContent(chestListView);
       
        Platform.runLater(chestListView::requestFocus);

        chestListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedChestRequest = newValue;
            if (selectedChestRequest != null ) {
                System.out.println("got run");
                dialog.getDialogPane().lookupButton(okButtonType).setDisable(false);
                System.out.println("enabled the button");
            } 
            else {
                System.out.println("wrong");
                dialog.getDialogPane().lookupButton(okButtonType).setDisable(true);
            }
        });
        // Handle the result of the dialog
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return selectedChestRequest;
            }
            System.out.println("weird");
            return null;
        });

        dialog.showAndWait().ifPresent(selectedForRequest -> {
            // Handle the selected chest here if needed
            // For example, show a new dialog with details of the selected chest
            HashMap<SecureChest, Integer> requestSentOredi = new HashMap<SecureChest, Integer>();
            try {
                requestSentOredi = database_item7.retrieveAccessRequestOther("defaultUser");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            Optional<SecureChest> found = requestSentOredi.entrySet().stream().filter(entry->entry.getKey().getOwner()
                            .equals(selectedForRequest.getOwner())).map(entry->entry.getKey()).findFirst();
                
            if (found.isPresent()){
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Message");
                alert.setContentText("You have already sent request to this chest");
                alert.getDialogPane().getStylesheets().add(cssFilePath);
                alert.getDialogPane().getStyleClass().add("dialog-pane");
                alert.showAndWait();
            }
            else{
                Dialog<Integer> dialog1 = new Dialog<>();
                dialog1.setTitle("Choose Permission Type");
                dialog1.setHeaderText("Select Permission Type to Request: ");
                dialog1.getDialogPane().getStylesheets().add(cssFilePath);
                dialog1.getDialogPane().getStyleClass().add("dialog-pane");

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);

                Button viewOnly = new Button("View Only");
                Button fullAccess = new Button("Full Access");
                viewOnly.getStyleClass().add("button");
                fullAccess.getStyleClass().add("button");

                grid.add(viewOnly, 0,0);
                grid.add(fullAccess, 1, 0);

                ButtonType confirm = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
                // dialog1.getDialogPane().getButtonTypes().clear();
                dialog1.getDialogPane().getButtonTypes().addAll(confirm, ButtonType.CANCEL);

                dialog.getDialogPane().lookupButton(okButtonType).getStyleClass().add("button");
                dialog.getDialogPane().lookupButton(ButtonType.CANCEL).getStyleClass().add("button");
                
                dialog1.getDialogPane().setContent(grid);

                // Get the confirm button to control its state
                // Button confirmButton = (Button) dialog1.getDialogPane().lookupButton(confirm);
                // confirmButton.setDisable(true); // Initially disable the confirm button

                viewOnly.getStyleClass().add("button");;
                fullAccess.getStyleClass().add("button");;
                        
                // Create event handlers for the buttons
                EventHandler<ActionEvent> viewOnlyHandler = event -> {
                    // Reset the style of the fullAccess button
                    fullAccess.getStyleClass().add("button");;
                    // Set the style of the viewOnly button
                    viewOnly.setStyle("-fx-background-color: #6c2929;");
                    selectedPermission = 1; // Set the selected permission
                };

                EventHandler<ActionEvent> fullAccessHandler = event -> {
                    // Reset the style of the viewOnly button
                    viewOnly.getStyleClass().add("button");;
                    // Set the style of the fullAccess button
                    fullAccess.setStyle("-fx-background-color: #6c2929");
                    selectedPermission = 2; // Set the selected permission
                };

                // Add the event handlers to the buttons
                viewOnly.setOnAction(viewOnlyHandler);
                fullAccess.setOnAction(fullAccessHandler);
                dialog1.setResultConverter(dialogButton1->{
                    if (dialogButton1==confirm) return selectedPermission;
                    else return null;
                });

                // Show the dialog and wait for user input
                dialog1.showAndWait().ifPresent(result1 -> {
                    // if (result1 == confirm) {
                        try {
                            selectedForRequest.acceptRequest(chest.getOwner(), selectedPermission);
                            display();
                            Alert alert1 = new Alert(AlertType.INFORMATION);
                            alert1.setTitle("Message");
                            alert1.setContentText("Request Sent to "+ selectedForRequest.getName());
                            alert1.getDialogPane().getStylesheets().add(cssFilePath);
                            alert1.getDialogPane().getStyleClass().add("dialog-pane");
                            alert1.showAndWait();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    // } else {
                    //     System.out.println("Permission request canceled.");
                    // }
                });
            }
        });
    }

    public void displayOtherChest() throws SQLException{
        GridPane gridOtherChest = new GridPane();
        int rowCount=0, colCount =0;
        if (!otherChest.isEmpty()||otherChest!=null){
            for (Map.Entry<SecureChest, Integer> entry: otherChest.entrySet()) {
                VBox itemVBox = new VBox(5);
                ImageView imageView = new ImageView(new Image(getClass()
                                                .getResourceAsStream("/minecraft/icon/" + 
                                                "SecureChest" + ".png")));
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);
                Button button = new Button();
                button.setGraphic(imageView);
                button.setTooltip(new Tooltip(entry.getKey().getName() + "\nOwner: " + 
                                            entry.getKey().getOwner() ));
                button.setMaxSize(120, 120);
                button.setMinSize(120,120);
                button.setPrefSize(120,120);
                button.setStyle("-fx-background-color: transparent;");
                itemVBox.getStyleClass().add("griditem");
                Label itemNamelabel = new Label("Chest Name: ");
                Label itemName = new Label(entry.getKey().getName());
                itemNamelabel.getStyleClass().add("label-in-griditem");
                itemName.getStyleClass().add("label-in-griditem");
                
                itemVBox.getChildren().addAll(button, itemNamelabel, itemName);
    
                gridOtherChest.add(itemVBox, colCount, rowCount);
                // gridItemBox.add(itemVBox, colCount, gridItemBox.getRowCount());
        
                scrollPaneOtherChest.setContent(gridOtherChest);
                // Increment column count
                colCount++;
    
                if(colCount==3){
                    rowCount++;
                    colCount=0;
                }
                button.setOnAction(e->{
                    try {
                        showOtherChestMainPage(e, entry.getKey());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                });
            }
        }
    }

    void handleViewChest(SecureChest chest) {
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

        for (Map.Entry<EnderBackpackItem, Integer> item: chest.getItemChest().entrySet()){
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

                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                imageView.setPreserveRatio(true);
                button.setTooltip(new Tooltip(item.getKey().getName() + "\nType: " + 
                                                item.getKey().getType() + "\nFunction: " + 
                                                item.getKey().getFunction() + "\nQuantity: " + 
                                                item.getValue()));
                button.setStyle("-fx-background-color: transparent;");
                box.getStyleClass().add("griditem");
                Label quantitylabel = new Label(String.valueOf(item.getValue()));
                quantitylabel.getStyleClass().add("label-in-griditem");
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
        // dialogPane.getButtonTypes().clear();
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().lookupButton(ButtonType.OK).getStyleClass().add("button");
        dialog.getDialogPane().lookupButton(ButtonType.CANCEL).getStyleClass().add("button");

        // Show the dialog
        dialog.showAndWait();
    }

    public void showOtherChestMainPage(ActionEvent event, SecureChest otherChestToShow) throws IOException{
        secureChestOther.chestToShow = otherChestToShow;
        secureChestOther.username = "defaultUser";
        Parent root = FXMLLoader.load(getClass().getResource("secureChestOther.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
        Stage stage = (Stage) viewChestUser.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle(otherChestToShow.getOwner() + "\'s Chest");
        stage.setOnCloseRequest(e->{
            otherChest.entrySet().stream().filter(entry->entry.getKey().equals(otherChestToShow))
                      .map(entry -> entry.getKey()).findFirst().ifPresent(chestToUpdate ->{
            chestToUpdate = secureChestOther.chestToShow;
            });
            try {
                Parent root1 = FXMLLoader.load(getClass().getResource("SecureChest.fxml"));
                Scene scene1 = new Scene(root1);
                scene1.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
                Stage stage1= new Stage();
                stage1.setTitle("Minecraft");
                stage1.setScene(scene1);
                Image icon1 = new Image(getClass().getResourceAsStream("/minecraft/icon/SecureChest.png"));
                stage.getIcons().clear();
                stage.getIcons().add(icon1);
                stage1.show();
                stage.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    @FXML
    void handleDeposit(ActionEvent event) throws IOException {
        depositChest.chest = chest;
        depositChest.username = "defaultUser";
        Parent root = FXMLLoader.load(getClass().getResource("depositChest.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) viewChestUser.getScene().getWindow();
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
        stage.setTitle("Deposit Item To Your Secure Chest");
        stage.setOnCloseRequest(e->{
            chest = depositChest.chest;
            try {
                Parent root1 = FXMLLoader.load(getClass().getResource("SecureChest.fxml"));
                Scene scene1 = new Scene(root1);
                scene1.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
                Stage stage1= new Stage();
                stage1.setTitle("Minecraft");
                stage1.setScene(scene1);
                Image icon1 = new Image(getClass().getResourceAsStream("/minecraft/icon/SecureChest.png"));
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
    void handleWithdraw(ActionEvent event) throws IOException {
        withdrawChest.chest = chest;
        withdrawChest.username = "defaultUser";
        Parent root = FXMLLoader.load(getClass().getResource("withdrawChest.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) viewChestUser.getScene().getWindow();
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
        stage.setTitle("Withdraw Item From Your Secure Chest");
        stage.setOnCloseRequest(e->{
            chest = withdrawChest.chest;
            try {
                Parent root1 = FXMLLoader.load(getClass().getResource("SecureChest.fxml"));
                Scene scene1 = new Scene(root1);
                scene1.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
                Stage stage1= new Stage();
                stage1.setTitle("Minecraft");
                stage1.setScene(scene1);
                Image icon1 = new Image(getClass().getResourceAsStream("/minecraft/icon/SecureChest.png"));
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
}
