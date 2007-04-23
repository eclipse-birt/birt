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
	private Object[] keyValues = null;
	private int dimensionPos = 0;

	public DimensionKey( int keylCount )
	{
		setKeyValues( new Object[keylCount] );
	}

	/**
	 * 
	 * @return
	 */
	public int getKeyFieldsCount( )
	{
		return getKeyValues().length;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.util.IStructure#getFieldValues()
	 */
	public Object[] getFieldValues( )
	{
		List result = new ArrayList( );
		int nullIndicator = 0;
		for ( int i = 0; i < getKeyValues().length; i++ )
		{
			if ( getKeyValues()[i] != null )
			{
				nullIndicator |= 1 << i;
				result.add( getKeyValues()[i] );
			}
		}
		result.add( new Integer( getKeyValues().length ) );
		result.add( new Integer( nullIndicator ) );
		result.add( new Integer( getDimensionPos() ) );
		
		return result.toArray( );
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo( Object o )
	{
		DimensionKey other = (DimensionKey) o;
		
		return  CompareUtil.compare( getKeyValues(), other.getKeyValues() );
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals( Object o )
	{
		boolean result;
		DimensionKey other = (DimensionKey) o;
		
		for ( int i = 0; i < getKeyValues().length; i++ )
		{
			if ( ( getKeyValues()[i] != null && other.getKeyValues()[i] == null )
					|| ( getKeyValues()[i] == null && other.getKeyValues()[i] != null ) )
			{
				return false;
			}
			else if ( getKeyValues()[i] != null && other.getKeyValues()[i] != null )
			{
				result = getKeyValues()[i].equals( other.getKeyValues()[i] );
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
		for(int i=0;i<getKeyValues().length;i++)
		{
			buffer.append( getKeyValues()[i] );
			buffer.append( ' ' );
		}
		return buffer.toString( );
	}
	
	public static IStructureCreator getCreator( )
	{
		return creator;
	}

	/**
	 * 
	 * @param keyValues
	 */
	public void setKeyValues( Object[] keyValues )
	{
		this.keyValues = keyValues;
	}

	/**
	 * 
	 * @return
	 */
	public Object[] getKeyValues( )
	{
		return keyValues;
	}

	/**
	 * 
	 * @param dimensionPos
	 */	
	public void setDimensionPos( int dimensionPos )
	{
		this.dimensionPos = dimensionPos;
	}

	/**
	 * 
	 * @return
	 */
	public int getDimensionPos( )
	{
		return dimensionPos;
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
		obj.setDimensionPos( ( (Integer) fields[fields.length - 1] ).intValue( ) );
		int pointer = 0;
		for ( int i = 0; i < levelCount; i++ )
		{
			if ( ( nullIndicator & ( 1 << i ) ) != 0 )
			{
				assert pointer < fields.length - 2;
				obj.getKeyValues()[i] = fields[pointer];
				pointer++;
			}
		}
		return obj;
	}
}