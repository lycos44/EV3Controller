package de.munro.ev3.rmi;

import de.munro.ev3.motor.ClimbBackMotor;
import de.munro.ev3.motor.ClimbFrontMotor;
import de.munro.ev3.motor.DriveMotor;
import de.munro.ev3.motor.SteeringMotor;
import de.munro.ev3.sensor.BackwardSensor;
import de.munro.ev3.sensor.ColorSensor;
import de.munro.ev3.sensor.DistanceSensor;
import de.munro.ev3.sensor.GyroSensor;
import ev3dev.sensors.Battery;
import lejos.utility.Delay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeviceRunner {
    private static final Logger LOG = LoggerFactory.getLogger(DeviceRunner.class);

    private BackwardSensor backwardSensor;
    private GyroSensor gyroSensor;
    private ColorSensor colorSensor;
    private DistanceSensor distanceSensor;

    private DriveMotor driveMotor;
    private ClimbBackMotor climbBackMotor;
    private SteeringMotor steeringMotor;
    private ClimbFrontMotor climbFrontMotor;

    public DeviceRunner() {
        this.backwardSensor = new BackwardSensor();
        this.gyroSensor = new GyroSensor();
        this.colorSensor = new ColorSensor();
        this.distanceSensor = new DistanceSensor();

        this.driveMotor = new DriveMotor();
        this.climbBackMotor = new ClimbBackMotor();
        this.steeringMotor = new SteeringMotor();
        this.climbFrontMotor = new ClimbFrontMotor();
    }

    public void stop() {
        this.driveMotor.stop();
        this.climbBackMotor.stop();
        this.steeringMotor.stop();
        this.climbFrontMotor.stop();
    }

    public void run() {
        LOG.info("start running()");

        int distance = 255;

        final int distance_threshold = 25;

        steeringMotor.init();
        climbFrontMotor.init();
        climbBackMotor.init();
        //Robot control loop
        final int iteration_threshold = 100;
        for(int i = 0; i <= iteration_threshold; i++) {
            driveMotor.backward();

            if (backwardSensor.isPressed()) {
                LOG.debug("*********** Backward touch ***********");
                backwardWithClimb();
            }
////            driveMotor.forward();
//
////            if(distanceSensor.getDistance() <= distance_threshold) {
////                LOG.debug("Detected obstacle");
////                backwardWithTurn();
////            }
////            if(gyroSensor.getGyroAngleRate() > 0) {
////                LOG.debug("Detected obstacle");
////                backwardWithTurn();
////            }
//
//            if (backwardSensor.isPressed()) {
//                LOG.debug("Backward touch");
//                backwardWithTurn();
//            }

            LOG.debug("Iteration: " + i);
            LOG.debug("Battery: " + Battery.getInstance().getVoltage());
            LOG.debug("Distance: " + distance);
//            if (i % 5 == 0) {
//                LOG.debug("Angle/Rate: {}", gyroSensor.getGyroAngleRate());
//            }
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

    private void backwardWithClimb() {
        driveMotor.stop();
        climbBackMotor.goClimb();
        driveMotor.backward();
        Delay.msDelay(2000);
        driveMotor.stop();
        climbBackMotor.goHome();
        // back wheels are up
        driveMotor.backward();
        Delay.msDelay(2000);
        driveMotor.stop();
        climbFrontMotor.goClimb();
        driveMotor.backward();
        Delay.msDelay(1000);
        driveMotor.stop();
        climbFrontMotor.goHome();
        // front wheels are up
    }
}
