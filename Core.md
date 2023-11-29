# Bring Core

## General DI flow diagram

![Bring DI diagram](https://github.com/YevgenDemoTestOrganization/bring/assets/73576438/0e8d074a-3d49-4099-bf8e-68b029056cce)

1. The BringApplication provides a simple entry point to initialize and run a BringApplicationContext.
   It allows the user to create and configure a BringApplicationContext either by specifying a base package for component scanning or by providing a class that contains configuration information.
2. The ClassPathScannerFactory is responsible for creating a set of class path scanners, each designed to scan the classpath for specific types of annotated classes relevant to the Bring application. It initializes scanners for components, services, and configuration classes etc.
3. The BeanPostProcessorDefinitionFactory is responsible for creating and managing a list of BeanFactoryPostProcessor instances. It initializes the list with default post-processors, such as the ConfigurationClassPostProcessor etc. In this step we create Bean definitions for classes annotated with annotations from ClassPathScanner
4. The next step will be go throw all bean definitions and register/create beans with dependencies
5. The last one BeanPostProcessorFactory is responsible for creating and managing a list BeanPostProcessor instances. 
It initializes the list with default post-processors such as the ScheduleBeanPostProcessor and add addition logic to them.

## Documentations

- We have two types of it via Markdown (see below) and [JavaDoc](https://yevgendemotestorganization.github.io/bring-core-javadoc/)


## Features:

If we need diagram classes we should use Wiki and add link to it.

 - Dependency Injection
   - [Constructor](core/Constructor.md)
   - [Setter](core/Setter.md)
   - [Field](core/Field.md)
   - Collections
   - @Primary
   - @Qualifier
   - @Order
   - [@Value](core/Value.md)
   - Prototype Beans into a Singleton


 - Configuration support
   - Annotation configuration @Component @Server
   - Annotation @Autowired (use for constructor (if you have one constructor no need to add it), field and setter)
   - Java Configuration @Bean @Configuration


- Dependency Injection exceptions
  - [Circular Dependencies](core/CircularDependencies.md)
  - No such bean exception
  - No unique bean exception
  - No constructor with Autowired annotation
  - etc


- addition items:
  - [Scheduling](core/Scheduling.md)
  - Properties file support 

    
    The `ValuePropertiesPostProcessor` class supports dynamic profile configuration using VM parameters. 
    Set the active profile with the `-Dbring.profiles.active=dev` parameter during application launch. 
    This enables the class to resolve profile-specific properties and apply them to the `DefaultBringBeanFactory`, allowing for adaptable configuration based on the specified profile. 
    Additionally, the active profiles influence the loading of different `application.properties` files. 
    For example, with an active profile of "dev," the class will load properties from `application-dev.properties`. This mechanism allows for customized property sets tailored to specific application environments.


  - [Logging](/features/core/Logging.md)
  - PostConstruct
  - PreDestroy
  - Logo