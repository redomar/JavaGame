package com.redomar.game;

import com.redomar.game.audio.AudioHandler;
import com.redomar.game.entities.Dummy;
import com.redomar.game.entities.Player;
import com.redomar.game.entities.Vendor;
import com.redomar.game.gfx.Screen;
import com.redomar.game.gfx.SpriteSheet;
import com.redomar.game.level.LevelHandler;
import com.redomar.game.lib.Font;
import com.redomar.game.lib.Time;
import com.redomar.game.script.PrintTypes;
import com.redomar.game.script.Printing;
import org.apache.commons.lang3.text.WordUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.im.InputContext;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/*
 * This module forms the core architecture of the JavaGame. It coordinates the various
 * audio and input handler components, generates the frame, renders the screen graphics, spawns 
 * NPCs and customizes the player. Game is also responsible for changing the maps and levels, as well
 * as displaying various messages on the screen (e.g. fps)
 */
public class Game extends Canvas implements Runnable {

	// Setting the size and name of the frame/canvas
	private static final long serialVersionUID = 1L;
	private static final String game_Version = "v1.8.3 Alpha";	// Current version of the game
	private static final int WIDTH = 160;				// The width of the screen
	private static final int HEIGHT = (WIDTH / 3 * 2);		// The height of the screen (two thirds of the width)
	private static final int SCALE = 3;				// Scales the size of the screen
	private static final String NAME = "Game";			// The name of the JFrame panel
	private static Game game;
	private static Time time = new Time();				// Date object that represents the calender's time value, in hh:mm:ss
	
	// The properties of the player, npc, and fps/tps
	private static int Jdata_Host;					// The host of a multiplayer game (only available in earlier versions)
	private static String Jdata_UserName = "";			// The player's username (initialized as an empty string)
	private static String Jdata_IP = "127.0.0.1";			// Displays an IP address
	private static boolean changeLevel = false;			// Determines whether the level should change (initialized to not change)
	private static boolean npc = false;				// Non-player character (NPC) initialized to non-existing
	private static int map = 0;					// Map of the level, initialized to no map (0)
	private static int shirtCol;					// The colour of the character's shirt
	private static int faceCol;					// The colour (ethnicity) of the character (their face)
	private static boolean[] alternateCols = new boolean[2];	// Boolean array with 2 elements (for determining shirt and face colour), all initialized to false
	private static int fps;						// The frame rate (frames per second), frequency at which images are displayed on the canvas 
	private static int tps;						// The ticks (ticks per second), unit measure of time for one iteration of the game logic loop.
	private static int steps;							
	private static boolean devMode;				// Determines whether the game is in developer mode
	private static boolean closingMode;				// Determines whether the game will exit

	// Audio, input, and mouse handler objects
	private static JFrame frame;				// Frame with support for JFC/swing component architecture
	private static AudioHandler backgroundMusic;		// AudioHandler object that can play music in the background (but can't turn it off)
	private static boolean running = false;			// Determines whether the game is currently in process (i.e. whether the game is running)
	private static InputHandler input;			// InputHandler object that accepts keyboard input and follows the appropriate actions
	private static MouseHandler mouse;			// MouseHandler object that tracks mouse movement and clicks, and follows the appropriate actions
	private static InputContext context;			// InputContext object that provides methods to control text input facilities
	private int tickCount = 0;				// Updates the number of ticks
	
