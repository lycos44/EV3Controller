package de.munro.ev3.motor;

import de.munro.ev3.rmi.EV3devConstants;
import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3MediumRegulatedMotor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClimbFrontMotor extends Motor {
    private static final Logger LOG = LoggerFactory.getLogger(ClimbFrontMotor.class);
    private static final int MOTOR_SPEED_INITIAL = 200;
    private static final int MOTOR_SPEED = 1000;
    private static final int MOTOR_INITIAL_UP_POSITION = 20;

    private BaseRegulatedMotor motor;
    private int downPosition = 0;

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
            LOG.error("Construct climbFront motor: ", e);
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
        rotateTillStopped(Rotation.reverse);
        resetTachoCount();
        rotateTillStopped(Rotation.ahead);
        downPosition = this.getTachoCount()-20;
        rotateTo(MOTOR_INITIAL_UP_POSITION);

        LOG.debug("(up, down): ({}, {})", MOTOR_INITIAL_UP_POSITION, downPosition);
        setSpeed(MOTOR_SPEED);
    }

    /**
     * turn the climbFront into the up position
     */
    public void goUp() {
        LOG.debug("goUp()");
        rotateTo(MOTOR_INITIAL_UP_POSITION);
    }

    /**
     * turn the climbFront into the down position
     */
    public void goDown() {
        LOG.debug("goDown()");
        rotateTo(downPosition);
    }
}
