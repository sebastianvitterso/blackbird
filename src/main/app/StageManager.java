package main.app;

import java.io.IOException;
import java.util.EnumMap;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.core.ui.CalendarController;
import main.utils.Refreshable;
import main.utils.View;

/**
 * Class for managing transitions between different scenes in the application. 
 */
public class StageManager {
	private static Stage stage;
	private static Image icon;
	private static EnumMap<View, Scene> viewToScene;
	private static Thread loaderThread;
	
	
	/**
	 * This method must be called from FXML Application thread on startup.
	 * @param stage - stage given by the FXML Application thread's {@code run} method.
	 * @see Runnable
	 */
	protected static void initialize(Stage stage) throws IOException {
		StageManager.stage = stage;
		StageManager.icon = new Image(StageManager.class.getClassLoader().getResourceAsStream("icons/logo_transparent.png"));
		StageManager.viewToScene = new EnumMap<>(View.class);
		
		// Load login elements prior to initializing loader.
		FXMLLoader loginLoader = new FXMLLoader(StageManager.class.getClassLoader().getResource(View.LOGIN_VIEW.getPathToFXML()));
		Parent loginParent = loginLoader.load();

		// Preloading all FXML files while in login screen.
		Loader loader = new Loader(loginLoader, loginParent);
		loaderThread = new Thread(loader);
		loaderThread.start();
	}
	
	/**
	 * Sets the FXML node to be displayed on screen, specified by {@code view}. 
	 * Closes the previous stage and creates a new one.
	 */
	public static void loadView(View view) {
		// Close previous stage
		stage.close();
		
		// Load root node in FXML hierarchy
		Scene scene = prepareScene(view);
		
		// Load new stage
		stage = new Stage();
		stage.setScene(scene);
		stage.setTitle(view.getTitle());
		stage.getIcons().add(icon);
		stage.sizeToScene();
		stage.centerOnScreen();
		
		// Set view-specific properties.
		switch (view) {
		case LOGIN_VIEW:
			scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
			stage.initStyle(StageStyle.TRANSPARENT);
			break;

		case ADMIN_VIEW:
			stage.setResizable(true);
			break;
		
		case MAIN_VIEW:
			stage.setResizable(true);
			break;
			
		default:
			break;
		}
		
		// Clear and display
		Object controller = Loader.getController(view);
		if (controller instanceof Refreshable)
			((Refreshable) controller).refresh();
		else
			System.err.printf("StageManager can only handle controllers of type 'Refreshable', missing in '%s'.", controller.getClass().getSimpleName());
		
		stage.show();
	}
	
	/**
	 * Returns the thread used when running the {@link Loader} class.
	 */
	protected static Thread getLoaderThread() {
		return loaderThread;
	}
	
	/**
	 * Returns the {@link Scene} object associated with given {@link View}.
	 * If no scene is present, a new one is created.
	 */
	private static Scene prepareScene(View view) {
		return viewToScene.computeIfAbsent(view, v -> new Scene(Loader.getParent(v)));
	}
	
	
	
}
