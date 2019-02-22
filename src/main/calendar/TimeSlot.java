package main.calendar;

import java.util.List;

import main.db.LoginManager;
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
		boolean myBool = false;
		for(Period period : periods) {
			if ( period.getStudentUsername().equals(LoginManager.getActiveUser().getUsername()) ) {
				myBool = true;
			}
		}
		return myBool;
	}
	
	/*
	 * TODO: IMPLEMENT METHODS REFERENCED FROM CALENDARGENERATOR
	 */
	
}
