 package main.calendar;

import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CalendarGenerator {
	private int weeknum;
	private LocalDate startOfWeek;
	private VBox view;
	private Map<LocalDateTime, Room> rooms = new HashMap<LocalDateTime, Room>();
	private StackPaneNode[][] stackPaneNodes = new StackPaneNode[5][16];

	public CalendarGenerator() {
		weeknum = getRelevantWeek();
		startOfWeek = calculateStartOfWeek();
		
		demoOppsett(); //Byttes med f.eks lastInnInfo() når databasen er koblet til
		
		GridPane dayLabels = createDayLabels();
		GridPane calendar = createCalendar();
		view = new VBox(dayLabels, calendar);
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
		updateAllCells();
	}
	//TODO: bug ved årendring, må fikses
	public LocalDate calculateStartOfWeek() {
		TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
		int dayNumInWeek = LocalDate.now().getDayOfWeek().getValue();
		int thisWeekNumber = LocalDate.now().get(woy);
		if (thisWeekNumber > weeknum) {
			return LocalDate.now().minusDays(dayNumInWeek - 1).plusDays((thisWeekNumber - weeknum) * 7);
		} else {
			return LocalDate.now().minusDays(dayNumInWeek - 1).minusDays((weeknum - thisWeekNumber) * 7);
		}
	}
	
	private void demoOppsett() {
		rooms.put(LocalDateTime.of(startOfWeek, localTimeOf(8)), new Room(0, 4));
		rooms.put(LocalDateTime.of(startOfWeek, localTimeOf(8.5)), new Room(0, 4));
		rooms.put(LocalDateTime.of(startOfWeek, localTimeOf(9)), new Room(0, 4));
		rooms.put(LocalDateTime.of(startOfWeek, localTimeOf(9.5)), new Room(0, 4));
		rooms.put(LocalDateTime.of(startOfWeek, localTimeOf(10)), new Room(3, 4));
		rooms.put(LocalDateTime.of(startOfWeek, localTimeOf(10.5)), new Room(3, 4));
		rooms.put(LocalDateTime.of(startOfWeek.plusDays(2), localTimeOf(10)), new Room(4, 4));
		rooms.put(LocalDateTime.of(startOfWeek.plusDays(2), localTimeOf(10.5)), new Room(4, 4));
		rooms.put(LocalDateTime.of(startOfWeek.plusDays(2), localTimeOf(11)), new Room(2, 4));
		rooms.put(LocalDateTime.of(startOfWeek.plusDays(2), localTimeOf(11.5)), new Room(2, 4));
		rooms.put(LocalDateTime.of(startOfWeek.plusDays(2), localTimeOf(12)), new Room(0, 4));
		rooms.put(LocalDateTime.of(startOfWeek.plusDays(2), localTimeOf(12.5)), new Room(0, 4));
	}

	private GridPane createDayLabels() {
		// Empty string below is there to make the StackPaneNode take up as much space
		// as the time columns
		String[] days = new String[] { "               ", "Mon", "Tue", "Wed", "Thu", "Fri" };
		for (int n = 1; n <= 5; n++) {
			LocalDate dateOfWeek = startOfWeek.plusDays(n - 1);
			days[n] += " " + dateOfWeek.getDayOfMonth() + "." + dateOfWeek.getMonthValue();
		}
		return createLabels(days);
	}

	private GridPane createLabels(String[] labelnames) {
		GridPane dayLabels = new GridPane();
		// dayLabels.setPrefWidth(1200);
		Integer col = 0;
		for (String txt : labelnames) {
			StackPaneNode sp = new StackPaneNode();
			sp.setPrefSize(400, 10);
			sp.addText(txt);
			dayLabels.add(sp, col++, 0);
		}
		return dayLabels;
	}

	private GridPane createCalendar() {
		GridPane calendar = new GridPane();
		// calendar.setPrefSize(600, 400);
		calendar.setGridLinesVisible(true);

		// Create rows and columns with anchor panes for the calendar
		for (int x = 0; x <= 5; x++) {
			for (int y = 0; y < 16; y++) {
				createCell(calendar, x, y);
			}
		}
		updateAllCells();
		return calendar;
	}

	public void updateAllCells() {
		for (int x = 1; x <= 5; x++) {
			for (int y = 0; y < 16; y++) {
				updateCell(x, y);
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
		LocalDateTime cellDateTime = calculateDateTime(x, y);
		sp.setDateTime(cellDateTime);
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
		Room room = rooms.get(cellDateTime);
		updateStackPaneNode(stackPaneNodes[x - 1][y], room, y);
	}

	public LocalTime localTimeOf(double hours) {
		int minutes = (int) ((hours % 1) * 60);
		return LocalTime.of((int) hours, minutes);
	}

	public void updateStackPaneNode(StackPaneNode sp, Room room, int y) {
		sp.getStyleClass().clear();
		sp.getChildren().clear();
		String text = "";
		if (room == null || room.getAmountAvailable() == 0) {
			sp.getStyleClass().add("unavailable" + (y % 2));
			return;
		} else if (room.getInRoom()) {
			sp.getStyleClass().add("selected" + (y % 2));
			text = "Bestilt";
		} else if (room.getAmountBooked() == room.getAmountAvailable()) {
			sp.getStyleClass().add("taken" + (y % 2));
			text = "Opptatt";
		} else {
			sp.getStyleClass().add("available" + (y % 2));
			text = "Ledig";
		}
		sp.addText(text + "(" + room.getAmountBooked() + "/" + room.getAmountAvailable() + ")", true);
	}

	public void AddRemoveTime(LocalDateTime dateTime, int x, int y) {
		Room room = rooms.get(dateTime);
		if (room == null)
			return;
		if (room.getInRoom()) {
			room.setInRoom(false);
			room.decreaseBooked();
		} else {
			if (room.getAmountBooked() == room.getAmountAvailable())
				return;
			room.setInRoom(true);
			room.increaseBooked();
		}
		updateCell(x, y);
	}

	public VBox getView() {
		return view;
	}
}
