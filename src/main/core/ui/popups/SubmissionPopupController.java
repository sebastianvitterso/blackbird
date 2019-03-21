package main.core.ui.popups;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.Instant;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
import main.utils.Role;
import main.utils.Status;
import main.utils.View;

/**
 * @author Beatrice
 *
 */
public class SubmissionPopupController implements Refreshable {
    @FXML private StackPane rootPane;
    @FXML private StackPane submissionGradingPane;
    @FXML private HBox outerHBox;

    @FXML private HBox submissionListHBox;
    @FXML private JFXListView<?> submissionListView;

    @FXML private Label assignmentTitleLabel;
    @FXML private Label assignmentDeadlineLabel;
    @FXML private Label assignmentMaxScoreLabel;
    @FXML private Label assignmentPassingScoreLabel;
    @FXML private Label assignmentDescriptionLabel;
    @FXML private JFXButton assignmentFileLinkButton;
    
    @FXML private VBox submissionVBox;
    @FXML private HBox submissionLowerHBox;
    @FXML private Label submissionStatusLabel;
    @FXML private Label submissionScoreLabel;
    @FXML private Label submissionCommentLabel;
    @FXML private HBox submissionFileUploadHBox;
    @FXML private JFXTextField submissionFileTextField;
    @FXML private JFXButton submissionFileLinkButton;
    @FXML private JFXButton submissionDeliverButton;
    
    @FXML private VBox gradingVBox;
    @FXML private Label gradingStatusLabel;
    @FXML private JFXTextField gradingScoreTextField;
    @FXML private Label gradingMaxScoreLabel;
    @FXML private JFXTextArea gradingCommentTextArea;
    @FXML private JFXButton gradingEvaluateButton;
    @FXML private JFXButton gradingFileLinkButton;

    private Assignment assignment;
    private Submission submission;
    private String assignmentFileName;
    private String submissionFileName;
    private JFXDialog dialog;
    private AssignmentsController assignmentsController; 
    private File selectedFile;
    private User selectedUser;
    
    @FXML
    private void initialize() {
    }
    
    @Override
    public void clear() {
    	// Set visibility
    	submissionFileUploadHBox.setVisible(true);
    	submissionScoreLabel.setVisible(true);
    	submissionCommentLabel.setVisible(true);
    	submissionFileLinkButton.setVisible(true);
    	gradingFileLinkButton.setVisible(true);
    	
    	// Clear data
    	submissionCommentLabel.setText("");
    	
    	// Grading
    	gradingCommentTextArea.clear();
    	gradingScoreTextField.setText("-");
    	gradingMaxScoreLabel.setText("/ -");
    	gradingStatusLabel.setText("");
    	
    	submissionGradingPane.getChildren().setAll(submissionVBox, gradingVBox);
    	
    	if (!outerHBox.getChildren().contains(submissionListHBox))
    		outerHBox.getChildren().add(0, submissionListHBox);
    	submissionListView.getItems().clear();
    	
    	
    }

	/**
	 * Runs any methods that require every controller to be initialized. This method
	 * should only be invoked by the FXML Loader class.
	 */
	@PostInitialize
	private void postInitialize() {
		assignmentsController = Loader.getController(View.ASSIGNMENTS_VIEW);
	}

//	/**
//	 * Connects this controller to associated JFXDialog.
//	 */
	public void connectDialog(JFXDialog dialog) {
		this.dialog = dialog;
	}
	
