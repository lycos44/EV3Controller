package de.munro.ev3.motor;

import de.munro.ev3.rmi.EV3devConstants;
import de.munro.ev3.sensor.CameraSensor;
import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import lejos.utility.Delay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CameraMotor extends Motor {
    private static final Logger LOG = LoggerFactory.getLogger(CameraMotor.class);
    private static final int MOTOR_SPEED = 400;
    private static final Polarity MOTOR_POLARITY = Polarity.INVERSED;

    private static final int LEFT = 1;
    private static final int RIGHT = -1;

    private static CameraMotor instance;
    private EV3LargeRegulatedMotor motor;

    private final int homePosition = 0;
    private int lastDirection = 0;
    private int leftmostPosition = 0;
    private int rightmostPosition = 0;

    public static CameraMotor getInstance() {
        if (null == instance) {
            instance = new CameraMotor();
        }
        return instance;
    }

    private CameraMotor() {
        this.motor = createMotor(EV3devConstants.CAMERA_MOTOR_PORT, MOTOR_POLARITY);
        if (null == this.motor) {
            this.motor = createMotor(EV3devConstants.CAMERA_MOTOR_PORT, MOTOR_POLARITY);
        }
    }

    public static boolean isInitialized() {
        LOG.debug("motor {}", instance);
        return null != instance && null != instance.motor;
    }

    @Override
    public EV3LargeRegulatedMotor getMotor() {
        return motor;
    }

    @Override
    public void init() {
        LOG.debug("init()");
        getMotor().setSpeed(MOTOR_SPEED);
        rotateTillSensorPressed(LEFT);
        getMotor().resetTachoCount();
        LOG.info( "CameraSensor.getInstance().isPressed(): "+CameraSensor.getInstance().isPressed());
        int left = getMotor().getTachoCount();
        LOG.debug("left: {}", left);
        rotateTillSensorPressed(RIGHT);
        LOG.info( "CameraSensor.getInstance().isPressed(): "+CameraSensor.getInstance().isPressed());
        int right = getMotor().getTachoCount();
        LOG.debug("right: {}", right);
        int home = (left+right)/2;
        leftmostPosition = home;
        rightmostPosition = -home;
        LOG.debug("home: {}", home);
        getMotor().resetTachoCount();
        rotateTo(LEFT, home);
        LOG.info( "CameraSensor.getInstance().isPressed(): "+CameraSensor.getInstance().isPressed());
        getMotor().resetTachoCount();
        LOG.debug("(left, home, right): ({}, {}, {})", leftmostPosition, homePosition, rightmostPosition);
    }

    public void rotateTillSensorPressed(int direction) {
        LOG.debug("rotateTillSensorPressed({})", direction==1?"left":"right");
        while ( !CameraSensor.getInstance().isPressed() || lastDirection != direction ) {
            go(direction);
            if ( !CameraSensor.getInstance().isPressed() && lastDirection != direction ) {
                lastDirection = direction;
                LOG.debug("lastDirection {}", lastDirection==1?"left":"right");
            }
            Delay.msDelay(100);
        }
        getMotor().stop();
        LOG.debug("stalled position: {}", getMotor().getTachoCount());
    }

    public void rotateTo(int direction, int limitAngle) {
        LOG.debug("rotateTo({}, {}) lastDirection {}", direction==1?"left":"right", limitAngle, lastDirection==1?"left":"right");
        int currentPosition = getMotor().getTachoCount();
        LOG.debug("(currentPosition: {}, limitAngle: {})", currentPosition, limitAngle);
        while ( (!CameraSensor.getInstance().isPressed() || lastDirection != direction) && currentPosition < limitAngle ) {
            go(direction);
            if ( !CameraSensor.getInstance().isPressed() && lastDirection != direction ) {
                lastDirection = direction;
                LOG.debug("lastDirection {}", lastDirection==1?"left":"right");
            }
            LOG.debug("CameraSensor.getInstance().isPressed(): {}", CameraSensor.getInstance().isPressed());
            currentPosition = getMotor().getTachoCount();
            LOG.debug("(currentPosition: {}, limitAngle: {})", currentPosition, limitAngle);
            Delay.msDelay(100);
        }
        getMotor().stop();
        LOG.debug("stalled position: {}", getMotor().getTachoCount());
    }

    private void go(int direction) {
        switch (direction) {
            case LEFT:
                forward();
                break;
            case RIGHT:
                backward();
                break;
        }
    }

    public int getLeftmostPosition() {
        return leftmostPosition;
    }

    public int getRightmostPosition() {
        return  rightmostPosition;
    }
}
