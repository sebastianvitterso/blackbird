package main.core.ui.popups;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import main.app.Loader;
import main.core.ui.tabs.AssignmentsController;
import main.db.AssignmentManager;
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
    @FXML private HBox lowerHBox;
    @FXML private JFXTextField fileTextField;
    @FXML private Label submissionFileLinkLabel;
    @FXML private JFXButton cancelButton;
    @FXML private JFXButton deliverButton;
    @FXML private JFXButton assignmentFileLinkButton;
    @FXML private JFXButton submissionFileLinkButton;
    
    private Assignment assignment;
    private String assignmentFileName;
    private Submission submission;
    private String submissionFileName;
    private JFXDialog dialog;
    private AssignmentsController assignmentController; 
    private File selectedFile;
    
    @FXML
    private void initialize() {
    }
    
    @Override
    public void clear() {
    }

	/**
	 * Runs any methods that require every controller to be initialized. This method
	 * should only be invoked by the FXML Loader class.
	 */
	@PostInitialize
	private void postInitialize() {
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
			lowerHBox.getChildren().remove(deliverButton);
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
			lowerHBox.getChildren().remove(deliverButton);
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
			lowerHBox.getChildren().remove(deliverButton);
			break;
		case NOT_DELIVERED:
			statusLabel.setText("Status: Ikke levert"); 
			scoreLabel.setVisible(false);
			commentLabel.setVisible(false);
			commentLabel.setText("");
			fileUploadHBox.setVisible(true);
			submissionFileLinkButton.setVisible(false);
			if(!lowerHBox.getChildren().contains(deliverButton)) {
				lowerHBox.getChildren().add(deliverButton);
			}
			break;
		case DEADLINE_EXCEEDED:
			statusLabel.setText("Status: For seint, lulz"); 
			scoreLabel.setVisible(false);
			commentLabel.setVisible(true);
			commentLabel.setText("Øvingen ble ikke levert innen tidsfristen.");
			fileUploadHBox.setVisible(false);
			submissionFileLinkButton.setVisible(false);
			lowerHBox.getChildren().remove(deliverButton);
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
    	File file  = selectedFile;
    	Timestamp time = Timestamp.valueOf(Instant.now().toString().replaceFirst("T", " ").substring(0, 19));
    	submission = new Submission(assignment, user, time, -1, null);
    	SubmissionManager.addSubmission(submission, file);
    	assignmentController.update();
    	dialog.close();
    }

    @FXML
    void handleBrowseClick(ActionEvent event) {
		Stage mainStage = (Stage) dialog.getScene().getWindow();
		System.out.println("Open file clicked");

		// Construct file chooser
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Velg fil");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("PDF file (*.pdf)", "*.pdf"));

		// Launch file chooser and retrive selected file
		selectedFile = fileChooser.showOpenDialog(mainStage);

		// Update displayed text
		fileTextField.setText(selectedFile != null ? selectedFile.getName() : "");
    }	
	
    @FXML
    void handleAssignmentDownloadButton(ActionEvent event) {
    	Stage mainStage = (Stage) dialog.getScene().getWindow();
    	InputStream is = AssignmentManager.getInputStreamFromAssignment(assignment);
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Lagre som");
    	fileChooser.getExtensionFilters().add(new ExtensionFilter("PDF file (*.pdf)", "*.pdf"));
    	fileChooser.setInitialFileName(assignmentFileName);
    	File file = fileChooser.showSaveDialog(mainStage);
    	
    	// Break if no file was selected
		if (file == null)
			return;
    	
    	FileOutputStream fos = null;
    	try {
			fos = new FileOutputStream(file);
			fos.write(is.readAllBytes());
			Desktop.getDesktop().open(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally { // I love making horrible code, you're welcome.
			try {fos.close();} catch (IOException e) {e.printStackTrace();}
		}
    }
    
    @FXML
    void handleSubmissionDownloadButton(ActionEvent event) {
    	Stage mainStage = (Stage) dialog.getScene().getWindow();
    	InputStream is = SubmissionManager.getInputStreamFromSubmission(submission);
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Lagre som");
    	fileChooser.getExtensionFilters().add(new ExtensionFilter("PDF file (*.pdf)", "*.pdf"));
    	fileChooser.setInitialFileName(submissionFileName);
    	File file = fileChooser.showSaveDialog(mainStage);
    	
    	// Break if no file was selected
		if (file == null)
			return;
    	
    	FileOutputStream fos = null;
    	try {
			fos = new FileOutputStream(file);
			fos.write(is.readAllBytes());
			Desktop.getDesktop().open(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally { // I love making horrible code, you're welcome. 
			try {fos.close();} catch (IOException e) {e.printStackTrace();}
		}
    }

}
