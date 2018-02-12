package com.redomar.game.script;

import com.redomar.game.Game;

import javax.swing.*;

public class PopUp{

	private JFrame frame;
	public boolean active;

	public PopUp(){
		active = true;
	}

	public int Warn(String msg){
		Object[] options = {"Continue"};
		if (active) {
            frame = Game.getFrame();
		    return JOptionPane.showOptionDialog(frame, msg, "Notice", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, options, options[0]);
        }
        else
            return 1;
	}
}
