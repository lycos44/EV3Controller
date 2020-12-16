package de.munro.ev3.motor;

import de.munro.ev3.logger.ClimbFrontMotorLogger;
import de.munro.ev3.rmi.EV3devConstants;
import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3MediumRegulatedMotor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClimbFrontMotor extends Motor {
    private static final Logger LOG = LoggerFactory.getLogger(ClimbFrontMotor.class);
    private static final int MOTOR_SPEED_INITIAL = 200;
    private static final int MOTOR_SPEED = 1000;

    private BaseRegulatedMotor motor;
    private final ClimbFrontMotorLogger climbFrontMotorLogger;
    private EV3devConstants.Climb currentPosition;

    /**
     * Constructor
     * @param climbFrontMotorLogger data logger
     */
    public ClimbFrontMotor(ClimbFrontMotorLogger climbFrontMotorLogger) {
        super(Polarity.inversed, MotorType.climbFront);
        this.climbFrontMotorLogger = climbFrontMotorLogger;
        this.currentPosition = climbFrontMotorLogger.getClimbFront();
        int attempts = 0;
        do {
            this.motor = createMotor();
        } while (null == this.motor && attempts++<1);
        if (null == this.motor) {
            LOG.error("Initialisation of {} failed", this.getClass().getSimpleName());
            System.exit(EV3devConstants.SYSTEM_UNEXPECTED_ERROR);
        }
        this.motor.setSpeed(MOTOR_SPEED_INITIAL);
        this.getLogger().setRunning(true);
    }

    /**
     * get the motor down position
     * @return down position
     */
    public int getDownPosition() {
        return Integer.parseInt(getProperties().get(DOWN_POSITION).toString());
    }

    /**
     * set the motor down position
     * @param position down
     */
    public void setDownPosition(int position) {
        getProperties().put(DOWN_POSITION, toString(position));
    }

    /**
     * get the motor up position
     * @return up position
     */
    public int getUpPosition() {
        return Integer.parseInt(getProperties().get(UP_POSITION).toString());
    }

    /**
     * set the motor up position
     * @param position up
     */
    public void setUpPosition(int position) {
        getProperties().put(UP_POSITION, toString(position));
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
     * @link Motor#doIt()
     */
    @Override
    public void readLoggerData() {
        if (currentPosition == getLogger().getClimbFront()) {
            return;
        }
        switch (getLogger().getClimbFront()) {
            case up:
                this.goUp();
                break;
            case down:
                this.goDown();
                break;
        }
        currentPosition = getLogger().getClimbFront();
    }

    /**
     * @link Motor#isRunning()
     */
    @Override
    public boolean isRunning() {
        return climbFrontMotorLogger.isRunning();
    }

    public ClimbFrontMotorLogger getLogger() {
        return climbFrontMotorLogger;
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
        setUpPosition(20);
        // search for the home position that can be set to the tolerance position
        rotateTillStopped(Rotation.reverse);
        resetTachoCount();
        rotateTillStopped(Rotation.ahead);
        setDownPosition(getTachoCount()-20);
        rotateTo(getUpPosition());

        setSpeed(MOTOR_SPEED);
        if (readPropertyFile()) {
            return;
        }
        writePropertyFile();
    }

    /**
     * @link Motor#verifyProperties()
     */
    @Override
    public boolean verifyProperties() {
        return getProperties().getProperty(DOWN_POSITION) != null
                && getProperties().getProperty(UP_POSITION) != null;
    }

    /**
     * @link Motor#logStatus()
     */
    @Override
    public void logStatus() {
        LOG.debug("(up, down, speed): ({}, {}, {})", getUpPosition(), getDownPosition(), getSpeed());
    }

    /**
     * turn the climbFront into the up position
     */
    public void goUp() {
        LOG.debug("goUp()");
        setSpeed(MOTOR_SPEED_INITIAL);
        rotateTo(getUpPosition());
        setSpeed(MOTOR_SPEED);
    }

    /**
     * turn the climbFront into the down position
     */
    public void goDown() {
        LOG.debug("goDown()");
        rotateTo(getDownPosition());
    }
}
