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

import java.util.Comparator;

/**
 * 
 */

public class IndexKey implements IComparableStructure
{

	private Object[] key;
	private int offset;
	private int dimensionPos;

	public IndexKey( )
	{

	}

	public Object[] getFieldValues( )
	{
		Object[] fields = null;
		fields = new Object[getKey().length+2];
		System.arraycopy( getKey(), 0, fields, 0, getKey().length );
		fields[fields.length-2] = new Integer( getOffset() );
		fields[fields.length-1] = new Integer( getDimensionPos() );
		
		return fields;
	}

	public int compareTo( Object o )
	{
		assert o instanceof IndexKey;
		IndexKey target = (IndexKey) o;
		
		for( int i=0;i<getKey().length;i++)
		{
			if ( getKey()[i] == null && target.getKey()[i] != null )
				return -1;
			if ( getKey()[i] == null && target.getKey()[i] == null )
				return 0;
			if ( getKey()[i] != null && target.getKey()[i] == null )
				return 1;
			int result = 0;
			if ( getKey()[i] instanceof Comparable )
			{
				result = ( (Comparable) getKey()[i] ).compareTo( target.getKey()[i] );
			}
			else
			{
				result = ( getKey()[i].toString() ).compareTo( target.getKey()[i].toString() );
			}
			if( result != 0 )
			{
				return result;
			}
		}
		return 0;
	}

	public static Comparator getKeyComparator( )
	{
		return new Comparator( ) {

			public int compare( Object obj1, Object obj2 )
			{
				return ( (IndexKey) obj1 ).compareTo( obj2 );
			}
		};
	}

	public static Comparator getIndexComparator( )
	{
		return new Comparator( ) {

			public int compare( Object obj1, Object obj2 )
			{
				int index1 = ( (IndexKey) obj1 ).getDimensionPos();
				int index2 = ( (IndexKey) obj2 ).getDimensionPos();
				if ( index1 < index2 )
				{
					return -1;
				}
				if ( index1 == index2 )
				{
					return 0;
				}
				return 1;
			}
		};
	}

	public static IStructureCreator getCreator( )
	{
		return new IndexKeyObjectCreator( );
	}

	/**
	 * @param key the key to set
	 */
	public void setKey( Object[] key )
	{
		this.key = key;
	}

	/**
	 * @return the key
	 */
	public Object[] getKey( )
	{
		return key;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset( int offset )
	{
		this.offset = offset;
	}

	/**
	 * @return the offset
	 */
	public int getOffset( )
	{
		return offset;
	}

	/**
	 * @param dimensionPos the dimensionPos to set
	 */
	public void setDimensionPos( int dimensionPos )
	{
		this.dimensionPos = dimensionPos;
	}

	/**
	 * @return the dimensionPos
	 */
	public int getDimensionPos( )
	{
		return dimensionPos;
	}

}

class IndexKeyObjectCreator implements IStructureCreator
{

	public IStructure createInstance( Object[] fields )
	{
		IndexKey obj = new IndexKey( );
		obj.setKey( new Object[fields.length - 2] );
		System.arraycopy( fields, 0, obj.getKey(), 0, obj.getKey().length );
		obj.setOffset( ( (Integer) fields[fields.length-2] ).intValue( ) );
		obj.setDimensionPos( ( (Integer) fields[fields.length-1] ).intValue( ) );
		
		return obj;
	}
}
