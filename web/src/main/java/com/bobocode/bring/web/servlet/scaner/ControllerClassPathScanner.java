package com.bobocode.bring.web.servlet.scaner;

import com.bobocode.bring.core.anotation.BeanProcessor;
import com.bobocode.bring.core.anotation.Controller;
import com.bobocode.bring.core.context.scaner.ClassPathScanner;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;


/**
 * Implementation of ClassPathScanner that scans for classes annotated with @Controller using Reflections.
 * This scanner uses the Reflections library to scan the classpath and retrieve classes annotated with @Component.
 *
 * @see ClassPathScanner
 * @see Controller
 * @see Reflections
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
@BeanProcessor
@AllArgsConstructor
@Slf4j
public class ControllerClassPathScanner implements ClassPathScanner {

    /** The Reflections instance used for scanning classes. */
    private final Reflections reflections;

    /**
     * Scans the classpath to retrieve classes annotated with @Controller.
     *
     * @return a set of classes annotated with @Controller
     */
    @Override
    public Set<Class<?>> scan() {
        return reflections.getTypesAnnotatedWith(getAnnotation());
    }

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return Controller.class;
    }

}
