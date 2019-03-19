package main.core.ui.popups;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import main.components.AnnouncementBox;
import main.db.LoginManager;
import main.db.UserManager;
import main.models.Period;
import main.models.TimeSlot;
import main.models.User;
import main.utils.PostInitialize;

public class CalendarPopupController {
	
	
	@FXML private JFXDialog dialog;
	@FXML private Label title;
	@FXML private VBox bookingVBox;
	
	private User user;
	
	
	////Initialization ////
	   /**
	    * Initializes every component in the user interface.
	    * This method is automatically invoked when loading the corresponding FXML file.
	    */
		@FXML 
		private void initialize() {
			// Bind 'register' button to being disabled when no title and description is given.
//			registerButton.disableProperty().bind(announcementTitleTextField.textProperty().isEmpty());
//			registerButton.disableProperty().bind(announcementDescriptionTextArea.textProperty().isEmpty());
			
		}
		
		/**
	     * Connects this controller to associated JFXDialog.
	     */
	    public void connectDialog(JFXDialog dialog) {
	    	this.dialog = dialog;
	    }
	    
	    public void createBookList(TimeSlot timeSlot, LocalDateTime localDateTime) {
	    	user = LoginManager.getActiveUser();
	    	String firstText = new SimpleDateFormat("dd. MMM HH:mm").format(Timestamp.valueOf(localDateTime));
	    	String secondText = new SimpleDateFormat("HH:mm").format(Timestamp.valueOf(localDateTime.plusMinutes(30)));
	    	title.setText(firstText + " - " + secondText);
	    	List<HBox> hBoxes = timeSlot.getPeriods().stream()
	    			.filter(period -> period.getAssistantUsername() != null)
	    			.filter(period -> user.getUsername().equals(period.getStudentUsername()) || period.getStudentUsername() == null)
	    			.map(period -> createHBox(period))
	    			.collect(Collectors.toList());
	    	bookingVBox.getChildren().clear();
	    	bookingVBox.getChildren().addAll(hBoxes);
	    	bookingVBox.getStylesheets().add("stylesheets/assignment_box.css");
	    }
	    public HBox createHBox(Period period) {
	    	HBox hBox = new HBox();
	    	hBox.setPadding(new Insets(0,8,0,0));
	    	hBox.setSpacing(20);
	    	hBox.setAlignment(Pos.CENTER);
	    	
	    	Label label = new Label(UserManager.getUser(period.getAssistantUsername()).getName());
	    	Region region = new Region();
	    	JFXButton button = new JFXButton("Book");
	    	
	    	button.getStyleClass().add("body-background");
			button.getStyleClass().add("root");
	    	
	    	hBox.getChildren().addAll(label, region, button);
	    	HBox.setHgrow(region, Priority.ALWAYS);
	    	return hBox;
	    }
		 /**
	     * Runs any methods that require every controller to be initialized.
	     * This method should only be invoked by the FXML Loader class.
	     */
	    @PostInitialize
	    private void postInitialize() {
	    	
	    }

}
