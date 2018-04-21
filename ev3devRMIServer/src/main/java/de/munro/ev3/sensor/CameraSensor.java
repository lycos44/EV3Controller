package de.munro.ev3.sensor;

import ev3dev.sensors.ev3.EV3TouchSensor;
import lejos.hardware.port.Port;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.munro.ev3.rmi.EV3devConstants.CAMERA_SENSOR_PORT;

public class CameraSensor extends Sensor {
    private static final Logger LOG = LoggerFactory.getLogger(CameraSensor.class);

    private static CameraSensor instance;
    private EV3TouchSensor touchSensor;
    private final Port port = CAMERA_SENSOR_PORT;

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

    private CameraSensor() {
        touchSensor = createSensor();
        if (null == this.touchSensor) {
            touchSensor = createSensor();
        }
    }

    public static CameraSensor getInstance() {
        if (null == instance) {
            instance = new CameraSensor();
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
