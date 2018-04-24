package de.munro.ev3.sensor;

import de.munro.ev3.rmi.EV3devConstants;
import ev3dev.sensors.ev3.EV3TouchSensor;
import lejos.hardware.port.Port;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackwardSensor extends Sensor {
    private static final Logger LOG = LoggerFactory.getLogger(BackwardSensor.class);

    private EV3TouchSensor touchSensor;
    private final Port port = EV3devConstants.BACKWARD_SENSOR_PORT;

    public BackwardSensor() {
        touchSensor = createSensor(port);
        if (null == this.touchSensor) {
            touchSensor = createSensor(port);
        }
    }

    protected EV3TouchSensor createSensor(Port port) {
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

    public boolean isInitialized() {
        LOG.debug("touchSensor {}", touchSensor);
        return null != touchSensor;
    }

    @Override
    public void run() {
        LOG.info(Thread.currentThread().getName()+" started");
        listenTouchSensor(touchSensor, LOG);
    }
}
