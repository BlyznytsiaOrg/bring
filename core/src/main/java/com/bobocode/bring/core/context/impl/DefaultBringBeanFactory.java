package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.context.BringBeanFactory;
import com.bobocode.bring.core.context.type.TypeResolverFactory;
import com.bobocode.bring.core.domain.BeanDefinition;
import com.bobocode.bring.core.exception.BeansException;
import com.bobocode.bring.core.exception.NoSuchBeanException;
import com.bobocode.bring.core.exception.NoUniqueBeanException;
import java.util.Map.Entry;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class DefaultBringBeanFactory implements BringBeanFactory {

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private final Map<Class<?>, List<String>> typeToBeanNames = new ConcurrentHashMap<>();
    
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    private final Map<String, Supplier<Object>> prototypeSuppliers = new ConcurrentHashMap<>();

    private final Map<String, List<Object>> interfaceNameToImplementations = new ConcurrentHashMap<>();

    @Setter
    @Getter
    private Map<String, String> properties = new ConcurrentHashMap<>();

    @Setter
    @Getter
    private String profileName;

    @Setter
    @Getter
    private TypeResolverFactory typeResolverFactory;

    @Override
    public <T> T getBean(Class<T> type) throws BeansException {
        Map<String, T> beans = getBeans(type);

        if (beans.size() > 1) {
            return getPrimary(beans, type);
        }

        return beans.values()
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchBeanException(type));
    }

    @Override
    public <T> T getBean(Class<T> type, String name) throws BeansException {
        Object obj = Optional.ofNullable(singletonObjects.get(name))
                .orElse(Optional.ofNullable(prototypeSuppliers.get(name))
                  .map(Supplier::get)
                  .orElse(null));
        
        if (Objects.isNull(obj)) {
            throw new NoSuchBeanException(type);
        }
        
        return type.cast(obj);
    }

    @Override
    public <T> Map<String, T> getBeans(Class<T> type) throws BeansException {
        List<String> beanNames = typeToBeanNames.entrySet()
                .stream()
                .filter(entry -> type.isAssignableFrom(entry.getKey()))
                .map(Entry::getValue)
                .flatMap(Collection::stream)
                .toList();
        
        return beanNames.stream()
                .collect(Collectors.toMap(Function.identity(), name -> getBean(type, name)));
    }

    @Override
    public <T> Map<String, T> getAllBeans() {
        return (Map<String, T>) singletonObjects;
    }

    protected Map<String, BeanDefinition> getBeanDefinitions() {
        return beanDefinitionMap;
    }
    
    protected Map<String, Object> getSingletonObjects() {
        return singletonObjects;
    }

    protected Map<String, Supplier<Object>> getPrototypeSuppliers() {
        return prototypeSuppliers;
    }

    protected Map<String, List<Object>> getInterfaceNameToImplementations() {
        return interfaceNameToImplementations;
    }
    
    protected Map<Class<?>, List<String>> getTypeToBeanNames() {
        return typeToBeanNames;
    }

    protected List<Object> getInterfaceNameToImplementations(String beanName) {
        return interfaceNameToImplementations.getOrDefault(beanName, new ArrayList<>());
    }

    void addSingletonBean(String beanName, Object bean) {
        singletonObjects.put(beanName, bean);
    }

    void addPrototypeBean(String beanName, Supplier<Object> supplier) {
        prototypeSuppliers.put(beanName, supplier);
    }

    void addInterfaceNameToImplementations(String interfaceName, Object implementation) {
        List<Object> implementations = interfaceNameToImplementations.getOrDefault(interfaceName, new ArrayList<>());
        implementations.add(implementation);
        interfaceNameToImplementations.put(interfaceName, implementations);
    }

    public void addBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(beanName, beanDefinition);

        List<String> beanNames = typeToBeanNames.getOrDefault(beanDefinition.getBeanClass(), new ArrayList<>());
        beanNames.add(beanName);
        typeToBeanNames.put(beanDefinition.getBeanClass(), beanNames);
    }
    
    public BeanDefinition getBeanDefinitionByName(String beanName) {
        return this.beanDefinitionMap.get(beanName);
    }
    
    public List<String> getAllBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet()
                .stream()
                .toList();
    }

    private <T> T getPrimary (Map<String, T> beans, Class<T> type) {
        List <T> foundBeans = beans.entrySet().stream().filter(entry -> getBeanDefinitionByName(entry.getKey()).isPrimary()).map(
            Entry::getValue).toList();
        if (foundBeans.size() != 1) {
            throw new NoUniqueBeanException(type);
        } else {
            return foundBeans.get(0);
        }
    }

}
