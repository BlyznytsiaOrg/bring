# Scheduling

The @ScheduledTask annotation is a part of the Bring Framework's scheduling support, designed to mark methods as scheduled tasks. This annotation enables automatic invocation of annotated methods at specified intervals, facilitating the execution of recurring tasks within applications.

### Annotation Details
The @ScheduledTask annotation provides the following attributes for customization:
- value: Specifies the name of the scheduled task.
- initialDelay: Represents the delay in milliseconds before the first execution (default: 1000 milliseconds).
- period: Denotes the fixed rate in milliseconds between consecutive executions (default: 5000 milliseconds).
- timeUnit: Specifies the time unit for initialDelay and period (default: TimeUnit.MILLISECONDS).


### Usage Example

```
import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.core.anotation.ScheduledTask;

import java.time.LocalDateTime;

@Component
public class MyScheduledTasks {

    @ScheduledTask(value = "myTask", period = 10000)
    public void scheduledMethod1() {
        System.out.println(Thread.currentThread().getName() + " scheduledMethod1 " + LocalDateTime.now());
    }
}
```

### Explanation

- @Service: Annotation indicating that the class is a Spring service component.
- @ScheduledTask: Applied to a method, marking it as a scheduled task.
  - value = "myTask": Assigns a specific name ("myTask") to this scheduled task.
  - initialDelay = 1000: Sets the initial delay before the first execution of the task to 1000 milliseconds (1 second).
  - period = 5000: Specifies the interval between consecutive executions of the task to 5000 milliseconds (5 seconds).
  - timeUnit = TimeUnit.MILLISECONDS: Indicates that the time values (initial delay and period) are in milliseconds.


The @ScheduledTask annotation simplifies the scheduling of recurring tasks within your application, enhancing efficiency by automating periodic executions.

