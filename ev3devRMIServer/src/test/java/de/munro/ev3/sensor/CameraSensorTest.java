package de.munro.ev3.sensor;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CameraSensorTest {

    @Test
    public void isInitialized() {
        assertThat(CameraSensor.isInitialized(), is(false));
    }
}