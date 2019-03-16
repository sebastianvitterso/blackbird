package main.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import main.db.LoginManager;
import main.db.PeriodManager;
import main.models.Period.PeriodType;

public class TimeSlot {
	private List<Period> periods;
	
	public TimeSlot() {
		periods = new ArrayList<Period>();
	}
	
	
	public TimeSlot(List<Period> periods) {
		this.periods = periods;
	}
	
	public TimeSlot(Course course, String timestamp) {
		List<Period> periods = PeriodManager.getPeriodsFromCourseAndTime(course, timestamp);
		this.periods = periods;
	}
	
	public TimeSlot(Course course, LocalDateTime localDateTime) {
		List<Period> periods = PeriodManager.getPeriodsFromCourseAndTime(course, localDateTime);
		this.periods = periods;
	}
	
	public static String localDateTimeToSQLDateTime(LocalDateTime time){
		// 2019-02-25T08:00 -> 2019-02-25 08:00:00
		return time.toString().replace("T", " ").concat(":00");
	}
	
	public int getPeriodCount() {
		return periods.size();
	}
	
	public int getPeriodCountByType(PeriodType periodType) {
		return (int) periods.stream().filter(x -> periodType.equals(x.getPeriodType())).count();
	}
	
	// TODO: Fix title of this method... I mean come on Seb, this is bad. 
	public boolean amStudentInTimeSlot() {
		for(Period period : periods) {
			if ( LoginManager.getActiveUser().getUsername().equals(period.getStudentUsername()) ) {
				return true;
			}
		}
		return false;
	}
	
	// TODO: Same stuff here, Seb - fix your shit.
	public boolean amAssistantInTimeSlot() {
		for(Period period : periods) {
			if ( LoginManager.getActiveUser().getUsername().equals(period.getAssistantUsername()) ) {
				return true;
			}
		}
		return false;
	}
	
	public boolean bookTimeSlot() {
		for (Period period : periods) {
			if(period.isOfPeriodType(PeriodType.BOOKABLE)) {
				PeriodManager.bookPeriod(period, LoginManager.getActiveUser());
				return true;
			}
		}
		return false;
	}
	
	public boolean unbookTimeSlot() {
		for (Period period : periods) {
			if(LoginManager.getActiveUser().getUsername().equals(period.getStudentUsername())) {
				PeriodManager.unbookPeriod(period);
				return true;
			}
		}
		return false;
	}
	
	public boolean tutorTimeSlot() {
		for (Period period : periods) {
			if(period.isOfPeriodType(PeriodType.CREATED)) {
				PeriodManager.tutorPeriod(period, LoginManager.getActiveUser());
				return true;
			}
		}
		return false;
	}
	
	public boolean untutorTimeSlot() {
		for (Period period : periods) {
			if(LoginManager.getActiveUser().getUsername().equals(period.getAssistantUsername())) {
				PeriodManager.untutorPeriod(period);
				return true;
			}
		}
		return false;
	}
	
}
