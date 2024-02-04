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
	private boolean mute = false;
	private boolean highlight = false;

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
		String ANSI_RESET = "\u001B[0m";
		String ANSI_CYAN = "\u001B[36m";
		String ANSI_BACKGROUND_BLACK = "\u001B[40m";
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

		if (mute) return;
		String formattedStringForConsole = String.format("%s %s\t %s%n", msgTime, msgType, message);
		if (highlight) {
			formattedStringForConsole = String.format("%s %s\t%s %s%s %s%n", msgTime, msgType, ANSI_BACKGROUND_BLACK, ANSI_CYAN, message, ANSI_RESET);
			highlight = false;
		}

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

	/**
	 * Highlight the message in the console
	 *
	 * @return Printer
	 */
	public Printer highlight() {
		this.highlight = true;
		return this;
	}

	public boolean removeLog() {
		return new File(".log.txt").delete();
	}

	public String getMessage() {
		return message;
	}

	public void mute() {
		this.mute = true;
	}

	@SuppressWarnings("unused")
	public void unmute() {
		this.mute = false;
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
