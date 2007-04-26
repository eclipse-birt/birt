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

package org.eclipse.birt.data.engine.olap.data.impl.aggregation.sort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;
import org.eclipse.birt.data.engine.olap.data.util.BufferedStructureArray;
import org.eclipse.birt.data.engine.olap.data.util.CompareUtil;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;

/**
 * Helper class to sort on aggregations.
 */

public class AggregationSortHelper
{

	/**
	 * The method by call which the sort on aggregation will be executed.
	 * @param base
	 * @param qualifier
	 * @param sortKeys
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	public static IAggregationResultSet sort( IAggregationResultSet base,
			AxisQualifier[] qualifier, SortKey[] sortKeys ) throws IOException,
			DataException
	{
		AggregationResultRowNavigator[] filteredAggrResultSet = createFilteredResultSet( sortKeys,
				qualifier );
		IDiskArray baseDiskArray = getDiskArrayFromAggregationResultSet( base );
		IDiskArray keyDiskArray = populateAggrDiskArray( base, sortKeys, filteredAggrResultSet );

		CompareUtil.sort( new WrapperedDiskArray( baseDiskArray, keyDiskArray ),
				new AggrResultRowComparator( populateNewSortKeys( base,
						sortKeys ) ),
				AggregationResultRow.getCreator( ) );

		return new AggregationResultSet( base.getAggregationDefinition( ),
				baseDiskArray,
				base.getKeyNames( ),
				base.getAggributeNames( ) );
	}

	/**
	 * Populate the aggregation disk array which is then used to combine with base disk array.
	 * @param base
	 * @param sortKeys
	 * @param filteredAggrResultSet
	 * @return
	 * @throws IOException
	 */
	private static IDiskArray populateAggrDiskArray(
			IAggregationResultSet base, SortKey[] sortKeys,
			AggregationResultRowNavigator[] filteredAggrResultSet )
			throws IOException
	{
		IDiskArray keyDiskArray = new BufferedStructureArray( AggregationResultRow.getCreator( ),
				4096 );
		for ( int i = 0; i < base.length( ); i++ )
		{
			base.seek( i );
			
			keyDiskArray.add( new AggrValueOnlyResultRow( toObjArray( createMatchedAggrRow( base,
					sortKeys,
					filteredAggrResultSet) ) ) );
		}
		return keyDiskArray;
	}

	/**
	 * Find all matched AggrRows and populate the aggregation value to a list.
	 * 
	 * @param base
	 * @param sortKeys
	 * @param filteredAggrResultSet
	 * @return
	 * @throws IOException
	 */
	private static List createMatchedAggrRow( IAggregationResultSet base,
			SortKey[] sortKeys,
			AggregationResultRowNavigator[] filteredAggrResultSet ) throws IOException
	{
		List keyValues = new ArrayList();
		for ( int x = 0; x < filteredAggrResultSet.length; x++ )
		{
			SortKey key = sortKeys[x];

			Object[] values = new Object[key.getLevelKeyIndex( )
					+ 1 - key.getLevelKeyOffset( )];
			boolean[] direction = new boolean[key.getLevelKeyIndex( )
					+ 1 - key.getLevelKeyOffset( )];
			// Each level only have one key.
			// Only when the aggr table is the measure table the offset of a key would be greater than 0.
			for ( int y = key.getLevelKeyOffset( ); y < key.getLevelKeyIndex( ) + 1; y++ )
			{
				String levelName = sortKeys[x].getTargetResultSet( )
						.getLevelName( y );
				values[y - key.getLevelKeyOffset( )] = base.getLevelKeyValue( base.getLevelIndex( levelName ) )[0];
				direction[y - key.getLevelKeyOffset( )] = base.getSortType( base.getLevelIndex( levelName ) ) == 1
						? false : true;
			}

			IAggregationResultRow row = getNextMatchRow( filteredAggrResultSet[x],
					values,
					direction,
					key );
			if ( row == null )
				continue;
			for ( int j = 0; j < key.getAggrKeys( ).length; j++ )
			{
				keyValues.add( row.getAggregationValues( )[j] );
			}
		}
		return keyValues;
	}

