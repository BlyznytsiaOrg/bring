package com.bobocode.bring.core.exception;

import java.util.*;

/**
 * Exception thrown to indicate the presence of cyclic dependencies between beans
 * within a Bring Dependency Injection framework. This exception provides information
 * about the classes involved in the cyclic dependency.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public class CyclicBeanException extends RuntimeException {

    private static final String MESSAGE = "Looks like you have cyclic dependency between those beans %s";

    /**
     * Constructs a new CyclicBeanException with a message indicating the classes involved in the cyclic dependency.
     *
     * @param classes Set of class names involved in the cyclic dependency
     */
    public CyclicBeanException(Set<String> classes) {
        super(String.format(MESSAGE, printCyclic(classes)));
    }

    /**
     * Generates a formatted string representation of the cyclic dependency involving classes.
     *
     * @param classes Set of class names involved in the cyclic dependency
     * @return A string representation of the cyclic dependency path among classes
     */
    private static String printCyclic(Set<String> classes) {
        if (classes.isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        Iterator<String> iterator = classes.iterator();
        String first = iterator.next();

        sb.append("[").append(first);

        while (iterator.hasNext()) {
            String current = iterator.next();
            sb.append(" -> ").append(current);
        }

        sb.append(" -> ").append(first).append("]");
        return sb.toString();
    }
}
