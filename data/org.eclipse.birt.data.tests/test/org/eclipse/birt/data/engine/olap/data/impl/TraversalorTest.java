
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

import java.io.IOException;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.olap.data.impl.Traversalor;

import junit.framework.TestCase;


/**
 * 
 */

public class TraversalorTest extends TestCase
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown( ) throws Exception
	{
		super.tearDown( );
	}

	/**
	 * 
	 * @throws IOException
	 * @throws BirtException
	 */
	public void testTraversalor( ) throws IOException,
			BirtException
	{
		int[] lengthArray = { 1, 1, 1};
		Traversalor traversalor = new Traversalor( lengthArray );
		while( traversalor.next( ) )
		{
			System.out.println( traversalor.getInt( 0 )
					+ ", " + traversalor.getInt( 1 ) + ", "
					+ traversalor.getInt( 2 ) );
		}
	}
}
