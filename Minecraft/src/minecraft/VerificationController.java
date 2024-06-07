/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package minecraft;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class VerificationController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private TextField verificationCodeField;
    @FXML
    private Label errorLabel;

    private SignupController signupController;
    private String email;
    private String username;
    private String password;

    public void setSignupController(SignupController signupController) {
        this.signupController = signupController;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @FXML
    protected void handleVerifyButtonAction() {
        String inputCode = verificationCodeField.getText();
        if (signupController.verifyCode(inputCode)) {
            signupController.completeSignup(email, username, password);
            Stage stage = (Stage) verificationCodeField.getScene().getWindow();
            stage.close();
            showSignupSuccessfulDialog();
        } else {
            errorLabel.setText("Invalid verification code.");
        }
    }

    private void showSignupSuccessfulDialog() {
        // Create an alert of type INFORMATION
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Signup Successful");
        alert.setHeaderText(null); // No header text

        // Create the content text with a larger font size
        Label contentText = new Label("Your signup was successful!");
        contentText.setStyle("-fx-font-size: 16px;"); // Set the font size to 16px

        // Set the content of the alert to the custom label
        alert.getDialogPane().setContent(contentText);
        // Show the alert and wait for the user to close it
        alert.showAndWait();
    }
    
}
