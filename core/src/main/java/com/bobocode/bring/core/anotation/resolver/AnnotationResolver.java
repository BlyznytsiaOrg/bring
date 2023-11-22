package com.bobocode.bring.core.anotation.resolver;

/**
 * Represents an interface for resolving annotations on classes.
 * Implementations of this interface define methods to determine support for handling
 * specific classes and to resolve information related to those classes' annotations.
 * <p>
 * Example usage:
 * An implementation of this interface might determine whether a certain annotation is supported
 * on a given class and provide functionality to extract information from that annotation.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public interface AnnotationResolver {

    /**
     * Checks if the resolver supports handling annotations on the specified class.
     *
     * @param clazz the class to check for support
     * @return true if the resolver supports the class; false otherwise
     */
    boolean isSupported(Class<?> clazz);

    /**
     * Resolves information related to annotations on the specified class.
     *
     * @param clazz the class for which to resolve annotation information
     * @return a string representing resolved information from the annotations
     */
    String resolve(Class<?> clazz);
}
