package de.munro.ev3.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Hello extends Remote
{
    String sayHello() throws RemoteException;
}