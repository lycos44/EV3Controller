package de.munro.ev3.sensor;

import ev3dev.sensors.ev3.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GyroSensor extends Sensor {

    private static final int SAMPLE_SIZE = 10;
    private final EV3GyroSensor sensor;
    private int offset;
    private float[] sample = new float[SAMPLE_SIZE];

    /**
     * Constructor
     */
    public GyroSensor() {
        sensor = new EV3GyroSensor(SensorType.gyro.getPort());
        sensor.reset();
    }

    /**
     * @link Sensor#getSensor
     */
    @Override
    public EV3GyroSensor getSensor() {
        return sensor;
    }

    public float[] getSample() {
        return sample;
    }

    /**
     * provides the rate currently measured
     * @return distance
     */
    public float getGyroAngleRate() {
        offset = ++offset % getSample().length;

        final SampleProvider sampleProvider = getSensor().getRateMode();
        sampleProvider.fetchSample(getSample(), offset);
        return getSample()[offset];
    }

    public boolean isReadyToExamine() {
        return offset == SAMPLE_SIZE;
    }

    public float getAverageSample() {
        float sum = 0.0F;
        for (int i=0; i<SAMPLE_SIZE; i++) {
            sum += sample[i];
        }
        return sum/sample.length;
    }

    public void reset() {
        for (int i=0; i<SAMPLE_SIZE; i++) {
            sample[i] = 0.0F;
        }
    }

    /**
     * @link Object#toString
     */
    @Override
    public String toString() {
        return Float.toString(getGyroAngleRate());
    }
}
