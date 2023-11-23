package com.bobocode.bring.core.context.scaner.impl;

import com.bobocode.bring.core.anotation.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;

@Slf4j
public class ConfigurationClassPathScanner extends AbstractClassPathScanner {

    public ConfigurationClassPathScanner(Reflections reflections) {
        super(reflections);
    }

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return Configuration.class;
    }

}
