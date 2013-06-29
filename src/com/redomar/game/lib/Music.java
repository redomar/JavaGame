package com.redomar.game.lib;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Random;

import com.redomar.game.Game;

import javazoom.jl.player.Player;

public class Music implements Runnable{

	private InputStream file;
	private Player musicPlayer;
	private static String songName[] = {"/music/yoshi song.mp3", "/music/Towards The End.mp3", "/music/Towards The End.mp3"};
	private static int songNumber;
	
	private static Random rand = new Random();
	
	public Music(InputStream url){
		this.file =  url;
	}
	
	public Music() {
		Music.songNumber = rand.nextInt(3);
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
	
	public synchronized void start(){
		this.run();
	}

	@Override
	public void run() {
		try {
			Thread.sleep(300);
			initSongNumber();
			//System.out.println("[MUSIC] loading song: " + songName[songNumber].substring(7, (songName[songNumber].length() - 4)));
			Music music = new Music(Game.class.getResourceAsStream(songName[songNumber]));
			//Thread.sleep(100);
			System.out.println("[MUSIC] playing song: " + songName[songNumber].substring(7, (songName[songNumber].length() - 4)));
			music.Play();
			this.run();
		} catch (InterruptedException e) {
			System.out.println("[ERROR][MUSIC] Could not stop, nothing currenly playing");
		}
	}
	
	public void stop() {
		
	}

	private void initSongNumber() {
		Music.songNumber = rand.nextInt(3);
	}

	public String[] getSongName() {
		return songName;
	}

	public int getSongNumber() {
		return songNumber;
	}
}
