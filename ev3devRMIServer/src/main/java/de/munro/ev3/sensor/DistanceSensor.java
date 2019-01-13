package de.munro.ev3.sensor;

import de.munro.ev3.rmi.EV3devConstants;
import de.munro.ev3.threadpool.Event;
import ev3dev.sensors.ev3.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DistanceSensor extends Sensor {
    private static final Logger LOG = LoggerFactory.getLogger(DistanceSensor.class);

    private EV3UltrasonicSensor sensor;

    public DistanceSensor() {
        sensor = new EV3UltrasonicSensor(EV3devConstants.DISTANCE_SENSOR_PORT);
    }

    @Override
    public EV3UltrasonicSensor getSensor() {
        return sensor;
    }

    @Override
    public void run() {
        LOG.info(Thread.currentThread().getName()+" started");
        final SampleProvider sampleProvider = getSensor().getDistanceMode();
        int distanceValue;

        while ( !Thread.interrupted() ) {
            float [] sample = new float[sampleProvider.sampleSize()];
            sampleProvider.fetchSample(sample, 0);
            distanceValue = (int)sample[0];
            setEvent(new Event(distanceValue));
//            LOG.debug("Event offered {}", getEvent().toString());
            Delay.msDelay(1000);
        }
    }
}
