package de.munro.ev3.sensor;

import ev3dev.sensors.ev3.EV3ColorSensor;
import lejos.hardware.port.Port;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.munro.ev3.rmi.EV3devConstants.COLOR_SENSOR_PORT;

public class ColorSensor extends Sensor {
    private static final Logger LOG = LoggerFactory.getLogger(ColorSensor.class);

    private static ColorSensor instance;
    private EV3ColorSensor colorSensor;

    protected EV3ColorSensor createSensor(Port port) {
        LOG.debug("createSensor({})", port);
        EV3ColorSensor sensor = null;
        try {
            sensor = new EV3ColorSensor(COLOR_SENSOR_PORT);
            LOG.debug("sensor {}", sensor);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return sensor;
    }

    private ColorSensor() {
        colorSensor = createSensor(COLOR_SENSOR_PORT);
        if (null == this.colorSensor) {
            colorSensor = createSensor(COLOR_SENSOR_PORT);
        }
    }

    public static ColorSensor getInstance() {
        if (null == instance) {
            instance = new ColorSensor();
        }
        return instance;
    }

    public static boolean isInitialized() {
        LOG.debug("colorSensor {}", instance);
        return null != instance && null != instance.colorSensor;
    }
}
