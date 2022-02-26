package de.munro.ev3.motor;

import de.munro.ev3.data.MotorData;
import de.munro.ev3.rmi.EV3devConstants;
import de.munro.ev3.rmi.RemoteEV3;
import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SteeringMotor extends Motor {

    private BaseRegulatedMotor motor;

    /**
     * Constructor
     * @param motorData motor data
     */
    public SteeringMotor(MotorData motorData) {
        super(Polarity.normal, RemoteEV3.MotorType.steering, motorData);
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
        rotateTillStopped(Rotation.ahead);//left
        resetTachoCount();
        int left = getTachoCount();
        log.debug("left: {}", left);
        rotateTillStopped(Rotation.reverse);//right
        int right = getTachoCount();
        log.debug("right: {}", right);
        int home = left+right/2;
        log.debug("home: {}", home);

        getMotorData().setPosition(RemoteEV3.Command.left, left);
        getMotorData().setPosition(RemoteEV3.Command.right, right);
        getMotorData().setPosition(RemoteEV3.Command.home, home);

        rotate(RemoteEV3.Command.home);
        getMotorData().setCommand(RemoteEV3.Command.home);
        log.debug(getMotorData().toString());
    }
}
