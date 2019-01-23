package de.munro.ev3.motor;

import de.munro.ev3.threadpool.Task;
import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import lejos.hardware.port.Port;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Motor {
    private static final Logger LOG = LoggerFactory.getLogger(Motor.class);

    public enum Polarity {
        NORMAL,
        INVERSED
    }
    private final Port port;
    private final Polarity polarity;
    private final Task.MotorType motorType;

    public Motor(Port port, Polarity polarity, Task.MotorType motorType) {
        this.port = port;
        this.polarity = polarity;
        this.motorType = motorType;
    }

    public boolean isInitialized() {
        LOG.debug("motor {}", getMotor());
        return null != getMotor();
    }

    public int getSpeed() {
        return getMotor().getSpeed();
    }

    abstract BaseRegulatedMotor getMotor();

    public Port getPort() {
        return port;
    }

    public Polarity getPolarity() {
        return polarity;
    }

    public Task.MotorType getMotorType() {
        return motorType;
    }

    public void forward() {
        LOG.debug("forward.polarity: {}", polarity);
        switch (polarity) {
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

    public void backward() {
        LOG.debug("backward.polarity: {}", polarity);
        switch (polarity) {
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

    public void rotateToPosition(int position) {
        int currentPosition = getTachoCount();
        LOG.debug("rotateToPosition: {}, to: {}", currentPosition, position);
        if (currentPosition < position) {
            backward();
            while (!getMotor().isStalled() && getTachoCount() < position) {
                LOG.debug("moving position: {}, to: {}, left: {}", getTachoCount(), position, getTachoCount() < position);
            }
        } else {
            forward();
            while (!getMotor().isStalled() && getTachoCount() > position) {
                LOG.debug("moving position: {}, to: {}, right: {}", getTachoCount(), position, getTachoCount() > position);
            }
        }
        LOG.debug("stop position: {}, to: {}, isStalled: {}", getTachoCount(), position, getMotor().isStalled());
        stop();
    }

    public void forwardTillStalled() {
        LOG.debug("forwardTillStalled()");
        forward();
        while (!getMotor().isStalled()) {
            LOG.debug("moving position: {}, isStalled: {}", getTachoCount(), getMotor().isStalled());
        }
        stop();
        LOG.debug("stalled position: {}, isStalled: {}", getTachoCount(), getMotor().isStalled());
    }

    public void backwardTillStalled() {
        LOG.debug("backwardTillStalled()");
        backward();
        while (!getMotor().isStalled()) {
            LOG.debug("moving position: {}, isStalled: {}", getTachoCount(), getMotor().isStalled());
        }
        stop();
        LOG.debug("stalled position: {}, isStalled: {}", getTachoCount(), getMotor().isStalled());
    }

    public void stop() {
        LOG.debug("{}.stop()", getMotorType());
        getMotor().stop();
    }

    public void brake() {
        LOG.debug("{}.brake()", getMotorType());
        getMotor().brake();
    }

    public void rotateTo(int angle) {
        LOG.debug("rotate({})", angle);
        getMotor().rotateTo(angle);
    }

    public int getTachoCount() {
        return getMotor().getTachoCount();
    }

    public void resetTachoCount() {
        LOG.debug("resetTachoCount()");
        getMotor().resetTachoCount();
    }
    public abstract void init();

    void proceedTask(Task.ActionType actionType) {
        switch (actionType) {
            case init:
                init();
        }
    }
}
