package de.munro.ev3.motor;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Properties;

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

        verify(steeringMotor).rotateTillStopped(Motor.Rotation.reverse);
        verify(steeringMotor).resetTachoCount();
        verify(steeringMotor).rotateTillStopped(Motor.Rotation.ahead);
        verify(steeringMotor).rotateTo(0);
    }

    @Test
    public void goHome() {
        SteeringMotor steeringMotor = Mockito.mock(SteeringMotor.class);
        doCallRealMethod().when(steeringMotor).goStraight();

        steeringMotor.goStraight();

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


    @Test
    public void getRightmostPosition() {
        SteeringMotor steeringMotor = Mockito.mock(SteeringMotor.class);
        doCallRealMethod().when(steeringMotor).setRightmostPosition(anyInt());
        doCallRealMethod().when(steeringMotor).getRightmostPosition();
        doCallRealMethod().when(steeringMotor).toString(anyInt());
        Properties properties = new Properties();
        when(steeringMotor.getProperties()).thenReturn(properties);

        steeringMotor.setRightmostPosition(345);

        assertThat(steeringMotor.getRightmostPosition(), Matchers.is(345));
    }

    @Test
    public void getLeftmostPosition() {
        SteeringMotor steeringMotor = Mockito.mock(SteeringMotor.class);
        doCallRealMethod().when(steeringMotor).setLeftmostPosition(anyInt());
        doCallRealMethod().when(steeringMotor).getLeftmostPosition();
        doCallRealMethod().when(steeringMotor).toString(anyInt());
        Properties properties = new Properties();
        when(steeringMotor.getProperties()).thenReturn(properties);

        steeringMotor.setLeftmostPosition(20);

        assertThat(steeringMotor.getLeftmostPosition(), Matchers.is(20));
    }

    @Test
    public void getHomePosition() {
        SteeringMotor steeringMotor = Mockito.mock(SteeringMotor.class);
        doCallRealMethod().when(steeringMotor).setHomePosition(anyInt());
        doCallRealMethod().when(steeringMotor).setImproveHomePosition(anyInt());
        doCallRealMethod().when(steeringMotor).getHomePosition();
        doCallRealMethod().when(steeringMotor).toString(anyInt());
        Properties properties = new Properties();
        when(steeringMotor.getProperties()).thenReturn(properties);

        steeringMotor.setHomePosition(20);
        steeringMotor.setImproveHomePosition(0);

        assertThat(steeringMotor.getHomePosition(), Matchers.is(20));
    }

    @Test
    public void verifyProperties() {
        SteeringMotor steeringMotor = Mockito.mock(SteeringMotor.class);
        doCallRealMethod().when(steeringMotor).verifyProperties();
        Properties properties = new Properties();
        properties.setProperty(Motor.RIGHTMOST_POSITION, "10");
        properties.setProperty(Motor.HOME_POSITION, "55");
        properties.setProperty(Motor.LEFTMOST_POSITION, "100");
        when(steeringMotor.getProperties()).thenReturn(properties);

        assertThat(steeringMotor.verifyProperties(), Matchers.is(true));
    }

    @Test
    public void verifyPropertiesWrongContents() {
        SteeringMotor climbFrontMotor = Mockito.mock(SteeringMotor.class);
        doCallRealMethod().when(climbFrontMotor).verifyProperties();
        Properties properties = new Properties();
        properties.setProperty("wrongPosition", "10");
        properties.setProperty(Motor.LEFTMOST_POSITION, "100");
        properties.setProperty(Motor.HOME_POSITION, "100");
        when(climbFrontMotor.getProperties()).thenReturn(properties);

        assertThat(climbFrontMotor.verifyProperties(), Matchers.is(false));
    }
}