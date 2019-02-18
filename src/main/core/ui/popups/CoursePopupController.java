package main.core.ui.popups;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.core.ui.Refreshable;

public class CoursePopupController implements Refreshable {
	@FXML private Label headerLabel;
    @FXML private JFXTextField courseCodeTextField;
    @FXML private JFXTextField courseNameTextField;
    @FXML private JFXTextArea descriptionTextArea;
    @FXML private JFXButton registerButton;
    
    @FXML
    void handleRegisterClick(ActionEvent event) {

    }

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}
}
