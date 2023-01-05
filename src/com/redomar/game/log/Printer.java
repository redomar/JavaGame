package com.redomar.game.log;

import com.redomar.game.lib.Time;

import java.io.File;
import java.util.Arrays;

public class Printer {

	private static int lineNumber = 0;
	private final Time time = new Time();
	private PrintTypes type;
	private String message;
	private boolean evalTypeAsException = false;

	public Printer() {
		this.type = PrintTypes.GAME;
	}

	public Printer(PrintTypes type) {
		this.type = type;
	}

	public void print(String message, PrintTypes type) {
		this.type = type;
		this.message = message;
		printOut();
	}

	public void print(String message) {
		this.message = message;
		printOut();
	}

	private void printOut() {
		String msgTime = "[" + time.getTime() + "]";
		String msgType = "[" + type.toString() + "]";

		PrintToLog logFile = printToLogType(type);
		if (lineNumber == 0) {

			String dashes = "";
			String title = ("[" + time.getTimeDate() + "]");
			char dash = '-';
			int number = Math.max((title.length() / 3), 10);

			char[] repeat = new char[number];
			Arrays.fill(repeat, dash);
			dashes += new String(repeat);

			logFile.log(String.format("%s%s%s", dashes, title, dashes));
			lineNumber++;
		}
		logFile.log(String.format("%s%s\t%s", msgTime, msgType, message));

		String formattedStringForConsole = String.format("%s%s %s%n", msgType, msgTime, message);

		if (type.equals(PrintTypes.ERROR) || evalTypeAsException) {
			System.err.printf(formattedStringForConsole);
			evalTypeAsException = false;
		} else {
			System.out.printf(formattedStringForConsole);
		}
	}

	private PrintToLog printToLogType(PrintTypes type) {
		if (type == PrintTypes.TEST) {
			return new PrintToLog(".PrintType-TEST.txt");
		} else {
			return new PrintToLog(".log.txt");
		}
	}

	public boolean removeLog() {
		return new File(".log.txt").delete();
	}

	public String getMessage() {
		return message;
	}

	/**
	 * Error that keeps the current {@link PrintTypes PrintType } instead of using {@link PrintTypes PrintTypes.ERROR}
	 *
	 * @return Printer
	 */
	public Printer cast() {
		this.evalTypeAsException = true;
		return this;
	}

	/**
	 * Print exception as message, cast to keep {@link PrintTypes PrintType }
	 *
	 * @param exceptionMessage message to print
	 * @return Printer
	 */
	public Printer exception(String exceptionMessage) {
		if (!this.evalTypeAsException) this.type = PrintTypes.ERROR;
		print(exceptionMessage);
		return this;
	}
}
