package main.core.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.app.Loader;
import main.models.Course;
import main.utils.PostInitialize;
import main.utils.View;

public class MenuController {
	private MainController mainController;
	
	@FXML private Label nameLabel;
    @FXML private Label roleLabel;
    @FXML private JFXComboBox<Course> courseComboBox;
    @FXML private JFXButton overviewButton;
    @FXML private JFXButton exercisesButton;
    @FXML private JFXButton schedulingButton;
    @FXML private JFXButton calendarButton;
    @FXML private JFXButton membersButton;
    
    
    @FXML
    private void initalize() {
    	
    }
    
    @PostInitialize
    public void postInitialize() {
    	mainController = Loader.getController(View.MAIN_VIEW);
    }
    
    //// Update ////
    
    
    
    
    
    @FXML void handleCalendarClick(ActionEvent event) {
//    	mainController.loadContent(View.CALENDAR_VIEW);
    }

    @FXML
    void handleExercisesClick(ActionEvent event) {

    }

    @FXML
    void handleLogoutClick(ActionEvent event) {

    }

    @FXML
    void handleMembersClick(ActionEvent event) {

    }

    @FXML
    void handleOverviewClick(ActionEvent event) {

    }

    @FXML
    void handleSchedulingClick(ActionEvent event) {

    }
}
