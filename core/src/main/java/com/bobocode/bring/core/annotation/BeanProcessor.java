package com.bobocode.bring.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark extensions for custom scanners, resolvers, or processors in the client's application.
 * These extensions can be used to perform specific tasks related to bean scanning, resolving, or processing.
 * In addition, we have some limitation on constructor some of the classes should have only default or one field.
 * for instance RestControllerBeanPostProcessor.
 * This limitation only for customization scanners, resolvers, or processors
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BeanProcessor {
}
