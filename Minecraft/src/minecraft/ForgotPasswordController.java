/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package minecraft;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.Random;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class ForgotPasswordController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    @FXML
    private TextField emailField;
    @FXML
    private TextField codeField;
    @FXML
    private Label errorLabel;

    private String generatedCode;
    private String userEmail;
    
    @FXML
    protected void handleGetCodeButtonAction() throws SQLException {
        String email = emailField.getText();

        userEmail = email;
        User user = database_user.getUserByEmail(email);
        if (user == null) {
            errorLabel.setText("No user found with this email.");
            return;
        }
        generatedCode = generateVerificationCode();
        try {
            GmailService.sendVerificationEmail(email, generatedCode);
            errorLabel.setText("Verification code sent to email.");
        } catch (Exception e) {
            errorLabel.setText("Failed to send verification email.");
            e.printStackTrace();
        }

    }
    

    @FXML
    protected void handleVerifyButton() throws SQLException {
        String code = codeField.getText();
        if (!code.equals(generatedCode)) {
                errorLabel.setText("Incorrect verification code.");
                return;
            }
        
         try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ResetPassword.fxml"));
            Parent root = loader.load();
            ResetPasswordController controller = loader.getController();
            controller.setUser(database_user.getUserByEmail(userEmail));

            Stage stage = new Stage();
            stage.setTitle("Reset Password");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage currentStage = (Stage) emailField.getScene().getWindow();
        currentStage.close();
    }

    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }
}
