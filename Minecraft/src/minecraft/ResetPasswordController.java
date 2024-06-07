/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package minecraft;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class ResetPasswordController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label errorLabel;

    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    protected void handleConfirmButtonAction() throws SQLException {
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("Password cannot be empty.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            errorLabel.setText("Password do not match.");
            return;
        }

        String hashedPassword = PasswordHash.hashPassword(newPassword);
        database_user.updateUserPassword(user.getEmail(), hashedPassword);
        System.out.println(hashedPassword);
        errorLabel.setText("Password has been reset.");

        Stage stage = (Stage) newPasswordField.getScene().getWindow();
        stage.close();
    }
}