	/**
	 * Find next matched row in current AggregationResultRowNavigator.
	 * @param orderedArray
	 * @param values
	 * @param dirs
	 * @param sk
	 * @return
	 * @throws IOException
	 */
	private static IAggregationResultRow getNextMatchRow(
			AggregationResultRowNavigator orderedArray, Object[] values,
			boolean[] dirs, SortKey sk ) throws IOException
	{
		IAggregationResultRow currentRow = orderedArray.getCurrentRow( );
		if ( currentRow == null )
			return null;

		Object[] currentValues = new Object[values.length];
		// Each level only have one key.
		for ( int y = 0; y < currentValues.length; y++ )
		{
			currentValues[y] = currentRow.getLevelMembers( )[sk.getLevelKeyOffset( )
					+ y].getKeyValues( )[0];
		}

		int i = CompareUtil.compare( values, currentValues, dirs );
		if ( i < 0 )
			return null;
		if ( i == 0 )
			return currentRow;
		if ( i > 0 )
		{
			orderedArray.next( );
			return getNextMatchRow( orderedArray, values, dirs, sk );
		}
		return null;
	}

	/**
	 * Provide new sort keys for base IAggregationResultSet.
	 * @param base
	 * @param sortKeys
	 * @return
	 */
	private static List populateNewSortKeys( IAggregationResultSet base,
			SortKey[] sortKeys )
	{
		SortKey[] sks = new SortKey[base.getLevelCount( )];
		for ( int i = 0; i < sks.length; i++ )
		{
			for ( int j = 0; j < sortKeys.length; j++ )
			{
				int levelIndexInBase = base.getLevelIndex( sortKeys[j].getLevelKeyName( ) );
				if ( i == levelIndexInBase )
					sks[i] = sortKeys[j];
				if ( i < levelIndexInBase )
					break;
			}
		}

		//For each level there should be a sort key.
		populateEmptySortKey( base, sks );
		
		return createNewSortKeys( base, sks );
	}

	private static List createNewSortKeys( IAggregationResultSet base, SortKey[] sks )
	{
		List newSortKeys = new ArrayList();
		int offset = 0;
		for ( int i = 0; i < sks.length; i++ )
		{
			String levelName = sks[i].getTargetResultSet( )
					.getLevelName( sks[i].getLevelKeyIndex( ) );
			int levelKeyIndex = base.getLevelIndex( levelName );
			int[] aggrKeyIndex = new int[sks[i].getAggrKeys( ).length];
			for ( int j = 0; j < sks[i].getAggrKeys( ).length; j++ )
			{
				aggrKeyIndex[j] = sks[i].getAggrKeys( )[j] + offset;
			}
			offset += sks[i].getAggrKeys( ).length;
			SortKey sk = new SortKey( aggrKeyIndex,
					sks[i].getAggrSortDirection( ),
					levelKeyIndex,
					0,
					sks[i].getTargetResultSet( ) );
			newSortKeys.add( sk );
		}
		return newSortKeys;
	}

	private static void populateEmptySortKey( IAggregationResultSet base,
			SortKey[] sks )
	{
		for ( int i = 0; i < sks.length; i++ )
		{
			if ( sks[i] == null )
			{
				sks[i] = new SortKey( new int[0], new boolean[0], i, 0, base );
			}
		}
	}

	private static IDiskArray getDiskArrayFromAggregationResultSet(
			IAggregationResultSet base ) throws IOException
	{
		IDiskArray diskArray = new BufferedStructureArray( AggregationResultRow.getCreator( ),
				4096 );
		for ( int j = 0; j < base.length( ); j++ )
		{
			base.seek( j );
			IAggregationResultRow temp = base.getCurrentRow( );
			diskArray.add( temp );
		}
		base.seek( 0 );
		return diskArray;
	}

	private static AggregationResultRowNavigator[] createFilteredResultSet(
			SortKey[] aggr, AxisQualifier[] qualifier ) throws IOException,
			DataException
	{
		AggregationResultRowNavigator[] filteredAggrResultSet = new AggregationResultRowNavigator[aggr.length];
		for ( int i = 0; i < aggr.length; i++ )
		{
			IAggregationResultSet rSet = aggr[i].getTargetResultSet( );
			IDiskArray diskArray = filterResultSet( rSet, qualifier[i] );
			filteredAggrResultSet[i] = new AggregationResultRowNavigator( diskArray );
		}
		return filteredAggrResultSet;
	}

