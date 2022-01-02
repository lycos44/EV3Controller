package de.munro.ev3.motor;

import de.munro.ev3.data.MotorData;
import de.munro.ev3.rmi.RemoteEV3;
import org.hamcrest.MatcherAssert;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class LiftMotorTest {
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void init() {
        LiftMotor liftMotor = Mockito.mock(LiftMotor.class);
        when(liftMotor.getTachoCount()).thenReturn(20).thenReturn(100);
        exit.checkAssertionAfterwards(() -> {
            verify(liftMotor, times(1)).rotateTillStopped(Motor.Rotation.reverse);
            verify(liftMotor, times(1)).resetTachoCount();
            verify(liftMotor, times(1)).rotateTillStopped(Motor.Rotation.ahead);
            verify(liftMotor, times(1)).rotateTo(20);
        });
        MotorData motorData = new MotorData(new RemoteEV3.Command[]{RemoteEV3.Command.up, RemoteEV3.Command.down}, 10);
        when(liftMotor.getMotorData()).thenReturn(motorData);
        doCallRealMethod().when(liftMotor).init();

        liftMotor.init();

        MatcherAssert.assertThat(liftMotor.getMotorData().getPositions().get(RemoteEV3.Command.up), is(20));
        MatcherAssert.assertThat(liftMotor.getMotorData().getPositions().get(RemoteEV3.Command.down), is(100));
    }
}