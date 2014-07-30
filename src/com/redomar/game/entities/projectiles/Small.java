package com.redomar.game.entities.projectiles;

import com.redomar.game.gfx.Colours;
import com.redomar.game.gfx.Screen;
import com.redomar.game.level.LevelHandler;

public class Small extends Projectile{

	public Small(LevelHandler level, int x, int y, double dir) {
		super(level, x, y, dir);
		range = life.nextInt(25) + 50;
		damage = 20;
		rate = 15;
		speed = 4;
		
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
		screen.render((int)x + 6,(int)y, 8 * 32, Colours.get(-1, 222, 333, 555), 0x00, 1);
	}

}
