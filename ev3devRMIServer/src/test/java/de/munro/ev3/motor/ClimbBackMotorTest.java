package de.munro.ev3.motor;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class ClimbBackMotorTest {
    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void init() {
        ClimbBackMotor climbBackMotor = Mockito.mock(ClimbBackMotor.class);
        exit.checkAssertionAfterwards(() -> {
            verify(climbBackMotor, times(1)).rotateTillStopped(Motor.Rotation.reverse);
            verify(climbBackMotor, times(1)).resetTachoCount();
            verify(climbBackMotor, times(1)).rotateTo(40);
            verify(climbBackMotor, times(1)).setSpeed(150);
        });
        doCallRealMethod().when(climbBackMotor).init();

        climbBackMotor.init();
    }

    @Test
    public void goUp() {
        ClimbBackMotor climbBackMotor = Mockito.mock(ClimbBackMotor.class);
        doCallRealMethod().when(climbBackMotor).goUp();

        climbBackMotor.goUp();

        verify(climbBackMotor).rotateTo(anyInt());
    }

    @Test
    public void goDown() {
        ClimbBackMotor climbBackMotor = Mockito.mock(ClimbBackMotor.class);
        doCallRealMethod().when(climbBackMotor).goDown();

        climbBackMotor.goDown();

        verify(climbBackMotor).rotateTo(anyInt());
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