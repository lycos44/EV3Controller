package de.munro.ev3.sensor;

import de.munro.ev3.rmi.EV3devConstants;
import ev3dev.sensors.ev3.EV3TouchSensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackwardSensor extends Sensor {
    private static final Logger LOG = LoggerFactory.getLogger(BackwardSensor.class);

    private EV3TouchSensor sensor;

    /**
     * constructor
     */
    public BackwardSensor() {
        sensor = new EV3TouchSensor(EV3devConstants.BACKWARD_SENSOR_PORT);
    }

    /**
     * @link Sensor#getSensor
     */
    @Override
    public EV3TouchSensor getSensor() {
        return sensor;
    }

    /**
     * @link EV3TouchSensor#isPressed
     */
    public boolean isPressed() {
        return getSensor().isPressed();
    }

    @Override
    public String toString() {
        return Boolean.toString(isPressed());
    }
}
