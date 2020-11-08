package de.munro.ev3.motor;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class ClimbFrontMotorTest {
    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void init() {
        ClimbFrontMotor climbFrontMotor = Mockito.mock(ClimbFrontMotor.class);
        exit.checkAssertionAfterwards(() -> {
            verify(climbFrontMotor, times(1)).rotateTillStopped(Motor.Rotation.reverse);
            verify(climbFrontMotor, times(1)).resetTachoCount();
            verify(climbFrontMotor, times(1)).rotateTo(20);
            verify(climbFrontMotor, times(1)).setSpeed(1000);
        });
        doCallRealMethod().when(climbFrontMotor).init();

        climbFrontMotor.init();
    }

    @Test
    public void goUp() {
        ClimbFrontMotor climbFrontMotor = Mockito.mock(ClimbFrontMotor.class);
        doCallRealMethod().when(climbFrontMotor).goUp();

        climbFrontMotor.goUp();

        verify(climbFrontMotor).rotateTo(anyInt());
    }

    @Test
    public void goDown() {
        ClimbFrontMotor climbFrontMotor = Mockito.mock(ClimbFrontMotor.class);
        doCallRealMethod().when(climbFrontMotor).goDown();

        climbFrontMotor.goDown();

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