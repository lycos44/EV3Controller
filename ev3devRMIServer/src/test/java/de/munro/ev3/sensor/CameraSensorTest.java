package de.munro.ev3.sensor;

import ev3dev.sensors.ev3.EV3TouchSensor;
import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class CameraSensorTest {
    @Test
    public void isPressed() {
        CameraSensor cameraSensor = Mockito.mock(CameraSensor.class);
        doCallRealMethod().when(cameraSensor).isPressed();
        EV3TouchSensor touchSensor = Mockito.mock(EV3TouchSensor.class);
        when(cameraSensor.getSensor()).thenReturn(touchSensor);
        when(touchSensor.isPressed()).thenReturn(true);

        assertThat(cameraSensor.isPressed(), is(true));

        verify(cameraSensor).getSensor();
        verify(touchSensor).isPressed();
    }
}