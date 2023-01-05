package com.redomar.game.log;

import com.redomar.game.menu.PopUp;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PopUpTest {
	private PopUp popUp;

	@Before
	public void setUp() {
		popUp = new PopUp();
		popUp.active = false;
	}

	@Test
	public void warnIntEfflux() {
		assertEquals(1, popUp.Warn("TEST"));
	}

}
