package main.core.ui.popups;

import com.jfoenix.controls.JFXDialog;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.utils.PostInitialize;

public class CalendarPopupController {
	
	private JFXDialog dialog;
	private Label title;
	
	
	////Initialization ////
	   /**
	    * Initializes every component in the user interface.
	    * This method is automatically invoked when loading the corresponding FXML file.
	    */
		@FXML 
		private void initialize() {
			// Bind 'register' button to being disabled when no title and description is given.
//			registerButton.disableProperty().bind(announcementTitleTextField.textProperty().isEmpty());
//			registerButton.disableProperty().bind(announcementDescriptionTextArea.textProperty().isEmpty());
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
	    	
	    }

}
