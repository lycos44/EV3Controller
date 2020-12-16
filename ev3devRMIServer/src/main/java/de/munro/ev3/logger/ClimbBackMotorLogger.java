package de.munro.ev3.logger;

import de.munro.ev3.rmi.EV3devConstants;

public interface ClimbBackMotorLogger {
    int getPing();
    int getClimbBackMotorPing();
    boolean isRunning();
    void setRunning(boolean running);

    /**
     * @link LoggerData#getClimbBack()
     */
    EV3devConstants.Climb getClimbBack();

    /**
     * @link LoggerData#setClimbBack()
     */
    void setClimbBack(EV3devConstants.Climb climbBack);
}
