package de.munro.ev3.data;

import lejos.robotics.Color;

public class SensorData {
    private boolean running = false;

    private boolean pressed;
    private int gyroAngleRate;
    private Color color;
    private int distance;

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public int getGyroAngleRate() {
        return gyroAngleRate;
    }

    public void setGyroAngleRate(int gyroAngleRate) {
        this.gyroAngleRate = gyroAngleRate;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
