/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package minecraft;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import minecraft.CreatureEncyclopedia.Creature;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class CreatureEncyclopediaController implements Initializable {

    @FXML
    private TextField searchField;
    @FXML
    private ListView<String> creatureListView;
    @FXML
    private TextArea creatureDetails;

    private CreatureEncyclopedia encyclopedia;

    // public CreatureEncyclopediaController() throws SQLException {
    //     this.encyclopedia = new CreatureEncyclopedia();
    // }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.encyclopedia = new CreatureEncyclopedia();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateCreatureListView();
    }
    String[] categories = {"Undead Mobs", "Passive Mobs", "Neutral Mobs", "Arthropods", "Illagers"};

    @FXML
    private void handleSearch(ActionEvent event) {
        String searchTerm = searchField.getText().toLowerCase();
        ObservableList<String> results = FXCollections.observableArrayList();
        for (String creatureName : encyclopedia.encyclopedia.keySet()) {
            Creature creatureInfo = encyclopedia.getCreatureInfo(creatureName);
            if (creatureName.toLowerCase().contains(searchTerm)
                    || creatureInfo.getType().toLowerCase().contains(searchTerm)
                    || creatureInfo.getSpecies().toLowerCase().contains(searchTerm)
                    || creatureInfo.getBehavior().toLowerCase().contains(searchTerm)
                    || creatureInfo.getHabitat().toLowerCase().contains(searchTerm)
                    || creatureInfo.getDrops().toLowerCase().contains(searchTerm)
                    || creatureInfo.getWeakness().toLowerCase().contains(searchTerm)
                    || creatureInfo.getCategory().toLowerCase().contains(searchTerm)) {
                results.add(creatureName);
            }
        }
        creatureListView.setItems(results);
    }

    @FXML
    private void handleFilter(ActionEvent event) {
        List<String> types = Arrays.asList("Undead Mobs","Flying mobs", "Passive Mobs", "Neutral Mobs", "Birds", "Mammals","Aquatic Mobs","Hostile Mobs");
        ChoiceDialog<String> dialog = new ChoiceDialog<>(types.get(0), types);
        dialog.setTitle("Filter by Type");
        dialog.setHeaderText("Select the type to filter by:");
        dialog.setContentText("Type:");
        String cssFilePath = getClass().getResource("minecraft-style.css").toExternalForm();
        dialog.getDialogPane().getStylesheets().add(cssFilePath);
        dialog.getDialogPane().getStyleClass().add("dialog-pane");

        // Apply styles to the actual buttons
        dialog.getDialogPane().lookupButton(ButtonType.OK).getStyleClass().add("button");
        dialog.getDialogPane().lookupButton(ButtonType.CANCEL).getStyleClass().add("button");

        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent()) {
            return;
        }

        String type = result.get();
        List<Creature> filteredCreatures = encyclopedia.filterByType(type);
        ObservableList<String> filteredList = FXCollections.observableArrayList();
        for (Creature creature : filteredCreatures) {
            filteredList.add(creature.getSpecies());
        }
        creatureListView.setItems(filteredList);
    }

    @FXML
    private void handleSortAZ(ActionEvent event) {
        List<Creature> sortedCreatures = encyclopedia.sortCreaturesAZ();
        updateCreatureListView(sortedCreatures);
    }

    @FXML
    private void handleSortZA(ActionEvent event) {
        List<Creature> sortedCreatures = encyclopedia.sortCreaturesZA();
        updateCreatureListView(sortedCreatures);
    }

    private void updateCreatureListView() {
        updateCreatureListView(encyclopedia.encyclopedia.values().stream().toList());
    }

    private void updateCreatureListView(List<Creature> creatures) {
        ObservableList<String> creatureNames = FXCollections.observableArrayList();
        for (Creature creature : creatures) {
            creatureNames.add(creature.getSpecies());
        }
        creatureListView.setItems(creatureNames);
    }

    @FXML
    private void handleCreatureSelection() {
        String selectedCreature = creatureListView.getSelectionModel().getSelectedItem();
        if (selectedCreature != null) {
            Creature creature = encyclopedia.getCreatureInfo(selectedCreature);
            StringBuilder details = new StringBuilder();
            details.append("Type: ").append(creature.getType()).append("\n");
            details.append("Species: ").append(creature.getSpecies()).append("\n");
            details.append("Behavior: ").append(creature.getBehavior()).append("\n");
            details.append("Habitat: ").append(creature.getHabitat()).append("\n");
            details.append("Drops: ").append(creature.getDrops()).append("\n");
            details.append("Weakness: ").append(creature.getWeakness()).append("\n");
            details.append("Category: ").append(creature.getCategory()).append("\n");
            details.append("Community Contributions: ").append(String.join(", ", creature.getCommunityContributions())).append("\n");
            creatureDetails.setText(details.toString());
        }
    }

    @FXML
    private void handleAddNote() throws SQLException {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Note");
        dialog.setHeaderText("Enter the name of the creature you want to add a note to:");
        String cssFilePath = getClass().getResource("minecraft-style.css").toExternalForm();
        dialog.getDialogPane().getStylesheets().add(cssFilePath);
        dialog.getDialogPane().getStyleClass().add("dialog-pane");

        ButtonType confirm = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirm, ButtonType.CANCEL);
        // Apply styles to the actual buttons
        dialog.getDialogPane().lookupButton(confirm).getStyleClass().add("button");
        dialog.getDialogPane().lookupButton(ButtonType.CANCEL).getStyleClass().add("button");
        MenuButton nameMenuButton = new MenuButton("Select Creature Name:");
        nameMenuButton.getStyleClass().add("menu-button");
        MenuItem Skeletons = new MenuItem("Skeletons");
        MenuItem Drowned = new MenuItem("Drowned");
        MenuItem Husks = new MenuItem("Husks");
        MenuItem SkeletonHorses = new MenuItem("Skeleton horses");
        MenuItem Withers = new MenuItem("Withers");
        MenuItem WitherSkeletons = new MenuItem("Wither Skeletons");
        MenuItem ZombieVillagers = new MenuItem("Zombie villagers");
        MenuItem Phantoms = new MenuItem("Phantoms");
        MenuItem Strays = new MenuItem("Strays");
        MenuItem ZombiePiglins = new MenuItem("Zombie Piglins");
        MenuItem Zoglins = new MenuItem("Zoglins");
        MenuItem Chickens = new MenuItem("Chickens");
        MenuItem Cows = new MenuItem("Cows");
        MenuItem Horses = new MenuItem("Horses");
        MenuItem Mooshrooms = new MenuItem("Mooshrooms");
        MenuItem Rabbits = new MenuItem("Rabbits");
        MenuItem Sheep = new MenuItem("Sheep");
        MenuItem Wolves = new MenuItem("Wolves");
        MenuItem Bats = new MenuItem("Bats");
        MenuItem Axolotls = new MenuItem("Axolotls");
        MenuItem Dolphins = new MenuItem("Dolphins");
        MenuItem Squids = new MenuItem("Squids");
        MenuItem GlowSquid = new MenuItem("Glow squid");
        MenuItem Guardians = new MenuItem("Guardians");
        MenuItem ElderGuardians = new MenuItem("Elder guardians");
        MenuItem Turtles = new MenuItem("Turtles");
        MenuItem Cod = new MenuItem("Cod");
        MenuItem Bees = new MenuItem("Bees");
        MenuItem CaveSpiders = new MenuItem("Cave spiders");
        MenuItem Endermites = new MenuItem("Endermites");
        MenuItem Silverfish = new MenuItem("Silverfish");
        MenuItem Spiders = new MenuItem("Spiders");
        MenuItem Pillagers = new MenuItem("Pillagers");
        MenuItem Illusioners = new MenuItem("Illusioners");
        MenuItem Ravagers = new MenuItem("Ravagers");
        MenuItem Evokers = new MenuItem("Evokers");
        MenuItem Vindicators = new MenuItem("Vindicators");
        MenuItem Villagers = new MenuItem("Villagers");
        MenuItem WanderingTraders = new MenuItem("Wandering Traders");
        MenuItem IronGolems = new MenuItem("Iron Golems");
        MenuItem Witches = new MenuItem("Witches");
        MenuItem Vexes = new MenuItem("Vexes");
        List<MenuItem> items = Arrays.asList(Skeletons, Drowned, Husks, SkeletonHorses, Withers,
                WitherSkeletons, ZombieVillagers, Phantoms, Strays,
                ZombiePiglins, Zoglins, Chickens, Cows, Horses, Mooshrooms,
                Rabbits, Sheep, Wolves, Bats, Axolotls, Dolphins, Squids,
                GlowSquid, Guardians, ElderGuardians, Turtles, Cod, Bees,
                CaveSpiders, Endermites, Silverfish, Spiders, Pillagers,
                Illusioners, Ravagers, Evokers, Vindicators, Villagers,
                WanderingTraders, IronGolems, Witches, Vexes);
        // items.forEach(e->setMenuItemStyle(e));
        items.forEach(e -> e.getStyleClass().add("menuitem"));
        nameMenuButton.getItems().addAll(items);
        items.forEach(e -> e.setOnAction(e1 -> nameMenuButton.setText(e.getText())));
        // String creatureName = dialog.showAndWait().orElse(null);
        // if (creatureName == null || creatureName.isEmpty() || !encyclopedia.encyclopedia.containsKey(creatureName)) {
        //     showAlert("Creature not found", "The creature " + creatureName + " does not exist in the encyclopedia.");
        //     return;
        // }

        HBox content = new HBox();
        content.setSpacing(10);
        content.setPadding(new Insets(20, 150, 10, 10));
        content.getChildren().add(nameMenuButton);

        dialog.getDialogPane().setContent(content);

        dialog.showAndWait().ifPresent(response -> {
            if (response == confirm) {
                handleAddNoteDialog(response, nameMenuButton);
            }
        });
    }

    private void handleAddNoteDialog(ButtonType response, MenuButton nameMenuButton) {
        String creatureName = nameMenuButton.getText();
        if (creatureName.equals("Select Creature Name:")) {
            showAlert("Error", "Please Select Creature Name You Want To Add Note To");
        } else {
            TextInputDialog noteDialog = new TextInputDialog();
            noteDialog.setTitle("Add Note");
            noteDialog.setHeaderText("Enter your note for " + creatureName + ":");
            noteDialog.setContentText("Note:");
            String cssFilePath = getClass().getResource("minecraft-style.css").toExternalForm();
            noteDialog.getDialogPane().getStylesheets().add(cssFilePath);
            noteDialog.getDialogPane().getStyleClass().add("dialog-pane");

            String note = noteDialog.showAndWait().orElse(null);
            if (note != null && !note.isEmpty()) {
                try {
                    encyclopedia.addNoteToCreature(creatureName, note);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                showAlert("Note added", "Note added successfully for " + creatureName + ".");
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        String cssFilePath = getClass().getResource("minecraft-style.css").toExternalForm();
        alert.getDialogPane().getStylesheets().add(cssFilePath);
        alert.getDialogPane().getStyleClass().add("dialog-pane");
        alert.showAndWait();
    }

}
