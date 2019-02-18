package main.app;

import java.io.IOException;
import java.util.EnumMap;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import main.core.ui.Refreshable;

/**
 * Preloader class for FXML files, runnable on a separate thread.
 * 
 * @author Patrik
 */
public class Loader implements Runnable {

	private static EnumMap<View, FXMLLoader> viewToLoader = new EnumMap<>(View.class);
	private static EnumMap<View, Parent> viewToParent = new EnumMap<>(View.class);

	public Loader(FXMLLoader loginLoader, Parent loginParent) {
		viewToLoader.put(View.LOGIN_SCREEN, loginLoader);
		viewToParent.put(View.LOGIN_SCREEN, loginParent);
	}

	@Override
	public void run() {
		for (View view : View.values()) {
			// Login has already been loaded
			if (view == View.LOGIN_SCREEN) {
				continue;
			}

			// Create FXML Reference
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(view.getPathToFXML()));
			viewToLoader.put(view, loader);

			// Load FXML file and cache parent
			try {
				Parent parent = loader.load();
				viewToParent.put(view, parent);
			} catch (IOException e) {
				System.err.printf("FXML load failed: %s", view.name());
				e.printStackTrace();
			}
		}
	}

	public static FXMLLoader getFXMLLoader(View view) {
		return viewToLoader.get(view);
	}

	public static Parent getParent(View view) {
		return viewToParent.get(view);
	}

	public static Refreshable getController(View view) {
		return viewToLoader.get(view).getController();
	}

}