package main.core.ui.popups;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.app.Loader;
import main.core.ui.AdminController;
import main.core.ui.AdminController.RecursiveTreeUser;
import main.db.UserManager;
import main.models.User;
import main.util.Clearable;
import main.util.PostInitialize;
import main.util.View;

public class UserPopupController implements Clearable {
	@FXML private Label headerLabel;
	@FXML private JFXTextField firstNameTextField;
	@FXML private JFXTextField lastNameTextField;
	@FXML private JFXTextField emailTextField;
	@FXML private JFXTextField usernameTextField;
	@FXML private JFXTextField passwordTextField;
	@FXML private JFXButton registerButton;

	private boolean editMode;
	private AdminController adminController;
	private JFXDialog dialog;

	
	//// Initialization ////
	/**
	 * Initializes every component in the user interface. This method is
	 * automatically invoked when loading the corresponding FXML file.
	 */
	@FXML
	private void initialize() {
		// Bind 'register' button to being disabled when no username is given.
		registerButton.disableProperty().bind(usernameTextField.textProperty().isEmpty());
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
	 * Prepares popup for displaying 'edit user' mode. This method must be called
	 * prior to displaying the dialog box.
	 */
	public void loadEditUser(RecursiveTreeUser user) {
		// Layout configurations for 'edit' mode.
		editMode = true;
		headerLabel.setText("Rediger bruker");
		registerButton.setText("Rediger");

		// Add current user info
		firstNameTextField.setText(user.getFirstName());
		lastNameTextField.setText(user.getLastName());
		emailTextField.setText(user.getEmail());
		usernameTextField.setText(user.getUsername());
		passwordTextField.setText(user.getPassword());
		
		// Make sure primary key cannot be changed
		usernameTextField.setDisable(true);
	}

	/**
	 * Prepares popup for displaying 'new user' mode. This method must be called
	 * prior to displaying the dialog box.
	 */
	public void loadNewUser() {
		// Layout configurations for 'new' mode.
		editMode = false;
		headerLabel.setText("Registrer bruker");
		registerButton.setText("Registrer");
	}

	/**
	 * Reads input parameters, creating a new 'User' object.
	 */
	private User createUserFromInput() {
		String firstName = firstNameTextField.getText();
		String lastName = lastNameTextField.getText();
		String email = emailTextField.getText();
		String username = usernameTextField.getText();
		String password = passwordTextField.getText();
		return new User(username, password, firstName, lastName, email);
	}

	
	//// Overrides ////
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		firstNameTextField.clear();
		lastNameTextField.clear();
		emailTextField.clear();
		usernameTextField.clear();
		passwordTextField.clear();
		usernameTextField.setDisable(false);
	}

	
	//// Event handlers ////
	@FXML
	void handleRegisterClick(ActionEvent event) {
//		DatabaseManager.submitRunnable(() -> {
			User user = createUserFromInput();

			if (editMode) {
//				DatabaseManager.submitRunnable(() -> {
					UserManager.updateUser(user);
//				});
			} else {
//				DatabaseManager.submitRunnable(() -> {
					UserManager.addUser(user);
//				});
			}

//			Platform.runLater(() -> {
				adminController.updateUserView();
				dialog.close();
//			});
//		});
	}

}
