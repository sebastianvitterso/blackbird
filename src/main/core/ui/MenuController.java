package main.core.ui;

import java.util.EnumMap;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.StringConverter;
import main.app.Loader;
import main.app.StageManager;
import main.db.CourseManager;
import main.db.LoginManager;
import main.models.Course;
import main.models.User;
import main.utils.PostInitialize;
import main.utils.Refreshable;
import main.utils.View;

public class MenuController implements Refreshable {
	private MainController mainController;
	private EnumMap<View, MenuButton> buttons = new EnumMap<>(View.class);
	
	@FXML private StackPane rootPane;
	@FXML private VBox menuButtonsVBox;
	@FXML private Circle imageCircle;
    @FXML private Label nameLabel;
    @FXML private Label roleLabel;
    @FXML private JFXComboBox<Course> courseComboBox;
	
    
    //// Initialization ////
    /**
     * Initializes every component in the user interface.
     * This method is automatically invoked when loading the corresponding FXML file.
     */
	@FXML
	private void initialize() {
		initializeMenuButtons();
		initializeCourseComboBox();
	}
	
	/**
	 * Initializes ConboBox holding selectable courses.
	 */
	private void initializeCourseComboBox() {
		courseComboBox.setConverter(new StringConverter<Course>() {
			@Override
			public String toString(Course course) {
				return String.format("%s - [%s]", course.getName(), course.getCourseCode());
			}
			
			@Override
			public Course fromString(String arg0) {
				return null;
			}
		});
		
		// ChangeListener for courses
//		courseComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//    	});
	}
	
	/**
	 * Initializes menu buttons by creating a mapping from {@code View} to corresponding buttom.
	 * @see MenuButton
	 */
	private void initializeMenuButtons() {
		buttons.put(View.OVERVIEW_VIEW, 	new MenuButton(View.OVERVIEW_VIEW, 		"Oversikt", 		new ImageView(Loader.getImage("icons/overview.png"))));
		buttons.put(View.EXERCISES_VIEW, 	new MenuButton(View.EXERCISES_VIEW, 	"Øvinger", 			new ImageView(Loader.getImage("icons/exercises.png"))));
		buttons.put(View.SCHEDULING_VIEW, 	new MenuButton(View.SCHEDULING_VIEW, 	"Timebestilling", 	new ImageView(Loader.getImage("icons/scheduling.png"))));
		buttons.put(View.MEMBERS_VIEW, 		new MenuButton(View.MEMBERS_VIEW, 		"Medlemmer", 		new ImageView(Loader.getImage("icons/members.png"))));
		buttons.put(View.ADMIN_VIEW, 		new MenuButton(View.ADMIN_VIEW, 		"Administrer", 		new ImageView(Loader.getImage("icons/admin.png"))));
//		buttons.put(View.CALENDAR_VIEW, 	new MenuButton(View.CALENDAR_VIEW, 		"Kalender",			new ImageView(Loader.getImage("icons/calendar.png"))));
		
		// Configure custom buttons
		for (MenuButton button : buttons.values()) {
			// Set styling
			button.getStyleClass().add("menu-button");
			
			// Disable buttons for unimplemented views
			if (button.getView().getPathToFXML() == null)
				button.setDisable(true);
			
			// Add ClickEvent handler
			button.setOnAction(event -> handleMenuButtonClick(event));
			
			// Configure image size, cannot be done through CSS without SVG formatting :(
			button.getImageView().setFitWidth(25);
			button.getImageView().setFitHeight(25);
		}
	}
	
    /**
     * Runs any methods that require every controller to be initialized.
     * This method should only be invoked by the FXML Loader class.
     */
    @PostInitialize
    private void postInitialize() {
    	mainController = Loader.getController(View.MAIN_VIEW);
    }
	
    
    //// Updates ////
    /**
	 * Updates every component in the user interface.
	 */
	@Override
	public void update() {
		User activeUser = LoginManager.getActiveUser();
		updatePersonalia(activeUser);
		updateCourseComboBox(activeUser);
		updateMenuButtons(activeUser);
		
		// Select first tab by default
		if (!menuButtonsVBox.getChildren().isEmpty())
			loadTab(((MenuButton) menuButtonsVBox.getChildren().get(0)).getView());
	}
	
