package com.redomar.game.menu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import com.redomar.game.lib.Font;



public class Menu implements Runnable{

	private static final int WIDTH = 160;
	private static final int HEIGHT = (WIDTH / 3 * 2);
	private static final int SCALE = 3;
	private static final String NAME = "Menu";
	
	private DedicatedJFrame frame = new DedicatedJFrame(WIDTH, HEIGHT, SCALE, NAME);
	private Font font = new Font();
	
	private boolean running = false;
	
	public synchronized void start() {
		running = true;
		new Thread(this).start();
	}

	public synchronized void stop() {
		running = false;
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
				frame.getFrame().setTitle("Frames: " + frames + " Ticks: " + ticks);
				frames = 0;
				ticks = 0;
			}
		}
	}

	private void render() {
		//frame.getFrame().getContentPane().setBackground(Color.GREEN);
		BufferStrategy bs = frame.getBufferStrategy();
		if (bs == null) {
			frame.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
		g.setColor(new Color(0xFFFF9900));
		g.setFont(font.getArial());
		g.drawString("Testing Phase...",40,30);
		bs.show();
		g.dispose();
		
	}

	public static void main(String[] args) {
		new Menu().start();
	}
	
}
