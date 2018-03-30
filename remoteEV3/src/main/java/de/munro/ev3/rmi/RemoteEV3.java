package de.munro.ev3.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteEV3 extends Remote
{
    String SERVICE_NAME = "RemoteEV3";

    boolean isInitialized() throws RemoteException;
    void beep() throws RemoteException;
    void forward() throws RemoteException;
    void backward() throws RemoteException;
    void stop() throws RemoteException;
    void climb() throws RemoteException;
    void shutdown() throws RemoteException;
}