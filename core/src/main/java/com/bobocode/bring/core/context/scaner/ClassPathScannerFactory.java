package com.bobocode.bring.core.context.scaner;

import com.bobocode.bring.core.anotation.resolver.AnnotationResolver;
import com.bobocode.bring.core.anotation.resolver.impl.*;
import com.bobocode.bring.core.context.scaner.impl.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * The {@code ClassPathScannerFactory} class is responsible for creating a set of class path scanners,
 * each designed to scan the classpath for specific types of annotated classes relevant to the Bring application.
 * It initializes scanners for components, services, and configuration classes.
 *
 * <p>The class is designed to work with the {@link Reflections} library to perform efficient classpath scanning.
 * It encapsulates the creation of specific scanners for different types of annotated classes.
 *
 * <p>The scanners created by this factory include:
 * <ul>
 *     <li>{@link ComponentClassPathScanner}: Scans for classes annotated with {@code @Component}.</li>
 *     <li>{@link ServiceClassPathScanner}: Scans for classes annotated with {@code @Service}.</li>
 *     <li>{@link ConfigurationClassPathScanner}: Scans for classes annotated with {@code @Configuration}.</li>
 * </ul>
 *
 * <p>Usage example:
 * <pre>{@code
 * Reflections reflections = new Reflections("com.example.myapp");
 * ClassPathScannerFactory scannerFactory = new ClassPathScannerFactory(reflections);
 * List<ClassPathScanner> scanners = scannerFactory.getClassPathScanners();
 * }</pre>
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 * @see ComponentClassPathScanner
 * @see ServiceClassPathScanner
 * @see ConfigurationClassPathScanner
 * @see Reflections
 */
@Slf4j
public class ClassPathScannerFactory {

    /**
     * The list of class path scanners created by this factory.
     */
    private final List<ClassPathScanner> classPathScanners;

    @Getter
    private final List<Class<? extends Annotation>> createdBeanAnnotations;

    private final List<AnnotationResolver> annotationResolvers;

    /**
     * Constructs a new ClassPathScannerFactory with the specified Reflections instance.
     *
     * @param reflections the Reflections instance used for classpath scanning
     * @see Reflections
     * @see ComponentClassPathScanner
     * @see ServiceClassPathScanner
     * @see ConfigurationClassPathScanner
     */
    public ClassPathScannerFactory(Reflections reflections) {
        this.classPathScanners = Arrays.asList(
                new ComponentClassPathScanner(reflections),
                new ServiceClassPathScanner(reflections),
                new ConfigurationClassPathScanner(reflections),
                new ProfileClassPathScanner(reflections),
                new ControllerClassPathScanner(reflections),
                new RestControllerClassPathScanner(reflections)
        );

        this.createdBeanAnnotations = classPathScanners.stream()
                        .map(ClassPathScanner::getAnnotation)
                        .collect(toList());

        this.annotationResolvers = List.of(
                new ComponentBeanNameAnnotationResolver(),
                new ServiceBeanNameAnnotationResolver(),
                new InterfaceBeanNameAnnotationResolver(),
                new ConfigurationBeanNameAnnotationResolver(),
                new ControllerBeanNameAnnotationResolver(),
                new RestControllerBeanNameAnnotationResolver()
        );

        log.info("Register ClassPathScannerFactory {}", Arrays.toString(classPathScanners.toArray()));
    }

    /**
     * Retrieves a set of classes representing beans to be created by aggregating scanned classes
     * from multiple ClassPathScanner instances.
     *
     * @return a set of classes to be instantiated as beans
     */
    public Set<Class<?>> getBeansToCreate() {
        return classPathScanners.stream()
                .flatMap(classPathScanner -> classPathScanner.scan().stream())
                .collect(Collectors.toSet());
    }

    /**
     * Resolves the name of the bean based on its class using registered annotation resolvers.
     *
     * @param clazz the class for which the bean name needs to be resolved
     * @return the resolved name of the bean
     * @throws IllegalStateException if no suitable resolver is found for the provided class
     */
    public String resolveBeanName(Class<?> clazz) {
        return annotationResolvers.stream()
                .filter(resolver -> resolver.isSupported(clazz))
                .findFirst()
                .map(annotationResolver -> annotationResolver.resolve(clazz))
                .orElseThrow(
                        () -> new IllegalStateException("No suitable resolver found for " + clazz.getName()));
    }
}
