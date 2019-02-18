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

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
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
import main.core.ui.popups.UserSelectionPopupController;
import main.data.Course;
import main.data.Course.Role;
import main.data.User;
import main.db.CourseManager;
import main.db.DatabaseManager;
import main.db.UserManager;

public class AdminController implements Refreshable {
	@FXML private StackPane rootPane;
    @FXML private Label courseNameLabel;

    @FXML private JFXListView<Course> courseListView;
    @FXML private JFXListView<User> professorListView;
    @FXML private JFXListView<User> assistantListView;
    @FXML private JFXListView<User> studentListView;
    
    @FXML private JFXTreeTableView<RecursiveTreeUser> userTreeTableView;
    @FXML private TreeTableColumn<RecursiveTreeUser, String> firstNameColumn;
    @FXML private TreeTableColumn<RecursiveTreeUser, String> lastNameColumn;
    @FXML private TreeTableColumn<RecursiveTreeUser, String> emailColumn;
    @FXML private TreeTableColumn<RecursiveTreeUser, String> usernameColumn;
    @FXML private TreeTableColumn<RecursiveTreeUser, String> passwordColumn;
    
    @FXML private JFXButton courseEditButton;
    @FXML private JFXButton courseDeleteButton;
    @FXML private JFXButton professorAddButton;
    @FXML private JFXButton professorDeleteButton;
    @FXML private JFXButton assistantAddButton;
    @FXML private JFXButton assistantDeleteButton;
    @FXML private JFXButton studentAddButton;
    @FXML private JFXButton studentDeleteButton;
    @FXML private JFXButton userEditButton;
    @FXML private JFXButton userDeleteButton;
    
    private ObservableList<Course> courses;
    private ObservableList<User> professors;
    private ObservableList<User> assistants;
    private ObservableList<User> students;
    private ObservableList<RecursiveTreeUser> users;
    

    @FXML
    private void initialize() {
    	initializeCourseListView();
    	initializeDescendantListViews();
    	initializeUserTreeTableView();
    }
    
    private void initializeCourseListView() {
    	// Observable list of courses to be shown in CourseListView
    	courses = courseListView.getItems();
    	
    	// Assign change listener to updating list views when selected course is changed.
    	ChangeListener<Course> courseChangeListener = (observable, oldValue, newValue) -> updateDescendantListViews();
    	courseListView.getSelectionModel().selectedItemProperty().addListener(courseChangeListener);
    	
    	// Bind edit and delete button to being disabled when no course is selected.
    	BooleanBinding notSelectedCourse = courseListView.getSelectionModel().selectedItemProperty().isNull();
    	courseEditButton.disableProperty().bind(notSelectedCourse);
    	courseDeleteButton.disableProperty().bind(notSelectedCourse);
    	
    }

    private void initializeDescendantListViews() {
    	// Observable ListViews reflected in GUI
    	professors = professorListView.getItems();
    	assistants = assistantListView.getItems();
    	students = studentListView.getItems();
    	
    	// Bind delete button to being disabled when no entities are selected.    	
    	BooleanBinding notSelectedCourse = courseListView.getSelectionModel().selectedItemProperty().isNull();
    	BooleanBinding notSelectedProfessor = professorListView.getSelectionModel().selectedItemProperty().isNull();
    	BooleanBinding notSelectedAssistant = professorListView.getSelectionModel().selectedItemProperty().isNull();
    	BooleanBinding notSelectedStudent = professorListView.getSelectionModel().selectedItemProperty().isNull();

    	professorAddButton.disableProperty().bind(notSelectedCourse);
    	assistantAddButton.disableProperty().bind(notSelectedCourse);
    	studentAddButton.disableProperty().bind(notSelectedCourse);

    	professorDeleteButton.disableProperty().bind(notSelectedProfessor);
    	assistantDeleteButton.disableProperty().bind(notSelectedAssistant);
    	studentDeleteButton.disableProperty().bind(notSelectedStudent);
    	
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
    	TreeItem<RecursiveTreeUser> root = new RecursiveTreeItem<RecursiveTreeUser>(users, RecursiveTreeObject::getChildren);
    	userTreeTableView.setRoot(root);
    	userTreeTableView.setShowRoot(false);
    	userTreeTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);  	
    	
    	// Bind edit and delete button to being disabled when no user is selected.
    	BooleanBinding notSelectedUser = userTreeTableView.getSelectionModel().selectedItemProperty().isNull();
    	userEditButton.disableProperty().bind(notSelectedUser);
    	userDeleteButton.disableProperty().bind(notSelectedUser);
    	
