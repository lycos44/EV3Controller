package de.munro.ev3.data;

import de.munro.ev3.rmi.EV3devConstants;

public class SteeringMotorData implements MotorData {
    private boolean done = true;
    private boolean running;
    private EV3devConstants.Turn turn = EV3devConstants.Turn.straight;

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

    public EV3devConstants.Turn getTurn() {
        return turn;
    }

    public void setTurn(EV3devConstants.Turn turn) {
        this.turn = turn;
    }
}
