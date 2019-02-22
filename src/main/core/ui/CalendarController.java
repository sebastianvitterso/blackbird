package main.core.ui;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import main.calendar.CalendarGenerator;


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

	CalendarGenerator generator = new CalendarGenerator();
	private int displayWeek = generator.getRelevantWeek(); 
	
	@FXML
	void initialize() {
		calendarPane.getChildren().setAll(generator.getView());
		updateWeek(displayWeek);
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
		
	}
	@FXML
	void handleMinusBtn() {
	}
	
	
}
