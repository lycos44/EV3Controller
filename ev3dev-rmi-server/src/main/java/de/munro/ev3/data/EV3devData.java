package de.munro.ev3.data;

public class EV3devData {
    private boolean stopped = false;
    private ClimbBackMotorData climbBackMotorData = new ClimbBackMotorData();
    private ClimbFrontMotorData climbFrontMotorData = new ClimbFrontMotorData();
    private DriveMotorData driveMotorData = new DriveMotorData();
    private SteeringMotorData steeringMotorData = new SteeringMotorData();
    private SensorData sensorData = new SensorData();

    public ClimbBackMotorData getClimbBackMotorData() {
        return climbBackMotorData;
    }

    public ClimbFrontMotorData getClimbFrontMotorData() {
        return climbFrontMotorData;
    }

    public DriveMotorData getDriveMotorData() {
        return driveMotorData;
    }

    public SteeringMotorData getSteeringMotorData() {
        return steeringMotorData;
    }

    public SensorData getSensorData() {
        return sensorData;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }
}
