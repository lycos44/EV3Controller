package de.munro.ev3.motor;

import de.munro.ev3.rmi.EV3devConstants;
import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DriveMotor extends Motor {
    private static final Logger LOG = LoggerFactory.getLogger(DriveMotor.class);
    private static final int MOTOR_SPEED = 200;

    private EV3LargeRegulatedMotor motor;
    private final Port port = EV3devConstants.DRIVE_MOTOR_PORT;
    private final Polarity polarity = Polarity.INVERSED;

    public DriveMotor() {
        this.motor = createMotor(port, polarity);
        if (null == this.motor) {
            this.motor = createMotor(port, polarity);
        }
    }

    @Override
    public int getSpeed() {
        return MOTOR_SPEED;
    }

    @Override
    public EV3LargeRegulatedMotor getMotor() {
        return motor;
    }

    @Override
    public void init() {
//        getMotor().setSpeed(MOTOR_SPEED);

        // go backward till backwardSensor isPressed

        // go forward 1cm

        // go backward and do climb

        // go backward 2cm

        // put climb in home position

        // go backward 25cm faster

        // turn back 180 degrees
    }

    @Override
    public void run() {

    }
}
