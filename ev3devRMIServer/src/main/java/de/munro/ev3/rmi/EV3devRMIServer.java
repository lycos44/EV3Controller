package de.munro.ev3.rmi;

import de.munro.ev3.controller.MotorThread;
import de.munro.ev3.controller.SensorThread;
import de.munro.ev3.logger.EV3devLogger;
import de.munro.ev3.logger.LoggerData;
import de.munro.ev3.motor.*;
import ev3dev.actuators.Sound;
import lejos.utility.Delay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import static de.munro.ev3.rmi.EV3devConstants.DELAY_PERIOD_SHORT;
import static de.munro.ev3.rmi.EV3devConstants.SYSTEM_FINISHED_SUCCESSFULLY;

public class EV3devRMIServer extends UnicastRemoteObject implements RemoteEV3 {
    private static final Logger LOG = LoggerFactory.getLogger(EV3devRMIServer.class);
    private static final String LOCAL_HOST = "localhost";

    private String host = LOCAL_HOST;
    // threads
    private Thread sensorThread;
    private Thread climbBackMotorThread;
    private Thread climbFrontMotorThread;
    private Thread driveMotorThread;
    private Thread steeringMotorThread;

    private final EV3devLogger ev3devLogger;

    /**
     * Constructor
     */
    private EV3devRMIServer(String[] args) throws RemoteException {
        super();
        if (null != args && args.length >= 1 && !args[0].isEmpty()) {
            this.host = args[0];
        }
        ev3devLogger = new EV3devLogger();
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

        ev3devRMIServer.setSensorThread(ev3devRMIServer.startSensorThread(ev3devRMIServer.getEv3devLogger()));
        ev3devRMIServer.setClimbBackMotorThread(ev3devRMIServer.startMotorThread(new ClimbBackMotor(ev3devRMIServer.getEv3devLogger().getClimbBackMotorLogger())));
        ev3devRMIServer.setClimbFrontMotorThread(ev3devRMIServer.startMotorThread(new ClimbFrontMotor(ev3devRMIServer.getEv3devLogger().getFrontMotorLogger())));
        ev3devRMIServer.setDriveMotorThread(ev3devRMIServer.startMotorThread(new DriveMotor(ev3devRMIServer.getEv3devLogger().getDriveMotorLogger())));
        ev3devRMIServer.setSteeringMotorThread(ev3devRMIServer.startMotorThread(new SteeringMotor(ev3devRMIServer.getEv3devLogger().getSteeringMotorLogger())));


        // To Stop the motor in case of pkill java for example
        Runtime.getRuntime().addShutdownHook(new Thread(ev3devRMIServer::halt));

        // controlling threads
        while (ev3devRMIServer.getLoggerData().isRunning()) {
            if (!ev3devRMIServer.getSteeringMotorThread().isAlive()) {
                LOG.error("steering motor stopped running!");
            }
            Delay.msDelay(DELAY_PERIOD_SHORT);
        }

        try {
            Naming.unbind(service);
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            LOG.error("RMI unbinding failed", e);
            System.exit(EV3devConstants.SYSTEM_UNEXPECTED_ERROR);
        }

        System.exit(SYSTEM_FINISHED_SUCCESSFULLY);
        LOG.info("stopped");
    }

    /**
     * stop all motors
     */
    private void halt() {
        this.getLoggerData().setRunning(false);
    }

    private Thread startSensorThread(EV3devLogger ev3devLogger) {
        Thread thread = new SensorThread(ev3devLogger.getSensorLogger());
        thread.start();
        return thread;
    }

    private Thread startMotorThread(Motor motor) {
        Thread thread = new MotorThread(motor);
        thread.setUncaughtExceptionHandler(
                (t, e) -> LOG.error(t + " throws exception: " + e));
        thread.start();
        return thread;
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
        sound.setVolume(volume / 2);
        sound.beep();
    }

    @Override
    public void forward() {
        LOG.debug("forward()");
        this.getLoggerData().setDriveDirection(EV3devConstants.Direction.forward);
    }

    @Override
    public void backward() {
        LOG.debug("backward()");
        this.getLoggerData().setDriveDirection(EV3devConstants.Direction.backward);
    }

    @Override
    public void stop() {
        LOG.debug("stop()");
        this.getLoggerData().setDriveDirection(EV3devConstants.Direction.stop);
    }

    @Override
    public void left() {
        LOG.debug("left()");
        this.getLoggerData().setSteeringTurn(EV3devConstants.Turn.left);
    }

    @Override
    public void right() {
        LOG.debug("right()");
        this.getLoggerData().setSteeringTurn(EV3devConstants.Turn.right);
    }

    @Override
    public void straight() {
        LOG.debug("straight()");
        this.getLoggerData().setSteeringTurn(EV3devConstants.Turn.straight);
    }

    @Override
    public void frontup() {
        LOG.debug("frontup()");
        this.getLoggerData().setClimbFront(EV3devConstants.Climb.up);
    }

    @Override
    public void frontdown() {
        LOG.debug("frontdown()");
        this.getLoggerData().setClimbFront(EV3devConstants.Climb.down);
    }

    @Override
    public void backup() {
        LOG.debug("backup()");
        this.getLoggerData().setClimbBack(EV3devConstants.Climb.up);
    }

    @Override
    public void backdown() {
        LOG.debug("backdown()");
        this.getLoggerData().setClimbBack(EV3devConstants.Climb.down);
    }

    @Override
    public void reset() {
        LOG.debug("reset()");
//        this.getEv3devStatus().setReset(true);
    }

    @Override
    public void test() {
        LOG.debug("test()");
//        this.getEv3devStatus().setTest(true);
    }

    @Override
    public void shutdown() {
        LOG.debug("shutdown()");
        this.getLoggerData().setRunning(false);
    }

    public LoggerData getLoggerData() {
        return ev3devLogger.getLoggerData();
    }

    public EV3devLogger getEv3devLogger() {
        return ev3devLogger;
    }

    public Thread getSensorThread() {
        return sensorThread;
    }

    public void setSensorThread(Thread sensorThread) {
        this.sensorThread = sensorThread;
    }

    public Thread getClimbBackMotorThread() {
        return climbBackMotorThread;
    }

    public void setClimbBackMotorThread(Thread climbBackMotorThread) {
        this.climbBackMotorThread = climbBackMotorThread;
    }

    public Thread getClimbFrontMotorThread() {
        return climbFrontMotorThread;
    }

    public void setClimbFrontMotorThread(Thread climbFrontMotorThread) {
        this.climbFrontMotorThread = climbFrontMotorThread;
    }

    public Thread getDriveMotorThread() {
        return driveMotorThread;
    }

    public void setDriveMotorThread(Thread driveMotorThread) {
        this.driveMotorThread = driveMotorThread;
    }

    public Thread getSteeringMotorThread() {
        return steeringMotorThread;
    }

    public void setSteeringMotorThread(Thread steeringMotorThread) {
        this.steeringMotorThread = steeringMotorThread;
    }
}
