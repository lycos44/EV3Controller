package de.munro.ev3.motor;

import de.munro.ev3.rmi.EV3devConstants;
import de.munro.ev3.threadpool.Task;
import de.munro.ev3.threadpool.ThreadPoolManager;
import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import lejos.utility.Delay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClimbMotor extends Motor {
    private static final Logger LOG = LoggerFactory.getLogger(ClimbMotor.class);
    private static final int MOTOR_SPEED = 100;

    private BaseRegulatedMotor motor;
    private ThreadPoolManager threadPoolManager;

    public ClimbMotor(ThreadPoolManager threadPoolManager) {
        super(EV3devConstants.CLIMB_MOTOR_PORT, Polarity.NORMAL, Task.MotorType.climb);
        this.motor = new EV3LargeRegulatedMotor(EV3devConstants.CLIMB_MOTOR_PORT);
        this.threadPoolManager = threadPoolManager;
    }

    public void initialize() throws EV3MotorInitializationException {
    }

    @Override
    public int getSpeed() {
        return MOTOR_SPEED;
    }

    @Override
    public BaseRegulatedMotor getMotor() {
        return motor;
    }

    @Override
    public void init() {
        LOG.debug("init()");
//        backwardTillStalled();
//        getMotor().resetTachoCount();
//        LOG.debug("homePosition: {}", homePosition);
//        forwardTillStalled();
//        climbPosition = getMotor().getTachoCount();
//        LOG.debug("climbPosition: {}", climbPosition);
//        getMotor().rotateTo(homePosition);
//        LOG.debug("set home: {}", getMotor().getTachoCount());
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
