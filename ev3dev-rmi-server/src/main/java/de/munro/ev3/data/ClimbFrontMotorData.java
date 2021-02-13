package de.munro.ev3.data;

import de.munro.ev3.rmi.EV3devConstants;

public class ClimbFrontMotorData implements MotorData {
    private boolean done = true;
    private boolean running;
    private EV3devConstants.Climb climb = EV3devConstants.Climb.up;

    public boolean isDone() {
        return done;
    }

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

    public EV3devConstants.Climb getClimb() {
        return climb;
    }

    public void setClimb(EV3devConstants.Climb climb) {
        this.climb = climb;
    }
}
