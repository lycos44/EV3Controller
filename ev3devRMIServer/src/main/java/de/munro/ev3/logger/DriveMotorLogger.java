package de.munro.ev3.logger;

import de.munro.ev3.rmi.EV3devConstants;

public interface DriveMotorLogger {
    int getPing();
    int getDriveMotorPing();
    boolean isRunning();
    void setRunning(boolean running);

    /**
     * @link Loggerdata#getDriveDirection
     */
    EV3devConstants.Direction getDriveDirection();

    /**
     * @link Loggerdata#setDriveDirection
     */
    void setDriveDirection(EV3devConstants.Direction driveDirection);
}
