package de.munro.ev3.motor;

import de.munro.ev3.logger.DriveMotorLogger;
import de.munro.ev3.rmi.EV3devConstants;
import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DriveMotor extends Motor {
    private static final Logger LOG = LoggerFactory.getLogger(DriveMotor.class);
    public static final int MOTOR_SPEED_NORMAL = 300;

    private BaseRegulatedMotor motor;
    private final DriveMotorLogger driveMotorLogger;
    private EV3devConstants.Direction currentDirection;

    /**
     * Constructor
     *
     * @param driveMotorLogger data logger
     */
    public DriveMotor(DriveMotorLogger driveMotorLogger) {
        super(Polarity.normal, MotorType.drive);
        this.driveMotorLogger = driveMotorLogger;
        this.currentDirection = driveMotorLogger.getDriveDirection();
        int attempts = 0;
        do {
            this.motor = createMotor();
        } while (null == this.motor && attempts++ < 1);
        if (null == this.motor) {
            LOG.error("Initialisation of {} failed", this.getClass().getSimpleName());
            System.exit(EV3devConstants.SYSTEM_UNEXPECTED_ERROR);
        }
        this.motor.setSpeed(MOTOR_SPEED_NORMAL);
        this.getLogger().setRunning(true);
    }

    /**
     * @link Motor#createMotor()
     */
    @Override
    EV3LargeRegulatedMotor createMotor() {
        try {
            return new EV3LargeRegulatedMotor(this.getMotorType().getPort());
        } catch (RuntimeException e) {
            LOG.error("Catch", e);
        }
        return null;
    }

    @Override
    public void readLoggerData() {
        if (currentDirection == getLogger().getDriveDirection()) {
            return;
        }
        switch (getLogger().getDriveDirection()) {
            case forward:
                this.forward();
                break;
            case backward:
                this.backward();
                break;
            case stop:
                this.stop();
        }
        currentDirection = getLogger().getDriveDirection();
    }

    @Override
    public boolean isRunning() {
        return driveMotorLogger.isRunning();
    }

    public DriveMotorLogger getLogger() {
        return driveMotorLogger;
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
        LOG.debug("init()");
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
        LOG.debug("(dir, speed, count):({}, {},{})", getLogger().getDriveDirection(), getSpeed(), getTachoCount());
    }

    /**
     * @link Motor#stop()
     */
    @Override
    public void stop() {
        super.stop();
        getMotor().setSpeed(MOTOR_SPEED_NORMAL);
    }
}
