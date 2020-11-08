package de.munro.ev3.motor;

import de.munro.ev3.rmi.EV3devConstants;
import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClimbBackMotor extends Motor {
    private static final Logger LOG = LoggerFactory.getLogger(ClimbBackMotor.class);
    private static final int MOTOR_SPEED_INITIAL = 50;
    private static final int MOTOR_SPEED = 600;

    private BaseRegulatedMotor motor;
    private int downPosition = 0;
    private int upPosition = 30;

    /**
     * Constructor
     */
    public ClimbBackMotor() {
        super(Polarity.INVERSED, MotorType.climbBack);
        int attempts = 0;
        do {
            this.motor = createMotor();
        } while (null == this.motor && attempts++<1);
        if (null == this.motor) {
            LOG.error("Initialisation of {} failed", this.getClass().getSimpleName());
            System.exit(EV3devConstants.SYSTEM_UNEXPECTED_ERROR);
        }
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
        // search for the home position that can be set to the tolerance position
        setSpeed(MOTOR_SPEED_INITIAL);
        rotateTillStopped(Rotation.reverse);
        resetTachoCount();

        rotateTillStopped(Rotation.ahead);
        downPosition = this.getTachoCount()-20;
        goUp();

        LOG.debug("(up, down): ({}, {})", upPosition, downPosition);
        setSpeed(MOTOR_SPEED);
    }

    /**
     * turn the climbFront into the up position
     */
    public void goUp() {
        LOG.debug("goUp()");
        rotateTo(upPosition);
        LOG.debug("(up): ({})", this.getTachoCount());
    }

    /**
     * turn the climbFront into the down position
     */
    public void goDown() {
        LOG.debug("goDown()");
        rotateTo(downPosition);
        LOG.debug("(down): ({})", this.getTachoCount());
    }
}
