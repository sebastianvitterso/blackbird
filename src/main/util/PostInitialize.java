package main.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be used in Controller classes.
 * FXML Loader will invoke all methods with this annotation when all controllers have been loaded successfully.
 * Controller must be listed as an enumeration in {@code View} in order to be recognized by the loader.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PostInitialize {

}
