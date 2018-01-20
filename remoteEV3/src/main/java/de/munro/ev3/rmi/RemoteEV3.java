package de.munro.ev3.rmi;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteEV3 extends Remote
{
    String SERVICE_NAME = "RemoteEV3";

    void beep() throws RemoteException;
    void forward() throws RemoteException;
    void backward() throws RemoteException;
    void stop() throws RemoteException;
    void shutdown() throws RemoteException;
}