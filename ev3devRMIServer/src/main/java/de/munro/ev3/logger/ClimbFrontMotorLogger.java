package de.munro.ev3.logger;

import de.munro.ev3.rmi.EV3devConstants;

public interface ClimbFrontMotorLogger {
    int getPing();
    int getClimbFrontMotorPing();
    boolean isRunning();
    void setRunning(boolean running);

    /**
     * @link LoggerData#getClimbFront
     */
    EV3devConstants.Climb getClimbFront();

    /**
     * @link LoggerData#setClimbFront
     */
    void setClimbFront(EV3devConstants.Climb climbBack);
}
