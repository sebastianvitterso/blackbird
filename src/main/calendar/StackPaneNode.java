package main.calendar;

import java.time.LocalDateTime;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import main.utils.Role;

/**
 * Create an anchor pane that can store additional data.
 */
public class StackPaneNode extends StackPane {

	private CalendarGenerator parent;
    private LocalDateTime date;
    private int x;
    private int y;

    public StackPaneNode(Node... children) {
        super(children);
        // Add action handler for mouse clicked
        this.setOnMouseClicked(e -> {onClicked(e);});
    }
    public void onClicked(MouseEvent e) {
    	if (parent == null)
    		return;
    	if(parent.getRole() == Role.PROFESSOR) {
    		if (!e.isControlDown() && !isFocused())
    			parent.resetSelections();
    		setFocused(!isFocused());
    	} else if (parent.getRole() == Role.STUDENT) {
    		this.parent.BookUnbookTimeSlot(date, x, y);
    	} else {
    		this.parent.TutorUntutorTimeSlot(date, x,y);
    	}
    }
    public void removeFocus() {
    	setFocused(false);
    }
    public void addText(String text) {
    	addText(text, false);
	}
    public void addText(String text, boolean styled) {
		Text txt = new Text(text);
		StackPane.setAlignment(txt, Pos.CENTER);
		getChildren().add(txt);
		if (styled) {
			txt.getStyleClass().add("text2");
		} else {
			txt.getStyleClass().add("text");
		}
    }
    public void setParent(CalendarGenerator parent) {
    	this.parent = parent;
    }

    public void setDateTime(LocalDateTime date) {
        this.date = date;
    }
    public void setX(int x) {
    	this.x = x;
    }
    public void setY(int y) {
    	this.y = y;
    }
    public LocalDateTime getDateTime() {
    	return date;
    }
}
