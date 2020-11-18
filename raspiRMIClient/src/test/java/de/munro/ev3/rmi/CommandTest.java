package de.munro.ev3.rmi;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CommandTest {

    @Test
    public void valueOf() {
        assertThat(Command.QUIT.getName(), is("q"));
        assertThat(Command.BEEP.getName(), is("beep"));
        assertThat(Command.FORWARD.getName() , is("forward"));
        assertThat(Command.BACKWARD.getName(), is("backward"));
        assertThat(Command.STOP.getName(), is("stop"));
        assertThat(Command.LEFT.getName(), is("left"));
        assertThat(Command.RIGHT.getName(), is("right"));
        assertThat(Command.STRAIGHT.getName(), is("straight"));
        assertThat(Command.FRONTUP.getName(), is("frontup"));
        assertThat(Command.FRONTDOWN.getName(), is("frontdown"));
        assertThat(Command.BACKUP.getName(), is("backup"));
        assertThat(Command.RESET.getName(), is("reset"));
        assertThat(Command.BACKDOWN.getName(), is("backdown"));
        assertThat(Command.SHUTDOWN.getName(), is("shutdown"));
    }
}
