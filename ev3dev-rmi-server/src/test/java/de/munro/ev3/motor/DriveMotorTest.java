package de.munro.ev3.motor;

import de.munro.ev3.data.MotorData;
import de.munro.ev3.rmi.RemoteEV3;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

public class DriveMotorTest {

    @Test
    public void init() {
        DriveMotor driveMotor = Mockito.mock(DriveMotor.class);
        MotorData motorData = new MotorData(new RemoteEV3.Command[]{}, 10);
        when(driveMotor.getMotorData()).thenReturn(motorData);
        doCallRealMethod().when(driveMotor).init();

        driveMotor.init();

        MatcherAssert.assertThat(driveMotor.getMotorData().getPositions().keySet().size(), is(0));
    }
}