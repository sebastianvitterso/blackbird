package main.core.ui.tabs;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeSortMode;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.SortType;
import main.app.Loader;
import main.core.ui.MenuController;
import main.core.ui.AdminController.RecursiveTreeUser;
import main.db.UserManager;
import main.models.Course;
import main.models.User;
import main.utils.PostInitialize;
import main.utils.Refreshable;
import main.utils.Role;
import main.utils.View;

public class MembersController implements Refreshable {
	private MenuController menuController;
	
	@FXML private JFXTreeTableView<RecursiveTreeUser> userTreeTableView;
    @FXML private TreeTableColumn<RecursiveTreeUser, String> firstNameColumn;
    @FXML private TreeTableColumn<RecursiveTreeUser, String> lastNameColumn;
    @FXML private TreeTableColumn<RecursiveTreeUser, String> emailColumn;
    @FXML private TreeTableColumn<RecursiveTreeUser, String> roleColumn;
    
    private ObservableList<RecursiveTreeUser> users;
    
	private List<User> userProfessorList;
	private List<User> userAssistantList;
	private List<User> userStudentList;
	
	/**
     * Runs any methods that require every controller to be initialized.
     * This method should only be invoked by the FXML Loader class.
     */
	@PostInitialize
    private void postInitialize() {
		menuController = Loader.getController(View.MENU_VIEW);
		initializeUserView();
    }
	
	private void initializeUserView() {	
		// Create observable list backing TreeTableView
		users = FXCollections.observableArrayList();
		
    	// Set column cell factories
    	firstNameColumn	.setCellValueFactory(cell -> cell.getValue().getValue().firstName);
    	lastNameColumn	.setCellValueFactory(cell -> cell.getValue().getValue().lastName);
    	emailColumn		.setCellValueFactory(cell -> cell.getValue().getValue().email);
    	roleColumn		.setCellValueFactory(cell -> cell.getValue().getValue().roleInfo);
    	
    	// Assign root node to TreeTableView for holding users
    	TreeItem<RecursiveTreeUser> root = new RecursiveTreeItem<RecursiveTreeUser>(users, RecursiveTreeObject::getChildren);
    	userTreeTableView.setRoot(root);
    	userTreeTableView.setShowRoot(false);
    	
    	
	}
	
	
	@Override
	public void update() {
		Course course = menuController.getSelectedCourse();
		if (course != null)
			updateUserView(course);
			
	}
	public void updateUserView(Course course) {
		List<User> userList = UserManager.usersFromCourse(course.getCourseCode());
		userProfessorList = UserManager.getUsersByRole(course, Role.PROFESSOR);
		userAssistantList = UserManager.getUsersByRole(course, Role.ASSISTANT);
		userStudentList = UserManager.getUsersByRole(course, Role.STUDENT);
		
		// Convert users to internal format
		List<RecursiveTreeUser> formattedUsers = userList.stream()
				.map(u -> new RecursiveTreeUser(u.getFirstName(), u.getLastName(), u.getEmail(), getRoleInfo(u, course)))
				.collect(Collectors.toList());
		
		Collections.sort(formattedUsers);
		
		users.clear();
		users.addAll(formattedUsers);
		userTreeTableView.getSelectionModel().clearSelection();
	}
	
	public String getRoleInfo(User user, Course course) {
		String info = "";
		if (userProfessorList.contains(user))
			info += ", Emneansvarlig";
		if (userAssistantList.contains(user))
			info += ", Studass";
		if (userStudentList.contains(user))
			info += ", Student";
		if (info.equals(""))
			throw new IllegalStateException("User had no role in this course?: " + user.getUsername());
		return info.substring(2);
	}
	
	@Override
	public void clear() {
		
	}
	
    public class RecursiveTreeUser extends RecursiveTreeObject<RecursiveTreeUser> implements Comparable<RecursiveTreeUser> {
    	private final StringProperty firstName, lastName, email, roleInfo;
    	
    	public RecursiveTreeUser(String firstName, String lastName, String email, String roleInfo) {
			this.firstName = new SimpleStringProperty(firstName);
			this.lastName = new SimpleStringProperty(lastName);
			this.email = new SimpleStringProperty(email);
			this.roleInfo = new SimpleStringProperty(roleInfo);
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
    	
    	public String getRoleInfo() {
			return roleInfo.get();
		}
    	
    	/**
		 * Prioritizes role, then firstname, then lastname, then email
		 * Could've been solved with userTreeTableView.getSortOrder().add(roleColumn);
		 * But this solution looks better in UI as there is no arrow
		 */
		@Override
		public int compareTo(RecursiveTreeUser o) {
			int RoleCompare = getRoleInfo().compareTo(o.getRoleInfo());
			int FirstNameCompare = getFirstName().compareTo(o.getFirstName());
			int LastNameCompare = getLastName().compareTo(o.getLastName());
			int EmailCompare = getEmail().compareTo(o.getEmail());
			List<Integer> compares = Arrays.asList(RoleCompare, FirstNameCompare, LastNameCompare, EmailCompare, 1);
			return compares.stream().filter(compare -> compare != 0).collect(Collectors.toList()).get(0);
		}
    }
}
