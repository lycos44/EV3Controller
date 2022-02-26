package de.munro.ev3.rmi;

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

public class SensorsDataOLD implements Serializable {
    private boolean running = false;

    private boolean backwardPressed;
    private int gyroAngleRate;
    private int colorID;
    private int distance;
    private RemoteEV3.Command liftBackDoing;
    private RemoteEV3.Command liftFrontDoing;
    private RemoteEV3.Command driveDoing;
    private RemoteEV3.Command steeringDoing;

    public SensorsDataOLD() {
        this.running = true;
        this.backwardPressed = false;
        this.gyroAngleRate = 0;
        this.colorID = 0;
        this.distance = 0;
        this.liftBackDoing = RemoteEV3.Command.up;
        this.liftFrontDoing = RemoteEV3.Command.up;
        this.driveDoing = RemoteEV3.Command.stop;
        this.steeringDoing = RemoteEV3.Command.home;
    }

    public SensorsDataOLD(boolean running, boolean backwardPressed, int gyroAngleRate, int colorID, int distance, RemoteEV3.Command liftBackDoing, RemoteEV3.Command liftFrontDoing, RemoteEV3.Command driveDoing, RemoteEV3.Command steeringDoing) {
        this.running = running;
        this.backwardPressed = backwardPressed;
        this.gyroAngleRate = gyroAngleRate;
        this.colorID = colorID;
        this.distance = distance;
        this.liftBackDoing = liftBackDoing;
        this.liftFrontDoing = liftFrontDoing;
        this.driveDoing = driveDoing;
        this.steeringDoing = steeringDoing;
    }

    public void setData(boolean backwardPressed, int gyroAngleRate, int colorID, int distance) {
        this.backwardPressed = backwardPressed;
        this.gyroAngleRate = gyroAngleRate;
        this.colorID = colorID;
        this.distance = distance;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isBackwardPressed() {
        return backwardPressed;
    }

    public void setBackwardPressed(boolean backwardPressed) {
        this.backwardPressed = backwardPressed;
    }

    public int getGyroAngleRate() {
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

    public RemoteEV3.Command getLiftBackDoing() {
        return liftBackDoing;
    }

    public void setLiftBackDoing(RemoteEV3.Command liftBackDoing) {
        this.liftBackDoing = liftBackDoing;
    }

    public RemoteEV3.Command getLiftFrontDoing() {
        return liftFrontDoing;
    }

    public void setLiftFrontDoing(RemoteEV3.Command liftFrontDoing) {
        this.liftFrontDoing = liftFrontDoing;
    }

    public RemoteEV3.Command getDriveDoing() {
        return driveDoing;
    }

    public void setDriveDoing(RemoteEV3.Command driveDoing) {
        this.driveDoing = driveDoing;
    }

    public RemoteEV3.Command getSteeringDoing() {
        return steeringDoing;
    }

    public void setSteeringDoing(RemoteEV3.Command steeringDoing) {
        this.steeringDoing = steeringDoing;
    }

    /**
     * @link Object#toString
     */
    @Override
    public String toString() {
        return "SensorsData {" + "\n" +
                "\tpressed:       " + backwardPressed + "\n" +
                "\tgyroAngleRate: " + gyroAngleRate + "\n" +
                "\tcolorID:       " + colorID + "\n" +
                "\tdistance:      " + distance + "\n" +
                "\tliftBack:      " + liftBackDoing + "\n" +
                "\tliftFront:     " + liftFrontDoing + "\n" +
                "\tdrive:         " + driveDoing + "\n" +
                "\tsteering:      " + steeringDoing + "\n" +
                "}";
    }
}
