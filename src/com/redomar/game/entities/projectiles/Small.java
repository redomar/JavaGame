package com.redomar.game.entities.projectiles;

import com.redomar.game.audio.AudioHandler;
import com.redomar.game.gfx.Colours;
import com.redomar.game.gfx.Screen;
import com.redomar.game.level.LevelHandler;

import java.io.File;

public class Small extends Projectile {

	public static final int FIRE_RATE = 10;
	private static final File smallShot = new File("/music/small.wav");

	public Small(LevelHandler level, int x, int y, double dir) {
		super(level, x, y, dir);
		range = 125 - life.nextInt(30);
		damage = 20;
		speed = 2;

		nx = speed * Math.cos(angle);
		ny = speed * Math.sin(angle);

		try {
			AudioHandler smallSound = new AudioHandler(smallShot);
			smallSound.setVolume(-15);
			smallSound.play();
		} catch (Exception e) {
			entityPrinter.cast().exception(e.toString()).exception("Unable to load Audio file " + smallShot.getName());
		}
	}

	public void tick() {
		if (tileCollision(x, y, (int) nx, (int) ny)) remove();
		move();
	}

	protected void move() {
		x += nx;
		y += ny;

		this.distance = Math.sqrt(Math.abs((xOrigin - x) * (xOrigin - x) + (yOrigin - y) * (yOrigin - y)));
		if (this.distance > range) remove();
	}

	public void render(Screen screen) {
		screen.render((int) x, (int) y, 8 * 32, Colours.get(-1, 522, 540, 555), 0x00, 1);
	}

}
