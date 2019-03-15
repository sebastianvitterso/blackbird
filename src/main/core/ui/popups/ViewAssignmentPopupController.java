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
import javafx.scene.layout.StackPane;
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
public class ViewAssignmentPopupController {
	//TODO: Change vapNames to something else
	@FXML private StackPane rootPane;
	@FXML private Label vapHeader;
	@FXML private Label vapDeadline;
	@FXML private Label vapMaxScore;
	@FXML private Label vapPassingScore;
	@FXML private Label vapDescription;
	@FXML private Label vapAssignmentFileLink;
	@FXML private Label vapStatus;
	@FXML private Label vapScore;
	@FXML private Label vapComment;
	@FXML private Label vapSubmissionFileLink;
	@FXML private JFXTextField vapFilename;
	@FXML private JFXButton vapUploadFileButton;
	@FXML private JFXButton vapDeliverButton;
	@FXML private JFXButton vapCancelButton;
    
//    private MenuController menuController;
    private JFXDialog dialog;
//    private File selectedFile;
//    private AssignmentsController assignmentController; 
    
    @FXML
    private void initialize() {
    }

	/**
	 * Runs any methods that require every controller to be initialized. This method
	 * should only be invoked by the FXML Loader class.
	 */
	@PostInitialize
	private void postInitialize() {
//		menuController = Loader.getController(View.MENU_VIEW);
//		assignmentController = Loader.getController(View.ASSIGNMENTS_VIEW);
	}

//	/**
//	 * Connects this controller to associated JFXDialog.
//	 */
	public void connectDialog(JFXDialog dialog) {
		this.dialog = dialog;
	}


	public void clear() {
	}
	
	

}
