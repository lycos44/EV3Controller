package de.munro.ev3.sensor;

import de.munro.ev3.rmi.EV3devConstants;
import ev3dev.sensors.ev3.EV3TouchSensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CameraSensor extends Sensor {
    private static final Logger LOG = LoggerFactory.getLogger(CameraSensor.class);

    private EV3TouchSensor sensor;

    public CameraSensor() {
        sensor = new EV3TouchSensor(EV3devConstants.CAMERA_SENSOR_PORT);
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
