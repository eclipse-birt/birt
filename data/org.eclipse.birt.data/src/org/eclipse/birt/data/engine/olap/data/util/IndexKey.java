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

	public Object[] key;
	public int offset;
	public int dimensionPos;

	public IndexKey( )
	{

	}

	public Object[] getFieldValues( )
	{
		Object[] fields = null;
		fields = new Object[key.length+2];
		System.arraycopy( key, 0, fields, 0, key.length );
		fields[fields.length-2] = new Integer( offset );
		fields[fields.length-1] = new Integer( dimensionPos );
		
		return fields;
	}

	public int compareTo( Object o )
	{
		assert o instanceof IndexKey;
		IndexKey target = (IndexKey) o;
		
		for( int i=0;i<key.length;i++)
		{
			if ( key[i] == null && target.key[i] != null )
				return -1;
			if ( key[i] == null && target.key[i] == null )
				return 0;
			if ( key[i] != null && target.key[i] == null )
				return 1;
			int result = ( (Comparable) key[i] ).compareTo( target.key[i] );
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
				int index1 = ( (IndexKey) obj1 ).dimensionPos;
				int index2 = ( (IndexKey) obj2 ).dimensionPos;
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

}

class IndexKeyObjectCreator implements IStructureCreator
{

	public IStructure createInstance( Object[] fields )
	{
		assert fields.length > 3;
		IndexKey obj = new IndexKey( );
		obj.key = new Object[fields.length - 1];
		System.arraycopy( fields, 0, obj.key, 0, obj.key.length );
		obj.offset = ( (Integer) fields[fields.length-2] ).intValue( );
		obj.dimensionPos = ( (Integer) fields[fields.length-1] ).intValue( );
		
		return obj;
	}
}
