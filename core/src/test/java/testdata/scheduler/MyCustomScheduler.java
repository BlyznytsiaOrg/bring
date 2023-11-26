package testdata.scheduler;

import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.core.anotation.ScheduledTask;

import java.util.concurrent.TimeUnit;

@Component
public class MyCustomScheduler {

    private boolean isRunTaskCalled = false;

    @ScheduledTask(value = "myTask")
    public void runTask() {
        isRunTaskCalled = true;
    }


    public boolean isRunTaskCalled() {
        return isRunTaskCalled;
    }
}
