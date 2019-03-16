package main.core.ui.tabs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import main.announcement.AnnouncementsView;
import main.app.Loader;
import main.core.ui.MainController;
import main.core.ui.MenuController;
import main.core.ui.popups.AnnouncementPopupController;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import main.app.Loader;
import main.core.ui.MainController;
import main.core.ui.MenuController;
import main.core.ui.popups.ViewAssignmentPopupController;
import main.db.AssignmentManager;
import main.db.LoginManager;
import main.db.SubmissionManager;
import main.models.Assignment;
import main.models.Course;
import main.models.Submission;
import main.utils.PostInitialize;
import main.utils.Refreshable;
import main.utils.Status;
import main.utils.View;

import main.utils.Role;

public class OverviewController implements Refreshable {

	private MainController mainController;
	private MenuController menuController;
	private AnnouncementPopupController announcementController;
	
	@FXML private StackPane rootPane;
	@FXML private Label courseDescriptionLabel;
	@FXML private VBox announcementVBox;
	@FXML private ScrollPane announcementScrollPane;
	@FXML private JFXButton newAnnouncementButton;
	@FXML private VBox assignmentVBox;
	
	
	@FXML
	private void initialize() {
		MainController.customScrolling(announcementScrollPane, announcementScrollPane.vvalueProperty(), bounds -> bounds.getHeight());
	}
	
	/**
     * Runs any methods that require every controller to be initialized.
     * This method should only be invoked by the FXML Loader class.
     */
	@PostInitialize
    private void postInitialize() {
		mainController = Loader.getController(View.MAIN_VIEW);
		menuController = Loader.getController(View.MENU_VIEW);
		announcementController = Loader.getController(View.POPUP_ANNOUNCEMENT_VIEW);
    }
	
    @FXML
    void handleNewAnnouncementClick(ActionEvent event) {
    	// Create dialog box
    	JFXDialog dialog = new JFXDialog(rootPane, (Region) Loader.getParent(View.POPUP_ANNOUNCEMENT_VIEW), DialogTransition.CENTER);
    	
    	// Initialize popup
    	announcementController.clear();
    	announcementController.connectDialog(dialog);
    	dialog.show();
    }
	
	@Override
	public void update() {
		Course selectedCourse = menuController.getSelectedCourse();
		if (selectedCourse != null)
			courseDescriptionLabel.setText(selectedCourse.getDescription());
		//OBS: Denne loades inn to ganger samtidig. En av dem bør fjernes for opptimalisering
		announcementVBox.getChildren().clear();
		announcementVBox.getChildren().add(new AnnouncementsView(selectedCourse).getView());
		if(menuController.getSelectedRole() == Role.PROFESSOR)
			newAnnouncementButton.setVisible(true);
		else
			newAnnouncementButton.setVisible(false);
		updateAssignmentButtons();
	}
	
	
	
	private void updateAssignmentButtons() {
		Course course = menuController.getSelectedCourse();
		List<Assignment> assignments = AssignmentManager.getAssignmentsFromCourse(course);
		List<Submission> submissions = SubmissionManager.getSubmissionsFromCourseAndUser(course, LoginManager.getActiveUser());

		List<AssignmentButton> buttons = new ArrayList<>();
		for (Assignment assignment : assignments) {
			Submission submission = submissions.stream()
					.filter(sub -> sub.getAssignment().getAssignmentID() == assignment.getAssignmentID())
					.findFirst()
					.orElse(null);
			AssignmentButton button = new AssignmentButton(assignment, submission);
			button.setOnAction(event -> {
				menuController.loadTab(View.ASSIGNMENTS_VIEW);
				ViewAssignmentPopupController popupController = Loader.getController(View.POPUP_VIEW_ASSIGNMENT_VIEW);
				JFXDialog dialog = new JFXDialog(mainController.getOuterStackPane(), (Region) Loader.getParent(View.POPUP_VIEW_ASSIGNMENT_VIEW), DialogTransition.CENTER);
				popupController.clear();
				popupController.connectDialog(dialog);
				popupController.loadAssignment(assignment);
				popupController.loadSubmission(submission);
				Loader.getParent(View.MENU_VIEW).addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
					dialog.close();
					e.consume();
				});
				dialog.show();	
			});
			buttons.add(button);
		}
		
		assignmentVBox.getChildren().setAll(buttons);
	}



	@Override
	public void clear() {
		
	}
	
	
	private class AssignmentButton extends JFXButton {
		private Assignment assignment;
		private Submission submission;
		
		
		public AssignmentButton(Assignment assignment, Submission submission) {
			this.assignment = assignment;
			this.submission = submission;
			initialize();
		}
		
		private void initialize() {
			// Set layout bounds
			setPrefWidth(280);
			setPrefHeight(40);
			
			// Set styling
			getStyleClass().addAll("button-text", "border");
			setButtonType(ButtonType.RAISED);
			setAlignment(Pos.CENTER_LEFT);
			Status status = Assignment.determineStatus(assignment, submission);
			switch (status) {
			case PASSED:
				getStyleClass().add("button-green");
				break;
			case WAITING:
				getStyleClass().add("button-orange");
				break;
			case FAILED:
				getStyleClass().add("button-red");
				break;
			case DEADLINE_EXCEEDED:
				getStyleClass().add("button-red");
				break;
			case NOT_DELIVERED:
				getStyleClass().add("button-default");
				break;
			default:
				break;
			}
			
			// Set text
			String formattedDeadline = new SimpleDateFormat("dd. MMM HH:mm").format(assignment.getDeadLine());
			setText(String.format("%s - %s", assignment.getTitle(), formattedDeadline));
		}
			
	}
}
