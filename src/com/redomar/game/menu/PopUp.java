package com.redomar.game.menu;

import com.redomar.game.Game;

import javax.swing.*;

public class PopUp {

	public boolean active;

	public PopUp() {
		active = true;
	}

	public int Warn(String msg) {
		Object[] options = {"Continue"};
		if (active) {
			JFrame frame = Game.getFrame();
			return JOptionPane.showOptionDialog(frame, msg, "Notice", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		} else return 1;
	}
}
