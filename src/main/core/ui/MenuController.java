package main.core.ui;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListCell;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import javafx.util.StringConverter;
import main.app.Loader;
import main.app.StageManager;
import main.db.CourseManager;
import main.db.LoginManager;
import main.models.Course;
import main.models.User;
import main.models.UserInCourse;
import main.utils.PostInitialize;
import main.utils.Refreshable;
import main.utils.Role;
import main.utils.View;

public class MenuController implements Refreshable {
	private MainController mainController;
	private EnumMap<View, MenuButton> buttons;
	private RotateTransition rotateTransition;

	// Fields for sectioning courses
	private Set<UserInCourse> headers;
	private ObservableList<UserInCourse> courseRelations;
	private SortedList<UserInCourse> sortedCourseRelations;
	private Comparator<UserInCourse> userInCourseComparator;
	private Predicate<UserInCourse> selectionPredicate;
	private Function<UserInCourse, String> sectionNamingFunction;
	private Function<UserInCourse, String> itemNamingFunction;
	
	@FXML private StackPane rootPane;
	@FXML private VBox menuButtonsVBox;
	@FXML private Circle imageCircle;
    @FXML private Label nameLabel;
    @FXML private Label roleLabel;
    @FXML private JFXComboBox<UserInCourse> courseRelationsComboBox;
	@FXML private JFXButton refreshButton;
    
    //// Initialization ////
    /**
     * Initializes every component in the user interface.
     * This method is automatically invoked when loading the corresponding FXML file.
     */
	@FXML
	private void initialize() {
		initializeMenuButtons();
		initializeRefreshButton();
		initializeCourseComboBox();
	}
	
