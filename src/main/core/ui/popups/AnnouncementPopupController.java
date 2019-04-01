package main.core.ui.popups;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.app.Loader;
import main.core.ui.MenuController;
import main.core.ui.tabs.OverviewController;
import main.db.AnnouncementManager;
import main.db.LoginManager;
import main.models.Course;
import main.models.User;
import main.utils.PostInitialize;
import main.utils.Refreshable;
import main.utils.Role;
import main.utils.View;

public class AnnouncementPopupController implements Refreshable{
	
	@FXML private Label headerLabel;
	@FXML private JFXTextField announcementTitleTextField;
	@FXML private JFXTextArea announcementDescriptionTextArea;
	@FXML private JFXButton registerButton;
	@FXML private JFXComboBox<String> audienceComboBox; 
	
	private MenuController menuController;
	private OverviewController overviewController;
	private JFXDialog dialog;
	
	
	////Initialization ////
   /**
    * Initializes every component in the user interface.
    * This method is automatically invoked when loading the corresponding FXML file.
    */
	@FXML 
	private void initialize() {
		// Bind 'register' button to being disabled when no title and description is given.
		registerButton.disableProperty().bind(announcementTitleTextField.textProperty().isEmpty());
		registerButton.disableProperty().bind(announcementDescriptionTextArea.textProperty().isEmpty());
		audienceComboBox.getItems().addAll("Alle", "Læringsassistenter, Emneansvarlige", "Emneansvarlige");
	}
	
	/**
     * Connects this controller to associated JFXDialog.
     */
    public void connectDialog(JFXDialog dialog) {
    	this.dialog = dialog;
    }
	
	 /**
     * Runs any methods that require every controller to be initialized.
     * This method should only be invoked by the FXML Loader class.
     */
    @PostInitialize
    private void postInitialize() {
    	menuController = Loader.getController(View.MENU_VIEW);
    	overviewController = Loader.getController(View.OVERVIEW_VIEW);
    }
    
    public void clear() {
    	announcementTitleTextField.clear();
    	announcementDescriptionTextArea.clear();
    }
	
	@FXML 
	public void createAnnouncement() {
		Course course = menuController.getSelectedCourse();
		User user = LoginManager.getActiveUser();
		Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
		String title = announcementTitleTextField.getText();
		String description = announcementDescriptionTextArea.getText();
		Role audience = null; 
		switch (audienceComboBox.getValue()) {
		case "Alle":
			audience = Role.STUDENT; 
			break;
		case "Læringsassistenter, Emneansvarlige":
			audience = Role.ASSISTANT; 
			break;
		case "Emneansvarlige":
			audience = Role.PROFESSOR; 
			break;
		}
		AnnouncementManager.addAnnouncement(course, user, timestamp, title, description, audience);
	}
	
	@FXML
	void handleRegisterClick(ActionEvent event) {
		createAnnouncement();
		overviewController.update();
		dialog.close();
	}

}
