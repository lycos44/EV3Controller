package de.munro.ev3.sensor;

import ev3dev.sensors.ev3.EV3TouchSensor;
import lejos.hardware.port.Port;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.munro.ev3.rmi.EV3devConstants.BACKWARD_SENSOR_PORT;

public class BackwardSensor extends Sensor {
    private static final Logger LOG = LoggerFactory.getLogger(BackwardSensor.class);

    private static BackwardSensor instance;
    private EV3TouchSensor touchSensor;
    private final Port port = BACKWARD_SENSOR_PORT;

    protected EV3TouchSensor createSensor() {
        LOG.debug("createSensor({})", port);
        EV3TouchSensor sensor = null;
        try {
            sensor = new EV3TouchSensor(port);
            LOG.debug("sensor {}", sensor);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return sensor;
    }

    private BackwardSensor() {
        touchSensor = createSensor();
        if (null == this.touchSensor) {
            touchSensor = createSensor();
        }
    }

    public static BackwardSensor getInstance() {
        if (null == instance) {
            instance = new BackwardSensor();
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
