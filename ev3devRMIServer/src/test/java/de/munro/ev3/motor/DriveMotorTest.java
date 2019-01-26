package de.munro.ev3.motor;

import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doCallRealMethod;

public class DriveMotorTest {

    @Test
    public void is2BeStopped() {
        DriveMotor driveMotor = Mockito.mock(DriveMotor.class);
        doCallRealMethod().when(driveMotor).is2BeStopped();

        assertThat(driveMotor.is2BeStopped(), is(false));
    }
}