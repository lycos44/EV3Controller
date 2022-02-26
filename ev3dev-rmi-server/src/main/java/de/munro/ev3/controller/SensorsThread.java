package de.munro.ev3.controller;

import de.munro.ev3.data.EV3devData;
import de.munro.ev3.data.MotorData;
import de.munro.ev3.rmi.RemoteEV3;
import de.munro.ev3.sensor.BackwardSensor;
import de.munro.ev3.sensor.ColorSensor;
import de.munro.ev3.sensor.DistanceSensor;
import de.munro.ev3.sensor.GyroSensor;
import de.munro.ev3.watch.WatchBackward;
import de.munro.ev3.watch.WatchDistance;
import lejos.utility.Delay;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import static de.munro.ev3.rmi.EV3devConstants.DELAY_PERIOD_SHORT;

@Slf4j
public class SensorsThread extends Thread {

    private static final String THREAD_NAME = "sensors";

    private final EV3devData ev3devData;

    /**
     * constructor
     *
     * @param ev3devData ev3devData to set
     */
    public SensorsThread(EV3devData ev3devData) {
        this.ev3devData = ev3devData;
        this.setName(THREAD_NAME);
    }

    /**
     * @link Thread#run()
     */
    @SneakyThrows
    @Override
    public void run() {
        BackwardSensor backwardSensor = new BackwardSensor();
        GyroSensor gyroSensor = new GyroSensor();
        ColorSensor colorSensor = new ColorSensor();
        DistanceSensor distanceSensor = new DistanceSensor();

        this.getEv3devData().getSensorsData().setRunning(true);
        WatchDistance watchDistance = new WatchDistance();
        WatchBackward watchBackward = new WatchBackward();

        do {
            synchronized (this.getEv3devData().getSensorsData()) {
                this.getEv3devData().getSensorsData().setData(
                        backwardSensor.isPressed(),
                        gyroSensor.getGyroAngleRate(),
                        colorSensor.getColorID(),
                        distanceSensor.getDistance());
            }

            if (this.getEv3devData().getMotorData(RemoteEV3.MotorType.drive).getMotorStatus() == MotorData.MotorStatus.running
                && this.getEv3devData().getMotorData(RemoteEV3.MotorType.drive).getCommand() != RemoteEV3.Command.stop) {

                if (gyroSensor.isReadyToExamine()) {
                    float averageSample = gyroSensor.getAverageSample();
                    if (averageSample != 0.0) {
                        this.getEv3devData().perform(RemoteEV3.MotorType.drive, RemoteEV3.Command.stop);
                    }
                    gyroSensor.reset();
                }

                if (this.getEv3devData().getSensorsData().isReset()) {
                    watchDistance.reset();
                    this.getEv3devData().getSensorsData().setReset(false);
                } else {

                    if (this.getEv3devData().getMotorData(RemoteEV3.MotorType.drive).getCommand() == RemoteEV3.Command.forward &&
                            watchDistance.isToBeStopped(getEv3devData().getSensorsData().getDistance())) {
                        log.debug("watchDistance.isToBeStopped");
                        this.getEv3devData().perform(RemoteEV3.MotorType.drive, RemoteEV3.Command.stop);
                    }
                    if (this.getEv3devData().getMotorData(RemoteEV3.MotorType.drive).getCommand() == RemoteEV3.Command.backward &&
                            (watchBackward.isToBeStopped(getEv3devData().getSensorsData().isBackwardPressed()) ||
                                    watchDistance.isStuck(getEv3devData().getSensorsData().getDistance()))) {
                        log.debug("watchBackward.isBackwardPressed: {}, watchDistance.isStuck {}", watchBackward.isToBeStopped(getEv3devData().getSensorsData().isBackwardPressed()), watchDistance.isStuck(getEv3devData().getSensorsData().getDistance()));
                        this.getEv3devData().perform(RemoteEV3.MotorType.drive, RemoteEV3.Command.stop);
                    }
                }
            }
            Delay.msDelay(DELAY_PERIOD_SHORT);
        } while (this.getEv3devData().getSensorsData().isRunning());

        log.info("{} stopped", this.getClass().getSimpleName());
    }

    public EV3devData getEv3devData() {
        return ev3devData;
    }
}
