package de.munro.ev3.sensor;

import ev3dev.sensors.ev3.EV3TouchSensor;
import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class BackwardSensorTest {
    @Test
    public void isPressed() {
        BackwardSensor backwardSensor = Mockito.mock(BackwardSensor.class);
        doCallRealMethod().when(backwardSensor).isPressed();
        EV3TouchSensor touchSensor = Mockito.mock(EV3TouchSensor.class);
        when(backwardSensor.getSensor()).thenReturn(touchSensor);
        when(touchSensor.isPressed()).thenReturn(true);

        assertThat(backwardSensor.isPressed(), is(true));

        verify(backwardSensor).getSensor();
        verify(touchSensor).isPressed();
    }
}