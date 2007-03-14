
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
package org.eclipse.birt.data.engine.olap.data.util;

import java.io.IOException;

import org.eclipse.birt.data.engine.olap.data.impl.Constants;

/**
 * A utility class which provide several set functions.
 */

public class SetUtil
{
	/**
	 * Get intersection from stacks.
	 * @param stacks
	 * @return
	 * @throws IOException
	 */
	public static IDiskArray getIntersection( PrimitiveDiskSortedStack[] stacks ) throws IOException
	{
		IDiskArray result = new BufferedPrimitiveDiskArray( Constants.LIST_BUFFER_SIZE );
		Object[] tmpObjects = new Object[stacks.length];
		
		Object currentObject = null;
		
		int i = 0;
		for ( i = 0; i < tmpObjects.length; i++ )
		{
			tmpObjects[i] = stacks[i].pop( );
			if(tmpObjects[i]==null)
			{
				return result;
			}
		}
		currentObject = tmpObjects[0];
		
		while ( true )
		{
			for ( i = 0; i < tmpObjects.length; i++ )
			{
				while ( ( (Comparable) tmpObjects[i] ).compareTo( currentObject ) < 0 )
				{
					tmpObjects[i] = stacks[i].pop( );
					if(tmpObjects[i]==null)
					{
						return result;
					}
				}
				if ( ( (Comparable) tmpObjects[i] ).compareTo( currentObject ) > 0 )
				{
					break;
				}
			}
			if ( i == tmpObjects.length )
			{
				i--;
			}
			if(( (Comparable) tmpObjects[i] ).compareTo( currentObject ) > 0)
			{
				currentObject = tmpObjects[i];
				continue;
			}
			result.add( currentObject );
			tmpObjects[0] = stacks[0].pop( );
			if(tmpObjects[0]==null)
			{
				return result;
			}
			currentObject = tmpObjects[0];
		}
	}
	
	/**
	 * Get intersection from disk arrays.
	 * @param stacks
	 * @return
	 * @throws IOException
	 */
	public static IDiskArray getIntersection( IDiskArray[] arrays ) throws IOException
	{
		PrimitiveDiskSortedStack[] stacks = new PrimitiveDiskSortedStack[arrays.length];
		for ( int i = 0; i < arrays.length; i++ )
		{
			stacks[i] = new PrimitiveDiskSortedStack( Constants.LIST_BUFFER_SIZE,
					true,
					true );
			if ( arrays[i] == null || arrays[i].size( ) == 0 )
			{
				return null;
			}
			for ( int j = 0; j < arrays[i].size( ); j++ )
			{
				stacks[i].push( arrays[i].get( j ) );
			}
		}
		return getIntersection( stacks );
	}
}
