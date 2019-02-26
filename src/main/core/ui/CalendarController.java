package main.core.ui;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import main.calendar.CalendarGenerator;
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
	
	private CalendarGenerator generator; 
	private int displayWeek; 
	
	@FXML
	void initialize() {
		generator = new CalendarGenerator();
	}
	
	public void showButtons() {
		if (generator.getRole() == Role.PROFESSOR) {
			removeStudass.setVisible(true);
			addStudass.setVisible(true);	
		} else {
			refreshBtn.setVisible(true);
		}
	}
		
	
	@Override
	public void update() {
		System.out.println("Updating");
		calendarPane.getChildren().setAll(generator.getView());
		displayWeek = generator.getRelevantWeek(); 
		updateWeek(displayWeek);

			showButtons();
		generator.updateAllCells();
	}
	
	@Override
	public void clear() {
		
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
	void handleRefreshBtn() {
		generator.updateAllCells();
	}
	@FXML
	void handlePlusBtn() {
		generator.changeSelectedAvailableSlots(1);
	}
	@FXML
	void handleMinusBtn() {
		generator.changeSelectedAvailableSlots(-1);
	}
	
}
