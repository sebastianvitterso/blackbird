package main.announcement;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
		//AnnouncementManager.addAnnouncement(CourseManager.getCourse("TDT4100"),UserManager.getUser("seb"),Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.now())), "Tittel \n Masse text");
		
		List<Announcement> announcements = AnnouncementManager.getAnnouncementsFromCourse(course);
		for(Announcement announcement : announcements) {
			System.out.println("HER ER EN: " + announcement.getText());
			VBox announcementView = createAnnouncementView(announcement);
			view.getChildren().add(announcementView);
		}
	}
	public VBox createAnnouncementView(Announcement announcement) {
		VBox announcementView = new VBox();
		
		HBox announcementInfo = new HBox();
		Image collapseImage = new Image(getClass().getClassLoader().getResourceAsStream("icons/collapse.png"));
		ImageView collapse = new ImageView(collapseImage);
		collapse.setFitHeight(24);
		collapse.setFitWidth(24);
		announcementInfo.getChildren().add(collapse);
		VBox titleDate = new VBox();
		Text title = new Text(announcement.getTitle());
		titleDate.getChildren().add(title);
		Text date = new Text(announcement.getTimestamp().toLocalDateTime().toString());
		titleDate.getChildren().add(date);
		announcementInfo.getChildren().add(titleDate);
		announcementView.getChildren().add(announcementInfo);
		Text body = new Text(announcement.getText());
		announcementView.getChildren().add(body);
		collapse.setOnMouseClicked(e -> body.setVisible(!body.isVisible()));
		
		return announcementView;
	}
	
	public VBox getView() {
		return view;
	}
}
