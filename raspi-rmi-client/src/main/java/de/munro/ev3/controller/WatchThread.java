package de.munro.ev3.controller;

import de.munro.ev3.rmi.InstructionDetails;
import de.munro.ev3.rmi.RemoteEV3;
import de.munro.ev3.rmi.SensorsDataOLD;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.naming.InvalidNameException;
import java.rmi.RemoteException;

import static java.lang.Math.abs;

@Slf4j
public class WatchThread extends Thread{
    private static final String THREAD_NAME = "watch";
    private static final int DELAY_PERIOD = 100;
    private static final int TIMES_CHECKED = 20;
    private static final int TOLERANCE = 2;


    private final RemoteEV3 remoteEV3;

    private final int [] distanceCourse;
    private final int [] gyroAngleRateCourse;
    private int nextDistance;
    private InstructionDetails instructionDetails;

    /**
     * constructor
     *
     * @param remoteEV3 remoteEV3 to set
     * @param instructionDetails
     */
    public WatchThread(RemoteEV3 remoteEV3, InstructionDetails instructionDetails) {
        this.instructionDetails = instructionDetails;
        this.remoteEV3 = remoteEV3;
        this.setName(THREAD_NAME);
        distanceCourse = new int[TIMES_CHECKED];
        gyroAngleRateCourse = new int[TIMES_CHECKED];
        nextDistance = 0;
    }

    /**
     * @link Thread#run()
     */
    @SneakyThrows
    @Override
    public void run() {
        while (!isInterrupted()) {
            sleep(DELAY_PERIOD);
            try {
                inspect(remoteEV3);
            } catch (RemoteException | InvalidNameException e) {
                log.debug(e.getMessage());
            }
        }
    }

    /**
     * read the status of the ev3 sensors
     * @param remoteEV3
     * @throws InvalidNameException
     * @throws RemoteException
     */
    private void inspect(RemoteEV3 remoteEV3) throws InvalidNameException, RemoteException {
        SensorsDataOLD sensorsData = remoteEV3.status();
        RemoteEV3.Command currentCommand;
        synchronized (instructionDetails) {
            currentCommand = instructionDetails.getCommand();
            log.debug("command: {}", currentCommand);
        }
        if (currentCommand == sensorsData.getDriveDoing() && sensorsData.getDriveDoing() != RemoteEV3.Command.stop) {
            log.debug("GyroAngleRate: {}", sensorsData.getGyroAngleRate());
            if (sensorsData.getDistance() < 20 && sensorsData.getDriveDoing() == RemoteEV3.Command.forward) {
                log.debug("stopped: barrier in front");
                remoteEV3.perform(RemoteEV3.MotorType.drive, RemoteEV3.Command.stop);
            } else if (sensorsData.isBackwardPressed() && sensorsData.getDriveDoing() == RemoteEV3.Command.backward) {
                log.debug("stopped: barrier in back");
                remoteEV3.perform(RemoteEV3.MotorType.drive, RemoteEV3.Command.stop);
            } else if (isStucked(sensorsData.getDistance())) {
                log.debug("stopped: got stuck");
                remoteEV3.perform(RemoteEV3.MotorType.drive, RemoteEV3.Command.stop);
            }
        }
    }

    protected float getGyroAngleRateVariation(int gyroAngleRate) {
        return 0;
    }

    protected boolean isStucked(int distance) {
        int variation = abs(distance - distanceCourse[nextDistance]);
        distanceCourse[nextDistance] = distance;
        nextDistance = ++nextDistance % distanceCourse.length;
        log.debug("variation: {}", variation);
        return variation < TOLERANCE;
    }
}
