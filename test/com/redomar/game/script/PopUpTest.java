package com.redomar.game.script;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PopUpTest {
    private PopUp popUp;

    @Before
    public void setUp() throws Exception {
        popUp = new PopUp();
        popUp.active = false;
    }

    @Test
    public void warnIntEfflux() throws Exception {
        assertEquals(1,popUp.Warn("TEST"));
    }

}
