package de.munro.ev3.data;

import de.munro.ev3.rmi.EV3devConstants;

public class DriveMotorData implements MotorData {
    private boolean done = true;
    private boolean running;
    private EV3devConstants.Direction direction = EV3devConstants.Direction.stop;

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void setRunning(boolean running) {
        this.running = running;
    }

    public EV3devConstants.Direction getDirection() {
        return direction;
    }

    public void setDirection(EV3devConstants.Direction direction) {
        this.direction = direction;
    }
}
