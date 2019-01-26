package de.munro.ev3.motor;

import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class CameraMotorTest {

    @Test
    public void is2BeStopped() {
        CameraMotor cameraMotor = Mockito.mock(CameraMotor.class);
        doCallRealMethod().when(cameraMotor).is2BeStopped();

        assertThat(cameraMotor.is2BeStopped(), is(false));
    }

    @Test
    public void init() {
        CameraMotor cameraMotor = Mockito.mock(CameraMotor.class);
        doCallRealMethod().when(cameraMotor).init();
        when(cameraMotor.isCameraSensorPressed()).thenReturn(false).thenReturn(true).thenReturn(false).thenReturn(true);

        cameraMotor.init();

        verify(cameraMotor, times(2)).stop();
        verify(cameraMotor).forward();
        verify(cameraMotor).backward();
    }

    @Test
    public void goHome() {
        CameraMotor cameraMotor = Mockito.mock(CameraMotor.class);
        doCallRealMethod().when(cameraMotor).goHome();

        cameraMotor.goHome();

        verify(cameraMotor).rotateTo(anyInt());
    }

    @Test
    public void goLeft() {
        CameraMotor cameraMotor = Mockito.mock(CameraMotor.class);
        doCallRealMethod().when(cameraMotor).goLeft();

        cameraMotor.goLeft();

        verify(cameraMotor).rotateTo(anyInt());
    }

    @Test
    public void goRight() {
        CameraMotor cameraMotor = Mockito.mock(CameraMotor.class);
        doCallRealMethod().when(cameraMotor).goRight();

        cameraMotor.goRight();

        verify(cameraMotor).rotateTo(anyInt());
    }
}