package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.context.BringBeanFactory;
import com.bobocode.bring.core.exception.BeansException;
import com.bobocode.bring.core.exception.NoSuchBeanException;
import com.bobocode.bring.core.exception.NoUniqueBeanException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class DefaultBringBeanFactory implements BringBeanFactory {

    private final Map<String, Object> beansNameToObject = new HashMap<>();
    private final Map<String, List<Object>> interfaceNameToImplementations = new HashMap<>();

    @Override
    public <T> T getBean(Class<T> type) throws BeansException {
        Map<String, T> beans = getBeans(type);

        if (beans.size() > 1) {
            throw new NoUniqueBeanException(type);
        }

        return beans.values().stream().findFirst().orElseThrow(() -> new NoSuchBeanException(type));
    }

    @Override
    public <T> T getBean(Class<T> type, String name) throws BeansException {
        Object obj = beansNameToObject.get(name);
        return type.cast(obj);
    }

    @Override
    public <T> Map<String, T> getBeans(Class<T> type) throws BeansException {
        return getAllBeans().entrySet()
                .stream()
                .filter(entry -> type.isAssignableFrom(entry.getValue().getClass()))
                .collect(toMap(Map.Entry::getKey, t -> type.cast(t.getValue())));
    }

    @Override
    public <T> Map<String, T> getAllBeans() {
        return (Map<String, T>) beansNameToObject;
    }

    protected Map<String, Object> getBeansNameToObject() {
        return beansNameToObject;
    }

    protected Map<String, List<Object>> getInterfaceNameToImplementations() {
        return interfaceNameToImplementations;
    }

    protected List<Object> getInterfaceNameToImplementations(String beanName) {
        return interfaceNameToImplementations.getOrDefault(beanName, new ArrayList<>());
    }

    void addBean(String beanName, Object bean) {
        beansNameToObject.put(beanName, bean);
    }

    void addInterfaceNameToImplementations(String interfaceName, Object implementation) {
        List<Object> implementations = interfaceNameToImplementations.getOrDefault(interfaceName, new ArrayList<>());
        implementations.add(implementation);
        interfaceNameToImplementations.put(interfaceName, implementations);
    }
}
