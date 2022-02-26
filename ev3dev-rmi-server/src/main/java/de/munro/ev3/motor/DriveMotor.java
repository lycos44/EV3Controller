package de.munro.ev3.motor;

import de.munro.ev3.data.MotorData;
import de.munro.ev3.rmi.EV3devConstants;
import de.munro.ev3.rmi.RemoteEV3;
import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import lombok.extern.slf4j.Slf4j;

import javax.naming.InvalidNameException;

@Slf4j
public class DriveMotor extends Motor {

    private BaseRegulatedMotor motor;

    /**
     * Constructor
     *
     * @param motorData model
     */
    public DriveMotor(MotorData motorData) {
        super(Polarity.normal, RemoteEV3.MotorType.drive, motorData);
        int attempts = 0;
        do {
            this.motor = createMotor();
        } while (null == this.motor && attempts++<3);
        if (null == this.motor) {
            log.error("Initialisation of {} failed", this.getClass().getSimpleName());
            System.exit(EV3devConstants.SYSTEM_UNEXPECTED_ERROR);
        }
        this.motor.setSpeed(getMotorData().getSpeed());
    }

    @Override
    protected void rotate(RemoteEV3.Command cmd) {
        log.debug("rotate: {}, {}", cmd, getMotorData().getPosition(cmd));
        switch (cmd) {
            case forward:
                this.forward();
                break;
            case backward:
                this.backward();
                break;
            case stop:
                this.stop();
        }
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
        log.debug(getMotorData().toString());
        getMotorData().setCommand(RemoteEV3.Command.stop);
    }

    /**
     * @link Motor#stop()
     */
    @Override
    public void stop() {
        super.stop();
        getMotor().setSpeed(EV3devConstants.DRIVE_MOTOR_SPEED_NORMAL);
    }
}
