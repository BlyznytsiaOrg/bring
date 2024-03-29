## Logging Documentation


### Purpose:

Logging is an essential aspect of software development that allows developers to monitor the execution flow, troubleshoot issues, and gather valuable information about the application's behavior. In Java, logging is commonly implemented using various logging frameworks.

### Log Levels

Java logging frameworks typically support different log levels, each indicating the severity of the message. The common log levels include:

- **TRACE**: The most detailed level, often used for debugging purposes.
- **DEBUG**: Messages that provide detailed information for debugging.
- **INFO**: Informational messages that highlight the progress of the application.
- **WARN**: Warning messages that indicate potential issues that do not necessarily lead to errors.
- **ERROR**: Messages indicating error conditions that should be addressed.
- **FATAL**: The most severe level, indicating a critical error that may lead to application failure.

The level of Bring log can be configured in VM options

```
-Dio.github.blyznytsiaorg.bring.web.log.level
-Dio.github.blyznytsiaorg.bring.web.server.log.level
-Dio.github.blyznytsiaorg.bring.web.servlet.log.level
-Dio.github.blyznytsiaorg.bring.log.level
-Dio.github.blyznytsiaorg.bring.core.bfpp.log.level
-Dio.github.blyznytsiaorg.bring.core.bpp.log.level
-Dio.github.blyznytsiaorg.bring.core.context.log.level
-Dio.github.blyznytsiaorg.bring.core.env.log.level
```

### Usage Example

```
-Dio.github.blyznytsiaorg.bring.web.log.level=TRACE
```
![Example of logging](https://github.com/BlyznytsiaOrg/bring/assets/66901090/c4aea013-15ca-4eab-9898-feb465937eb6)