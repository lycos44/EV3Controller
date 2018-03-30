package de.munro.ev3.motor;

import de.munro.ev3.rmi.EV3devConstants;
import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClimbMotor extends Motor{
    private static final Logger LOG = LoggerFactory.getLogger(ClimbMotor.class);

    private static ClimbMotor instance;
    private EV3LargeRegulatedMotor motor;

    public static ClimbMotor getInstance() {
        if (null == instance) {
            instance = new ClimbMotor();
        }
        return instance;
    }

    private ClimbMotor() {
        this.motor = createMotor(EV3devConstants.CLIMB_MOTOR_PORT);
        if (null == this.motor) {
            this.motor = createMotor(EV3devConstants.CLIMB_MOTOR_PORT);
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
}
