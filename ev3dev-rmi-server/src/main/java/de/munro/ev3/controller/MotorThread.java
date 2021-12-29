package de.munro.ev3.controller;

import de.munro.ev3.motor.Motor;
import lombok.SneakyThrows;
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
    @SneakyThrows
    @Override
    public void run() {
        getMotor().init();
        log.debug("run motor: {}", getMotor());

        action: while(true) {
            synchronized (getMotor().getMotorData()) {
                getMotor().getMotorData().wait();
                if (getMotor().getMotorData().isToBeStopped()) {
                    break action;
                }
                getMotor().takeAction();
            }
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
