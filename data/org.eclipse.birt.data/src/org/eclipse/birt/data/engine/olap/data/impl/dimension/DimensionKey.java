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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.olap.data.util.CompareUtil;
import org.eclipse.birt.data.engine.olap.data.util.IComparableStructure;
import org.eclipse.birt.data.engine.olap.data.util.IStructure;
import org.eclipse.birt.data.engine.olap.data.util.IStructureCreator;

/**
 * 
 */

public class DimensionKey implements IComparableStructure
{

	private static IStructureCreator creator = new DimensionKeyCreator( );
	public Object[] keyValues = null;
	public int dimensionPos = 0;

	public DimensionKey( int keylCount )
	{
		keyValues = new Object[keylCount];
	}

	/**
	 * 
	 * @return
	 */
	public int getKeyFieldsCount( )
	{
		return keyValues.length;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.util.IStructure#getFieldValues()
	 */
	public Object[] getFieldValues( )
	{
		List result = new ArrayList( );
		int nullIndicator = 0;
		for ( int i = 0; i < keyValues.length; i++ )
		{
			if ( keyValues[i] != null )
			{
				nullIndicator |= 1 << i;
				result.add( keyValues[i] );
			}
		}
		result.add( new Integer( keyValues.length ) );
		result.add( new Integer( nullIndicator ) );
		result.add( new Integer( dimensionPos ) );
		
		return result.toArray( );
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo( Object o )
	{
		DimensionKey other = (DimensionKey) o;
		
		return  CompareUtil.compare( keyValues, other.keyValues );
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals( Object o )
	{
		boolean result;
		DimensionKey other = (DimensionKey) o;
		
		for ( int i = 0; i < keyValues.length; i++ )
		{
			if ( ( keyValues[i] != null && other.keyValues[i] == null )
					|| ( keyValues[i] == null && other.keyValues[i] != null ) )
			{
				return false;
			}
			else if ( keyValues[i] != null && other.keyValues[i] != null )
			{
				result = keyValues[i].equals( other.keyValues[i] );
				if ( !result )
				{
					return result;
				}
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		for(int i=0;i<keyValues.length;i++)
		{
			buffer.append( keyValues[i] );
			buffer.append( ' ' );
		}
		return buffer.toString( );
	}
	
	public static IStructureCreator getCreator( )
	{
		return creator;
	}
}

/**
 * 
 * @author Administrator
 *
 */
class DimensionKeyCreator implements IStructureCreator
{

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.util.IStructureCreator#createInstance(java.lang.Object[])
	 */
	public IStructure createInstance( Object[] fields )
	{
		int levelCount = ( (Integer) fields[fields.length - 3] ).intValue( );
		DimensionKey obj = new DimensionKey( levelCount );
		int nullIndicator = ( (Integer) fields[fields.length - 2] ).intValue( );
		obj.dimensionPos = ( (Integer) fields[fields.length - 1] ).intValue( );
		int pointer = 0;
		for ( int i = 0; i < levelCount; i++ )
		{
			if ( ( nullIndicator & ( 1 << i ) ) != 0 )
			{
				assert pointer < fields.length - 2;
				obj.keyValues[i] = fields[pointer];
				pointer++;
			}
		}
		return obj;
	}
}