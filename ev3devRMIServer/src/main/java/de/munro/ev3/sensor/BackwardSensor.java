package de.munro.ev3.sensor;

import de.munro.ev3.rmi.EV3devConstants;
import ev3dev.sensors.ev3.EV3TouchSensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackwardSensor extends Sensor {
    private static final Logger LOG = LoggerFactory.getLogger(BackwardSensor.class);

    private EV3TouchSensor sensor;

    public BackwardSensor() {
        sensor = new EV3TouchSensor(EV3devConstants.BACKWARD_SENSOR_PORT);
    }

    public EV3TouchSensor getSensor() {
        return sensor;
    }

    @Override
    public void run() {
        LOG.info(Thread.currentThread().getName()+" started");
        listenTouchSensor(sensor, LOG);
    }
}
