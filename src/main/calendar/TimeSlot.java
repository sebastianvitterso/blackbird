package main.calendar;

import java.util.List;

import main.db.LoginManager;
import main.db.PeriodManager;
import main.models.Period;
import main.models.Period.PeriodType;

public class TimeSlot {
	private List<Period> periods;
	
	public TimeSlot(List<Period> periods) {
		this.periods = periods;
	}
	
	public int getPeriodCount() {
		return periods.size();
	}
	
	public int getPeriodCountByType(PeriodType periodType) {
		return (int) periods.stream().filter(x -> periodType.equals(x.getPeriodType())).count();
	}
	
	// TODO: Fix title of this method... I mean come on Seb, this is bad. 
	public boolean amStudentInTimeSlot() {
		boolean myBool = false;
		for(Period period : periods) {
			if ( period.getStudentUsername().equals(LoginManager.getActiveUser().getUsername()) ) {
				myBool = true;
			}
		}
		return myBool;
	}
	
	// TODO: Same stuff here, Seb - fix your shit.
	public boolean amAssistantInTimeSlot() {
		for(Period period : periods) {
			if ( period.getAssistantUsername().equals(LoginManager.getActiveUser().getUsername()) ) {
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
			if(period.getStudentUsername() == LoginManager.getActiveUser().getUsername()) {
				PeriodManager.bookPeriod(period, LoginManager.getActiveUser());
				return true;
			}
		}
		return false;
	}
}
