package main.utils;

/**
 * A {@code Refreshable} view allow reuse of the graphical user interface without reloading.
 */
public interface Refreshable {
	/**
	 * Clears user interface, resetting it to its' initial state.
	 */
	default void clear() {
		System.out.printf("The method 'clear()' is not yet implemented for %s.%n", getClass().getSimpleName());
	}
	
	/**
	 * Updates every component in the user interface.
	 */
	default void update() {
		System.out.printf("The method 'update()' is not yet implemented for %s.%n", getClass().getSimpleName());
	}
	
	/**
	 * Clears and updates the user interface.
	 */
	default void refresh() {
		System.out.printf("Refreshing %s.%n", getClass().getSimpleName());
		clear();
		update();
	}
}
