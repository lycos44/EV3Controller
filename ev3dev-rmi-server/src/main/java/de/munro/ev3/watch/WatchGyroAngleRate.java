package de.munro.ev3.watch;

public class WatchGyroAngleRate {
    private static final int TIMES_CHECKED = 20;
    private static final double TOLERANCE = 0.5;
    private final int[] gyroAngleRateCourse = new int[TIMES_CHECKED];
    private int nextGyroAngleRate = 0;

    public boolean isToBeCorrected(int gyroAngleRate) {
        gyroAngleRateCourse[nextGyroAngleRate] = gyroAngleRate;
        nextGyroAngleRate = ++nextGyroAngleRate % gyroAngleRateCourse.length;
        int sum = 0;
        for(int angleRate : gyroAngleRateCourse) {
            sum += angleRate;
        }

        return (double) sum /TIMES_CHECKED > TOLERANCE;
    }
}