	public void load(Assignment assignment, Submission submission, Role role) {
		this.assignment = assignment;
		assignmentTitleLabel.setText(assignment.getTitle());
		assignmentDescriptionLabel.setText(assignment.getDescription());
		assignmentDeadlineLabel.setText(String.valueOf(assignment.getDeadLine()));
		assignmentMaxScoreLabel.setText(String.valueOf(assignment.getMaxScore()));
		assignmentPassingScoreLabel.setText(String.valueOf(assignment.getPassingScore()));
		assignmentFileName = String.format("oppgaver-%s-%s.pdf", assignment.getCourse().getCourseCode(), assignment.getTitle());
		assignmentFileLinkButton.setText(assignmentFileName);
		
		this.submission = submission;
		Status status = Assignment.determineStatus(assignment, this.submission);
		submissionStatusLabel.setText(status.getNorwegianName());
		gradingStatusLabel.setText(status.getNorwegianName());
		
		switch (role) {
		case PROFESSOR:
		case ASSISTANT:
			submissionGradingPane.getChildren().remove(submissionVBox);
			switch(status){
			case PASSED:
				gradingStatusLabel.setText(status.getNorwegianName());
				gradingScoreTextField.setText(String.valueOf(submission.getScore()));
				gradingMaxScoreLabel.setText(String.format("/ %s", assignment.getMaxScore()));
				gradingCommentTextArea.setText(submission.getComment());
				submissionFileName = String.format("innlevering-%s-%s-%s.pdf", 
						assignment.getCourse().getCourseCode(), 
						assignment.getTitle(), 
						submission.getUser().getUsername());
				gradingFileLinkButton.setText(submissionFileName);
				break;
			case WAITING:
				gradingStatusLabel.setText(status.getNorwegianName());
				gradingScoreTextField.setText("-");
				gradingMaxScoreLabel.setText(String.format("/ %s", assignment.getMaxScore()));
				submissionFileName = String.format("innlevering-%s-%s-%s.pdf", 
						assignment.getCourse().getCourseCode(), 
						assignment.getTitle(), 
						submission.getUser().getUsername());
				gradingFileLinkButton.setText(submissionFileName);
				break;
			case FAILED:
				gradingStatusLabel.setText(status.getNorwegianName());
				gradingScoreTextField.setText(String.valueOf(submission.getScore()));
				gradingMaxScoreLabel.setText(String.format("/ %s", assignment.getMaxScore()));
				gradingCommentTextArea.setText(submission.getComment());
				submissionFileName = String.format("innlevering-%s-%s-%s.pdf", 
						assignment.getCourse().getCourseCode(), 
						assignment.getTitle(), 
						submission.getUser().getUsername());
				gradingFileLinkButton.setText(submissionFileName);
				break;
			case NOT_DELIVERED:
				throw(new IllegalStateException("Should not get PROFESSOR/ASSISTANT and NOT_DELIVERED at the same time."));
			case DEADLINE_EXCEEDED:
				throw(new IllegalStateException("Should not get PROFESSOR/ASSISTANT and DEADLINE_EXCEEDED at the same time."));
			default:
				break;
			}
			
			break;
			
			
		case STUDENT:
			outerHBox.getChildren().remove(submissionListHBox);
			submissionGradingPane.getChildren().remove(gradingVBox);
			
			
			switch(status){
			case PASSED:
				submissionScoreLabel.setText(String.format("%s / %s", submission.getScore(), assignment.getMaxScore()));
				submissionCommentLabel.setText(submission.getComment());
				submissionFileUploadHBox.setVisible(false);
				submissionFileName = String.format("innlevering-%s-%s-%s.pdf", 
						assignment.getCourse().getCourseCode(), 
						assignment.getTitle(), 
						submission.getUser().getUsername());
				submissionFileLinkButton.setText(submissionFileName);
				submissionLowerHBox.getChildren().remove(submissionDeliverButton);
				break;
			case WAITING:
				submissionScoreLabel.setText(String.format("%s / %s", "-", assignment.getMaxScore()));
				submissionCommentLabel.setVisible(false);
				submissionFileUploadHBox.setVisible(false);
				submissionFileName = String.format("innlevering-%s-%s-%s.pdf", 
						assignment.getCourse().getCourseCode(), 
						assignment.getTitle(), 
						submission.getUser().getUsername());
				submissionFileLinkButton.setText(submissionFileName);
				submissionLowerHBox.getChildren().remove(submissionDeliverButton);
				break;
			case FAILED:
				submissionScoreLabel.setText(String.format("%s / %s", submission.getScore(), assignment.getMaxScore()));
				submissionCommentLabel.setText(submission.getComment());
				submissionFileUploadHBox.setVisible(false);
				submissionFileName = String.format("innlevering-%s-%s-%s.pdf", 
						assignment.getCourse().getCourseCode(), 
						assignment.getTitle(), 
						submission.getUser().getUsername());
				submissionFileLinkButton.setText(submissionFileName);
				submissionLowerHBox.getChildren().remove(submissionDeliverButton);
				break;
			case NOT_DELIVERED:
				submissionScoreLabel.setText(String.format("%s / %s", "-", assignment.getMaxScore()));
				submissionCommentLabel.setVisible(false);
				submissionFileLinkButton.setVisible(false);
				if(!submissionLowerHBox.getChildren().contains(submissionDeliverButton)) {
					submissionLowerHBox.getChildren().add(submissionDeliverButton);
				}
				break;
			case DEADLINE_EXCEEDED:
				submissionScoreLabel.setText(String.format("%s / %s", "0", assignment.getMaxScore()));
				submissionCommentLabel.setText("Øvingen ble ikke levert innen tidsfristen.");
				submissionFileUploadHBox.setVisible(false);
				submissionFileLinkButton.setVisible(false);
				submissionLowerHBox.getChildren().remove(submissionDeliverButton);
				break;
			default:
				break;
			}
			
			break;

		default:
			break;
		}
		
		loadSubmission(submission);
	}
	
	
	public void loadAssignment(Assignment assignment) {
		
	}
	
	public void loadSubmission(Submission submission) {
		
		
		
	}
	

	@FXML
    void handleCancelClick(ActionEvent event) {

		dialog.close();
    }

    @FXML
    void handleDeliverClick(ActionEvent event) {
    	User user = LoginManager.getActiveUser();
    	File file  = selectedFile;
    	Timestamp time = Timestamp.from(Instant.now());
    	submission = new Submission(assignment, user, time, -1, null);
    	SubmissionManager.addSubmission(submission, file);
    	assignmentsController.update();
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
		submissionFileTextField.setText(selectedFile != null ? selectedFile.getName() : "");
    }	
	
    @FXML
    void handleAssignmentDownloadClick(ActionEvent event) {
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
    	
		try (FileOutputStream fos = new FileOutputStream(file)) {
			fos.write(is.readAllBytes());
			Desktop.getDesktop().open(file);
		} catch (IOException e) {
			e.printStackTrace();
		} 
    }
    
    @FXML
    void handleSubmissionDownloadClick(ActionEvent event) {
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
    	
    	try (FileOutputStream fos = new FileOutputStream(file)) {
			fos.write(is.readAllBytes());
			Desktop.getDesktop().open(file);
		} catch (IOException e) {
			e.printStackTrace();
		} 
    }

    @FXML
    void handleEvaluateClick(ActionEvent event) {
    	String comment = gradingCommentTextArea.getText();
    	int score = Integer.valueOf(gradingScoreTextField.getText());
    	SubmissionManager.gradeSubmission(submission, score, comment);
    	assignmentsController.update();
    	dialog.close();
    }

}
