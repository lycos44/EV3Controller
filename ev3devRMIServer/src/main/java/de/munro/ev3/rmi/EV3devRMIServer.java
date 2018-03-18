package de.munro.ev3.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import de.munro.ev3.rmi.RemoteEV3;
import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import ev3dev.sensors.ev3.EV3ColorSensor;
import ev3dev.sensors.ev3.EV3TouchSensor;
import ev3dev.sensors.ev3.EV3UltrasonicSensor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EV3devRMIServer extends UnicastRemoteObject implements RemoteEV3 {
    private static final Logger LOG = LoggerFactory.getLogger(EV3devRMIServer.class);
    private static final String LOCAL_HOST = "localhost";

    private String host = LOCAL_HOST;

    private EV3LargeRegulatedMotor motorDrive;      // MotorPort.A
    private EV3LargeRegulatedMotor motorClimb;      // MotorPort.B
    private EV3LargeRegulatedMotor steeringMotor;   // MotorPort.C
    private EV3LargeRegulatedMotor cameraMotor;     // MotorPort.D

    private EV3UltrasonicSensor distanceSensor;     // SensorPort.S2
    private EV3ColorSensor colorSensor;             // SensorPort.S3
    private EV3TouchSensor touchSensor;             // SensorPort.S4

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
            String service = String.format("//%s/%s", ev3devRMIServer.getHost(), RemoteEV3.SERVICE_NAME);
            Naming.rebind(service, ev3devRMIServer);
            LOG.info("Registered service: {}", service);

            RemoteEV3 remoteEV3 = (RemoteEV3) Naming.lookup(service);
            ev3devRMIServer.initializeRemote(remoteEV3);
            if (!remoteEV3.isInitialized()) {
                ev3devRMIServer.initializeRemote(remoteEV3);
            }
            LOG.info("Service initialized: {}", remoteEV3.isInitialized());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        LOG.info("Service successfully started");
    }

    protected void initializeRemote(RemoteEV3 remoteEV3) {
        LOG.info("initializeRemote()");
        try {
            remoteEV3.initialize();
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
    public void initialize() throws RemoteException {
        LOG.info("initialize()");
        distanceSensor = (null==distanceSensor)?new EV3UltrasonicSensor(SensorPort.S2):distanceSensor;
        colorSensor = (null==colorSensor)?new EV3ColorSensor(SensorPort.S3):colorSensor;
        touchSensor = (null==touchSensor)?new EV3TouchSensor(SensorPort.S4):touchSensor;

        motorDrive = (null==motorDrive)?createMotor(MotorPort.A):motorDrive;
        motorClimb = (null==motorClimb)?createMotor(MotorPort.B):motorClimb;
        steeringMotor = (null==steeringMotor)?createMotor(MotorPort.C):steeringMotor;
        cameraMotor = (null==cameraMotor)?createMotor(MotorPort.D):cameraMotor;

        //To Stop the motor in case of pkill java for example
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Emergency stop");
            motorDrive.stop();
            motorClimb.stop();
            steeringMotor.stop();
            cameraMotor.stop();
            LOG.info("All motors stopped");
        }));
    }

    @Override
    public boolean isInitialized() throws RemoteException {
        LOG.info("isInitialized()");
        if (null == distanceSensor) {
            LOG.error("distanceSensor not initialized");
            return false;
        }
        if (null == colorSensor) {
            LOG.error("colorSensor not initialized");
            return false;
        }
        if (null == touchSensor) {
            LOG.error("touchSensor not initialized");
            return false;
        }
        if (null == motorDrive) {
            LOG.error("motorDrive not initialized");
            return false;
        }
        if (null == motorClimb) {
            LOG.error("motorClimb not initialized");
            return false;
        }
        if (null == steeringMotor) {
            LOG.error("steeringMotor not initialized");
            return false;
        }
        if (null == cameraMotor) {
            LOG.error("cameraMotor not initialized");
            return false;
        }
        return true;
    }

    @Override
    public void beep() throws RemoteException {
        LOG.info("beep()");
    }

    @Override
    public void forward() throws RemoteException {
        LOG.info("forward()");
        motorDrive.forward();
    }

    @Override
    public void backward() throws RemoteException {
        LOG.info("backward()");
        motorDrive.backward();
    }

    @Override
    public void stop() throws RemoteException {
        LOG.info("stop()");
        motorDrive.stop();
    }

    @Override
    public void shutdown() throws RemoteException {
        LOG.info("shutdown()");
    }

    private EV3LargeRegulatedMotor createMotor(Port port) {
        LOG.info("createMotor({})", port);
        EV3LargeRegulatedMotor motor = new EV3LargeRegulatedMotor(port);
        motor.brake();
        motor.setSpeed(200);

        return motor;
    }
}
