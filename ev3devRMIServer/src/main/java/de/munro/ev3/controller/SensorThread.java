package de.munro.ev3.controller;

import de.munro.ev3.logger.SensorLogger;
import de.munro.ev3.sensor.BackwardSensor;
import de.munro.ev3.sensor.ColorSensor;
import de.munro.ev3.sensor.DistanceSensor;
import de.munro.ev3.sensor.GyroSensor;
import lejos.utility.Delay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.munro.ev3.rmi.EV3devConstants.DELAY_PERIOD_SHORT;

public class SensorThread extends Thread {
    private static final Logger LOG = LoggerFactory.getLogger(SensorThread.class);

    private static final String THREAD_NAME = "sensors";
    private BackwardSensor backwardSensor;
    private GyroSensor gyroSensor;
    private ColorSensor colorSensor;
    private DistanceSensor distanceSensor;

    private final SensorLogger sensorLogger;

    /**
     * constructor
     *
     * @param sensorLogger logger instance
     */
    public SensorThread(SensorLogger sensorLogger) {
        this.sensorLogger = sensorLogger;
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

        while (sensorLogger.isRunning()) {
            logStatus();
            Delay.msDelay(DELAY_PERIOD_SHORT);
        }

        LOG.info("{} stopped", this.getClass().getSimpleName());
    }

    /**
     * print log message with current status
     */
    private void logStatus() {
        LOG.debug("(back, gyro, color, dist):({}, {}, {}, {})", backwardSensor.isPressed(), gyroSensor.getGyroAngleRate(), colorSensor.toString(), distanceSensor.getDistance());
    }
}
