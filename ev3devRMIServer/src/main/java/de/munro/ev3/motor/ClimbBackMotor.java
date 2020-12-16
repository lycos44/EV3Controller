package de.munro.ev3.motor;

import de.munro.ev3.logger.ClimbBackMotorLogger;
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
    private final ClimbBackMotorLogger climbBackMotorLogger;
    private EV3devConstants.Climb currentPosition;

    /**
     * Constructor
     * @param climbBackMotorLogger data logger
     */
    public ClimbBackMotor(ClimbBackMotorLogger climbBackMotorLogger) {
        super(Polarity.inversed, MotorType.climbBack);
        this.climbBackMotorLogger = climbBackMotorLogger;
        this.currentPosition = climbBackMotorLogger.getClimbBack();
        int attempts = 0;
        do {
            this.motor = createMotor();
        } while (null == this.motor && attempts++<1);
        if (null == this.motor) {
            LOG.error("Initialisation of {} failed", this.getClass().getSimpleName());
            System.exit(EV3devConstants.SYSTEM_UNEXPECTED_ERROR);
        }
        this.getLogger().setRunning(true);
    }

    /**
     * @link Motor#go()
     */
    @Override
    public void readLoggerData() {
        if (currentPosition == getLogger().getClimbBack()) {
            return;
        }
        switch (getLogger().getClimbBack()) {
            case up:
                this.goUp();
                break;
            case down:
                this.goDown();
                break;
        }
        currentPosition = getLogger().getClimbBack();
    }

    @Override
    public boolean isRunning() {
        return climbBackMotorLogger.isRunning();
    }

    public ClimbBackMotorLogger getLogger() {
        return climbBackMotorLogger;
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
        setUpPosition(30);
        setSpeed(MOTOR_SPEED_INITIAL);
        rotateTillStopped(Rotation.reverse);
        resetTachoCount();

        rotateTillStopped(Rotation.ahead);
        setDownPosition(this.getTachoCount()-20);
        goUp();

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
