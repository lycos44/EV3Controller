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
import lejos.robotics.Color;
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

    /**
     * Constructor
     */
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

    /**
     * main thread methode: initialize, polling for events
     * @param ev3devRMIServer server instance
     */
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

        // the EV3devStatus is checked, to decide what to do next
        while (true) {
            // what about are battery, is it running low?
            double voltage = Math.round(10.0 * Battery.getInstance().getVoltage()) / 10.0;
            if (voltage <= MINIMUM_VOLTAGE) {
                LOG.error("EV3 battery is running low: {}, CANNOT PROCEED, PLEASE RECHARGE!", voltage);
                System.exit(EV3devConstants.SYSTEM_UNEXPECTED_ERROR);
            }

            // the show is over?
            if (ev3devRMIServer.getEv3devStatus().isToBeStopped()) {
                LOG.info("needs to be stopped: {}", ev3devRMIServer.getEv3devStatus().isToBeStopped());
                break;
            }

            // reset
            if (ev3devRMIServer.getEv3devStatus().isReset()) {
                LOG.info("reset steering motor properties");
                steeringMotor.reset();
                ev3devRMIServer.getEv3devStatus().setReset(false);
            }

            // test
            if (ev3devRMIServer.getEv3devStatus().isTest()) {
                LOG.info("test");
                doTheTest();
                ev3devRMIServer.getEv3devStatus().setTest(false);
            }

            // touched barrier in the rear
            if (backwardSensor.isPressed()) {
                LOG.debug("*********** Backward: touch ***********");
                backwardWithClimb();
            }

            // noticed barrier in front, less than 30 to go
            if (distanceSensor.getDistance() <= 30
                    && ev3devRMIServer.getEv3devStatus().getDirection() == EV3devStatus.Direction.forward
                    && driveMotor.getSpeed() == DriveMotor.MOTOR_SPEED_NORMAL
            ) {
                LOG.debug("*********** Forward: short distance ***********");
                driveMotor.setSpeed(DriveMotor.MOTOR_SPEED_SLOW);
            }

            // noticed barrier in front, less than 20 to go
            if (distanceSensor.getDistance() <= 20
                    && ev3devRMIServer.getEv3devStatus().getDirection() == EV3devStatus.Direction.forward
            ) {
                LOG.debug("*********** Forward: no distance ***********");
                ev3devRMIServer.getEv3devStatus().setDirection(EV3devStatus.Direction.stop);
            }

            // no floor detected in front
            if ((colorSensor.toString().equals("none") || colorSensor.toString().equals("undefined")) && ev3devRMIServer.getEv3devStatus().getDirection() == EV3devStatus.Direction.forward) {
                LOG.debug("*********** Forward: unknown surface ***********");
                ev3devRMIServer.getEv3devStatus().setDirection(EV3devStatus.Direction.stop);
            }

            LOG.info("motor status: ({})", ev3devRMIServer.getEv3devStatus());
            LOG.info("sensor status: ({}, {}, {}, {}, {}, {})", this.driveMotor.getTachoCount(), Battery.getInstance().getVoltage(), distanceSensor, backwardSensor, gyroSensor, colorSensor);

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

    private void doTheTest() {
        this.driveMotor.setSpeed(100);
        this.driveMotor.forward();
        while(this.colorSensor.getColorID() != Color.BLACK) {
            Delay.msDelay(DELAY_PERIOD_SHORT);
        }
        this.driveMotor.stop();
    }

    /**
     * stop all motors
     */
    void stop() {
        this.driveMotor.stop();
        this.climbBackMotor.stop();
        this.steeringMotor.stop();
        this.climbFrontMotor.stop();
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
        climbBackMotor.goDown();
        Delay.msDelay(2000);
        climbBackMotor.goUp();
        // back is up
        Delay.msDelay(2000);
        climbFrontMotor.goDown();
        Delay.msDelay(1000);
        climbFrontMotor.goUp();
        // front is up
    }
}
