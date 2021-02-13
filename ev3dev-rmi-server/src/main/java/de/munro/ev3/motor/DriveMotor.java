package de.munro.ev3.motor;

import de.munro.ev3.data.DriveMotorData;
import de.munro.ev3.rmi.EV3devConstants;
import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DriveMotor extends Motor {

    public static final int MOTOR_SPEED_NORMAL = 300;

    private BaseRegulatedMotor motor;
    private final DriveMotorData driveMotorData;
    private EV3devConstants.Direction direction;

    /**
     * Constructor
     *
     * @param driveMotorData data
     */
    public DriveMotor(DriveMotorData driveMotorData) {
        super(Polarity.normal, MotorType.drive);
        this.driveMotorData = driveMotorData;
        this.direction = driveMotorData.getDirection();
        int attempts = 0;
        do {
            this.motor = createMotor();
        } while (null == this.motor && attempts++<2);
        if (null == this.motor) {
            log.error("Initialisation of {} failed", this.getClass().getSimpleName());
            System.exit(EV3devConstants.SYSTEM_UNEXPECTED_ERROR);
        }
        this.motor.setSpeed(MOTOR_SPEED_NORMAL);
        this.driveMotorData.setRunning(true);
    }

    /**
     * @link Motor#createMotor()
     */
    @Override
    EV3LargeRegulatedMotor createMotor() {
        try {
            return new EV3LargeRegulatedMotor(this.getMotorType().getPort());
        } catch (RuntimeException e) {
            log.error("Catch", e);
        }
        return null;
    }

    @Override
    public void workOutMotorData() {
        if (direction == driveMotorData.getDirection()) {
            return;
        }
        switch (driveMotorData.getDirection()) {
            case forward:
                this.forward();
                break;
            case backward:
                this.backward();
                break;
            case stop:
                this.stop();
        }
        direction = driveMotorData.getDirection();
        driveMotorData.setDone(true);
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
        log.debug("init()");
        if (readPropertyFile()) {
            return;
        }
        writePropertyFile();
    }

    /**
     * @link Motor#verifyProperties()
     */
    @Override
    public boolean verifyProperties() {
        return getProperties().isEmpty();
    }

    /**
     * @link Motor#logStatus()
     */
    @Override
    public void logStatus() {
        log.debug("(dir, speed, count):({}, {},{})", driveMotorData.getDirection(), getSpeed(), getTachoCount());
    }

    /**
     * @link Motor#stop()
     */
    @Override
    public void stop() {
        super.stop();
        getMotor().setSpeed(MOTOR_SPEED_NORMAL);
    }

    /**
     * @return current driveMotorData
     */
    public DriveMotorData getMotorData() {
        return driveMotorData;
    }
}
