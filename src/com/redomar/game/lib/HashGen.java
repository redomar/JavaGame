package com.redomar.game.lib;

import org.apache.commons.lang3.StringUtils;

public class HashGen {

	private String hexHash;
	private boolean prefix;
	private int hexLength;

	/**
	 * Use for generating a hex Hash. Requires two parameters;
	 * @param showPrefix to show 0x prefix.
	 * @param length Length of hash.
	 */
	public HashGen(boolean showPrefix, int length){
		this.prefix = showPrefix;
		this.hexLength = length;
		init();
	}

	//Remove null char or prepend 0x
	private void init(){
		if(prefix){
			hexHash = "0x";
		} else {
			hexHash = "";
		}
	}

	/**
	 * Retrieve hash
	 * @return String containing hash
	 */
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
