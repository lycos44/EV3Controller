package de.munro.ev3.motor;

import de.munro.ev3.data.MotorData;
import de.munro.ev3.rmi.RemoteEV3;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class SteeringMotorTest {
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void init() {
        SteeringMotor steeringMotor = Mockito.mock(SteeringMotor.class);
        when(steeringMotor.getTachoCount()).thenReturn(0).thenReturn(160);
        exit.checkAssertionAfterwards(() -> {
            verify(steeringMotor, times(1)).rotateTillStopped(Motor.Rotation.ahead);
            verify(steeringMotor, times(1)).resetTachoCount();
            verify(steeringMotor, times(1)).rotateTillStopped(Motor.Rotation.reverse);
            verify(steeringMotor, times(1)).rotateTo(80);
        });
        MotorData motorData = new MotorData(new RemoteEV3.Command[]{RemoteEV3.Command.left, RemoteEV3.Command.home, RemoteEV3.Command.right}, 10);
        when(steeringMotor.getMotorData()).thenReturn(motorData);
        doCallRealMethod().when(steeringMotor).init();

        steeringMotor.init();

        MatcherAssert.assertThat(steeringMotor.getMotorData().getPositions().get(RemoteEV3.Command.left), Matchers.is(0));
        MatcherAssert.assertThat(steeringMotor.getMotorData().getPositions().get(RemoteEV3.Command.home), Matchers.is(80));
        MatcherAssert.assertThat(steeringMotor.getMotorData().getPositions().get(RemoteEV3.Command.right), Matchers.is(160));
    }
}