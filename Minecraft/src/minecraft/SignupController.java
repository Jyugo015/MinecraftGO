/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package minecraft;

import java.io.IOException;
import java.net.URL;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
public class SignupController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField emailField;
    
    @FXML
    private Label usernameError;
    @FXML
    private Label emailError;
    @FXML
    private Label passwordError;
    @FXML
    private Label confirmPasswordError;
    
    private String verificationCode;    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    protected void handleSignupButtonAction(ActionEvent event) throws IOException, SQLException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String email = emailField.getText();
        
        boolean hasError = false;
        
        clearErrors();

        if (username.isEmpty()) {
            usernameError.setText("Username cannot be empty");
            hasError = true;
        }

        if (password.isEmpty()) {
            passwordError.setText("Password cannot be empty");
            hasError = true;
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordError.setText("Confirm Password cannot be empty");
            hasError = true;
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordError.setText("Passwords do not match");
            hasError = true;
        }

        if (email.isEmpty()) {
            emailError.setText("Email cannot be empty");
            hasError = true;
        } else if (!isValidEmail(email)) {
            emailError.setText("Invalid Email");
            hasError = true;
        }

        if (database_user.getUserByEmail(email) != null) {
            emailError.setText("The email is already taken.");
            hasError = true;
        }

        if (database_user.getUserByUsername(username)!= null){
            usernameError.setText("The username is already taken.");
            hasError = true;
        }

        if (hasError) {
            return;
        }
        // Generate a verification code
        verificationCode = generateVerificationCode();
        try {
            GmailService.sendVerificationEmail(email, verificationCode);
        } catch (Exception e) {
            emailError.setText("Failed to send verification email.");
            e.printStackTrace();
            return;
        }

        // Show verification code input dialog
        showVerificationDialog(email, username, password);
    }

    private String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        int code = random.nextInt(999999);
        return String.format("%06d", code);
    }

    private void showVerificationDialog(String email, String username, String password) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Verification.fxml"));
        Parent root = loader.load();
        VerificationController controller = loader.getController();
        controller.setSignupController(this);
        controller.setEmail(email);
        controller.setUsername(username);
        controller.setPassword(password);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    protected void handleBackToLoginButtonAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene loginScene = new Scene(root);
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(loginScene);
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.endsWith(".com");
    }

    private void clearErrors() {
        usernameError.setText("");
        emailError.setText("");
        passwordError.setText("");
    }
    
     public boolean verifyCode(String inputCode) {
        return verificationCode.equals(inputCode);
    }

    public void completeSignup(String email, String username, String password) {
        String hashedPassword = PasswordHash.hashPassword(password);
        User newUser = new User(username, email, hashedPassword);
        boolean userAdded = database_user.addUser(newUser);

        if (userAdded) {
            System.out.println("User signed up: " + username + ", " + email);
            // Navigate back to login
            try {
                Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
                Scene loginScene = new Scene(root);
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(loginScene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            confirmPasswordError.setText("An error occurred during signup.");
        }
    }

}