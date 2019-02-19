package main.core.ui.popups;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.app.Loader;
import main.app.View;
import main.core.ui.AdminController;
import main.core.ui.Refreshable;
import main.data.Course;
import main.data.Course.Role;
import main.db.DatabaseManager;

public class CoursePopupController implements Refreshable {
	@FXML private Label headerLabel;
    @FXML private JFXTextField courseCodeTextField;
    @FXML private JFXTextField courseNameTextField;
    @FXML private JFXTextArea courseDescriptionTextArea;
    @FXML private JFXButton registerButton;
    
    private boolean editMode;
    private AdminController adminController;
    private JFXDialog dialog;
    
   
    @FXML
    private void initialize() {
    	// Bind 'register' button to being disabled when no course code is given
    	registerButton.disableProperty().bind(courseCodeTextField.textProperty().isEmpty());
    }
    
    /**
     * Connects this controller to associated JFXDialog.
     */
    public void connectDialog(JFXDialog dialog) {
    	this.dialog = dialog;
    	adminController = (AdminController) Loader.getController(View.ADMIN_VIEW);
    }
    
    public void loadEditCourse(Course course) {
    	// Layout configurations for 'edit' mode.
    	editMode = true;
    	headerLabel.setText("Rediger emne");
    	registerButton.setText("Rediger");
    	
    	// Add current course info
    	courseCodeTextField.setText(course.getCourseCode());
    	courseNameTextField.setText(course.getName());
    	courseDescriptionTextArea.setText(course.getDescription());
    	
    	// Make sure primary key cannot be changed
    	courseCodeTextField.setDisable(true);
    }
    
    public void loadNewCourse() {
    	// Layout configurations for 'new' mode.
    	editMode = false;
    	headerLabel.setText("Registrer emne");
    	registerButton.setText("Registrer");
    	
    	// Clear current course info
    	courseCodeTextField.clear();
    	courseNameTextField.clear();
    	courseDescriptionTextArea.clear();
    	
    	// Enable 'course code' text field.
    	courseCodeTextField.setDisable(false);
    }
    
    private Course createCourseFromInput() {
    	String courseCode = courseCodeTextField.getText();
    	String courseName = courseNameTextField.getText();
    	String courseDescription = courseDescriptionTextArea.getText();
    	return new Course(courseCode, courseName, courseDescription);
    }
    
    @FXML
    void handleRegisterClick(ActionEvent event) {
    	DatabaseManager.submitRunnable(() -> {
        	Course course = createCourseFromInput();
        	
        	if (editMode) {
        		adminController.updateCourse(course);
        	} else {
        		adminController.registerCourse(course);
        	}
        	
        	Platform.runLater(() -> {
        		adminController.refresh();
        		dialog.close();
        	});
    	});
    }

	@Override
	public void refresh() {
		courseCodeTextField.setDisable(false);
		courseCodeTextField.clear();
		courseNameTextField.clear();
		courseDescriptionTextArea.clear();
	}
}
