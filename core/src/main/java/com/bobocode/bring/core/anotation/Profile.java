package com.bobocode.bring.core.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Indicates that the annotated element is associated with specific profiles defined in property files,
 * such as 'application.properties' or 'application-test.properties'.
 * Profiles are used to configure different sets of properties for various environments or conditions.
 * <p>
 * This annotation typically correlates with profile-specific configurations in property files
 * (e.g., 'application.properties', 'application-test.properties') where specific settings are defined
 * based on the active profiles.
 * <p>
 * Example usage in a configuration class:
 * <pre>{@code
 * {@literal @}Profile("development")
 * public class DevelopmentConfiguration {
 *     // Configuration specific to the "development" profile
 * }
 * }</pre>
 *
 *
 * @author Blyzhnytsia Team
 *  @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Profile {

    /**
     * The name of the profile associated with configuration property files.
     *
     * @return the name of the profile
     */
    String value() default "";
}
