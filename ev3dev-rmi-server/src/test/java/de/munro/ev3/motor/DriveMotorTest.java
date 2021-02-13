package de.munro.ev3.motor;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Properties;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

public class DriveMotorTest {

    @Test
    public void is2BeStopped() {
        DriveMotor driveMotor = Mockito.mock(DriveMotor.class);
        doCallRealMethod().when(driveMotor).is2BeStopped();

        MatcherAssert.assertThat(driveMotor.is2BeStopped(), is(false));
    }

    @Test
    public void verifyProperties() {
        DriveMotor driveMotor = Mockito.mock(DriveMotor.class);
        doCallRealMethod().when(driveMotor).verifyProperties();
        Properties properties = new Properties();
        when(driveMotor.getProperties()).thenReturn(properties);

        assertThat(driveMotor.verifyProperties(), is(true));
    }
}