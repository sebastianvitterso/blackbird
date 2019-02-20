package main.core.ui.popups;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.app.Loader;
import main.core.ui.AdminController;
import main.db.CourseManager;
import main.models.Course;
import main.util.Clearable;
import main.util.PostInitialize;
import main.util.View;

public class CoursePopupController implements Clearable {
	@FXML private Label headerLabel;
    @FXML private JFXTextField courseCodeTextField;
    @FXML private JFXTextField courseNameTextField;
    @FXML private JFXTextArea courseDescriptionTextArea;
    @FXML private JFXButton registerButton;
    
    private boolean editMode;
    private AdminController adminController;
    private JFXDialog dialog;
    
    
    //// Initialization ////
    /**
     * Initializes every component in the user interface.
     * This method is automatically invoked when loading the corresponding FXML file.
     */
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
    }
    
    /**
     * Runs any methods that require every controller to be initialized.
     * This method should only be invoked by the FXML Loader class.
     */
    @PostInitialize
    public void postInitialize() {
    	adminController = Loader.getController(View.ADMIN_VIEW);
    }
    
    
    //// Loading ////
    /**
     * Prepares popup for displaying 'edit course' mode.
     * This method must be called prior to displaying the dialog box.
     */
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
    
    /**
     * Prepares popup for displaying 'new course' mode.
     * This method must be called prior to displaying the dialog box.
     */
    public void loadNewCourse() {
    	// Layout configurations for 'new' mode.
    	editMode = false;
    	headerLabel.setText("Registrer emne");
    	registerButton.setText("Registrer");
    }
    
    /**
     * Reads input parameters, creating a new 'Course' object.
     */
    private Course createCourseFromInput() {
    	String courseCode = courseCodeTextField.getText();
    	String courseName = courseNameTextField.getText();
    	String courseDescription = courseDescriptionTextArea.getText();
    	return new Course(courseCode, courseName, courseDescription);
    }
    
    
    //// Overrides ////
    /**
     * {@inheritDoc}
     */
	@Override
	public void clear() {
		courseCodeTextField.clear();
		courseNameTextField.clear();
		courseDescriptionTextArea.clear();
		courseCodeTextField.setDisable(false);
	}
    
	
	//// Event handlers ////
    @FXML
    void handleRegisterClick(ActionEvent event) {
//    	DatabaseManager.submitRunnable(() -> {
        	Course course = createCourseFromInput();
        	
        	if (editMode) {
//    			DatabaseManager.submitRunnable(() -> {
    				CourseManager.updateCourse(course);
//    			});
        	} else {
//    			DatabaseManager.submitRunnable(() -> {
    				CourseManager.registerCourse(course);
//    			});
        	}
        	
//        	Platform.runLater(() -> {
        		adminController.updateCourseView();
        		dialog.close();
//        	});
//    	});
    }
    
}
