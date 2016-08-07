package com.redomar.game.net;

import com.redomar.game.Game;
import com.redomar.game.entities.PlayerMP;
import com.redomar.game.net.packets.Packet;
import com.redomar.game.net.packets.Packet.PacketTypes;
import com.redomar.game.net.packets.Packet00Login;
import com.redomar.game.net.packets.Packet01Disconnect;
import com.redomar.game.net.packets.Packet02Move;
import com.redomar.game.script.PrintTypes;
import com.redomar.game.script.Printing;

import java.io.IOException;
import java.net.*;

public class GameClient extends Thread {

	private InetAddress ipAddress;
	private DatagramSocket socket;
	private Game game;
	private Printing print = new Printing();

	public GameClient(Game game, String ipAddress) {
		this.setGame(game);
		try {
			this.socket = new DatagramSocket();
			this.ipAddress = InetAddress.getByName(ipAddress);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.parsePacket(packet.getData(), packet.getAddress(),
					packet.getPort());
			// System.out.println("SERVER > "+new String(packet.getData()));
		}
	}

	private void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
		Packet packet = null;
		switch (type) {
			default:
			case INVALID:
				break;
			case LOGIN:
				packet = new Packet00Login(data);
				handleLogin((Packet00Login) packet, address, port);
				break;
			case DISCONNECT:
				packet = new Packet01Disconnect(data);
				print.print("[" + address.getHostAddress() + ":" + port
						+ "] " + ((Packet01Disconnect) packet).getUsername()
						+ " has disconnected...", PrintTypes.NETWORK);
				Game.getLevel().removeEntity(
						((Packet01Disconnect) packet).getUsername());
				break;
			case MOVE:
				packet = new Packet02Move(data);
				this.handleMove((Packet02Move) packet);
				break;
		}
	}

	private void handleLogin(Packet00Login packet, InetAddress address, int port) {
		print.print("[" + address.getHostAddress() + ":" + port + "] "
				+ packet.getUsername() + " has joined...", PrintTypes.NETWORK);
		PlayerMP player = new PlayerMP(Game.getLevel(), packet.getX(),
				packet.getY(), packet.getUsername(), address, port, Game.getShirtCol(), Game.getFaceCol());
		Game.getLevel().addEntity(player);
	}

	private void handleMove(Packet02Move packet) {
		Game.getLevel().movePlayer(packet.getUsername(), packet.getX(),
				packet.getY(), packet.getNumSteps(), packet.isMoving(),
				packet.getMovingDir());
	}

	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length,
				ipAddress, 1331);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

}
