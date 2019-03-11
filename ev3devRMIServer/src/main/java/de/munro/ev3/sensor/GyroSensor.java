package de.munro.ev3.sensor;

import de.munro.ev3.rmi.EV3devConstants;
import ev3dev.sensors.ev3.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GyroSensor extends Sensor {
    private static final Logger LOG = LoggerFactory.getLogger(GyroSensor.class);

    private EV3GyroSensor sensor;

    /**
     * Constructor
     */
    public GyroSensor() {
        sensor = new EV3GyroSensor(EV3devConstants.GYRO_SENSOR_PORT);
        sensor.reset();
    }

    /**
     * @link Sensor#getSensor
     */
    @Override
    public EV3GyroSensor getSensor() {
        return sensor;
    }

    /**
     * provides the rate currently measured
     * @return distance
     */
    public int getGyroAngleRate() {
        final SampleProvider sampleProvider = getSensor().getRateMode();
        float [] sample = new float[sampleProvider.sampleSize()];
        sampleProvider.fetchSample(sample, 0);
        return  (int)sample[0];
    }
}
