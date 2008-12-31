
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
import java.util.Date;

import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionKey;
import org.eclipse.birt.data.engine.olap.data.util.BufferedStructureArray;

import junit.framework.TestCase;


/**
 * 
 */

public class DimensionKeyTest extends TestCase
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
	
	public void testSaveAndLoad() throws IOException
	{
		int keyCount = 10000;
		BufferedStructureArray bufferedStructureArray = new BufferedStructureArray( DimensionKey.getCreator( ), 2000 );
		for( int i=0; i<keyCount;i++)
		{
			bufferedStructureArray.add( create(i) );
		}
		for( int i=0; i<keyCount;i++)
		{
			assertEquals( bufferedStructureArray.get( i ), create(i) );
		}
		bufferedStructureArray.clear( );
		bufferedStructureArray.close( );
	}
	
	private DimensionKey create( int i )
	{
		DimensionKey key = new DimensionKey( 3 );
		key.getKeyValues()[0] = new Integer( i ) ;
		key.getKeyValues()[1] = String.valueOf( i + 1 );
		key.getKeyValues()[2] = new Date( i + 2 );
		return key;
	}
}
