package de.munro.ev3.motor;

import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Motor {
    private static final Logger LOG = LoggerFactory.getLogger(Motor.class);

    protected EV3LargeRegulatedMotor createMotor(Port port) {
        LOG.debug("createMotor({})", port);
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

    public void forward() {
        getMotor().forward();
    }

    public void backward() {
        getMotor().backward();
    }

    public void stop() {
        getMotor().stop();
    }
}
