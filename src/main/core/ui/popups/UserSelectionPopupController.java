package main.core.ui.popups;

import java.util.List;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSpinner;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import main.app.Loader;
import main.app.View;
import main.core.ui.AdminController;
import main.core.ui.Refreshable;
import main.data.Course;
import main.data.Course.Role;
import main.data.User;
import main.db.DatabaseManager;
import main.db.UserManager;

public class UserSelectionPopupController implements Refreshable {
	@FXML private StackPane rootPane;
    @FXML private Label titleLabel;
    @FXML private JFXListView<User> userSelectionListView;
    @FXML private JFXSpinner listViewSpinner;
    
    private JFXDialog dialog;
    private AdminController controller;
    private Course course;
    private Role role;
    
    
    public void configure(JFXDialog dialog, String title, Course course, Role role) {
    	controller = (AdminController) Loader.getController(View.ADMIN_VIEW);
    	titleLabel.setText(title);
    	this.dialog = dialog;
    	this.course = course;
    	this.role = role;
    	
    	listViewSpinner.setVisible(true);
    }
    
    public void fetchUsers() {
    	DatabaseManager.submitRunnable(() -> {
    		// Retrive list of addable users
    		List<User> users = UserManager.getUsersExcludingRole(course, role);

    		// Update visuals in FX Application thread
    		Platform.runLater(() -> {
    			listViewSpinner.setVisible(false);
    			userSelectionListView.getItems().setAll(users);
    		});
    	});
		
		
    }
    
    @Override
	public void refresh() {
		Task<Void> task = new Task<>() {
			@Override
			protected Void call() throws Exception {
				return null;
			}
		};
		
		task.setOnSucceeded(event -> {
			listViewSpinner.setVisible(false);
		});
	}
    
    @FXML
    void handleAddSelectedClick(ActionEvent event) {
    	// Retrive selected users
    	List<User> selectedUsers = userSelectionListView.getSelectionModel().getSelectedItems();
    	
    	// Add users to list in admin view
    	controller.addUsersToCourse(selectedUsers, course, role);
    	
    	// Close dialog
    	dialog.close();
    }
    
    
}
