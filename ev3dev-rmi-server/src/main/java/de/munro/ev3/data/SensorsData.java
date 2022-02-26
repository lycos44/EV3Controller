package de.munro.ev3.data;

import de.munro.ev3.rmi.RemoteEV3;

public class SensorsData {
    private boolean running = false;
    private boolean backwardPressed;
    private float gyroAngleRate;
    private int colorID;
    private int distance;
    private boolean reset;

    public void setData(boolean backwardPressed, float gyroAngleRate, int colorID, int distance) {
        this.backwardPressed = backwardPressed;
        this.gyroAngleRate = gyroAngleRate;
        this.colorID = colorID;
        this.distance = distance;
    }

    public boolean isBackwardPressed() {
        return backwardPressed;
    }

    public void setBackwardPressed(boolean backwardPressed) {
        this.backwardPressed = backwardPressed;
    }

    public float getGyroAngleRate() {
        return gyroAngleRate;
    }

    public void setGyroAngleRate(int gyroAngleRate) {
        this.gyroAngleRate = gyroAngleRate;
    }

    public int getColorID() {
        return colorID;
    }

    public void setColorID(int colorID) {
        this.colorID = colorID;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public boolean isReset() {
        return reset;
    }
}
