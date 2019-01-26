package de.munro.ev3.motor;

import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class ClimbMotorTest {

    @Test
    public void is2BeStopped() {
        ClimbMotor climbMotor = Mockito.mock(ClimbMotor.class);
        doCallRealMethod().when(climbMotor).is2BeStopped();

        assertThat(climbMotor.is2BeStopped(), is(false));
    }

    @Test
    public void init() {
        ClimbMotor climbMotor = Mockito.mock(ClimbMotor.class);
        doCallRealMethod().when(climbMotor).init();
        EV3LargeRegulatedMotor regulatedMotor = Mockito.mock(EV3LargeRegulatedMotor.class);
        when(climbMotor.getMotor()).thenReturn(regulatedMotor);
        when(regulatedMotor.isStalled()).thenReturn(true).thenReturn(true);

        climbMotor.init();

        verify(climbMotor, times(2)).stop();
        verify(climbMotor).forward();
        verify(climbMotor).backward();
    }
}