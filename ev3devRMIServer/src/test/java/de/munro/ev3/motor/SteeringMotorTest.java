package de.munro.ev3.motor;

import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SteeringMotorTest {

    @Test
    public void init() {
        SteeringMotor steeringMotor = Mockito.mock(SteeringMotor.class);
        doCallRealMethod().when(steeringMotor).init();
        when(steeringMotor.is2BeStopped()).thenReturn(false).thenReturn(true).thenReturn(false).thenReturn(true);

        steeringMotor.init();

        verify(steeringMotor).rotateTillStopped(Motor.Direction.BACKWARD);
        verify(steeringMotor).resetTachoCount();
        verify(steeringMotor).rotateTillStopped(Motor.Direction.FORWARD);
        verify(steeringMotor).rotateTo(0);
    }

    @Test
    public void goHome() {
        SteeringMotor steeringMotor = Mockito.mock(SteeringMotor.class);
        doCallRealMethod().when(steeringMotor).goHome();

        steeringMotor.goHome();

        verify(steeringMotor).rotateTo(anyInt());
    }

    @Test
    public void goLeft() {
        SteeringMotor steeringMotor = Mockito.mock(SteeringMotor.class);
        doCallRealMethod().when(steeringMotor).goLeft();

        steeringMotor.goLeft();

        verify(steeringMotor).rotateTo(anyInt());
    }

    @Test
    public void goRight() {
        SteeringMotor steeringMotor = Mockito.mock(SteeringMotor.class);
        doCallRealMethod().when(steeringMotor).goRight();

        steeringMotor.goRight();

        verify(steeringMotor).rotateTo(anyInt());
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