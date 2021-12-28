package de.munro.ev3.rmi;

import de.munro.ev3.controller.MotorThread;
import de.munro.ev3.controller.SensorThread;
import de.munro.ev3.data.EV3devData;
import de.munro.ev3.data.SensorData;
import de.munro.ev3.motor.*;
import ev3dev.actuators.Sound;
import lejos.utility.Delay;
import lombok.extern.slf4j.Slf4j;

import javax.naming.InvalidNameException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import static de.munro.ev3.rmi.EV3devConstants.DELAY_PERIOD_SHORT;
import static de.munro.ev3.rmi.EV3devConstants.SYSTEM_FINISHED_SUCCESSFULLY;

@Slf4j
public class EV3devRMIServer extends UnicastRemoteObject implements RemoteEV3 {
    
    private static final String LOCAL_HOST = "localhost";

    private String host = LOCAL_HOST;
    // threads
    private Thread sensorThread;
    private final List<MotorThread> motorThreads;

    private final EV3devData ev3devData = new EV3devData();

    /**
     * Constructor
     */
    private EV3devRMIServer(String[] args) throws RemoteException {
        super();
        if (null != args && args.length >= 1 && !args[0].isEmpty()) {
            this.host = args[0];
        }
        motorThreads = new ArrayList<>();
    }

    public static void main(String[] args) {
        log.info("Started {}", (Object) args);

        try {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        } catch (RemoteException e) {
            log.error("Remote object registering failed", e);
            System.exit(EV3devConstants.SYSTEM_UNEXPECTED_ERROR);
        }

        EV3devRMIServer ev3devRMIServer = null;
        try {
            ev3devRMIServer = new EV3devRMIServer(args);
        } catch (RemoteException e) {
            log.error("Initialization failed", e);
            System.exit(EV3devConstants.SYSTEM_UNEXPECTED_ERROR);
        }

        //RMI
        String service = String.format("//%s/%s", ev3devRMIServer.getHost(), RemoteEV3.SERVICE_NAME);
        log.debug("RMI binding: {}", service);
        try {
            Naming.rebind(service, ev3devRMIServer);
        } catch (RemoteException | MalformedURLException e) {
            log.error("RMI binding failed", e);
            System.exit(EV3devConstants.SYSTEM_UNEXPECTED_ERROR);
        }
        log.info("started successfully");

        // init motor threads
        ev3devRMIServer.createThreads();

        // To Stop the motor in case of pkill java for example
        Runtime.getRuntime().addShutdownHook(new Thread(ev3devRMIServer::halt));

        // controlling threads
        ev3devRMIServer.controlThreads();

        try {
            Naming.unbind(service);
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            log.error("RMI unbinding failed", e);
            System.exit(EV3devConstants.SYSTEM_UNEXPECTED_ERROR);
        }

        System.exit(SYSTEM_FINISHED_SUCCESSFULLY);
        log.info("stopped");
    }

    /**
     * create all threads
     */
    private void createThreads() {
        // start sensor thread
        this.setSensorThread(this.startSensorThread(this.getEv3devData().getSensorData()));
        log.info("Sensor instances created.");

        // instantiate motors
        Motor driveMotor = new DriveMotor(this.getEv3devData().getDriveMotorData());
        Motor climbBackMotor = new ClimbBackMotor(this.getEv3devData().getClimbBackMotorData());
        Motor climbFrontMotor = new ClimbFrontMotor(this.getEv3devData().getClimbFrontMotorData());
        Motor steeringMotor = new SteeringMotor(this.getEv3devData().getSteeringMotorData());
        log.info("Motor instances created.");

        // start motor threads
        this.getMotorThreads().add(this.startMotorThread(climbBackMotor));
        this.getMotorThreads().add(this.startMotorThread(climbFrontMotor));
        this.getMotorThreads().add(this.startMotorThread(driveMotor));
        this.getMotorThreads().add(this.startMotorThread(steeringMotor));
        log.info("Threads running.");
    }

    /**
     * check all threads for necessary updates
     */
    private void controlThreads() {
        while (!getEv3devData().isStopped()) {
            for (MotorThread motorThread : motorThreads) {
                if (!motorThread.getMotor().getMotorData().isDone()) {
                    notifyAll();
                }
            }
            Delay.msDelay(DELAY_PERIOD_SHORT);
        }
    }

    /**
     * stop all motors
     */
    private void halt() {
        this.getEv3devData().setStopped(true);
    }

    /**
     * start the thread responsible to watch all sensor devices
     * @param sensorData sensorData to set
     * @return current sensorThread
     */
    private Thread startSensorThread(SensorData sensorData) {
        Thread thread = new SensorThread(sensorData);
        thread.start();
        return thread;
    }

    /**
     * start the thread responsible for a single motor device
     * @param motor motor to set
     * @return current motorThread
     */
    private MotorThread startMotorThread(Motor motor) {
        MotorThread thread = new MotorThread(motor);
        thread.setUncaughtExceptionHandler(
                (t, e) -> log.error(t + " throws exception: " + e));
        thread.start();
        return thread;
    }

    /**
     * @return current host name
     */
    private String getHost() {
        return host;
    }

    @Override
    public boolean isInitialized() {
        return false;
    }

    @Override
    public void beep() {
        log.debug("beep()");
        Sound sound = Sound.getInstance();
        int volume = sound.getVolume();
        sound.setVolume(volume / 2);
        sound.beep();
    }

    @Override
    public void shutdown() {
        log.debug("shutdown()");
        this.getEv3devData().setStopped(true);
    }

    @Override
    public void perform(MotorType motorType, Command command) throws RemoteException, InvalidNameException {

    }

    @Override
    public void set(MotorType motor, Command command, Integer value) throws RemoteException, InvalidNameException {

    }

    @Override
    public void read(MotorType motor) throws RemoteException, InvalidNameException {

    }

    @Override
    public void write(MotorType motor) throws RemoteException, InvalidNameException {

    }

    @Override
    public void show(MotorType motor) throws RemoteException, InvalidNameException {

    }

    /**
     * @return current sensorThread
     */
    public Thread getSensorThread() {
        return sensorThread;
    }

    /**
     * @param sensorThread sensorThread to set
     */
    public void setSensorThread(Thread sensorThread) {
        this.sensorThread = sensorThread;
    }

    /**
     * @return current motorThreads
     */
    public List<MotorThread> getMotorThreads() {
        return motorThreads;
    }

    /**
     * @return current ev3devData
     */
    public EV3devData getEv3devData() {
        return ev3devData;
    }
}
