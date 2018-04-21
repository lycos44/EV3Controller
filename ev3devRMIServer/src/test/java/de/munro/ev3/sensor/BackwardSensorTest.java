package de.munro.ev3.sensor;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BackwardSensorTest {

    @Test
    public void isInitialized() {
        assertThat(BackwardSensor.isInitialized(), is(false));
    }
}