package de.munro.ev3.sensor;

import ev3dev.sensors.ev3.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GyroSensor extends Sensor {

    private final EV3GyroSensor sensor;

    /**
     * Constructor
     */
    public GyroSensor() {
        sensor = new EV3GyroSensor(SensorType.gyro.getPort());
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

    /**
     * @link Object#toString
     */
    @Override
    public String toString() {
        return Integer.toString(getGyroAngleRate());
    }
}
