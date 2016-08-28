package com.redomar.game.entities.projectiles;

import com.redomar.game.audio.AudioEffect;
import com.redomar.game.gfx.Colours;
import com.redomar.game.gfx.Screen;
import com.redomar.game.level.LevelHandler;

public class Small extends Projectile{

	public static final int FIRE_RATE = 10;

	public Small(LevelHandler level, int x, int y, double dir) {
		super(level, x, y, dir);
		range = 125 - life.nextInt(30);
		damage = 20;
		speed = 2;

		nx = speed * Math.cos(angle);
		ny = speed * Math.sin(angle);

		//smallSound.setVolume(-15);

		AudioEffect smallSound;
		smallSound = new AudioEffect("/sfx/smallProjectile.wav");
		smallSound.play();
	}

	public void tick() {
		if (tileCollision(x, y,(int) nx,(int) ny)) remove();
		move();
	}

	protected void move(){
		x += nx;
		y += ny;

		double distance = Math.sqrt(Math.abs((xOrigin - x)*(xOrigin - x)+(yOrigin - y)*(yOrigin - y)));
		this.distance = distance;
		if(this.distance > range) remove();
	}

	public void render(Screen screen) {
		screen.render((int)x,(int)y, 8 * 32, Colours.get(-1, 522, 540, 555), 0x00, 1);
	}

}
