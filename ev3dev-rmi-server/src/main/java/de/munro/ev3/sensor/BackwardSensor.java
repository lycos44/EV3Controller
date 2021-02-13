package de.munro.ev3.sensor;

import ev3dev.sensors.ev3.EV3TouchSensor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BackwardSensor extends Sensor {

    private EV3TouchSensor sensor;

    /**
     * constructor
     */
    public BackwardSensor() {
        sensor = new EV3TouchSensor(SensorType.backward.getPort());
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

    /**
     * @link Object#toString
     */
    @Override
    public String toString() {
        return Boolean.toString(isPressed());
    }
}
