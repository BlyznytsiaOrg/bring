package com.bobocode.bring.core.context;

import java.util.Map;

/**
 * An interface representing a simplified Bean Factory, allowing retrieval of beans by type or name.
 * Implementations of this interface provide methods to retrieve beans based on specified criteria.
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
public interface BringBeanFactory {

    /**
     * Retrieve a bean of the specified type.
     *
     * @param type The class/type of the bean to retrieve.
     * @param <T>  The type of the bean.
     * @return An instance of the requested bean or throw NoSuchBeanException
     */
    <T> T getBean(Class<T> type);

    /**
     * Retrieve a bean of the specified type and name.
     *
     * @param type The class/type of the bean to retrieve.
     * @param name The name of the bean to retrieve.
     * @param <T>  The type of the bean.
     * @return An instance of the requested bean or throw NoSuchBeanException
     */
    <T> T getBean(Class<T> type, String name);

    /**
     * Retrieve a map of beans of the specified type, where the key is the bean name and the value is the bean instance.
     *
     * @param type The class/type of the beans to retrieve.
     * @param <T>  The type of the beans.
     * @return A map containing bean names as keys and corresponding bean instances as values or throw NoSuchBeanException
     */
    <T> Map<String, T> getBeans(Class<T> type);

    /**
     * Retrieve a map containing all registered beans, where the key is the bean name and the value is the bean instance.
     *
     * @param <T> The type of the beans.
     * @return A map containing all registered bean names as keys and corresponding bean instances as values.
     */
    <T> Map<String, T> getAllBeans();
}