	/**
	 * Initializes ConboBox holding selectable courses.
	 */
	private void initializeCourseComboBox() {
		// Converter determines how combobox displays courses
		courseRelationsComboBox.setConverter(new StringConverter<UserInCourse>() {
			@Override
			public String toString(UserInCourse userInCourse) {
				return String.format("%s - [%s]", userInCourse.getCourse().getName(), userInCourse.getCourse().getCourseCode());
			}
			
			@Override
			public UserInCourse fromString(String arg0) {
				return null;
			}
		});
		
		// Actions specified bv the changelistener are invoked whenever courses are (re)selected
		courseRelationsComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			// Update current tab
			Refreshable controller = mainController.getCurrentController();
			if (controller != null)
				controller.refresh();
			
			// Update role label
			updateRoleLabel(courseRelationsComboBox.getSelectionModel().getSelectedItem());
			System.out.printf("CourseChangeListener [%s -> %s]%n",  oldValue, newValue);
			
			// Recalculate listview size within combobox
			courseRelationsComboBox.autosize();
    	});
		
		// Change underlying ComboBox container to support sorting
		userInCourseComparator = (uic1, uic2) -> {
			// First sort based on role
			int roleComparison = uic1.getRole().compareTo(uic2.getRole());
			if (roleComparison != 0)
				return roleComparison;
			
			// In case roles are equal, sort alphabetically
			return uic1.getCourse().getName().compareTo(uic2.getCourse().getName());
		};
		courseRelations = FXCollections.observableArrayList();
		sortedCourseRelations = new SortedList<UserInCourse>(courseRelations, userInCourseComparator);
		courseRelationsComboBox.setItems(sortedCourseRelations);
		
		// Split listView into sections based on role in course
		headers = new HashSet<>();
		
		selectionPredicate = uic -> headers.contains(uic);
		sectionNamingFunction = uic -> uic.getRole().getNorwegianName();
		itemNamingFunction = uic -> String.format("%s - [%s]", uic.getCourse().getName(), uic.getCourse().getCourseCode());
		
		courseRelationsComboBox.setCellFactory(listView -> {
			listView.minWidthProperty().set(350);
			listView.setFixedCellSize(0);
			listView.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
				if (!(event.getPickResult().getIntersectedNode() instanceof JFXButton))
					event.consume();
			});
			return new SectionedListCell<UserInCourse>(selectionPredicate, sectionNamingFunction, itemNamingFunction, listView);
		});
	}
	
	/**
	 * Initializes menu buttons by creating a mapping from {@code View} to corresponding buttom.
	 * @see MenuButton
	 */
	private void initializeMenuButtons() {
		// Initialize button mapping
		buttons = new EnumMap<>(View.class);

		buttons.put(View.OVERVIEW_VIEW, 	new MenuButton(View.OVERVIEW_VIEW, 		"Oversikt", 		new ImageView(Loader.getImage("icons/overview.png"))));
		buttons.put(View.SCHEDULING_VIEW, 	new MenuButton(View.SCHEDULING_VIEW, 	"Timebestilling", 	new ImageView(Loader.getImage("icons/scheduling.png"))));
		buttons.put(View.EXERCISES_VIEW, 	new MenuButton(View.EXERCISES_VIEW, 	"Øvinger", 			new ImageView(Loader.getImage("icons/exercises.png"))));
		buttons.put(View.MEMBERS_VIEW, 		new MenuButton(View.MEMBERS_VIEW, 		"Medlemmer", 		new ImageView(Loader.getImage("icons/members.png"))));
		buttons.put(View.ADMIN_VIEW, 		new MenuButton(View.ADMIN_VIEW, 		"Administrer", 		new ImageView(Loader.getImage("icons/admin.png"))));
		
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
	 * Initializes menu buttons by creating a mapping from {@code View} to corresponding buttom.
	 * @see MenuButton
	 */
	private void initializeRefreshButton() {
		rotateTransition = new RotateTransition();
		rotateTransition.setByAngle(360);
		rotateTransition.setDuration(Duration.millis(500));
		rotateTransition.setInterpolator(Interpolator.EASE_BOTH);
		rotateTransition.setNode(refreshButton);
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
		System.out.println("Menu update");
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
		
		updateRoleLabel(courseRelationsComboBox.getSelectionModel().getSelectedItem());
		
		imageCircle.setFill(new ImagePattern(Loader.getImage("icons/silhouette.jpg")));
	}
	
	/**
	 * Updates role to be displayed in personalia.
	 */
	private void updateRoleLabel(UserInCourse userInCourse) {
		// TODO: Hardcoded admin check
		if (LoginManager.getActiveUser().getUsername().equals("admin")) {
			roleLabel.setText("Admin");
			return;
		}
		
		// Update role
		if (userInCourse != null)
			roleLabel.setText(userInCourse.getRole().getNorwegianName());
		else
			roleLabel.setText("Ikke tilgjengelig");
	}
	
	/**
	 * Updates the underlying container holding the users selectable courses.
	 */
	public void updateCourseComboBox(User user) {
		// TODO: Hardcode admin behavior
		if (LoginManager.getActiveUser().getUsername().equals("admin")) {
			courseRelationsComboBox.setVisible(false);
			return;
		} else {
			courseRelationsComboBox.setVisible(true);
		}
		
		// Fetch selectable courses from database for given user
		List<UserInCourse> courseRelationsFromDB = CourseManager.getUserInCourseRelations(user);
		
		// Assign headers to leading entries
		headers.clear();
		for (Role role : Role.values())
			courseRelationsFromDB.stream()
				.sorted(userInCourseComparator)
				.filter(uic -> uic.getRole() == role)
				.findFirst()
				.ifPresent(uic -> headers.add(uic));
		
		// Update displayed courses
		courseRelations.setAll(courseRelationsFromDB);
		
		// Select first course, if present
		if (!courseRelationsComboBox.getItems().isEmpty())
			courseRelationsComboBox.getSelectionModel().selectFirst();
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
				buttons.get(View.SCHEDULING_VIEW),
				buttons.get(View.EXERCISES_VIEW),
				buttons.get(View.MEMBERS_VIEW));
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
		courseRelationsComboBox.setPromptText("");
		courseRelations.clear();
		courseRelationsComboBox.getSelectionModel().clearSelection();
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
	
	/**
	 * Returns the selected course.
	 */
	public Course getSelectedCourse() {
		UserInCourse userInCourse =  courseRelationsComboBox.getSelectionModel().getSelectedItem();
		return (userInCourse != null) ? userInCourse.getCourse() : null;
	}
	
	/**
	 * Returns the selected role.
	 */
	public Role getSelectedRole() {
		UserInCourse userInCourse =  courseRelationsComboBox.getSelectionModel().getSelectedItem();
		return (userInCourse != null) ? userInCourse.getRole() : null;
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
	
	@FXML
	void handleRefreshButtonClick(ActionEvent event) {
		rotateTransition.play();
		mainController.update();
		
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

	/**
	 * Custom implementation of JFXListCell to be used in ListView cell factories in order to 
	 * split ListView into sections.
	 */
	private class SectionedListCell<T> extends JFXListCell<T> {
		private final Predicate<T> sectionPredicate;
		private final Function<T, String> sectionNamingFunction;
		private final Function<T, String> itemNamingFunction;
		
		private final ListView<T> listView;
		private final VBox vbox;
		
		/**
		 * Creates a sectioned list cell.
		 * @param selectionPredicate - Predicate determining if this cell should contain a section header.
		 * @param sectionNamingFunction - Function yielding a header name for the section contained by this cell, if any.
		 * @param itemNamingFunction - Function yielding the text to be displayed in this cell.
		 * @param listView - Underlying ListView, given by the cell factory callback method.
		 */
		public SectionedListCell(Predicate<T> selectionPredicate, 
				Function<T, String> sectionNamingFunction, 
				Function<T, String> itemNamingFunction,
				ListView<T> listView) {
			super();
			this.sectionPredicate = selectionPredicate;
			this.sectionNamingFunction = sectionNamingFunction;
			this.itemNamingFunction = itemNamingFunction;
			this.listView = listView;
			
			// Initialize vbox
			vbox = createVBox();
			
			// ListCell properties
			setPadding(Insets.EMPTY);
			setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		}
		
		private JFXButton createButton(String text){
			JFXButton button = new JFXButton();
			button.setText(text);
			button.setMinWidth(Control.USE_PREF_SIZE);
			button.setMaxWidth(Double.MAX_VALUE);
			button.setPadding(new Insets(8, 12, 8, 20));
			button.getStyleClass().add("sectioned-list-cell");
			button.setAlignment(Pos.CENTER_LEFT);
			button.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
				listView.getSelectionModel().select(getIndex());
			});
			return button;
		}
		
		private VBox createVBox() {
			VBox vbox = new VBox();
			vbox.setMaxWidth(Double.MAX_VALUE);
			return vbox;
		}
		
		private Region createRegion() {
			Region region = new Region();
			region.setMaxWidth(Double.MAX_VALUE);
			region.setPrefHeight(1);
			region.getStyleClass().add("sectioned-separator");
			return region;
		}

		private Label createLabel(String sectionName) {
			Label label = new Label(sectionName);
			label.getStyleClass().add("sectioned-label");
			label.setPadding(new Insets(6, 12, 6, 8));
			label.setMaxWidth(Double.MAX_VALUE);
			label.setAlignment(Pos.CENTER_LEFT);
			return label;
		}
		
		@Override
		protected void updateItem(T item, boolean empty) {
			super.updateItem(item, empty);
			
			// Break if cell has no content
			if (empty || item == null) {
				setText(null);
				setGraphic(null);
				return;
			}

			// Create button, required for all cells
			String text = itemNamingFunction.apply(item);
			JFXButton button = createButton(text);
			
			// If section is requested
			if (sectionPredicate.test(item)) {
				String sectionName = sectionNamingFunction.apply(item);
				Label label = createLabel(sectionName);
				Region regionUpper = createRegion();
				Region regionLower = createRegion();
				
				vbox.getChildren().setAll(regionUpper, label, regionLower, button);
			} else {
				vbox.getChildren().setAll(button);
			}

			// Update graphic
			setGraphic(vbox);
		}
	}
}
