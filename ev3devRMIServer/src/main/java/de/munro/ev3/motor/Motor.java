package de.munro.ev3.motor;

import de.munro.ev3.rmi.EV3Device;
import de.munro.ev3.threadpool.Task;
import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.utility.Delay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Motor implements EV3Device {
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
        switch (polarity) {
            case NORMAL:
                getMotor().forward();
                break;
            case INVERSED:
                getMotor().backward();
                break;
        }
    }

    public void backward() {
        switch (polarity) {
            case NORMAL:
                getMotor().backward();
                break;
            case INVERSED:
                getMotor().forward();
                break;
        }
    }

    public void forwardTillStalled() {
        LOG.debug("forwardTillStalled()");
        while (!getMotor().isStalled()) {
            forward();
            Delay.msDelay(1000);
        }
        getMotor().stop();
        LOG.debug("stalled position: {}", getMotor().getTachoCount());
    }

    public void backwardTillStalled() {
        LOG.debug("backwardTillStalled()");
        while (!getMotor().isStalled()) {
            backward();
            Delay.msDelay(1000);
        }
        getMotor().stop();
        LOG.debug("stalled position: {}", getMotor().getTachoCount());
    }

    public void stop() {
        getMotor().stop();
    }

    public abstract void init();

    void proceedTask(Task.ActionType actionType) {
        switch (actionType) {
            case init:
                init();
        }
    }
}
