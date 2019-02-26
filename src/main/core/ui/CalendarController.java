package main.core.ui;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import main.calendar.Calendar;
import main.db.CourseManager;
import main.utils.Refreshable;
import main.utils.Role;


public class CalendarController implements Refreshable {
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
	@FXML
	private JFXButton refreshBtn;
	
	private Calendar calendar; 
	private int displayWeek; 
	
	@FXML
	void initialize() {
		calendar = new Calendar();
	}
	
	public void showButtons() {
		if (calendar.getRole() == Role.PROFESSOR) {
			removeStudass.setVisible(true);
			addStudass.setVisible(true);	
		} else {
			refreshBtn.setVisible(true);
		}
	}
		
	
	@Override
	public void update() {
		calendarPane.getChildren().setAll(calendar.getView());
		displayWeek = calendar.getRelevantWeek(); 
		updateWeek(displayWeek);
		calendar.setCourse(CourseManager.getCourse("TDT4100")); // TODO: FIx this, bruv. 
		calendar.setRole(calendar.getRole());
		showButtons();
		calendar.updateAllCells();
	}
	
	@Override
	public void clear() {
		
	}
	
	
	void updateWeek(int week) {
		weekLabel.setText("Uke " + Integer.toString(week));
	}

	@FXML
	void handleTodayBtn() {
		displayWeek = calendar.getRelevantWeek(); 
		calendar.changeWeekUpdate(displayWeek);
		updateWeek(displayWeek);

	}
	
	@FXML
	void handleLeftBtn() {
		displayWeek--; 
		if (displayWeek <= 1) {
			displayWeek = 1; 
		}
		calendar.changeWeekUpdate(displayWeek);
		updateWeek(displayWeek);
	}
	
	@FXML
	void handleRightBtn() {
		displayWeek++; 
		if (displayWeek >= 52) {
			displayWeek = 52; 
		}
		calendar.changeWeekUpdate(displayWeek);
		updateWeek(displayWeek);
	}
	@FXML
	void handleRefreshBtn() {
		calendar.updateAllCells();
	}
	@FXML
	void handlePlusBtn() {
		calendar.changeSelectedAvailableSlots(1);
	}
	@FXML
	void handleMinusBtn() {
		calendar.changeSelectedAvailableSlots(-1);
	}
	
}
