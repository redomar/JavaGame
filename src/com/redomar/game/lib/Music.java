package com.redomar.game.lib;

import java.io.BufferedInputStream;
import java.io.InputStream;

import com.redomar.game.Game;

import javazoom.jl.player.Player;

public class Music implements Runnable{

	private InputStream file;
	private Player musicPlayer;
	
	public Music(InputStream url){
		this.file =  url;
	}
	
	public Music() {
		
	}

	public void Play(){
		try {
            BufferedInputStream buffered = new BufferedInputStream(file);  
            musicPlayer = new Player(buffered);  
            musicPlayer.play();
		} catch (Exception e) {
			System.out.println("Problem playing file " + file);  
            System.out.println(e);
		}
	}

	@Override
	public void run() {
		Music music = new Music(Game.class.getResourceAsStream("/music/yoshi.mp3"));
		while(true){
			music.Play();
		}
	}
	
	public void stop() {
		Music music = new Music(Game.class.getResourceAsStream("/music/yoshi.mp3"));
		while(true){
			music.stop();
		}
	}
	
	
}
