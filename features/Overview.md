[![Maven Central](https://img.shields.io/maven-central/v/io.github.blyznytsiaorg.bring.web/web.svg?label=Maven%20Central)](https://mvnrepository.com/artifact/io.github.blyznytsiaorg.bring.web/web/1.0.0)

# Bring Framework Overview

*The Bring Framework* simplifies the development of Java enterprise applications by providing support for leveraging the Java language within an enterprise setting. 
For *the Bring Framework* 1.0 development and Java 17 is the required version.


*The Bring Framework* accommodates self-contained entities within a single jar, using embedded servers and can operate in cloud environments.


# Bring build using modules

*The Bring Framework* consists of features organized into two modules. 
These modules are grouped into **Core Container** and **Web** as shown in the following diagram.

<img width="922" alt="image" src="https://github.com/BlyznytsiaOrg/bring/assets/73576438/eff5591f-ddb3-41f1-9c18-b476836abcf0">


The **Core Container** is the backbone of the Bring framework, offering essential functionalities like the Inversion of Control (IoC) container and the ApplicationContext. 
It encompasses the following module:

- **[Bring Core](Core.md)** module is at the core of *the Bring Framework*, delivering essential functionalities like IoC (Inversion of Control) and DI (Dependency Injection). 
At its center lies the IoC container, which serves as the framework's nucleus, responsible for the creation and administration of beans instances. 
This container utilizes dependency injection to effectively interconnect these beans. BeanFactory, foundational to the IoC container, tasked with overseeing a bean's lifecycle. 
Serving as the fundamental interface to access the IoC container, the Bean Factory provides essential methods for obtaining beans.
- **Bring Context**: provides the ApplicationContext, which is an advanced version of the BeanFactory and provides additional features, such as resource loading.

The **Web** segment encompasses module designed to assist in constructing web applications, featuring:

- **[Bring Web](Web.md)** module presents a framework tailored for web application development.
  It provides a range of functionalities, covering tasks such as managing HTTP requests and responses, as well as handling static content. 
This enables developers to combine view technology and API in one place.

**Miscellaneous:**

The Miscellaneous section comprises additional features offering diverse functionalities, such as:

- **Scheduling** refers to the automated execution of tasks at predefined intervals, allowing them to run in a loop based on specific times or intervals without requiring manual intervention.


# Design Philosophy

This framework is crafted with educational principles at its core, aiming to empower individuals in mastering reflection, architectural design, and comprehending Dependency Inversion. 
It serves as a guide to understanding the construction of web servlet applications. Its primary goal is to facilitate team collaboration and cohesive development efforts.

# Feedback and Contributions

If you suspect an issue within *the Bring Framework* or wish to propose a new feature, kindly utilize [GitHub Issues](https://github.com/BlyznytsiaOrg/bring/issues/new) for reporting problems or submitting feature suggestions
If you have a solution in mind or a suggested fix, you can submit a pull request on [Github](https://github.com/BlyznytsiaOrg/bring). In addition, please read and configure you idea to follow our [Setup Code Style Guidelines](https://github.com/BlyznytsiaOrg/bring/wiki/Setup-Code-Style-Guidelines)

# Getting Started

If you're new to *Bring*, consider initiating your experience with a [Bring playground application repo](https://github.com/BlyznytsiaOrg/bring-playground) with a variety of examples of how to use it. 
It offers a swift and opinionated method to develop a Bring-based application ready for play. 
Leveraging *the Bring Framework*, it prioritizes conventions over extensive configurations, aiming to accelerate your setup process and swiftly get you up and running.