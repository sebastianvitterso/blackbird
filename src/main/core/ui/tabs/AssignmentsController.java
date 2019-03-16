package main.core.ui.tabs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import main.app.Loader;
import main.core.ui.MainController;
import main.core.ui.MenuController;
import main.core.ui.components.AssignmentBoxController;
import main.core.ui.popups.AssignmentPopupController;
import main.db.AssignmentManager;
import main.db.LoginManager;
import main.db.SubmissionManager;
import main.models.Assignment;
import main.models.Course;
import main.models.Submission;
import main.models.User;
import main.utils.PostInitialize;
import main.utils.Refreshable;
import main.utils.Role;
import main.utils.View;

public class AssignmentsController implements Refreshable {
	@FXML private StackPane rootPane;
	@FXML private VBox assignmentVBox;
	@FXML private JFXButton newAssignmentButton;

	private MainController mainController;
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
    	mainController = Loader.getController(View.MAIN_VIEW);
		menuController = Loader.getController(View.MENU_VIEW);
		assignmentController = Loader.getController(View.POPUP_ASSIGNMENT_VIEW);
    }
	
    /**
	 * Updates every component in the user interface.
	 */
	@Override
	public void update() {
		updateAssignments();
		updateContainerMapping();
		updateDisplayedContainers();
	}

	/**
	 * Updates the assignment and submission containers, fetching updates from database.
	 */
	private void updateAssignments() {
		Course course = menuController.getSelectedCourse();
		assignments = AssignmentManager.getAssignmentsFromCourse(course);
		submissions = SubmissionManager.getSubmissionsFromCourseAndUser(course, LoginManager.getActiveUser());
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
			Submission submission = submissions.stream()
					.filter(sub -> sub.getAssignment().getAssignmentID() == assignment.getAssignmentID())
					.findFirst()
					.orElse(null);
			controller.loadSubmission(submission);
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
		
		// Append 'new assignment' button if logged in user is a professor
		if (menuController.getSelectedRole() == Role.PROFESSOR)
			assignmentVBox.getChildren().add(newAssignmentButton);
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
    	JFXDialog dialog = new JFXDialog(mainController.getOuterStackPane(), (Region) Loader.getParent(View.POPUP_ASSIGNMENT_VIEW), DialogTransition.CENTER);
    	
    	// Initialize popup
    	assignmentController.clear();
    	assignmentController.connectDialog(dialog);
    	dialog.show();
	}
	
	public static void main(String[] args) {
		ObservableList<User> users = FXCollections.observableArrayList();
		users.addListener((ListChangeListener.Change<? extends User> change) -> {
			System.out.println(change);
		});
		 User user = new User("patkj", "sad", "sdsd", "sdasd", "dssd");
		 users.add(user);
		 System.out.println("Changing name");
		 user.setFirstName("ola nor");
	}
}
