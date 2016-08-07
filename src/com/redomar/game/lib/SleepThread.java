package com.redomar.game.lib;

import com.redomar.game.Game;

public class SleepThread implements Runnable{

	public void run() {
		try {
			Thread.sleep(1500);
			Game.setClosing(false);
			System.out.println("time up");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
