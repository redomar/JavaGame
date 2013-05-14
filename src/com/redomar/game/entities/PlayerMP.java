package com.redomar.game.entities;

import java.net.InetAddress;

import com.redomar.game.InputHandler;
import com.redomar.game.level.LevelHandler;

public class PlayerMP extends Player{
	
	public InetAddress ipAddess;
	public int port;

	public PlayerMP(LevelHandler level, int x, int y, InputHandler input, String userName, InetAddress ipAddress, int port) {
		super(level, x, y, input, userName);
		this.ipAddess = ipAddress;
		this.port = port;
	}
	
	public PlayerMP(LevelHandler level, int x, int y, String userName, InetAddress ipAddress, int port) {
		super(level, x, y, null, userName);
		this.ipAddess = ipAddress;
		this.port = port;
	}
	
	@Override
	public void tick(){
		super.tick();
	}

}
