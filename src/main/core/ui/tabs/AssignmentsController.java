package main.core.ui.tabs;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import main.app.Loader;
import main.core.ui.MenuController;
import main.core.ui.components.AssignmentBoxController;
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
	private MenuController menuController;
	private Map<Assignment, FXMLLoader> mapToLoaders = new HashMap<>();
	private Set<Assignment> assignments;
	private Set<Submission> submissions;
	@FXML private VBox assignmentVBox;
	
	
	@FXML
	private void initialize() {
//		assignmentVBox.getChildren().clear();
	}
	
	/**
     * Runs any methods that require every controller to be initialized.
     * This method should only be invoked by the FXML Loader class.
     */
    @PostInitialize
    private void postInitialize() {
		menuController = Loader.getController(View.MENU_VIEW);
    }
	
	@Override
	public void update() {
		updateAssignments();
	}
	
	private void updateAssignments() {
		Course course = menuController.getSelectedCourse();
		assignments = new HashSet<>(AssignmentManager.getAssignmentsFromCourse(course));
		submissions = new HashSet<>(SubmissionManager.getSubmissionsFromCourseAndUser(course, LoginManager.getActiveUser()));
		
		// Create mapping for FXML loader
		for (Assignment assignment : assignments) {
			// Create loader
			URL pathToFXML = getClass().getClassLoader().getResource(View.ASSIGNMENT_BOX.getPathToFXML());
			FXMLLoader loader = new FXMLLoader(pathToFXML);
			try {
				loader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mapToLoaders.put(assignment, loader);
			
			// Configure assignment container
			AssignmentBoxController controller = loader.getController();
			controller.loadAssignment(assignment);
			
			// Determine assignment status
			Submission submission = submissions.stream()
					.filter(sub -> sub.getAssignment().getAssignmentID() == assignment.getAssignmentID())
					.findFirst()
					.orElse(null);
			controller.loadStatus(Assignment.determineStatus(assignment, submission));
		}
		
		// Inject assignment container into VBox
		List<StackPane> parents = mapToLoaders.values().stream()
				.map(loader -> (StackPane) loader.getRoot())
				.collect(Collectors.toList());
		assignmentVBox.getChildren().clear();
		assignmentVBox.getChildren().setAll(parents);
	}

	@Override
	public void clear() {
		assignmentVBox.getChildren().clear();
	}
	
	
}
