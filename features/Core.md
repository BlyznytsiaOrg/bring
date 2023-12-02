# Bring Core

## General DI Flow Diagram

![Bring DI diagram](https://github.com/YevgenDemoTestOrganization/bring/assets/73576438/0e8d074a-3d49-4099-bf8e-68b029056cce)

1. The **BringApplication** provides a simple entry point to initialize and run a **BringApplicationContext**.
   It allows the user to create and configure a **BringApplicationContext** either by specifying a base package for component scanning or by providing a class that contains configuration information.
2. The **ClassPathScannerFactory** is responsible for creating a set of class path scanners, each designed to scan the classpath for specific types of annotated classes relevant to the Bring application. It initializes scanners for components, services, and configuration classes etc.
3. The **BeanPostProcessorDefinitionFactory** is responsible for creating and managing a list of **BeanFactoryPostProcessor** instances. It initializes the list with default post-processors, such as the **ConfigurationClassPostProcessor** etc. In this step we create Bean definitions for classes annotated with annotations from **ClassPathScanner**.
4. The next step will be to go through all bean definitions and register/create beans with dependencies.
5. The last one **BeanPostProcessorFactory** is responsible for creating and managing a list **BeanPostProcessor** instances. 
It initializes the list with default post-processors such as the **ScheduleBeanPostProcessor** and add addition logic to them.

## Documentations

- There are two types of documentation: Markdown (see below) and [JavaDoc](https://yevgendemotestorganization.github.io/bring-core-javadoc/)


## Features:

 - Dependency Injection
   - [Constructor](core/Constructor.md)
   - [Setter](core/Setter.md)
   - [Field](core/Field.md)
   - [Collections](core/Collections.md)
   - [Prototype Bean](core/Prototype.md)
   - [Singleton Bean](core/Singleton.md)


 - Configuration support
   - [Annotation configuration](core/annotation/Component.md) `@Component`, `@Service`.
   - [Annotation](core/annotation/Autowired.md) `@Autowired`
   - [Java Configuration](core/annotation/Configuration.md) `@Bean`, `@Configuration`.


 - Annotations
   - [@Autowired](core/annotation/Autowired.md)
   - [@Bean](core/annotation/Bean.md)
   - [@BeanProcessor](core/annotation/BeanProcessor.md)
   - [@Component](core/annotation/Component.md)
   - [@Configuration](core/annotation/Configuration.md)
   - [@Order](core/annotation/Order.md)
   - [@PostConstruct](core/annotation/PostConstruct.md)
   - [@Primary](core/annotation/Primary.md)
   - [@Qualifier](core/annotation/Qualifier.md)
   - [@ScheduledTask](core/annotation/ScheduledTask.md)
   - [@Scope](core/annotation/Scope.md)
   - [@Service](core/annotation/Service.md)
   - [@Value](core/annotation/Value.md)


 - Exceptions
   - [BeanAnnotationMissingException](core/exception/BeanAnnotationMissingException.md)
   - [BeanPostProcessorConstructionLimitationException](core/exception/BringGeneralException.md)
   - [BringGeneralException](core/exception/BringGeneralException.md)
   - [CyclicBeanException](core/exception/CircularDependencies.md)
   - [NoConstructorWithAutowiredAnnotationBeanException](core/exception/NoConstructorWithAutowiredAnnotationBeanException.md)
   - [NoSuchBeanException](core/exception/NoSuchBeanException.md)
   - [NoUniqueBeanException](core/exception/NoUniqueBeanException.md)
   - [PostConstructException](core/exception/PostConstructException.md)
   - [PreDestroyException](core/exception/PreDestroyException.md)
   - [PropertyValueNotFoundException](core/exception/PropertyValueNotFoundException.md)



- additional items:
  - [Scheduling](core/Scheduling.md)
  - [Properties File Support](core/PropertiesFileSupport.md)
  - [Logging](core/Logging.md)
  - [Banner](core/Banner.md)