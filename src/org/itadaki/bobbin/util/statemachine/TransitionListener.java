/*
 * Copyright (c) 2010 Matthew J. Francis and Contributors of the Bobbin Project
 * This file is distributed under the MIT licence. See the LICENCE file for further information.
 */
package org.itadaki.bobbin.util.statemachine;


/**
 * A listener for state transitions
 * 
 * @param <S> The type of the state
 * @param <I> The type of the input
 */
public interface TransitionListener<S,I> {

	/**
	 * Indicates that the state has transitioned
	 *
	 * @param stateBefore The state before the transition
	 * @param input The input
	 * @param stateAfter The state after the transition (which may be the same as before the
	 *        transition)
	 */
	public void stateTransitioned (S stateBefore, I input, S stateAfter);

}
