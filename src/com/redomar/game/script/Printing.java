package com.redomar.game.script;

import com.redomar.game.lib.Time;
import com.redomar.game.script.PrintTypes;

public class Printing {

	private PrintTypes type;
	private Time time = new Time();
	private String message;
	private boolean redMode = false;

	public Printing() {

	}

	public void print(String message, PrintTypes type) {
		this.type = type;
		setMessage(message);
		readMessageType(type);
		printOut();		
	}
	
	private void printOut(){
		String msgTime = "[" + time.getTime() + "]";
		String msgType = "[" + type.toString() + "]";
		if(redMode == true){
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
		if(type == PrintTypes.ERROR){
			this.redMode = true;
		} else {
			this.redMode = false;
		}
	}
}
