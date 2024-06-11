/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package minecraft;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField emailField;

    @FXML
    private Label usernameError;
    @FXML
    private Label emailError;
    @FXML
    private Label passwordError;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    protected void handleLoginButtonAction(ActionEvent event) throws SQLException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();
        
        boolean hasError = false;

        clearErrors();

        if (username.isEmpty()) {
            usernameError.setText("Username cannot be empty.");
            hasError = true;
        }

        if (password.isEmpty()) {
            passwordError.setText("Password cannot be empty.");
            hasError = true;
        }

        if (email.isEmpty()) {
            emailError.setText("Email cannot be empty.");
            hasError = true;
        } else if (!isValidEmail(email)) {
            emailError.setText("Invalid Email");
            hasError = true;
        }

        if (hasError) {
            return;
        }
       
        User user = database_user.getUserByEmail(email);

        if (user == null) {
            emailError.setText("No user found with this email.");
            return;
        }
        
        //password hashing, administrator cannot really get to know the password of user
        String hashedPassword = PasswordHash.hashPassword(password);

        if (!user.getUsername().equals(username) || !user.getHashedPassword().equals(hashedPassword)) {
            usernameError.setText("Incorrect username or password.");
            return;
        }

        System.out.println("User logged in: " + username + ", " + email);
        
        try {
            EnderBackpackController.username = username;
            MultitoolGUIController.username = username;
            newPotionSatchel.username= username;
            PotionSatchelController.username = username;
            AutofarmerblockController.username = username;
            SecureChestController.username = username;
            AdventurerDiaryController.username= username;
            Parent root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("minecraft-style.css").toExternalForm());
            Stage stage = new Stage();
            stage.setTitle("Minecraft");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage currentStage = (Stage) emailField.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    protected void handleSignupButtonAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Signup.fxml"));
        Scene signupScene = new Scene(root);
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setTitle("Sign Up");
        stage.setScene(signupScene);
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && (email.endsWith(".com")||email.endsWith(".my"));
    }

     private void clearErrors() {
        usernameError.setText("");
        emailError.setText("");
        passwordError.setText("");
    }

    @FXML
    protected void handleForgotPasswordButtonAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("ForgotPassword.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Forgot Password");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
