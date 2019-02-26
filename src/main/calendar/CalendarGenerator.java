 package main.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import main.db.CourseManager;
import main.db.LoginManager;
import main.db.PeriodManager;
import main.models.Course;
import main.models.Period;
import main.models.Period.PeriodType;
import main.utils.Role;

public class CalendarGenerator {
	private int weeknum;
	private LocalDate startOfWeek;
	private VBox view;
	private Map<LocalDateTime, TimeSlot> timeSlots = new HashMap<LocalDateTime, TimeSlot>();
	private StackPaneNode[][] stackPaneNodes = new StackPaneNode[5][16];
	private StackPaneNode[] dayPaneNodes = new StackPaneNode[5];
	private Course course;
	public CalendarGenerator() {
		weeknum = getRelevantWeek();
		startOfWeek = calculateStartOfWeek();

		course = CourseManager.getCourse("TDT4100");
		
		GridPane dayLabels = createDayLabels();
		GridPane calendar = createCalendar();
		view = new VBox(dayLabels, calendar);
	}
	public Role getRole() {
		//@@@@@ Skal hentes av seg selv ved senere implementasjon
		return LoginManager.getActiveUser().getUsername().equals("bea") ? Role.STUDENT : Role.ASSISTANT; 
	}
	//Current week if monday-friday, else next week
	public int getRelevantWeek() {
		TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
		int weekNumber = LocalDate.now().plusDays(2).get(woy);
		return weekNumber;
	}
	
