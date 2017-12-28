package de.munro.ev3.rmi;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class HelloImpl extends UnicastRemoteObject implements Hello
{
    private static final String LOCAL_HOST = "localhost";
    private String host;

    public HelloImpl(String[] args) throws RemoteException {
        if (args.length == 1) {
            setHost(args[0]);
        } else {
            setHost(LOCAL_HOST);
        }
    }

    public String sayHello() { return "Hello world!"; }

    public static void main(String args[])
    {
        try {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            HelloImpl hello = new HelloImpl(args);
            Naming.rebind("//"+hello.getHost()+"/HelloServer", hello);
            System.err.println("Server ready");
        }
        catch (Exception e)
        {
            System.out.println("HelloImpl err: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}