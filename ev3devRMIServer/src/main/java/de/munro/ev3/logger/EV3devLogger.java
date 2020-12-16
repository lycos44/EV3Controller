package de.munro.ev3.logger;

public class EV3devLogger {
    private final LoggerData loggerData;

    /**
     * constructor
     */
    public EV3devLogger() {
        this.loggerData = new LoggerData();
    }

    /**
     * get loggerData
     * @return loggerData
     */
    public LoggerData getLoggerData() {
        return loggerData;
    }

    /**
     * get climbBackMotor loggerData
     * @return loggerData
     */
    public ClimbBackMotorLogger getClimbBackMotorLogger() {
        return loggerData;
    }

    /**
     * get climbFrontMotor loggerData
     * @return loggerData
     */
    public ClimbFrontMotorLogger getFrontMotorLogger() {
        return loggerData;
    }

    /**
     * get driveMotor loggerData
     * @return loggerData
     */
    public DriveMotorLogger getDriveMotorLogger() {
        return loggerData;
    }

    /**
     * get steeringMotor loggerData
     * @return loggerData
     */
    public SteeringMotorLogger getSteeringMotorLogger() {
        return loggerData;
    }

    /**
     * get sensor loggerData
     * @return loggerData
     */
    public SensorLogger getSensorLogger() {
        return loggerData;
    }
}
