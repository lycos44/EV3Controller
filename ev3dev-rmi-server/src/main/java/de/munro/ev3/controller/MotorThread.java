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
        log.debug("run motor: {}", getMotor());
        while(getMotor().getMotorData().isRunning()) {
//            getMotor().logStatus();
            getMotor().takeAction();

            Thread.yield();
        }

        log.info("{} stopped", this.getMotor().getMotorType());
    }

    /**
     * get the current motor instance
     * @return motor
     */
    public Motor getMotor() {
        return motor;
    }
}
