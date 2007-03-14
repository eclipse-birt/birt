
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
package org.eclipse.birt.data.engine.olap.data.impl.dimension;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.data.api.ISelection;
import org.eclipse.birt.data.engine.olap.data.impl.Constants;
import org.eclipse.birt.data.engine.olap.data.util.BaseDiskSortedStack;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.IndexKey;
import org.eclipse.birt.data.engine.olap.data.util.PrimitiveDiskSortedStack;
import org.eclipse.birt.data.engine.olap.data.util.SetUtil;

/**
 * 
 */

public class DimensionFilterHelper
{
	/**
	 * 
	 * @param levels
	 * @param filters
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	public static IDiskArray find( Level[] levels, ISelection[][] filters ) throws IOException, DataException
	{
		ArrayList filterResults = new ArrayList( );
		for ( int i = 0; i < levels.length; i++ )
		{
			filterResults.add( find(levels[i], filters[i]) );
		}
		PrimitiveDiskSortedStack[] stackResults = new PrimitiveDiskSortedStack[filterResults.size( )];
		System.arraycopy( filterResults.toArray( ),
				0,
				stackResults,
				0,
				stackResults.length );
		IDiskArray AndFilterResults = SetUtil.getIntersection( stackResults );
		return AndFilterResults;
	}
	
	/**
	 * 
	 * @param level
	 * @param filter
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	private static BaseDiskSortedStack find(Level level, ISelection[] filter) throws IOException, DataException
	{
		IDiskArray indexKeyArray = level.diskIndex.find( filter );
		PrimitiveDiskSortedStack resultStack = new PrimitiveDiskSortedStack( Constants.LIST_BUFFER_SIZE, true, true );
		for ( int i = 0; i < indexKeyArray.size( ); i++ )
		{
			IndexKey key = (IndexKey)indexKeyArray.get( i );
			resultStack.push( new Integer(key.dimensionPos) );
		}
		return resultStack;
	}
	
	
}
