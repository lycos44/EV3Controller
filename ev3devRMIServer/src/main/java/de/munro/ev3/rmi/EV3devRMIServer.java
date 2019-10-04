package de.munro.ev3.rmi;

import ev3dev.actuators.Sound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import static de.munro.ev3.rmi.EV3devConstants.SYSTEM_FINISHED_SUCCESSFULLY;

public class EV3devRMIServer extends UnicastRemoteObject implements RemoteEV3 {
    private static final Logger LOG = LoggerFactory.getLogger(EV3devRMIServer.class);
    private static final String LOCAL_HOST = "localhost";

    // Configuration
    private String host = LOCAL_HOST;
    private EV3devStatus ev3devStatus;

    private EV3devRMIServer(String[] args) throws RemoteException {
        super();
        if (null != args && args.length >= 1 && !args[0].isEmpty()){
            this.host = args[0];
        }
        ev3devStatus = new EV3devStatus();
    }

    public static void main(String[] args) {
        LOG.info("Started {}", (Object) args);

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
        LOG.debug("RMI binding: {}", service);
        try {
            Naming.rebind(service, ev3devRMIServer);
        } catch (RemoteException | MalformedURLException e) {
            LOG.error("RMI binding failed", e);
            System.exit(EV3devConstants.SYSTEM_UNEXPECTED_ERROR);
        }
        LOG.info("started successfully");
        DeviceRunner deviceRunner = new DeviceRunner();
        ev3devRMIServer.getEv3devStatus().setDeviceRunner(deviceRunner);
        //To Stop the motor in case of pkill java for example
        Runtime.getRuntime().addShutdownHook(new Thread(deviceRunner::stop));

        deviceRunner.run(ev3devRMIServer);
        try {
            Naming.unbind(service);
        } catch (RemoteException |NotBoundException | MalformedURLException e) {
            LOG.error("RMI unbinding failed", e);
            System.exit(EV3devConstants.SYSTEM_UNEXPECTED_ERROR);
        }

        System.exit(SYSTEM_FINISHED_SUCCESSFULLY);
        LOG.info("stopped");
    }

    private String getHost() {
        return host;
    }

    @Override
    public boolean isInitialized() {
        return false;
    }

    @Override
    public void beep() {
        LOG.debug("beep()");
        Sound sound = Sound.getInstance();
        int volume = sound.getVolume();
        sound.setVolume(volume/2);
        sound.beep();
    }

    @Override
    public void forward() {
        LOG.debug("forward()");
        this.getEv3devStatus().setMoving(EV3devStatus.Moving.forward);
    }

    @Override
    public void backward() {
        LOG.debug("backward()");
        this.getEv3devStatus().setMoving(EV3devStatus.Moving.backward);
    }

    @Override
    public void stop() {
        LOG.debug("stop()");
        this.getEv3devStatus().setMoving(EV3devStatus.Moving.stop);
    }

    @Override
    public void left() {
        LOG.debug("left()");
        this.getEv3devStatus().setDirection(EV3devStatus.Direction.left);
    }

    @Override
    public void right() {
        LOG.debug("right()");
        this.getEv3devStatus().setDirection(EV3devStatus.Direction.right);
    }

    @Override
    public void straight() {
        LOG.debug("straight()");
        this.getEv3devStatus().setDirection(EV3devStatus.Direction.straight);
    }

    @Override
    public void climb() {
        LOG.debug("climb()");
//        this.getEv3devStatus().setEv3devAction(Ev3devAction.climb);
    }

    @Override
    public void shutdown() {
        LOG.debug("shutdown()");
        this.getEv3devStatus().setToBeStopped(true);
    }

    /**
     * provides the current status of the ev3dev devices
     * @return ev3devStatus
     */
    EV3devStatus getEv3devStatus() {
        return ev3devStatus;
    }
}
