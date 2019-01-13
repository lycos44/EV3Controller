package de.munro.ev3.motor;

import de.munro.ev3.rmi.EV3devConstants;
import de.munro.ev3.threadpool.Task;
import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3MediumRegulatedMotor;
import lejos.utility.Delay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SteeringMotor extends Motor {
    private static final Logger LOG = LoggerFactory.getLogger(SteeringMotor.class);
    private static final int MOTOR_SPEED = 200;

    private BaseRegulatedMotor motor;

    private int homePosition = 0;
    private int leftmostPosition = 0;
    private int rightmostPosition = 0;

    public SteeringMotor() {
        super(EV3devConstants.STEERING_MOTOR_PORT, Polarity.INVERSED, Task.MotorType.steering);
        this.motor = new EV3MediumRegulatedMotor(EV3devConstants.STEERING_MOTOR_PORT);
        this.motor.setSpeed(MOTOR_SPEED);
    }

    @Override
    public int getSpeed() {
        return getMotor().getSpeed();
    }

    @Override
    BaseRegulatedMotor getMotor() {
        return motor;
    }

    @Override
    public void init() {
        int start = getMotor().getTachoCount();
        LOG.debug("start: {}", start);
        backwardTillStalled();
        getMotor().resetTachoCount();
        int left = getMotor().getTachoCount();
        LOG.debug("left: {}", left);
        forwardTillStalled();
        int right = getMotor().getTachoCount();
        LOG.debug("right: {}", right);
        int home = (left+right)/2;
        leftmostPosition = home;
        rightmostPosition = -home;
        getMotor().rotateTo(home);
        getMotor().resetTachoCount();
        LOG.debug("homePosition: {}", this.getMotor().getTachoCount());
        LOG.debug("leftmostPosition: {}", this.leftmostPosition);
        LOG.debug("rightmostPosition: {}", this.rightmostPosition);
        Delay.msDelay(1000);
        getMotor().rotateTo(60);
        LOG.debug("position: {}", this.getMotor().getTachoCount());
    }

    public void goHome() {
        LOG.debug("goHome()");
        if (getMotor().getTachoCount() != homePosition) {
            getMotor().rotateTo(homePosition);
        }
        LOG.debug("Position: {}", getMotor().getTachoCount());
    }

    public void goLeft() {
        LOG.debug("goLeft()");
        LOG.debug("Position: {}", getMotor().getTachoCount());
        LOG.debug("leftmostPosition: {}", this.leftmostPosition);
        getMotor().rotateTo(leftmostPosition);
        LOG.debug("Position: {}", getMotor().getTachoCount());
    }

    public void goRight() {
        LOG.debug("goRight()");
        goHome();
        getMotor().rotateTo(rightmostPosition);
        LOG.debug("Position: {}", getMotor().getTachoCount());
    }

    @Override
    public void run() {
        LOG.info(Thread.currentThread().getName()+" started");
        while ( !Thread.interrupted() ) {
            Delay.msDelay(1000);
        }
        this.stop();
        LOG.info(Thread.currentThread().getName()+" stopped");
    }
}
