package de.munro.ev3.motor;

import de.munro.ev3.rmi.EV3devConstants;
import de.munro.ev3.threadpool.Task;
import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DriveMotor extends Motor {
    private static final Logger LOG = LoggerFactory.getLogger(DriveMotor.class);
    private static final int MOTOR_SPEED = 200;

    private BaseRegulatedMotor motor;

    public DriveMotor() {
        super(EV3devConstants.DRIVE_MOTOR_PORT, Polarity.INVERSED, Task.MotorType.drive);
        this.motor = createMotor(EV3devConstants.DRIVE_MOTOR_PORT);
        this.motor.setSpeed(MOTOR_SPEED);
    }

    private EV3LargeRegulatedMotor createMotor(Port port) {
        try {
            return new EV3LargeRegulatedMotor(port);
        } catch (RuntimeException e) {
            LOG.error("Catch", e);
        }
        return null;
    }

    @Override
    BaseRegulatedMotor getMotor() {
        return motor;
    }

    @Override
    public void init() {
    }
}
