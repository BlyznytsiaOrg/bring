package com.bobocode.bring.core.domain;

/**
 * Enumeration representing the scope of beans in an application.
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
public enum BeanScope {

  /**
   * Indicates that a single instance of the bean is created and shared throughout the application context.
   */
  SINGLETON,

  /**
   * Indicates that a new instance of the bean is created whenever it is requested.
   */
  PROTOTYPE
  
}
