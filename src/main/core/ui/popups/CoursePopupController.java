package main.core.ui.popups;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CoursePopupController {
	@FXML private Label headerLabel;
    @FXML private JFXTextField courseCodeTextField;
    @FXML private JFXTextField courseNameTextField;
    @FXML private JFXTextArea descriptionTextArea;
    @FXML private JFXButton registerButton;
    
    @FXML
    void handleRegisterClick(ActionEvent event) {

    }
}
