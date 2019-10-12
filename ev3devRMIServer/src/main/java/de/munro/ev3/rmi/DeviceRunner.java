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

class DeviceRunner {
    private static final Logger LOG = LoggerFactory.getLogger(DeviceRunner.class);
    private static final int DELAY_PERIOD_NORMAL = 2000;
    private static final int DELAY_PERIOD_SHORT = 500;
    private static final float MINIMUM_VOLTAGE = 7.0F;

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

        // Robot control loop
        LOG.info("start running()");
        EV3devStatus.Direction currentDirection = EV3devStatus.Direction.stop;
        EV3devStatus.Turn currentTurn = EV3devStatus.Turn.straight;
        EV3devStatus.Climb currentFront = EV3devStatus.Climb.up;
        EV3devStatus.Climb currentBack = EV3devStatus.Climb.up;
        double currentVoltage = 0;

        while (true) {
            // what about are battery, is it running low?
            double voltage = Math.round(10.0 * Battery.getInstance().getVoltage()) / 10.0;
            if (voltage <= MINIMUM_VOLTAGE) {
                LOG.error("EV3 battery is running low: {}, CANNOT PROCEED, PLEASE RECHARGE!", voltage);
                System.exit(EV3devConstants.SYSTEM_UNEXPECTED_ERROR);
            }
            // adapt speed?
            if (currentVoltage == 0 || currentVoltage > voltage) {
                adaptSpeed(voltage);
            }

            // the show is over?
            if (ev3devRMIServer.getEv3devStatus().isToBeStopped()) {
                LOG.info("needs to be stopped: {}", ev3devRMIServer.getEv3devStatus().isToBeStopped());
                break;
            }

            if (backwardSensor.isPressed()) {
                LOG.debug("*********** Backward: touch ***********");
//                backwardWithClimb();
            }

            if (distanceSensor.getDistance() <= 30
                    && ev3devRMIServer.getEv3devStatus().getDirection() == EV3devStatus.Direction.forward
                    && driveMotor.getSpeed() == DriveMotor.MOTOR_SPEED_NORMAL
            ) {
                LOG.debug("*********** Forward: short distance ***********");
                driveMotor.setSpeed(DriveMotor.MOTOR_SPEED_SLOW);
            }

            if (distanceSensor.getDistance() <= 15
                    && ev3devRMIServer.getEv3devStatus().getDirection() == EV3devStatus.Direction.forward
            ) {
                LOG.debug("*********** Forward: no distance ***********");
                ev3devRMIServer.getEv3devStatus().setDirection(EV3devStatus.Direction.stop);
            }

            if ((colorSensor.toString().equals("none") || colorSensor.toString().equals("undefined")) && ev3devRMIServer.getEv3devStatus().getDirection() == EV3devStatus.Direction.forward) {
                LOG.debug("*********** Forward: unknown surface ***********");
                ev3devRMIServer.getEv3devStatus().setDirection(EV3devStatus.Direction.stop);
            }
            LOG.info("motor status: ({})", ev3devRMIServer.getEv3devStatus());
            LOG.info("sensor status: ({}, {}, {}, {}, {})", Battery.getInstance().getVoltage(), distanceSensor, backwardSensor, gyroSensor, colorSensor);

            // direction needs to be changed?
            if (currentDirection != ev3devRMIServer.getEv3devStatus().getDirection()) {
                LOG.info("next to do: {}", ev3devRMIServer.getEv3devStatus().getDirection());
                currentDirection = doDirection(ev3devRMIServer.getEv3devStatus().getDirection());
            }

            // turn?
            if (currentTurn != ev3devRMIServer.getEv3devStatus().getTurn()) {
                LOG.info("next to do: {}", ev3devRMIServer.getEv3devStatus().getTurn());
                currentTurn = doTurn(ev3devRMIServer.getEv3devStatus().getTurn());
            }

            // climb front?
            if (currentFront != ev3devRMIServer.getEv3devStatus().getFront()) {
                LOG.info("next to do: {}", ev3devRMIServer.getEv3devStatus().getFront());
                currentFront = doFront(ev3devRMIServer.getEv3devStatus().getFront());
            }

            // climb back?
            if (currentBack != ev3devRMIServer.getEv3devStatus().getBack()) {
                LOG.info("next to do: {}", ev3devRMIServer.getEv3devStatus().getBack());
                currentBack = doBack(ev3devRMIServer.getEv3devStatus().getBack());
            }

            Delay.msDelay(DELAY_PERIOD_SHORT);
        }
    }

    private void adaptSpeed(double voltage) {

    }

    private EV3devStatus.Climb doFront(EV3devStatus.Climb front) {
        switch (front) {
            case up:
                climbFrontMotor.goUp();
                break;
            case down:
                climbFrontMotor.goDown();
        }
        return front;
    }

    private EV3devStatus.Climb doBack(EV3devStatus.Climb back) {
        switch (back) {
            case up:
                climbBackMotor.goUp();
                break;
            case down:
                climbBackMotor.goDown();
        }
        return back;
    }

    private EV3devStatus.Turn doTurn(EV3devStatus.Turn turn) {
        switch (turn) {
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
        return turn;
    }

    private EV3devStatus.Direction doDirection(EV3devStatus.Direction direction) {
        switch (direction) {
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
        return direction;
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
        climbBackMotor.goDown();
        driveMotor.backward();
        Delay.msDelay(2000);
        driveMotor.stop();
        climbBackMotor.goUp();
        // back wheels are up
        driveMotor.backward();
        Delay.msDelay(2000);
        driveMotor.stop();
        climbFrontMotor.goDown();
        driveMotor.backward();
        Delay.msDelay(1000);
        driveMotor.stop();
        climbFrontMotor.goUp();
        // front wheels are up
    }
}
