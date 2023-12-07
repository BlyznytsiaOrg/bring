package io.github.blyznytsiaorg.bring.core.context.scaner;

import io.github.blyznytsiaorg.bring.core.annotation.BeanProcessor;
import io.github.blyznytsiaorg.bring.core.annotation.resolver.AnnotationResolver;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.github.blyznytsiaorg.bring.core.utils.ReflectionUtils.*;

/**
 * Factory responsible for managing and handling classpath scanning operations for bean processing.
 * It collaborates with ClassPathScanner and AnnotationResolver implementations for scanning and resolving annotations.
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
@Slf4j
public class ClassPathScannerFactory {

    private final List<ClassPathScanner> classPathScanners;
    private final List<AnnotationResolver> annotationResolvers;

    // List of annotations indicating beans created by the scanning process.
    @Getter
    private final List<Class<? extends Annotation>> createdBeanAnnotations;

    /**
     * Constructs the ClassPathScannerFactory with reflections for initializing scanners and resolvers.
     *
     * @param reflections The Reflections instance used for scanning and resolving annotations.
     */
    public ClassPathScannerFactory(Reflections reflections) {
        // Retrieve and initialize ClassPathScanner implementations.
        this.classPathScanners = reflections.getSubTypesOf(ClassPathScanner.class)
                .stream()
                .filter(clazz -> clazz.isAnnotationPresent(BeanProcessor.class))
                .sorted(ORDER_COMPARATOR)
                .map(clazz -> clazz.cast(getConstructorWithOneParameter(clazz, Reflections.class, reflections)))
                .collect(Collectors.toList());

        // Retrieve and initialize AnnotationResolver implementations.
        this.annotationResolvers = reflections.getSubTypesOf(AnnotationResolver.class)
                .stream()
                .filter(clazz -> clazz.isAnnotationPresent(BeanProcessor.class))
                .map(clazz -> clazz.cast(getConstructorWithOutParameters(clazz)))
                .collect(Collectors.toList());

        // Collect annotations used to identify created beans during scanning.
        this.createdBeanAnnotations = classPathScanners.stream()
                        .map(ClassPathScanner::getAnnotation)
                        .collect(Collectors.toList());

        log.info("Register ClassPathScannerFactory {}", Arrays.toString(classPathScanners.toArray()));
    }

    /**
     * Retrieves a set of classes identified as beans to be created by the registered scanners.
     *
     * @return A set of classes to be created based on scanning.
     */
    public Set<Class<?>> getBeansToCreate() {
        return classPathScanners.stream()
                .flatMap(classPathScanner -> classPathScanner.scan().stream())
                .collect(Collectors.toSet());
    }

    /**
     * Resolves the bean name for a given class using registered AnnotationResolvers.
     *
     * @param clazz The class for which the bean name is to be resolved.
     * @return The resolved name of the bean.
     */
    public String resolveBeanName(Class<?> clazz) {
        log.info("Resolving bean name for class [{}]", clazz.getName());
        return annotationResolvers.stream()
                .filter(resolver -> resolver.isSupported(clazz))
                .findFirst()
                .map(annotationResolver -> annotationResolver.resolve(clazz))
                .orElse(null);
    }

}
