package de.munro.ev3.sensor;

import ev3dev.sensors.ev3.EV3UltrasonicSensor;
import lejos.hardware.port.Port;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.munro.ev3.rmi.EV3devConstants.DISTANCE_SENSOR_PORT;

public class DistanceSensor extends Sensor {
    private static final Logger LOG = LoggerFactory.getLogger(DistanceSensor.class);

    private static DistanceSensor instance;
    private EV3UltrasonicSensor ultrasonicSensor;

    protected EV3UltrasonicSensor createSensor(Port port) {
        LOG.debug("createSensor({})", port);
        EV3UltrasonicSensor sensor = null;
        try {
            sensor = new EV3UltrasonicSensor(DISTANCE_SENSOR_PORT);
            LOG.debug("sensor {}", sensor);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return sensor;
    }

    private DistanceSensor() {
        ultrasonicSensor = createSensor(DISTANCE_SENSOR_PORT);
        if (null == this.ultrasonicSensor) {
            ultrasonicSensor = createSensor(DISTANCE_SENSOR_PORT);
        }
    }

    public static DistanceSensor getInstance() {
        if (null == instance) {
            instance = new DistanceSensor();
        }
        return instance;
    }

    public static boolean isInitialized() {
        LOG.debug("distanceSensor {}", instance);
        return null != instance && null != instance.ultrasonicSensor;
    }
}
