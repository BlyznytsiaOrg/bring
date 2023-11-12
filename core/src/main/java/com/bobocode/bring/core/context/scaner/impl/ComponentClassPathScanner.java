package com.bobocode.bring.core.context.scaner.impl;

import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.core.context.scaner.ClassPathScanner;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.util.Set;

@AllArgsConstructor
@Slf4j
public class ComponentClassPathScanner implements ClassPathScanner {

    private final Reflections reflections;
    @Override
    public Set<Class<?>> scan() {
        return reflections.getTypesAnnotatedWith(Component.class);
    }
}
