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
import javax.swing.UIManager;

import org.apache.commons.lang3.text.WordUtils;

import com.redomar.game.entities.Dummy;
import com.redomar.game.entities.Player;
import com.redomar.game.entities.PlayerMP;
import com.redomar.game.gfx.Screen;
import com.redomar.game.gfx.SpriteSheet;
import com.redomar.game.level.LevelHandler;
import com.redomar.game.lib.Font;
import com.redomar.game.lib.Music;
import com.redomar.game.lib.Time;
import com.redomar.game.net.GameClient;
import com.redomar.game.net.GameServer;
import com.redomar.game.net.packets.Packet00Login;
import com.thehowtotutorial.splashscreen.JSplash;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	private static final String game_Version = "v1.5.4 Alpha";

	// Setting the size and name of the frame/canvas
	private static final int WIDTH = 160;
	private static final int HEIGHT = (WIDTH / 3 * 2);
	private static final int SCALE = 3;
	private static final String NAME = "Game";
	private static Game game;
	private static int Jdata_Host;
	private static String Jdata_UserName = "";
	private static String Jdata_IP = "127.0.0.1";
	private static boolean changeLevel = false;
	private static int map = 0;

	private JFrame frame;

	private boolean running = false;
	private int tickCount = 0;

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
			BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer())
			.getData();
	private int[] colours = new int[6 * 6 * 6];

	private BufferedImage image2 = new BufferedImage(WIDTH, HEIGHT - 30, BufferedImage.TYPE_INT_RGB);
	private Screen screen;
	private InputHandler input;
	private WindowHandler window;
	private LevelHandler level;
	private static Player player;
	private Dummy dummy;
	private Music music = new Music();
	private Time time = new Time();
	private Font font = new Font();
	private Thread musicThread = new Thread(music);
	private String nowPlaying;
	private boolean notActive = true;
	private boolean noAudioDevice = false;
	private int trigger = 0;
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
		setGame(this);
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
		setMap("/levels/custom_level.png");
		setMap(1);
		Packet00Login loginPacket = new Packet00Login(player.getUsername(), player.x, player.y);

		if (socketServer != null) {
			socketServer.addConnection((PlayerMP) getPlayer(), loginPacket);
		}

		// socketClient.sendData("ping".getBytes());
		loginPacket.writeData(getSocketClient());
	}

	public void setMap(String Map_str) {
		setLevel(new LevelHandler(Map_str));
		setPlayer(new PlayerMP(getLevel(), 100, 100, input,
				Jdata_UserName, null, -1));
		dummy = new Dummy(getLevel(), "h", 215, 215, 500, 543);
		level.addEntity(dummy);
		level.addEntity(player);
	}

	public synchronized void start() {
		running = true;
		new Thread(this).start();
		
		if (Jdata_Host == 0) {
			socketServer = new GameServer(this);
			socketServer.start();
		}

		setSocketClient(new GameClient(this, Jdata_IP));
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
			boolean shouldRender = false;

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
		setTickCount(getTickCount() + 1);
		getLevel().tick();
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		int xOffset = getPlayer().x - (screen.getWidth() / 2);
		int yOffset = getPlayer().y - (screen.getHeight() / 2);

		getLevel().renderTiles(screen, xOffset, yOffset);

		/*
		 * for (int x = 0; x < level.width; x++) { int colour = Colours.get(-1,
		 * -1, -1, 000); if (x % 10 == 0 && x != 0) { colour = Colours.get(-1,
		 * -1, -1, 500); } Font.render((x % 10) + "", screen, 0 + (x * 8), 0,
		 * colour, 1); }
		 */

		getLevel().renderEntities(screen);

		for (int y = 0; y < screen.getHeight(); y++) {
			for (int x = 0; x < screen.getWidth(); x++) {
				int colourCode = screen.getPixels()[x + y * screen.getWidth()];
				if (colourCode < 255) {
					pixels[x + y * WIDTH] = colours[colourCode];
				}
			}
		}
		
		if (noAudioDevice == false){
			if (input.isPlayMusic() == true && notActive == true){
				int musicOption = JOptionPane.showConfirmDialog(this, "You are about to turn on music and can be VERY loud", "Music Options", 2, 2);
				if (musicOption == 0){
					musicThread.start();
					notActive = false;
				} else {
					System.out.println("Canceled");
					input.setPlayMusic(false);
				}
			}			
		}
		
		if (input.isChangeLevel() == true && getTickCount() % 60 == 0){
			Game.setChangeLevel(true);
			input.setChangeLevel(false);
		}
		
		if (changeLevel == true){
			if(getMap() == 1){
				setMap("/levels/water_level.png");	
				setMap(2);
			}else if(getMap() == 2){
				setMap("/levels/custom_level.png");
				setMap(1);
			}
			changeLevel = false;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.drawRect(0, 0, getWidth(), getHeight());
		g.drawImage(image, 0, 0, getWidth(), getHeight()-30, null);
//		Font.render("Hi", screen, 0, 0, Colours.get(-1, -1, -1, 555), 1);
		g.drawImage(image2, 0, getHeight()-30, getWidth(), getHeight(), null);
		g.setColor(Color.WHITE);
		g.setFont(font.getSegoe());
		g.drawString("Welcome "+WordUtils.capitalizeFully(player.getSantizedUsername()), 3, getHeight()-17);
		g.setColor(Color.YELLOW);
		g.drawString(time.getTime(), (getWidth() - 58), (getHeight()-3));
		g.setColor(Color.WHITE);
		if(noAudioDevice == true){
			g.setColor(Color.RED);
			g.drawString("MUSIC is OFF | no audio device for playback", 3, getHeight()-3);
			trigger++;
			if(trigger == 25){
				JOptionPane.showMessageDialog(this, "No Audio device found", "Audio Issue", 0);
			}
		} else if (notActive == true){
			g.setColor(Color.RED);
			g.drawString("MUSIC is OFF | press 'M' to start", 3, getHeight()-3);
		} else{
			g.setColor(Color.GREEN);
			g.drawString("MUSIC is ON | You cannot turn off the music", 3, getHeight()-3);
			g.setColor(Color.WHITE);
			setNowPlaying(WordUtils.capitalize(music.getSongName()[music.getSongNumber()].substring(7, (music.getSongName()[music.getSongNumber()].length() - 4))));
			if (getNowPlaying().startsWith("T")){
				g.drawString(nowPlaying, getWidth() - (nowPlaying.length() * 9) + 12, getHeight() - 17);
			} else {
				g.drawString(nowPlaying, getWidth() - (nowPlaying.length() * 9) + 8, getHeight() - 17);
			}
		}
		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		try {
			JSplash splash = new JSplash(Game.class.getResource("/splash/splash.png"), true, true, false, game_Version, null, Color.RED, Color.ORANGE);
			splash.toFront();
			splash.splashOn();
			splash.setProgress(10, "Initializing Game");
			Thread.sleep(250);
			splash.setProgress(25, "Loading Classes");
			Thread.sleep(125);
			splash.setProgress(35, "Applying Configurations");
			Thread.sleep(125);
			splash.setProgress(40, "Loading Sprites");
			Thread.sleep(250);
			splash.setProgress(50, "Loading Textures");
			Thread.sleep(125);
			splash.setProgress(60, "Loading Map");
			Thread.sleep(500);
			splash.setProgress(80, "Configuring Map");
			Thread.sleep(125);
			splash.setProgress(90, "Pulling InputPanes");
			Thread.sleep(250);
			splash.setProgress(92, "Aquring data: Multiplayer");
			Thread.sleep(125);
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			Jdata_Host = JOptionPane.showConfirmDialog(getGame(), "Do you want to be the HOST?");
			if (Jdata_Host == 1){
				Jdata_IP = JOptionPane.showInputDialog(getGame(), "Enter the name \nleave blank for local");
			}
			Thread.sleep(125);
			splash.setProgress(95, "Aquring data: Username");
			Thread.sleep(125);
			splash.setProgress(96, "Initalizing as Server:Host");
			Jdata_UserName = JOptionPane.showInputDialog(getGame(), "Enter a name");
			splash.setProgress(97, "Connecting as" + Jdata_UserName);
			Thread.sleep(250);
			splash.splashOff();
			new Game().start();
//			new Menu().start();
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

	public static Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		Game.player = player;
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

	public String getNowPlaying() {
		return nowPlaying;
	}

	public void setNowPlaying(String nowPlaying) {
		this.nowPlaying = nowPlaying;
	}

	public int getTickCount() {
		return tickCount;
	}

	public void setTickCount(int tickCount) {
		this.tickCount = tickCount;
	}

	public static Game getGame() {
		return game;
	}

	public static void setGame(Game game) {
		Game.game = game;
	}

	public static boolean isChangeLevel() {
		return changeLevel;
	}

	public static void setChangeLevel(boolean changeLevel) {
		Game.changeLevel = changeLevel;
	}

	public static int getMap() {
		return map;
	}

	public static void setMap(int map) {
		Game.map = map;
	}

}
