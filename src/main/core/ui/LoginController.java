package main.core.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.base.ValidatorBase;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.db.LoginManager;
import main.utils.Clearable;

public class LoginController implements Clearable {
	private LoginManager loginManager;
	
	private double xOffset;
	private double yOffset;
	
	@FXML private AnchorPane root;
    @FXML private JFXTextField usernameTextField;
    @FXML private JFXPasswordField passwordField;
    @FXML private JFXButton loginButton;
    @FXML private Label statusLabel;
    
    private FieldValidator usernameValidator;
    private FieldValidator passwordValidator;

    
    public LoginController() {
    	loginManager = new LoginManager(this);
	}
    
    @FXML
    void initialize() {
    	initializeMouseListeners();
    	initializeTextInputValidation();
    }
    
    private void initializeTextInputValidation() {
    	usernameValidator = new FieldValidator();
    	passwordValidator = new FieldValidator();
    	Image icon = new Image(LoginController.class.getClassLoader().getResourceAsStream("icons/invalid.png"));
		
    	ImageView userIcon = new ImageView(icon);
		ImageView passIcon = new ImageView(icon);
		
		userIcon.getStyleClass().add("login-validator-icon");
		passIcon.getStyleClass().add("login-validator-icon");
		
		usernameValidator.setIcon(userIcon);
		passwordValidator.setIcon(passIcon);
		
    	usernameTextField.getValidators().add(usernameValidator);
    	passwordField.getValidators().add(passwordValidator);
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
    
	@Override
	public void clear() {
		usernameTextField.setText(null);
		passwordField.setText(null);
		usernameTextField.requestFocus();
	}
	
	public void invalidCredentials() {
		usernameValidator.setError(true);
		passwordValidator.setError(true);
		
		usernameTextField.validate();
		passwordField.validate();
		
		statusLabel.setText("Ugyldig brukernavn eller passord.");
	}
	
    @FXML
    void handlePasswordFieldKeyPressed(KeyEvent event) {
    	if (event.getCode() == KeyCode.ENTER) {
    		handleLoginClick(null);
    	}
    }
    
    @FXML
    void handleLoginClick(ActionEvent event) {
    	statusLabel.setText(null);
    	
		usernameValidator.setError(false);
		passwordValidator.setError(false);

		usernameTextField.validate();
		passwordField.validate();
		
    	String username = usernameTextField.getText();
    	String password = passwordField.getText();
    	
    	loginManager.login(username, password);
    }
    
    @FXML
    void handleExitClick(ActionEvent event) {
    	((Stage) root.getScene().getWindow()).close();
    }

    private class FieldValidator extends ValidatorBase {

	    public FieldValidator() {
	    	
	    }

	    @Override
		protected void eval() {
		}
	    
	    public void setError(boolean error) {
	    	hasErrors.set(error);
	    }
	}

	
}