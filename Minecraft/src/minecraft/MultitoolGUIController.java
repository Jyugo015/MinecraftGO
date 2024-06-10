/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package minecraft;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class MultitoolGUIController implements Initializable {

    @FXML
    private ListView<Tool> toolList;
    @FXML
    private ListView<Tool> multiToolList;
    @FXML
    private Label sizeLabel;

    private MultipleTool multipleTools;

    private List<Tool> toolslist = new ArrayList<Tool>();

    public static String username;

    @FXML
    private Button addButton;
    @FXML
    private Button removeButton;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        try {
            multipleTools = new MultipleTool(username);
            toolslist = database_item2.retrieveTool(username);
            toolslist.forEach(e->System.out.println(e));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (toolslist!=null){
            ObservableList<Tool> tools = FXCollections.observableArrayList(toolslist);
            toolList.setItems(tools);
        }
        // 初始化 multiToolList
        try {
            updateToolListView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateMultiToolListView();
    }

    private void updateToolListView() throws SQLException {
        if (toolslist!=null){
            ObservableList<Tool> observableTools = FXCollections.observableArrayList(toolslist);
            toolList.setItems(observableTools);
            toolList.refresh();
        }
    }

    private void updateMultiToolListView() {
        if (multipleTools.getAllTools()!=null){
            multiToolList.setItems(FXCollections.observableArrayList(multipleTools.getAllTools()));
            toolList.refresh();
            updateSizeLabel();
        }
    }

    @FXML
    private void handleAddToMultiTool() throws SQLException {
        Tool selectedTool = toolList.getSelectionModel().getSelectedItem();
        if (selectedTool != null) {
            database_item2.addMultiTool(username, selectedTool.getName(), selectedTool.getType(),
                                   selectedTool.getFunction(), selectedTool.getGrade());
            database_item2.removeTool( selectedTool.getName(), selectedTool.getGrade(), selectedTool.getToolId(), username);
            multipleTools.addTool(new Tool(selectedTool, database_item2.retrieveLastToolID(username)));            
            toolslist.remove(selectedTool);
            updateMultiToolListView();
            updateToolListView();
        }
    }

    @FXML
    private void handleRemoveFromMultiTool() throws SQLException {
        Tool selectedTool = multiToolList.getSelectionModel().getSelectedItem();
        if (selectedTool != null) {
            multipleTools.removeTool(selectedTool);
            toolslist.add(selectedTool);
            database_item2.addTool(username, selectedTool.getName(), selectedTool.getType(), selectedTool.getFunction(), selectedTool.getGrade());
            database_item2.removeMultiTool(selectedTool.getName(), selectedTool.getGrade(), selectedTool.getToolId(), username);
            updateMultiToolListView();
            updateToolListView();
        }
    }

    @FXML
    private void handleClearList() throws SQLException {
        List<Tool> multipletoollist = multipleTools.getAllTools();
        if (multipletoollist!= null){
            for (Tool tool : multipletoollist){
                database_itemBox.addItem(username, tool.getName(), 1);
                toolslist.add(tool);
            }
            multipleTools.clear();
            database_item2.clearMultiTool(username);
            updateMultiToolListView();
            updateToolListView();
        }
    }

    private void updateSizeLabel() {
        sizeLabel.setText("Size: " + multiToolList.getItems().size());
    }

    @FXML
    private void handleUpgradeTool(ActionEvent event) throws SQLException {
        Tool selectedTool = multiToolList.getSelectionModel().getSelectedItem();
        if (selectedTool != null) {
            try {
                selectedTool.setGrade(selectedTool.getGrade() + 1); 
                database_item2.upgradeTool(username, selectedTool.getName(), selectedTool.getToolId());
                updateToolListView();  
                updateMultiToolListView(); 
            } catch (IllegalStateException e) {
                showErrorDialog("Error", "Upgrade failed: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleDowngradeTool(ActionEvent event) throws SQLException {
        Tool selectedTool = multiToolList.getSelectionModel().getSelectedItem();
        if (selectedTool != null&&selectedTool.getGrade()==1){
            showErrorDialog("Warning", 
            "You cannot further downgrade a multitool with grade equals to 1.");
        }
        if (selectedTool != null && selectedTool.getGrade()!=1) {
            try {
                selectedTool.setGrade(selectedTool.getGrade() - 1); 
                database_item2.downgradeTool(username, selectedTool.getName(), selectedTool.getToolId());
                updateToolListView();  
                updateMultiToolListView(); 
            } catch (IllegalStateException e) {
                showErrorDialog("Error", "Downgrade failed: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleSwitchTool(ActionEvent event) {
        Tool currentTool = multiToolList.getSelectionModel().getSelectedItem();
        if (currentTool != null) {
            try {
                Tool nextTool = multipleTools.switchToolDown(currentTool);
                multiToolList.getSelectionModel().select(nextTool);
                multiToolList.scrollTo(nextTool);  
            } catch (IllegalStateException e) {
                showErrorDialog("Error", "Switching tool failed: " + e.getMessage());
            }
        }
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        String cssFilePath = getClass().getResource("minecraft-style.css").toExternalForm();
        alert.getDialogPane().getStylesheets().add(cssFilePath);
        alert.getDialogPane().getStyleClass().add("dialog-pane");
        alert.showAndWait();
    }

    @FXML
    private void handleUseTool(ActionEvent event) {
        if (multipleTools.getAllTools()!=null&&!multipleTools.getAllTools().isEmpty()){
            MultiToolScene2.username = username;
            MultiToolScene2 scene2 = new MultiToolScene2();        
            try {
                scene2.start((Stage) ((Button) event.getSource()).getScene().getWindow());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
