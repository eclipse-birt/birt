
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
package org.eclipse.birt.data.engine.olap.data.impl.aggregation;

import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;
import org.eclipse.birt.data.engine.olap.data.util.IComparableStructure;
import org.eclipse.birt.data.engine.olap.data.util.IStructure;
import org.eclipse.birt.data.engine.olap.data.util.IStructureCreator;
import org.eclipse.birt.data.engine.olap.data.util.ObjectArrayUtil;

/**
 * Describes a aggregation result row.
 */

public class AggregationResultRow implements IComparableStructure
{
	private Member[] levelMembers = null;
	private Object[] aggregationValues = null;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.util.IStructure#getFieldValues()
	 */
	public Object[] getFieldValues( )
	{
		Object[][] objectArrays = new Object[getLevelMembers().length + 1][];
		for ( int i = 0; i < getLevelMembers().length; i++ )
		{
			objectArrays[i] = getLevelMembers()[i].getFieldValues( );
		}
		if ( getAggregationValues() != null )
		{
			objectArrays[objectArrays.length - 1] = new Object[getAggregationValues().length + 1];
			objectArrays[objectArrays.length - 1][0] = new Integer(1);
			System.arraycopy( getAggregationValues(),
					0,
					objectArrays[objectArrays.length - 1],
					1,
					getAggregationValues().length );
		}
		else
		{
			objectArrays[objectArrays.length - 1] = new Object[1];
			objectArrays[objectArrays.length - 1][0] = new Integer(0);
		}
		return ObjectArrayUtil.convert( objectArrays );
	}
	
	/**
	 * 
	 * @return
	 */
	public static IStructureCreator getCreator()
	{
		return new AggregationResultObjectCreator( );
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo( Object o )
	{
		AggregationResultRow other = (AggregationResultRow) o;
		for ( int i = 0; i < getLevelMembers().length; i++ )
		{
			int result = ( getLevelMembers()[i] ).compareTo( other.getLevelMembers()[i] );
			if ( result < 0 )
			{
				return result;
			}
			else if ( result > 0 )
			{
				return result;
			}
		}
		return 0;
	}

	void setLevelMembers( Member[] levelMembers )
	{
		this.levelMembers = levelMembers;
	}

	Member[] getLevelMembers( )
	{
		return levelMembers;
	}

	void setAggregationValues( Object[] aggregationValues )
	{
		this.aggregationValues = aggregationValues;
	}

	Object[] getAggregationValues( )
	{
		return aggregationValues;
	}
}

/**
 * 
 * @author Administrator
 *
 */
class AggregationResultObjectCreator implements IStructureCreator
{
	private static IStructureCreator levelMemberCreator = Member.getCreator( );
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.util.IStructureCreator#createInstance(java.lang.Object[])
	 */
	public IStructure createInstance( Object[] fields )
	{
		AggregationResultRow result = new AggregationResultRow( );
		Object[][] objectArrays = ObjectArrayUtil.convert( fields );
		
		result.setLevelMembers( new Member[objectArrays.length - 1] );
		for ( int i = 0; i < result.getLevelMembers().length; i++ )
		{
			result.getLevelMembers()[i] = (Member) levelMemberCreator.createInstance( objectArrays[i] );
		}
		if ( objectArrays[objectArrays.length - 1][0].equals( new Integer( 1 ) ) )
		{
			result.setAggregationValues( new Object[objectArrays[objectArrays.length - 1].length-1] );
			System.arraycopy( objectArrays[objectArrays.length - 1],
					1,
					result.getAggregationValues(),
					1,
					result.getAggregationValues().length );
		}
		
		return result;
	}
}