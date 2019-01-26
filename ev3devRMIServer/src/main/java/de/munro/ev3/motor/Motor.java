package de.munro.ev3.motor;

import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import lejos.hardware.port.Port;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Motor {
    private static final Logger LOG = LoggerFactory.getLogger(Motor.class);

    public enum Direction {
        FORWARD,
        BACKWARD,
        NOT_SET
    }
    public enum Polarity {
        NORMAL,
        INVERSED
    }
    public enum MotorType {
        camera,
        climb,
        drive,
        steering
    }

    private final Port port;
    private final Polarity polarity;
    private final MotorType motorType;
    private Direction directionStalled = Direction.NOT_SET;

    /**
     * Constructor
     */
    public Motor(Port port, Polarity polarity, MotorType motorType) {
        this.port = port;
        this.polarity = polarity;
        this.motorType = motorType;
    }

    /**
     * information about the member motor instance
     * @return true, if the member motor is not null
     */
    public boolean isInitialized() {
        LOG.debug("isInitialized()");
        return null != getMotor();
    }

    /**
     * @link BaseRegulatedMotor#getSpeed()
     */
    public int getSpeed() {
        return getMotor().getSpeed();
    }

    /**
     * @return motor
     */
    abstract BaseRegulatedMotor getMotor();

    /**
     * @return port
     */
    public Port getPort() {
        return port;
    }

    /**
     * @return polarity
     */
    public Polarity getPolarity() {
        return polarity;
    }

    /**
     * @return motorType
     */
    public MotorType getMotorType() {
        return motorType;
    }

    /**
     * @return motorType
     */
    public Direction getDirectionStalled() {
        return directionStalled;
    }

    /**
     * @param directionStalled
     */
    public void setDirectionStalled(Direction directionStalled) {
        this.directionStalled = directionStalled;
    }

    /**
     * provides information about the status of the motor
     * @return true, if the motor has to be stopped
     */
    abstract boolean is2BeStopped();

    /**
     * @link BaseRegulatedMotor#isStalled()
     */
    public boolean isStalled() {
        return getMotor().isStalled();
    }

    /**
     * create a new motor instance
     * @param port motor connected to
     * @return EV3MediumRegulatedMotor
     */
    abstract BaseRegulatedMotor createMotor(Port port);

    /**
     * calls {@link BaseRegulatedMotor#backward()} or {@link BaseRegulatedMotor#forward()}
     * depending on the polarity of the current motor instance
     */
    public void forward() {
        LOG.debug("forward.polarity: {}", getPolarity());
        setDirectionStalled(Direction.NOT_SET);
        switch (getPolarity()) {
            case NORMAL:
                LOG.debug("getMotor().forward()");
                getMotor().forward();
                break;
            case INVERSED:
                LOG.debug("getMotor().backward()");
                getMotor().backward();
                break;
        }
    }

    /**
     * calls @link BaseRegulatedMotor#backward() or @link BaseRegulatedMotor#forward()
     * depending on the polarity of the current motor instance
     */
    public void backward() {
        LOG.debug("backward.polarity: {}", getPolarity());
        switch (getPolarity()) {
            case NORMAL:
                LOG.debug("getMotor().backward()");
                getMotor().backward();
                break;
            case INVERSED:
                LOG.debug("getMotor().forward()");
                getMotor().forward();
                break;
        }
    }

    /**
     * rotate in direction until the call of is2BeStopped provides true
     * @param direction
     */
    public void rotateTillStopped(Direction direction) {
        LOG.debug("rotateTillStopped()");
        if (getDirectionStalled().equals(direction)) {
            LOG.debug("Tried to rotate in stalled direction");
            return;
        }
        switch (direction) {
            case FORWARD: forward();
            break;
            case BACKWARD: backward();
        }
        while(!is2BeStopped()) {
        }
        setDirectionStalled(direction);
        stop();
    }

    /**
     * @link BaseRegulatedMotor#stop()
     */
    public void stop() {
        LOG.debug("{}.stop()", getMotorType());
        getMotor().stop();
    }

    /**
     * @link BaseRegulatedMotor#brake()
     */
    public void brake() {
        LOG.debug("{}.brake()", getMotorType());
        getMotor().brake();
    }

    /**
     * @link BaseRegulatedMotor#rotateTo()
     */
    public void rotateTo(int angle) {
        LOG.debug("rotate({})", angle);
        getMotor().rotateTo(angle);
    }

    /**
     * @link BaseRegulatedMotor#getTachoCount()
     */
    public int getTachoCount() {
        return getMotor().getTachoCount();
    }

    /**
     * @link BaseRegulatedMotor#resetTachoCount()
     */
    public void resetTachoCount() {
        LOG.debug("resetTachoCount()");
        getMotor().resetTachoCount();
    }

    /**
     * initialize status of motor, i.e., find home position
     */
    public abstract void init();
}
