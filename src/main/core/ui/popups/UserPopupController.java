package main.core.ui.popups;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.core.ui.Refreshable;

public class UserPopupController implements Refreshable {
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

	@Override
	public void refresh() {
		
	}
}
