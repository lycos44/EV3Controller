package de.munro.ev3.rmi;

import java.rmi.RemoteException;

/**
 * Dummy RemoteEV3 class for testing command line input
 */
public class RemoteEV3Dummy implements RemoteEV3 {
    /**
     * @link RemoveEV3#isInitialized
     */
    @Override
    public boolean isInitialized() throws RemoteException {
        return false;
    }

    /**
     * @link RemoveEV3#beep
     */
    @Override
    public void beep() throws RemoteException {

    }

    /**
     * @link RemoveEV3#shutdown
     */
    @Override
    public void shutdown() throws RemoteException {

    }

    /**
     * @link RemoveEV3#perform
     */
    @Override
    public void perform(MotorType motorType, Command command) {

    }

    /**
     * @link RemoveEV3#set
     */
    @Override
    public void set(MotorType motor, Command command, Integer value) throws RemoteException {

    }

    /**
     * @link RemoveEV3#read
     */
    @Override
    public void read(MotorType motor) throws RemoteException {

    }

    /**
     * @link RemoveEV3#write
     */
    @Override
    public void write(MotorType motor) throws RemoteException {

    }

    /**
     * @link RemoveEV3#show
     */
    @Override
    public void show(MotorType motor) throws RemoteException {

    }
}
