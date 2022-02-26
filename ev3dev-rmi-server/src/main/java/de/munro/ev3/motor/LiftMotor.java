package de.munro.ev3.motor;

import de.munro.ev3.data.MotorData;
import de.munro.ev3.rmi.EV3devConstants;
import de.munro.ev3.rmi.RemoteEV3;
import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3MediumRegulatedMotor;
import lombok.extern.slf4j.Slf4j;

import javax.naming.InvalidNameException;
import java.util.Properties;

@Slf4j
public class LiftMotor extends Motor {

    private BaseRegulatedMotor motor;
    private int adjustUp;

    /**
     * Constructor
     * @param polarity Polarity
     * @param motorType type of the motor
     * @param motorData model
     */
    public LiftMotor(Polarity polarity, RemoteEV3.MotorType motorType, MotorData motorData, int adjustUp) {
        super(polarity, motorType, motorData);
        this.adjustUp = adjustUp;
        int attempts = 0;
        do {
            log.info("Create motor: {}", attempts);
            this.motor = createMotor();
        } while (null == this.motor && attempts++<3);
        if (null == this.motor) {
            log.error("Initialisation of {} failed", this.getClass().getSimpleName());
            System.exit(EV3devConstants.SYSTEM_UNEXPECTED_ERROR);
        }
        this.motor.setSpeed(getMotorData().getSpeed());
    }

    /**
     * @link Motor#getMotor()
     */
    @Override
    BaseRegulatedMotor getMotor() {
        return motor;
    }

    /**
     * @link Motor#init()
     */
    @Override
    public void init() {
        log.debug("init()");
        rotateTillStopped(Rotation.reverse);//up
        resetTachoCount();
        int up = getTachoCount();
        log.debug("up: {}", getTachoCount());
        rotateTillStopped(Rotation.ahead);//down
        int down = getTachoCount();
        log.debug("down: {}", down);

        getMotorData().setPosition(RemoteEV3.Command.up, up+adjustUp);
        getMotorData().setPosition(RemoteEV3.Command.down, down);

        rotate(RemoteEV3.Command.up);
        getMotorData().setCommand(RemoteEV3.Command.up);
        log.debug(getMotorData().toString());
    }
}
