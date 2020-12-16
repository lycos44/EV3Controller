package de.munro.ev3.rmi;

public class EV3devConstants {

    public enum Direction {
        forward,
        backward,
        stop,
    }

    public enum Turn {
        left,
        right,
        straight,
    }

    public enum Climb {
        up,
        down
    }

    public static final int SYSTEM_FINISHED_SUCCESSFULLY = 0;
    public static final int SYSTEM_UNEXPECTED_ERROR = -1;

    public static final int DELAY_PERIOD_SHORT = 500;
}