	/**
	 * Updates user related components (Name, image and role).
	 */
	public void updatePersonalia(User user) {
		// TODO: Need information about users role for given course in returned query (UserCourse-relation)
		nameLabel.setText(user.getName());
		roleLabel.setText("Student");
		
		imageCircle.setFill(new ImagePattern(Loader.getImage("icons/silhouette.jpg")));
	}
	
	/**
	 * Updates the underlying container holding the users selectable courses.
	 */
	public void updateCourseComboBox(User user) {
		// Fetch selectable courses from database for given user
		List<Course> courses = CourseManager.getCoursesFromUser(user);
		
		// Update displayed courses
		courseComboBox.getItems().setAll(courses);
		
		// Select first course, if present
		if (!courseComboBox.getItems().isEmpty())
			courseComboBox.getSelectionModel().selectFirst();
	}
	
	/**
	 * Updates the buttons visible to the user based on user role.
	 */
	public void updateMenuButtons(User user) {
		// In the case of admin login
		if (user.getUsername().equals("admin")) {
			menuButtonsVBox.getChildren().setAll(buttons.get(View.ADMIN_VIEW));
			return;
		}

		menuButtonsVBox.getChildren().setAll(
				buttons.get(View.OVERVIEW_VIEW),
				buttons.get(View.EXERCISES_VIEW),
				buttons.get(View.SCHEDULING_VIEW),
				buttons.get(View.MEMBERS_VIEW));
//				buttons.get(View.CALENDAR_VIEW));
	}
	
	
	//// Clearing ////
	/**
	 * Clears every component in the user interface.
	 */
	@Override
	public void clear() {
		clearCourseComboBox();
		clearButtonSelection();
	}
	
	/**
	 * Clears the list of selectable courses.
	 */
	private void clearCourseComboBox() {
		courseComboBox.setPromptText("");
		courseComboBox.getItems().clear();
		courseComboBox.getSelectionModel().clearSelection();
	}

	/**
	 * Unselects buttons and reverts node styling.
	 */
	private void clearButtonSelection() {
		// Unselect all buttons
		menuButtonsVBox.getChildren().forEach(button -> button.getStyleClass().remove("menu-button-active"));
		
		// Reset focus
		rootPane.requestFocus();
	}

	
	//// Helper methods ////
	/**
	 * Sets the tab to be displayed on screen, specified by {@code view}.
	 */
	public void loadTab(View view) {
		// Break if view is not implemented
		if (view.getPathToFXML() == null)
			return;
		
		// Unselect all buttons
		clearButtonSelection();
		
		// Select clicked button
		buttons.get(view).getStyleClass().add("menu-button-active");
		
		// Load corresponding tab
		mainController.loadTab(view);
	}
	
	
	//// Event handlers ////
	void handleMenuButtonClick(ActionEvent event) {
		// Find view
		View view = ((MenuButton) event.getSource()).getView();
		
		// Load new tab
		loadTab(view);
	}
	
	@FXML
    void handleLogOutClick(ActionEvent event) {
    	StageManager.loadView(View.LOGIN_VIEW);
    }
	
	
	/**
	 * Custom implementation of JFXButton which can be linked to a {@code View} enumeration.
	 */
	private class MenuButton extends JFXButton {
		private final View view;
		
		public MenuButton(View view, String text, ImageView graphic) {
			super(text, graphic);
			this.view = view;
		}
		
		public View getView() {
			return view;
		}
		
		public ImageView getImageView() {
			return (ImageView) super.getGraphic();
		}
	}
}
