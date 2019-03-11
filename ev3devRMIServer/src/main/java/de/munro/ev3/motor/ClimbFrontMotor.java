package de.munro.ev3.motor;

import de.munro.ev3.rmi.EV3devConstants;
import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3MediumRegulatedMotor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClimbFrontMotor extends Motor {
    private static final Logger LOG = LoggerFactory.getLogger(ClimbFrontMotor.class);
    private static final int MOTOR_SPEED_INITIAL = 100;
    private static final int MOTOR_SPEED = 400;
    public static final int TOLERANCE_POSITION = 15;

    private BaseRegulatedMotor motor;

    private int homePosition = 0;
    private int climbPosition = 0;

    /**
     * Constructor
     */
    public ClimbFrontMotor() {
        super(Polarity.INVERSED, MotorType.climbFront);
        int attempts = 0;
        do {
            this.motor = createMotor();
        } while (null == this.motor && attempts++<1);
        if (null == this.motor) {
            LOG.error("Initialisation of {} failed", this.getClass().getSimpleName());
            System.exit(EV3devConstants.SYSTEM_UNEXPECTED_ERROR);
        }
        this.motor.setSpeed(MOTOR_SPEED_INITIAL);
    }

    /**
     * @link Motor#createMotor()
     */
    @Override
    EV3MediumRegulatedMotor createMotor() {
        try {
            return new EV3MediumRegulatedMotor(this.getMotorType().getPort());
        } catch (RuntimeException e) {
            LOG.error("Construct climbFront motor", e);
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
        // search for the home position that can be set to the tolerance position
        rotateTillStopped(Direction.BACKWARD);
        resetTachoCount();
        homePosition = TOLERANCE_POSITION;
        rotateTillStopped(Direction.FORWARD);
        climbPosition = getTachoCount()-TOLERANCE_POSITION;
        rotateTo(homePosition);
        LOG.debug("(home, climbFront): ({}, {})", homePosition, climbPosition);
        setSpeed(MOTOR_SPEED);
    }

    /**
     * turn the climbFront into the home position
     */
    public void goHome() {
        LOG.debug("goHome()");
        rotateTo(homePosition);
    }

    /**
     * turn the climbFront into the home position
     */
    public void goClimb() {
        LOG.debug("goClimb()");
        rotateTo(climbPosition);
    }
}
