package de.munro.ev3.sensor;

import de.munro.ev3.rmi.EV3devConstants;
import ev3dev.sensors.ev3.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DistanceSensor extends Sensor {
    private static final Logger LOG = LoggerFactory.getLogger(DistanceSensor.class);

    private EV3UltrasonicSensor sensor;

    /**
     * Constructor
     */
    public DistanceSensor() {
        sensor = new EV3UltrasonicSensor(EV3devConstants.DISTANCE_SENSOR_PORT);
    }

    /**
     * @link Sensor#getSensor
     */
    @Override
    public EV3UltrasonicSensor getSensor() {
        return sensor;
    }

    /**
     * provides the distance currently measured
     * @return distance
     */
    public int getDistance() {
        SampleProvider sampleProvider = getSensor().getDistanceMode();
        float [] sample = new float[sampleProvider.sampleSize()];
        sampleProvider.fetchSample(sample, 0);
        // minimum distance 0, enough to avoid crash
        return  (int)sample[0];
    }

    @Override
    public String toString() {
        return Integer.toString(getDistance());
    }
}
