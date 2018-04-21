package de.munro.ev3.motor;

import de.munro.ev3.rmi.EV3Device;
import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
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
    private Polarity polarity = Polarity.NORMAL;

    @Override
    public void run() {
    }

    protected EV3LargeRegulatedMotor createMotor(Port port, Polarity polarity) {
        LOG.debug("createMotor({})", port);
        this.polarity = polarity;
        EV3LargeRegulatedMotor motor = null;
        try {
            motor = new EV3LargeRegulatedMotor(port);
            motor.brake();
            motor.setSpeed(200);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return motor;
    }

    public abstract EV3LargeRegulatedMotor getMotor();

    public Polarity getPolarity() {
        return polarity;
    }

    public void setPolarity(Polarity polarity) {
        this.polarity = polarity;
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
}
