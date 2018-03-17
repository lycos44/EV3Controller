package de.munro.ev3.rmi;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import de.munro.ev3.rmi.RemoteEV3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EV3devRMIServer extends UnicastRemoteObject implements RemoteEV3 {
    private static final Logger LOG = LoggerFactory.getLogger(EV3devRMIServer.class);
    private static final String LOCAL_HOST = "localhost";

    private String host = LOCAL_HOST;

    public EV3devRMIServer(String[] args) throws RemoteException {
        if (args.length == 1) {
            setHost(args[0]);
        }
    }

    public static void main(String args[]) {
        LOG.info("Started {}", (Object[])args);

        try {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        } catch (RemoteException e) {
            LOG.error(e.getMessage(), e);
        }
        try {
            EV3devRMIServer ev3devRMIServer = new EV3devRMIServer(args);
            String servicename = "//" + ev3devRMIServer.getHost() + "/RemoteEV3";
            Naming.rebind(servicename, ev3devRMIServer);
            LOG.info("Server ready: " + servicename);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public void beep() throws RemoteException {
        LOG.info("beep()");
    }

    @Override
    public void forward() throws RemoteException {
        LOG.info("forward()");
    }

    @Override
    public void backward() throws RemoteException {
        LOG.info("backward()");
    }

    @Override
    public void stop() throws RemoteException {
        LOG.info("stop()");
    }

    @Override
    public void shutdown() throws RemoteException {
        LOG.info("shutdown()");
    }
}
