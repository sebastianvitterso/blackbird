package main.core.ui;

import java.util.List;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import main.app.Loader;
import main.core.ui.popups.CoursePopupController;
import main.core.ui.popups.UserPopupController;
import main.core.ui.popups.UserSelectionPopupController;
import main.db.CourseManager;
import main.db.UserManager;
import main.models.Course;
import main.models.User;
import main.utils.PostInitialize;
import main.utils.Refreshable;
import main.utils.Role;
import main.utils.View;

public class AdminController implements Refreshable {
	// FXML fields
	@FXML private StackPane rootPane;
    @FXML private Label courseNameLabel;
    @FXML private JFXTabPane tabPane;

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
    
    // Controller references
    private CoursePopupController courseController;
    private UserSelectionPopupController userSelectionController;
    private UserPopupController userController;
    
    // Underlying containers
    private ObservableList<Course> courses;
    private ObservableList<User> professors;
    private ObservableList<User> assistants;
    private ObservableList<User> students;
    private ObservableList<RecursiveTreeUser> users;
    
    // Selections
    private ObservableList<Course> selectedCourses;
    private ObservableList<User> selectedProfessors;
    private ObservableList<User> selectedAssistants;
    private ObservableList<User> selectedStudents;
    private ObservableList<TreeItem<RecursiveTreeUser>> selectedUsers;

