package com.redomar.game.lib;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HashTest {

	private HashGen hashGen;

	@Before
	public void setUp() throws Exception{
		hashGen = new HashGen(false,10);
	}

	@Test
	public void hashNotEmpty(){
		assertNotNull(hashGen.getHash());
	}

	@Test
	public void hashZeroLengthNotNull(){
		HashGen hg = new HashGen(false,0);
		assertNotNull(hg.getHash());
	}

	@Test
	public void compareHashAndPrevious(){
		hashGen.setHexLength(8);
		assertEquals(hashGen.getHash(), hashGen.getPreviousHash());
	}

	@Test
	public void previousShouldNotGenNewHash(){
		assertEquals(hashGen.getPreviousHash(), hashGen.getPreviousHash());
	}

	@Test
	public void hashLengthEqualsZero(){
		HashGen hg = new HashGen(false, 0);
		assertEquals(0,hg.getHash().length());
	}

	@Test
	public void hashLengthEqualsGivenLength(){
		HashGen hg = new HashGen(false, 80);
		assertEquals(80, hg.getHash().length());
	}

	@Test
	public void hashLengthEqualsSetLength(){
		HashGen hg = new HashGen(false, 80);
		hg.setHexLength(5);
		assertEquals(5,hg.getHash().length());
	}

	@Test
	public void hashPrefix(){
		HashGen hg = new HashGen(true,0);
		assertEquals("0x", hg.getHash());
	}

}
