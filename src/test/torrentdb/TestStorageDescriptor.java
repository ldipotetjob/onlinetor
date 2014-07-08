/*
 * Copyright (c) 2010 Matthew J. Francis and Contributors of the Bobbin Project
 * This file is distributed under the MIT licence. See the LICENCE file for further information.
 */
package test.torrentdb;

import static org.junit.Assert.*;

import org.itadaki.bobbin.torrentdb.PiecesetDescriptor;
import org.junit.Test;



/**
 * Tests PiecesetDescriptor
 */
public class TestStorageDescriptor {

	/**
	 * Tests 0 pieces
	 */
	@Test
	public void test0() {

		PiecesetDescriptor descriptor = new PiecesetDescriptor (1024, 0);

		assertEquals (0, descriptor.getLength());
		assertEquals (1024, descriptor.getPieceSize());
		assertEquals (0, descriptor.getLastPieceLength());
		assertEquals (0, descriptor.getNumberOfPieces());
		assertTrue (descriptor.isRegular());

	}


	/**
	 * Tests 1 whole piece
	 */
	@Test
	public void test1() {

		PiecesetDescriptor descriptor = new PiecesetDescriptor (1024, 1024);

		assertEquals (1024, descriptor.getLength());
		assertEquals (1024, descriptor.getPieceSize());
		assertEquals (1024, descriptor.getPieceLength (0));
		assertEquals (1024, descriptor.getLastPieceLength());
		assertEquals (1, descriptor.getNumberOfPieces());
		assertTrue (descriptor.isRegular());

	}


	/**
	 * Tests 1.5 pieces
	 */
	@Test
	public void test1x5() {

		PiecesetDescriptor descriptor = new PiecesetDescriptor (1024, 1524);

		assertEquals (1524, descriptor.getLength());
		assertEquals (1024, descriptor.getPieceSize());
		assertEquals (1024, descriptor.getPieceLength (0));
		assertEquals (500, descriptor.getPieceLength (1));
		assertEquals (500, descriptor.getLastPieceLength());
		assertEquals (2, descriptor.getNumberOfPieces());
		assertFalse (descriptor.isRegular());

	}

}
