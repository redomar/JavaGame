package com.redomar.game.audio;

import javax.sound.sampled.*;

/**
 * For uncompressed files like .wav
 */
public class AudioEffect{

	private Clip clip;
	private boolean active = false;

	public AudioEffect(String path){
		try{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(path));
			AudioFormat baseformat = audioInputStream.getFormat();
			AudioInputStream decodedAudioInputStream = AudioSystem.getAudioInputStream(
					baseformat, audioInputStream);
			clip = AudioSystem.getClip();
			clip.open(decodedAudioInputStream);
		} catch (Exception e){
			System.err.println(e.getStackTrace());
		}
	}

	public void play(){
		if(clip == null) return;
		stop();
		clip.setFramePosition(0);
		clip.start();
		active = true;
	}

	public void setVolume(float velocity){
		FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		volume.setValue(velocity);

	}

	public void stop() {
		if (clip.isRunning()) clip.stop();
		active = false;
	}

	public void close(){
		stop();
		clip.close();
	}

	public boolean getActive(){
		return this.active;
	}

}