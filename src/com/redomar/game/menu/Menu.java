package com.redomar.game.menu;

import com.redomar.game.Game;
import com.redomar.game.audio.AudioHandler;
import com.redomar.game.event.Mouse;
import com.redomar.game.lib.Font;
import com.redomar.game.log.PrintTypes;
import com.redomar.game.log.Printer;
import com.thehowtotutorial.splashscreen.JSplash;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

public class Menu implements Runnable {

	private static final int VOLUME_IN_DB = -20;
	private static final String NAME = "Menu";
	private static final int WIDTH = 160;
	private static final int HEIGHT = (WIDTH / 3 * 2);
	private static final int SCALE = 3;
	private static boolean running = false;
	private static boolean selectedStart = true;
	private static boolean selectedExit = false;
	private static DedicatedJFrame frame;

	private final Font font = new Font();
	private final MouseListener menuMouseListener = new Mouse();
	private final KeyListener menuKeyListener = new MenuInput();

	private final Color SELECTED_COLOUR = new Color(0xFFFF8800);
	private final Color UNSELECTED_COLOUR = new Color(0xFFCC5500);

	public static void play() {
		Printer printer = new Printer();
		String property = System.getProperty("java.version");
		printer.print("RUNTIME JAVA VERSION " + property, PrintTypes.SYSTEM);
		try {
			// Splash screen
			AtomicReference<URL> splashImageResource = new AtomicReference<>(Game.class.getResource("/splash/splash.png"));
			JSplash splash = new JSplash(splashImageResource.get(), true, true, false, Game.getGameVersion(), null, Color.RED, Color.ORANGE);
			splash.toFront();
			splash.requestFocus();
			splash.splashOn();

			// Background tasks
			try {
				Game.setBackgroundMusic(new AudioHandler("/music/Towards The End.wav", true));
				Game.getBackgroundMusic().setVolume(VOLUME_IN_DB);
			} catch (Exception e) {
				printer.exception(e.getMessage());
			}
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			Game.setAlternateColsR(true);
			Game.setAlternateColsS(true);
			splash.setProgress(100, "Starting Game...");
			Thread.sleep(650);

			// Frame Init
			frame = new DedicatedJFrame(WIDTH, HEIGHT, SCALE, NAME);
			frame.getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.requestFocus();

			// Hide splash
			splash.splashOff();
			splash.removeAll();
		} catch (Exception e) {
			printer.exception(e.getMessage());
		}
	}

	public static DedicatedJFrame getFrame() {
		return Menu.frame;
	}

	public static boolean isRunning() {
		return running;
	}

	public static void setRunning(boolean running) {
		Menu.running = running;
	}

	public static boolean isSelectedStart() {
		return selectedStart;
	}

	public static void setSelectedStart(boolean selectedStart) {
		Menu.selectedStart = selectedStart;
	}

	public static boolean isSelectedExit() {
		return selectedExit;
	}

	public static void setSelectedExit(boolean selectedExit) {
		Menu.selectedExit = selectedExit;
	}

	public static synchronized void stop() {
		running = false;
	}

	public synchronized void start() {
		running = true;
		play();
		new Thread(this, "MENU").start();
	}

	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 30D;

		int ticks = 0;
		int frames = 0;

		long lastTimer = System.currentTimeMillis();
		double delta = 0;

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = false;

			while (delta >= 1) {
				ticks++;
				delta -= 1;
				shouldRender = true;
			}

			if (shouldRender) {
				frames++;
				render();
			}

			if (System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				frame.getFrame().setTitle("Frames: " + frames + " Ticks: " + ticks);
				frames = 0;
				ticks = 0;
			}
		}
	}

	private void render() {
		// frame.getFrame().getContentPane().setBackground(Color.GREEN);
		frame.addMouseMotionListener((MouseMotionListener) menuMouseListener);
		frame.addMouseListener(menuMouseListener);
		frame.addKeyListener(menuKeyListener);
		BufferStrategy bs = frame.getBufferStrategy();
		if (bs == null) {
			frame.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
		g.setColor(new Color(0xFF660000));
		g.fillRect(0, 0, WIDTH * 3, HEIGHT * 3);
		g.setColor(new Color(0xFFFF9900));
		g.setFont(font.getArial());
		g.drawString("Welcome to JavaGame", 35, 30);
		g.drawLine(0, HEIGHT * 3, 0, 0);
		g.drawLine(0, 0, (WIDTH * 3), 0);
		g.drawLine((WIDTH * 3), 0, (WIDTH * 3), (HEIGHT * 3));
		g.drawLine(0, (HEIGHT * 3), (WIDTH * 3), (HEIGHT * 3));
		// (LEFT,DOWN,WIDTH,HEIGHT)
		paintButtons(isSelectedStart(), isSelectedExit(), g);
		bs.show();
		g.dispose();

	}

	private void paintButtons(boolean start, boolean exit, Graphics g) {
		// START
		if (!start) {
			g.setColor(new Color(0xFFBB4400));
			g.fillRect(35, 40, (frame.getWidth() - 67), 113);
			g.setColor(UNSELECTED_COLOUR);
		} else {
			g.setColor(new Color(0xFFDD6600));
			g.fillRect(35, 40, (frame.getWidth() - 67), 113);
			g.setColor(SELECTED_COLOUR);
		}
		g.fillRect(35, 40, (frame.getWidth() - 70), 110);
		g.setColor(Color.BLACK);
		g.drawString("Start", 220, 95);
		// EXIT
		if (!exit) {
			g.setColor(new Color(0xFFBB4400));
			g.fillRect(35, 170, (frame.getWidth() - 67), 113);
			g.setColor(UNSELECTED_COLOUR);
		} else {
			g.setColor(new Color(0xFFDD6600));
			g.fillRect(35, 170, (frame.getWidth() - 67), 113);
			g.setColor(SELECTED_COLOUR);
		}
		g.fillRect(35, 170, (frame.getWidth() - 70), 110);
		g.setColor(Color.BLACK);
		g.drawString("Exit", 220, 220);
	}

}
