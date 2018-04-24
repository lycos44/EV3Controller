package de.munro.ev3.rmi;

import de.munro.ev3.threadpool.ThreadPoolManager;
import ev3dev.actuators.Sound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class EV3devRMIServer extends UnicastRemoteObject implements RemoteEV3 {
    private static final Logger LOG = LoggerFactory.getLogger(EV3devRMIServer.class);
    private static final String LOCAL_HOST = "localhost";

    private String host = LOCAL_HOST;
    private static final ThreadPoolManager threadPoolManager = new ThreadPoolManager();

    public EV3devRMIServer(String[] args) throws RemoteException {
        if (args.length == 1) {
            setHost(args[0]);
        }
    }

    public static void main(String args[]) {
        LOG.info("Started {}", (Object[]) args);

        try {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        } catch (RemoteException e) {
            LOG.error(e.getMessage(), e);
            LOG.error("Starting failed");
            return;
        }

        try {
            EV3devRMIServer ev3devRMIServer = new EV3devRMIServer(args);
            String service = String.format("//%s/%s", ev3devRMIServer.getHost(), RemoteEV3.SERVICE_NAME);
            Naming.rebind(service, ev3devRMIServer);
            LOG.info("Registered service: {}", service);
            ev3devRMIServer.initialize();
            if (ev3devRMIServer.isInitialized()) {
                LOG.info("successfully initialized");
            } else {
                LOG.error("initialization failed, restart necessary");
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        //To Stop the motor in case of pkill java for example
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Emergency stop!");
            threadPoolManager.shutdown();
            LOG.info("All motors stopped");
        }));

        LOG.info("Service successfully started");
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    private void initialize() {
    }

    @Override
    public boolean isInitialized() throws RemoteException {
        return false;
    }

    @Override
    public void beep() throws RemoteException {
        LOG.debug("beep()");
        Sound sound = Sound.getInstance();
        int volume = sound.getVolume();
        sound.setVolume(volume/2);
        sound.beep();
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
        LOG.debug("climb()");
        // drive backward until the touch sensor is pressed
        // BackwardSensor.getInstance().isPressed();
        // lower down the climb shift and go backward
        // if the climb shift has reached its final position, go further backward,
        // then stop and reposition the climb shift
    }

    @Override
    public void shutdown() throws RemoteException {
        LOG.debug("shutdown()");
    }
}
