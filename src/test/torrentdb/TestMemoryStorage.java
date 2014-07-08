/*
 * Copyright (c) 2010 Matthew J. Francis and Contributors of the Bobbin Project
 * This file is distributed under the MIT licence. See the LICENCE file for further information.
 */
package test.torrentdb;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.Arrays;

import org.itadaki.bobbin.torrentdb.Filespec;
import org.itadaki.bobbin.torrentdb.InfoFileset;
import org.itadaki.bobbin.torrentdb.MemoryStorage;
import org.itadaki.bobbin.torrentdb.PiecesetDescriptor;
import org.itadaki.bobbin.torrentdb.Storage;
import org.junit.Test;

import test.Util;


/**
 * Tests MemoryStorage
 */
public class TestMemoryStorage {

	/**
	 * Tests creating a MemoryStorage that is too large
	 * @throws IOException
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testTooLarge() throws IOException {

		Storage storage = new MemoryStorage();
		storage.open (262144, new InfoFileset (new Filespec ("test.txt", (long)Integer.MAX_VALUE + 1)));

	}


	/**
	 * Tests reading a short final piece
	 * @throws Exception 
	 */
	@Test
	public void testShortFinalPiece1() throws Exception {

		ByteBuffer expectedPiece = ByteBuffer.wrap (new byte[] { 1 });

		Storage storage = new MemoryStorage();
		storage.open (1024, new InfoFileset (new Filespec ("test.txt", 1025L)));

		byte[] writtenPiece = new byte[1024];
		Arrays.fill (writtenPiece, (byte)1);
		storage.write (1, ByteBuffer.wrap (writtenPiece));

		ByteBuffer readPiece = storage.read (1);

		assertEquals (expectedPiece, readPiece);

	}


	/**
	 * Tests reading a short final piece
	 * @throws Exception 
	 */
	@Test
	public void testShortFinalPiece2() throws Exception {

		ByteBuffer expectedPiece = ByteBuffer.wrap (new byte[] { 1 });

		Storage storage = new MemoryStorage();
		storage.open (1024, new InfoFileset (new Filespec ("test.txt", 1025L)));

		ByteBuffer writtenPiece = ByteBuffer.wrap (new byte[] { 1 });
		storage.write (1, writtenPiece);

		ByteBuffer readPiece = storage.read (1);

		assertEquals (expectedPiece, readPiece);

	}


	/**
	 * Tests an output channel writing to 1 block
	 * @throws Exception
	 */
	@Test
	public void testOutputChannel1() throws Exception {

		Storage storage = new MemoryStorage();
		storage.open (1024, new InfoFileset (new Filespec ("test.txt", 1024L)));
		WritableByteChannel outputChannel = storage.openOutputChannel (0, 0);

		ByteBuffer data = ByteBuffer.wrap (Util.pseudoRandomBlock (0, 1024, 1024));
		outputChannel.write (data);

		data.rewind();
		assertEquals (storage.read (0), data);

	}


	/**
	 * Tests an output channel writing to 1.5 blocks
	 * @throws Exception
	 */
	@Test
	public void testOutputChannel1p5() throws Exception {

		Storage storage = new MemoryStorage();
		storage.open (1024, new InfoFileset (new Filespec ("test.txt", 1524L)));
		WritableByteChannel outputChannel = storage.openOutputChannel (0, 0);

		ByteBuffer data = ByteBuffer.wrap (Util.pseudoRandomBlock (0, 1524, 1524));
		outputChannel.write (data);

		data.rewind();
		assertEquals (storage.read (0), data.asReadOnlyBuffer().limit(1024));
		assertEquals (storage.read (1), data.asReadOnlyBuffer().position(1024).limit(1524));

	}


	/**
	 * Tests an output channel writing from blocks 0.5 to 1.5
	 * @throws Exception
	 */
	@Test
	public void testOutputChannel0p5x1p5() throws Exception {

		Storage storage = new MemoryStorage();
		storage.open (1024, new InfoFileset (new Filespec ("test.txt", 1524L)));
		WritableByteChannel outputChannel = storage.openOutputChannel (0, 500);

		ByteBuffer data = ByteBuffer.allocate (1524);
		data.position (500);
		data.put (Util.pseudoRandomBlock (0, 1024, 1024));
		data.position (500);
		outputChannel.write (data);

		data.rewind();
		assertEquals (storage.read (0), data.asReadOnlyBuffer().limit (1024));
		assertEquals (storage.read (1), data.asReadOnlyBuffer().position(1024).limit (1524));

	}


	/**
	 * Tests extending
	 *
	 * @throws Exception
	 */
	@Test
	public void testExtend() throws Exception {

		PiecesetDescriptor expectedDescriptor1 = new PiecesetDescriptor (1024, 1024);
		PiecesetDescriptor expectedDescriptor2 = new PiecesetDescriptor (1024, 2048);
		Storage storage = new MemoryStorage();
		storage.open (1024, new InfoFileset (new Filespec ("test.txt", 1024L)));

		assertEquals (expectedDescriptor1, storage.getPiecesetDescriptor());

		storage.extend (2048);

		ByteBuffer piece = ByteBuffer.wrap (Util.pseudoRandomBlock (1, 1024, 1024));
		storage.write (1, piece);

		assertEquals (expectedDescriptor2, storage.getPiecesetDescriptor());

	}


}
