package de.munro.ev3.motor;

import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Motor {
    private static final Logger LOG = LoggerFactory.getLogger(Motor.class);

    public enum Rotation {
        ahead,
        reverse,
        stalled
    }
    public enum Polarity {
        NORMAL,
        INVERSED
    }
    public enum MotorType {
        drive(MotorPort.A),
        climbBack(MotorPort.B),
        steering(MotorPort.C),
        climbFront(MotorPort.D);

        private final Port port;

        MotorType(Port port) {
            this.port = port;
        }

        public Port getPort() {
            return port;
        }
    }

    private final Polarity polarity;
    private final MotorType motorType;
    private Rotation rotationStalled = Rotation.stalled;
    private int relativeSpeed;

    /**
     * Constructor
     */
    public Motor(Polarity polarity, MotorType motorType) {
        this.polarity = polarity;
        this.motorType = motorType;
    }

    /**
     * get relativeSpeed value
     * @return relativeSpeed
     */
    public int getRelativeSpeed() {
        return relativeSpeed;
    }

    /**
     * set value fo relativeSpeed
     * @param relativeSpeed
     */
    public void setRelativeSpeed(int relativeSpeed) {
        this.relativeSpeed = relativeSpeed;
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
     * @link BaseRegulatedMotor#setSpeed()
     */
    public void setSpeed(int speed) {
        getMotor().setSpeed(speed);
    }

    /**
     * @return motor
     */
    abstract BaseRegulatedMotor getMotor();

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
    public Rotation getRotationStalled() {
        return rotationStalled;
    }

    /**
     * @param rotationStalled
     */
    public void setRotationStalled(Rotation rotationStalled) {
        this.rotationStalled = rotationStalled;
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
     * @return EV3MediumRegulatedMotor
     */
    abstract BaseRegulatedMotor createMotor();

    /**
     * calls {@link BaseRegulatedMotor#backward()} or {@link BaseRegulatedMotor#forward()}
     * depending on the polarity of the current motor instance
     */
    public void forward() {
        LOG.debug("forward.polarity: {}", getPolarity());
        setRotationStalled(Rotation.stalled);
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
     * rotate until the call of is2BeStopped provides true
     * @param rotation
     */
    public void rotateTillStopped(Rotation rotation) {
        LOG.debug("rotateTillStopped()");
        if (getRotationStalled().equals(rotation)) {
            LOG.debug("Tried to rotate in stalled direction");
            return;
        }
        switch (rotation) {
            case ahead: forward();
            break;
            case reverse: backward();
        }
        while(!is2BeStopped()) {
        }
        setRotationStalled(rotation);
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
