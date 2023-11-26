package com.bobocode.bring.core.bpp.impl;

import com.bobocode.bring.core.BringApplication;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testdata.scheduler.MyCustomScheduler;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class ScheduleBeanPostProcessorTest {

    @SneakyThrows
    @DisplayName("Should run scheduler and check that it is triggered")
    @Test
    void shouldRunSchedulerAndCheckThatItIsTriggered() {
        //given
        var bringApplicationContext = BringApplication.run("testdata.scheduler");
        int i = 0;

        //when
        var myCustomScheduler = bringApplicationContext.getBean(MyCustomScheduler.class);

        waitUntilTaskExecuted(i, myCustomScheduler);

        //then
        assertThat(myCustomScheduler.isRunTaskCalled()).isTrue();
    }

    @SneakyThrows
    private static void waitUntilTaskExecuted(int i, MyCustomScheduler myCustomScheduler) {
        while(i <= 2) {
            if (myCustomScheduler.isRunTaskCalled()) {
                break;
            }

            TimeUnit.SECONDS.sleep(2);
            i++;
        }
    }


}