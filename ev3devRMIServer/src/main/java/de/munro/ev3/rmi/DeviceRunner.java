package de.munro.ev3.rmi;

import de.munro.ev3.motor.ClimbBackMotor;
import de.munro.ev3.motor.ClimbFrontMotor;
import de.munro.ev3.motor.DriveMotor;
import de.munro.ev3.motor.SteeringMotor;
import de.munro.ev3.sensor.BackwardSensor;
import de.munro.ev3.sensor.ColorSensor;
import de.munro.ev3.sensor.DistanceSensor;
import de.munro.ev3.sensor.GyroSensor;
import lejos.utility.Delay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DeviceRunner {
    private static final Logger LOG = LoggerFactory.getLogger(DeviceRunner.class);

    private BackwardSensor backwardSensor;
    private GyroSensor gyroSensor;
    private ColorSensor colorSensor;
    private DistanceSensor distanceSensor;

    private DriveMotor driveMotor;
    private ClimbBackMotor climbBackMotor;
    private SteeringMotor steeringMotor;
    private ClimbFrontMotor climbFrontMotor;

    DeviceRunner() {
        this.backwardSensor = new BackwardSensor();
        this.gyroSensor = new GyroSensor();
        this.colorSensor = new ColorSensor();
        this.distanceSensor = new DistanceSensor();

        this.driveMotor = new DriveMotor();
        this.climbBackMotor = new ClimbBackMotor();
        this.steeringMotor = new SteeringMotor();
        this.climbFrontMotor = new ClimbFrontMotor();
    }

    void stop() {
        this.driveMotor.stop();
        this.climbBackMotor.stop();
        this.steeringMotor.stop();
        this.climbFrontMotor.stop();
    }

    void run(EV3devRMIServer ev3devRMIServer) {
        LOG.info("initialize all motor devices");

        // initialize motors
        driveMotor.init();
        steeringMotor.init();
        climbFrontMotor.init();
        climbBackMotor.init();

        //Robot control loop
        LOG.info("start running()");
        EV3devStatus.Moving currentlyMoving = EV3devStatus.Moving.stop;
        EV3devStatus.Direction currentDirection = EV3devStatus.Direction.straight;
        while (true) {
            // the show is over?
            if (ev3devRMIServer.getEv3devStatus().isToBeStopped()) {
                LOG.info("needs to be stopped: {}", ev3devRMIServer.getEv3devStatus().isToBeStopped());
                break;
            }

            if (backwardSensor.isPressed()) {
                LOG.debug("*********** Backward touch ***********");
//                backwardWithClimb();
            }

            // moving has changed?
            if (currentlyMoving != ev3devRMIServer.getEv3devStatus().getMoving()) {
                LOG.info("next to do: {}", ev3devRMIServer.getEv3devStatus().getMoving());
                currentlyMoving = doMoving(ev3devRMIServer.getEv3devStatus().getMoving());
            }

            // direction has changed?
            if (currentDirection != ev3devRMIServer.getEv3devStatus().getDirection()) {
                LOG.info("next to do: {}", ev3devRMIServer.getEv3devStatus().getDirection());
                currentDirection = doDirection(ev3devRMIServer.getEv3devStatus().getDirection());
            }

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

//            LOG.debug("Iteration: " + i);
//            LOG.debug("Battery: " + Battery.getInstance().getVoltage());
//            LOG.debug("Distance: " + distance);
//            if (i % 5 == 0) {
//                LOG.debug("Angle/Rate: {}", gyroSensor.getGyroAngleRate());
//            }
            LOG.debug("");
            Delay.msDelay(2000);
        }
    }

    private EV3devStatus.Direction doDirection(EV3devStatus.Direction direction) {
        switch (direction) {
            case left:
                steeringMotor.goLeft();
                break;
            case right:
                steeringMotor.goRight();
                break;
            case straight:
                steeringMotor.goStraight();
                break;
        }
        return direction;
    }

    private EV3devStatus.Moving doMoving(EV3devStatus.Moving moving) {
        switch (moving) {
            case forward:
                driveMotor.forward();
                break;
            case backward:
                driveMotor.backward();
                break;
            case stop:
                driveMotor.stop();
                break;
        }
        return moving;
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
        steeringMotor.goStraight();
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
