package com.redomar.game.log;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Mohamed on 23/08/2016.
 * <p>
 * This file tests the com.redomar.game.script.PrintToLog class
 */
public class PrintToLogTest {

	private PrintToLog print;

	@Test(expected = NullPointerException.class)
	public void PrintToLogNullFile() {
		print = new PrintToLog(null);
	}

	@Test
	public void PrintToLogDoesWork() {
		print = new PrintToLog(".PrintToLogDoesWork.txt");
		assertTrue(print.getUrl().exists());
		assertTrue(print.getUrl().delete());
	}

}
