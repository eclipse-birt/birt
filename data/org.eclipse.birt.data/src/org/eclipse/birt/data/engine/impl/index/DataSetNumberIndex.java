/*******************************************************************************
 * Copyright (c) 2004, 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.impl.index;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;

public class DataSetNumberIndex implements IDataSetIndex
{



	private int segNumber;
	private List boundaryStartingValues = new ArrayList();
	private Seg[] segs;

	public DataSetNumberIndex( RAInputStream raIn ) throws DataException
	{
		try
		{
			this.segNumber = IOUtil.readInt( raIn );
			this.segs = new Seg[this.segNumber];
			this.boundaryStartingValues = new ArrayList();
			long[] offsets = new long[this.segNumber];

			DataInputStream din = new DataInputStream( raIn );
			for ( int i = 0; i < offsets.length; i++ )
			{
				offsets[i] = IOUtil.readLong( din );
			}
			for ( int i = 0; i < segNumber; i++ )
			{
				this.boundaryStartingValues.add( IOUtil.readObject( din ) );
				this.segs[i] = new Seg( raIn, offsets[i] );
			}
		}
		catch ( IOException e )
		{
			throw new DataException( e.getLocalizedMessage( ), e );
		}

	}

	public Set<Integer> seekG( Object target, boolean incEqual) throws DataException
	{
		Set<Integer> result = new HashSet<Integer>();
		int primaryIndex = binarySearch( target, this.boundaryStartingValues, IConditionalExpression.OP_LE );
		
		if( primaryIndex < 0 || primaryIndex >= this.boundaryStartingValues.size( ))
			return result;
		result = segs[primaryIndex].seekG( target, incEqual );
		for( int i = primaryIndex+1; i < segs.length; i++ )
		{
			result.addAll( segs[i].seekAll( ) );
		}
		return result;
	}
	
	public Set<Integer> seekL( Object target, boolean incEqual ) throws DataException
	{
		Set<Integer> result = new HashSet<Integer>();
		int primaryIndex = binarySearch( target, this.boundaryStartingValues, IConditionalExpression.OP_LE );
		
		if( primaryIndex < 0 || primaryIndex >= this.boundaryStartingValues.size( ))
			return result;
		result = segs[primaryIndex].seekL( target, incEqual );
		for( int i = 0; i < primaryIndex; i++ )
		{
			result.addAll( segs[i].seekAll( ) );
		}
		return result;
	}
	
	public Set<Integer> seekEQ( Object target ) throws DataException
	{
		Set<Integer> result = new HashSet<Integer>();
		int primaryIndex = binarySearch( target, this.boundaryStartingValues, IConditionalExpression.OP_LE );
		if( primaryIndex < 0 || primaryIndex >= this.boundaryStartingValues.size( ))
			return result;
		return segs[primaryIndex].seek( target );
	}

	public Set<Integer> seekBetween( Object target1, Object target2 ) throws DataException
	{
		Set<Integer> result = new HashSet<Integer>( );
		int primaryIndex1 = binarySearch( target1,
				this.boundaryStartingValues,
				IConditionalExpression.OP_LE );
		int primaryIndex2 = binarySearch( target2,
				this.boundaryStartingValues,
				IConditionalExpression.OP_LE );
		if ( ( primaryIndex1 == primaryIndex2 )
				&& primaryIndex1 >= 0
				&& primaryIndex1 < this.boundaryStartingValues.size( ) )
		{
			return segs[primaryIndex1].seekBetween( target1, target2 );
		}
		if( primaryIndex1 > primaryIndex2 )
			return result;
		
		result.addAll( segs[primaryIndex1].seekG( target1, true ) );
		result.addAll( segs[primaryIndex2].seekL( target2, true ) );
		for( int i = primaryIndex1 + 1; i < primaryIndex2 ; i++ )
			result.addAll( segs[i].seekAll( ) );
		return result;
	}
	
	
	private static Integer binarySearch( Object target, List sortedList,
			int searchOption ) throws DataException
	{
		int low = 0;
		int high = sortedList.size( ) - 1;
		int mid = ( low + high ) >> 1;

		while ( low <= high )
		{
			mid = ( low + high ) >> 1;
			Object midVal = sortedList.get( mid );
			int cmp = ScriptEvalUtil.compare( midVal, target );

			if ( cmp < 0 )
				low = mid + 1;
			else if ( cmp > 0 )
				high = mid - 1;
			else
			{
				if ( searchOption == IConditionalExpression.OP_EQ
						|| searchOption == IConditionalExpression.OP_GE
						|| searchOption == IConditionalExpression.OP_LE )
					return mid;
				break;
			}
		}

		if( low > high )
		{
			int temp = low;
			low = high;
			high = temp;
			if( high >= sortedList.size( ))
				high = sortedList.size( ) - 1;
		}
		if ( searchOption == IConditionalExpression.OP_EQ )
		{
			int threshHold = -1;
			for ( int i = low; i <= high; i++ )
			{
				if ( ScriptEvalUtil.compare( sortedList.get( i ), target ) == 0 )
				{
					return i;
				}
			}

		}
		else if ( searchOption == IConditionalExpression.OP_GE )
		{
			for ( int i = low; i <= high; i++ )
			{
				if ( ScriptEvalUtil.compare( sortedList.get( i ), target ) >= 0 )
				{
					return i;
				}
			}
		}
		else if ( searchOption == IConditionalExpression.OP_LE )
		{
			for ( int i = high; i >= low; i-- )
			{
				if ( ScriptEvalUtil.compare( sortedList.get( i ), target ) <= 0 )
				{
					return i;
				}
			}
		}
		else if ( searchOption == IConditionalExpression.OP_GT )
		{
			//If value is found
			if ( high == low )
				return high + 1;
			for ( int i = low; i <= high; i++ )
			{
				if ( ScriptEvalUtil.compare( sortedList.get( i ), target ) > 0 )
				{
					return i;
				}
			}
		}
		else if ( searchOption == IConditionalExpression.OP_LT )
		{
			//if value is found
			if  ( high == low )
				return high - 1;
			for ( int i = high; i >= low; i-- )
			{
				if ( ScriptEvalUtil.compare( sortedList.get( i ), target ) < 0 )
				{
					return i;
				}
			}
		}
		return -1; // key not found
	}

	private class Seg
	{

		private SoftReference<List> keys = new SoftReference( null );
		private SoftReference<List<Set<Integer>>> indexs = new SoftReference( null );
		private RAInputStream raIn;
		private long offset;

		public Seg( RAInputStream raIn, long offset )
		{
			this.raIn = raIn;
			this.offset = offset;
		}

		public int getSize( ) throws DataException
		{
			init( );
			return this.keys.get( ).size( );
		}

		public Set<Integer> seekAll( ) throws DataException
		{
			init( );
			List<Set<Integer>> indexList = this.indexs.get( );
			Set<Integer> result = new HashSet<Integer>( );
			for ( int i = 0; i < indexList.size( ); i++ )
			{
				result.addAll( indexList.get( i ) );
			}
			return result;
		}

		public Set<Integer> seekG( Object value, boolean incEqual )
				throws DataException
		{
			init( );
			List keyList = this.keys.get( );
			List<Set<Integer>> indexList = this.indexs.get( );
			int threshHold = binarySearch( value, keyList, incEqual
					? IConditionalExpression.OP_GE : IConditionalExpression.OP_GT );
			if ( threshHold < 0 || threshHold >= keyList.size( ) )
				return new HashSet( );
			Set<Integer> result = new HashSet<Integer>( );

			for ( int i = threshHold; i < keyList.size( ); i++ )
			{
				result.addAll( indexList.get( i ) );
			}
			return result;
		}

		public Set<Integer> seekL( Object value, boolean incEqual )
				throws DataException
		{
			init( );
			List keyList = this.keys.get( );
			List<Set<Integer>> indexList = this.indexs.get( );
			int threshHold = binarySearch( value, keyList, incEqual
					? IConditionalExpression.OP_LE : IConditionalExpression.OP_LT );
			if ( threshHold < 0 || threshHold >= keyList.size( ) )
				return new HashSet( );
			Set<Integer> result = new HashSet<Integer>( );

			for ( int i = 0; i <= threshHold; i++ )
			{
				result.addAll( indexList.get( i ) );
			}
			return result;
		}

		public Set<Integer> seekBetween( Object value1, Object value2 )
				throws DataException
		{
			init( );
			List keyList = this.keys.get( );
			List<Set<Integer>> indexList = this.indexs.get( );
			int threshHold1 = binarySearch( value1, keyList, IConditionalExpression.OP_GE );
			int threshHold2 = binarySearch( value2, keyList, IConditionalExpression.OP_LE );
			if ( threshHold1 > threshHold2 )
				return new HashSet( );

			Set<Integer> result = new HashSet<Integer>( );

			for ( int i = threshHold1; i <= threshHold2; i++ )
			{
				result.addAll( indexList.get( i ) );
			}
			return result;
		}

		public Set<Integer> seek( Object value ) throws DataException
		{
			init( );
			List keyList = this.keys.get( );
			List<Set<Integer>> indexList = this.indexs.get( );
			int threshHold = binarySearch( value, keyList, IConditionalExpression.OP_EQ );
			if ( threshHold < 0 || threshHold >= keyList.size( ) )
				return new HashSet( );
			return indexList.get( threshHold );
		}

		private void init( ) throws DataException
		{
			try
			{
				if ( this.keys.get( ) == null || this.indexs.get( ) == null )
				{
					List keyList = new LinkedList( );
					List<List<Integer>> indexList = new LinkedList<List<Integer>>( );
					this.raIn.seek( offset );
					DataInputStream din = new DataInputStream( this.raIn );
					int size = IOUtil.readInt( this.raIn );
					for ( int i = 0; i < size; i++ )
					{
						keyList.add( IOUtil.readObject( din ) );
						indexList.add( IOUtil.readList( din ) );
					}
					this.keys = new SoftReference( keyList );
					this.indexs = new SoftReference( indexList );
				}
			}
			catch ( Exception e )
			{
				throw new DataException( e.getLocalizedMessage( ), e );
			}
		}
	}

	public Set<Integer> getKeyIndex( Object key, int searchType ) throws DataException
	{
		if( searchType == IConditionalExpression.OP_EQ )
		{
			return this.seekEQ( key );
		}
		else if( searchType == IConditionalExpression.OP_LE )
		{
			return this.seekL( key, true );
		}
		else if( searchType == IConditionalExpression.OP_LT )
		{
			return this.seekL( key, false );
		}
		else if( searchType == IConditionalExpression.OP_GE )
		{
			return this.seekG( key, true );
		}
		else if( searchType == IConditionalExpression.OP_GT )
		{
			return this.seekG( key, false );
		}
		else if( searchType == IConditionalExpression.OP_BETWEEN )
		{
			assert key instanceof List && ((List)key).size( ) == 2;
			return this.seekBetween( ((List)key).get( 0 ), ((List)key).get( 1 ) );
		}
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.index.IDataSetIndex#supportFilter(int)
	 */
	public boolean supportFilter( int filterType ) throws DataException
	{
		if ( filterType != IConditionalExpression.OP_BETWEEN
				&& filterType != IConditionalExpression.OP_EQ
				&& filterType != IConditionalExpression.OP_LE
				&& filterType != IConditionalExpression.OP_LT
				&& filterType != IConditionalExpression.OP_GE
				&& filterType != IConditionalExpression.OP_GT )
			return false;
		return true;
	}
}
