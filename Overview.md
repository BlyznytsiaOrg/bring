# Bring Framework Overview

Bring simplifies the development of Java enterprise applications by offering comprehensive support for leveraging the Java language within an enterprise setting. 
It extends its capabilities by accommodating Groovy and Kotlin as alternative languages on the JVM, allowing flexibility in architecture design tailored to diverse application requirements. 
Bring Framework 1.0, Java 17 is the required version for Bring development.

Bring's flexibility caters to diverse application scenarios found in large enterprises. 
These applications often endure extended lifecycles, necessitating compatibility with specific JDK and application server versions, sometimes beyond the immediate control of developers. 
Others operate as self-contained entities within a single jar, utilizing embedded servers, and might even function within cloud environments. 
Bring's adaptability addresses these distinct deployment needs, ensuring seamless integration regardless of the deployment environment's complexities.


# Bring build using modules

The Bring Framework consists of features organized into two modules. 
These modules are grouped into Core Container, Web as shown in the following diagram.

<img width="922" alt="image" src="https://github.com/YevgenDemoTestOrganization/bring/assets/73576438/f659a4e7-5b66-44f9-b832-645d67a3d3f8">


The **Core Container** is the backbone of the Bring framework, offering essential functionalities like the Inversion of Control (IoC) container and the ApplicationContext. 
It encompasses the following module:

- **Bring Core** module is at the core of the Bring framework, delivering essential functionalities like IoC (Inversion of Control) and DI (Dependency Injection). 
At its center lies the IoC container, which serves as the framework's nucleus, responsible for the creation and administration of beans instances. 
This container utilizes dependency injection to effectively interconnect these beans. BeanFactory, foundational to the IoC container, tasked with overseeing a bean's lifecycle. 
Serving as the fundamental interface to access the IoC container, the Bean Factory provides essential methods for obtaining beans.
- **Bring Context**: provides the ApplicationContext, which is an advanced version of the BeanFactory and provides additional features, such as resource loading.

The **Web** segment encompasses module designed to assist in constructing web applications, featuring:

- **Bring Web** module presents a comprehensive Model-View-Controller (MVC) framework tailored for web application development. 
It offers an array of functionalities, spanning from managing HTTP requests and responses to handling static content. 

This enabling developers to combine view technology and api in one place.

**Miscellaneous:**

The Miscellaneous section comprises additional features offering diverse functionalities, such as:

- **Scheduling** refers to the automated execution of tasks at predefined intervals, allowing them to run in a loop based on specific times or intervals without requiring manual intervention.


# Design Philosophy

This framework is crafted with educational principles at its core, aiming to empower individuals in mastering reflection, architectural design, and comprehending Dependency Inversion. 
It serves as a guide to understanding the construction of web servlet applications. Its primary goal is to facilitate team collaboration and cohesive development efforts.

# Feedback and Contributions

If you suspect an issue within the Bring Framework or wish to propose a new feature, kindly utilize [GitHub Issues](https://github.com/YevgenDemoTestOrganization/bring/issues/new) for reporting problems or submitting feature suggestions
If you have a solution in mind or a suggested fix, you can submit a pull request on [Github](https://github.com/YevgenDemoTestOrganization/bring). In addition, please read and configure you idea to follow our [Setup Code Style Guidelines](https://github.com/YevgenDemoTestOrganization/bring/wiki/Setup-Code-Style-Guidelines)

# Getting Started

If you're new to Bring, consider initiating your experience with a [Bring playground application repo](https://github.com/YevgenDemoTestOrganization/bring-playground) with a variety of examples of how to use it.. 
Bring Boot offers a swift and opinionated method to develop a Bring-based application ready for production. 
Leveraging the Bring Framework, it prioritizes conventions over extensive configurations, aiming to accelerate your setup process and swiftly get you up and running.