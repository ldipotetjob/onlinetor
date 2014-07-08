/*
 * Copyright (c) 2010 Matthew J. Francis and Contributors of the Bobbin Project
 * This file is distributed under the MIT licence. See the LICENCE file for further information.
 */
package test.util;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;

import org.itadaki.bobbin.util.DSAUtil;
import org.junit.Test;



/**
 * Tests DSAUtil
 */
public class TestDSAUtil {

	/**
	 * Tests a DER<>P1363 round trip on sample signatures
	 */
	@Test
	public void testRoundTrip() {

		byte[][] derSignatures = new byte[][] {
				{ 48, 43, 2, 19, 21, 64, 63, 18, -43, 65, 54, 19, -35, -52, 127, 116, -6, -89, -40, 29, 13, -110, 42, 2, 20, 72, 19, -121, 75, -12, -61, 76, -15, 44, -35, -73, 103, 51, 104, 107, 125, -109, 13, 96, 83 },
				{ 48, 43, 2, 20, 40, 106, 60, 1, 17, -1, 58, -111, 78, 121, -33, -72, 75, -9, 53, 72, 98, 28, -90, -5, 2, 19, 33, -75, 114, -103, -58, 2, -77, 12, -24, 58, 30, 115, 105, 47, 118, -48, 73, -83, -28 },
				{ 48, 44, 2, 20, 83, -73, 52, 14, -57, 34, 30, -88, 105, -38, -115, -111, 65, 76, -90, -39, 104, -67, -16, 126, 2, 20, 109, 40, 122, 45, -34, 54, 68, -119, 42, 112, -79, 74, -80, -69, 18, 5, -85, -56, -95, 10 }, 
				{ 48, 45, 2, 20, 17, 74, -125, -89, -61, 86, -23, -117, -33, -97, -3, 112, -122, -30, 48, 6, -81, -76, 98, 0, 2, 21, 0, -128, -27, -78, -68, -44, -29, -119, 93, 70, -31, -83, -25, 38, 7, 34, -67, 52, 41, 112, 58 },
				{ 48, 45, 2, 21, 0, -110, 82, 122, 29, 99, 30, 91, -76, -128, 62, -51, -24, -95, 42, -8, -128, 5, -125, 80, 125, 2, 20, 35, -92, 122, -87, 84, -26, -109, 106, 118, 58, 114, 82, 12, 109, -74, 91, -43, -56, 55, 8 },
				{ 48, 46, 2, 21, 0, -123, -45, -89, -89, 78, 54, -118, -6, 114, -13, -20, -28, -67, 43, -19, 69, -2, 77, -102, -7, 2, 21, 0, -111, 20, 27, 9, 119, 25, -126, 12, -80, -6, -87, -28, -47, 81, 95, -78, -98, 86, 10, 62 } 
		};

		for (byte[] derSignature : derSignatures) {

			byte[] p1363Signature = DSAUtil.derSignatureToP1363Signature (derSignature);

			assertEquals (40, p1363Signature.length);
			assertEquals (ByteBuffer.wrap (derSignature), DSAUtil.p1363SignatureToDerSignature (ByteBuffer.wrap (p1363Signature)));

		}

	}


}
