/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.internal.document.v2;

import junit.framework.TestCase;

/**
 * test if the cache following the cache rules
 * 
 * Cache is used by the report contnet, the rule is each report item can't be
 * read more than once.
 * 
 */
public class ContentTreeCacheTest extends TestCase
{

	ContentTreeCache cache = new ContentTreeCache( );

	/**
	 * Create a cache, add the report items into the cahe sequencly test if the
	 * cahce drop the un-used report item correctly.
	 */
	public void testSequenceCache( )
	{
		addEntry( 0, -1 );
		assertEquals(0, getEntry(0));
		
		addEntry( 1, 0 );
		assertEquals(0, getEntry(0));
		assertEquals(1, getEntry(1));
		
		addEntry( 2, 1 );
		assertEquals(0, getEntry(0));
		assertEquals(1, getEntry(1));
		assertEquals(2, getEntry(2));
		
		addEntry( 3, 1 );
		assertEquals(0, getEntry(0));
		assertEquals(1, getEntry(1));
		assertEquals(-1, getEntry(2));
		assertEquals(3, getEntry(3));
		
		addEntry( 4, 0 );
		assertEquals(0, getEntry(0));
		assertEquals(-1, getEntry(1));
		assertEquals(-1, getEntry(2));
		assertEquals(-1, getEntry(3));
		assertEquals(4, getEntry(4));
		
		addEntry( 5, 4 );
		assertEquals(0, getEntry(0));
		assertEquals(-1, getEntry(1));
		assertEquals(-1, getEntry(2));
		assertEquals(-1, getEntry(3));
		assertEquals(4, getEntry(4));
		assertEquals(5, getEntry(5));
		
		addEntry( 6, 4 );
		assertEquals(0, getEntry(0));
		assertEquals(-1, getEntry(1));
		assertEquals(-1, getEntry(2));
		assertEquals(-1, getEntry(3));
		assertEquals(4, getEntry(4));
		assertEquals(-1, getEntry(5));
		assertEquals(6, getEntry(6));
		
		addEntry( 7, -1 );
		assertEquals(-1, getEntry(0));
		assertEquals(-1, getEntry(1));
		assertEquals(-1, getEntry(2));
		assertEquals(-1, getEntry(3));
		assertEquals(-1, getEntry(4));
		assertEquals(-1, getEntry(5));
		assertEquals(-1, getEntry(6));
		assertEquals(7, getEntry(7));
	}

	/**
	 * create a cache, add the report items into the cache randomly test if the
	 * cache drop the un-used entry correctly.
	 */
	public void testRandomCache( )
	{

	}

	protected void addEntry( long offset, long parent )
	{
		cache.addEntry( new ContentTreeCache.TreeEntry( offset, parent,
				offset + 1, null ) );
	}

	protected long getEntry( long offset )
	{
		ContentTreeCache.TreeEntry entry = cache.getEntry( offset );
		if ( entry == null )
		{
			return -1;
		}
		return offset;
	}
}
