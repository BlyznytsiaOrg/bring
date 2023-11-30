package com.bobocode.bring.core.exception;

public class PreDestroyException extends RuntimeException {
  public static String PRE_DESTROY_EXCEPTION_MESSAGE ="@PreDestroy should be added to method without parameters";

  public PreDestroyException(Throwable cause) {
    super(PRE_DESTROY_EXCEPTION_MESSAGE, cause);
  }
}
