package main.core.ui.popups;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
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
    @FXML private JFXListView<Submission> submissionListView;

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
    	
    	if(!submissionLowerHBox.getChildren().contains(submissionDeliverButton)) {
			submissionLowerHBox.getChildren().add(submissionDeliverButton);
		}
    	
    }

	/**
	 * Runs any methods that require every controller to be initialized. This method
	 * should only be invoked by the FXML Loader class.
	 */
	@PostInitialize
	private void postInitialize() {
		assignmentsController = Loader.getController(View.ASSIGNMENTS_VIEW);
	}

	/**
	 * Connects this controller to associated JFXDialog.
	 */
	public void connectDialog(JFXDialog dialog) {
		this.dialog = dialog;
	}
	
	public void load(Assignment assignment, Submission submission, Role role) {
		loadAssignment(assignment);
		
		this.submission = submission;
		Status status = Submission.determineStatus(assignment, this.submission);
		submissionStatusLabel.setText(status.getNorwegianName());
		gradingStatusLabel.setText(status.getNorwegianName());
		
		switch (role) {
		case PROFESSOR: case ASSISTANT:
			List<Submission> submissions = SubmissionManager.getSubmissionsFromAssignment(assignment);
			System.out.format("Submissions: %s%n", submissions);
			submissionListView.getItems().setAll(submissions);
			submissionListView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
				onSelectedSubmissionChange(newValue);
			});
			submissionListView.setCellFactory(new Callback<ListView<Submission>, ListCell<Submission>>() {
				
				@Override
				public ListCell<Submission> call(ListView<Submission> param) {
					ListCell<Submission> cell = new ListCell<Submission>() {
						@Override
						protected void updateItem(Submission submission, boolean empty) {
							super.updateItem(submission, empty);
							if (submission != null)
								setText(submission.getUser().getName());
							else
								setText(null);
						}
					};
					return cell;
				}
			});
			submissionGradingPane.getChildren().remove(submissionVBox);
			gradingVBox.setVisible(false);
			break;

		case STUDENT:
			outerHBox.getChildren().remove(submissionListHBox);
			submissionGradingPane.getChildren().remove(gradingVBox);
			switch(status){
			case PASSED:
				updateSubmissionViewIfDelivered(submission, String.valueOf(submission.getScore()), submission.getComment());
				break;
			case WAITING:
				updateSubmissionViewIfDelivered(submission, "-", "");
				break;
			case FAILED:
				updateSubmissionViewIfDelivered(submission, String.valueOf(submission.getScore()), submission.getComment());
				break;
			case NOT_DELIVERED:
				updateSubmissionViewIfNotDelivered("-", "", true);
				break;
			case DEADLINE_EXCEEDED:
				updateSubmissionViewIfNotDelivered("0", "Øvingen ble ikke levert innen tidsfristen.", false);
				break;
			}
		}
	}

	private void updateSubmissionViewIfDelivered(Submission submission, String submissionScore, String submissionComment) {
		submissionScoreLabel.setText(String.format("%s / %s", submissionScore, assignment.getMaxScore()));
		submissionCommentLabel.setText(submissionComment);
		submissionFileUploadHBox.setVisible(false);
		submissionFileName = String.format("innlevering-%s-%s-%s.pdf", 
				assignment.getCourse().getCourseCode(), 
				assignment.getTitle(), 
				submission.getUser().getUsername());
		submissionFileLinkButton.setText(submissionFileName);
		submissionLowerHBox.getChildren().remove(submissionDeliverButton);
	}

	private void updateSubmissionViewIfNotDelivered(String submissionScore, String submissionComment, boolean showDelivery) {
		submissionScoreLabel.setText(String.format("%s / %s", submissionScore, assignment.getMaxScore()));
		submissionCommentLabel.setText(submissionComment);
		submissionFileLinkButton.setVisible(false);
		submissionFileUploadHBox.setVisible(showDelivery);
		if (!showDelivery  &&  submissionLowerHBox.getChildren().contains(submissionDeliverButton))
			submissionLowerHBox.getChildren().remove(submissionDeliverButton);
	}
	
	private void onSelectedSubmissionChange(Submission submission) {
		gradingVBox.setVisible(true);
		Status status = Submission.determineStatus(assignment, submission);
		switch(status){
		case PASSED:
			updateGradingView(submission, String.valueOf(submission.getScore()), submission.getComment());
			break;
		case WAITING:
			updateGradingView(submission, "", "");
			break;
		case FAILED:
			updateGradingView(submission,String.valueOf(submission.getScore()), submission.getComment());
			break;
		case NOT_DELIVERED:
			throw new IllegalStateException("Should not get PROFESSOR/ASSISTANT and NOT_DELIVERED at the same time.");
		case DEADLINE_EXCEEDED:
			throw new IllegalStateException("Should not get PROFESSOR/ASSISTANT and DEADLINE_EXCEEDED at the same time.");
		default:
			break;
		}
	}
	
	private void updateGradingView(Submission submission, String gradingScore, String gradingComment) {
		gradingStatusLabel.setText(Submission.determineStatus(assignment, submission).getNorwegianName());  
		gradingScoreTextField.setText(gradingScore);
		gradingMaxScoreLabel.setText(String.format("/ %s", assignment.getMaxScore()));
		gradingCommentTextArea.setText(gradingComment);
		submissionFileName = String.format("innlevering-%s-%s-%s.pdf", 
				assignment.getCourse().getCourseCode(), 
				assignment.getTitle(), 
				submission.getUser().getUsername());
		gradingFileLinkButton.setText(submissionFileName);
	}

	private void loadAssignment(Assignment assignment) {
		this.assignment = assignment;
		assignmentTitleLabel.setText(assignment.getTitle());
		assignmentDescriptionLabel.setText(assignment.getDescription());
		assignmentDeadlineLabel.setText(String.valueOf(assignment.getDeadLine()));
		assignmentMaxScoreLabel.setText(String.valueOf(assignment.getMaxScore()));
		assignmentPassingScoreLabel.setText(String.valueOf(assignment.getPassingScore()));
		assignmentFileName = String.format("oppgaver-%s-%s.pdf", assignment.getCourse().getCourseCode(), assignment.getTitle());
		assignmentFileLinkButton.setText(assignmentFileName);
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
