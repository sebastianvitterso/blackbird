package main.core.ui.tabs;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import main.app.Loader;
import main.core.ui.MenuController;
import main.core.ui.components.AssignmentBoxController;
import main.core.ui.popups.AssignmentPopupController;
import main.db.AssignmentManager;
import main.db.LoginManager;
import main.db.SubmissionManager;
import main.models.Assignment;
import main.models.Course;
import main.models.Submission;
import main.utils.PostInitialize;
import main.utils.Refreshable;
import main.utils.View;

public class AssignmentsController implements Refreshable {
	@FXML private StackPane rootPane;
	@FXML private VBox assignmentVBox;

	private MenuController menuController;
	private AssignmentPopupController assignmentController; 
	private Map<FXMLLoader, Assignment> containers;
	private List<Assignment> assignments;
	private List<Submission> submissions;
	
	@FXML
	private void initialize() {
		containers = new HashMap<>();
		assignments = new ArrayList<>();
		submissions = new ArrayList<>();
	}
	
	/**
     * Runs any methods that require every controller to be initialized.
     * This method should only be invoked by the FXML Loader class.
     */
    @PostInitialize
    private void postInitialize() {
		menuController = Loader.getController(View.MENU_VIEW);
    }
	
    /**
	 * Updates every component in the user interface.
	 */
	@Override
	public void update() {
		System.out.println();
		System.out.println("\n-------- update start -------- ");
		System.out.println();

		Instant time1 = Instant.now();

		updateAssignments();
		Instant time2 = Instant.now();
		System.out.println("updateAssignments() = " + Duration.between(time1, time2).toString().replaceFirst("PT", ""));

		updateContainerMapping();
		Instant time3 = Instant.now();
		System.out.println("updateContainerMapping() = " + Duration.between(time2, time3).toString().replaceFirst("PT", ""));

		updateDisplayedContainers();
		Instant time4 = Instant.now();
		System.out.println("updateDisplayedContainers() = " + Duration.between(time3, time4).toString().replaceFirst("PT", ""));

		System.out.println("IN TOTAL = " + Duration.between(time1, time4).toString().replaceFirst("PT", ""));
		System.out.println();
		System.out.println("-------- update end -------- \n");
		
	}

	/**
	 * Updates the assignment and submission containers, fetching updates from database.
	 */
	private void updateAssignments() {
		System.out.println();
		System.out.println("-- updateAssignments start -- ");
		System.out.println();

		Instant time1 = Instant.now();
		
		Course course = menuController.getSelectedCourse();
		Instant time2 = Instant.now();
		System.out.format("getSelectedCourse() = %s%n%n", Duration.between(time1, time2).toString().replaceFirst("PT", ""));
		
		assignments = AssignmentManager.getAssignmentsFromCourse(course);
		Instant time3 = Instant.now();
		System.out.format("getAssignmentsFromCourse() = %s%n%n", Duration.between(time2, time3).toString().replaceFirst("PT", ""));

		submissions = SubmissionManager.getSubmissionsFromCourseAndUser(course, LoginManager.getActiveUser());
		Instant time4 = Instant.now();
		System.out.format("getSubmissionsFromCourseAndUser() = %s%n%n", Duration.between(time3, time4).toString().replaceFirst("PT", ""));
		
		System.out.println("IN TOTAL = " + Duration.between(time1, time4).toString().replaceFirst("PT", ""));
		System.out.println();
		System.out.println("-- updateAssignments end -- \n");
	}

	/**
	 * Updates the mapping between containers (AssignmentBox) and corresponding assignments.
	 */
	private void updateContainerMapping() {
		// Remove container mappings
		containers.entrySet().forEach(e -> e.setValue(null));
		
		// Assert that there exists a container for every assignment
		int diff = assignments.size() - containers.size();
		if (diff > 0)
			for (int i = 0; i < diff; i++) 
				containers.put(Loader.createFXMLLoader(View.ASSIGNMENT_BOX), null);
		
		// Pair up every assignment to a container (AssignmentBox)
		Iterator<Assignment> assignmentIterator = assignments.iterator();
		Iterator<Entry<FXMLLoader, Assignment>> containerIterator = containers.entrySet().iterator();
		while (assignmentIterator.hasNext()) {
			// Pair assignment to container
			Assignment assignment = assignmentIterator.next();
			Entry<FXMLLoader, Assignment> entry = containerIterator.next();
			entry.setValue(assignment);
			
			// Configure assignment container
			AssignmentBoxController controller = entry.getKey().getController();
			controller.loadAssignment(assignment);
			
			// Determine assignment status
			System.out.printf("%nSubmission pre filtering: %s%n", submissions.stream().map(Submission::toString).collect(Collectors.joining("\n\t", "\n\t", "")));
			Submission submission = submissions.stream()
					.filter(sub -> sub.getAssignment().getAssignmentID() == assignment.getAssignmentID())
					.findFirst()
					.orElse(null);
			System.out.printf("Found submission: %s for assignment %s%n",  submission, assignment);
			controller.loadStatus(Assignment.determineStatus(assignment, submission));
		}
	}

	/**
	 * Updates which assignments are displayed in the user interface.
	 */
	private void updateDisplayedContainers() {
		List<StackPane> parents = containers.entrySet().stream()
				.filter(entry -> entry.getValue() != null)
				.map(entry -> (StackPane) entry.getKey().getRoot())
				.collect(Collectors.toList());
		assignmentVBox.getChildren().setAll(parents);
	}
	
	@Override
	public void clear() {
		containers.entrySet().forEach(e -> e.setValue(null));
		assignmentVBox.getChildren().clear();
		assignments.clear();
		submissions.clear();
	}
	
	@FXML
	public void handleNewAssignmentClick(ActionEvent event)	{
		// Create dialog box
    	JFXDialog dialog = new JFXDialog(rootPane, (Region) Loader.getParent(View.POPUP_COURSE_VIEW), DialogTransition.CENTER);
    	
    	// Initialize popup
    	assignmentController.clear();
    	assignmentController.connectDialog(dialog);
    	assignmentController.loadNewExercise();
    	dialog.show();
	}
	
	
}
