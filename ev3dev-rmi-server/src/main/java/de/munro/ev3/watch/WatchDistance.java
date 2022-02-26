package de.munro.ev3.watch;

import lombok.extern.slf4j.Slf4j;

import static java.lang.Math.abs;

@Slf4j
public class WatchDistance {
    private static final int MINIMUM_DISTANCE = 20;
    private static final int TOLERANCE = 2;
    private int lastDistance = 0;

    /**
     * inspect the distance values caught and decide, whether the drive motor needs to be stopped
     * @param currentDistance currently measured distance
     * @return true, if either the car got stuck or the distance to a wall got to small
     *          false, else
     */
    public boolean isToBeStopped(int currentDistance) {
        int before = lastDistance;
        lastDistance = currentDistance;
        return isTooClose(currentDistance) || isStuck(before,currentDistance);
    }

    /**
     * inspect the distance values caught and decide, whether the got stuck
     * @param currentDistance currently measured distance
     * @return true, if either the car got stuck
     *          false, else
     */
    public boolean isStuck(int currentDistance) {
        int before = lastDistance;
        lastDistance = currentDistance;
        return isStuck(before,currentDistance);
    }

    /**
     * inspect the current distance to a wall
     * @param current distance
     * @return true, if the distance is smaller or equal to MINIMUM_DISTANCE
     *          false, else
     */
    protected boolean isTooClose(int current) {
        log.debug("tooClose: {}", current);
        return current <= MINIMUM_DISTANCE;
    }

    /**
     * compare the currently measured distance with the distance measured TIMES_CHECKED before
     * @param before distance measured TIMES_CHECKED before
     * @param current currently measured distance
     * @return true, the difference between the currently measured distance with the distance measured TIMES_CHECKED before is smaller or equal to TOLERANCE
     *          false, else
     */
    protected boolean isStuck(int before, int current) {
        int variation = abs(current - before);
        log.debug("stuck: {}, {}, {}", before, current, variation);
        return variation < TOLERANCE;
    }

    public void reset() {
        log.debug("reset()");
        lastDistance = 0;
    }
}
