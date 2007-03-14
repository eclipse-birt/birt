
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
package org.eclipse.birt.data.engine.olap.data.impl.facttable;


/**
 * A dimension can be divided to several sub dimensions by a DimensionDivision.
 * An instance of DimensionDivision contains a range array to indicate the start
 * and end of sub dimensions.
 */

public class DimensionDivision
{
	IntRange[] ranges = null;
	
	/**
	 * 
	 * @param dimensionMemberCount
	 * @param subDimensionCount
	 */
	public DimensionDivision( int dimensionMemberCount, int subDimensionCount )
	{
		if ( dimensionMemberCount <= subDimensionCount )
		{
			ranges = new IntRange[dimensionMemberCount];
			for ( int i = 0; i < dimensionMemberCount; i++ )
			{
				ranges[i] = new IntRange( i, i);
			}
			return;
		}
		int[] subDimensionMemberCount = new int[subDimensionCount];
		int baseSize = dimensionMemberCount / subDimensionCount;
		for ( int i = 0; i < dimensionMemberCount % subDimensionCount; i++ )
		{
			subDimensionMemberCount[i] = baseSize + 1;
		}
		for ( int i = dimensionMemberCount % subDimensionCount; i < subDimensionCount; i++ )
		{
			subDimensionMemberCount[i] = baseSize;
		}
		ranges = new IntRange[subDimensionCount];
		ranges[0] = new IntRange(0, subDimensionMemberCount[0] - 1);
		
		for ( int i = 1; i < ranges.length; i++ )
		{
			ranges[i] = new IntRange(  );
			ranges[i].start = ranges[i - 1].end + 1;
			ranges[i].end = ranges[i].start + subDimensionMemberCount[i] - 1;
		}
		
		assert ranges[ranges.length].end == dimensionMemberCount - 1;
	}
	
	int getSubDimensionIndex( int dimensionIndex )
	{
		for ( int i = 0; i < ranges.length; i++ )
		{
			if ( ranges[i].contains( dimensionIndex ) )
			{
				return i;
			}
		}

		return -1;
	}

}


class IntRange
{
	int start;
	int end;
	
	IntRange()
	{
		
	}
			
	IntRange( int start, int end)
	{
		this.start = start;
		this.end = end;
	}
	
	boolean contains( int i )
	{
		return i >= start && i <= end;
	}
}