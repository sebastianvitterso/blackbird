package main.core.ui.popups;

import java.io.File;
import java.sql.Timestamp;
import java.time.Instant;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import main.app.Loader;
import main.core.ui.tabs.AssignmentsController;
import main.db.LoginManager;
import main.db.SubmissionManager;
import main.models.Assignment;
import main.models.Submission;
import main.models.User;
import main.utils.PostInitialize;
import main.utils.Refreshable;
import main.utils.Status;
import main.utils.View;

/**
 * @author Beatrice
 *
 */
public class ViewAssignmentPopupController implements Refreshable {
	
	@FXML private StackPane rootPane;
    @FXML private Label headerLabel;
    @FXML private Label deadlineLabel;
    @FXML private Label maxScoreLabel;
    @FXML private Label passingScoreLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label statusLabel;
    @FXML private Label scoreLabel;
    @FXML private Label commentLabel;
    @FXML private HBox fileUploadHBox;
    @FXML private JFXTextField filenameTextField;
    @FXML private Label submissionFileLinkLabel;
    @FXML private JFXButton cancelButton;
    @FXML private JFXButton deliverButton;
    @FXML private JFXButton assignmentFileLinkButton;
    @FXML private JFXButton submissionFileLinkButton;
    
    private Assignment assignment;
    private String assignmentFileName;
    private Submission submission;
    private String submissionFileName;
    
    
//    private MenuController menuController;
    private JFXDialog dialog;
//    private File selectedFile;
    private AssignmentsController assignmentController; 
    
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
		assignmentController = Loader.getController(View.ASSIGNMENTS_VIEW);
	}

//	/**
//	 * Connects this controller to associated JFXDialog.
//	 */
	public void connectDialog(JFXDialog dialog) {
		this.dialog = dialog;
	}

	public void loadAssignment(Assignment assignment) {
		this.assignment = assignment;
		headerLabel.setText(assignment.getTitle());
		descriptionLabel.setText(assignment.getDescription());
		deadlineLabel.setText(String.format("Tidsfrist: %s", assignment.getDeadLine()));
		maxScoreLabel.setText(String.format("Maks: %s poeng", assignment.getMaxScore()));
		passingScoreLabel.setText(String.format("Krav: %s poeng", assignment.getPassingScore()));
		//TODO: Normalizer.normalize ? Bør oppgavene kunne hete "oppgaver-TDT4140-Øving 0"? Bør vi fikse æøå?
		assignmentFileName = String.format("oppgaver-%s-%s.pdf", assignment.getCourse().getCourseCode(), assignment.getTitle());
		assignmentFileLinkButton.setText(assignmentFileName);
	}
	
	public void loadSubmission(Submission submission) {
		this.submission = submission;
		Status status = Assignment.determineStatus(assignment, this.submission);
		switch(status){
		case PASSED:
			statusLabel.setText("Status: Godkjent"); 
			scoreLabel.setVisible(true);
			scoreLabel.setText(String.format("Poeng: %s/%s", submission.getScore(), assignment.getMaxScore()));
			commentLabel.setVisible(true);
			commentLabel.setText(submission.getComment());
			fileUploadHBox.setVisible(false);
			submissionFileLinkButton.setVisible(true);
			submissionFileName = String.format("innlevering-%s-%s-%s.pdf", 
					assignment.getCourse().getCourseCode(), assignment.getTitle(), submission.getUser().getUsername());
			submissionFileLinkButton.setText(submissionFileName);
			deliverButton.setVisible(false);
			break;
		case WAITING:
			statusLabel.setText("Status: Venter på godkjenning"); 
			scoreLabel.setVisible(false);
			commentLabel.setVisible(false);
			commentLabel.setText("");
			fileUploadHBox.setVisible(false);
			submissionFileLinkButton.setVisible(true);
			submissionFileName = String.format("innlevering-%s-%s-%s.pdf", 
					assignment.getCourse().getCourseCode(), assignment.getTitle(), submission.getUser().getUsername());
			submissionFileLinkButton.setText(submissionFileName);
			deliverButton.setVisible(false);
			break;
		case FAILED:
			statusLabel.setText("Status: Ikke godkjent"); 
			scoreLabel.setVisible(true);
			scoreLabel.setText(String.format("Poeng: %s/%s", submission.getScore(), assignment.getMaxScore()));
			commentLabel.setVisible(true);
			commentLabel.setText(submission.getComment());
			fileUploadHBox.setVisible(false);
			submissionFileLinkButton.setVisible(true);
			submissionFileName = String.format("innlevering-%s-%s-%s.pdf", 
					assignment.getCourse().getCourseCode(), assignment.getTitle(), submission.getUser().getUsername());
			submissionFileLinkButton.setText(submissionFileName);
			deliverButton.setVisible(false);
			break;
		case NOT_DELIVERED:
			statusLabel.setText("Status: Ikke levert"); 
			scoreLabel.setVisible(false);
			commentLabel.setVisible(false);
			commentLabel.setText("");
			fileUploadHBox.setVisible(true);
			submissionFileLinkButton.setVisible(false);
			deliverButton.setVisible(true);
			break;
		case DEADLINE_EXCEEDED:
			statusLabel.setText("Status: For seint, lulz"); 
			scoreLabel.setVisible(false);
			commentLabel.setVisible(true);
			commentLabel.setText("Øvingen ble ikke levert innen tidsfristen.");
			fileUploadHBox.setVisible(false);
			submissionFileLinkButton.setVisible(false);
			deliverButton.setVisible(false);
			break;
		default:
			break;
		}
	}
	
	
	@FXML
    void handleCancelClick(ActionEvent event) {

		dialog.close();
    }

    @FXML
    void handleDeliverClick(ActionEvent event) {
    	User user = LoginManager.getActiveUser();
    	String filepath = filenameTextField.getText();
    	Timestamp time = Timestamp.valueOf(Instant.now().toString().replaceFirst("T", " ").substring(0, 19));
    	String extension = "";
    	int dotIndex = filepath.lastIndexOf('.');
    	if (dotIndex > 0) {
    	    extension = filepath.substring(dotIndex+1);
    	}
    	if(!extension.equals("pdf")) {
    		System.err.printf("Only pdf's should be uploaded, your file is of type '%s'.", extension);
    	}
    	submission = new Submission(assignment, user, time, -1, null);
    	SubmissionManager.addSubmission(submission, filepath);
    	assignmentController.update();
    	dialog.close();
    }

    @FXML
    void handleBrowseClick(ActionEvent event) {
		Stage mainStage = (Stage) dialog.getScene().getWindow();
		System.out.println("Open file clicked");

		// Construct file chooser
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Data File");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("PDF file (*.pdf)", "*.pdf"));

		// Launch file chooser and retrive selected file
		File file = fileChooser.showOpenDialog(mainStage);

		// Break if no file was selected
		if (file == null)
			return;
			
		filenameTextField.setText(file.getAbsolutePath());
    }	
	
    @FXML
    void handleAssignmentDownloadButton(ActionEvent event) {
    	System.out.println("'handleAssignmentDownloadButton' not implemented.");
    	
    }
    
    @FXML
    void handleSubmissionDownloadButton(ActionEvent event) {
    	System.out.println("'handleSubmissionDownloadButton' not implemented.");
    	
    }

}
