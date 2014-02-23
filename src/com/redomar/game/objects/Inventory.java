package com.redomar.game.objects;

import com.redomar.game.Game;

public class Inventory {

	public static int x;
	public static int y;
	public static boolean open = false;
	private static InventoryWindow inv_window = new InventoryWindow();
	
	public static void activate() {
		x = Game.getPlayer().getX();
		y = Game.getPlayer().getY();
		
		if(Game.getLevel().getTile(x >> 3, y >> 3).getId() == 8){
			if(!open){
				System.out.println("Opened\nInside this Bag their is:"+inside());
				open = true;
				Game.getPlayer().setMoving(false);
				//Game.getInput().untoggle(true);
				inv_window.start();
			}
		}else{
			if(open){
				open = false;
				//Game.getInput().untoggle(false);
				inv_window.stop();
				inv_window.getFrame().setVisible(false);
				inv_window.getFrame().stopFrame();
			}
		}
	}
	
	private static String inside(){
		String items = " ";
		for (Items item : Items.values()) {
			items = items + item.toString() + ", ";
		}
		return items;
	}
}
