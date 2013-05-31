package com.redomar.game.lib;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.net.URL;

import com.redomar.game.Game;

import javazoom.jl.player.Player;

public class Music implements Runnable{

	private String file;
	private Player musicPlayer;
	
	public Music(URL url){
		this.file =  url.toString().substring(6);
	}
	
	public Music() {
		
	}

	public void Play(){
		try {
			FileInputStream inputFile     = new FileInputStream(file);  
            BufferedInputStream buffered = new BufferedInputStream(inputFile);  
            musicPlayer = new Player(buffered);  
            musicPlayer.play();
		} catch (Exception e) {
			System.out.println("Problem playing file " + file);  
            System.out.println(e);
		}
	}

	@Override
	public void run() {
		Music music = new Music(Game.class.getResource("/music/yoshi.mp3"));
		music.Play();
	}
}
