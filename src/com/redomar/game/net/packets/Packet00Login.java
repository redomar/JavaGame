package com.redomar.game.net.packets;

import com.redomar.game.net.GameClient;
import com.redomar.game.net.GameServer;

public class Packet00Login extends Packet {

	private String username;

	public Packet00Login(byte[] data) {
		super(00);
		this.username = readData(data);
	}

	public Packet00Login(String username) {
		super(00);
		this.username = username;
	}

	@Override
	public byte[] getData() {
		return ("00" + this.username).getBytes();
	}

	@Override
	public void writeData(GameClient client) {
		client.sendData(getData());
	}

	@Override
	public void writeData(GameServer server) {
		server.sendDataToAllClients(getData());
	}

	public String getUsername() {
		return username;
	}
}
