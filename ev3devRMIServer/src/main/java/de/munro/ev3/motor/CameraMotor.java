package de.munro.ev3.motor;

import de.munro.ev3.rmi.EV3devConstants;
import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import lejos.utility.Delay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CameraMotor extends Motor {
    private static final Logger LOG = LoggerFactory.getLogger(CameraMotor.class);

    private static CameraMotor instance;
    private EV3LargeRegulatedMotor motor;

    private final int homePosition = 0;
    private int leftmostPosition = 0;
    private int rightmostPosition = 0;

    public static CameraMotor getInstance() {
        if (null == instance) {
            instance = new CameraMotor();
        }
        return instance;
    }

    private CameraMotor() {
        this.motor = createMotor(EV3devConstants.CAMERA_MOTOR_PORT, Polarity.INVERSED);
        if (null == this.motor) {
            this.motor = createMotor(EV3devConstants.CAMERA_MOTOR_PORT, Polarity.INVERSED);
        }
    }

    public static boolean isInitialized() {
        LOG.debug("motor {}", instance);
        return null != instance && null != instance.motor;
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

        getMotor().resetTachoCount();
        LOG.debug("(left, home, right): ({}, {}, {})", leftmostPosition, homePosition, rightmostPosition);
    }

    public int getLeftmostPosition() {
        return leftmostPosition;
    }

    public int getRightmostPosition() {
        return  rightmostPosition;
    }
}
