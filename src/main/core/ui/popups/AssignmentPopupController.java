package main.core.ui.popups;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import main.app.Loader;
import main.core.ui.MenuController;
import main.db.AssignmentManager;
import main.models.Assignment;
import main.models.Course;
import main.utils.PostInitialize;
import main.utils.View;

/**
 * @author Beatrice
 *
 */
public class AssignmentPopupController {
	
	@FXML private Label headerLabel;
    @FXML private JFXTextField courseNameTextField;
    @FXML private JFXTextArea courseDescriptionTextArea;
    @FXML private JFXDatePicker datePicker;
    @FXML private JFXTimePicker timePicker;
    @FXML private Label pointLabel;
    @FXML private Label maxPointLabel; 
    @FXML private JFXTextField fileNameTextField;
    @FXML private JFXButton uploadFileButton;
    @FXML private JFXButton registerButton;
    
    private MenuController menuController;
    private JFXDialog dialog;
    private File selectedFile;
    private boolean editMode;
    
    @FXML
    private void initialize() {
    	// Bind 'register' button to being disabled when no course code is given
//    	registerButton.disableProperty().bind(courseNameTextField.textProperty().isEmpty());
    }

	/**
	 * Runs any methods that require every controller to be initialized. This method
	 * should only be invoked by the FXML Loader class.
	 */
	@PostInitialize
	private void postInitialize() {
		menuController = Loader.getController(View.MENU_VIEW);
	}

	/**
	 * Connects this controller to associated JFXDialog.
	 */
	public void connectDialog(JFXDialog dialog) {
		this.dialog = dialog;
	}

	/**
	 * Prepares popup for displaying 'new exercise' mode. This method must be called
	 * prior to displaying the dialog box.
	 */
	public void loadNewExercise() {
		// Layout configurations for 'new' mode.
		editMode = false;
		headerLabel.setText("Registrer ny øving");
		registerButton.setText("Registrer");
	}

	/**
	 * Converts a {@link LocalDate} and {@link LocalTime} pair to corresponding
	 * {@link Timestamp}.
	 */
	private Timestamp getDeadline() {
		LocalDate exDate = datePicker.getValue();
		LocalTime exTime = timePicker.getValue();
		LocalDateTime dateTime = LocalDateTime.of(exDate, exTime);
		return Timestamp.from(dateTime.toInstant(ZoneOffset.UTC));
	}

	/**
	 * Reads input parameters, creating a new 'Course' object.
	 */
	private Assignment createAssignmentFromInput() {
		Course course = menuController.getSelectedCourse();
		String title = headerLabel.getText();
		String description = courseDescriptionTextArea.getText();
		Timestamp deadline = getDeadline();
		int maxScore = Integer.parseInt(maxPointLabel.getText());
		int passingScore = Integer.parseInt(pointLabel.getText());
		String name = courseNameTextField.getText();

		return new Assignment(-1, course, title, description, deadline, maxScore, passingScore);

	}

	public void clear() {
		courseNameTextField.clear();
		courseDescriptionTextArea.clear();
		datePicker.setValue(null);
		timePicker.setValue(null);
		maxPointLabel.setText("");
		courseNameTextField.setDisable(false);
	}

	@FXML
	private void handleFileOpenClick(ActionEvent event) {
		// Retrive parent for file chooser
		Stage mainStage = (Stage) dialog.getScene().getWindow();

		// Construct file chooser
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Data File");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("PDF file (*.pdf)", "*.pdf"));

		// Launch file chooser and retrive selected file
		selectedFile = fileChooser.showOpenDialog(mainStage);

		fileNameTextField.setText(selectedFile.getPath());

	}

	@FXML
	void handleRegisterClick(ActionEvent event) {
		Assignment assignment = createAssignmentFromInput();
		AssignmentManager.addAssignment(assignment, fileNameTextField.getText());
		// String courseCode = (assignment.getCourse()).getCourseCode();
		// AssignmentManager.addAssignment(courseCode, assignment.getTitle(),
		// assignment.getDeadLine().toString(), assignment.getMaxScore(),
		// assignment.getPassingScore(), fileNameTextField.getText());

		/*
		 * TODO: gjøre ferdig denne! Hva skjer med editmode?
		 */
		/*
		 * if (editMode) { CourseManager.updateCourse(course); } else {
		 * CourseManager.registerCourse(course); }
		 * 
		 * adminController.updateCourseView(); dialog.close();
		 */
	}

}
