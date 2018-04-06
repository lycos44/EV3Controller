package de.munro.ev3.motor;

import de.munro.ev3.rmi.EV3devConstants;
import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DriveMotor extends Motor {
    private static final Logger LOG = LoggerFactory.getLogger(DriveMotor.class);

    private static DriveMotor instance;
    private EV3LargeRegulatedMotor motor;

    public static DriveMotor getInstance() {
        if (null == instance) {
            instance = new DriveMotor();
        }
        return instance;
    }

    private DriveMotor() {
        this.motor = createMotor(EV3devConstants.DRIVE_MOTOR_PORT, Polarity.NORMAL);
        if (null == this.motor) {
            this.motor = createMotor(EV3devConstants.DRIVE_MOTOR_PORT, Polarity.NORMAL);
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
