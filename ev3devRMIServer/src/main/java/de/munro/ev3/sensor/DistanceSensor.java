package de.munro.ev3.sensor;

import de.munro.ev3.rmi.EV3devConstants;
import ev3dev.sensors.ev3.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DistanceSensor extends Sensor {
    private static final Logger LOG = LoggerFactory.getLogger(DistanceSensor.class);

    private EV3UltrasonicSensor sensor;
    private SampleProvider sampleProvider;

    public DistanceSensor() {
        sensor = new EV3UltrasonicSensor(EV3devConstants.DISTANCE_SENSOR_PORT);
        sampleProvider = sensor.getDistanceMode();
    }

    @Override
    public EV3UltrasonicSensor getSensor() {
        return sensor;
    }

    public int getDistance() {
        float [] sample = new float[sampleProvider.sampleSize()];
        sampleProvider.fetchSample(sample, 0);
        return  (int)sample[0];
    }
}