	public void changeWeekUpdate(int weeknum) {
		this.weeknum = weeknum;
		startOfWeek = calculateStartOfWeek();
		updateDayPaneNodes();
		resetSelections();
		updateAllCells();
	}
	public void resetSelections() {
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 16; y++) {
				stackPaneNodes[x][y].removeFocus();
			}
		}
	}
	//Amount must be -1 or 1
	public void changeSelectedAvailableSlots(int amount) {
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 16; y++) {
				StackPaneNode node = stackPaneNodes[x][y];
				if (node.isFocused()) {
					String time = TimeSlot.localDateTimeToSQLDateTime(node.getDateTime());
					System.out.println(time);
					if (amount == 1) {
						String courseCode = course.getCourseCode();
						String username = LoginManager.getActiveUser().getUsername();
						PeriodManager.addPeriod(courseCode, time, username);
					} else {
						//TODO: Legg til at den sletter created først
						List<Period> periods = PeriodManager.getPeriodsFromCourseAndTime(course, node.getDateTime());
						if(periods.size() > 0)
							PeriodManager.deletePeriod(periods.get(0));	
					}
					updateCell(x+1,y);
				}
			}
		}
	}
	
	public LocalDate calculateStartOfWeek() {
		TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
		int dayNumInWeek = LocalDate.now().getDayOfWeek().getValue();
		int thisWeekNumber = LocalDate.now().get(woy);
		if (thisWeekNumber > weeknum) {
			return LocalDate.now().minusDays(dayNumInWeek - 1).minusDays((thisWeekNumber - weeknum) * 7);
		} else {
			return LocalDate.now().minusDays(dayNumInWeek - 1).plusDays((weeknum - thisWeekNumber) * 7);
		}
	}

	private GridPane createDayLabels() {
		GridPane dayLabels = new GridPane();
		Integer col = 0;
		for (int n = 0; n < 6; n++) {
			StackPaneNode sp = new StackPaneNode();
			sp.setPrefSize(400, 10);
			dayLabels.add(sp, col++, 0);
			if (n == 0) {
				sp.addText("               ");
				//Empty string is there to make the StackPaneNode 
				//take up as much space as the time columns
			} else {
				dayPaneNodes[n-1] = sp;	
			}
		}
		updateDayPaneNodes();
		return dayLabels;
	}

	private void updateDayPaneNodes() {
		String[] days = new String[] {"Mon", "Tue", "Wed", "Thu", "Fri" };
		for (int n = 0; n < 5; n++) {
			LocalDate dateOfWeek = startOfWeek.plusDays(n);
			days[n] += " " + dateOfWeek.getDayOfMonth() + "." + dateOfWeek.getMonthValue();
			dayPaneNodes[n].getChildren().clear();
			dayPaneNodes[n].addText(days[n]);
		}
	}

	private GridPane createCalendar() {
		GridPane calendar = new GridPane();
		calendar.setGridLinesVisible(true);

		// Create rows and columns with anchor panes for the calendar
		for (int x = 0; x <= 5; x++) {
			for (int y = 0; y < 16; y++) {
				createCell(calendar, x, y);
			}
		}
		return calendar;
	}

	public void updateAllCells() {
		LocalDateTime startOfWeekMidnight = LocalDateTime.of(startOfWeek, localTimeOf(0));
		Map<String, TimeSlot> timeSlotMap = PeriodManager.getPeriodsFromCourseAndWeekStartTime(course, startOfWeekMidnight);
		for (int x = 1; x <= 5; x++) {
			for (int y = 0; y < 16; y++) {
				String timeStamp = TimeSlot.localDateTimeToSQLDateTime(calculateDateTime(x,y));
				updateCell(x, y, timeSlotMap.get(timeStamp));
			}
		}
	}

	public void createCell(GridPane calendar, int x, int y) {
		StackPaneNode sp = new StackPaneNode();
		sp.setPrefSize(400, 200);
		Border border = new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.1)));
		sp.setBorder(border);
		calendar.add(sp, x, y);

		boolean isTimeColumn = (x == 0);
		if (isTimeColumn) {
			// Adder tid (f.eks "8:00 - 8:30") til column
			double hours = 8 + y / 2.0;
			String startTimeString = localTimeOf(hours).toString();
			String endTimeString = localTimeOf(hours + 0.5f).toString();
			sp.addText(startTimeString + " - " + endTimeString);
			sp.getStyleClass().add("available" + (y % 2));
			return;
		}

		sp.setParent(this);
		sp.setX(x);
		sp.setY(y);

		stackPaneNodes[x - 1][y] = sp;
	}

	public LocalDateTime calculateDateTime(int x, int y) {
		double hours = 8 + y / 2.0;
		LocalDate date = startOfWeek.plusDays(x - 1);
		LocalTime time = localTimeOf(hours);
		return LocalDateTime.of(date, time);
	}

	public void updateCell(int x, int y) {
		LocalDateTime cellDateTime = calculateDateTime(x, y);
		TimeSlot timeSlot = new TimeSlot(course, cellDateTime);
		timeSlots.put(cellDateTime, timeSlot);
		updateStackPaneNode(stackPaneNodes[x - 1][y], timeSlot, cellDateTime, y);
	}
	
	public void updateCell(int x, int y, TimeSlot timeSlot) {
		if (timeSlot == null) 
			timeSlot = new TimeSlot(); // fix for nullPointerExceptions. 
		LocalDateTime cellDateTime = calculateDateTime(x, y);
		timeSlots.put(cellDateTime, timeSlot);
		updateStackPaneNode(stackPaneNodes[x - 1][y], timeSlot, cellDateTime, y);
	}

	public LocalTime localTimeOf(double hours) {
		int minutes = (int) ((hours % 1) * 60);
		return LocalTime.of((int) hours, minutes);
	}

	public void updateStackPaneNode(StackPaneNode sp, TimeSlot timeSlot, LocalDateTime dateTime, int y) {
		sp.setDateTime(dateTime);
		sp.getStyleClass().clear();
		sp.getChildren().clear();
		String text = "";
		Role myRole = getRole();
		int created = timeSlot.getPeriodCountByType(PeriodType.CREATED);
		int bookable = timeSlot.getPeriodCountByType(PeriodType.BOOKABLE);
		int booked = timeSlot.getPeriodCountByType(PeriodType.BOOKED);
		if (myRole == Role.STUDENT) {
			if ((bookable + booked) == 0) {
				sp.getStyleClass().add("unavailable" + (y % 2));
				return;
			} else if (timeSlot.amStudentInTimeSlot()) {
				sp.getStyleClass().add("selected" + (y % 2));
				text = "Bestilt";
			} else if (bookable == 0) {
				sp.getStyleClass().add("taken" + (y % 2));
				text = "Opptatt";
			} else {
				sp.getStyleClass().add("available" + (y % 2));
				text = "Ledig";
			}
			sp.addText(text + "(" + booked + "/" + (bookable + booked) + ")", true);
		} else if (myRole == Role.ASSISTANT) {
			if (timeSlot.getPeriodCount() == 0) {
				sp.getStyleClass().add("unavailable" + (y % 2));
				return;
			} else if (timeSlot.amAssistantInTimeSlot()) {
				sp.getStyleClass().add("selected" + (y % 2));
				text = "Valgt";
			} else if (created == 0) {
				sp.getStyleClass().add("taken" + (y % 2));
				text = "Fullt";
			} else {
				sp.getStyleClass().add("available" + (y % 2));
				text = "Ikke fullt";
			}
			sp.addText(text + "(" + (bookable + booked) + "/" + (bookable + booked+ created) + ")", true);
		} else {
			if (timeSlot.getPeriodCount() == 0) {
				sp.getStyleClass().add("unavailable" + (y % 2));
				return;
			} else {
				sp.getStyleClass().add("available" + (y % 2));
				text = "Plasser: ";
			}
			sp.addText(text + timeSlot.getPeriodCount(), true);
		}
		

	}
	//Updater før slik at vi har riktig data, updater etterpå for å vise riktig data etter booking/unbooking
	public void BookUnbookTimeSlot(LocalDateTime dateTime, int x, int y) {
		updateCell(x, y);
		TimeSlot timeSlot = timeSlots.get(dateTime);
		if (timeSlot.amStudentInTimeSlot()) {
			timeSlot.unbookTimeSlot();
		} else if (timeSlot.getPeriodCountByType(PeriodType.BOOKABLE) > 0){
			timeSlot.bookTimeSlot();
		}
		updateCell(x, y);
	}
	//Updater før slik at vi har riktig data, updater etterpå for å vise riktig data etter tutor/untutor
	public void TutorUntutorTimeSlot(LocalDateTime dateTime, int x, int y) {
		updateCell(x, y);
		TimeSlot timeSlot = timeSlots.get(dateTime);
		if (timeSlot.amAssistantInTimeSlot()) {
			timeSlot.untutorTimeSlot();
		} else if(timeSlot.getPeriodCountByType(PeriodType.CREATED) > 0){
			timeSlot.tutorTimeSlot();
		}
		updateCell(x, y);
	}

	public VBox getView() {
		return view;
	}
}
