package main.core.ui.popups;

import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSpinner;

import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.StackPane;
import main.app.Loader;
import main.core.ui.AdminController;
import main.db.UserManager;
import main.models.Course;
import main.models.Course.Role;
import main.models.User;
import main.util.Clearable;
import main.util.PostInitialize;
import main.util.View;

public class UserSelectionPopupController implements Clearable {
	@FXML private StackPane rootPane;
	@FXML private Label titleLabel;
	@FXML private JFXListView<User> userSelectionListView;
	@FXML private JFXSpinner listViewSpinner;
	@FXML private JFXButton addSelectedButton;

	private JFXDialog dialog;
	private AdminController adminController;
	private Course course;
	private Role role;

	
	//// Initialization ////
	/**
	 * Initializes every component in the user interface. This method is
	 * automatically invoked when loading the corresponding FXML file.
	 */
	@FXML
	private void initialize() {
		// Allow multiselect
		userSelectionListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		// Bind 'add selected' button to being disabled when no entities are selected.
		BooleanBinding notSelected = userSelectionListView.getSelectionModel().selectedItemProperty().isNull();
		addSelectedButton.disableProperty().bind(notSelected);
	}

	/**
     * Connects this controller to associated JFXDialog.
     */
    public void connectDialog(JFXDialog dialog) {
    	this.dialog = dialog;
    }
    
	/**
	 * Runs any methods that require every controller to be initialized. This method
	 * should only be invoked by the FXML Loader class.
	 */
	@PostInitialize
	public void postInitialize() {
		adminController = Loader.getController(View.ADMIN_VIEW);
	}

	
	//// Loading ////
	/**
     * Prepares popup for displaying user selection for specified role.
     * This method must be called prior to displaying the dialog box.
     */
	public void loadUserSelection(String title, Course course, Role role) {
		titleLabel.setText(title);
		this.course = course;
		this.role = role;
		
//		DatabaseManager.submitRunnable(() -> {
			// Retrive list of addable users
			List<User> users = UserManager.getUsersExcludingRole(course, role);

			// Update visuals in FX Application thread
//			Platform.runLater(() -> {
				listViewSpinner.setVisible(false);
				userSelectionListView.getItems().setAll(users);
//			});
//		});
	}

	
	//// Overrides ////
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		userSelectionListView.getItems().clear();
		listViewSpinner.setVisible(true);
	}
	
	
	//// Event handlers ////
	@FXML
	void handleAddSelectedClick(ActionEvent event) {
//		DatabaseManager.submitRunnable(() -> {
			// Retrive selected users
			List<User> selectedUsers = userSelectionListView.getSelectionModel().getSelectedItems();

			// Add database entries for new user relations
			UserManager.addUsersToCourse(selectedUsers, course, role);

			// Update admin view and close dialog
//			Platform.runLater(() -> {
				adminController.updateViewByRole(role);
				dialog.close();
//			});
//		});
	}

}
