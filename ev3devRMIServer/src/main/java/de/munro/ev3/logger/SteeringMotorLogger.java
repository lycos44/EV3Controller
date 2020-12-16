package de.munro.ev3.logger;

import de.munro.ev3.rmi.EV3devConstants;

public interface SteeringMotorLogger {
    int getPing();
    int getSteeringMotorPing();
    boolean isRunning();
    void setRunning(boolean running);

    /**
     * @link Loggerdata#getSteeringTurn
     */
    EV3devConstants.Turn getSteeringTurn();

    /**
     * @link Loggerdata#setSteeringTurn
     */
    void setSteeringTurn(EV3devConstants.Turn steeringTurn);
}
