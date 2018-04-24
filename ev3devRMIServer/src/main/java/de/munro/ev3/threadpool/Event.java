package de.munro.ev3.threadpool;

public class Event {

    private final int value;
    private boolean done;

    public Event (int value) {
        this.value = value;
        this.done = false;
    }

    public void setDone() {
        this.done = true;
    }

    public boolean isDone() {
        return done;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[");
        stringBuffer.append(value).append(", ");
        stringBuffer.append(done);
        stringBuffer.append("]");
        return stringBuffer.toString();
    }
}
