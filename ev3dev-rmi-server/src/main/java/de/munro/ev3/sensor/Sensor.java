package de.munro.ev3.sensor;

import ev3dev.sensors.BaseSensor;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Sensor {

    public enum SensorType {
        backward(SensorPort.S1),
        gyro(SensorPort.S2),
        color(SensorPort.S3),
        distance(SensorPort.S4);

        private final Port port;

        SensorType(Port port) {
            this.port = port;
        }

        public Port getPort() {
            return port;
        }
    }

    /**
     * get current sensor instance
     * @return sensor instance
     */
    public abstract BaseSensor getSensor();

    /**
     * get initialized status
     * @return sensor is initialized
     */
    public boolean isInitialized() {
        log.debug("isInitialized()");
        return null != getSensor();
    }
}
