package de.munro.ev3.sensor;

import de.munro.ev3.rmi.EV3Device;
import de.munro.ev3.threadpool.Event;
import ev3dev.sensors.BaseSensor;
import ev3dev.sensors.ev3.EV3TouchSensor;
import lejos.utility.Delay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Sensor implements EV3Device {
    public static final int TOUCHSENSOR_PRESSED = 1;
    private Event event;

    private static final Logger LOG = LoggerFactory.getLogger(Sensor.class);

    public abstract BaseSensor getSensor();

    public boolean isInitialized() {
        LOG.debug("sensor {}", getSensor());
        return null != getSensor();
    }

    public synchronized Event getEvent() {
        return event;
    }

    public synchronized void setEvent(Event event) {
        this.event = event;
    }

    public void listenTouchSensor(EV3TouchSensor touchSensor, Logger logger) {
        while ( !Thread.interrupted() ) {
            if ( touchSensor.isPressed() && null == getEvent() ) {
                setEvent(new Event(1));
                logger.debug("Event offered: {}", getEvent().toString());
            } else if ( !touchSensor.isPressed() && null != getEvent() && getEvent().isDone() ) {
                logger.debug("Event removed: {}", getEvent().toString());
                setEvent(null);
            }
            Delay.msDelay(100);
        }
    }
}
