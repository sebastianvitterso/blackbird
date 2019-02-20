package main.util;

/**
 * A 'Clearable' view allow reuse of the graphical user interface without reloading.
 */
public interface Clearable {
	/**
	 * Clears graphical interface, resetting it to its' initial state.
	 */
	default void clear() {
		System.out.printf("The method 'clear()' is not yet implemented for %s.%n", getClass().getSimpleName());
	}
}
