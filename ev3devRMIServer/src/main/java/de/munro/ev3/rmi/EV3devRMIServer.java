package de.munro.ev3.rmi;

import de.munro.ev3.motor.DriveMotor;
import de.munro.ev3.sensor.BackwardSensor;
import de.munro.ev3.sensor.DistanceSensor;
import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import ev3dev.sensors.Battery;
import lejos.hardware.port.MotorPort;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class EV3devRMIServer extends UnicastRemoteObject implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(EV3devRMIServer.class);
    private static final String LOCAL_HOST = "localhost";

    //Configuration
    private final static int motorSpeed = 200;
    private String host = LOCAL_HOST;

    public EV3devRMIServer(String[] args) throws RemoteException {
        super();
        if (null != args && args.length >= 1 && !args[0].isEmpty()){
            this.host = args[0];
        }
    }

    public static void main(String[] args) {
        LOG.info("Started {}", (Object[]) args);
        if (null != args && args.length == 1 && args[0].equalsIgnoreCase("test")) {
            test();
            System.exit(EV3devConstants.SYSTEM_FINISHED_SUCCESSFULLY);
        }

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
        (new Thread(ev3devRMIServer)).start();

        String service = String.format("//%s/%s", ev3devRMIServer.getHost(), RemoteEV3.SERVICE_NAME);
        try {
            Naming.rebind(service, ev3devRMIServer);
        } catch (RemoteException | MalformedURLException e) {
            LOG.error("RMI name binding failed", e);
            System.exit(EV3devConstants.SYSTEM_UNEXPECTED_ERROR);
        }
    }

    private static void test() {
        LOG.info("Starting motors on A");
        final EV3LargeRegulatedMotor mA = new EV3LargeRegulatedMotor(MotorPort.A);
        mA.setSpeed(500);
        mA.brake();
        mA.forward();
        LOG.info(String.format("Large Motor is moving: %s at speed %d", mA.isMoving(), mA.getSpeed()));
        Delay.msDelay(2000);
        mA.stop();
        LOG.info("Stopped motors");    }

    private String getHost() {
        return host;
    }

    public void run() {
        LOG.info("thread running()");
        DriveMotor driveMotor = new DriveMotor();
        DistanceSensor distanceSensor = new DistanceSensor();
        BackwardSensor backwardSensor = new BackwardSensor();

        //To Stop the motor in case of pkill java for example
        Runtime.getRuntime().addShutdownHook(new Thread(() -> driveMotor.stop()));

        final SampleProvider sp = distanceSensor.getSensor().getDistanceMode();
        int distance = 255;

        final int distance_threshold = 35;

        //Robot control loop
        final int iteration_threshold = 400;
        for(int i = 0; i <= iteration_threshold; i++) {
            forward(driveMotor);

            float [] sample = new float[sp.sampleSize()];
            sp.fetchSample(sample, 0);
            distance = (int)sample[0];

            if(distance <= distance_threshold){
                System.out.println("Detected obstacle");
                backwardWithTurn(driveMotor);
            }

            if (backwardSensor.getSensor().isPressed()) {
                System.out.println("Backward touch");
                backwardWithTurn(driveMotor);
            }

            System.out.println("Iteration: " + i);
            System.out.println("Battery: " + Battery.getInstance().getVoltage());
            System.out.println("Distance: " + distance);
            System.out.println();
        }

        driveMotor.stop();
    }

    private void forward(DriveMotor motor){
        motor.forward();
    }

    private void backwardWithTurn(DriveMotor motor){
        motor.backward();
        Delay.msDelay(1000);
        motor.stop();
    }

}
