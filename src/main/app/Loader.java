package main.app;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import main.utils.PostInitialize;
import main.utils.View;

/**
 * Preloader class for FXML files, runnable on a separate thread.
 */
public class Loader implements Runnable {
	private static EnumMap<View, FXMLLoader> viewToLoader = new EnumMap<>(View.class);
	private static EnumMap<View, Parent> viewToParent = new EnumMap<>(View.class);

	
	public Loader(FXMLLoader loginLoader, Parent loginParent) {
		viewToLoader.put(View.LOGIN_VIEW, loginLoader);
		viewToParent.put(View.LOGIN_VIEW, loginParent);
	}

	
	/**
	 * {@inheritDoc}
	 * Loads FXML files followed by invocations to non-private {@code @PostInitialize} methods in controllers
	 */
	@Override
	public void run() {
		// Load FXML views
		loadViews();
		
		// Invoke methods with @PostInitialize annotation
		invokeMethodsAnnotatedWith(PostInitialize.class);
		
		System.out.println("FXML Loader finished.");
	}

	/**
	 * Loads FXML files, storing references to FXMLLoaders and Parents in an enumeration mapping.
	 */
	private void loadViews() {
		for (View view : View.values()) {
			// Break if view is not implemented
			if (view.getPathToFXML() == null)
				continue;
			
			// Break if view should not load on startup
			if (!view.isLoadOnStartup())
				continue;
			
			// Login has already been loaded
			if (view == View.LOGIN_VIEW)
				continue;

			// Create FXML Reference
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(view.getPathToFXML()));
			viewToLoader.put(view, loader);

			// Load FXML file and cache parent
			try {
				Parent parent = loader.load();
				System.out.printf("View successfully loaded: %s%n", view.name());
				viewToParent.put(view, parent);
			} catch (IOException e) {
				System.err.printf("FXML load failed: %s", view.name());
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Invoke all methods with the specified annotation, regardless of access modifier.
	 */
	private void invokeMethodsAnnotatedWith(Class<? extends Annotation> annotation) {
		// Handle @PostInitialize annotations
		for (View view : View.values()) {
			// Break if view is not implemented
			if (view.getPathToFXML() == null)
				continue;

			// Break if view should not load on startup
			if (!view.isLoadOnStartup())
				continue;
			
			Object controller = getController(view);
			List<Method> annotatedMethods = getMethodsAnnotatedWith(controller.getClass(), annotation);
			
			// Invoke every method with specified annotation.
			for (Method method : annotatedMethods) {
				try {
					method.setAccessible(true);
					method.invoke(controller);
					System.out.printf("@PostInitialize invoked: View: %s, Method: %s%n", view.name(), method.getName());
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Returns the image located at the specified file path as an {@code Image}.
	 */
	public static Image getImage(String path) {
		return new Image(Loader.class.getClassLoader().getResourceAsStream(path));
	}
	
	/**
	 * Returns parent node in the FXML hierarchy represented by the view specified.
	 */
	public static Parent getParent(View view) {
		return viewToParent.get(view);
	}

	/**
	 * Returns the controller paired with the FXMLLoader for the specified view.
	 */
	public static <T> T getController(View view) {
		return viewToLoader.get(view).getController();
	}
	
	/**
	 * Returns a newly initialized FXMLLoader represented by the given {@code View}.
	 */
	public static FXMLLoader createFXMLLoader(View view) {
		URL pathToFXML = Loader.class.getClassLoader().getResource(view.getPathToFXML());
		FXMLLoader loader = new FXMLLoader(pathToFXML);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return loader;
	}
	
	/**
	 * Returns the FXMLLoader used for loading the FXML file represented by the view specified.
	 */
	public static FXMLLoader getFXMLLoader(View view) {
		return viewToLoader.get(view);
	}

	/**
	 * Returns a list of all methods within the specified class with a given {@code annotation}. 
	 */
	private static List<Method> getMethodsAnnotatedWith(Class<?> type, Class<? extends Annotation> annotation) {
	    return Arrays.stream(type.getDeclaredMethods())
				.filter(m -> m.isAnnotationPresent(annotation))
				.collect(Collectors.toList());
	}
	
}