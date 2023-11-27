## @Value Annotation Documentation


### Purpose:

The @Value annotation in Bring is used to inject values from property files into Bring beans.

- Syntax:

```
@Value("property.name")
private String propertyName;
```

- Parameters:
  - property.name: Refers to the property key in the properties file that resolves to a value.


- Example Usage:

```
import com.bobocode.bring.core.annotation.Component;
import com.bobocode.bring.core.annotation.Value;

@Component
public class MyComponent {

    @Value("bring.main.banner-mode")
    private String bannerMode;
}
```

- Supported place 
  - constructor injection
  - setter injection 
  - field injection


- Explanation:
  - Import Statements: 
    - com.bobocode.bring.core.annotation.Value: Import the @Value annotation.
  - Annotation Usage: 
    - Annotate the fields in your class that you want to populate with values from the properties file using @Value("${property.key}").
  - Property Injection:
    - The username and password fields will be populated from the application.properties file or any other property file specified in your Bring configuration.
  - Usage in application.properties:

    ```
    bring.main.banner-mode=on
    ```

Ensure that application.properties is placed in the classpath of your application.

The @Value annotation is a convenient way to inject property values into your Bring components, allowing for externalized configuration and flexibility in managing your application's properties.


//TODO add addition link to bringplayground repo with more examples:




