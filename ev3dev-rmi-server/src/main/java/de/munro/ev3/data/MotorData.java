package de.munro.ev3.data;

public interface MotorData {
    boolean isDone();

    void setDone(boolean done);

    boolean isRunning();

    void setRunning(boolean running);
}
