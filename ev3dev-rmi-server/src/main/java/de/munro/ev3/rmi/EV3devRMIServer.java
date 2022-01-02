package de.munro.ev3.rmi;

import de.munro.ev3.controller.MotorThread;
import de.munro.ev3.controller.SensorThread;
import de.munro.ev3.data.EV3devData;
import de.munro.ev3.data.MotorData;
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
import java.util.Collections;
import java.util.List;

import static de.munro.ev3.rmi.EV3devConstants.*;

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
     * read command line arguments
     */
    private EV3devRMIServer(String[] args) throws RemoteException {
        super();
        if (null != args && args.length >= 1 && !args[0].isEmpty()) {
            this.host = args[0];
        }
        motorThreads = Collections.synchronizedList(new ArrayList<>());
    }

    public static void main(String[] args) throws InvalidNameException {
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
    }

    /**
     * create all threads
     */
    private void createThreads() {
        // start sensor thread
        this.setSensorThread(this.startSensorThread(this.getEv3devData().getSensorData()));
        log.info("Sensor instances created.");

        // instantiate motors
        Motor driveMotor = new DriveMotor(this.getEv3devData().getMotorData(MotorType.drive));
        Motor liftBackMotor = new LiftMotor(
                Motor.Polarity.inversed,
                RemoteEV3.MotorType.liftBack,
                this.getEv3devData().getMotorData(MotorType.liftBack));
        Motor liftFrontMotor = new LiftMotor(
                Motor.Polarity.inversed,
                RemoteEV3.MotorType.liftFront,
                this.getEv3devData().getMotorData(MotorType.liftFront));
        Motor steeringMotor = new SteeringMotor(this.getEv3devData().getMotorData(MotorType.steering));
        log.info("Motor instances created.");

        // start motor threads
        this.getMotorThreads().add(this.startMotorThread(liftBackMotor));
        this.getMotorThreads().add(this.startMotorThread(liftFrontMotor));
        this.getMotorThreads().add(this.startMotorThread(driveMotor));
        this.getMotorThreads().add(this.startMotorThread(steeringMotor));
        log.info("Threads running.");
    }

    /**
     * stop all motors
     */
    private void halt() {
        log.debug("halt()");

        // notify threads to stop
        for (MotorType motorType : MotorType.values()) {
            synchronized (this.getEv3devData().getMotorData(motorType)) {
                this.getEv3devData().getMotorData(motorType).setMotorStatus(MotorData.MotorStatus.toBeStopped);
                this.getEv3devData().getMotorData(motorType).notify();
            }
        }

        // wait for all threads stopped
        boolean allThreadsStopped;
        do {
            allThreadsStopped = true;
            for (MotorType motorType : MotorType.values()) {
                allThreadsStopped &= this.getEv3devData().getMotorData(motorType).getMotorStatus() == MotorData.MotorStatus.stopped;
            }
            Delay.msDelay(DELAY_PERIOD_SHORT);
        } while (!allThreadsStopped);

        //RMI
        String service = String.format("//%s/%s", this.getHost(), RemoteEV3.SERVICE_NAME);
        log.debug("RMI unbinding: {}", service);
        try {
            Naming.unbind(service);
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            log.error("RMI unbinding failed", e);
        }
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
    public void beep() throws RemoteException {
        log.debug("beep()");
        Sound sound = Sound.getInstance();
        int volume = sound.getVolume();
        sound.setVolume(volume / 2);
        sound.beep();
    }

    @Override
    public void shutdown() throws RemoteException {
        log.debug("shutdown()");
        System.exit(SYSTEM_FINISHED_SUCCESSFULLY);
    }

    @Override
    public void perform(MotorType motorType, Command command) throws RemoteException {
        log.debug("perform({}, {})", motorType, command);

        synchronized (this.getEv3devData().getMotorData(motorType)) {
            this.getEv3devData().getMotorData(motorType).setInstruction(Instruction.perform);
            this.getEv3devData().getMotorData(motorType).setCommand(command);
            this.getEv3devData().getMotorData(motorType).notify();
        }
    }

    @Override
    public void set(MotorType motorType, Command command, Integer value) throws RemoteException {
        log.debug("set({}, {}, {})", motorType, command, value);
        synchronized (this.getEv3devData().getMotorData(motorType)) {
            this.getEv3devData().getMotorData(motorType).setPosition(command, value);
        }
    }

    @Override
    public void read(MotorType motorType) throws RemoteException {
        log.debug("read({})", motorType);
        synchronized (this.getEv3devData().getMotorData(motorType)) {
            this.getEv3devData().getMotorData(motorType).setInstruction(Instruction.read);
            this.getEv3devData().getMotorData(motorType).notify();
        }
    }

    @Override
    public void write(MotorType motorType) throws RemoteException {
        log.debug("write({})", motorType);
        synchronized (this.getEv3devData().getMotorData(motorType)) {
            this.getEv3devData().getMotorData(motorType).setInstruction(Instruction.write);
            this.getEv3devData().getMotorData(motorType).notify();
        }
    }

    @Override
    public void show(MotorType motorType) throws RemoteException {
        log.debug("show({})", motorType);
        log.debug(motorType + "" + this.getEv3devData().getMotorData(motorType));
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
     * Gets the motorThreads
     * @return motorThreads
     */
    public List<MotorThread> getMotorThreads() {
        return motorThreads;
    }

    /**
     * Gets the ev3devData
     * @return ev3devData
     */
    public EV3devData getEv3devData() {
        return ev3devData;
    }
}
