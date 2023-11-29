# Properties File Support

The `ValuePropertiesPostProcessor` class in Bring Framework supports dynamic profile configuration using VM parameters.

To set the active profile, use the `-Dbring.profiles.active=dev` parameter during application launch. This allows the class to resolve profile-specific properties and apply them to the `DefaultBringBeanFactory`, enabling adaptable configuration based on the specified profile.

Moreover, the active profiles influence the loading of different `application.properties` files. For instance, with an active profile of "dev," the class will load properties from `application-dev.properties`. This mechanism facilitates customized property sets tailored to specific application environments.    