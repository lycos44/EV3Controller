package de.munro.ev3.motor;

import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class ClimbBackMotorTest {

    @Test
    public void init() {
        ClimbBackMotor climbBackMotor = Mockito.mock(ClimbBackMotor.class);
        doCallRealMethod().when(climbBackMotor).init();
        when(climbBackMotor.isStalled()).thenReturn(false).thenReturn(true).thenReturn(false).thenReturn(true);

        climbBackMotor.init();

        verify(climbBackMotor).rotateTillStopped(Motor.Direction.BACKWARD);
        verify(climbBackMotor).resetTachoCount();
        verify(climbBackMotor).rotateTillStopped(Motor.Direction.FORWARD);
        verify(climbBackMotor).rotateTo(0);
    }

    @Test
    public void is2BeStopped() {
        ClimbBackMotor climbBackMotor = Mockito.mock(ClimbBackMotor.class);
        doCallRealMethod().when(climbBackMotor).is2BeStopped();
        when(climbBackMotor.isStalled()).thenReturn(true);

        assertThat(climbBackMotor.is2BeStopped(), is(true));

        verify(climbBackMotor).isStalled();
    }
}