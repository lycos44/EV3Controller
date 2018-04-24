package de.munro.ev3.threadpool;

import de.munro.ev3.motor.CameraMotor;
import de.munro.ev3.motor.ClimbMotor;
import de.munro.ev3.motor.DriveMotor;
import de.munro.ev3.motor.SteeringMotor;
import de.munro.ev3.sensor.BackwardSensor;
import de.munro.ev3.sensor.CameraSensor;
import de.munro.ev3.sensor.ColorSensor;
import de.munro.ev3.sensor.DistanceSensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolManager {
    private static final Logger LOG = LoggerFactory.getLogger(ThreadPoolManager.class);

    private static ThreadPoolManager instance;
    private ExecutorService executor;
    private Queue<Task> taskQueue;
    private BackwardSensor backwardSensor;
    private CameraSensor cameraSensor;
    private ColorSensor colorSensor;
    private DistanceSensor distanceSensor;

    public ThreadPoolManager() {
        taskQueue = new LinkedList<>();
        // first action is init
        taskQueue.add(new Task(Task.ActionType.init, Task.MotorType.camera));

        executor = Executors.newFixedThreadPool(8);

        backwardSensor = createBackwardSensor();
        executor.execute(backwardSensor);
        cameraSensor = new CameraSensor();
        executor.execute(cameraSensor);
        colorSensor = new ColorSensor();
        executor.execute(colorSensor);
        distanceSensor = new DistanceSensor();
        executor.execute(distanceSensor);

        executor.execute(new DriveMotor());
        executor.execute(new ClimbMotor());
        executor.execute(new SteeringMotor());
        executor.execute(new CameraMotor(this));
    }

    /**
     * for test
     * @return BackwardSensor
     */
    protected BackwardSensor createBackwardSensor() {
        return new BackwardSensor();
    }

    public void shutdown() {
        LOG.info("shutdown");
        executor.shutdown();
    }

    void addTask (Task.ActionType actionType, Task.MotorType ... types) {
        Task task = new Task(actionType, types);
        taskQueue.add(task);
    }

    public synchronized Task getTask() {
        return taskQueue.element();
    }

    public void setTaskDone(Task.MotorType motorType) {
        Task task = getTask();
        task.unsetAssignedTo(motorType);
        if (task.isDone()) {
            taskQueue.remove();
        }
    }

    public Event getBackwardSensorEvent() {
        return backwardSensor.getEvent();
    }

    public Event getCameraSensorEvent() {
        return cameraSensor.getEvent();
    }

    public Event getColorSensorEvent() {
        return colorSensor.getEvent();
    }

    public Event getDistanceSensorEvent() {
        return distanceSensor.getEvent();
    }

    public Queue<Task> getTaskQueue() {
        return taskQueue;
    }
}
