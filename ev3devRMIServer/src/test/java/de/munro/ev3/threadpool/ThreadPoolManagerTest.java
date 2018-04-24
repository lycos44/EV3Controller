package de.munro.ev3.threadpool;

import de.munro.ev3.sensor.BackwardSensor;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class ThreadPoolManagerTest {
    private ThreadPoolManager threadPoolManager;
    private BackwardSensor backwardSensor;

    @Before
    public void setUp() {
        backwardSensor = new BackwardSensor();
        threadPoolManager = new ThreadPoolManager() {
            @Override
            protected BackwardSensor createBackwardSensor() {
                return backwardSensor;
            }
        };
    }

    @Test
    public void setTaskDone() {
        threadPoolManager.addTask(Task.ActionType.init, Task.MotorType.climb, Task.MotorType.camera);
        threadPoolManager.setTaskDone(Task.MotorType.climb);
        threadPoolManager.setTaskDone(Task.MotorType.camera);
        assertThat(threadPoolManager.getTask(), is(nullValue()));
    }
}