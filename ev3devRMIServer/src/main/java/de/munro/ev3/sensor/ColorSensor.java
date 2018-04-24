package de.munro.ev3.sensor;

import de.munro.ev3.rmi.EV3devConstants;
import ev3dev.sensors.ev3.EV3ColorSensor;
import lejos.hardware.port.Port;
import lejos.utility.Delay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.munro.ev3.rmi.EV3devConstants.COLOR_SENSOR_PORT;

public class ColorSensor extends Sensor {
    private static final Logger LOG = LoggerFactory.getLogger(ColorSensor.class);

    private EV3ColorSensor colorSensor;
    private final Port port = EV3devConstants.COLOR_SENSOR_PORT;

    public ColorSensor() {
        colorSensor = createSensor(port);
        if (null == this.colorSensor) {
            colorSensor = createSensor(port);
        }
    }

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

    public boolean isInitialized() {
        LOG.debug("colorSensor {}", colorSensor);
        return null != colorSensor;
    }

    @Override
    public void run() {
        LOG.info(Thread.currentThread().getName()+" started");
        while ( !Thread.interrupted() ) {
            Delay.msDelay(1000);
        }
    }
}
