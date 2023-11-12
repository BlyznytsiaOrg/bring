package com.bobocode.bring.core.context.scaner;

import com.bobocode.bring.core.context.scaner.impl.ComponentClassPathScanner;
import com.bobocode.bring.core.context.scaner.impl.ConfigurationClassPathScanner;
import com.bobocode.bring.core.context.scaner.impl.ServiceClassPathScanner;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class ClassPathScannerFactory {

    private final List<ClassPathScanner> classPathScanners;

    public ClassPathScannerFactory(Reflections reflections) {
        this.classPathScanners = Arrays.asList(
                new ComponentClassPathScanner(reflections),
                new ServiceClassPathScanner(reflections),
                new ConfigurationClassPathScanner(reflections)
        );
    }

    public void register(ClassPathScanner classPathScanner) {
        classPathScanners.add(classPathScanner);
    }

    public Set<Class<?>> getBeansToCreate() {
        return classPathScanners.stream()
                .flatMap(classPathScanner -> classPathScanner.scan().stream())
                .collect(Collectors.toSet());
    }
}
