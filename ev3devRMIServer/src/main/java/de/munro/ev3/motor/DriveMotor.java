package de.munro.ev3.motor;

import de.munro.ev3.rmi.EV3devConstants;
import de.munro.ev3.threadpool.Task;
import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import lejos.utility.Delay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DriveMotor extends Motor {
    private static final Logger LOG = LoggerFactory.getLogger(DriveMotor.class);
    private static final int MOTOR_SPEED = 200;

    private BaseRegulatedMotor motor;

    public DriveMotor() {
        super(EV3devConstants.DRIVE_MOTOR_PORT, Polarity.INVERSED, Task.MotorType.drive);
        this.motor = new EV3LargeRegulatedMotor(EV3devConstants.DRIVE_MOTOR_PORT);
        this.motor.setSpeed(MOTOR_SPEED);
    }

    @Override
    public int getSpeed() {
        return getMotor().getSpeed();
    }

    @Override
    BaseRegulatedMotor getMotor() {
        return motor;
    }

    @Override
    public void init() {

        // go backward till backwardSensor isPressed
        getMotor().backward();
        Delay.msDelay(1000);
        // go forward 1cm
        getMotor().forward();
        Delay.msDelay(1000);
        // go backward and do climb

        // go backward 2cm

        // put climb in home position

        // go backward 25cm faster

        // turn back 180 degrees
        getMotor().stop();
    }

    @Override
    public void run() {
        LOG.info(Thread.currentThread().getName()+" started");
        while ( !Thread.interrupted() ) {
            Delay.msDelay(1000);
        }
        this.stop();
        LOG.info(Thread.currentThread().getName()+" stopped");
    }
}
