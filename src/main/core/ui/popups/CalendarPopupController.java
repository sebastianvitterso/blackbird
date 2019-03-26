package main.core.ui.popups;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;

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
import main.components.Calendar;
import main.db.LoginManager;
import main.db.PeriodManager;
import main.db.UserManager;
import main.models.Period;
import main.models.TimeSlot;
import main.models.User;

public class CalendarPopupController {
	@FXML private Text title;
	@FXML private VBox bookingVBox;

	private Calendar calendar;
	private User user;
	private boolean amStudentInTimeSlot;

    public void connectCalendar(Calendar calendar) {
    	this.calendar = calendar;
    }
    
    public void createBookList(TimeSlot timeSlot, LocalDateTime localDateTime) {
    	amStudentInTimeSlot = timeSlot.amStudentInTimeSlot();
    	
    	user = LoginManager.getActiveUser();
    	String firstText = new SimpleDateFormat("dd. MMM HH:mm").format(Timestamp.valueOf(localDateTime));
    	String secondText = new SimpleDateFormat("HH:mm").format(Timestamp.valueOf(localDateTime.plusMinutes(30)));
    	title.setText(firstText + " - " + secondText);
    	List<HBox> bookHBoxes = timeSlot.getPeriods().stream()
    			.filter(period -> period.getAssistantUsername() != null)
    			.filter(period -> user.getUsername().equals(period.getStudentUsername()) || period.getStudentUsername() == null)
    			.map(period -> createBookHBox(period, localDateTime))
    			.collect(Collectors.toList());
    	bookingVBox.getChildren().clear();
    	bookingVBox.getChildren().addAll(bookHBoxes);
    	bookingVBox.getStylesheets().add("stylesheets/assignment_box.css");
    }
    public HBox createBookHBox(Period period, LocalDateTime localDateTime) {
    	HBox hBox = new HBox();
    	hBox.setPadding(new Insets(0,8,0,0));
    	hBox.setSpacing(20);
    	hBox.setAlignment(Pos.CENTER);
    	
    	Label nameLabel = new Label(UserManager.getUser(period.getAssistantUsername()).getName());
    	Region region = new Region();
    	
    	boolean booked = user.getUsername().equals(period.getStudentUsername());
    	JFXButton bookButton = new JFXButton(booked ? "Unbook" : "Book");
    	bookButton.setDisable(amStudentInTimeSlot && !booked);
    	bookButton.setOnAction(e -> {
    		if (booked)
    			PeriodManager.unbookPeriod(period); 	
    		else
    			PeriodManager.bookPeriod(period, user);	
    		calendar.updateAllCells();
    		createBookList(calendar.getTimeSlot(localDateTime), localDateTime);
    	});
    	
    	bookButton.getStyleClass().add("body-background");
    	bookButton.getStyleClass().add("root");
    	bookButton.setMinWidth(72);
    	
    	hBox.getChildren().addAll(nameLabel, region, bookButton);
    	HBox.setHgrow(region, Priority.ALWAYS);
    	return hBox;
    }
}
