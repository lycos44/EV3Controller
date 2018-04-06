package de.munro.ev3.motor;

import de.munro.ev3.rmi.EV3devConstants;
import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SteeringMotor extends Motor {
    private static final Logger LOG = LoggerFactory.getLogger(SteeringMotor.class);

    private static SteeringMotor instance;
    private EV3LargeRegulatedMotor motor;

    public static SteeringMotor getInstance() {
        if (null == instance) {
            instance = new SteeringMotor();
        }
        return instance;
    }

    private SteeringMotor() {
        this.motor = createMotor(EV3devConstants.STEERING_MOTOR_PORT, Polarity.NORMAL);
        if (null == this.motor) {
            this.motor = createMotor(EV3devConstants.STEERING_MOTOR_PORT, Polarity.NORMAL);
        }
    }

    public static boolean isInitialized() {
        LOG.debug("motor {}", instance);
        return null != instance && null != instance.motor;
    }

    @Override
    public EV3LargeRegulatedMotor getMotor() {
        return motor;
    }

    @Override
    public void init() {

    }
}
