package com.redomar.game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.redomar.game.entities.Player;
import com.redomar.game.entities.PlayerMP;
import com.redomar.game.gfx.Screen;
import com.redomar.game.gfx.SpriteSheet;
import com.redomar.game.level.LevelHandler;
import com.redomar.game.net.GameClient;
import com.redomar.game.net.GameServer;
import com.redomar.game.net.packets.Packet00Login;
import com.thehowtotutorial.splashscreen.JSplash;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	// Setting the size and name of the frame/canvas
	public static final int WIDTH = 160;
	public static final int HEIGHT = (WIDTH / 3 * 2);
	public static final int SCALE = 3;
	public static final String NAME = "Game";
	public static Game game;
	private static int Jdata_Host;
	private static String Jdata_UserName = "";	

	private JFrame frame;

	public boolean running = false;
	public int tickCount = 0;

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
			BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer())
			.getData();
	private int[] colours = new int[6 * 6 * 6];

	private Screen screen;
	private InputHandler input;
	private WindowHandler window;
	private LevelHandler level;
	private Player player;

	private GameClient socketClient;
	private GameServer socketServer;
	
	public Game() {
		setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		setFrame(new JFrame(NAME));
		getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getFrame().setLayout(new BorderLayout());
		getFrame().add(this, BorderLayout.CENTER);
		getFrame().pack();
		getFrame().setResizable(false);
		getFrame().setLocationRelativeTo(null);
		getFrame().setVisible(true);

	}

	public void init() {
		game = this;
		int index = 0;
		for (int r = 0; r < 6; r++) {
			for (int g = 0; g < 6; g++) {
				for (int b = 0; b < 6; b++) {
					int rr = (r * 255 / 5);
					int gg = (g * 255 / 5);
					int bb = (b * 255 / 5);
					colours[index++] = rr << 16 | gg << 8 | bb;
				}
			}
		}

		screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/sprite_sheet.png"));
		input = new InputHandler(this);
		setWindow(new WindowHandler(this));
		setLevel(new LevelHandler("/levels/water_level.png"));

		setPlayer(new PlayerMP(getLevel(), 100, 100, input,
				Jdata_UserName, null, -1));

		level.addEntity(player);
		Packet00Login loginPacket = new Packet00Login(player.getUsername(), player.x, player.y);

		if (socketServer != null) {
			socketServer.addConnection((PlayerMP) getPlayer(), loginPacket);
		}

		// socketClient.sendData("ping".getBytes());
		loginPacket.writeData(getSocketClient());
	}

	public synchronized void start() {
		running = true;
		new Thread(this).start();

		if (Jdata_Host == 0) {
			socketServer = new GameServer(this);
			socketServer.start();
		}

		setSocketClient(new GameClient(this, "127.0.0.1"));
		getSocketClient().start();
	}

	public synchronized void stop() {
		running = false;
	}

	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 60D;

		int ticks = 0;
		int frames = 0;

		long lastTimer = System.currentTimeMillis();
		double delta = 0;

		init();

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;

			while (delta >= 1) {
				ticks++;
				tick();
				delta -= 1;
				shouldRender = true;
			}

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (shouldRender) {
				frames++;
				render();
			}

			if (System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				getFrame().setTitle("Frames: " + frames + " Ticks: " + ticks);
				frames = 0;
				ticks = 0;
			}
		}

	}

	public void tick() {
		tickCount++;
		getLevel().tick();
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		int xOffset = getPlayer().x - (screen.width / 2);
		int yOffset = getPlayer().y - (screen.height / 2);

		getLevel().renderTiles(screen, xOffset, yOffset);

		/*
		 * for (int x = 0; x < level.width; x++) { int colour = Colours.get(-1,
		 * -1, -1, 000); if (x % 10 == 0 && x != 0) { colour = Colours.get(-1,
		 * -1, -1, 500); } Font.render((x % 10) + "", screen, 0 + (x * 8), 0,
		 * colour, 1); }
		 */

		getLevel().renderEntities(screen);

		for (int y = 0; y < screen.height; y++) {
			for (int x = 0; x < screen.width; x++) {
				int colourCode = screen.pixels[x + y * screen.width];
				if (colourCode < 255) {
					pixels[x + y * WIDTH] = colours[colourCode];
				}
			}
		}

		Graphics g = bs.getDrawGraphics();

		g.drawRect(0, 0, getWidth(), getHeight());
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		try {
			JSplash splash = new JSplash(Game.class.getResource("/splash/splash.png"), true, true, false, "v1.4 Alpha", null, Color.RED, Color.ORANGE);
			splash.splashOn();
			splash.setProgress(10, "Initializing Game");
			Thread.sleep(750);
			splash.setProgress(25, "Loading Classes");
			Thread.sleep(500);
			splash.setProgress(35, "Applying Configurations");
			Thread.sleep(500);
			splash.setProgress(40, "Loading Sprites");
			Thread.sleep(750);
			splash.setProgress(50, "Loading Textures");
			Thread.sleep(500);
			splash.setProgress(60, "Loading Map");
			Thread.sleep(1500);
			splash.setProgress(80, "Configuring Map");
			Thread.sleep(500);
			splash.setProgress(90, "Pulling InputPanes");
			Thread.sleep(750);
			splash.setProgress(92, "Aquring data: Multiplayer");
			Thread.sleep(200);
			Jdata_Host = JOptionPane.showConfirmDialog(game, "Do you want to be the HOST?");
			Thread.sleep(200);
			splash.setProgress(95, "Aquring data: Username");
			Thread.sleep(200);
			splash.setProgress(96, "Initalizing as Server:Host");
			Jdata_UserName = JOptionPane.showInputDialog(game, "Enter a name");
			splash.setProgress(97, "Connecting as" + Jdata_UserName);
			Thread.sleep(500);
			splash.splashOff();
			new Game().start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public GameClient getSocketClient() {
		return socketClient;
	}

	public void setSocketClient(GameClient socketClient) {
		this.socketClient = socketClient;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public LevelHandler getLevel() {
		return level;
	}

	public void setLevel(LevelHandler level) {
		this.level = level;
	}

	public WindowHandler getWindow() {
		return window;
	}

	public void setWindow(WindowHandler window) {
		this.window = window;
	}

}
