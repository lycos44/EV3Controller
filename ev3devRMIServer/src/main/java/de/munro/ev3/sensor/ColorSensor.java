package de.munro.ev3.sensor;

import de.munro.ev3.rmi.EV3devConstants;
import ev3dev.sensors.ev3.EV3ColorSensor;
import lejos.robotics.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColorSensor extends Sensor {
    private static final Logger LOG = LoggerFactory.getLogger(ColorSensor.class);

    private EV3ColorSensor sensor;

    /**
     * Constructor
     */
    public ColorSensor() {
        sensor = new EV3ColorSensor(EV3devConstants.COLOR_SENSOR_PORT);
    }

    /**
     * @link Sensor#getSensor
     */
    @Override
    public EV3ColorSensor getSensor() {
        return sensor;
    }

    /**
     * @link EV3ColorSensor#getColorID
     */
    public int getColorID() {
        return sensor.getColorID();
    }

    /**
     * @link Object#toString
     */
    @Override
    public String toString() {
        switch (getColorID()) {
            case Color.NONE: return "none";
            case Color.BLACK: return "black";
            case Color.BLUE: return "blue";
            case Color.GREEN: return "green";
            case Color.YELLOW: return "yellow";
            case Color.RED: return "red";
            case Color.WHITE: return "white";
            case Color.BROWN: return "brown";
        }
        return "undefined";
    }
}
