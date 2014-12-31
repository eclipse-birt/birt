
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
import java.util.Comparator;


/**
 * 
 */

public class PrimitiveDiskSortedStack extends BaseDiskSortedStack
{
	public PrimitiveDiskSortedStack( int bufferSize, boolean isAscending,
			boolean forceDistinct )
	{
		super( bufferSize, isAscending, forceDistinct, null );
	}
	
	public PrimitiveDiskSortedStack( int bufferSize, boolean forceDistinct,
			Comparator comparator )
	{
		super( bufferSize, forceDistinct, comparator, null );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.util.BaseDiskSortedStack#saveToDisk(int, int)
	 */
	protected void saveToDisk( int fromIndex, int toIndex ) throws IOException
	{
		PrimitiveDiskArray diskList = new PrimitiveDiskArray( );
		for ( int i = fromIndex; i <= toIndex; i++ )
		{
			diskList.add( buffer[i] );
		}
		segments.add( diskList );
	}

}
