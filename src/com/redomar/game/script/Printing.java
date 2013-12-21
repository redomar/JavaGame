package com.redomar.game.script;

import com.redomar.game.lib.Time;
import com.redomar.game.script.PrintTypes;

public class Printing {

	private PrintTypes type;
	private Time time = new Time();
	private String message;

	public Printing() {

	}

	public void print(String message, PrintTypes type) {
		this.type = type;
		setMessage(message);
		printOut();		
	}
	
	private void printOut(){
		String msgTime = "[" + time.getTime() + "]";
		String msgType = "[" + type.toString() + "]";
		System.out.println(msgType + msgTime + getMessage());
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
