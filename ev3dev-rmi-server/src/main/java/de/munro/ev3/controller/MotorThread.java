package de.munro.ev3.controller;

import de.munro.ev3.motor.Motor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MotorThread extends Thread {

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

        while(getMotor().getMotorData().isRunning()) {
            getMotor().logStatus();
            getMotor().workOutMotorData();

            Thread.yield();
        }

        log.info("{} stopped", this.getClass().getSimpleName());
    }

    /**
     * get the current motor instance
     * @return motor
     */
    public Motor getMotor() {
        return motor;
    }
}
