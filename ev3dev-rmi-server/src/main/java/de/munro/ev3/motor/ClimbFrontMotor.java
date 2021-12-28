package de.munro.ev3.motor;

import de.munro.ev3.data.ClimbFrontMotorData;
import de.munro.ev3.data.MotorData;
import de.munro.ev3.rmi.EV3devConstants;
import de.munro.ev3.rmi.RemoteEV3;
import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClimbFrontMotor extends Motor {
    private static final int MOTOR_SPEED_INITIAL = 200;
    private static final int MOTOR_SPEED = 1000;

    private BaseRegulatedMotor motor;
    private final ClimbFrontMotorData climbFrontMotorData;
    private EV3devConstants.Climb climb;

    /**
     * Constructor
     * @param climbFrontMotorData inputdata
     */
    public ClimbFrontMotor(ClimbFrontMotorData climbFrontMotorData) {
        super(Polarity.inversed, RemoteEV3.MotorType.liftFront,climbFrontMotorData);
        this.climbFrontMotorData = climbFrontMotorData;
        this.climb = climbFrontMotorData.getClimb();
        int attempts = 0;
        do {
            log.info("Create motor: {}", attempts);
            this.motor = createMotor(MotorPort.D);
        } while (null == this.motor && attempts++<2);
        if (null == this.motor) {
            log.error("Initialisation of {} failed", this.getClass().getSimpleName());
            System.exit(EV3devConstants.SYSTEM_UNEXPECTED_ERROR);
        }
        this.motor.setSpeed(MOTOR_SPEED_INITIAL);
        this.climbFrontMotorData.setRunning(true);
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
    EV3MediumRegulatedMotor createMotor(lejos.hardware.port.Port port) {
        try {
            return new EV3MediumRegulatedMotor(port);
        } catch (RuntimeException e) {
            log.error("Construct climbFront motor: ", e);
        }
        return null;
    }

    /**
     * @link Motor#workOutMotorData()
     */
    @Override
    public void takeAction() {
        log.debug("takeAction() ({})", climbFrontMotorData.getClimb());
        if (climb == climbFrontMotorData.getClimb()) {
            return;
        }
        switch (climbFrontMotorData.getClimb()) {
            case up:
                this.goUp();
                break;
            case down:
                this.goDown();
                break;
        }
        climb = climbFrontMotorData.getClimb();
        climbFrontMotorData.setDone(true);
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
//        setUpPosition(20);
//        // search for the home position that can be set to the tolerance position
//        rotateTillStopped(Rotation.reverse);
//        resetTachoCount();
//        rotateTillStopped(Rotation.ahead);
//        setDownPosition(getTachoCount()-20);
//        rotateTo(getUpPosition());
//
//        setSpeed(MOTOR_SPEED);
//        if (readPropertyFile()) {
//            return;
//        }
//        writePropertyFile();
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
     * turn the climbFront into the up position
     */
    public void goUp() {
        log.debug("goUp()");
        setSpeed(MOTOR_SPEED_INITIAL);
        rotateTo(getUpPosition());
        setSpeed(MOTOR_SPEED);
    }

    /**
     * turn the climbFront into the down position
     */
    public void goDown() {
        log.debug("goDown()");
        rotateTo(getDownPosition());
    }

    /**
     * @return current climbFrontMotorData
     */
    @Override
    public MotorData getMotorData() {
        return climbFrontMotorData;
    }
}
