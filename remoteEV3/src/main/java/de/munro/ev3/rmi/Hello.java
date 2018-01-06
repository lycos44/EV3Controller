package de.munro.ev3.rmi;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Hello extends Remote
{
    String SERVICE_NAME = "HelloServer";
    String sayHello() throws RemoteException;

    void shutdown()  throws RemoteException;
}