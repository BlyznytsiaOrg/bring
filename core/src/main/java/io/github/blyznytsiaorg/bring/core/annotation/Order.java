package io.github.blyznytsiaorg.bring.core.annotation;

import io.github.blyznytsiaorg.bring.core.context.type.OrderComparator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code @Order} defines the sort order for an annotated component.
 *
 * <p>The {@link #value} is optional and represents an order value for objects that should be orderable,
 * for example in a Collection. Lower values have higher priority. The default value is
 * {@code Integer.MAX_VALUE}, indicating the lowest priority (losing to any
 * other specified order value).
 *  <p>Consult the javadoc for {@link OrderComparator
 * OrderComparator} for details on the sort semantics for non-ordered objects.
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
public @interface Order {
    /**
     * The order value.
     * <p>Default is {@link Integer#MAX_VALUE}.
     */
    int value() default Integer.MAX_VALUE;
}
