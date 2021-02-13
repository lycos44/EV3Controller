package de.munro.ev3.sensor;

import ev3dev.sensors.ev3.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DistanceSensor extends Sensor {

    private EV3UltrasonicSensor sensor;

    /**
     * Constructor
     */
    public DistanceSensor() {
        sensor = new EV3UltrasonicSensor(SensorType.distance.getPort());
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

    /**
     * @link Object#toString
     */
    @Override
    public String toString() {
        return Integer.toString(getDistance());
    }
}
