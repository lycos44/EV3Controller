package de.munro.ev3.motor;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.mockito.Mockito;

import java.util.Properties;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class ClimbFrontMotorTest {
    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void init() {
        ClimbFrontMotor climbFrontMotor = Mockito.mock(ClimbFrontMotor.class);
        when(climbFrontMotor.getUpPosition()).thenReturn(20);
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

    @Test
    public void getDownPosition() {
        ClimbFrontMotor climbFrontMotor = Mockito.mock(ClimbFrontMotor.class);
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
        ClimbFrontMotor climbFrontMotor = Mockito.mock(ClimbFrontMotor.class);
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
        ClimbFrontMotor climbFrontMotor = Mockito.mock(ClimbFrontMotor.class);
        doCallRealMethod().when(climbFrontMotor).verifyProperties();
        Properties properties = new Properties();
        properties.setProperty(Motor.DOWN_POSITION, "10");
        properties.setProperty(Motor.UP_POSITION, "100");
        when(climbFrontMotor.getProperties()).thenReturn(properties);

        assertThat(climbFrontMotor.verifyProperties(), is(true));
    }

    @Test
    public void verifyPropertiesWrongContents() {
        ClimbFrontMotor climbFrontMotor = Mockito.mock(ClimbFrontMotor.class);
        doCallRealMethod().when(climbFrontMotor).verifyProperties();
        Properties properties = new Properties();
        properties.setProperty("wrongPosition", "10");
        properties.setProperty(Motor.UP_POSITION, "100");
        when(climbFrontMotor.getProperties()).thenReturn(properties);

        assertThat(climbFrontMotor.verifyProperties(), is(false));
    }

    @Test
    public void verifyPropertiesEmpty() {
        ClimbFrontMotor climbFrontMotor = Mockito.mock(ClimbFrontMotor.class);
        doCallRealMethod().when(climbFrontMotor).verifyProperties();
        Properties properties = new Properties();
        when(climbFrontMotor.getProperties()).thenReturn(properties);

        assertThat(climbFrontMotor.verifyProperties(), is(false));
    }
}