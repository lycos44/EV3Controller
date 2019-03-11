package de.munro.ev3.rmi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class EV3devRMIServer extends UnicastRemoteObject implements RemoteEV3 {
    private static final Logger LOG = LoggerFactory.getLogger(EV3devRMIServer.class);
    private static final String LOCAL_HOST = "localhost";

    // Configuration
    private String host = LOCAL_HOST;

    public EV3devRMIServer(String[] args) throws RemoteException {
        super();
        if (null != args && args.length >= 1 && !args[0].isEmpty()){
            this.host = args[0];
        }
    }

    public static void main(String[] args) {
        LOG.info("Started {}", (Object[]) args);

        try {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        } catch (RemoteException e) {
            LOG.error("Remote object registering failed", e);
            System.exit(EV3devConstants.SYSTEM_UNEXPECTED_ERROR);
        }

        EV3devRMIServer ev3devRMIServer = null;
        try {
            ev3devRMIServer = new EV3devRMIServer(args);
        } catch (RemoteException e) {
            LOG.error("Initialization failed", e);
            System.exit(EV3devConstants.SYSTEM_UNEXPECTED_ERROR);
        }

        //RMI
        String service = String.format("//%s/%s", ev3devRMIServer.getHost(), RemoteEV3.SERVICE_NAME);
        try {
            Naming.rebind(service, ev3devRMIServer);
        } catch (RemoteException | MalformedURLException e) {
            LOG.error("RMI binding failed", e);
            System.exit(EV3devConstants.SYSTEM_UNEXPECTED_ERROR);
        }
        LOG.info("started successfully");
        DeviceRunner deviceRunner = new DeviceRunner();
        //To Stop the motor in case of pkill java for example
        Runtime.getRuntime().addShutdownHook(new Thread(() -> deviceRunner.stop()));

        deviceRunner.run();
        try {
            Naming.unbind(service);
        } catch (RemoteException |NotBoundException | MalformedURLException e) {
            LOG.error("RMI unbinding failed", e);
            System.exit(EV3devConstants.SYSTEM_UNEXPECTED_ERROR);
        }
        System.exit(EV3devConstants.SYSTEM_FINISHED_SUCCESSFULLY);
        LOG.info("stopped");
    }

    private String getHost() {
        return host;
    }

    @Override
    public boolean isInitialized() throws RemoteException {
        return false;
    }

    @Override
    public void beep() throws RemoteException {
        LOG.debug("()");
    }

    @Override
    public void forward() throws RemoteException {
        LOG.debug("forward()");
    }

    @Override
    public void backward() throws RemoteException {
        LOG.debug("backward()");
    }

    @Override
    public void stop() throws RemoteException {
        LOG.debug("stop()");
    }

    @Override
    public void climb() throws RemoteException {
        LOG.debug("climbBack()");
    }

    @Override
    public void shutdown() throws RemoteException {
        LOG.debug("shutdown()");
        System.exit(0);
    }
}
