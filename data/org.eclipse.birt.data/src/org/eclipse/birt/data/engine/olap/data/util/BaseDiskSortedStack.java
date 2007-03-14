
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * A disk stack. This util class can be used to sort a arry.
 */

abstract public class BaseDiskSortedStack
{
	private static final int DEFAULT_BUFFER_SIZE = 1000;
	
	protected List segments = null;
	protected Object[] buffer = null;
	
	private int bufferPos = 0;
	private Object[] popBuffer = null;
	private int[] pointers = null;
	private Comparator comparator = null;
	private boolean forceDistinct = false;
	private Object lastPopObject = null;
	private int size = 0;

	/**
	 * 
	 * @param bufferSize
	 * @param isAscending
	 * @param forceDistinct
	 */
	public BaseDiskSortedStack( int bufferSize, boolean isAscending,
			boolean forceDistinct )
	{
		this( bufferSize, forceDistinct, createComparator( isAscending ) );
	}
	
	/**
	 * 
	 * @param bufferSize
	 * @param forceDistinct
	 * @param comparator
	 */
	public BaseDiskSortedStack( int bufferSize, boolean forceDistinct,
			Comparator comparator )
	{
		if ( bufferSize <= 0 )
		{
			buffer = new Object[DEFAULT_BUFFER_SIZE];
		}
		else
		{
			buffer = new Object[bufferSize];
		}
		segments = new ArrayList( );
		this.comparator = comparator;
		this.forceDistinct = forceDistinct;
		this.size = 0;
	}

	/**
	 * 
	 * @param isAscending
	 */
	private static Comparator createComparator( boolean isAscending )
	{
		if ( isAscending )
		{
			return new Comparator( ) {

				public int compare( Object obj1, Object obj2 )
				{
					Comparable data1 = (Comparable) obj1;
					Comparable data2 = (Comparable) obj2;
					return data1.compareTo( data2 );
				}
			};
		}
		else
		{
			return new Comparator( ) {

				public int compare( Object obj1, Object obj2 )
				{
					Comparable data1 = (Comparable) obj1;
					Comparable data2 = (Comparable) obj2;
					return data2.compareTo( data1 );
				}
			};
		}
	}

	/**
	 * 
	 * @param o
	 * @return
	 * @throws IOException
	 */
	public void push( Object o ) throws IOException
	{
		if ( bufferPos < buffer.length )
		{
			buffer[bufferPos] = o;
			bufferPos++;
		}
		else
		{
			sort( buffer );
			if ( forceDistinct )
			{
				int endIndex = removeDuplicated( buffer );
				saveToDisk( 0, endIndex );
			}
			else
			{
				saveToDisk( 0, buffer.length - 1 );
			}
			buffer[0] = o;
			bufferPos = 1;
		}
		size++;
	}
	
	/**
	 * 
	 * @return
	 */
	public int size( )
	{
		return size;
	}

	/**
	 * @throws IOException
	 * 
	 * 
	 */
	abstract protected void saveToDisk( int fromIndex, int toIndex ) throws IOException;

	/**
	 * Sort an array of ResultObjects using stored comparator.
	 * 
	 * @param self,
	 *            which needs to be sorted
	 */
	private void sort( Object[] objectArray )
	{
		Arrays.sort( objectArray, comparator );
	}
	
	/**
	 * 
	 * @param objectArray
	 * @param fromIndex
	 * @param toIndex
	 */
	private void sort( Object[] objectArray, int fromIndex, int toIndex )
	{
		Arrays.sort( objectArray, fromIndex, toIndex, comparator );
	}
	
	/**
	 * 
	 * @param objectArray
	 * @return
	 */
	private int removeDuplicated( Object[] objectArray )
	{
		return removeDuplicated( objectArray, 0, objectArray.length - 1 );
	}
	
	/**
	 * 
	 * @param objectArray
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 */
	private int removeDuplicated( Object[] objectArray, int fromIndex, int toIndex )
	{
		int pos = fromIndex;
		
		for ( int i = fromIndex + 1; i <= toIndex; i++ )
		{
			if ( comparator.compare( objectArray[i], objectArray[pos] ) != 0 )
			{
				objectArray[++pos] = objectArray[i];
			}
		}
		return pos;
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public Object pop( ) throws IOException
	{
		if ( popBuffer == null )
		{
			initPop( );
		}

		int min = getMin( );
		if ( min < 0 )
		{
			return null;
		}
		Object reObj = popBuffer[min];
		popBuffer[min] = readNext( min );
		if ( forceDistinct )
		{
			if ( lastPopObject == null )
			{
				lastPopObject = reObj;
			}
			else
			{
				if ( ((Comparable)lastPopObject).compareTo( reObj ) == 0 )
				{
					return pop( );
				}
			}
		}
		lastPopObject = reObj;
		return reObj;
	}

	/**
	 * 
	 * @return
	 */
	private int getMin( )
	{
		int result = -1;
		for ( int i = 0; i < popBuffer.length; i++ )
		{
			if ( popBuffer[i] != null )
			{
				result = i;
				break;
			}
		}
		if ( result == -1 )
		{
			return -1;
		}
		for ( int i = result + 1; i < popBuffer.length; i++ )
		{
			if ( popBuffer[i] != null
					&& this.comparator.compare( popBuffer[i],
							popBuffer[result] ) < 0 )
			{
				result = i;
			}
		}
		return result;
	}

	/**
	 * 
	 * @throws IOException
	 */
	private void initPop( ) throws IOException
	{
		sort( buffer, 0, bufferPos );
		if ( this.forceDistinct )
		{
			bufferPos = removeDuplicated( buffer, 0, bufferPos - 1 ) + 1;
		}
		popBuffer = new Object[getSegmentCount( )];
		pointers = new int[getSegmentCount( )];
		for ( int i = 0; i < popBuffer.length; i++ )
		{
			popBuffer[i] = readNext( i );
		}
	}

	/**
	 * 
	 * @return
	 */
	private int getSegmentCount( )
	{
		return segments.size( ) + 1;
	}

	/**
	 * 
	 * @param segmentNo
	 * @return
	 * @throws IOException
	 */
	private Object readNext( int segmentNo ) throws IOException
	{
		if ( segmentNo < segments.size( ) )
		{
			BaseDiskArray diskList = (BaseDiskArray) ( segments.get( segmentNo ) );
			if ( pointers[segmentNo] < diskList.size( ) )
				return diskList.get( pointers[segmentNo]++ );
			else
				return null;

		}
		if ( pointers[segmentNo] >= bufferPos )
		{
			return null;
		}
		return buffer[pointers[segmentNo]++];
	}
	
	
	/**
	 * 
	 * @throws IOException
	 */
	public void close( ) throws IOException
	{
		for( int i=0;i<segments.size( );i++)
		{
			BaseDiskArray diskList = (BaseDiskArray) ( segments.get( i ) );
			diskList.close( );
		}
	}
	
}
