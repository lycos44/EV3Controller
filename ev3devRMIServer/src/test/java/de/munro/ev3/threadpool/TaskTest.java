package de.munro.ev3.threadpool;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class TaskTest {

    private Task task;

    @Before
    public  void setUp() {
        task = new Task(Task.ActionType.init, Task.MotorType.camera, Task.MotorType.climb);
    }

    @Test
    public void isDone() {
        assertThat(task.isDone(), is(false));
        task.unsetAssignedTo(Task.MotorType.camera);
        assertThat(task.isDone(), is(false));
        task.unsetAssignedTo(Task.MotorType.climb);
        assertThat(task.isDone(), is(true));
    }

    @Test
    public void isAssignedTo() {
        assertThat(task.isAssignedTo(Task.MotorType.camera), is(true));
        assertThat(task.isAssignedTo(Task.MotorType.climb), is(true));
        assertThat(task.isAssignedTo(Task.MotorType.drive), is(false));
        assertThat(task.isAssignedTo(Task.MotorType.steering), is(false));
    }

    @Test
    public void unsetAssignedTo() {
        assertThat(task.isAssignedTo(Task.MotorType.camera), is(true));
        task.unsetAssignedTo(Task.MotorType.camera);
        assertThat(task.isAssignedTo(Task.MotorType.camera), is(false));
    }

    @Test
    public void getActionType() {
        assertThat(task.getActionType(), is(Task.ActionType.init));
    }
}