	private static IDiskArray filterResultSet( IAggregationResultSet rSet,
			AxisQualifier qualifier ) throws IOException, DataException
	{
		IDiskArray diskArray = new BufferedStructureArray( AggregationResultRow.getCreator( ),
				4096 );
		for ( int j = 0; j < rSet.length( ); j++ )
		{
			rSet.seek( j );
			int[] index = qualifier.getLevelIndex( );
			Object[] values = new Object[index.length];
			for ( int i = 0; i < index.length; i++ )
			{
				values[i] = rSet.getLevelKeyValue( index[i] )[0];
			}

			if ( CompareUtil.compare( values, qualifier.getLevelValue( ) ) == 0 )
			{
				IAggregationResultRow temp = rSet.getCurrentRow( );
				diskArray.add( temp );
			}
		}
		return diskArray;
	}

	static Object[] toObjArray( List l )
	{
		Object[] objs = new Object[l.size( )];
		for ( int i = 0; i < objs.length; i++ )
		{
			objs[i] = l.get( i );
		}
		return objs;
	}

	static boolean[] toDirectionArray( List sortKeys )
	{
		List result = new ArrayList( );
		for ( int i = 0; i < sortKeys.size( ); i++ )
		{
			SortKey sk = (SortKey) sortKeys.get( i );
			for ( int j = 0; j < sk.getAggrSortDirection( ).length; j++ )
			{
				result.add( new Boolean( sk.getAggrSortDirection( )[j] ) );
			}
			result.add( new Boolean( sk.getLevelSortDirection( ) ) );
		}

		boolean[] objs = new boolean[result.size( )];
		for ( int i = 0; i < objs.length; i++ )
		{
			objs[i] = ( (Boolean) result.get( i ) ).booleanValue( );
		}
		return objs;
	}
}

/**
 * A navigator class for an IDiskArray instance.
 *
 */
class AggregationResultRowNavigator
{

	private int index;
	private IDiskArray array;

	/**
	 * 
	 * @param array
	 */
	AggregationResultRowNavigator( IDiskArray array )
	{
		this.index = 0;
		this.array = array;
	}

