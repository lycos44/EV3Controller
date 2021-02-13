package de.munro.ev3.motor;

import de.munro.ev3.data.MotorData;
import de.munro.ev3.data.SteeringMotorData;
import de.munro.ev3.rmi.EV3devConstants;
import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3MediumRegulatedMotor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SteeringMotor extends Motor {
    
    private static final int MOTOR_SPEED = 200;
    private static final int MOTOR_POSITION_BUFFER = 10;

    private BaseRegulatedMotor motor;
    private final SteeringMotorData steeringMotorData;
    private EV3devConstants.Turn turn;

    /**
     * Constructor
     * @param steeringMotorData steeringMotorData to set
     */
    public SteeringMotor(SteeringMotorData steeringMotorData) {
        super(Polarity.normal, MotorType.steering);
        this.steeringMotorData = steeringMotorData;
        this.turn = steeringMotorData.getTurn();
        int attempts = 0;
        do {
            this.motor = createMotor();
        } while (null == this.motor && attempts++<2);
        if (null == this.motor) {
            log.error("Initialisation of {} failed", this.getClass().getSimpleName());
            System.exit(EV3devConstants.SYSTEM_UNEXPECTED_ERROR);
        }
        this.motor.setSpeed(MOTOR_SPEED);
        this.steeringMotorData.setRunning(true);
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
            log.error("Construct steering motor: ", e);
        }
        return null;
    }

    @Override
    public void workOutMotorData() {
        if (turn == steeringMotorData.getTurn()) {
            return;
        }
        switch (steeringMotorData.getTurn()) {
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
        turn = steeringMotorData.getTurn();
        steeringMotorData.setDone(true);
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
        log.debug("init()");
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
        log.debug("reset()");
        readPropertyFile();

        rotateTillStopped(Rotation.reverse);
        resetTachoCount();
        rotateTillStopped(Rotation.ahead);
        rotateTo(getHomePosition());
        log.debug("(left, home, right): ({}, {}, {})", getLeftmostPosition(), getHomePosition(), getRightmostPosition());
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
        log.debug("(left, home, right): ({}, {}, {})", getLeftmostPosition(), getHomePosition(), getRightmostPosition());
    }

    /**
     * turn the steering into the home position
     */
    public void goStraight() {
        log.debug("goStraight()");
        rotateTo(getHomePosition());
    }

    /**
     * turn the steering into the leftmost position
     */
    public void goLeft() {
        log.debug("goLeft()");
        rotateTo(getLeftmostPosition());
    }

    /**
     * turn the steering into the rightmost position
     */
    public void goRight() {
        log.debug("goRight()");
        rotateTo(getRightmostPosition());
    }

    /**
     * @return current steeringMotorData
     */
    @Override
    public MotorData getMotorData() {
        return steeringMotorData;
    }
}
