package de.munro.ev3.controller;

import de.munro.ev3.motor.Motor;
import lejos.utility.Delay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.munro.ev3.rmi.EV3devConstants.DELAY_PERIOD_SHORT;

public class MotorThread extends Thread {
    private static final Logger LOG = LoggerFactory.getLogger(MotorThread.class);

    private Motor motor;

    /**
     * constructor
     * @param motor current motor
     */
    public MotorThread(Motor motor) {
        this.motor = motor;
        this.setName(motor.getMotorType().name());
    }

    /**
     * @link Thread#run()
     */
    @Override
    public void run() {
        getMotor().init();

        while(getMotor().isRunning()) {
            getMotor().logStatus();
            getMotor().readLoggerData();
            Delay.msDelay(DELAY_PERIOD_SHORT);
        }

        LOG.info("{} stopped", this.getClass().getSimpleName());
    }

    /**
     * get the current motor instance
     * @return motor
     */
    public Motor getMotor() {
        return motor;
    }
}
