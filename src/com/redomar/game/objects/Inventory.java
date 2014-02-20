package com.redomar.game.objects;

import com.redomar.game.Game;

public class Inventory {

	public static int x;
	public static int y;
	public static boolean open = false;
	
	public static void activate() {
		x = Game.getPlayer().getX();
		y = Game.getPlayer().getY();
		
		if(Game.getLevel().getTile(x >> 3, y >> 3).getId() == 8){
			if(!open){
				System.out.println("Open");
				open = true;
			}
		}
	}
}
