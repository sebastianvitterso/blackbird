package main.core.ui;

import java.util.List;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import main.app.Loader;
import main.app.StageManager;
import main.app.View;

public class AdminController {
	@FXML private StackPane rootPane;
    @FXML private Label courseNameLabel;

    @FXML private JFXListView<?> courseListView;
    @FXML private JFXListView<?> professorListView;
    @FXML private JFXListView<?> assistantListView;
    @FXML private JFXListView<?> studentListView;
    
    @FXML private JFXTreeTableView<User> userTreeTableView;
    @FXML private TreeTableColumn<User, String> firstNameColumn;
    @FXML private TreeTableColumn<User, String> lastNameColumn;
    @FXML private TreeTableColumn<User, String> emailColumn;
    @FXML private TreeTableColumn<User, String> usernameColumn;
    @FXML private TreeTableColumn<User, String> passwordColumn;
    
    @FXML private JFXButton courseEditButton;
    @FXML private JFXButton courseDeleteButton;
    @FXML private JFXButton professorDeleteButton;
    @FXML private JFXButton assistantDeleteButton;
    @FXML private JFXButton studentDeleteButton;
    @FXML private JFXButton userEditButton;
    @FXML private JFXButton userDeleteButton;
    
    private ObservableList<User> users;
    

    @FXML
    void initialize() {
    	initializeCourseListView();
    	initializeProfessorListView();
    	initializeAssistantListView();
    	initializeStudentListView();
    	initializeUserTreeTableView();
    }
    
    private void initializeCourseListView() {
    	
    }

    private void initializeProfessorListView() {
    	
    }

	private void initializeAssistantListView() {
		
	}

	private void initializeStudentListView() {
		
	}

	private void initializeUserTreeTableView() {
    	// Observable list of users to be shown in TreeTableView
    	users = FXCollections.observableArrayList();
    	
    	// Set column cell factories
    	firstNameColumn	.setCellValueFactory(cell -> cell.getValue().getValue().firstName);
    	lastNameColumn	.setCellValueFactory(cell -> cell.getValue().getValue().lastName);
    	emailColumn		.setCellValueFactory(cell -> cell.getValue().getValue().email);
    	usernameColumn	.setCellValueFactory(cell -> cell.getValue().getValue().username);
    	passwordColumn	.setCellValueFactory(cell -> cell.getValue().getValue().password);
    	
    	// Assign root node to TreeTableView for holding users
    	TreeItem<User> root = new RecursiveTreeItem<AdminController.User>(users, RecursiveTreeObject::getChildren);
    	userTreeTableView.setRoot(root);
    	userTreeTableView.setShowRoot(false);
    	userTreeTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);  	
    	
    	// Bind delete button to being disabled when no entries are selected.
    	BooleanBinding binding = userTreeTableView.getSelectionModel().selectedIndexProperty().isEqualTo(-1);
    	userDeleteButton.disableProperty().bind(binding);
    	
    	// Sample data
    	users.add(new User("Patrik", "Kjærran", "patrikkj@stud.ntnu.no", "patrikkj", "manpower123"));
    	users.add(new User("Eric", "Veliyulin", "ericve@stud.ntnu.no", "ericve", "manpower123"));
    	users.add(new User("Beatrice", "Kiær", "beatriceki@stud.ntnu.no", "beatriceki", "girlpower123"));
    	users.add(new User("Sebastian", "Vittersø", "sebastianvi@stud.ntnu.no", "sebastianvi", "manpower123"));
    	users.add(new User("Eivind Yu", "Nilsen", "eivindni@stud.ntnu.no", "eivindni", "manpower123"));
    	users.add(new User("Francis Dao Quang", "Dao", "francisda@stud.ntnu.no", "francisda", "manpower123"));
	}

	@FXML
    void handleAddAssistantClick(ActionEvent event) {
    	JFXDialog dialog = new JFXDialog(rootPane, (Region) Loader.getParent(View.POPUP_ASSISTANT_VIEW), DialogTransition.CENTER);
    	dialog.show();
    }

    @FXML
    void handleAddCourseClick(ActionEvent event) {
    	JFXDialog dialog = new JFXDialog(rootPane, (Region) Loader.getParent(View.POPUP_COURSE_VIEW), DialogTransition.CENTER);
    	dialog.show();
    }

    @FXML
    void handleAddProfessorClick(ActionEvent event) {
    	JFXDialog dialog = new JFXDialog(rootPane, (Region) Loader.getParent(View.POPUP_PROFESSOR_VIEW), DialogTransition.CENTER);
    	dialog.show();
    }

    @FXML
    void handleAddStudentClick(ActionEvent event) {
    	JFXDialog dialog = new JFXDialog(rootPane, (Region) Loader.getParent(View.POPUP_STUDENT_VIEW), DialogTransition.CENTER);
    	dialog.show();
    }

    @FXML
    void handleAddUserClick(ActionEvent event) {
    	JFXDialog dialog = new JFXDialog(rootPane, (Region) Loader.getParent(View.POPUP_USER_VIEW), DialogTransition.CENTER);
    	dialog.show();
    }

    @FXML
    void handleDeleteAssistantClick(ActionEvent event) {

    }

    @FXML
    void handleDeleteCourseClick(ActionEvent event) {

    }

    @FXML
    void handleDeleteProfessorClick(ActionEvent event) {

    }

    @FXML
    void handleDeleteStudentClick(ActionEvent event) {

    }

    @FXML
    void handleDeleteUserClick(ActionEvent event) {
    	List<User> usersForDeletion = userTreeTableView.getSelectionModel()
    			.getSelectedItems()
    			.stream()
    			.map(TreeItem::getValue)
    			.collect(Collectors.toList());
    	users.removeAll(usersForDeletion);
    }

    @FXML
    void handleEditCourseClick(ActionEvent event) {

    }

    @FXML
    void handleEditUserClick(ActionEvent event) {

    }

    @FXML
    void handleExitClick(ActionEvent event) {
    	((Stage) rootPane.getScene().getWindow()).close();
    }

    @FXML
    void handleLogOutClick(ActionEvent event) {
    	StageManager.loadView(View.LOGIN_SCREEN);
    }
    
    
    /**
     * Class used internally by TreeTableView for representing users.
     * @author Patrik
     */
    private class User extends RecursiveTreeObject<User> {
    	final StringProperty firstName, lastName, email, username, password;
    	
    	public User(String firstName, String lastName, String email, String username, String password) {
			this.firstName = new SimpleStringProperty(firstName);
			this.lastName = new SimpleStringProperty(lastName);
			this.email = new SimpleStringProperty(email);
			this.username = new SimpleStringProperty(username);
			this.password = new SimpleStringProperty(password);
		}
    }
}
