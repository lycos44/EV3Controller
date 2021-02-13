package de.munro.ev3.controller;

import de.munro.ev3.data.SensorData;
import de.munro.ev3.sensor.BackwardSensor;
import de.munro.ev3.sensor.ColorSensor;
import de.munro.ev3.sensor.DistanceSensor;
import de.munro.ev3.sensor.GyroSensor;
import lejos.utility.Delay;
import lombok.extern.slf4j.Slf4j;

import static de.munro.ev3.rmi.EV3devConstants.DELAY_PERIOD_SHORT;

@Slf4j
public class SensorThread extends Thread {

    private static final String THREAD_NAME = "sensors";
    private BackwardSensor backwardSensor;
    private GyroSensor gyroSensor;
    private ColorSensor colorSensor;
    private DistanceSensor distanceSensor;

    private final SensorData sensorData;

    /**
     * constructor
     *
     * @param sensorData sensorData to set
     */
    public SensorThread(SensorData sensorData) {
        this.sensorData = sensorData;
        this.setName(THREAD_NAME);
    }

    /**
     * @link Thread#run()
     */
    @Override
    public void run() {
        this.backwardSensor = new BackwardSensor();
        this.gyroSensor = new GyroSensor();
        this.colorSensor = new ColorSensor();
        this.distanceSensor = new DistanceSensor();

        while (sensorData.isRunning()) {
            logStatus();
            Delay.msDelay(DELAY_PERIOD_SHORT);
        }

        log.info("{} stopped", this.getClass().getSimpleName());
    }

    /**
     * print log message with current status
     */
    private void logStatus() {
        log.info("(back, gyro, color, dist):({}, {}, {}, {})", backwardSensor.isPressed(), gyroSensor.getGyroAngleRate(), colorSensor.toString(), distanceSensor.getDistance());
    }
}
