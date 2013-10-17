package com.redomar.game.lib;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.redomar.game.menu.Menu;

public class Mouse implements MouseListener, MouseMotionListener{
	

	public void mouseDragged(MouseEvent e) {
		
	}

	public void mouseMoved(MouseEvent e) {
		if (e.getX() > 35 && e.getX() < 440){
			//START is being selected
			if(e.getY() > 35 && e.getY() < 125){
				Menu.selectedStart = true;
			}else{
				Menu.selectedStart = false;
			}
			//EXIT is being selected
			if(e.getY() > 160 && e.getY() < 250){
				Menu.selectedExit = true;
			}else{
				Menu.selectedExit = false;
			}
		}else{
			Menu.selectedStart = false;
			Menu.selectedExit = false;
		}
		
	}

	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		
	}

	public void mouseReleased(MouseEvent e) {
		
	}

}
