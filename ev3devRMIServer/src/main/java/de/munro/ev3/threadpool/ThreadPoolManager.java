package de.munro.ev3.threadpool;

import de.munro.ev3.motor.*;
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

    private ExecutorService executor;
    private Queue<Task> taskQueue;

    private BackwardSensor backwardSensor;
    private CameraSensor cameraSensor;
    private ColorSensor colorSensor;
    private DistanceSensor distanceSensor;

    private DriveMotor driveMotor;
    private ClimbMotor climbMotor;
    private SteeringMotor steeringMotor;
    private CameraMotor cameraMotor;

    public ThreadPoolManager() {
        taskQueue = new LinkedList<>();
        // first action is init
        taskQueue.add(new Task(Task.ActionType.init, Task.MotorType.camera));

        executor = Executors.newFixedThreadPool(8);
    }

    public void initialize() {
//        try {
//            backwardSensor = new BackwardSensor();
//            executor.execute(backwardSensor);
//            cameraSensor = new CameraSensor();
//            executor.execute(cameraSensor);
//            colorSensor = new ColorSensor();
//            executor.execute(colorSensor);
//            distanceSensor = new DistanceSensor();
//            executor.execute(distanceSensor);
//
//            driveMotor = new DriveMotor(this);
//            executor.execute(driveMotor);
//            climbMotor = new ClimbMotor();
//            executor.execute(climbMotor);
//            steeringMotor = new SteeringMotor();
//            executor.execute(steeringMotor);
//            cameraMotor = new CameraMotor(this);
//            executor.execute(cameraMotor);
//        } catch (EV3MotorInitializationException e) {
//            LOG.error(e.getMessage(), e);
//            this.shutdown();
//        }
    }

    public void shutdown() {
        LOG.info("shutdown");
        executor.shutdown();
    }

    void addTask(Task.ActionType actionType, Task.MotorType... types) {
        LOG.info("add Task: {}, {}", actionType, types );
        Task task = new Task(actionType, types);
        taskQueue.add(task);
    }

    public synchronized Task getTask() {
        if (!taskQueue.isEmpty()) {
            return taskQueue.element();
        }
        return null;
    }

    public void setTaskDone(Task.MotorType motorType) {
        LOG.info("setTaskDone({})", motorType);
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

    public boolean isInitialized() {
        if (!backwardSensor.isInitialized()) return false;
        if (!cameraSensor.isInitialized()) return false;
        if (!colorSensor.isInitialized()) return false;
        if (!distanceSensor.isInitialized()) return false;

        if (null != driveMotor && !driveMotor.isInitialized()) return false;
        if (null != climbMotor && !climbMotor.isInitialized()) return false;
        if (null != steeringMotor && !steeringMotor.isInitialized()) return false;
        if (null != cameraMotor && !cameraMotor.isInitialized()) return false;

        return true;
    }

    public void execute(Motor motor) {
        executor.execute(motor);
    }
}
