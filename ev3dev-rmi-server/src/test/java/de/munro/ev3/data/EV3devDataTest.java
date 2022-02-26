package de.munro.ev3.data;

import de.munro.ev3.rmi.RemoteEV3;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

class EV3devDataTest {

    @Test
    void perform() {
        EV3devData ev3devData = Mockito.mock(EV3devData.class);
        doCallRealMethod().when(ev3devData).perform(any(RemoteEV3.MotorType.class), any(RemoteEV3.Command.class));
        MotorData motorData = new MotorData(new RemoteEV3.Command[]{}, 10);
        when(ev3devData.getMotorData(RemoteEV3.MotorType.drive)).thenReturn(motorData);
        SensorsData sensorsData = Mockito.mock(SensorsData.class);
        when(ev3devData.getSensorsData()).thenReturn(sensorsData);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ev3devData.perform(RemoteEV3.MotorType.drive, RemoteEV3.Command.left);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ev3devData.perform(RemoteEV3.MotorType.drive, RemoteEV3.Command.up);
        });
        Assertions.assertDoesNotThrow(() -> {
            ev3devData.perform(RemoteEV3.MotorType.drive, RemoteEV3.Command.forward);
        });
        Assertions.assertDoesNotThrow(() -> {
            ev3devData.perform(RemoteEV3.MotorType.drive, RemoteEV3.Command.backward);
        });
    }
}