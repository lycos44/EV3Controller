package de.munro.ev3.data;

import de.munro.ev3.rmi.EV3devConstants;
import de.munro.ev3.rmi.RemoteEV3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EV3devData {
   private final SensorData sensorData = new SensorData();

    private Map<RemoteEV3.MotorType, MotorData> motorDates = new ConcurrentHashMap<>();

    public EV3devData() {
        motorDates.put(RemoteEV3.MotorType.liftBack, new MotorData(
                new RemoteEV3.Command[] {RemoteEV3.Command.up, RemoteEV3.Command.down},
                EV3devConstants.LIFT_BACK_MOTOR_SPEED_INITIAL));
        motorDates.put(RemoteEV3.MotorType.liftFront, new MotorData(
                new RemoteEV3.Command[] {RemoteEV3.Command.up, RemoteEV3.Command.down},
                EV3devConstants.LIFT_FRONT_MOTOR_SPEED_INITIAL));
        motorDates.put(RemoteEV3.MotorType.drive, new MotorData(
                new RemoteEV3.Command[] {},
                EV3devConstants.DRIVE_MOTOR_SPEED_NORMAL));
        motorDates.put(RemoteEV3.MotorType.steering, new MotorData(
                new RemoteEV3.Command[] {RemoteEV3.Command.left, RemoteEV3.Command.home, RemoteEV3.Command.right},
                EV3devConstants.STEERING_MOTOR_SPEED));
    }

    public MotorData getMotorData(RemoteEV3.MotorType motorType) {
        return motorDates.get(motorType);
    }

    public SensorData getSensorData() {
        return sensorData;
    }

    @Override
    public String toString() {
        return "{" + "\n" +
                "\tliftBackMotorData:  " + motorDates.get(RemoteEV3.MotorType.liftBack) + "\n" +
                "\tliftFrontMotorData: " + motorDates.get(RemoteEV3.MotorType.liftFront) + "\n" +
                "\tdriveMotorData:     " + motorDates.get(RemoteEV3.MotorType.drive) + "\n" +
                "\tsteeringMotorData:  " + motorDates.get(RemoteEV3.MotorType.steering) + "\n" +
                "\tsensorData:         " + sensorData + "\n" +
                "}";
    }
}
