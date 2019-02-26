package main.core.ui.tabs;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import main.app.Loader;
import main.core.ui.CalendarController;
import main.utils.PostInitialize;
import main.utils.Refreshable;
import main.utils.View;

public class SchedulingController implements Refreshable {
	private CalendarController calendarController;
	
	@FXML private StackPane calendarPane;
	
	/**
     * Runs any methods that require every controller to be initialized.
     * This method should only be invoked by the FXML Loader class.
     */
    @PostInitialize
    private void postInitialize() {
    	calendarController = Loader.getController(View.CALENDAR_VIEW);
    	calendarPane.getChildren().setAll(Loader.getParent(View.CALENDAR_VIEW));
    }
	
	
	@Override
	public void update() {
		calendarController.update();
	}
	
	@Override
	public void clear() {
		
	}
	
	
}
