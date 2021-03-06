package main.core.ui;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import main.app.Loader;
import main.components.Calendar;
import main.models.Course;
import main.utils.PostInitialize;
import main.utils.Refreshable;
import main.utils.Role;
import main.utils.View;


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
	private MenuController menuController;
	
	@FXML
	void initialize() {
		calendar = new Calendar(calendarPane);
	}
	
	/**
     * Runs any methods that require every controller to be initialized.
     * This method should only be invoked by the FXML Loader class.
     */
    @PostInitialize
    private void postInitialize() {
    	menuController = Loader.getController(View.MENU_VIEW);
    }
	
	public void showButtons() {
		if (menuController.getSelectedRole() == Role.PROFESSOR) {
			removeStudass.setVisible(true);
			addStudass.setVisible(true);	
		} else {
			refreshBtn.setVisible(true);
		}
	}
		
	
	@Override
	public void update() {
		calendarPane.getChildren().setAll(calendar.getView());
		displayWeek = Calendar.getRelevantWeek(); 
		Course selectedCourse = menuController.getSelectedCourse();
		if (selectedCourse == null)
			return;
		calendar.setCourse(selectedCourse);
		calendar.setRole(menuController.getSelectedRole());
		showButtons();
		updateWeek(displayWeek);
	}
	
	@Override
	public void clear() {
		removeStudass.setVisible(false);
		addStudass.setVisible(false);
		refreshBtn.setVisible(false);
		calendar.resetSelections();
	}
	
	
	void updateWeek(int week) {
		weekLabel.setText("Uke " + Integer.toString(week));
		calendar.changeWeekUpdate(week);
	}

	@FXML
	void handleTodayBtn() {
		displayWeek = Calendar.getRelevantWeek(); 
		updateWeek(displayWeek);
	}
	
	@FXML
	void handleLeftBtn() {
		displayWeek--; 
		if (displayWeek <= 1) {
			displayWeek = 1; 
		}
		updateWeek(displayWeek);
	}
	
	@FXML
	void handleRightBtn() {
		displayWeek++; 
		if (displayWeek >= 52) {
			displayWeek = 52; 
		}
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
