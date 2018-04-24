package de.munro.ev3.motor;

import de.munro.ev3.rmi.EV3devConstants;
import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClimbMotor extends Motor {
    private static final Logger LOG = LoggerFactory.getLogger(ClimbMotor.class);
    private static final int MOTOR_SPEED = 100;

    private EV3LargeRegulatedMotor motor;
    private final Port port = EV3devConstants.CLIMB_MOTOR_PORT;
    private final Polarity polarity = Polarity.NORMAL;

    private final int homePosition = 0;
    private int climbPosition;

    public ClimbMotor() {
        this.motor = createMotor(port, polarity);
        if (null == this.motor) {
            this.motor = createMotor(port, polarity);
        }
    }

    @Override
    public int getSpeed() {
        return MOTOR_SPEED;
    }

    @Override
    public EV3LargeRegulatedMotor getMotor() {
        return motor;
    }

    @Override
    public void init() {
        LOG.debug("init()");
        backwardTillStalled();
        getMotor().resetTachoCount();
        LOG.debug("homePosition: {}", homePosition);
        forwardTillStalled();
        climbPosition = getMotor().getTachoCount();
        LOG.debug("climbPosition: {}", climbPosition);
        getMotor().rotateTo(homePosition);
        LOG.debug("set home: {}", getMotor().getTachoCount());
    }

    @Override
    public void run() {

    }
}
