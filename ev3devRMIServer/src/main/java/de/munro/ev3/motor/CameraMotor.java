package de.munro.ev3.motor;

import de.munro.ev3.rmi.EV3devConstants;
import de.munro.ev3.threadpool.Task;
import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3MediumRegulatedMotor;
import lejos.utility.Delay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CameraMotor extends Motor {
    private static final Logger LOG = LoggerFactory.getLogger(CameraMotor.class);
    private static final int MOTOR_SPEED = 400;

    private static final int TURN_LEFT = 1;
    private static final int TURN_RIGHT = -1;

    private BaseRegulatedMotor motor;

    private final int homePosition = 0;
    private int lastDirection = 0;
    private int leftmostPosition = 0;
    private int rightmostPosition = 0;

    /**
     * Constructor
     */
    public CameraMotor() {
        super(EV3devConstants.CAMERA_MOTOR_PORT, Polarity.INVERSED, Task.MotorType.camera);
        this.motor = new EV3MediumRegulatedMotor(EV3devConstants.CAMERA_MOTOR_PORT);
        this.motor.setAcceleration(MOTOR_SPEED);
    }

    public void initialize() throws EV3MotorInitializationException {
    }

    /**
     * @link Motor#getMotor()
     */
    @Override
    public BaseRegulatedMotor getMotor() {
        return motor;
    }

    /**
     * @link Motor#init()
     */
    @Override
    public void init() {
        LOG.debug("init()");
        rotateTillSensorPressed(TURN_LEFT);
        getMotor().resetTachoCount();
        int left = getMotor().getTachoCount();
        LOG.debug("left: {}", left);
        rotateTillSensorPressed(TURN_RIGHT);
        int right = getMotor().getTachoCount();
        LOG.debug("right: {}", right);
        int home = (left+right)/2;
        leftmostPosition = home;
        rightmostPosition = -home;
        LOG.debug("home: {}", home);
        getMotor().resetTachoCount();
        rotateTo(TURN_LEFT, home);
        getMotor().resetTachoCount();
        LOG.debug("(left, home, right): ({}, {}, {})", leftmostPosition, homePosition, rightmostPosition);
    }

    private boolean isCameraSensorPressed() {
        return false;
    }

    /**
     * the rotation of the motor proceeds into the direction until the corresponding touchSensor is pressed.
     * @param direction
     */
    public void rotateTillSensorPressed(int direction) {
        LOG.debug("rotateTillSensorPressed({})", direction==1?"left":"right");
        while ( !isCameraSensorPressed() || lastDirection != direction ) {
            go(direction);
            // when changing the direction, the sensor still stays pressed for some time.
            if ( !isCameraSensorPressed() && lastDirection != direction ) {
                lastDirection = direction;
                LOG.debug("lastDirection {}", lastDirection==1?"left":"right");
            }
            Delay.msDelay(100);
        }
        // the event is removed by the sensor itself, when the sensor is not pressed anymore
        getMotor().stop();
        LOG.debug("stalled position: {}", getMotor().getTachoCount());
    }

    /**
     * the motor rotates into the direction until the limitAngle has been reached.
     * @param direction
     * @param limitAngle
     */
    private void rotateTo(int direction, int limitAngle) {
        LOG.debug("rotateTo({}, {}) lastDirection {}", direction==1?"left":"right", limitAngle, lastDirection==1?"left":"right");
        int currentPosition = getMotor().getTachoCount();
        LOG.debug("(currentPosition: {}, limitAngle: {})", currentPosition, limitAngle);
        getMotor().rotateTo(limitAngle);
//        while ( (!isCameraSensorPressed() || lastDirection != direction) && currentPosition < limitAngle ) {
//            go(direction);
//            if ( !isCameraSensorPressed() && lastDirection != direction ) {
//                lastDirection = direction;
//                LOG.debug("lastDirection {}", lastDirection==1?"left":"right");
//            }
//            LOG.debug("CameraSensor.isPressed(): {}", isCameraSensorPressed());
//            currentPosition = getMotor().getTachoCount();
//            LOG.debug("(currentPosition: {}, limitAngle: {})", currentPosition, limitAngle);
//            Delay.msDelay(100);
//        }
//        getMotor().stop();
        LOG.debug("stalled position: {}", getMotor().getTachoCount());
    }

    /**
     * @link Runnable#run()
     */
    @Override
    public void run() {
        LOG.info(Thread.currentThread().getName()+" started");
        while ( !Thread.interrupted() ) {
//            Task task = threadPoolManager.getTask();
//            if (null != task && task.isAssignedTo(getMotorType())) {
//                proceedTask(task.getActionType());
//                threadPoolManager.setTaskDone(Task.MotorType.camera);
//            }
            Delay.msDelay(1000);
        }
        this.stop();
        LOG.info(Thread.currentThread().getName()+" stopped");
    }

    private void go(int direction) {
        switch (direction) {
            case TURN_LEFT:
                forward();
                break;
            case TURN_RIGHT:
                backward();
                break;
        }
    }

    /**
     * @return leftmostPosition
     */
    int getLeftmostPosition() {
        return leftmostPosition;
    }

    /**
     * @return rightmostPosition
     */
    int getRightmostPosition() {
        return  rightmostPosition;
    }
}
