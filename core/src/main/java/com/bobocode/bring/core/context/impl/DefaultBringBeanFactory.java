package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.context.BringBeanFactory;
import com.bobocode.bring.core.context.type.TypeResolverFactory;
import com.bobocode.bring.core.domain.BeanDefinition;
import com.bobocode.bring.core.exception.NoSuchBeanException;
import com.bobocode.bring.core.exception.NoUniqueBeanException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Default implementation of the BringBeanFactory interface providing basic bean management functionalities.
 * Manages bean definitions, singleton, and prototype beans.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Slf4j
@Getter
public class DefaultBringBeanFactory implements BringBeanFactory {

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private final Map<Class<?>, List<String>> typeToBeanNames = new ConcurrentHashMap<>();

    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    private final Map<String, Supplier<Object>> prototypeSuppliers = new ConcurrentHashMap<>();

    @Setter
    private Map<String, String> properties = new ConcurrentHashMap<>();

    @Setter
    private String profileName;

    @Setter
    private TypeResolverFactory typeResolverFactory;

    /**
     * Retrieves a bean of the given type from the bean factory.
     * If multiple beans of the same type exist, returns the primary one.
     *
     * @param type The class type of the bean to retrieve.
     * @param <T>  The type of the bean.
     * @return The bean instance.
     * @throws NoSuchBeanException If no bean of the given type is found.
     * @throws NoUniqueBeanException If more than one primary bean of the given type is found.
     */
    @Override
    public <T> T getBean(Class<T> type) {
        Map<String, T> beans = getBeans(type);

        if (beans.size() > 1) {
            return getPrimary(beans, type);
        }

        return beans.values().stream()
                .findFirst()
                .orElseThrow(() -> {
                    if (type.isInterface()) {
                        return new NoSuchBeanException(String.format("No such bean that implements this %s ", type));
                    }
                    return new NoSuchBeanException(type);
                });
    }

    /**
     * Retrieves a bean of the given type and name from the bean factory.
     *
     * @param type The class type of the bean to retrieve.
     * @param name The name of the bean.
     * @param <T>  The type of the bean.
     * @return The bean instance.
     * @throws NoSuchBeanException If no bean of the given type and name is found.
     */
    @Override
    public <T> T getBean(Class<T> type, String name) {
        Object bean = Optional.ofNullable(getBeanByName(name))
                .orElseThrow(() -> {
                    if (type.isInterface()) {
                        return new NoSuchBeanException(String.format("No such bean that implements this %s ", type));
                    }
                    return new NoSuchBeanException(type);
                });

        return type.cast(bean);
    }

    /**
     * Retrieves all beans of the specified type from the factory.
     *
     * @param type The type of the beans to retrieve.
     * @param <T>  The type of the beans.
     * @return A map of bean names to their respective instances.
     */
    @Override
    public <T> Map<String, T> getBeans(Class<T> type) {
        List<String> beanNames = typeToBeanNames.entrySet().stream()
                .filter(entry -> type.isAssignableFrom(entry.getKey()))
                .map(Entry::getValue)
                .flatMap(Collection::stream)
                .toList();

        return beanNames.stream()
                .collect(Collectors.toMap(Function.identity(), name -> getBean(type, name)));
    }

    /**
     * Retrieves a bean instance by its name.
     *
     * @param beanName The name of the bean.
     * @return The retrieved bean instance.
     */
    public Object getBeanByName(String beanName) {
        return Optional.ofNullable(getPrototypeSuppliers().get(beanName))
                .map(Supplier::get)
                .orElse(getSingletonObjects().get(beanName));
    }

    /**
     * Retrieves all singleton beans registered in the factory.
     *
     * @param <T> The type of the beans.
     * @return A map of singleton bean names to their respective instances.
     */
    @Override
    public <T> Map<String, T> getAllBeans() {
        return (Map<String, T>) singletonObjects;
    }

    /**
     * Adds a singleton bean to the factory with the given name and instance.
     *
     * @param beanName The name of the singleton bean.
     * @param bean     The instance of the singleton bean to be added.
     */
    void addSingletonBean(String beanName, Object bean) {
        singletonObjects.put(beanName, bean);
    }

    /**
     * Adds a prototype bean to the factory with the given name and supplier.
     *
     * @param beanName The name of the prototype bean.
     * @param supplier The supplier supplying instances of the prototype bean.
     */
    void addPrototypeBean(String beanName, Supplier<Object> supplier) {
        prototypeSuppliers.put(beanName, supplier);
    }

    /**
     * Adds a bean definition to the factory with the given name and definition.
     * Updates the type-to-bean-names mapping accordingly.
     *
     * @param beanName      The name of the bean.
     * @param beanDefinition The definition of the bean to be added.
     */
    public void addBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        log.debug("Registering BeanDefinition of [{}]", beanName);
        this.beanDefinitionMap.put(beanName, beanDefinition);

        List<String> beanNames = typeToBeanNames.getOrDefault(beanDefinition.getBeanClass(), new ArrayList<>());
        beanNames.add(beanName);
        typeToBeanNames.put(beanDefinition.getBeanClass(), beanNames);
    }

    /**
     * Retrieves the bean definition associated with the given bean name.
     *
     * @param beanName The name of the bean for which the definition is requested.
     * @return The BeanDefinition object associated with the provided bean name.
     */
    public BeanDefinition getBeanDefinitionByName(String beanName) {
        return this.beanDefinitionMap.get(beanName);
    }

    /**
     * Retrieves a list containing names of all registered bean definitions in the factory.
     *
     * @return A list containing the names of all registered bean definitions.
     */
    public List<String> getAllBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().stream().toList();
    }

    /**
     * Checks if a bean with the specified name has been created.
     *
     * @param beanName The name of the bean to check.
     * @return true if the bean has been created, false otherwise.
     */
    public boolean isBeanCreated(String beanName) {
        return singletonObjects.containsKey(beanName) || prototypeSuppliers.containsKey(beanName);
    }

    private <T> T getPrimary (Map<String, T> beans, Class<T> type) {
        List <T> foundBeans = beans.entrySet()
                .stream()
                .filter(entry -> getBeanDefinitionByName(entry.getKey()).isPrimary())
                .map(Entry::getValue)
                .toList();
        if (foundBeans.size() != 1) {
            throw new NoUniqueBeanException(type);
        } else {
            return foundBeans.get(0);
        }
    }
}