	// Graphics
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT,		// Describes a rasterized image with dimensions WIDTH and HEIGHT, and RGB colour
			BufferedImage.TYPE_INT_RGB);	
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer())	// Array of red, green and blue values for each pixel, as well as an alpha value (if there is an alpha channel)
			.getData();
	private int[] colours = new int[6 * 6 * 6];					// Array of 216 unique colours (6 shades of R, 6 of G, and 6 of B)
	private BufferedImage image2 = new BufferedImage(WIDTH, HEIGHT - 30,
			BufferedImage.TYPE_INT_RGB);
	private Screen screen;						// Screen object that accepts a width, height, and sprite sheet - to generate and render a screen
	private WindowHandler window;
	private LevelHandler level;					// LevelHandler object that loads and renders levels along with tiles, entities, projectiles and more. 
	
	//The entities of the game
	private Player player;				// This is the actual player	
	private Dummy dummy;			// This is a dummy npc (follows the player around)
	private Vendor vendor;				// This is a vendor npc (random movement)
	private Font font = new Font();			// Font object capable of displaying 2 fonts: Arial and Segoe UI
	private String nowPlaying;					
	private boolean notActive = true;			
	private int trigger = 0;					
	private Printing print = new Printing();		// Print object that can display various messages and error logs

	/**
	 * @author Redomar
	 * @version Alpha 1.8.3
	 */
	public Game() {
		context = InputContext.getInstance();	// Stores the input context for the window
		
		// The game can only be played in one distinct window size
		setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		setFrame(new JFrame(NAME));						// Creates the frame
		getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		// Exits the program when user closes the frame
		getFrame().setLayout(new BorderLayout());				// Border lays out a container
		getFrame().add(this, BorderLayout.CENTER);				// Centers the canvas inside the JFrame
		getFrame().pack();							// Sizes the frame so that all its contents are at or above their preferred sizes
		getFrame().setResizable(false);						// Prevents the user from resizing the frame
		getFrame().setLocationRelativeTo(null);					// Centres the window on the screen
		getFrame().setVisible(true);						// Displays the frame

		requestFocus();
		setDevMode(false);
		setClosing(false);
	}

	/*
	 * This method will spawn a dummy NPC into the level only if they are allowed to be spawned in. 
	 * They will be spawned at position (100, 150)  with a red shirt and caucasian face.
	 */
	public static void npcSpawn() {
		if (isNpc() == true) {	// If NPCs are allowed in the level
			game.setDummy(new Dummy(game.level, "Dummy", 100, 150, 500,	// Set a new dummy NPC on the current game level, with name 'Dummy'
					543));							// at position (100, 150), with shirt colour equal to 500 (red) and face colour equal to 543 (caucasian)
			game.level.addEntity(Game.getDummy());				// Add the previously defined dummy NPC to the game level
		}
	}

	/*
	 * This method will remove a dummy NPC from the level only if they are not allowed to be in it. 
	 */
	public static void npcKill() {
		if (isNpc() == false) {	// If NPCs are not allowed in the level
			game.level.removeEntity(Game.getDummy());	// Remove the current dummy NPC from the level
		}
	}

	public static JFrame getFrame() {
		return Game.frame;
	}

	public static void setFrame(JFrame frame) {
		Game.frame = frame;
	}

	public static Player getPlayer() {
		return game.player;
	}

	public void setPlayer(Player player) {
		game.player = player;
	}

	public static LevelHandler getLevel() {
		return game.level;
	}

	public void setLevel(LevelHandler level) {
		this.level = level;
	}

	public static Time getTime() {
		return Game.time;
	}

	public void setTime(Time time) {
		Game.time = time;
	}

	public static Game getGame() {
		return game;
	}

	public static void setGame(Game game) {
		Game.game = game;
	}

	public static boolean isRunning() {
		return running;
	}

	public static void setRunning(boolean running) {
		Game.running = running;
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

	public void setMap(String Map_str) {
		setLevel(new LevelHandler(Map_str));
		if (alternateCols[0]) {			// If the first element (shirt colour) is set to True
			Game.setShirtCol(240);		// The player's shirt colour is set to 240 (green)
		}
		if (!alternateCols[0]) {			// If the first element (shirt colour) is set to False
			Game.setShirtCol(111);		// The player's shirt colour is set to 111 (black)
		}
		if (alternateCols[1]) {			// If the last element (face colour) is set to True
			Game.setFaceCol(310);		// The player's face colour is set to 310 (African)
		}
		if (!alternateCols[1]) {			// If the last element (face colour) is set to False
			Game.setFaceCol(543);		// The player's face colour is set to 543 (caucasian)
		}
		setPlayer(new Player(level, 100, 100, input,			// Create a new player on the current level, at position (100, 100), with an InputHandler (i.e. keyboard),
				getJdata_UserName(), shirtCol, faceCol));	// Username, shirt colour and face colour. 
		level.addEntity(player);						// Add the specified player to the level
	}

	public static void setMap(int map) {
		Game.map = map;
	}

	public static boolean isNpc() {
		return npc;
	}

	public static void setNpc(boolean npc) {
		Game.npc = npc;
	}

	public static Dummy getDummy() {
		return game.dummy;
	}

	public void setDummy(Dummy dummy) {
		this.dummy = dummy;
	}

	public static String getJdata_IP() {
		return Jdata_IP;
	}

	public static void setJdata_IP(String jdata_IP) {
		Jdata_IP = jdata_IP;
	}

	public static int getJdata_Host() {
		return Jdata_Host;
	}

	public static void setJdata_Host(int jdata_Host) {
		Jdata_Host = jdata_Host;
	}

	public static String getJdata_UserName() {
		return Jdata_UserName;
	}

	public static void setJdata_UserName(String jdata_UserName) {
		Jdata_UserName = jdata_UserName;
	}

	public static String getGameVersion() {
		return game_Version;
	}

	public static int getShirtCol() {
		return shirtCol;
	}

	public static void setShirtCol(int shirtCol) {
		Game.shirtCol = shirtCol;
	}

	public static int getFaceCol() {
		return faceCol;
	}

	public static void setFaceCol(int faceCol) {
		Game.faceCol = faceCol;
	}

	public static boolean[] getAlternateCols() {
		return alternateCols;
	}

	// Sets alternateCols to a new boolean array (with size 2)
	public static void setAlternateCols(boolean[] alternateCols) {
		Game.alternateCols = alternateCols;
	}

	// Sets the second/last element (face colour) of alternateCols to true or false
	public static void setAlternateColsR(boolean alternateCols) {
		Game.alternateCols[1] = alternateCols;
	}

	// Sets the first element (shirt colour) of alternateCols to true or false
	public static void setAlternateColsS(boolean alternateCols) {
		Game.alternateCols[0] = alternateCols;
	}

	public static void setBackgroundMusic(AudioHandler backgroundMusic) {
		Game.backgroundMusic = backgroundMusic;
	}

	public static AudioHandler getBackgroundMusic(){
		return Game.backgroundMusic;
	}

	public static InputHandler getInput() {
		return input;
	}

	public void setInput(InputHandler input) {
		Game.input = input;
	}

	public static MouseHandler getMouse() {
		return mouse;
	}

	public static void setMouse(MouseHandler mouse) {
		Game.mouse = mouse;
	}

	public static boolean isDevMode() {
		return devMode;
	}

	public static void setDevMode(boolean devMode) {
		Game.devMode = devMode;
	}

	public static boolean isClosing() {
		return closingMode;
	}

	public static void setClosing(boolean closing) {
		Game.closingMode = closing;
	}

	/*
	* This method initializes the game once it starts. It populates the colour array with actual colours (6 shades each of RGB).
	* This method also builds the game level, spawns a new vendor NPC, and begins accepting keyboard and mouse input/tracking. 
	*/
	public void init() {
		setGame(this);
		int index = 0;
		for (int r = 0; r < 6; r++) {			// For r in all 6 shades of R
			for (int g = 0; g < 6; g++) {		// For g in all 6 shades of G
				for (int b = 0; b < 6; b++) {	// For b in all 6 shades of B
					int rr = (r * 255 / 5);	// 255 used instead of 256 colours in order to have a transparent colour. Splits 255 into 6 sections/shades 
					int gg = (g * 255 / 5);	// (e.g. 0, 51, 102, 153, etc.) for each of rr, gg, and bb. 
					int bb = (b * 255 / 5);
					colours[index++] = rr << 16 | gg << 8 | bb;	// All colour values (RGB) are placed into one 32-bit integer by using 'shift' and 'or' operations, populating the colour array
				}
			}
		}

		screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/sprite_sheet.png"));	// Loads a new screen with a width, height, and SpriteSheet
		input = new InputHandler(this);							// Input begins to record key presses
		setMouse(new MouseHandler(this));						// Mouse tracking and clicking is now recorded
		setWindow(new WindowHandler(this));
		setMap("/levels/custom_level.png");						// custom_level.png map is set upon initialization
		setMap(1);									// Map set to 1 (custom_level.png)

		game.setVendor(new Vendor(level, "Vendor", 215, 215, 304, 543));		// Set a new vendor NPC on the custom_level, with name "Vendor", at position (215, 215), with shirt colour 304 (purple) and face colour 543 (caucasian)
		level.addEntity(getVendor());							// Add the previously defined vendor NPC to the game level
	}

	/*
	* This method will start the game and allow the user to start playing
	*/
	public synchronized void start() {
		Game.setRunning(true);		// Game will run
		new Thread(this, "GAME").start();	// Thread is an instance of Runnable. Whenever it is started, it will run the run() method 
	}

	/*
	* This method will stop the game
	*/
	public synchronized void stop() {
		Game.setRunning(false);		// Game will not run
	}

	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 60D;

		int ticks = 0;
		int frames = 0;

		long lastTimer = System.currentTimeMillis();
		double delta = 0;

		init();

		while (Game.isRunning()) {
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
				getFrame().setTitle(
						"JavaGame - Version "
								+ WordUtils.capitalize(game_Version).substring(
								1, game_Version.length()));
				fps = frames;
				tps = ticks;
				frames = 0;
				ticks = 0;
			}
		}

	}

	public void tick() {
		setTickCount(getTickCount() + 1);
		level.tick();
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		int xOffset = (int) getPlayer().getX() - (screen.getWidth() / 2);
		int yOffset = (int) getPlayer().getY() - (screen.getHeight() / 2);

		level.renderTiles(screen, xOffset, yOffset);

		/*
		 * for (int x = 0; x < level.width; x++) { int colour = Colours.get(-1,
		 * -1, -1, 000); if (x % 10 == 0 && x != 0) { colour = Colours.get(-1,
		 * -1, -1, 500); } Font.render((x % 10) + "", screen, 0 + (x * 8), 0,
		 * colour, 1); }
		 */

		level.renderEntities(screen);
		level.renderProjectileEntities(screen);

		for (int y = 0; y < screen.getHeight(); y++) {
			for (int x = 0; x < screen.getWidth(); x++) {
				int colourCode = screen.getPixels()[x + y * screen.getWidth()];
				if (colourCode < 255) {
					pixels[x + y * WIDTH] = colours[colourCode];
				}
			}
		}

		if (isChangeLevel() == true && getTickCount() % 60 == 0) {
			Game.setChangeLevel(true);
			setChangeLevel(false);
		}

		if (changeLevel == true) {
			print.print("Teleported into new world", PrintTypes.GAME);
			if (getMap() == 1) {
				setMap("/levels/water_level.png");
				if (getDummy() != null) { // Gave nullPointerException(); upon
					// entering new world.
					level.removeEntity(getDummy());
					setNpc(false);
				}
				level.removeEntity(getVendor());
				setMap(2);
			} else if (getMap() == 2) {
				setMap("/levels/custom_level.png");
				level.removeEntity(getDummy());
				setNpc(false);
				level.addEntity(getVendor());
				setMap(1);
			}
			changeLevel = false;
		}

		Graphics g = bs.getDrawGraphics();
		g.drawRect(0, 0, getWidth(), getHeight());
		g.drawImage(image, 0, 0, getWidth(), getHeight() - 30, null);
		status(g, isDevMode(), isClosing());
		// Font.render("Hi", screen, 0, 0, Colours.get(-1, -1, -1, 555), 1);
		g.drawImage(image2, 0, getHeight() - 30, getWidth(), getHeight(), null);
		g.setColor(Color.WHITE);
		g.setFont(font.getSegoe());
		g.drawString(
				"Welcome "
						+ WordUtils.capitalizeFully(player
						.getSanitisedUsername()), 3, getHeight() - 17);
		g.setColor(Color.ORANGE);

		if (context.getLocale().getCountry().equals("BE")
				|| context.getLocale().getCountry().equals("FR")) {
			g.drawString("Press A to quit", (getWidth() / 2)
					- ("Press A to quit".length() * 3), getHeight() - 17);
		} else {
			g.drawString("Press Q to quit", (getWidth() / 2)
					- ("Press Q to quit".length() * 3), getHeight() - 17);
		}
		g.setColor(Color.YELLOW);
		g.drawString(time.getTime(), (getWidth() - 58), (getHeight() - 3));
		g.setColor(Color.GREEN);
		if(backgroundMusic.getActive()) {
			g.drawString("MUSIC is ON ", 3, getHeight() - 3);
		}
		g.dispose();
		bs.show();
	}

	private void status(Graphics g, boolean TerminalMode, boolean TerminalQuit) {
		if (TerminalMode == true) {
			g.setColor(Color.CYAN);
			g.drawString("JavaGame Stats", 0, 10);
			g.drawString("FPS/TPS: " + fps + "/" + tps, 0, 25);
			if ((player.getNumSteps() & 15) == 15) {
				steps += 1;
			}
			g.drawString("Foot Steps: " + steps, 0, 40);
			g.drawString(
					"NPC: " + WordUtils.capitalize(String.valueOf(isNpc())), 0,
					55);
			g.drawString("Mouse: " + getMouse().getX() + "x |"
					+ getMouse().getY() + "y", 0, 70);
			if (getMouse().getButton() != -1)
				g.drawString("Button: " + getMouse().getButton(), 0, 85);
			g.setColor(Color.CYAN);
			g.fillRect(getMouse().getX() - 12, getMouse().getY() - 12, 24, 24);
		}
		if (TerminalQuit == true) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(Color.RED);
			g.drawString("Shutting down the Game", (getWidth() / 2) - 70,
					(getHeight() / 2) - 8);
			g.dispose();
		}
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

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

}
