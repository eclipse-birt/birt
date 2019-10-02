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

package org.eclipse.birt.report.model.api;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * An Iterator over the elements in a slot. Each call to <code>getNext( )</code>
 * returns a handle of type {@link DesignElementHandle}.
 * 
 */
public class SlotIterator implements Iterator<DesignElementHandle>
{

	/**
	 * Handle to the slot over which to iterate.
	 */
	protected final SlotHandle slotHandle;

	/**
	 * Current iteration position.
	 */
	protected int posn;

	/**
	 * Constructs an iterator for the given slot.
	 * 
	 * @param handle
	 *            handle to the slot over which to iterate
	 */
	public SlotIterator( SlotHandle handle )
	{
		slotHandle = handle;
		posn = 0;
	}

	/**
	 * Removes the element at the current iterator position.
	 */
	@Override
	public void remove( )
	{
		if ( !hasNext( ) )
			return;
		try
		{
			slotHandle.dropAndClear( posn );
		}
		catch ( SemanticException e )
		{
			// Should not fail. But, if it does, ignore
			// the error.

			assert false;
		}
	}

	@Override
	public boolean hasNext( )
	{
		return posn < slotHandle.getCount( );
	}

	/**
	 * Returns a handle to the next content element. The handle is one of the
	 * various element classes derived from <code>DesignElementHandle</code>.
	 * 
	 * @return a handle to the next content element.
	 */
	@Override
	public DesignElementHandle next()
	{
		return slotHandle.get( posn++ );
	}

}
