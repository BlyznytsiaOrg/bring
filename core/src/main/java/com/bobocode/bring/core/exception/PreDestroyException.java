package com.bobocode.bring.core.exception;

/**
 * Exception thrown when method annotated with @PreDestroy violates rules of usage such as have parameters.
 * Extends RuntimeException.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public class PreDestroyException extends RuntimeException {

  public static final String PRE_DESTROY_EXCEPTION_MESSAGE ="@PreDestroy should be added to method without parameters";

  /**
   * Constructs a PreDestroyException with the specified message.
   *
   * @param cause The Throwable cause for this exception
   */
  public PreDestroyException(Throwable cause) {
    super(PRE_DESTROY_EXCEPTION_MESSAGE, cause);
  }

}
