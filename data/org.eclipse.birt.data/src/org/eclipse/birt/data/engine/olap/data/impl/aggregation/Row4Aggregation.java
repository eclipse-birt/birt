
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
	Member[] levelMembers;
	Object[] measures;
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.util.IStructure#getFieldValues()
	 */
	public Object[] getFieldValues( )
	{
		Object[][] objectArrays = new Object[levelMembers.length+1][];
		for ( int i = 0; i < levelMembers.length; i++ )
		{
			objectArrays[i] = levelMembers[i].getFieldValues( );
		}
		objectArrays[objectArrays.length-1] = measures;
		return ObjectArrayUtil.convert( objectArrays );
	}
	
	/*
	 * 
	 */
	public static IStructureCreator getCreator()
	{
		return new Row4AggregationCreator( );
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
		
		result.levelMembers = new Member[objectArrays.length - 1];
		for ( int i = 0; i < result.levelMembers.length; i++ )
		{
			result.levelMembers[i] = (Member) levelMemberCreator.createInstance( objectArrays[i] );
		}
		result.measures = objectArrays[objectArrays.length-1];
		
		return result;
	}
}
