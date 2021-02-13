package de.munro.ev3.motor;

import de.munro.ev3.data.ClimbBackMotorData;
import de.munro.ev3.data.MotorData;
import de.munro.ev3.rmi.EV3devConstants;
import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import lombok.extern.slf4j.Slf4j;

import static de.munro.ev3.rmi.EV3devConstants.Climb.up;

@Slf4j
public class ClimbBackMotor extends Motor {

    private static final int MOTOR_SPEED_INITIAL = 50;
    private static final int MOTOR_SPEED = 600;

    private BaseRegulatedMotor motor;
    private final ClimbBackMotorData climbBackMotorData;
    private EV3devConstants.Climb climb;

    /**
     * Constructor
     * @param climbBackMotorData input data
     */
    public ClimbBackMotor(ClimbBackMotorData climbBackMotorData) {
        super(Polarity.inversed, MotorType.climbBack);
        this.climbBackMotorData = climbBackMotorData;
        this.climb = climbBackMotorData.getClimb();
        int attempts = 0;
        do {
            log.info("Create motor: {}", attempts);
            this.motor = createMotor();
        } while (null == this.motor && attempts++<2);
        if (null == this.motor) {
            log.error("Initialisation of {} failed", this.getClass().getSimpleName());
            System.exit(EV3devConstants.SYSTEM_UNEXPECTED_ERROR);
        }
        climbBackMotorData.setRunning(true);
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
            log.error("Construct climbBack motor", e);
        }
        return null;
    }

    /**
     * @link Motor#workOutMotorData()
     */
    @Override
    public void workOutMotorData() {
        if (climb == climbBackMotorData.getClimb()) {
            return;
        }
        setSpeed(climbBackMotorData.getClimb());
        this.turn(climbBackMotorData.getClimb());
        climb = climbBackMotorData.getClimb();
        climbBackMotorData.setDone(true);
    }

    private void setSpeed(EV3devConstants.Climb climb) {
        if (climb == up) {
            setSpeed(MOTOR_SPEED_INITIAL);
            return;
        }
        setSpeed(MOTOR_SPEED);
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
        // search for the home position that can be set to the tolerance position
        setUpPosition(30);
        setSpeed(MOTOR_SPEED_INITIAL);
        rotateTillStopped(Rotation.reverse);
        resetTachoCount();

        rotateTillStopped(Rotation.ahead);
        setDownPosition(this.getTachoCount()-20);
        turn(up);

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
        log.debug("(up, down, speed): ({}, {}, {})", getUpPosition(), getDownPosition(), getSpeed());
    }

    /**
     * turn the climbFront motor into the climb position
     */
    public void turn(EV3devConstants.Climb climb) {
        int newPosition = getPosition(climb);
        rotateTo(newPosition);
    }

    private int getPosition(EV3devConstants.Climb climb) {
        switch (climb) {
            case up: return Integer.parseInt(getProperties().get(UP_POSITION).toString());
            case down: return Integer.parseInt(getProperties().get(DOWN_POSITION).toString());
        }
        return 0;
    }

    /**
     * @return current climbBackMotorData
     */
    @Override
    public MotorData getMotorData() {
        return climbBackMotorData;
    }
}
