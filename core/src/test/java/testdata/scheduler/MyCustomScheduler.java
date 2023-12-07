package testdata.scheduler;

import io.github.blyznytsiaorg.bring.core.annotation.Component;
import io.github.blyznytsiaorg.bring.core.annotation.ScheduledTask;

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
