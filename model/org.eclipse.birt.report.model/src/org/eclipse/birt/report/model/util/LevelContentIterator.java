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

package org.eclipse.birt.report.model.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;

/**
 * 
 */

public class LevelContentIterator implements Iterator
{

	/**
	 * The maximal level.
	 */

	protected static final int MAX_LEVEL = Integer.MAX_VALUE;

	/**
	 * List of content elements.
	 */

	List elementContents = null;

	/**
	 * Current iteration position.
	 */

	protected int posn = 0;

	/**
	 * Constructs a iterator that will visit all the content element within the
	 * given <code>element</code>
	 * 
	 * @param element
	 *            the element to visit.
	 * @param level
	 *            the depth of elements to iterate
	 */

	public LevelContentIterator( DesignElement element, int level )
	{
		assert element != null;

		elementContents = new ArrayList( );
		buildContentsList( element, level );
	}

	/**
	 * Constructs a iterator that will visit all the content element within the
	 * given slot id of the given <code>element</code>
	 * 
	 * @param element
	 *            the element to visit.
	 * @param slotId
	 *            the slot id
	 * @param level
	 *            the depth of elements to iterate.
	 */

	public LevelContentIterator( DesignElement element, int slotId, int level )
	{
		assert element != null;

		elementContents = new ArrayList( );

		buildContentsList( element, slotId, level );
	}

	/**
	 * Adds the content elements in the given container element into
	 * <code>elementContents</code>
	 * 
	 * @param element
	 *            the next element to build.
	 */

	private void buildContentsList( DesignElement element, int level )
	{
		if ( level <= 0 )
			return;

		for ( int i = 0; i < element.getDefn( ).getSlotCount( ); i++ )
		{
			buildContentsList( element, i, level - 1 );
		}
	}

	/**
	 * Adds the content elements of the given slot in the given container
	 * element into <code>elementContents</code>
	 * 
	 * @param element
	 *            the next element to build.
	 * @param slotId
	 *            the slot id.
	 */

	private void buildContentsList( DesignElement element, int slotId, int level )
	{
		ContainerSlot slot = element.getSlot( slotId );
		assert slot != null;

		for ( Iterator iter = slot.getContents( ).iterator( ); iter.hasNext( ); )
		{
			DesignElement e = (DesignElement) iter.next( );
			elementContents.add( e );

			buildContentsList( e, level - 1 );
		}
	}

	/**
	 * Not allowed.
	 */

	public void remove( )
	{
		assert false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#hasNext()
	 */

	public boolean hasNext( )
	{
		return posn < elementContents.size( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#next()
	 */

	public Object next( )
	{
		return elementContents.get( posn++ );
	}

}
