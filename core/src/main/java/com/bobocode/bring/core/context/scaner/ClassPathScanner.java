package com.bobocode.bring.core.context.scaner;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * Defines an interface for scanning the classpath to discover classes.
 * Implementations of this interface are responsible for scanning the classpath
 * and returning a set of discovered classes.
 * Then, add annotation @BeanProcessor and Bring will automatically pick it up.
 *
 * <p>
 *     Example: implement custom scanner to scan classes from a custom package.
 *     <pre>
 *         @BeanProcessor
 *         @AllArgsConstructor
 *         @Slf4j
 *         public class CustomClassPathScanner implements ClassPathScanner {
 *               private final Reflections reflections;
 *
 *               @Override
 *               public Set<Class<?>> scan() {
 *                   return reflections.getTypesAnnotatedWith(getAnnotation());
 *               }
 *
 *              @Override
 *              public Class<? extends Annotation> getAnnotation() {
 *                     return YourCustomAnnotation.class;
 *              }
 *         }
 * </p>
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
public interface ClassPathScanner {

    /**
     * Scans the classpath to discover classes.
     *
     * @return a set of classes discovered during the scan
     */
    Set<Class<?> > scan();


    /**
     * Retrieves the annotation type that the scanner is designed to search for.
     *
     * @return The {@code Class} object representing the annotation type.
     */
    Class<? extends Annotation> getAnnotation();
}
