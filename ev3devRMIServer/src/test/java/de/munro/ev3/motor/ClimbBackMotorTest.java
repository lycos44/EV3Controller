package de.munro.ev3.motor;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.mockito.Mockito;

import static de.munro.ev3.rmi.EV3devConstants.SYSTEM_UNEXPECTED_ERROR;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class ClimbBackMotorTest {
    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void init() {
        ClimbBackMotor climbBackMotor = Mockito.mock(ClimbBackMotor.class);
        exit.expectSystemExitWithStatus(SYSTEM_UNEXPECTED_ERROR);
        exit.checkAssertionAfterwards(() -> {
            verify(climbBackMotor, times(2)).rotateTillStopped(Motor.Direction.BACKWARD);
            verify(climbBackMotor, times(2)).resetTachoCount();
            verify(climbBackMotor, times(2)).rotateTillStopped(Motor.Direction.FORWARD);
            verify(climbBackMotor, times(2)).rotateTo(0);
        });
        doCallRealMethod().when(climbBackMotor).init();
        when(climbBackMotor.isStalled()).thenReturn(false).thenReturn(true).thenReturn(false).thenReturn(true);

        climbBackMotor.init();
    }

    @Test
    public void goHome() {
        ClimbBackMotor climbBackMotor = Mockito.mock(ClimbBackMotor.class);
        doCallRealMethod().when(climbBackMotor).goHome();

        climbBackMotor.goHome();

        verify(climbBackMotor).rotateTo(anyInt());
    }

    @Test
    public void goClimb() {
        ClimbBackMotor climbBackMotor = Mockito.mock(ClimbBackMotor.class);
        doCallRealMethod().when(climbBackMotor).goClimb();

        climbBackMotor.goClimb();

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