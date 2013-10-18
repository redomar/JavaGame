package com.redomar.game.script;

import com.redomar.game.lib.Time;

public class Printing {

	private int type = 0;
	private Time time = new Time();
	private String message;
	private String typeName = "[System]";
	
	public Printing(){
		
	}
	
	public void print(String message, int type){
		setType(type);
		setMessage(message);
		System.out.println("["+time.getTime()+"]"+type()+getMessage());		
	}
	
	private String type(){
		if (getType() == 1){
			this.typeName = "[GAME]";
		}else if(getType() == 2){
			this.typeName = "[MUSIC]";
		}
		
		return this.typeName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
