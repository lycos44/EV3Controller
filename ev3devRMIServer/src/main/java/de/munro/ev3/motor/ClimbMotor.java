package de.munro.ev3.motor;

import de.munro.ev3.rmi.EV3devConstants;
import de.munro.ev3.threadpool.Task;
import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClimbMotor extends Motor {
    private static final Logger LOG = LoggerFactory.getLogger(ClimbMotor.class);
    private static final int MOTOR_SPEED = 30;

    private BaseRegulatedMotor motor;

    private int lowestPosition = 0;
    private int highstPosition = 0;

    /**
     * Constructor
     */
    public ClimbMotor() {
        super(EV3devConstants.CLIMB_MOTOR_PORT, Polarity.INVERSED, Task.MotorType.climb);
        this.motor = createMotor(EV3devConstants.CLIMB_MOTOR_PORT);
        this.motor.setSpeed(MOTOR_SPEED);
    }

    private EV3LargeRegulatedMotor createMotor(Port port) {
        try {
            return new EV3LargeRegulatedMotor(port);
        } catch (RuntimeException e) {
            LOG.error("Catch", e);
        }
        return null;
    }

    /**
     * @link Motor#getMotor()
     */
    @Override
    BaseRegulatedMotor getMotor() {
        return motor;
    }

    /**
     * @link Motor#init()
     */
    @Override
    public void init() {
        LOG.debug("init()");
        // search for the position that can be set to zero
        backward();
        while(!getMotor().isStalled()) {
        }
        stop();
        resetTachoCount();
        forward();
        while(!getMotor().isStalled()) {
        }
        stop();
        highstPosition = getTachoCount();
        lowestPosition = 0;
        LOG.debug("(lowest, highest): ({}, {})", lowestPosition, highstPosition);
    }
}
