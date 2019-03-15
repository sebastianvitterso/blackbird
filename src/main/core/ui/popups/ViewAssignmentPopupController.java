package main.core.ui.popups;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import main.models.Assignment;
import main.models.Submission;
import main.utils.PostInitialize;
import main.utils.Refreshable;
import main.utils.Status;

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
    private Submission submission;
    
    
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

	public void loadAssignment(Assignment assignment) {
		this.assignment = assignment;
		headerLabel.setText(assignment.getTitle());
		descriptionLabel.setText(assignment.getDescription());
		deadlineLabel.setText(String.format("Tidsfrist: %s", assignment.getDeadLine()));
		maxScoreLabel.setText(String.format("Maks: %s poeng", assignment.getMaxScore()));
		passingScoreLabel.setText(String.format("Krav: %s poeng", assignment.getPassingScore()));
		//TODO: Normalizer.normalize ? Bør oppgavene kunne hete "oppgaver-TDT4140-Øving 0"? Bør vi fikse æøå?
		assignmentFileLinkButton.setText(String.format("oppgaver-%s-%s.pdf", assignment.getCourse().getCourseCode(), assignment.getTitle()));
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
			submissionFileLinkButton.setText(String.format("innlevering-%s-%s-%s.pdf", 
					assignment.getCourse().getCourseCode(), assignment.getTitle(), submission.getUser().getUsername()));
			deliverButton.setVisible(false);
			break;
		case WAITING:
			statusLabel.setText("Status: Venter på godkjenning"); 
			scoreLabel.setVisible(false);
			commentLabel.setVisible(false);
			commentLabel.setText("");
			fileUploadHBox.setVisible(false);
			submissionFileLinkButton.setVisible(true);
			submissionFileLinkButton.setText(String.format("innlevering-%s-%s-%s.pdf", 
					assignment.getCourse().getCourseCode(), assignment.getTitle(), submission.getUser().getUsername()));
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
			submissionFileLinkButton.setText(String.format("innlevering-%s-%s-%s.pdf", 
					assignment.getCourse().getCourseCode(), assignment.getTitle(), submission.getUser().getUsername()));
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
    	System.out.println("'handleDeliverClick' not implemented.");
    	
    	dialog.close();
    }

    @FXML
    void handleBrowseClick(ActionEvent event) {
    	System.out.println("'handleBrowseClick' not implemented.");

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
