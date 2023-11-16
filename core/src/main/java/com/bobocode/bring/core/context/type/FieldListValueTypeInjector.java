package com.bobocode.bring.core.context.type;

import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import static com.bobocode.bring.core.utils.ReflectionUtils.extractImplClasses;

public class FieldListValueTypeInjector implements FieldValueTypeInjector {

    private final Reflections reflections;

    public FieldListValueTypeInjector(Reflections reflections) {
        this.reflections = reflections;
    }

    @Override
    public boolean hasAnnotatedWithValue(Field field) {
        return Collection.class.isAssignableFrom(field.getType());
    }

    @Override
    public Object setValueToField(Field field, Object bean) {
        ParameterizedType genericTypeOfField = (ParameterizedType) field.getGenericType();
        return extractImplClasses(genericTypeOfField, reflections);
    }
}
//package com.bobocode.bring.core.context.type;
//
//        import lombok.SneakyThrows;
//        import org.reflections.Reflections;
//
//        import java.lang.annotation.ElementType;
//        import java.lang.reflect.Executable;
//        import java.lang.reflect.ParameterizedType;
//        import java.lang.reflect.Type;
//        import java.util.Arrays;
//        import java.util.Collection;
//        import java.util.Collections;
//        import java.util.List;
//
//public class ListInjector {
//
//    private final Class<?> clazz;
//    private final Reflections reflections;
//
//    public ListInjector(Class<?> clazz, Reflections reflections) {
//        this.clazz = clazz;
//        this.reflections = reflections;
//    }
//
//    @SneakyThrows
//    public List<Class<?>> getInterfaceImplementations(ElementType elementType) {
//        switch (elementType) {
//            case FIELD -> {
//                for (var field : clazz.getDeclaredFields()) {
//                    Class<?> fieldType = field.getType();
//                    if (Collection.class.isAssignableFrom(fieldType)) {
//                        ParameterizedType genericTypeOfField = (ParameterizedType) field.getGenericType();
//                        return extractImplClasses(genericTypeOfField);
//                    }
//                }
//                return Collections.emptyList();
//            }
//
//            case CONSTRUCTOR -> {
//                Arrays.stream(clazz.getDeclaredConstructors())
//                        .map(Executable::getParameters)
//                        .flatMap(Arrays::stream)
//                        .forEach(parameter -> {
//                            Class<?> constructorParameterType = parameter.getType();
//                            if (Collection.class.isAssignableFrom(constructorParameterType)) {
//                                ParameterizedType genericTypeOfField = (ParameterizedType) parameter.getParameterizedType();
//                                extractImplClasses(genericTypeOfField);
//                            }
//                        });
//            }
//
//            case METHOD -> {
//                for (var field : clazz.getDeclaredFields()) {
//                    for (var method : clazz.getDeclaredMethods()) {
//                        if (method.getName().startsWith("set") && method.getName().length() == field.getName().length() + 3) {
//
//                        }
//                    }
//                }
//            }
//        }
//
//        @SneakyThrows
//        private List<Class<?>> extractImplClasses(ParameterizedType genericType) {
//            Type actualTypeArgument = genericType.getActualTypeArguments()[0];
//            if (actualTypeArgument instanceof Class actualTypeArgumentClass) {
//                String name = actualTypeArgumentClass.getName();
//                Class<?> interfaceClass = null;
//                interfaceClass = Class.forName(name);
//
//                return (List<Class<?>>) reflections.getSubTypesOf(interfaceClass)
//                        .stream()
//                        .toList();
////            classNames.forEach(System.out::println);
////                            ReflectionUtils.setField(field, object, classNames);
//            }
//            return Collections.emptyList();
//        }
//    }
