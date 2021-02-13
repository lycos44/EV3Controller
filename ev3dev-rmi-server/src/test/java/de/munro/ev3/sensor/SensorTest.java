package de.munro.ev3.sensor;

import ev3dev.sensors.BaseSensor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SensorTest {

    @Test
    public void isInitialized() {
        Sensor sensor = Mockito.mock(Sensor.class);
        doCallRealMethod().when(sensor).isInitialized();
        BaseSensor baseSensor = Mockito.mock(BaseSensor.class);
        when(sensor.getSensor()).thenReturn(baseSensor);

        assertThat(sensor.isInitialized(), is(true));

        verify(sensor).getSensor();
    }
}