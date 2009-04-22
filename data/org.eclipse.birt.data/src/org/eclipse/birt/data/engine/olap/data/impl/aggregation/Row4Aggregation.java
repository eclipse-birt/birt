
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
import org.eclipse.birt.data.engine.olap.data.util.IStructure;
import org.eclipse.birt.data.engine.olap.data.util.IStructureCreator;
import org.eclipse.birt.data.engine.olap.data.util.ObjectArrayUtil;

/**
 * 
 */

public class Row4Aggregation implements IStructure
{
	private Member[] levelMembers;
	private Object[] measures;
	private Object[] parameterValues;
	private int[] dimPos;
	
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.util.IStructure#getFieldValues()
	 */
	public Object[] getFieldValues( )
	{
		Object[][] objectArrays = new Object[getLevelMembers().length + 3][];
		for ( int i = 0; i < getLevelMembers().length; i++ )
		{
			objectArrays[i] = getLevelMembers()[i].getFieldValues( );
		}
		objectArrays[objectArrays.length-3] = measures;
		objectArrays[objectArrays.length-2] = parameterValues;
		Integer[] dimPosObj = null;
		if( dimPos == null )
		{
			dimPosObj = new Integer[1];
			dimPosObj[0] = new Integer( 0 );
		}
		else
		{
			dimPosObj = new Integer[dimPos.length + 1];
			dimPosObj[0] = new Integer( 1 );
			for ( int i = 0; i < dimPos.length; i++ )
			{
				dimPosObj[i + 1] = new Integer( dimPos[i] );
			}
		}
		objectArrays[objectArrays.length-1] = dimPosObj;
		return ObjectArrayUtil.convert( objectArrays );
	}
	
	/*
	 * 
	 */
	public static IStructureCreator getCreator()
	{
		return new Row4AggregationCreator( );
	}

	
	public int[] getDimPos( )
	{
		return dimPos;
	}

	
	public void setDimPos( int[] dimPos )
	{
		this.dimPos = dimPos;
	}

	public void setLevelMembers( Member[] levelMembers )
	{
		this.levelMembers = levelMembers;
	}

	public Member[] getLevelMembers( )
	{
		return levelMembers;
	}

	public void setMeasures( Object[] measures )
	{
		this.measures = measures;
	}

	public Object[] getMeasures( )
	{
		return measures;
	}

	
	public Object[] getParameterValues( )
	{
		return parameterValues;
	}

	
	public void setParameterValues( Object[] parameterValues )
	{
		this.parameterValues = parameterValues;
	}
}

/**
 * 
 * @author Administrator
 *
 */
class Row4AggregationCreator implements IStructureCreator
{
	private static IStructureCreator levelMemberCreator = Member.getCreator( );
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.util.IStructureCreator#createInstance(java.lang.Object[])
	 */
	public IStructure createInstance( Object[] fields )
	{
		Object[][] objectArrays = ObjectArrayUtil.convert( fields );
		Row4Aggregation result = new Row4Aggregation( );
		
		result.setLevelMembers( new Member[objectArrays.length - 3] );
		for ( int i = 0; i < result.getLevelMembers().length; i++ )
		{
			result.getLevelMembers()[i] = (Member) levelMemberCreator.createInstance( objectArrays[i] );
		}
		result.setMeasures( objectArrays[objectArrays.length-3] );
		result.setParameterValues( objectArrays[objectArrays.length-2] );
		if( objectArrays[objectArrays.length-1][0].equals( new Integer( 1 ) ) )
		{
			int[] dimPos = new int[objectArrays[objectArrays.length - 1].length - 1];
			for ( int i = 0; i < dimPos.length; i++ )
			{
				dimPos[i] = ((Integer)(objectArrays[objectArrays.length-1][i+1])).intValue( );
			}
			result.setDimPos( dimPos );
		}
		
		return result;
	}
}
