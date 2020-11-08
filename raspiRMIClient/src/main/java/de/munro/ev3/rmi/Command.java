package de.munro.ev3.rmi;

public enum Command {
    QUIT("q"),
    BEEP("beep"),
    FORWARD("forward"),
    BACKWARD("backward"),
    STOP("stop"),
    LEFT("left"),
    RIGHT("right"),
    STRAIGHT("straight"),
    FRONTUP("frontup"),
    FRONTDOWN("frontdown"),
    BACKUP("backup"),
    BACKDOWN("backdown"),
    SHUTDOWN("shutdown");

    private final String name;

    Command(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
