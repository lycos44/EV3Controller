package de.munro.ev3.data;

import de.munro.ev3.rmi.EV3devConstants;
import de.munro.ev3.rmi.RemoteEV3;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class EV3devData {

    private final SensorsData sensorsData = new SensorsData();

    private final Map<RemoteEV3.MotorType, MotorData> motorsData = new ConcurrentHashMap<>();

    public EV3devData() {
        motorsData.put(RemoteEV3.MotorType.liftBack, new MotorData(
                new RemoteEV3.Command[]{RemoteEV3.Command.up, RemoteEV3.Command.down},
                EV3devConstants.LIFT_BACK_MOTOR_SPEED_INITIAL));
        motorsData.put(RemoteEV3.MotorType.liftFront, new MotorData(
                new RemoteEV3.Command[]{RemoteEV3.Command.up, RemoteEV3.Command.down},
                EV3devConstants.LIFT_FRONT_MOTOR_SPEED_INITIAL));
        motorsData.put(RemoteEV3.MotorType.drive, new MotorData(
                new RemoteEV3.Command[]{},
                EV3devConstants.DRIVE_MOTOR_SPEED_NORMAL));
        motorsData.put(RemoteEV3.MotorType.steering, new MotorData(
                new RemoteEV3.Command[]{RemoteEV3.Command.left, RemoteEV3.Command.home, RemoteEV3.Command.right},
                EV3devConstants.STEERING_MOTOR_SPEED));
    }

    public MotorData getMotorData(RemoteEV3.MotorType motorType) {
        return motorsData.get(motorType);
    }

    public SensorsData getSensorsData() {
        return sensorsData;
    }

    @Override
    public String toString() {
        return "{" + "\n" +
                "\tliftBackMotorData:  " + motorsData.get(RemoteEV3.MotorType.liftBack) + "\n" +
                "\tliftFrontMotorData: " + motorsData.get(RemoteEV3.MotorType.liftFront) + "\n" +
                "\tdriveMotorData:     " + motorsData.get(RemoteEV3.MotorType.drive) + "\n" +
                "\tsteeringMotorData:  " + motorsData.get(RemoteEV3.MotorType.steering) + "\n" +
                "\tsensorsData:        " + sensorsData + "\n" +
                "}";
    }

    public void perform(RemoteEV3.MotorType motorType, RemoteEV3.Command command) {
        if (!command.containsMotorType(motorType)) {
            throw new IllegalArgumentException("MotorType: "+motorType+" does not support "+command+" operations");
        }
        synchronized (this.getMotorData(motorType)) {
            log.debug("perform {}, {}", motorType, command);
            this.getSensorsData().setReset(true);
            this.getMotorData(motorType).setInstruction(RemoteEV3.Instruction.perform);
            this.getMotorData(motorType).setCommand(command);
            this.getMotorData(motorType).notify();
        }
    }

    public void set(RemoteEV3.MotorType motorType, RemoteEV3.Command command, Integer value) {
        synchronized (this.getMotorData(motorType)) {
            this.getMotorData(motorType).setPosition(command, value);
        }
    }

    public void read(RemoteEV3.MotorType motorType) {
        synchronized (this.getMotorData(motorType)) {
            this.getMotorData(motorType).setInstruction(RemoteEV3.Instruction.read);
            this.getMotorData(motorType).notify();
        }
    }

    public void write(RemoteEV3.MotorType motorType) {
        synchronized (this.getMotorData(motorType)) {
            this.getMotorData(motorType).setInstruction(RemoteEV3.Instruction.write);
            this.getMotorData(motorType).notify();
        }
    }
}
