package de.munro.ev3.controller;

import static org.hamcrest.Matchers.is;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

class WatchThreadTest {

    @Test
    void noChangeInDistance() {
        WatchThread watchThread = new WatchThread(null, null);

        MatcherAssert.assertThat(watchThread.isStucked(0), is(true));
        MatcherAssert.assertThat(watchThread.isStucked(1), is(true));
        MatcherAssert.assertThat(watchThread.isStucked(2), is(false));
        MatcherAssert.assertThat(watchThread.isStucked(3), is(false));
    }
}