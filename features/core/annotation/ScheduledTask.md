# ScheduledTask Annotation

**Annotation used to mark a method as a scheduled task. This annotation is part of the Bring Framework's scheduling support, allowing the method to be automatically invoked at specified intervals.**

The annotated method must adhere to the requirements for a scheduled task method, including having no parameters and a void return type.

You can customize the scheduling behavior by providing values for the `value`, `initialDelay`, `period`, and `timeUnit` attributes. If not specified, the default values will be used.

**Usage Example:**
```
@Service
public class MyScheduledService {

    @ScheduledTask(value = "myTask", initialDelay = 1000, period = 5000, timeUnit = TimeUnit.MILLISECONDS)
    public void myScheduledMethod() {
        // Task logic
    }
}
```

- [Java Doc](https://yevgendemotestorganization.github.io/bring-core-javadoc/com/bobocode/bring/core/annotation/ScheduledTask.html)