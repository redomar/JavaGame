package com.redomar.game.script;

import com.redomar.game.Game;

import javax.swing.*;

public class PopUp{

	private JFrame frame;

	public PopUp(){
		frame = Game.getFrame();
	}

	public int Warn(String msg){
		Object[] options = {"Continue"};
		return JOptionPane.showOptionDialog(frame, msg, "Notice", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, options, options[0]);
	}
}
