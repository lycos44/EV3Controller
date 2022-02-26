package de.munro.ev3.watch;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;

class WatchDistanceTest {

    @Test
    void isToBeStopped() {
        WatchDistance watchDistance = new WatchDistance();

        MatcherAssert.assertThat(watchDistance.isToBeStopped(518), is(false));
        MatcherAssert.assertThat(watchDistance.isToBeStopped(518), is(true));
        MatcherAssert.assertThat(watchDistance.isToBeStopped(516), is(false));
        MatcherAssert.assertThat(watchDistance.isToBeStopped(10), is(true));
    }

    @Test
    void isTooClose() {
        WatchDistance watchDistance = new WatchDistance();

        MatcherAssert.assertThat(watchDistance.isTooClose(19), is(true));
        MatcherAssert.assertThat(watchDistance.isTooClose(20), is(true));
        MatcherAssert.assertThat(watchDistance.isTooClose(21), is(false));
    }

    @Test
    void isStuck() {
        WatchDistance watchDistance = new WatchDistance();

        MatcherAssert.assertThat(watchDistance.isStuck(518, 518), is(true));
        MatcherAssert.assertThat(watchDistance.isStuck(518, 520), is(false));
        MatcherAssert.assertThat(watchDistance.isStuck(518, 516), is(false));
    }
}