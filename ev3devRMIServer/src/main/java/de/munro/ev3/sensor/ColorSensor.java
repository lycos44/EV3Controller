package de.munro.ev3.sensor;

import de.munro.ev3.rmi.EV3devConstants;
import ev3dev.sensors.ev3.EV3ColorSensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColorSensor extends Sensor {
    private static final Logger LOG = LoggerFactory.getLogger(ColorSensor.class);

    private EV3ColorSensor sensor;

    public ColorSensor() {
        sensor = new EV3ColorSensor(EV3devConstants.COLOR_SENSOR_PORT);
    }

    @Override
    public EV3ColorSensor getSensor() {
        return sensor;
    }
}
