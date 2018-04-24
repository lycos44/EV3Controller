package de.munro.ev3.motor;

import de.munro.ev3.rmi.EV3devConstants;
import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.utility.Delay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SteeringMotor extends Motor {
    private static final Logger LOG = LoggerFactory.getLogger(SteeringMotor.class);
    private static final int MOTOR_SPEED = 200;

    private EV3LargeRegulatedMotor motor;
    private final Port port = EV3devConstants.STEERING_MOTOR_PORT;
    private final Polarity polarity = Polarity.NORMAL;

    private int leftmostPosition = 0;
    private int rightmostPosition = 0;

    public SteeringMotor() {
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
        Delay.msDelay(1000);
    }

    @Override
    public void run() {

    }
}
