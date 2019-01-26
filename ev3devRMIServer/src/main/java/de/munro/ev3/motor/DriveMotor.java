package de.munro.ev3.motor;

import de.munro.ev3.rmi.EV3devConstants;
import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DriveMotor extends Motor {
    private static final Logger LOG = LoggerFactory.getLogger(DriveMotor.class);
    private static final int MOTOR_SPEED = 200;

    private BaseRegulatedMotor motor;

    /**
     * Constructor
     */
    public DriveMotor() {
        super(EV3devConstants.DRIVE_MOTOR_PORT, Polarity.INVERSED, MotorType.drive);
        this.motor = createMotor(EV3devConstants.DRIVE_MOTOR_PORT);
        this.motor.setSpeed(MOTOR_SPEED);
    }

    /**
     * @link Motor#createMotor()
     */
    @Override
    EV3LargeRegulatedMotor createMotor(Port port) {
        try {
            return new EV3LargeRegulatedMotor(port);
        } catch (RuntimeException e) {
            LOG.error("Catch", e);
        }
        return null;
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
        return false;
    }

    /**
     * @link Motor#init()
     */
    @Override
    public void init() {
    }
}
