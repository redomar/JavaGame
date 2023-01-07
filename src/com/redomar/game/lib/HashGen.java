package com.redomar.game.lib;

import org.apache.commons.lang3.StringUtils;

public class HashGen {

	private String hexHash;
	private boolean prefix;
	private int hexLength;
	private String previousHash;

	/**
	 * Use for generating a hex Hash. Requires two parameters;
	 *
	 * @param showPrefix to show 0x prefix.
	 * @param length     Length of hash.
	 */
	public HashGen(boolean showPrefix, int length) {
		setPrefix(showPrefix);
		setHexLength(length);
		init();
	}

	//Remove null char or prepend 0x
	private void init() {
		if (prefix) {
			hexHash = "0x";
		} else {
			hexHash = "";
		}
	}

	/**
	 * Retrieve hash
	 *
	 * @return String containing hash
	 */
	public String getHash() {
		setHash(hexLength);
		return hexHash;
	}

	private void setHash(int hexLength) {

		String hex;

		for (int i = 0; i < hexLength; i++) {
			hex = Integer.toHexString(randNumGen());
			hex = StringUtils.capitalize(hex);
			hexHash = String.format("%s%s", hexHash, hex);
		}
		previousHash = hexHash;

	}

	private int randNumGen() {
		return (int) (Math.random() * 16);
	}

	//getters and setters

	public void setPrefix(boolean prefix) {
		this.prefix = prefix;
	}

	public void setHexLength(int hexLength) {
		this.hexLength = hexLength;
	}

	public String getPreviousHash() {
		if (previousHash == null) return null;
		return previousHash;
	}
}
