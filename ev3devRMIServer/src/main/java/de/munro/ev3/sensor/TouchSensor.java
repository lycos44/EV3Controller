package de.munro.ev3.sensor;

import ev3dev.sensors.ev3.EV3TouchSensor;
import lejos.hardware.port.Port;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.munro.ev3.rmi.EV3devConstants.TOUCH_SENSOR_PORT;

public class TouchSensor extends Sensor {
    private static final Logger LOG = LoggerFactory.getLogger(TouchSensor.class);

    private static TouchSensor instance;
    private EV3TouchSensor touchSensor;

    protected EV3TouchSensor createSensor(Port port) {
        LOG.debug("createSensor({})", port);
        EV3TouchSensor sensor = null;
        try {
            sensor = new EV3TouchSensor(TOUCH_SENSOR_PORT);
            LOG.debug("sensor {}", sensor);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return sensor;
    }

    private TouchSensor() {
        touchSensor = createSensor(TOUCH_SENSOR_PORT);
        if (null == this.touchSensor) {
            touchSensor = createSensor(TOUCH_SENSOR_PORT);
        }
    }

    public static TouchSensor getInstance() {
        if (null == instance) {
            instance = new TouchSensor();
        }
        return instance;
    }

    public static boolean isInitialized() {
        LOG.debug("touchSensor {}", instance);
        return null != instance && null != instance.touchSensor;
    }

    public boolean isPressed () {
        return touchSensor.isPressed();
    }
}
