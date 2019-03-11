package de.munro.ev3.motor;

import de.munro.ev3.rmi.EV3devConstants;
import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClimbBackMotor extends Motor {
    private static final Logger LOG = LoggerFactory.getLogger(ClimbBackMotor.class);
    private static final int MOTOR_SPEED_INITIAL = 30;
    private static final int MOTOR_SPEED = 150;

    private BaseRegulatedMotor motor;

    private int homePosition = 0;
    private int climbPosition = 0;

    /**
     * Constructor
     */
    public ClimbBackMotor() {
        super(Polarity.NORMAL, MotorType.climbBack);
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
    EV3LargeRegulatedMotor createMotor() {
        try {
            return new EV3LargeRegulatedMotor(this.getMotorType().getPort());
        } catch (RuntimeException e) {
            LOG.error("Construct climbBack motor", e);
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
        climbPosition = getTachoCount();
        rotateTo(homePosition);
        LOG.debug("(home, climbBack): ({}, {})", homePosition, climbPosition);
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
