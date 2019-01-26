package de.munro.ev3.sensor;

import ev3dev.sensors.BaseSensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Sensor {
    private static final Logger LOG = LoggerFactory.getLogger(Sensor.class);

    public abstract BaseSensor getSensor();

    public boolean isInitialized() {
        LOG.debug("isInitialized()");
        return null != getSensor();
    }
}
