package main.app;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import main.util.PostInitialize;
import main.util.View;

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
				System.out.printf("View successfully loaded: %s%n", view.name());
				viewToParent.put(view, parent);
			} catch (IOException e) {
				System.err.printf("FXML load failed: %s", view.name());
				e.printStackTrace();
			}
		}
		
		// Handle @PostInitialize annotations
		for (View view : View.values()) {
			Object controller = getController(view);
			List<Method> annotatedMethods = getMethodsAnnotatedWith(controller.getClass(), PostInitialize.class);
			
			// Invoke every method with specified annotation.
			for (Method method : annotatedMethods) {
				try {
					method.invoke(controller);
					System.out.printf("@PostInitialize invoked: View: %s, Method: %s%n", view.name(), method.getName());
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("FXML Loader finished.");
	}
	
	private static List<Method> getMethodsAnnotatedWith(Class<?> type, Class<? extends Annotation> annotation) {
	    return Arrays.stream(type.getDeclaredMethods())
				.filter(m -> m.isAnnotationPresent(annotation))
				.collect(Collectors.toList());
	}
	
	public static FXMLLoader getFXMLLoader(View view) {
		return viewToLoader.get(view);
	}

	public static Parent getParent(View view) {
		return viewToParent.get(view);
	}

	public static <T> T getController(View view) {
		return viewToLoader.get(view).getController();
	}

}