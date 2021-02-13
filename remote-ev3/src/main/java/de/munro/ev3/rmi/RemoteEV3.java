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
    void left() throws RemoteException;
    void right() throws RemoteException;
    void straight() throws RemoteException;

    void shutdown() throws RemoteException;

    void frontUp() throws RemoteException;
    void frontDown() throws RemoteException;
    void backUp() throws RemoteException;
    void backDown() throws RemoteException;

    void reset() throws RemoteException;

    void test() throws RemoteException;
}