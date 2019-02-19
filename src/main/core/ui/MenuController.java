package main.core.ui;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MenuController {

	@FXML private JFXButton btnOversikt;
	@FXML private JFXButton btnOvinger;
	@FXML private JFXButton btnTimebestilling;
	@FXML private JFXButton btnKalender;
	@FXML private JFXButton btnMedlemmer;
	
	// Event handlers
	
	@FXML
    void handleOversiktClick(ActionEvent event) {
		// .. go to Oversikt
    }
	
	@FXML
    void handleOvingerClick(ActionEvent event) {
		// .. go to Ovinger
    }
	
	@FXML
    void handleTimebestillingClick(ActionEvent event) {
		// .. go to Timebestilling
    }
	
	@FXML
    void handleKalenderClick(ActionEvent event) {
		// .. go to Kalender
    }
	
	@FXML
    void handleMedlemmerClick(ActionEvent event) {
		// .. go to Medlemmer
    }
	
	@FXML
    void handleLogoutClick(ActionEvent event) {
		// .. log out and go to login screen
    }

}
