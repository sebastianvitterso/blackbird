package main.core.ui.popups;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UserPopupController {
	@FXML private Label headerLabel;
    @FXML private JFXTextField firstNameTextField;
    @FXML private JFXTextField lastNameTextField;
    @FXML private JFXTextField emailTextField;
    @FXML private JFXTextField usernameTextField;
    @FXML private JFXTextField passwordTextField;
    @FXML private JFXButton registerButton;
    
    @FXML
    void initialize() {
    	registerButton.disableProperty().bind(usernameTextField.textProperty().isEmpty());
    }
    
    @FXML
    void handleRegisterClick(ActionEvent event) {
    	
    }

}
