package com.redomar.game.entities.projectiles;

import com.redomar.game.gfx.Colours;
import com.redomar.game.gfx.Screen;
import com.redomar.game.level.LevelHandler;

public class Medium extends Projectile{

	public static final int FIRE_RATE = 20;

	public Medium(LevelHandler level, int x, int y, double dir) {
		super(level, x, y, dir);
		range = 60 - life.nextInt(10);
		damage = 80;
		speed = 1;

		nx = speed * Math.cos(angle);
		ny = speed * Math.sin(angle);
	}

	public void tick() {
		if (tileCollision(x, y,(int) nx,(int) ny)) remove();
		move();
	}

	protected void move() {
		x += nx;
		y += ny;

		double distance = Math.sqrt(Math.abs((xOrigin - x)*(xOrigin - x)+(yOrigin - y)*(yOrigin - y)));
		this.distance = distance;
		if(this.distance > range) remove();
	}

	public void render(Screen screen) {
		screen.render((int)x, (int)y, 7 * 32, Colours.get(-1, 311, 510, 544), 0x00, 1);
	}

}
