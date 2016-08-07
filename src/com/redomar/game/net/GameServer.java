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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class GameServer extends Thread {

	private DatagramSocket socket;
	private Game game;
	private List<PlayerMP> connectedPlayers = new ArrayList<PlayerMP>();
	private Printing print = new Printing();

	public GameServer(Game game) {
		this.setGame(game);
		try {
			this.socket = new DatagramSocket(1331);
		} catch (SocketException e) {
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

			// String message = new String(packet.getData());
			// System.out.println("CLIENT ["+packet.getAddress().getHostAddress()+":"+packet.getPort()+"] "+message);
			// if(message.trim().equalsIgnoreCase("ping")){
			// sendData("pong".getBytes(), packet.getAddress(),
			// packet.getPort());
			// }
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
				print.print("[" + address.getHostAddress() + ":" + port
						+ "] " + ((Packet00Login) packet).getUsername()
						+ " has connected...", PrintTypes.SERVER);
				PlayerMP player = new PlayerMP(Game.getLevel(), 10, 10,
						((Packet00Login) packet).getUsername(), address, port, Game.getShirtCol(), Game.getFaceCol());
				this.addConnection(player, (Packet00Login) packet);
				break;
			case DISCONNECT:
				packet = new Packet01Disconnect(data);
				print.print("[" + address.getHostAddress() + ":" + port
						+ "] " + ((Packet01Disconnect) packet).getUsername()
						+ " has disconnected...", PrintTypes.SERVER);
				this.removeConnection((Packet01Disconnect) packet);
				break;
			case MOVE:
				packet = new Packet02Move(data);
				this.handleMove(((Packet02Move) packet));
		}
	}

	private void handleMove(Packet02Move packet) {
		if (getPlayerMP(packet.getUsername()) != null) {
			int index = getPlayerMPIndex(packet.getUsername());
			PlayerMP player = this.connectedPlayers.get(index);
			player.setX(packet.getX());
			player.setY(packet.getY());
			player.setNumSteps(packet.getNumSteps());
			player.setMoving(packet.isMoving());
			player.setMovingDir(packet.getMovingDir());
			packet.writeData(this);
		}
	}

	public void addConnection(PlayerMP player, Packet00Login packet) {
		boolean alreadyConnected = false;
		for (PlayerMP p : this.connectedPlayers) {
			if (player.getUsername().equalsIgnoreCase(p.getUsername())) {
				if (p.ipAddess == null) {
					p.ipAddess = player.ipAddess;
				}

				if (p.port == -1) {
					p.port = player.port;
				}

				alreadyConnected = true;
			} else {
				sendData(packet.getData(), p.ipAddess, p.port);

				packet = new Packet00Login(p.getUsername(), (int) p.getX(), (int) p.getY());
				sendData(packet.getData(), player.ipAddess, player.port);
			}
		}
		if (!alreadyConnected) {
			this.connectedPlayers.add(player);
		}
	}

	public void removeConnection(Packet01Disconnect packet) {
		this.connectedPlayers.remove(getPlayerMPIndex(packet.getUsername()));
		packet.writeData(this);
	}

	public PlayerMP getPlayerMP(String username) {
		for (PlayerMP player : this.connectedPlayers) {
			if (player.getUsername().equalsIgnoreCase(username)) {
				return player;
			}
		}
		return null;
	}

	public int getPlayerMPIndex(String username) {
		int index = 0;
		for (PlayerMP player : this.connectedPlayers) {
			if (player.getUsername().equalsIgnoreCase(username)) {
				break;
			} else {
				index++;
			}
		}
		return index;
	}

	public void sendData(byte[] data, InetAddress ipAddress, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length,
				ipAddress, port);
		try {
			this.socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendDataToAllClients(byte[] data) {
		for (PlayerMP p : connectedPlayers) {
			sendData(data, p.ipAddess, p.port);
		}
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
}
