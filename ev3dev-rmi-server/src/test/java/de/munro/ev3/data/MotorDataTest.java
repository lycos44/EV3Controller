package de.munro.ev3.data;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Properties;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;

class MotorDataTest {

    @Test
    void verify() {
        MotorData motorData = Mockito.mock(MotorData.class);
        doCallRealMethod().when(motorData).verify(any(Properties.class));
        Properties properties = new Properties();

        MatcherAssert.assertThat(motorData.verify(properties), is(true));
    }
}