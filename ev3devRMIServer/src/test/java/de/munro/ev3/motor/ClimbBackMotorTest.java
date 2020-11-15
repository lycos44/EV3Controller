package de.munro.ev3.motor;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.mockito.Mockito;

import java.util.Properties;

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
            verify(climbBackMotor, times(1)).goUp();
            verify(climbBackMotor, times(1)).resetTachoCount();
            verify(climbBackMotor, times(1)).rotateTillStopped(Motor.Rotation.reverse);
            verify(climbBackMotor, times(1)).rotateTillStopped(Motor.Rotation.ahead);
            verify(climbBackMotor, times(1)).setSpeed(50);
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

        verify(climbBackMotor, times(1)).rotateTo(anyInt());
    }

    @Test
    public void is2BeStopped() {
        ClimbBackMotor climbBackMotor = Mockito.mock(ClimbBackMotor.class);
        doCallRealMethod().when(climbBackMotor).is2BeStopped();
        when(climbBackMotor.isStalled()).thenReturn(true);

        assertThat(climbBackMotor.is2BeStopped(), is(true));

        verify(climbBackMotor).isStalled();
    }

    @Test
    public void getDownPosition() {
        ClimbBackMotor climbFrontMotor = Mockito.mock(ClimbBackMotor.class);
        doCallRealMethod().when(climbFrontMotor).setDownPosition(anyInt());
        doCallRealMethod().when(climbFrontMotor).getDownPosition();
        doCallRealMethod().when(climbFrontMotor).toString(anyInt());
        Properties properties = new Properties();
        when(climbFrontMotor.getProperties()).thenReturn(properties);

        climbFrontMotor.setDownPosition(345);

        assertThat(climbFrontMotor.getDownPosition(), is(345));
    }

    @Test
    public void getUpPosition() {
        ClimbBackMotor climbFrontMotor = Mockito.mock(ClimbBackMotor.class);
        doCallRealMethod().when(climbFrontMotor).setUpPosition(anyInt());
        doCallRealMethod().when(climbFrontMotor).getUpPosition();
        doCallRealMethod().when(climbFrontMotor).toString(anyInt());
        Properties properties = new Properties();
        when(climbFrontMotor.getProperties()).thenReturn(properties);

        climbFrontMotor.setUpPosition(20);

        assertThat(climbFrontMotor.getUpPosition(), is(20));
    }

    @Test
    public void verifyProperties() {
        ClimbBackMotor climbFrontMotor = Mockito.mock(ClimbBackMotor.class);
        doCallRealMethod().when(climbFrontMotor).verifyProperties();
        Properties properties = new Properties();
        properties.setProperty(Motor.DOWN_POSITION, "10");
        properties.setProperty(Motor.UP_POSITION, "100");
        when(climbFrontMotor.getProperties()).thenReturn(properties);

        assertThat(climbFrontMotor.verifyProperties(), is(true));
    }

    @Test
    public void verifyPropertiesWrongContents() {
        ClimbBackMotor climbFrontMotor = Mockito.mock(ClimbBackMotor.class);
        doCallRealMethod().when(climbFrontMotor).verifyProperties();
        Properties properties = new Properties();
        properties.setProperty("wrongPosition", "10");
        properties.setProperty(Motor.UP_POSITION, "100");
        when(climbFrontMotor.getProperties()).thenReturn(properties);

        assertThat(climbFrontMotor.verifyProperties(), is(false));
    }

    @Test
    public void verifyPropertiesEmpty() {
        ClimbBackMotor climbFrontMotor = Mockito.mock(ClimbBackMotor.class);
        doCallRealMethod().when(climbFrontMotor).verifyProperties();
        Properties properties = new Properties();
        when(climbFrontMotor.getProperties()).thenReturn(properties);

        assertThat(climbFrontMotor.verifyProperties(), is(false));
    }
}