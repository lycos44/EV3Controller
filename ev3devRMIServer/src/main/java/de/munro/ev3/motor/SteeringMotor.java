package de.munro.ev3.motor;

import de.munro.ev3.rmi.EV3devConstants;
import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3MediumRegulatedMotor;
import lejos.hardware.port.Port;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SteeringMotor extends Motor {
    private static final Logger LOG = LoggerFactory.getLogger(SteeringMotor.class);
    private static final int MOTOR_SPEED = 200;

    private BaseRegulatedMotor motor;

    private int leftmostPosition = 0;
    private int rightmostPosition = 0;
    private int homePosition = 0;

    /**
     * Constructor
     */
    public SteeringMotor() {
        super(EV3devConstants.STEERING_MOTOR_PORT, Polarity.NORMAL, MotorType.steering);
        this.motor = createMotor(EV3devConstants.STEERING_MOTOR_PORT);
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
        return isStalled();
    }

    /**
     * @link Motor#init()
     */
    @Override
    public void init() {
        LOG.debug("init()");
        // search for the position that can be set to zero
        rotateTillStopped(Direction.BACKWARD);
        resetTachoCount();
        rotateTillStopped(Direction.FORWARD);
        leftmostPosition = getTachoCount();
        homePosition = leftmostPosition/2;
        rotateTo(homePosition);
        LOG.debug("(left, home, right): ({}, {}, {})", leftmostPosition, homePosition, rightmostPosition);
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
