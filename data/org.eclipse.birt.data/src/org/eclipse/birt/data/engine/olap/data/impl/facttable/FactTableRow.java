
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionKey;
import org.eclipse.birt.data.engine.olap.data.util.IComparableStructure;
import org.eclipse.birt.data.engine.olap.data.util.IStructure;
import org.eclipse.birt.data.engine.olap.data.util.IStructureCreator;

/**
 * Describes a row in a fact table. It includes dimension key columns and measure columns.
 */

public class FactTableRow implements IComparableStructure
{
	private static IStructureCreator creator = null;
	DimensionKey[] dimensionKeys;
	Object[] measures;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.util.IStructure#getFieldValues()
	 */
	public Object[] getFieldValues( )
	{
		assert dimensionKeys != null && measures != null;
		assert dimensionKeys.length > 0  && measures.length > 0;
		
		List result = new ArrayList( );
		result.add( new Integer( dimensionKeys.length ) );
		for ( int i = 0; i < dimensionKeys.length; i++ )
		{
			Object[] dimensionFields = dimensionKeys[i].getFieldValues( );
			result.add( new Integer( dimensionFields.length ) );
			for ( int j = 0; j < dimensionFields.length; j++ )
			{
				result.add( dimensionFields[j] );
			}
		}
		
		result.add( new Integer( measures.length ) );
		
		for ( int i = 0; i < measures.length; i++ )
		{
			result.add( measures[i] );
		}
		return result.toArray( );
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo( Object o )
	{
		FactTableRow other = (FactTableRow) o;

		assert other.dimensionKeys.length == this.dimensionKeys.length;

		for ( int i = 0; i < dimensionKeys.length; i++ )
		{
			int result = ( dimensionKeys[i] ).compareTo( other.dimensionKeys[i] );
			if ( result != 0 )
			{
				return result;
			}
		}
		return 0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals( Object o )
	{
		FactTableRow other = (FactTableRow) o;

		if( other.dimensionKeys.length != this.dimensionKeys.length)
		{
			return false;
		}
		
		for ( int i = 0; i < dimensionKeys.length; i++ )
		{
			if ( !dimensionKeys[i].equals( other.dimensionKeys[i] ) )
			{
				return false;
			}
		}
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString( )
	{
		StringBuffer buffer = new StringBuffer();
		for(int i=0;i<dimensionKeys.length;i++)
		{
			buffer.append( dimensionKeys[i] );
		}
		for(int i=0;i<measures.length;i++)
		{
			if ( measures[i] == null )
				buffer.append( "null" );
			else
				buffer.append( measures[i] );
			buffer.append( ' ' );
		}
		return buffer.toString( );
	}
	
	public static IStructureCreator getCreator( )
	{
		if ( creator == null )
		{
			creator = new FactTableRowCreator( );
		}
		return creator;
	}
}

/**
 * 
 * @author Administrator
 *
 */
class FactTableRowCreator implements IStructureCreator
{

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.util.IStructureCreator#createInstance(java.lang.Object[])
	 */
	public IStructure createInstance( Object[] fields )
	{
		IStructureCreator dimensionCreator = DimensionKey.getCreator( );
		FactTableRow result = new FactTableRow( );
		
		int pointer = 0;
		int dimensionCount = ((Integer)fields[pointer]).intValue( );
		pointer++;
		result.dimensionKeys = new DimensionKey[dimensionCount];
		
		for( int i=0;i<dimensionCount;i++ )
		{
			Object[] dimensionFields = new Object[((Integer)fields[pointer]).intValue( )];
			pointer++;
			System.arraycopy( fields, pointer, dimensionFields, 0, dimensionFields.length );
			pointer+=dimensionFields.length;
			result.dimensionKeys[i] = 
				(DimensionKey)dimensionCreator.createInstance( dimensionFields );
		}
		
		result.measures = new Object[((Integer)fields[pointer]).intValue( )];
		pointer++;
		System.arraycopy( fields, pointer, result.measures, 0, result.measures.length );
		return result;
	}
}