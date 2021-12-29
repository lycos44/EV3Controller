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
        steering
    }

    enum Command {
        forward,
        backward,
        stop,
        left,
        right,
        home,
        up,
        down
    }

    enum Instruction {
        beep,
        perform,
        set,
        read,
        write,
        show,
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
}