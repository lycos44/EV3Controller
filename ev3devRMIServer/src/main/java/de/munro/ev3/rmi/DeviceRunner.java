package de.munro.ev3.rmi;

import de.munro.ev3.motor.CameraMotor;
import de.munro.ev3.motor.ClimbMotor;
import de.munro.ev3.motor.DriveMotor;
import de.munro.ev3.motor.SteeringMotor;
import de.munro.ev3.sensor.BackwardSensor;
import de.munro.ev3.sensor.CameraSensor;
import de.munro.ev3.sensor.ColorSensor;
import de.munro.ev3.sensor.DistanceSensor;
import ev3dev.sensors.Battery;
import lejos.utility.Delay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeviceRunner {
    private static final Logger LOG = LoggerFactory.getLogger(DeviceRunner.class);

    private BackwardSensor backwardSensor;
    private CameraSensor cameraSensor;
    private ColorSensor colorSensor;
    private DistanceSensor distanceSensor;

    private DriveMotor driveMotor;
    private ClimbMotor climbMotor;
    private SteeringMotor steeringMotor;
    private CameraMotor cameraMotor;

    public DeviceRunner() {
        this.backwardSensor = new BackwardSensor();
        this.cameraSensor = new CameraSensor();
        this.colorSensor = new ColorSensor();
        this.distanceSensor = new DistanceSensor();

        this.driveMotor = new DriveMotor();
        this.climbMotor = new ClimbMotor();
        this.steeringMotor = new SteeringMotor();
        this.cameraMotor = new CameraMotor(this.cameraSensor);
    }

    public void stop() {
        this.driveMotor.stop();
        this.climbMotor.stop();
        this.steeringMotor.stop();
        this.cameraMotor.stop();
    }

    public void run() {
        LOG.info("start running()");

        int distance = 255;

        final int distance_threshold = 25;

        steeringMotor.init();
        cameraMotor.init();
        climbMotor.init();
        //Robot control loop
        final int iteration_threshold = 200;
        for(int i = 0; i <= iteration_threshold; i++) {
            driveMotor.forward();

            if(distanceSensor.getDistance() <= distance_threshold) {
                LOG.debug("Detected obstacle");
                backwardWithTurn();
            }

            if (backwardSensor.isPressed()) {
                LOG.debug("Backward touch");
                backwardWithTurn();
            }

            if (cameraSensor.isPressed()) {
                LOG.debug("CameraSensor touch");
            }
            LOG.debug("Iteration: " + i);
            LOG.debug("Battery: " + Battery.getInstance().getVoltage());
            LOG.debug("Distance: " + distance);
            LOG.debug("");
        }
    }

    private void backwardWithTurn() {
        driveMotor.stop();
        steeringMotor.goLeft();
        driveMotor.backward();
        Delay.msDelay(4000);
        driveMotor.stop();
        steeringMotor.goRight();
        driveMotor.forward();
        Delay.msDelay(4000);
        driveMotor.stop();
        steeringMotor.goHome();
    }
}
