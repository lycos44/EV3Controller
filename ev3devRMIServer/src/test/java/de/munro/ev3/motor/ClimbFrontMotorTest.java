package de.munro.ev3.motor;

import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class ClimbFrontMotorTest {

    @Test
    public void init() {
        ClimbFrontMotor climbFrontMotor = Mockito.mock(ClimbFrontMotor.class);
        doCallRealMethod().when(climbFrontMotor).init();
        when(climbFrontMotor.isStalled()).thenReturn(false).thenReturn(true).thenReturn(false).thenReturn(true);

        climbFrontMotor.init();

        verify(climbFrontMotor).rotateTillStopped(Motor.Direction.BACKWARD);
        verify(climbFrontMotor).resetTachoCount();
        verify(climbFrontMotor).rotateTillStopped(Motor.Direction.FORWARD);
        verify(climbFrontMotor).rotateTo(ClimbFrontMotor.TOLERANCE_POSITION);
    }

    @Test
    public void goHome() {
        ClimbFrontMotor climbFrontMotor = Mockito.mock(ClimbFrontMotor.class);
        doCallRealMethod().when(climbFrontMotor).goHome();

        climbFrontMotor.goHome();

        verify(climbFrontMotor).rotateTo(anyInt());
    }

    @Test
    public void goClimb() {
        ClimbFrontMotor climbFrontMotor = Mockito.mock(ClimbFrontMotor.class);
        doCallRealMethod().when(climbFrontMotor).goClimb();

        climbFrontMotor.goClimb();

        verify(climbFrontMotor).rotateTo(anyInt());
    }

    @Test
    public void is2BeStopped() {
        SteeringMotor steeringMotor = Mockito.mock(SteeringMotor.class);
        doCallRealMethod().when(steeringMotor).is2BeStopped();
        when(steeringMotor.isStalled()).thenReturn(true);

        assertThat(steeringMotor.is2BeStopped(), is(true));

        verify(steeringMotor).isStalled();
    }
}