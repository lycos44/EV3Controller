package de.munro.ev3.motor;

import de.munro.ev3.data.MotorData;
import de.munro.ev3.rmi.EV3devConstants;
import de.munro.ev3.rmi.RemoteEV3;
import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import lombok.extern.slf4j.Slf4j;

import javax.naming.InvalidNameException;

@Slf4j
public class SteeringMotor extends Motor {

    private BaseRegulatedMotor motor;

    /**
     * Constructor
     * @param motorData motorData
     */
    public SteeringMotor(MotorData motorData) throws InvalidNameException {
        super(Polarity.normal, RemoteEV3.MotorType.steering, motorData);
        int attempts = 0;
        do {
            this.motor = createMotor();
        } while (null == this.motor && attempts++<2);
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
     * @link Motor#rotate()
     */
    @Override
    protected void rotate(RemoteEV3.Command cmd) throws InvalidNameException {
        log.debug("rotate: {}, {}", cmd, getMotorData().getPosition(cmd));
        rotateTo(getMotorData().getPosition(cmd));
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

        try {
            getMotorData().setPosition(RemoteEV3.Command.left, left);
            getMotorData().setPosition(RemoteEV3.Command.right, right);
            getMotorData().setPosition(RemoteEV3.Command.home, home);
//        Properties properties = readPropertyFile();
//        if (getMotorData().verify(properties)) {
//            getMotorData().setPositions(properties);
//            log.debug(getMotorData().toString());
//        }
            rotate(RemoteEV3.Command.home);
        } catch (InvalidNameException e) {
            e.printStackTrace();
        }
        log.debug(getMotorData().toString());
        setRunning(true);
    }
}
