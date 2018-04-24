package de.munro.ev3.sensor;

import de.munro.ev3.rmi.EV3devConstants;
import de.munro.ev3.threadpool.Event;
import ev3dev.sensors.ev3.EV3UltrasonicSensor;
import lejos.hardware.port.Port;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.munro.ev3.rmi.EV3devConstants.DISTANCE_SENSOR_PORT;

public class DistanceSensor extends Sensor {
    private static final Logger LOG = LoggerFactory.getLogger(DistanceSensor.class);

    private EV3UltrasonicSensor ultrasonicSensor;
    private final Port port = EV3devConstants.DISTANCE_SENSOR_PORT;

    public DistanceSensor() {
        ultrasonicSensor = createSensor(port);
        if (null == this.ultrasonicSensor) {
            ultrasonicSensor = createSensor(port);
        }
    }

    protected EV3UltrasonicSensor createSensor(Port port) {
        LOG.debug("createSensor({})", port);
        EV3UltrasonicSensor sensor = null;
        try {
            sensor = new EV3UltrasonicSensor(DISTANCE_SENSOR_PORT);
            LOG.debug("distanceSensor {}", sensor);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return sensor;
    }

    public boolean isInitialized() {
        LOG.debug("distanceSensor {}", ultrasonicSensor);
        return null != ultrasonicSensor;
    }

    @Override
    public void run() {
        LOG.info(Thread.currentThread().getName()+" started");
        final SampleProvider sampleProvider = ultrasonicSensor.getDistanceMode();
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
