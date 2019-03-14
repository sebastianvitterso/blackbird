package main.announcement;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javafx.geometry.Insets;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import main.db.AnnouncementManager;
import main.db.CourseManager;
import main.db.LoginManager;
import main.db.UserManager;
import main.models.Announcement;
import main.models.Course;
import main.models.User;

public class AnnouncementsView {
	VBox view = new VBox();
	public AnnouncementsView(Course course) {
		//AnnouncementManager.addAnnouncement(CourseManager.getCourse("TDT4100"),UserManager.getUser("seb"),Timestamp.valueOf(LocalDateTime.now()), "Ingen forelesning 13.37", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
		//view.getStylesheets().add("stylesheets/announcementsview.css");
		addMargin(view, 20);
		List<Announcement> announcements = AnnouncementManager.getAnnouncementsFromCourse(course);
		Collections.sort(announcements);
		for(Announcement announcement : announcements) {
			VBox announcementView = createAnnouncementView(announcement);
			view.getChildren().add(announcementView);
		}
	}
	public VBox createAnnouncementView(Announcement announcement) {
		VBox announcementView = new VBox();
		
		Text title = new Text(announcement.getTitle());
		title.getStyleClass().add("title");
		announcementView.getChildren().add(title);
		addMargin(announcementView, 5);
		Text date = new Text(dateText(announcement.getTimestamp()));
		date.getStyleClass().add("date");
		announcementView.getChildren().add(date);
		addMargin(announcementView, 5);
		Text body = new Text(announcement.getText());
		body.setWrappingWidth(580);
		announcementView.getChildren().add(body);
		addMargin(announcementView, 20);
		return announcementView;
	}
	public String dateText(Timestamp timestamp) {
		LocalDateTime dateTime = timestamp.toLocalDateTime();
		int day = dateTime.getDayOfMonth();
		String[] months = new String[]{"januar","februar","mars","april","mai","juni","juli","august","september","oktober","november","desember"};
		String month = months[dateTime.getMonthValue()];
		int year = dateTime.getYear();
		String time = dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
		return String.format("Lagt inn %s. %s %s kl %s", day, month, year, time);
	}
	public void addMargin(VBox announcementView, int size) {
		Region region = new Region();
		region.setPrefHeight(size);
		announcementView.getChildren().add(region);
	}

	public VBox getView() {
		return view;
	}
}