    // Selection properties
    private IntegerBinding courseSelectionSize;
    private IntegerBinding professorSelectionSize;
    private IntegerBinding assistantSelectionSize;
    private IntegerBinding studentSelectionSize;
    private IntegerBinding userSelectionSize;
	
    
    //// Initialization ////
    /**
     * Initializes every component in the user interface.
     * This method is automatically invoked when loading the corresponding FXML file.
     */
    @FXML
    private void initialize() {
    	initializeCourseView();
    	initializeDescendantViews();
    	initializeUserView();
    	initializeChangeListeners();
    	initializeBindings();
    	
    	// Update when switching between tabs
    	tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> update());
    }
    
    /**
     * Initialize course view..
     */
	private void initializeCourseView() {
		// Allow selection of multiple entities.
    	courseListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	
    	// Create references to underlying containers
    	courses = courseListView.getItems();

    	// Create references to selections
    	selectedCourses = courseListView.getSelectionModel().getSelectedItems();
    	
    	// Assign string converter for displaying courses
//    	courseListView.setCellFactory(new Callback<ListView<Course>, ListCell<Course>>() {
//			@Override
//			public ListCell<Course> call(ListView<Course> listView) {
//				return new ListCell<Course>() {
//					@Override
//					protected void updateItem(Course course, boolean empty) {
//						super.updateItem(course, empty);
//						
//						if (course != null && !empty)
//							setText(String.format("%s - %s", course.getCourseCode(), course.getName()));
//						else
//							setText(null);
//					}
//				};
//			}
//		});
    }

	/**
	 * Initialize the role-specific views.
	 */
    private void initializeDescendantViews() {
    	// Allow selection of multiple entities.
    	professorListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	assistantListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	studentListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	
    	// Create references to underlying containers
    	professors = professorListView.getItems();
    	assistants = assistantListView.getItems();
    	students = studentListView.getItems();
    	
    	// Create references to selections
    	selectedProfessors = professorListView.getSelectionModel().getSelectedItems();
    	selectedAssistants = assistantListView.getSelectionModel().getSelectedItems();
    	selectedStudents = studentListView.getSelectionModel().getSelectedItems();
    }

    /**
     * Initialize user view.
     */
	private void initializeUserView() {
		// Allow selection of multiple entities.
		userTreeTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);  	
		
		// Create observable list backing TreeTableView
		users = FXCollections.observableArrayList();
		
		// Create references to selections
		selectedUsers = userTreeTableView.getSelectionModel().getSelectedItems();
		
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
	}

	/** 
	 * Initialize change listeners, making the interface respond to changes.
	 */
	private void initializeChangeListeners() {
		// Assign change listener to focus property, unselecting entities when focus is lost.
		// Assures that users from multiple roles cannot be selected simultaneously.
    	professorListView.focusedProperty().addListener((observable, oldValue, newValue) -> {
    		if (newValue) {
    			studentListView.getSelectionModel().clearSelection();
    			assistantListView.getSelectionModel().clearSelection();
    		}
    	});
    	
    	assistantListView.focusedProperty().addListener((observable, oldValue, newValue) -> {
    		if (newValue) {
    			professorListView.getSelectionModel().clearSelection();
    			studentListView.getSelectionModel().clearSelection();
    		}
    	});
    	
    	studentListView.focusedProperty().addListener((observable, oldValue, newValue) -> {
    		if (newValue) {
    			professorListView.getSelectionModel().clearSelection();
    			assistantListView.getSelectionModel().clearSelection();
    		}
    	});
    	
    	// Assign change listener to updating list views when selected course is changed.
    	selectedCourses.addListener(new ListChangeListener<Course>() {
			@Override
			public void onChanged(Change<? extends Course> arg0) {
				System.out.println(arg0);
				updateDescendantViews();
			}
		});
    			
	}
	
	/**
	 * Initialize property bindings, making sure that the interface
	 * is in a valid state by disallowing invalid actions.
	 */
	private void initializeBindings() {
    	courseSelectionSize = Bindings.size(selectedCourses);
    	professorSelectionSize = Bindings.size(selectedProfessors);
    	assistantSelectionSize = Bindings.size(selectedAssistants);
    	studentSelectionSize = Bindings.size(selectedStudents);
    	userSelectionSize = Bindings.size(selectedUsers);
    	
    	// Bind role specific list views to being disabled if no single course is selected.
    	professorListView.disableProperty().bind(courseSelectionSize.isNotEqualTo(1));
    	assistantListView.disableProperty().bind(courseSelectionSize.isNotEqualTo(1));
    	studentListView.disableProperty().bind(courseSelectionSize.isNotEqualTo(1));
    	
    	// Bind course 'edit' button to being disabled when no single course is selected.
    	courseEditButton.disableProperty().bind(courseSelectionSize.isNotEqualTo(1));
    	
    	// Bind course 'delete' button to being disabled when no course is selected.
    	courseDeleteButton.disableProperty().bind(courseSelectionSize.isEqualTo(0));
    	
    	// Bind role 'add' button to being disabled when no single course is selected.
    	professorAddButton.disableProperty().bind(courseSelectionSize.isNotEqualTo(1));
    	assistantAddButton.disableProperty().bind(courseSelectionSize.isNotEqualTo(1));
    	studentAddButton.disableProperty().bind(courseSelectionSize.isNotEqualTo(1));
    	
    	// Bind role 'delete' button to being disabled when no entity is selected.
    	professorDeleteButton.disableProperty().bind(professorSelectionSize.isEqualTo(0));
    	assistantDeleteButton.disableProperty().bind(assistantSelectionSize.isEqualTo(0));
    	studentDeleteButton.disableProperty().bind(studentSelectionSize.isEqualTo(0));		
    	
    	// Bind user 'edit' button to being disabled when no single user is selected.
    	userEditButton.disableProperty().bind(userSelectionSize.isNotEqualTo(1));
    	
    	// Bind user 'delete' button to being disabled when no user is selected.
    	userDeleteButton.disableProperty().bind(userSelectionSize.isEqualTo(0));
    	
    	// Dynamic tab resizing
    	tabPane.tabMinWidthProperty().bind(rootPane.widthProperty().divide(2));
    	tabPane.tabMaxWidthProperty().bind(rootPane.widthProperty().divide(2));    	
	}
	
	/**
     * Runs any methods that require every controller to be initialized.
     * This method should only be invoked by the FXML Loader class.
     */
	@PostInitialize
    private void postInitialize() {
    	courseController = Loader.getController(View.POPUP_COURSE_VIEW);
    	userSelectionController = Loader.getController(View.POPUP_USER_SELECTION_VIEW);
    	userController = Loader.getController(View.POPUP_USER_VIEW);
    }
	
	
	//// GUI Updates ////
	/**
	 * Updates every component in the user interface.
	 */
	@Override
	public void update() {
		updateCourseView();
		updateDescendantViews();
		updateUserView();
	}
	
	/**
	 * Updates the underlying container used by the 'course' ListView, fetching updates from database.
	 * Changes are reflected in the user interface.
	 */
	public void updateCourseView() {
		// Fetch list of courses from database
		List<Course> courseList = CourseManager.getCourses();
		
		// Update course list via FX Application thread
		courses.setAll(courseList);
		courseListView.getSelectionModel().clearSelection();
    }
	
	/**
	 * Updates the underlying container used by the ListView for every role, fetching updates from database.
	 * Changes are reflected in the user interface.
	 */
	public void updateDescendantViews() {
		// Update course name
		if (selectedCourses.size() == 1)
			courseNameLabel.setText(String.format("%s (%s)", selectedCourses.get(0).getName(), selectedCourses.get(0).getCourseCode()));
		else
			courseNameLabel.setText(null);
		
    	updateViewByRole(Role.PROFESSOR);
    	updateViewByRole(Role.ASSISTANT);
    	updateViewByRole(Role.STUDENT);
    }
	
	/**
	 * Updates the underlying container used by the ListView for the role specified, fetching updates from database.
	 * Changes are reflected in the user interface.
	 */
	public void updateViewByRole(Role role) {
		// Clear previous data
		clearViewByRole(role);
		
		// Break if no single course is selected
		if (courseSelectionSize.isNotEqualTo(1).get())
			return;
		
		// Fetch updated user lists from database
		List<User> updatedList = UserManager.getUsersByRole(selectedCourses.get(0), role);
		
		// TODO: Add concurrency
		// Update lists reflecting GUI in FX Application thread
		switch (role) {
		case PROFESSOR:
			professorListView.getItems().setAll(updatedList);
			professorListView.getSelectionModel().clearSelection();
			break;
		
		case ASSISTANT:
			assistantListView.getItems().setAll(updatedList);
			assistantListView.getSelectionModel().clearSelection();
			break;
		
		case STUDENT:
			studentListView.getItems().setAll(updatedList);
			studentListView.getSelectionModel().clearSelection();
			break;
		}
	}
	
	/**
	 * Updates the underlying containers used by the 'user' TreeTableView, fetching updates from database.
	 * Changes are reflected in the user interface.
	 */
	public void updateUserView() {
		// Fetch list of courses from database
		List<User> userList = UserManager.getUsers();
		
		// Convert users to internal format
		List<RecursiveTreeUser> formattedUsers = userList.stream()
				.map(u -> new RecursiveTreeUser(u.getFirstName(), u.getLastName(), u.getEmail(), u.getUsername(), u.getPassword()))
				.collect(Collectors.toList());
		
		// Update course list via FX Application thread
		users.clear();
		users.addAll(formattedUsers);
		userTreeTableView.getSelectionModel().clearSelection();
    	userTreeTableView.getSortOrder().clear();
	}
	
	
	//// GUI Clearing ////
	/**
	 * Clears every component in the user interface.
	 */
	@Override
	public void clear() {
		clearCourseView();
		clearDescendantViews();
		clearUserView();
		clearSelections();
	}
	
	/**
	 * Clears the underlying container used by the 'course' ListView.
	 * Changes are reflected in the user interface.
	 */
	public void clearCourseView() {
		courses.clear();
    }

	/**
	 * Clears the underlying container used by the ListView for every role.
	 * Changes are reflected in the user interface.
	 */
	public void clearDescendantViews() {
    	clearViewByRole(Role.PROFESSOR);
    	clearViewByRole(Role.ASSISTANT);
    	clearViewByRole(Role.STUDENT);
    }
	
	/**
	 * Clears the underlying container used by the ListView for the role specified.
	 * Changes are reflected in the user interface.
	 */
	public void clearViewByRole(Role role) {
		switch (role) {
		case PROFESSOR:
			professors.clear();
			break;
		
		case ASSISTANT:
			assistants.clear();
			break;
		
		case STUDENT:
			students.clear();
			break;
		}
	}
	
	/**
	 * Clears the underlying containers used by the 'user' TreeTableView.
	 * Changes are reflected in the user interface.
	 */
	public void clearUserView() {
		users.clear();
	}
	
	/**
	 * Clears selection for every selectable component in the user interface.
	 */
	public void clearSelections() {
		// Unselect everything
		courseListView.getSelectionModel().clearSelection();
		professorListView.getSelectionModel().clearSelection();
		assistantListView.getSelectionModel().clearSelection();
		studentListView.getSelectionModel().clearSelection();
		userTreeTableView.getSelectionModel().clearSelection();
		
		// Force recalculation of bound properties
    	courseSelectionSize.invalidate();
    	professorSelectionSize.invalidate();
    	assistantSelectionSize.invalidate();
    	studentSelectionSize.invalidate();
    	userSelectionSize.invalidate();
	}
	
	
	//// Event handlers ////
	/*
	 * Course view
	 */
    @FXML
    void handleAddCourseClick(ActionEvent event) {
    	// Create dialog box
    	JFXDialog dialog = new JFXDialog(rootPane, (Region) Loader.getParent(View.POPUP_COURSE_VIEW), DialogTransition.CENTER);
    	
    	// Initialize popup
    	courseController.clear();
    	courseController.connectDialog(dialog);
    	courseController.loadNewCourse();
    	dialog.show();
    }
    
    @FXML
    void handleEditCourseClick(ActionEvent event) {
    	// Create dialog box
    	JFXDialog dialog = new JFXDialog(rootPane, (Region) Loader.getParent(View.POPUP_COURSE_VIEW), DialogTransition.CENTER);
    	
    	// Initialize popup
    	courseController.clear();
    	courseController.connectDialog(dialog);
    	courseController.loadEditCourse(selectedCourses.get(0));
    	dialog.show();

    }
    
    
    
    @FXML
    void handleDeleteCourseClick(ActionEvent event) {
    	JFXDialogLayout content = new JFXDialogLayout();
    	content.setHeading(new Text("Slett Fag"));
    	String f1 = null;
    	String f2 = null;
    	if(selectedCourses.size() == 1) {
    		f1 = "faget";
    		f2 = "dette ";
    	}
    	else {
    		f1= "fagene";
    		f2 = "disse ";
    	}
    	content.setBody(new Text("Er du sikker på at du vil slette " + f1 + "?\n" 
				+ "All data som forbindes med " + f2 + f1 + " vil gå tapt."));
    	
    	JFXDialog dialog = new JFXDialog(rootPane, content, JFXDialog.DialogTransition.CENTER);
    	JFXButton deleteButton = new JFXButton("Slett");
    	deleteButton.setStyle("-fx-background-color: #903030; -fx-text-fill: #DDDDDD;");
    	JFXButton cancelButton = new JFXButton("Avbryt");
    	cancelButton.setStyle("-fx-background-color: #979797; -fx-text-fill: #DDDDDD;");
    	deleteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				CourseManager.deleteCourses(selectedCourses);
				updateCourseView();
				dialog.close();
			}
		});
    	cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				dialog.close();
			}
		});
    	content.setActions(cancelButton, deleteButton);
    	dialog.show();
    }
    
    /*
     * Professor view
     */
    
    @FXML
    void handleAddProfessorClick(ActionEvent event) {
    	JFXDialog dialog = new JFXDialog(rootPane, (Region) Loader.getParent(View.POPUP_USER_SELECTION_VIEW), DialogTransition.CENTER);
    	
    	userSelectionController.clear();
    	userSelectionController.connectDialog(dialog);
    	userSelectionController.loadUserSelection("Velg emneansvarlige", selectedCourses.get(0), Role.PROFESSOR);
    	dialog.show();
    }

    @FXML
    void handleDeleteProfessorClick(ActionEvent event) {
		UserManager.deleteUsersFromCourseGivenRole(selectedProfessors, selectedCourses.get(0), Role.PROFESSOR);
		updateViewByRole(Role.PROFESSOR);
    }
    
    /*
     * Assistant view
     */
    
    @FXML
    void handleAddAssistantClick(ActionEvent event) {
    	JFXDialog dialog = new JFXDialog(rootPane, (Region) Loader.getParent(View.POPUP_USER_SELECTION_VIEW), DialogTransition.CENTER);
    	
    	userSelectionController.clear();
    	userSelectionController.connectDialog(dialog);
    	userSelectionController.loadUserSelection("Velg læringsassistenter", selectedCourses.get(0), Role.ASSISTANT);
    	dialog.show();
    }
    
    @FXML
    void handleDeleteAssistantClick(ActionEvent event) {
		UserManager.deleteUsersFromCourseGivenRole(selectedAssistants, selectedCourses.get(0), Role.ASSISTANT);
		updateViewByRole(Role.ASSISTANT);
    }
    
    /*
     * Student view
     */
    
    @FXML
    void handleAddStudentClick(ActionEvent event) {
    	JFXDialog dialog = new JFXDialog(rootPane, (Region) Loader.getParent(View.POPUP_USER_SELECTION_VIEW), DialogTransition.CENTER);
    	
    	userSelectionController.clear();
    	userSelectionController.connectDialog(dialog);
    	userSelectionController.loadUserSelection("Velg studenter", selectedCourses.get(0), Role.STUDENT);
    	dialog.show();
    }

    @FXML
    void handleDeleteStudentClick(ActionEvent event) {
		UserManager.deleteUsersFromCourseGivenRole(selectedStudents, selectedCourses.get(0), Role.STUDENT);
		updateViewByRole(Role.STUDENT);
    }
    
    /*
     * User view
     */
    
    @FXML
    void handleAddUserClick(ActionEvent event) {
    	// Create dialog box
    	JFXDialog dialog = new JFXDialog(rootPane, (Region) Loader.getParent(View.POPUP_USER_VIEW), DialogTransition.CENTER);
    	
    	// Initialize popup
    	userController.clear();
    	userController.connectDialog(dialog);
    	userController.loadNewUser();
    	dialog.show();
    }

    @FXML
    void handleEditUserClick(ActionEvent event) {
    	// Create dialog box
    	JFXDialog dialog = new JFXDialog(rootPane, (Region) Loader.getParent(View.POPUP_USER_VIEW), DialogTransition.CENTER);
    	
    	// Initialize popup
    	userController.clear();
    	userController.connectDialog(dialog);
    	userController.loadEditUser(selectedUsers.get(0).getValue());
    	dialog.show();
    }
    
    @FXML
    void handleDeleteUserClick(ActionEvent event) {
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
		updateUserView();
    }
    
    
    /**
     * Class used internally by TreeTableView for representing users.
     */
    public class RecursiveTreeUser extends RecursiveTreeObject<RecursiveTreeUser> {
    	private final StringProperty firstName, lastName, email, username, password;
    	
    	public RecursiveTreeUser(String firstName, String lastName, String email, String username, String password) {
			this.firstName = new SimpleStringProperty(firstName);
			this.lastName = new SimpleStringProperty(lastName);
			this.email = new SimpleStringProperty(email);
			this.username = new SimpleStringProperty(username);
			this.password = new SimpleStringProperty(password);
		}
    	
    	public String getFirstName() {
			return firstName.get();
		}
    	
    	public String getLastName() {
			return lastName.get();
		}
    	
    	public String getEmail() {
			return email.get();
		}
    	
    	public String getUsername() {
			return username.get();
		}
    	
    	public String getPassword() {
			return password.get();
		}
    }

}
