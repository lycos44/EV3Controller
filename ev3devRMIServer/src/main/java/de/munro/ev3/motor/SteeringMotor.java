package de.munro.ev3.motor;

import de.munro.ev3.logger.SteeringMotorLogger;
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
    private final SteeringMotorLogger steeringMotorLogger;
    private EV3devConstants.Turn currentTurn;

    /**
     * Constructor
     * @param steeringMotorLogger data logger
     */
    public SteeringMotor(SteeringMotorLogger steeringMotorLogger) {
        super(Polarity.normal, MotorType.steering);
        this.steeringMotorLogger = steeringMotorLogger;
        this.currentTurn = steeringMotorLogger.getSteeringTurn();
        int attempts = 0;
        do {
            this.motor = createMotor();
        } while (null == this.motor && attempts++<1);
        if (null == this.motor) {
            LOG.error("Initialisation of {} failed", this.getClass().getSimpleName());
            System.exit(EV3devConstants.SYSTEM_UNEXPECTED_ERROR);
        }
        this.motor.setSpeed(MOTOR_SPEED);
        this.getLogger().setRunning(true);
    }

    /**
     * get the leftmost motor position
     * @return leftmost position
     */
    public int getLeftmostPosition() {
        return Integer.parseInt(getProperties().get(LEFTMOST_POSITION).toString());
    }

    /**
     * set the leftmost motor position
      * @param position leftmost
     */
    public void setLeftmostPosition(int position) {
        getProperties().put(LEFTMOST_POSITION, toString(position));
    }

    /**
     * get the rightmost motor position
     * @return rightmost position
     */
    public int getRightmostPosition() {
        return Integer.parseInt(getProperties().get(RIGHTMOST_POSITION).toString());
    }

    /**
     * set the leftmost motor position
     * @param position rightmost
     */
    public void setRightmostPosition(int position) {
        getProperties().put(RIGHTMOST_POSITION, toString(position));
    }

    /**
     * get the home position of the motor
     * @return home position
     */
    public int getHomePosition() {
        int homePosition = Integer.parseInt(getProperties().get(HOME_POSITION).toString());
        homePosition += Integer.parseInt(getProperties().get(IMPROVE_HOME_POSITION).toString());
        return homePosition;
    }

    /**
     * set the home position of the motor
     * @param position home
     */
    public void setHomePosition(int position) {
        getProperties().put(HOME_POSITION, toString(position));
    }

    /**
     * set the home position of the motor
     * @param position home
     */
    public void setImproveHomePosition(int position) {
        getProperties().put(IMPROVE_HOME_POSITION, toString(position));
    }

    /**
     * @link Motor#createMotor()
     */
    @Override
    EV3MediumRegulatedMotor createMotor() {
        try {
            return new EV3MediumRegulatedMotor(this.getMotorType().getPort());
        } catch (RuntimeException e) {
            LOG.error("Construct steering motor: ", e);
        }
        return null;
    }

    @Override
    public void readLoggerData() {
        if (currentTurn == getLogger().getSteeringTurn()) {
            return;
        }
        switch (getLogger().getSteeringTurn()) {
            case left:
                this.goLeft();
                break;
            case right:
                this.goRight();
                break;
            case straight:
                this.goStraight();
                break;
        }
        currentTurn = getLogger().getSteeringTurn();
    }

    @Override
    public boolean isRunning() {
        return steeringMotorLogger.isRunning();
    }

    public SteeringMotorLogger getLogger() {
        return steeringMotorLogger;
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
        rotateTillStopped(Rotation.reverse);
        resetTachoCount();
        rotateTillStopped(Rotation.ahead);
        setLeftmostPosition(getTachoCount());
        setHomePosition(getLeftmostPosition()/2);
        setImproveHomePosition(0);
        rotateTo(getHomePosition());
        // adjust positions
        setLeftmostPosition(getLeftmostPosition()-MOTOR_POSITION_BUFFER);
        setRightmostPosition(MOTOR_POSITION_BUFFER);
        if (readPropertyFile()) {
            return;
        }
        writePropertyFile();
    }

    /**
     * reset motor settings
     */
    public void reset() {
        LOG.debug("reset()");
        readPropertyFile();

        rotateTillStopped(Rotation.reverse);
        resetTachoCount();
        rotateTillStopped(Rotation.ahead);
        rotateTo(getHomePosition());
        LOG.debug("(left, home, right): ({}, {}, {})", getLeftmostPosition(), getHomePosition(), getRightmostPosition());
    }

    /**
     * @link Motor#verifyProperties()
     */
    @Override
    public boolean verifyProperties() {
        return getProperties().getProperty(LEFTMOST_POSITION) != null
                && getProperties().getProperty(HOME_POSITION) != null
                && getProperties().getProperty(RIGHTMOST_POSITION) != null;
    }

    /**
     * @link Motor#logStatus()
     */
    @Override
    public void logStatus() {
        LOG.debug("(left, home, right): ({}, {}, {})", getLeftmostPosition(), getHomePosition(), getRightmostPosition());
    }

    /**
     * turn the steering into the home position
     */
    public void goStraight() {
        LOG.debug("goStraight()");
        rotateTo(getHomePosition());
    }

    /**
     * turn the steering into the leftmost position
     */
    public void goLeft() {
        LOG.debug("goLeft()");
        rotateTo(getLeftmostPosition());
    }

    /**
     * turn the steering into the rightmost position
     */
    public void goRight() {
        LOG.debug("goRight()");
        rotateTo(getRightmostPosition());
    }
}
