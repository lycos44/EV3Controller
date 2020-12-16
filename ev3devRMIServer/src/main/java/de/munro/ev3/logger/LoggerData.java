package de.munro.ev3.logger;

import de.munro.ev3.rmi.EV3devConstants;

public class LoggerData implements ClimbBackMotorLogger, ClimbFrontMotorLogger, DriveMotorLogger, SteeringMotorLogger, SensorLogger {
    private int ping;
    private int climbBackMotorPing;
    private int climbFrontMotorPing;
    private int driveMotorPing;
    private int sensorPing;
    private int steeringMotorPing;
    private volatile boolean running = false;
    // drive motor
    private EV3devConstants.Direction driveDirection = EV3devConstants.Direction.stop;
    // steering motor
    private EV3devConstants.Turn steeringTurn = EV3devConstants.Turn.straight;
    // climb front
    private EV3devConstants.Climb climbFront = EV3devConstants.Climb.up;
    // climb back
    private EV3devConstants.Climb climbBack = EV3devConstants.Climb.up;

    /**
     * get ping value
     * @return current ping value
     */
    public int getPing() {
        return ping;
    }

    /**
     * set ping value
     */
    public void setPing(int ping) {
        this.ping = ping;
    }

    /**
     * get climbBackMotorPing value
     * @return current climbBackMotorPing value
     */
    public int getClimbBackMotorPing() {
        return climbBackMotorPing;
    }

    /**
     * set climbBackMotorPing value
     */
    public void setClimbBackMotorPing(int climbBackMotorPing) {
        this.climbBackMotorPing = climbBackMotorPing;
    }

    /**
     * get climbBack value
     * @return current climbBack value
     */
    public EV3devConstants.Climb getClimbBack() {
        return climbBack;
    }

    /**
     * set climbBack value
     */
    public void setClimbBack(EV3devConstants.Climb climbBack) {
        this.climbBack = climbBack;
    }

    /**
     * get climbFrontMotorPing value
     * @return current climbFrontMotorPing value
     */
    public int getClimbFrontMotorPing() {
        return climbFrontMotorPing;
    }

    /**
     * set climbFrontMotorPing value
     */
    public void setClimbFrontMotorPing(int climbFrontMotorPing) {
        this.climbFrontMotorPing = climbFrontMotorPing;
    }

    /**
     * get climbFront value
     * @return current climbFront value
     */
    public EV3devConstants.Climb getClimbFront() {
        return climbFront;
    }

    /**
     * set climbFront value
     */
    public void setClimbFront(EV3devConstants.Climb climbFront) {
        this.climbFront = climbFront;
    }

    /**
     * get driveMotorPing value
     * @return current driveMotorPing value
     */
    public int getDriveMotorPing() {
        return driveMotorPing;
    }

    /**
     * set driveMotorPing value
     */
    public void setDriveMotorPing(int driveMotorPing) {
        this.driveMotorPing = driveMotorPing;
    }

    /**
     * get driveDirection value
     * @return current driveDirection value
     */
    public EV3devConstants.Direction getDriveDirection() {
        return driveDirection;
    }

    /**
     * set driveDirection value
     */
    public void setDriveDirection(EV3devConstants.Direction driveDirection) {
        this.driveDirection = driveDirection;
    }

    /**
     * get steeringMotorPing value
     * @return current steeringMotorPing value
     */
    public int getSteeringMotorPing() {
        return steeringMotorPing;
    }

    /**
     * set steeringMotorPing value
     */
    public void setSteeringMotorPing(int steeringMotorPing) {
        this.steeringMotorPing = steeringMotorPing;
    }

    /**
     * get steeringTurn value
     * @return current steeringTurn value
     */
    public EV3devConstants.Turn getSteeringTurn() {
        return steeringTurn;
    }

    /**
     * set steeringTurn value
     */
    public void setSteeringTurn(EV3devConstants.Turn steeringTurn) {
        this.steeringTurn = steeringTurn;
    }

    /**
     * get sensorPing value
     * @return current sensorPing value
     */
    public int getSensorPing() {
        return sensorPing;
    }

    /**
     * set sensorPing value
     */
    public void setSensorPing(int sensorPing) {
        this.sensorPing = sensorPing;
    }

    /**
     * get running value
     * @return current running value
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * set running value
     */
    public void setRunning(boolean running) {
        this.running = running;
    }
}
