package com.bobocode.bring.core.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation indicating that a field, constructor, or method should be autowired by the Bring IoC container.
 * This annotation can be applied to fields, constructors, and methods in a Bring bean class to let Bring automatically
 * inject the dependencies at runtime.
 *
 * <p>When applied to a field, the Bring IoC container will automatically inject a compatible bean, resolved by type,
 * into the annotated field. When applied to a constructor, the container will use constructor injection to provide
 * the required dependencies. If applied to a method, the container will invoke the method after initializing the bean,
 * injecting the necessary dependencies into the method parameters.
 *
 * <p>This annotation is part of the Bring Framework's dependency injection mechanism, enabling the creation of loosely
 * coupled and easily testable components.
 *
 * <p><strong>Usage Example:</strong>
 * <pre>
 * {@code
 *  @Component
 *  public class MyComponent {
 *
 *      @Autowired
 *      private MyDependency myDependency;
 *
 *      // Constructors and methods can also be annotated
 *      @Autowired
 *      public MyComponent(MyDependency myDependency) {
 *          this.myDependency = myDependency;
 *      }
 *  }
 * }
 * </pre>
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
@Target({ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
}
