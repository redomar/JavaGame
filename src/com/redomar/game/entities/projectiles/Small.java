package com.redomar.game.entities.projectiles;

import com.redomar.game.gfx.Colours;
import com.redomar.game.gfx.Screen;
import com.redomar.game.level.LevelHandler;

public class Small extends Projectile{
	
	public static final int FIRE_RATE = 12;

	public Small(LevelHandler level, int x, int y, double dir) {
		super(level, x, y, dir);
		range = 50 + life.nextInt(5);
		damage = 20;
		speed = 3;
		
		nx = speed * Math.cos(angle);
		ny = speed * Math.sin(angle);
	}

	public void tick() {
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
		screen.render((int)x,(int)y, 8 * 32, Colours.get(-1, 222, 333, 555), 0x00, 1);
	}

}