    	// Sample data
    	users.add(new RecursiveTreeUser("Patrik", "Kjærran", "patrikkj@stud.ntnu.no", "patrikkj", "manpower123"));
    	users.add(new RecursiveTreeUser("Eric", "Veliyulin", "ericve@stud.ntnu.no", "ericve", "manpower123"));
    	users.add(new RecursiveTreeUser("Beatrice", "Kiær", "beatriceki@stud.ntnu.no", "beatriceki", "girlpower123"));
    	users.add(new RecursiveTreeUser("Sebastian", "Vittersø", "sebastianvi@stud.ntnu.no", "sebastianvi", "manpower123"));
    	users.add(new RecursiveTreeUser("Eivind Yu", "Nilsen", "eivindni@stud.ntnu.no", "eivindni", "manpower123"));
    	users.add(new RecursiveTreeUser("Francis Dao Quang", "Dao", "francisda@stud.ntnu.no", "francisda", "manpower123"));
	}

	
	@Override
	public void refresh() {
		updateCourseListView();
		updateDescendantListViews();
	}
	
	private void updateCourseListView() {
		DatabaseManager.submitRunnable(() -> {
			// Fetch list of courses from database
			List<Course> courseList = CourseManager.getCourses();
			
    		// Update course list via FX Application thread
			Platform.runLater(() -> courses.setAll(courseList));
		});
    }

    private void updateDescendantListViews() {
    	DatabaseManager.submitRunnable(() -> {
    		// Retrive selected course
    		Course course = courseListView.getSelectionModel().getSelectedItem();
    		
    		// Cancel if no course is selected
    		if (course == null)
    			return;
    		
    		// Fetch updated user lists from database
    		List<User> professorList = UserManager.getUsersByRole(course, Role.PROFESSOR);
    		List<User> assistantList = UserManager.getUsersByRole(course, Role.ASSISTANT);
    		List<User> studentList = UserManager.getUsersByRole(course, Role.STUDENT);
    		
    		// Update lists reflecting GUI in FX Application thread
    		Platform.runLater(() -> {
    			professors.setAll(professorList);
    			assistants.setAll(assistantList);
    			students.setAll(studentList);
    		});	
    	});
    }

	private void updateUserTreeTableView() {
	
	}
	
	public void addUsersToCourse(List<User> users, Course course, Role role) {
		
	}
	
	

    @FXML
    void handleAddCourseClick(ActionEvent event) {
    	JFXDialog dialog = new JFXDialog(rootPane, (Region) Loader.getParent(View.POPUP_COURSE_VIEW), DialogTransition.CENTER);
    	dialog.show();
    }

    @FXML
    void handleAddProfessorClick(ActionEvent event) {
    	JFXDialog dialog = new JFXDialog(rootPane, (Region) Loader.getParent(View.POPUP_USER_SELECTION_VIEW), DialogTransition.CENTER);
    	UserSelectionPopupController controller = (UserSelectionPopupController) Loader.getController(View.POPUP_USER_SELECTION_VIEW);
    	controller.configure(dialog, "Velg emneansvarlige", courseListView.getSelectionModel().getSelectedItem(), Role.PROFESSOR);
    	controller.fetchUsers();
    	dialog.show();
    }
    
    @FXML
    void handleAddAssistantClick(ActionEvent event) {
    	JFXDialog dialog = new JFXDialog(rootPane, (Region) Loader.getParent(View.POPUP_USER_SELECTION_VIEW), DialogTransition.CENTER);
    	UserSelectionPopupController controller = (UserSelectionPopupController) Loader.getController(View.POPUP_USER_SELECTION_VIEW);
    	controller.configure(dialog, "Velg læringsassistenter", courseListView.getSelectionModel().getSelectedItem(), Role.ASSISTANT);
    	controller.fetchUsers();
    	dialog.show();
    }
    
    @FXML
    void handleAddStudentClick(ActionEvent event) {
    	JFXDialog dialog = new JFXDialog(rootPane, (Region) Loader.getParent(View.POPUP_USER_SELECTION_VIEW), DialogTransition.CENTER);
    	UserSelectionPopupController controller = (UserSelectionPopupController) Loader.getController(View.POPUP_USER_SELECTION_VIEW);
    	controller.configure(dialog, "Velg studenter", courseListView.getSelectionModel().getSelectedItem(), Role.STUDENT);
    	controller.fetchUsers();
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
    	DatabaseManager.submitRunnable(() -> {
    		// Fetch selected users from TableView.
    		List<RecursiveTreeUser> usersForDeletion = userTreeTableView.getSelectionModel()
        			.getSelectedItems()
        			.stream()
        			.map(TreeItem::getValue)
        			.collect(Collectors.toList());
    		
    		// Map from TableView's internal format to list of usernames.
    		List<String> usernames = usersForDeletion.stream()
    				.map(e -> e.username.get())
    				.collect(Collectors.toList());
    		
    		// Remove users from database
    		UserManager.deleteUsers(usernames);
    		
    		// Update user list reflecting GUI in FX Application thread
        	Platform.runLater(() -> users.removeAll(usersForDeletion));
    	});
    	
    	
    	
    	
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
    private class RecursiveTreeUser extends RecursiveTreeObject<RecursiveTreeUser> {
    	final StringProperty firstName, lastName, email, username, password;
    	
    	public RecursiveTreeUser(String firstName, String lastName, String email, String username, String password) {
			this.firstName = new SimpleStringProperty(firstName);
			this.lastName = new SimpleStringProperty(lastName);
			this.email = new SimpleStringProperty(email);
			this.username = new SimpleStringProperty(username);
			this.password = new SimpleStringProperty(password);
		}
    }


	
}
