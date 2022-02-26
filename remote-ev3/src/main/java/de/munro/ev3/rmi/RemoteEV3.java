package de.munro.ev3.rmi;

import javax.naming.InvalidNameException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteEV3 extends Remote
{
    String SERVICE_NAME = "RemoteEV3";

    enum MotorType {
        liftBack,
        liftFront,
        drive,
        steering;

        public static MotorType getEnum(String value) {
            for(MotorType v : values()) {
                if (v.toString().equalsIgnoreCase(value)) return v;
            }
            throw new IllegalArgumentException();
        }
    }

    enum Command {
        forward(new MotorType[]{MotorType.drive}),
        backward(new MotorType[]{MotorType.drive}),
        stop(new MotorType[]{MotorType.drive}),
        left(new MotorType[]{MotorType.steering}),
        right(new MotorType[]{MotorType.steering}),
        home(new MotorType[]{MotorType.steering}),
        up(new MotorType[]{MotorType.liftBack, MotorType.liftFront}),
        down(new MotorType[]{MotorType.liftBack, MotorType.liftFront});

        private MotorType[] motorTypes;

        Command(MotorType[] motorTypes) {
            this.motorTypes = motorTypes;
        }

        public MotorType[] getMotorTypes() {
            return motorTypes;
        }

        public boolean containsMotorType(MotorType motorType) {
            for (MotorType mt : getMotorTypes()) {
                if (mt == motorType) return true;
            }
            return false;
        }

        public static Command getEnum(String value, MotorType motorType) {
            for(Command v : values()) {
                if (v.toString().equalsIgnoreCase(value) && v.containsMotorType(motorType)) return v;
            }
            throw new IllegalArgumentException();
        }
    }

    enum Instruction {
        beep,
        perform,
        set,
        read,
        write,
        show,
        status,
        quit,
        shutdown
    }

    boolean isInitialized() throws RemoteException;

    void beep() throws RemoteException;
    void shutdown() throws RemoteException, InvalidNameException;

    void perform(MotorType motorType, Command command) throws RemoteException, InvalidNameException;

    void set(MotorType motor, Command command, Integer value) throws RemoteException, InvalidNameException;
    void read(MotorType motor) throws RemoteException, InvalidNameException;
    void write(MotorType motor) throws RemoteException, InvalidNameException;
    void show(MotorType motor) throws RemoteException, InvalidNameException;
    SensorsDataOLD status() throws RemoteException, InvalidNameException;
}