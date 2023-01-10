package com.redomar.game.log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by Mohamed on 28/08/2016.
 * This file tests the com.redomar.game.script.Printing class
 */
public class PrinterTest {

	private Printer printer;

	@Before
	public void setUp() {
		printer = new Printer();
	}

	@Test
	public void printToFileWorks() {
		printer.print("TESTING STRING", PrintTypes.TEST);
		File file = new File(".PrintType-TEST.txt");
		assertTrue(file.exists());
		assertTrue(file.delete());
	}

	@Test
	public void getMessageIsNull() {
		assertNull(printer.getMessage());
	}

	@Test
	public void messageShouldBeNullAfterPrinting() {
		printer.print("Not Null", PrintTypes.TEST);
		assertNotNull(printer.getMessage());
	}

	@After
	public void cleanUp() {
		printer = null;
	}

}