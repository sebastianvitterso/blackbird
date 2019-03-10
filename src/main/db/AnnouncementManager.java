package main.db;

import java.util.List;
import java.util.Map;

import main.models.Announcement;
import main.models.Course;

public class AnnouncementManager {
	public static List<Announcement> getAnnouncements(){
		List<Map<String, String>> announcementMaps = DatabaseManager.sendQuery("SELECT * FROM announcement");
		return DatabaseUtil.MapsToAnnouncements(announcementMaps);
	}
	
	public static List<Announcement> getAnnouncementsFromCourse(Course course){
		List<Map<String, String>> announcementMaps = DatabaseManager.sendQuery(
				String.format("SELECT * FROM announcement WHERE course_code = '%s'", course.getCourseCode()));
		return DatabaseUtil.MapsToAnnouncements(announcementMaps);
	}
	
	
}
