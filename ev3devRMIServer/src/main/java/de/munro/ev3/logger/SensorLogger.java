package de.munro.ev3.logger;

public interface SensorLogger {
    int getPing();
    int getSensorPing();
    boolean isRunning();
}
