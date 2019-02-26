package main.core.ui;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import main.calendar.CalendarGenerator;
import main.db.CourseManager;
import main.db.LoginManager;
import main.models.Course.Role;


public class CalendarController {
	@FXML
	private AnchorPane root;
	@FXML
	private StackPane calendarPane;
	@FXML
	private JFXButton todaybtn;
	@FXML
	private JFXButton left;
	@FXML
	private JFXButton right;
	@FXML
	private Label weekLabel;
	@FXML
	private JFXButton removeStudass;
	@FXML
	private JFXButton addStudass;

	CalendarGenerator generator; 
	private int displayWeek; 
	
	
	@FXML
	void initialize() {
		generator = new CalendarGenerator();
		
		calendarPane.getChildren().setAll(generator.getView());
		displayWeek = generator.getRelevantWeek(); 
		updateWeek(displayWeek);
		
		//TODO: lage en spørring for å sjekke rollen til innloggeren: 
		//Typ: 
		/* if (generator.getRole() == Role.PROFESSOR) {
			removeStudass.setVisible(true);
			addStudass.setVisible(true);
		}*/
		
	}
		
	
	void updateWeek(int week) {
		weekLabel.setText("Uke " + Integer.toString(week));
	}

	@FXML
	void handleTodayBtn() {
		displayWeek = generator.getRelevantWeek(); 
		generator.changeWeekUpdate(displayWeek);
		updateWeek(displayWeek);

	}
	
	@FXML
	void handleLeftBtn() {
		displayWeek--; 
		if (displayWeek <= 1) {
			displayWeek = 1; 
		}
		generator.changeWeekUpdate(displayWeek);
		updateWeek(displayWeek);
	}
	
	@FXML
	void handleRightBtn() {
		displayWeek++; 
		if (displayWeek >= 52) {
			displayWeek = 52; 
		}
		generator.changeWeekUpdate(displayWeek);
		updateWeek(displayWeek);
	}
	
	@FXML
	void handlePlusBtn() {
		generator.changeSelectedAvailableSlots(1);
	}
	@FXML
	void handleMinusBtn() {
		generator.changeSelectedAvailableSlots(-1);
	}
	
	/*
	 * if (CourseManager.isUserRoleInCourse(LoginManager.getActiveUser(), CourseManager.getCourse("TDT4140"), Role.ASSISTANT)) {
			removeStudass.setVisible(true);
			addStudass.setVisible(true);
	 */
}
