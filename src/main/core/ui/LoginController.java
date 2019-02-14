package main.core.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController {

	private double xOffset;
	private double yOffset;
	
	@FXML private AnchorPane root;
    @FXML private JFXTextField usernameTextField;
    @FXML private JFXPasswordField passwordField;
    @FXML private JFXButton loginButton;
    
    
    @FXML
    void initialize() {
    	initializeMouseListeners();
    }
    
    /**
     * Mouse listener listening to MouseEvents at upper part of login interface.
     * Moving the interface can be done by clicking and dragging.
     */
	private void initializeMouseListeners() {
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
           @Override
           public void handle(MouseEvent event) {
               xOffset = event.getSceneX();
               yOffset = event.getSceneY();
           }});
        
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	if (yOffset < 70) {
            		Stage stage = (Stage) root.getScene().getWindow();
	                stage.setX(event.getScreenX() - xOffset);
	                stage.setY(event.getScreenY() - yOffset);
            	}
            }});
	}
    
	private void triggerInvalidLogin() {
	}
    
	// Event handlers
    @FXML
    void handlePasswordFieldKeyPressed(KeyEvent event) {
    	if (event.getCode() == KeyCode.ENTER) {
    		// ..validate and attempt login
    	}
    }
    
    @FXML
    void handleLoginClick(ActionEvent event) {
    }
    
    @FXML
    void handleExitClick(ActionEvent event) {
    	((Stage) root.getScene().getWindow()).close();
    }

	
}
