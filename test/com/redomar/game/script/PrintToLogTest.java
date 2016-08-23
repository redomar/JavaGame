package com.redomar.game.script;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Mohamed on 23/08/2016.
 *
 * This file tests the com.redomar.game.script.PrintToLog class
 */
public class PrintToLogTest {

	PrintToLog print;

	@Test(expected = NullPointerException.class)
	public void PrintToLogNullFile() {
		print = new PrintToLog(null);
	}

	@Test
	public void PrintToLogDoesWork() {
		print = new PrintToLog(".Test.txt");
		assertTrue(print.getUrl().exists());
		print.getUrl().delete();
	}

}
