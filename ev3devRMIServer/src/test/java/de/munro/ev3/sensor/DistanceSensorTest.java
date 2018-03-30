package de.munro.ev3.sensor;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DistanceSensorTest {

    @Test
    public void isInitialized() {
        assertThat(DistanceSensor.isInitialized(), is(false));
    }
}