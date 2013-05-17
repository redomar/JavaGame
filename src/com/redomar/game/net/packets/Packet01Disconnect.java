package com.redomar.game.net.packets;

import com.redomar.game.net.GameClient;
import com.redomar.game.net.GameServer;

public class Packet01Disconnect extends Packet {

	private String username;

	public Packet01Disconnect(byte[] data) {
		super(01);
		this.username = readData(data);
	}

	public Packet01Disconnect(String username) {
		super(01);
		this.username = username;
	}

	@Override
	public byte[] getData() {
		return ("01" + this.username).getBytes();
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
