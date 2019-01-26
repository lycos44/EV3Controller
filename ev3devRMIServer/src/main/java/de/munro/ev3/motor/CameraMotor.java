package de.munro.ev3.motor;

import de.munro.ev3.rmi.EV3devConstants;
import de.munro.ev3.sensor.CameraSensor;
import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3MediumRegulatedMotor;
import lejos.hardware.port.Port;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CameraMotor extends Motor {
    private static final Logger LOG = LoggerFactory.getLogger(CameraMotor.class);
    private static final int MOTOR_SPEED = 800;

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
        super(EV3devConstants.CAMERA_MOTOR_PORT, Polarity.NORMAL, MotorType.camera);
        this.cameraSensor = cameraSensor;
        this.motor = createMotor(EV3devConstants.CAMERA_MOTOR_PORT);
        this.motor.setSpeed(MOTOR_SPEED);
    }

    /**
     * @link Motor#createMotor()
     */
    @Override
    EV3MediumRegulatedMotor createMotor(Port port) {
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
     * @link Motor#is2BeStopped()
     */
    @Override
    boolean is2BeStopped() {
        return false;
    }

    /**
     * @link Motor#init()
     */
    @Override
    public void init() {
        LOG.debug("init()");
        // search for the position that can be set to zero
        backward();
        boolean cameraSensorIritated = isCameraSensorPressed();
        while(!isCameraSensorPressed() || cameraSensorIritated) {
            if (cameraSensorIritated) {
                cameraSensorIritated = isCameraSensorPressed();
            }
        }
        stop();
        resetTachoCount();
        forward();
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

    /**
     * @link EV3TouchSensor#isPressed()
     */
    boolean isCameraSensorPressed() {
        return cameraSensor.isPressed();
    }

    /**
     * turn the camera into the home position
     */
    public void goHome() {
        LOG.debug("goHome()");
        rotateTo(homePosition);
    }

    /**
     * turn the camera into the leftmost position
     */
    public void goLeft() {
        LOG.debug("goLeft()");
        rotateTo(leftmostPosition);
    }

    /**
     * turn the camera into the rightmost position
     */
    public void goRight() {
        LOG.debug("goRight()");
        rotateTo(rightmostPosition);
    }
}
