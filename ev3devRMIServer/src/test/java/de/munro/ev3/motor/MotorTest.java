package de.munro.ev3.motor;

import ev3dev.actuators.lego.motors.BaseRegulatedMotor;
import org.junit.Test;
import org.mockito.Mockito;

import static de.munro.ev3.motor.Motor.Polarity.INVERSED;
import static de.munro.ev3.motor.Motor.Polarity.NORMAL;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class MotorTest {

    @Test
    public void isInitialized() {
        Motor motor = Mockito.mock(Motor.class);
        doCallRealMethod().when(motor).isInitialized();
        BaseRegulatedMotor regulatedMotor = Mockito.mock(BaseRegulatedMotor.class);
        when(motor.getMotor()).thenReturn(regulatedMotor);

        assertThat(motor.isInitialized(), is(true));

        verify(motor).getMotor();
    }

    @Test
    public void isStalled() {
        Motor motor = Mockito.mock(Motor.class);
        doCallRealMethod().when(motor).isStalled();
        BaseRegulatedMotor regulatedMotor = Mockito.mock(BaseRegulatedMotor.class);
        when(motor.getMotor()).thenReturn(regulatedMotor);
        when(regulatedMotor.isStalled()).thenReturn(true);

        assertThat(motor.isStalled(), is(true));

        verify(motor).getMotor();
        verify(regulatedMotor).isStalled();
    }

    @Test
    public void forwardInversed() {
        Motor motor = Mockito.mock(Motor.class);
        doCallRealMethod().when(motor).forward();
        when(motor.getPolarity()).thenReturn(INVERSED);
        BaseRegulatedMotor regulatedMotor = Mockito.mock(BaseRegulatedMotor.class);
        when(motor.getMotor()).thenReturn(regulatedMotor);

        motor.forward();

        verify(regulatedMotor).backward();
    }

    @Test
    public void forwardNormal() {
        Motor motor = Mockito.mock(Motor.class);
        doCallRealMethod().when(motor).forward();
        when(motor.getPolarity()).thenReturn(NORMAL);
        BaseRegulatedMotor regulatedMotor = Mockito.mock(BaseRegulatedMotor.class);
        when(motor.getMotor()).thenReturn(regulatedMotor);

        motor.forward();

        verify(regulatedMotor).forward();
    }

    @Test
    public void backwardInversed() {
        Motor motor = Mockito.mock(Motor.class);
        doCallRealMethod().when(motor).backward();
        when(motor.getPolarity()).thenReturn(INVERSED);
        BaseRegulatedMotor regulatedMotor = Mockito.mock(BaseRegulatedMotor.class);
        when(motor.getMotor()).thenReturn(regulatedMotor);

        motor.backward();

        verify(regulatedMotor).forward();
    }

    @Test
    public void backwardNormal() {
        Motor motor = Mockito.mock(Motor.class);
        doCallRealMethod().when(motor).backward();
        when(motor.getPolarity()).thenReturn(NORMAL);
        BaseRegulatedMotor regulatedMotor = Mockito.mock(BaseRegulatedMotor.class);
        when(motor.getMotor()).thenReturn(regulatedMotor);

        motor.backward();

        verify(regulatedMotor).backward();
    }

    @Test
    public void rotateTillStopped() {
        Motor motor = Mockito.mock(Motor.class);
        doCallRealMethod().when(motor).rotateTillStopped(any());
        doCallRealMethod().when(motor).setDirectionStalled(any());
        doCallRealMethod().when(motor).getDirectionStalled();
        when(motor.is2BeStopped()).thenReturn(false).thenReturn(false).thenReturn(true);
        motor.setDirectionStalled(Motor.Direction.NOT_SET);

        motor.rotateTillStopped(Motor.Direction.FORWARD);

        assertThat(motor.getDirectionStalled(), is(Motor.Direction.FORWARD));
        verify(motor).forward();
        verify(motor, times(3)).is2BeStopped();
    }

    @Test
    public void stop() {
        Motor motor = Mockito.mock(Motor.class);
        doCallRealMethod().when(motor).stop();
        BaseRegulatedMotor regulatedMotor = Mockito.mock(BaseRegulatedMotor.class);
        when(motor.getMotor()).thenReturn(regulatedMotor);

        motor.stop();

        verify(motor).getMotor();
        verify(regulatedMotor).stop();
    }

    @Test
    public void brake() {
        Motor motor = Mockito.mock(Motor.class);
        doCallRealMethod().when(motor).brake();
        BaseRegulatedMotor regulatedMotor = Mockito.mock(BaseRegulatedMotor.class);
        when(motor.getMotor()).thenReturn(regulatedMotor);

        motor.brake();

        verify(motor).getMotor();
        verify(regulatedMotor).brake();
    }

    @Test
    public void rotateTo() {
        Motor motor = Mockito.mock(Motor.class);
        doCallRealMethod().when(motor).rotateTo(anyInt());
        BaseRegulatedMotor regulatedMotor = Mockito.mock(BaseRegulatedMotor.class);
        when(motor.getMotor()).thenReturn(regulatedMotor);

        int angle = 90;
        motor.rotateTo(angle);

        verify(motor).getMotor();
        verify(regulatedMotor).rotateTo(angle);
    }

    @Test
    public void getTachoCount() {
        Motor motor = Mockito.mock(Motor.class);
        doCallRealMethod().when(motor).getTachoCount();
        BaseRegulatedMotor regulatedMotor = Mockito.mock(BaseRegulatedMotor.class);
        when(motor.getMotor()).thenReturn(regulatedMotor);
        when(regulatedMotor.getTachoCount()).thenReturn(123);

        assertThat(motor.getTachoCount(), is(123));

        verify(motor).getMotor();
        verify(regulatedMotor).getTachoCount();
    }

    @Test
    public void resetTachoCount() {
        Motor motor = Mockito.mock(Motor.class);
        doCallRealMethod().when(motor).resetTachoCount();
        BaseRegulatedMotor regulatedMotor = Mockito.mock(BaseRegulatedMotor.class);
        when(motor.getMotor()).thenReturn(regulatedMotor);

        motor.resetTachoCount();

        verify(motor).getMotor();
        verify(regulatedMotor).resetTachoCount();
    }
}