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
import main.core.ui.tabs.AssignmentsController;
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
    @FXML private JFXTextField nameTextField;
    @FXML private JFXTextArea descriptionTextArea;
    @FXML private JFXDatePicker datePicker;
    @FXML private JFXTimePicker timePicker;
    @FXML private Label pointLabel;
    @FXML private JFXTextField maxPoints; 
    @FXML private JFXTextField fileNameTextField;
    @FXML private JFXButton uploadFileButton;
    @FXML private JFXButton registerButton;
    
    private MenuController menuController;
    private JFXDialog dialog;
    private File selectedFile;
    private AssignmentsController assignmentController; 
    
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
		assignmentController = Loader.getController(View.ASSIGNMENTS_VIEW);
	}

	/**
	 * Connects this controller to associated JFXDialog.
	 */
	public void connectDialog(JFXDialog dialog) {
		this.dialog = dialog;
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
		String description = descriptionTextArea.getText();
		Timestamp deadline = getDeadline();
		int maxScore = Integer.parseInt(maxPoints.getText());
		int passingScore = Integer.parseInt(pointLabel.getText());
		String name = nameTextField.getText();

		return new Assignment(-1, course, title, description, deadline, maxScore, passingScore);

	}

	public void clear() {
		nameTextField.clear();
		descriptionTextArea.clear();
		datePicker.setValue(null);
		timePicker.setValue(null);
		maxPoints.setText("");
		nameTextField.setDisable(false);
	}
	
	//laste opp fil
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
		
		assignmentController.update();
		dialog.close();
		 
	}

}
