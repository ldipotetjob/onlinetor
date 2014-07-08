/*
 * Copyright (c) 2010 Matthew J. Francis and Contributors of the Bobbin Project
 * This file is distributed under the MIT licence. See the LICENCE file for further information.
 */
package org.itadaki.bobbin.connectionmanager;


/**
 * An interface used to advise that a Connection may be ready for reading and /
 * or writing
 */
public interface ConnectionReadyListener {

	/**
	 * Called to indicate that a Connection may be ready for reading and / or
	 * writing
	 *
	 * @param connection The Connection that may be ready
	 * @param readable If true, the Connection may be ready to be read from
	 * @param writeable If true, the Connection may be ready to be written to
	 */
	public void connectionReady (Connection connection, boolean readable, boolean writeable);


}
