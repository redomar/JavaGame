package com.redomar.game.lib;

import org.apache.commons.lang3.StringUtils;

public class HashGen {

	private String hexHash;
	private boolean prefix;
	private int hexLength;

	public HashGen(boolean showPrefix, int length){
		this.prefix = showPrefix;
		this.hexLength = length;
		init();
	}

	private void init(){
		if(prefix){
			hexHash = "0x";
		} else {
			hexHash = "";
		}
	}

	public String getHash(){
		setHash(hexLength);
		return hexHash;
	}

	private void setHash(int hexLength){

		String hex;

		for (int i = 0; i < hexLength; i++){
			hex = Integer.toHexString(randNumGen());
			hex = StringUtils.capitalize(hex);
			hexHash = hexHash + hex;
		}

	}

	private int randNumGen(){
		int rand = (int)(Math.random() * 16);
		return rand;
	}
}
