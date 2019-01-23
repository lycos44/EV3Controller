package de.munro.ev3.motor;

import de.munro.ev3.rmi.EV3devConstants;
import de.munro.ev3.sensor.CameraSensor;
import de.munro.ev3.threadpool.Task;
import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3MediumRegulatedMotor;
import lejos.hardware.port.Port;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CameraMotor extends Motor {
    private static final Logger LOG = LoggerFactory.getLogger(CameraMotor.class);
    private static final int MOTOR_SPEED = 400;

    private BaseRegulatedMotor motor;

    private int leftmostPosition = 0;
    private int rightmostPosition = 0;
    private int homePosition = 0;
    private CameraSensor cameraSensor;

    /**
     * Constructor
     * @param cameraSensor
     */
    public CameraMotor(CameraSensor cameraSensor) {
        super(EV3devConstants.CAMERA_MOTOR_PORT, Polarity.NORMAL, Task.MotorType.camera);
        this.cameraSensor = cameraSensor;
        this.motor = createMotor(EV3devConstants.CAMERA_MOTOR_PORT);
        this.motor.setSpeed(MOTOR_SPEED);
    }

    private EV3MediumRegulatedMotor createMotor(Port port) {
        try {
            return new EV3MediumRegulatedMotor(port);
        } catch (RuntimeException e) {
            LOG.error("Catch", e);
        }
        return null;
    }

    /**
     * @link Motor#getMotor()
     */
    @Override
    BaseRegulatedMotor getMotor() {
        return motor;
    }

    /**
     * @link Motor#init()
     */
    @Override
    public void init() {
        LOG.debug("init()");
        // search for the position that can be set to zero
        getMotor().backward();
        boolean cameraSensorIritated = isCameraSensorPressed();
        while(!isCameraSensorPressed() || cameraSensorIritated) {
            if (cameraSensorIritated) {
                cameraSensorIritated = isCameraSensorPressed();
            }
        }
        stop();
        resetTachoCount();
        getMotor().forward();
        cameraSensorIritated = isCameraSensorPressed();
        while(!isCameraSensorPressed() || cameraSensorIritated) {
            if (cameraSensorIritated) {
                cameraSensorIritated = isCameraSensorPressed();
            }
        }
        stop();
        leftmostPosition = getTachoCount();
        rightmostPosition = 0;
        homePosition = leftmostPosition/2-100;
        rotateTo(homePosition);
        LOG.debug("(left, home, right): ({}, {}, {})", leftmostPosition, homePosition, rightmostPosition);
    }

    private boolean isCameraSensorPressed() {
        return cameraSensor.isPressed();
    }

    public void goHome() {
        LOG.debug("goHome()");
        rotateTo(homePosition);
    }

    public void goLeft() {
        LOG.debug("goLeft()");
        rotateTo(leftmostPosition);
    }

    public void goRight() {
        LOG.debug("goRight()");
        rotateTo(rightmostPosition);
    }
}
