package de.munro.ev3.sensor;

import ev3dev.sensors.ev3.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

public class DistanceSensorTest {

    @Test
    public void getDistance() {
        DistanceSensor distanceSensor = Mockito.mock(DistanceSensor.class);
        doCallRealMethod().when(distanceSensor).getDistance();
        EV3UltrasonicSensor ev3UltrasonicSensor = Mockito.mock(EV3UltrasonicSensor.class);
        SampleProvider sampleProvider = Mockito.mock(SampleProvider.class);
        when(sampleProvider.sampleSize()).thenReturn(1);
        when(ev3UltrasonicSensor.getDistanceMode()).thenReturn(sampleProvider);
        when(distanceSensor.getSensor()).thenReturn(ev3UltrasonicSensor);

        assertThat(distanceSensor.getDistance(), is(0));
    }
}