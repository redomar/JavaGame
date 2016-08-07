package com.redomar.game.entities;

import com.redomar.game.InputHandler;
import com.redomar.game.level.LevelHandler;

import java.net.InetAddress;

public class PlayerMP extends Player {

	public InetAddress ipAddess;
	public int port;

	public PlayerMP(LevelHandler level, int x, int y, InputHandler input,
					String userName, InetAddress ipAddress, int port, int shirtCol, int faceCol) {
		super(level, x, y, input, userName, shirtCol, faceCol);
		this.ipAddess = ipAddress;
		this.port = port;
	}

	public PlayerMP(LevelHandler level, int x, int y, String userName,
					InetAddress ipAddress, int port, int shirtCol, int faceCol) {
		super(level, x, y, null, userName, shirtCol, faceCol);
		this.ipAddess = ipAddress;
		this.port = port;
	}

	@Override
	public void tick() {
		super.tick();
	}

}
