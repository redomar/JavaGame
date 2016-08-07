package com.redomar.game.objects;

import com.redomar.game.menu.DedicatedJFrame;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class InventoryWindow implements Runnable{

	private static final int WIDTH = 160;
	private static final int HEIGHT = (WIDTH / 3 * 2);
	private static final int SCALE = 2;
	private static final String NAME = "Inventory";

	private static boolean running = false;

	private static DedicatedJFrame frame;
	private static InventoryHandler window;

	public static InventoryHandler getWindow() {
		return window;
	}

	public static void setWindow(InventoryHandler inventoryHandler) {
		InventoryWindow.window = inventoryHandler;
	}

	public synchronized void start(){
		running = true;
		setFrame(new DedicatedJFrame(WIDTH, HEIGHT, SCALE, NAME));
		new Thread(this, NAME).start();
	}

	public synchronized void stop(){
		running = false;
	}

	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 30D;

		int ticks = 0;
		int frames = 0;

		long lastTimer = System.currentTimeMillis();
		double delta = 0;

		setWindow(new InventoryHandler(frame));

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
				frame.getFrame().setTitle(
						"Frames: " + frames + " Ticks: " + ticks);
				frames = 0;
				ticks = 0;
			}
		}
	}

	private void render() {
		BufferStrategy bs = frame.getBufferStrategy();
		if(bs == null){
			frame.createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH*SCALE+10, HEIGHT*SCALE+10);
		g.setColor(Color.WHITE);
		g.drawString(NAME, 50, 50);
		bs.show();
		g.dispose();
	}

	public DedicatedJFrame getFrame() {
		return frame;
	}

	public static void setFrame(DedicatedJFrame frame) {
		InventoryWindow.frame = frame;
	}
}
