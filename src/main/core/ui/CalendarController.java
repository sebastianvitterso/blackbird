package main.core.ui;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import main.app.Loader;
import main.calendar.Calendar;
import main.db.CourseManager;
import main.db.LoginManager;
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
		calendar = new Calendar();
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
		if (CourseManager.getRoleInCourse(LoginManager.getActiveUser(), Calendar.course) == Role.PROFESSOR) {
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
		updateWeek(displayWeek);
		Course selectedCourse = menuController.getSelectedCourse();
		if (selectedCourse == null)
			return;
		calendar.setCourse(selectedCourse);
		calendar.setRole(CourseManager.getRoleInCourse(LoginManager.getActiveUser(), selectedCourse));
		showButtons();
		calendar.updateAllCells();
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
	}

	@FXML
	void handleTodayBtn() {
		displayWeek = Calendar.getRelevantWeek(); 
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
