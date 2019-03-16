package main.core.ui;

import java.util.function.Function;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import main.app.Loader;
import main.utils.PostInitialize;
import main.utils.Refreshable;
import main.utils.View;

public class MainController implements Refreshable {
	private MenuController menuController;
	
	private View currentView;
	private Region currentParent;
	private Refreshable currentController;

	@FXML private StackPane rootPane;
    @FXML private StackPane menuPane;
    @FXML private StackPane outerStackPane;
    @FXML private StackPane contentPane;
    @FXML private ScrollPane scrollPane;
    
    
    /**
     * Initializes every component in the user interface.
     * This method is automatically invoked when loading the corresponding FXML file.
     */
    @FXML
    private void initialize() {
    	smoothScrolling(scrollPane);
    }
    
    /**
     * Runs any methods that require every controller to be initialized.
     * This method should only be invoked by the FXML Loader class.
     */
    @PostInitialize
    private void postInitialize() {
    	menuController = Loader.getController(View.MENU_VIEW);
    	menuPane.getChildren().setAll(Loader.getParent(View.MENU_VIEW));
//    	loadTab(View.OVERVIEW_VIEW);
    }
    
	/**
	 * Sets the tab to be displayed on screen, specified by {@code view}.
	 */
	public void loadTab(View view) {
		// Update current tab references
		currentView = view;
		currentParent = (Region) Loader.getParent(view);
		currentController = Loader.getController(view);
		
		// Cover scrollpane by binding tab height
		currentParent.minHeightProperty().bind(scrollPane.heightProperty());
		
		// Refresh tab
		currentController.refresh();
		
		// Inject new tab
		contentPane.getChildren().setAll(currentParent);
	}
	
	/**
	 * {@inheritDoc}
	 * This is done for every {@code Refreshable} contained in this interface.
	 */
	@Override
	public void update() {
		// Update menu
		menuController.update();
		
		// Update tab
		if (currentController != null)
			currentController.update();
		
		// Reset focus
		rootPane.requestFocus();
	}

	/**
	 * {@inheritDoc}
	 * This is done for every {@code Refreshable} contained in this interface.
	 */
	@Override
	public void clear() {
		// Clear menu
		menuController.clear();
		
		// Clear tab
		if (currentController != null)
			currentController.clear();
	}
	
	public View getCurrentView() {
		return currentView;
	}
	
	public Region getCurrentParent() {
		return currentParent;
	}
	
	public Refreshable getCurrentController() {
		return currentController;
	}
	
	public StackPane getOuterStackPane() {
		return outerStackPane;
	}
	
	/**
	 * Implements custom scrolling behavior for ScrollPanes.
	 */
	public static void customScrolling(ScrollPane scrollPane, DoubleProperty scrollDirection, Function<Bounds, Double> sizeFunc) {
        final double[] frictions = {0.99, 0.1, 0.05, 0.04, 0.03, 0.02, 0.01, 0.04, 0.01, 0.008, 0.008, 0.008, 0.008, 0.0006, 0.0005, 0.00003, 0.00001};
        final double[] pushes = {1};
        final double[] derivatives = new double[frictions.length];

        Timeline timeline = new Timeline();
        final EventHandler<MouseEvent> dragHandler = event -> timeline.stop();
        final EventHandler<ScrollEvent> scrollHandler = event -> {
            if (event.getEventType() == ScrollEvent.SCROLL) {
                int direction = event.getDeltaY() > 0 ? -1 : 1;
                for (int i = 0; i < pushes.length; i++) {
                    derivatives[i] += direction * pushes[i];
                }
                if (timeline.getStatus() == Animation.Status.STOPPED) {
                    timeline.play();
                }
                event.consume();
            }
        };
        if (scrollPane.getContent().getParent() != null) {
            scrollPane.getContent().getParent().addEventHandler(MouseEvent.DRAG_DETECTED, dragHandler);
            scrollPane.getContent().getParent().addEventHandler(ScrollEvent.ANY, scrollHandler);
        }
        scrollPane.getContent().parentProperty().addListener((o,oldVal, newVal)->{
            if (oldVal != null) {
                oldVal.removeEventHandler(MouseEvent.DRAG_DETECTED, dragHandler);
                oldVal.removeEventHandler(ScrollEvent.ANY, scrollHandler);
            }
            if (newVal != null) {
                newVal.addEventHandler(MouseEvent.DRAG_DETECTED, dragHandler);
                newVal.addEventHandler(ScrollEvent.ANY, scrollHandler);
            }
        });
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(3), (event) -> {
            for (int i = 0; i < derivatives.length; i++) {
                derivatives[i] *= frictions[i];
            }
            for (int i = 1; i < derivatives.length; i++) {
                derivatives[i] += derivatives[i - 1];
            }
            double dy = derivatives[derivatives.length - 1];
            double size = sizeFunc.apply(scrollPane.getContent().getLayoutBounds());
            scrollDirection.set(Math.min(Math.max(scrollDirection.get() + dy / size, 0), 1));
            if (Math.abs(dy) < 0.001) {
                timeline.stop();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
    }

	/**
	 * Implements smooth scrolling behavoir for ScrollPanes.
	 */
    public static void smoothScrolling(ScrollPane scrollPane) {
        customScrolling(scrollPane, scrollPane.vvalueProperty(), bounds -> bounds.getHeight());
    }
}
