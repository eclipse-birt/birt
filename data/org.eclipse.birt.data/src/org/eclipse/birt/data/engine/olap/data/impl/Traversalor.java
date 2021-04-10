/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.data.impl;

import java.util.logging.Logger;

/**
 * 
 */

public class Traversalor {

	private int cursor = -1;
	private int length = 0;
	private int maxLength = 1;
	private boolean valid = true;

	private int[] positionValue;
	private int[] lengthArray;
	private int[] current;

	private static Logger logger = Logger.getLogger(Traversalor.class.getName());

	/**
	 * 
	 * @param lengthArray
	 */
	public Traversalor(int[] lengthArray) {
		logger.entering(Traversalor.class.getName(), "Traversalor", lengthArray);
		this.lengthArray = lengthArray;
		this.length = lengthArray.length;
		this.positionValue = new int[length];
		this.current = new int[length];

		init();
		logger.exiting(Traversalor.class.getName(), "Traversalor");
	}

	/**
	 * 
	 *
	 */
	private void init() {
		checkSyntax();
		computeMaxLength();
		computePositionValue();
	}

	/**
	 * 
	 *
	 */
	private void checkSyntax() {
		for (int i = 0; i < length; i++) {
			if (lengthArray[i] <= 0) {
				valid = false;
				return;
			}
		}
	}

	/**
	 * 
	 *
	 */
	private void computeMaxLength() {
		for (int i = 0; i < length; i++) {
			maxLength *= lengthArray[i];
		}
	}

	/**
	 * 
	 *
	 */
	private void computePositionValue() {
		for (int i = length - 1; i >= 0; i--) {
			positionValue[i] = 1;

			if (i != length - 1) {
				for (int j = length - 1; j > i; j--) {
					positionValue[i] *= lengthArray[j];
				}
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public boolean next() {
		checkValid();

		if (!valid)
			return false;

		cursor++;
		computeCurrent();

		return true;
	}

	/**
	 * 
	 *
	 */
	private void checkValid() {
		if (!valid)
			return;

		if (cursor == maxLength - 1)
			valid = false;
	}

	/**
	 * 
	 *
	 */
	private void computeCurrent() {
		int position = cursor;
		for (int i = 0; i < length; i++) {
			current[i] = position / positionValue[i];
			if (current[i] > 0)
				position = position % positionValue[i];
		}
	}

	/**
	 * 
	 * @return
	 */
	public int[] getIntArray() {
		return current;
	}

	public int getInt(int index) {
		return current[index];
	}

//	public static void main( String[] args )
//	{
//		int[] testArray = {
//				5, 3, 8
//		};
//		Traversalor testTraversalor = new Traversalor( testArray );
//		int i = 0;
//		{
//			while ( testTraversalor.next( ) )
//			{
//				int[] test = testTraversalor.getIntArray( );
//				for ( int j = 0; j < test.length; j++ )
//					System.out.print( test[j] + "\t" );
//				System.out.println( i++ );
//			}
//		}
//	}
}
