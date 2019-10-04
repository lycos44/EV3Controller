package de.munro.ev3.motor;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.mockito.Mockito;

import static de.munro.ev3.rmi.EV3devConstants.SYSTEM_UNEXPECTED_ERROR;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class ClimbFrontMotorTest {
    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void init() {
        ClimbFrontMotor climbFrontMotor = Mockito.mock(ClimbFrontMotor.class);
        exit.expectSystemExitWithStatus(SYSTEM_UNEXPECTED_ERROR);
        exit.checkAssertionAfterwards(() -> {
            verify(climbFrontMotor, times(2)).rotateTillStopped(Motor.Direction.BACKWARD);
            verify(climbFrontMotor, times(2)).resetTachoCount();
            verify(climbFrontMotor, times(2)).rotateTillStopped(Motor.Direction.FORWARD);
            verify(climbFrontMotor, times(2)).rotateTo(20);
        });
        doCallRealMethod().when(climbFrontMotor).init();
        when(climbFrontMotor.isStalled()).thenReturn(false).thenReturn(true).thenReturn(false).thenReturn(true);

        climbFrontMotor.init();
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
        ClimbFrontMotor climbFrontMotor = Mockito.mock(ClimbFrontMotor.class);
        doCallRealMethod().when(climbFrontMotor).is2BeStopped();
        when(climbFrontMotor.isStalled()).thenReturn(true);

        assertThat(climbFrontMotor.is2BeStopped(), is(true));

        verify(climbFrontMotor).isStalled();
    }
}