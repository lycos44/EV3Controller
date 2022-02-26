package de.munro.ev3.controller;

import de.munro.ev3.data.MotorData;
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
        getMotor().getMotorData().setMotorStatus(MotorData.MotorStatus.running);
        log.debug("motor: {} {}", getMotor().getMotorType(), getMotor().getMotorData().getMotorStatus());

        do {
            synchronized (getMotor().getMotorData()) {
                getMotor().getMotorData().wait();
                log.debug("run {}", getMotor().getMotorData().getCommand());
                if (getMotor().getMotorData().getMotorStatus() == MotorData.MotorStatus.running) {
                    getMotor().takeAction();
                }
            }
        } while(getMotor().getMotorData().getMotorStatus() == MotorData.MotorStatus.running);

        getMotor().getMotorData().setMotorStatus(MotorData.MotorStatus.stopped);
        log.info("{} stopped", this.getMotor().getMotorType());
        getMotor().stop();
        setMotor(null);
    }

    /**
     * Gets the current motor instance
     * @return motor
     */
    public Motor getMotor() {
        return motor;
    }

    /**
     * Sets the motor
     * @param motor motor
     */
    public void setMotor(Motor motor) {
        this.motor = motor;
    }
}