	/**
	 * 
	 * @return
	 */
	public boolean next( )
	{
		this.index++;
		if ( this.array.size( ) <= this.index )
		{
			this.index = this.array.size( );
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public IAggregationResultRow getCurrentRow( ) throws IOException
	{
		if ( this.index >= this.array.size( ) )
			return null;
		return (IAggregationResultRow) this.array.get( this.index );
	}
}

/**
 * 
 *
 */
class WrapperedDiskArray implements IDiskArray
{

	private int index;
	private IDiskArray base;
	private IDiskArray aggrValues;

	WrapperedDiskArray( IDiskArray base, IDiskArray aggrValues )
	{
		this.base = base;
		this.aggrValues = aggrValues;
		this.index = 0;
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	private Object getCurrentBaseRow( ) throws IOException
	{
		return this.base.get( this.index );
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	private Object getCurrentKeyRow( ) throws IOException
	{
		return this.aggrValues.get( this.index );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.util.IDiskArray#add(java.lang.Object)
	 */
	public boolean add( Object o ) throws IOException
	{
		assert o instanceof CombinedAggrResultRow;
		CombinedAggrResultRow obj = (CombinedAggrResultRow) o;
		AggregationResultRow baseRow = new AggregationResultRow( );
		baseRow.setLevelMembers( obj.getLevelMembers( ) );
		this.base.add( baseRow );

		AggregationResultRow keyRow = new AggregationResultRow( );
		keyRow.setAggregationValues( obj.getAggregationValues( ) );
		this.aggrValues.add( keyRow );
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.util.IDiskArray#clear()
	 */
	public void clear( ) throws IOException
	{
		this.base.clear( );
		this.aggrValues.clear( );

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.util.IDiskArray#close()
	 */
	public void close( ) throws IOException
	{
		this.base.close( );
		this.aggrValues.close( );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.util.IDiskArray#get(int)
	 */
	public Object get( int index ) throws IOException
	{
		this.index = index;
		return new CombinedAggrResultRow( (IAggregationResultRow) this.getCurrentBaseRow( ),
				(IAggregationResultRow) this.getCurrentKeyRow( ) );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.util.IDiskArray#size()
	 */
	public int size( )
	{
		return this.base.size( );
	}
}

/**
 * This class combine two AggregationResultRow object to one. Specifically, it will 
 * get level values from "base", and aggregation values from "key".
 * @author Administrator
 *
 */
class CombinedAggrResultRow implements IAggregationResultRow
{

	private Member[] levelMember;
	private Object[] aggr;

	public CombinedAggrResultRow( IAggregationResultRow base,
			IAggregationResultRow aggr )
	{
		this.levelMember = base.getLevelMembers( );
		this.aggr = aggr.getAggregationValues( );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.api.IAggregationResultRow#getAggregationValues()
	 */
	public Object[] getAggregationValues( )
	{
		return this.aggr;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.api.IAggregationResultRow#getLevelMembers()
	 */
	public Member[] getLevelMembers( )
	{
		return this.levelMember;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.api.IAggregationResultRow#setAggregationValues(java.lang.Object[])
	 */
	public void setAggregationValues( Object[] aggregationValues )
	{
		this.aggr = aggregationValues;

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.api.IAggregationResultRow#setLevelMembers(org.eclipse.birt.data.engine.olap.data.impl.dimension.Member[])
	 */
	public void setLevelMembers( Member[] levelMembers )
	{
		this.levelMember = levelMembers;
	}

}

/**
 * This implementation of IAggregationResultRow only stores aggregation values, without
 * store any level info.
 *
 */
class AggrValueOnlyResultRow implements IAggregationResultRow
{

	private Object[] aggrValues;

	/**
	 * 
	 * @param aggrValues
	 */
	public AggrValueOnlyResultRow( Object[] aggrValues )
	{
		this.aggrValues = aggrValues;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.api.IAggregationResultRow#getAggregationValues()
	 */
	public Object[] getAggregationValues( )
	{
		return this.aggrValues;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.api.IAggregationResultRow#getLevelMembers()
	 */
	public Member[] getLevelMembers( )
	{
		throw new UnsupportedOperationException( );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.api.IAggregationResultRow#setAggregationValues(java.lang.Object[])
	 */
	public void setAggregationValues( Object[] aggregationValues )
	{
		this.aggrValues = aggregationValues;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.api.IAggregationResultRow#setLevelMembers(org.eclipse.birt.data.engine.olap.data.impl.dimension.Member[])
	 */
	public void setLevelMembers( Member[] levelMembers )
	{
		throw new UnsupportedOperationException( );
	}
}

/**
 * The comparator implementation which is used to compare two AggrResultRows 
 * according to given key.
 * 
 *
 */
class AggrResultRowComparator implements Comparator
{

	private List keys;

	AggrResultRowComparator( List keys )
	{
		this.keys = keys;
	}

	public int compare( Object arg0, Object arg1 )
	{
		IAggregationResultRow obj1 = (IAggregationResultRow) arg0;
		IAggregationResultRow obj2 = (IAggregationResultRow) arg1;
		List keyValue1 = new ArrayList( );
		List keyValue2 = new ArrayList( );

		for ( int i = 0; i < keys.size( ); i++ )
		{
			SortKey sk = (SortKey) keys.get( i );
			for ( int j = 0; j < sk.getAggrKeys( ).length; j++ )
			{
				keyValue1.add( obj1.getAggregationValues( )[sk.getAggrKeys( )[j]] );
				keyValue2.add( obj2.getAggregationValues( )[sk.getAggrKeys( )[j]] );
			}
			keyValue1.add( obj1.getLevelMembers( )[sk.getLevelKeyIndex( )].getKeyValues( )[0] );
			keyValue2.add( obj2.getLevelMembers( )[sk.getLevelKeyIndex( )].getKeyValues( )[0] );
		}

		return CompareUtil.compare( AggregationSortHelper.toObjArray( keyValue1 ),
				AggregationSortHelper.toObjArray( keyValue2 ),
				AggregationSortHelper.toDirectionArray( this.keys ) );
	}

}
