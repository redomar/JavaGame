package com.redomar.game.script;

import com.redomar.game.lib.Time;

import java.util.Arrays;

public class Printing {

	private static int lineNumber = 0;
	private PrintTypes type;
	private Time time = new Time();
	private String message;
	private String msgTime;
	private String msgType;
	private boolean errorMode = false;
	private PrintToLog logFile;

	public Printing() {

	}

	public void print(String message, PrintTypes type) {
		this.type = type;
		setMessage(message);
		readMessageType(type);
		printOut();
	}

	private void printOut(){
		msgTime = "[" + time.getTime() + "]";
		msgType = "[" + type.toString() + "]";

		logFile = new PrintToLog(".log.txt");
		if (lineNumber == 0) {

			String dashes = "";
			char dash = '-';
			int number = 16;

			char[] repeat = new char[number];
			Arrays.fill(repeat, dash);
			dashes += new String(repeat);

			logFile.log(dashes + msgTime + dashes + "\n" + msgTime + msgType + this.getMessage());
			lineNumber++;
		} else {
			logFile.log(msgTime + msgType + this.getMessage());
		}


		if(errorMode) {
			System.err.println(msgType + msgTime + message);
		}else{
			System.out.println(msgType + msgTime + message);
		}
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private void readMessageType(PrintTypes type){
		this.errorMode = type == PrintTypes.ERROR;
	}
}
