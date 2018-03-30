package de.munro.ev3.sensor;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ColorSensorTest {

    @Test
    public void isInitialized() {
        assertThat(ColorSensor.isInitialized(), is(false));
    }
}
