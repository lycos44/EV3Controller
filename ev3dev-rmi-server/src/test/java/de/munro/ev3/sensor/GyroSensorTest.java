package de.munro.ev3.sensor;

import ev3dev.sensors.ev3.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

public class GyroSensorTest {

    @Test
    public void getGyroAngleRate() {
        GyroSensor gyroSensor = Mockito.mock(GyroSensor.class);
        doCallRealMethod().when(gyroSensor).getGyroAngleRate();
        EV3GyroSensor ev3GyroSensor = Mockito.mock(EV3GyroSensor.class);
        SampleProvider sampleProvider = Mockito.mock(SampleProvider.class);
        when(sampleProvider.sampleSize()).thenReturn(1);
        when(ev3GyroSensor.getRateMode()).thenReturn(sampleProvider);
        when(gyroSensor.getSample()).thenReturn(new float[10]);
        when(gyroSensor.getSensor()).thenReturn(ev3GyroSensor);

        MatcherAssert.assertThat(gyroSensor.getGyroAngleRate(), is(0.0F));
    }
}