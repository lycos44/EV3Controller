package de.munro.ev3.rmi;

public class EV3devStatus {

    public enum Direction {
        left,
        right,
        straight,
    }

    public enum Moving {
        forward,
        backward,
        stop,
    }

    private boolean toBeStopped = false;
    private DeviceRunner deviceRunner;
    private Direction direction = Direction.straight;
    private Moving moving = Moving.stop;

    /**
     * returns flag to stop processing
     * @return toBeStopped
     */
    boolean isToBeStopped() {
        return toBeStopped;
    }

    /**
     * set flag to stop processing
     * @param toBeStopped, flag, marking process to be stopped
     */
    void setToBeStopped(boolean toBeStopped) {
        this.toBeStopped = toBeStopped;
    }

    /**
     * provides information about the devices
     * @param deviceRunner, referencing all devices
     */
    void setDeviceRunner(DeviceRunner deviceRunner) {
        this.deviceRunner = deviceRunner;
    }

    /**
     * get the value of direction
     * @return direction, current value
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * set the value of direction
     * @param direction, value to be assigned
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * get the value of moving
     * @return moving
     */
    Moving getMoving() {
        return moving;
    }

    /**
     * set the value of moving
     * @param moving, value to be assigned
     */
    void setMoving(Moving moving) {
        this.moving = moving;
    }
}
