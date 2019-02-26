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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import main.app.StageManager;
import main.db.LoginManager;
import main.utils.Refreshable;
import main.utils.View;

public class LoginController implements Refreshable {
	private double xOffset;
	private double yOffset;
	
	@FXML private StackPane rootPane;
    @FXML private JFXTextField usernameTextField;
    @FXML private JFXPasswordField passwordField;
    @FXML private JFXButton loginButton;
    @FXML private Label statusLabel;
    
    private FieldValidator usernameValidator;
    private FieldValidator passwordValidator;

    
    /**
     * Initializes every component in the user interface.
     * This method is automatically invoked when loading the corresponding FXML file.
     */
    @FXML
    private void initialize() {
    	initializeMouseListeners();
    	initializeTextInputValidation();
    }
    
    /**
     * Initializes field validators for nodes accepting input from the user.
     */
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
        rootPane.setOnMousePressed(new EventHandler<MouseEvent>() {
           @Override
           public void handle(MouseEvent event) {
               xOffset = event.getSceneX();
               yOffset = event.getSceneY();
           }});
        
        rootPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	if (yOffset < 70) {
            		Stage stage = (Stage) rootPane.getScene().getWindow();
	                stage.setX(event.getScreenX() - xOffset);
	                stage.setY(event.getScreenY() - yOffset);
            	}
            }});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		usernameTextField.setText(null);
		passwordField.setText(null);
		usernameTextField.requestFocus();
	}
	
	/**
	 * Method to be invoked when invalid login credentials are given. Updates the state of input validators.
	 */
	private void invalidCredentials() {
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
    	
    	if (LoginManager.login(username, password))
    		StageManager.loadView(View.MAIN_VIEW);
    	else
    		invalidCredentials();
    }
    
    @FXML
    void handleExitClick(ActionEvent event) {
    	((Stage) rootPane.getScene().getWindow()).close();
    }
    
    /**
     * Custom implementation of {@link ValidatorBase} where errors can be triggered manually.
     */
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