# Bring Project

This is a simple Bring library that provides a basic Dependency injection container and Web framework
The library is designed to be easy to use and includes the following functionality:

- ...

# Arch 
 - DI 
 - Web

## DI flow diagram

![Bring DI diagram](https://github-production-user-asset-6210df.s3.amazonaws.com/73576438/282532279-b91629f2-7f02-449f-8133-62dc3045d321.png)

1. The BringApplication provides a simple entry point to initialize and run a BringApplicationContext.
   It allows the user to create and configure a BringApplicationContext either by specifying a base package for component scanning or by providing a class that contains configuration information.

2. The ClassPathScannerFactory is responsible for creating a set of class path scanners, each designed to scan the classpath for specific types of annotated classes relevant to the Bring application. It initializes scanners for components, services, and configuration classes etc.

3. The BeanPostProcessorDefinitionFactory is responsible for creating and managing a list of BeanFactoryPostProcessor instances. It initializes the list with default post-processors, such as the ConfigurationClassPostProcessor etc. In this step we create Bean definitions for classes annotated with annotations from ClassPathScanner

4. The BeanPostProcessorFactory is responsible for creating and managing a list BeanPostProcessor instances. It initializes the list with default post-processors such as the ScheduleBeanPostProcessor and add addition loginc to them.


## Web flow diagram

TODO

## Installation

To use this library, you can include the JAR file in your Java project. You can either build the JAR file from the provided source code or download a pre-built JAR file. Here are the steps for both options:


## License

This Calculator library is provided under the [MIT License](LICENSE). Feel free to use, modify, and distribute it as needed. If you find any issues or have suggestions for improvements, please [open an issue](link-to-issues) on this repository.

## Contact

If you have any questions or need assistance, please feel free to contact the author:

Author: Your Name
Email: your@email.com
GitHub: [Your GitHub Profile](link-to-your-github)
