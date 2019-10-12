package de.munro.ev3.rmi;

public class EV3devStatus {

    public enum Turn {
        left,
        right,
        straight,
    }

    public enum Direction {
        forward,
        backward,
        stop,
    }

    public enum Climb {
        up,
        down
    }

    private boolean toBeStopped = false;
    private DeviceRunner deviceRunner;
    private Turn turn = Turn.straight;
    private Direction direction = Direction.stop;
    private Climb front = Climb.up;
    private Climb back = Climb.up;

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
     * get the value of turn
     * @return turn, current value
     */
    public Turn getTurn() {
        return turn;
    }

    /**
     * set the value of turn
     * @param turn, value to be assigned
     */
    public void setTurn(Turn turn) {
        this.turn = turn;
    }

    /**
     * get the value of direction
     * @return direction
     */
    Direction getDirection() {
        return direction;
    }

    /**
     * set the value of direction
     * @param direction, value to be assigned
     */
    void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * get the value of front
     * @return front
     */
    public Climb getFront() {
        return front;
    }

    /**
     * set the value of front
     * @param front, value to be assigned
     */
    public void setFront(Climb front) {
        this.front = front;
    }

    /**
     * get the value of back
     * @return back
     */
    public Climb getBack() {
        return back;
    }

    /**
     * set the value of
     * @param back, value to be assigned
     */
    public void setBack(Climb back) {
        this.back = back;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(toBeStopped).append(", ");
        sb.append(direction).append(", ");
        sb.append(turn).append(", ");
        sb.append(front).append(", ");
        sb.append(back);

        return sb.toString();
    }
}
