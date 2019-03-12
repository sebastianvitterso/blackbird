package main.core.ui.tabs;

import java.util.Map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import main.app.Loader;
import main.core.ui.MenuController;
import main.db.AssignmentManager;
import main.models.Assignment;
import main.models.Course;
import main.utils.PostInitialize;
import main.utils.Refreshable;
import main.utils.View;

public class AssignmentsController implements Refreshable {
	private MenuController menuController;
	private Map<Assignment, FXMLLoader> mapToLoaders;
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
	
//	private FXMLLoader createLoader(Assignment assignment) {
//		mapToLoaders.put(assignment, new FXMLLoader(getClass().getClassLoader().getResource(View.ASSIGNMENT_BOX.getPathToFXML())));
//	}
//	
	private void updateAssignments() {
		Course course = menuController.getSelectedCourse();
		AssignmentManager.getAssignmentsFromCourse(course);
	}

	@Override
	public void clear() {
	}
	
	
}
