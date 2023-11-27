package testdata.scheduler;

import com.bobocode.bring.core.annotation.Component;
import com.bobocode.bring.core.annotation.ScheduledTask;

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
