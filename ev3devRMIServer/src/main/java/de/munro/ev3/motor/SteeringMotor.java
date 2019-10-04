package de.munro.ev3.motor;

import de.munro.ev3.rmi.EV3devConstants;
import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3MediumRegulatedMotor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SteeringMotor extends Motor {
    private static final Logger LOG = LoggerFactory.getLogger(SteeringMotor.class);
    private static final int MOTOR_SPEED = 200;
    private static final int MOTOR_POSITION_BUFFER = 10;

    private BaseRegulatedMotor motor;

    private int leftmostPosition = 0;
    private int rightmostPosition = 0;
    private int homePosition = 0;

    /**
     * Constructor
     */
    public SteeringMotor() {
        super(Polarity.NORMAL, MotorType.steering);
        int attempts = 0;
        do {
            this.motor = createMotor();
        } while (null == this.motor && attempts++<1);
            if (null == this.motor) {
            LOG.error("Initialisation of {} failed", this.getClass().getSimpleName());
            System.exit(EV3devConstants.SYSTEM_UNEXPECTED_ERROR);
        }
        this.motor.setSpeed(MOTOR_SPEED);
    }

    /**
     * @link Motor#createMotor()
     */
    @Override
    EV3MediumRegulatedMotor createMotor() {
        try {
            return new EV3MediumRegulatedMotor(this.getMotorType().getPort());
        } catch (RuntimeException e) {
            LOG.error("Construct steering motor:", e);
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
        // adjust positions
        leftmostPosition -= MOTOR_POSITION_BUFFER;
        rightmostPosition = MOTOR_POSITION_BUFFER;
        LOG.debug("(left, home, right): ({}, {}, {})", leftmostPosition, homePosition, rightmostPosition);
    }

    /**
     * turn the climbFront into the home position
     */
    public void goStraight() {
        LOG.debug("goStraight()");
        rotateTo(homePosition);
    }

    /**
     * turn the climbFront into the leftmost position
     */
    public void goLeft() {
        LOG.debug("goLeft()");
        rotateTo(leftmostPosition);
    }

    /**
     * turn the climbFront into the rightmost position
     */
    public void goRight() {
        LOG.debug("goRight()");
        rotateTo(rightmostPosition);
    }
}
