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

public class DiskSortedStack extends BaseDiskSortedStack
{
	private IStructureCreator creator = null;

	public DiskSortedStack( int bufferSize, boolean isAscending,
			boolean forceDistinct, IStructureCreator creator )
	{
		super( bufferSize, isAscending, forceDistinct, creator);
		this.creator = creator;
	}
	
	public DiskSortedStack( int bufferSize, boolean forceDistinct,
			Comparator comparator, IStructureCreator creator )
	{
		super( bufferSize, forceDistinct, comparator, creator );
		this.creator = creator;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.util.BaseDiskSortedStack#pushBufferToDisk(int, int)
	 */
	protected void saveToDisk( int fromIndex, int toIndex ) throws IOException
	{
		StructureDiskArray diskList = new StructureDiskArray( creator );
		for ( int i = fromIndex; i <= toIndex; i++ )
		{
			diskList.add( buffer[i] );
		}
		segments.add( diskList );
	}

}
