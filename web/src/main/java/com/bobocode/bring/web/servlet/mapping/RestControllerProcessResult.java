package com.bobocode.bring.web.servlet.mapping;

import java.lang.reflect.Method;

/**
 * The {@code RestControllerProcessResult} record represents the result of processing
 * a method in a web controller.
 *<p>
 * This record encapsulates information such as the method itself and the result of its execution.
 *</p>
 * @author Blyzhnytsia Team
 * @since 1.0
 *
 * @param method The method that was processed.
 * @param result The result of the method execution.
 */
public record RestControllerProcessResult(Method method, Object result) {
}
