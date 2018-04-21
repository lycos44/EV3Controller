package de.munro.ev3.motor;

import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({CameraMotor.class})
public class CameraMotorTest {

    @Before
    public void setup() {
    }

    @Ignore
    @Test
    public void init() throws Exception {
        PowerMockito.mockStatic(CameraMotor.class);
        CameraMotor cameraMotorMock = PowerMockito.mock(CameraMotor.class);
        EV3LargeRegulatedMotor motorMock = mock(EV3LargeRegulatedMotor.class);
        when(motorMock.getTachoCount()).thenReturn(0).thenReturn(240);
        when(cameraMotorMock.getMotor()).thenReturn(motorMock);
        PowerMockito.doCallRealMethod().when(cameraMotorMock, "init");
        PowerMockito.doCallRealMethod().when(cameraMotorMock, "getLeftmostPosition");
        PowerMockito.doCallRealMethod().when(cameraMotorMock, "getRightmostPosition");

        cameraMotorMock.init();

        Assert.assertThat(cameraMotorMock.getLeftmostPosition(),is(120));
        Assert.assertThat(cameraMotorMock.getRightmostPosition(),is(-120));
    }
}