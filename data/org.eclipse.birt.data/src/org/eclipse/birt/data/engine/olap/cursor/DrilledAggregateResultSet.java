/**
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.cursor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDrillFilter;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;
import org.eclipse.birt.data.engine.olap.data.util.BufferedStructureArray;
import org.eclipse.birt.data.engine.olap.data.util.CompareUtil;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.eclipse.birt.data.engine.olap.query.view.CubeQueryDefinitionUtil;

/**
 * 
 * @author Administrator
 * 
 */
public class DrilledAggregateResultSet implements IAggregationResultSet
{

	private IDiskArray bufferedStructureArray;
	private DimLevel[] dimLevel;
	private DrilledAggregationCalculator calculator;
	private IAggregationResultRow resultObject;
	private IAggregationResultSet aggregationRsFromCube;
	private int currentPosition;

	public DrilledAggregateResultSet(
			IAggregationResultSet aggregationRsFromCube,
			List<IEdgeDrillFilter[]> drillFilters ) throws IOException, DataException
	{
		bufferedStructureArray = new BufferedStructureArray( AggregationResultRow.getCreator( ),
				2000 );

		this.dimLevel = aggregationRsFromCube.getAllLevels( );
		this.aggregationRsFromCube = aggregationRsFromCube;
		
		if ( aggregationRsFromCube.getAggregationCount( ) > 0 )
		{
			AggregationDefinition aggr = aggregationRsFromCube.getAggregationDefinition( );
			calculator =  new DrilledAggregationCalculator( aggr );
		}

		for ( int k = 0; k < aggregationRsFromCube.length( ); k++ )
		{
			aggregationRsFromCube.seek( k );
			IEdgeDrillFilter targetDrill = getTargetDrillOperation( aggregationRsFromCube.getCurrentRow( ),
					drillFilters );

			if ( targetDrill == null )
			{
				bufferedStructureArray.add( aggregationRsFromCube.getCurrentRow( ) );
				continue;
			}
			
			List<IAggregationResultRow> tempBufferArray = populateResultSet( aggregationRsFromCube,
					targetDrill );
			
			List<IEdgeDrillFilter[]> drills = this.getRemainingDrillOperation( targetDrill,
					drillFilters );

			if ( !drills.isEmpty( ) )
			{
				tempBufferArray = populateNextResultSet( tempBufferArray,
						drills );
			}
			
			if ( this.calculator == null )
			{
				removeDuplictedRow( tempBufferArray );
			}
			else
				recalculateAggregation( tempBufferArray );
			
			sortAggregationRow( tempBufferArray );

			Iterator<IAggregationResultRow> iter = tempBufferArray.iterator( );
			while ( iter.hasNext( ) )
				bufferedStructureArray.add( iter.next( ) );
			k = aggregationRsFromCube.getPosition( );
		}
		
		this.resultObject = (IAggregationResultRow) bufferedStructureArray.get( 0 );
	}

	private void sortAggregationRow( List<IAggregationResultRow> aggregationRows )
	{
		final int[] sortType = this.aggregationRsFromCube.getSortType( );

		final boolean[] sorts = new boolean[sortType.length];
		for ( int i = 0; i < sortType.length; i++ )
		{
			if ( ISortDefinition.SORT_ASC == sortType[i] )
				sorts[i] = true;
			else if ( ISortDefinition.SORT_DESC == sortType[i] )
				sorts[i] = false;
			else
				sorts[i] = true;
		}
		Collections.sort( aggregationRows, new Comparator( ) {

			public int compare( Object o1, Object o2 )
			{
				IAggregationResultRow row1 = (IAggregationResultRow) o1;
				IAggregationResultRow row2 = (IAggregationResultRow) o2;
				Object[] keyValues1 = new Object[row1.getLevelMembers( ).length];
				Object[] keyValues2 = new Object[row2.getLevelMembers( ).length];
				for ( int i = 0; i < row1.getLevelMembers( ).length; i++ )
				{
					if ( row1.getLevelMembers( )[i] != null )
						keyValues1[i] = row1.getLevelMembers( )[i].getKeyValues( )[0];
					else
						keyValues1[i] = null;
					if ( row2.getLevelMembers( )[i] != null )
						keyValues2[i] = row2.getLevelMembers( )[i].getKeyValues( )[0];
					else
						keyValues2[i] = null;						
				}
				return CompareUtil.compare( keyValues1, keyValues2, sorts );
			}
		} );
	}

	private void removeDuplictedRow( List<IAggregationResultRow> aggregationRows )
	{
		for ( int i = 0; i < aggregationRows.size( ); i++ )
		{
			IAggregationResultRow rows = aggregationRows.get( i );
			for ( int k = i + 1; k < aggregationRows.size( ); )
			{
				if ( rows.compareTo( aggregationRows.get( k ) ) == 0 )
				{
					aggregationRows.remove( k );
				}
				else
				{
					k++;
				}
			}
		}
	}

	private void recalculateAggregation(
			List<IAggregationResultRow> aggregationRows ) throws DataException,
			IOException
	{
		Set<Integer> duplicatedIndex = new LinkedHashSet<Integer>( );
		for ( int i = 0; i < aggregationRows.size( ); i++ )
		{
			this.calculator.start( );
			List<Integer> positions = getRowsPositionInAggregationRows( i,
					aggregationRows );
			for ( int k = 0; k < positions.size( ); k++ )
			{
				this.calculator.onRow( aggregationRows.get( positions.get( k ) ) );
				if ( k != 0 )
					duplicatedIndex.add( positions.get( k ) );
			}
			this.calculator.finish( aggregationRows.get( i ) );
			
			int baseIndex = 0;
			Iterator<Integer> iter = duplicatedIndex.iterator( );
			while ( iter.hasNext( ) )
			{
				int index = iter.next( ).intValue( );
				aggregationRows.remove( index - baseIndex );
				baseIndex++;
			}
			duplicatedIndex.clear( );
		}
	}

	private List<Integer> getRowsPositionInAggregationRows( int index,
			List<IAggregationResultRow> aggregationRows )
	{
		List<Integer> position = new ArrayList<Integer>( );
		position.add( index );
		IAggregationResultRow row = (IAggregationResultRow) aggregationRows.get( index );
		for ( int i = index + 1; i < aggregationRows.size( ); i++ )
		{
			if ( row.compareTo( aggregationRows.get( i ) ) == 0 )
				position.add( i );
		}
		return position;
	}

	private List<IAggregationResultRow> populateNextResultSet(
			List<IAggregationResultRow> tempBufferArray,
			List<IEdgeDrillFilter[]> nextDrills ) throws IOException
	{
		List finalBufferArray = new ArrayList<IAggregationResultRow>( );
		for ( int i = 0; i < tempBufferArray.size( ); i++ )
		{
			IAggregationResultRow rows = tempBufferArray.get( i );
			for ( int k = 0; k < nextDrills.size( ); k++ )
			{
				IEdgeDrillFilter[] filters = nextDrills.get( k );
				for ( int t = 0; t < filters.length; t++ )
				{
					IEdgeDrillFilter targetFilter = filters[t];
					rows = this.populateResultSet( rows, targetFilter );
				}
			}
			finalBufferArray.add( rows );
		}
		return finalBufferArray;
	}

	private IAggregationResultRow populateResultSet(
			IAggregationResultRow aggregationRow, IEdgeDrillFilter targetDrill )
			throws IOException
	{
		IAggregationResultRow row = aggregationRow;
		if ( isDrilledElement( aggregationRow, targetDrill ) )
		{
			Member[] drillMember = new Member[this.dimLevel.length];
			for ( int i = 0; i < drillMember.length; i++ )
			{
				drillMember[i] = new Member( );
				if ( !this.dimLevel[i].getDimensionName( )
						.equals( targetDrill.getTargetHierarchy( )
								.getDimension( )
								.getName( ) ) )
				{
					drillMember[i] = aggregationRow.getLevelMembers( )[i];
				}
				else
				{
					List comparableLevels = CubeQueryDefinitionUtil.getDrilledTargetLevels( targetDrill );
					if ( comparableLevels.contains( this.dimLevel[i] ) )
					{
						drillMember[i] = aggregationRow.getLevelMembers( )[i];
					}
					else
					{
						drillMember[i] = null;
					}
					continue;
				}
			}
			row = new AggregationResultRow( drillMember,
					aggregationRow.getAggregationValues( ) );
		}
		return row;
	}

	private List<IAggregationResultRow> populateResultSet(
			IAggregationResultSet aggregationRsFromCube,
			IEdgeDrillFilter targetDrill ) throws IOException
	{
		List<IAggregationResultRow> drillResultSet = new ArrayList<IAggregationResultRow>( );
		int k = aggregationRsFromCube.getPosition( );
		for ( ; k < aggregationRsFromCube.length( ); k++ )
		{
			aggregationRsFromCube.seek( k );

			if ( isDrilledElement( aggregationRsFromCube.getCurrentRow( ),
					targetDrill ) )
			{
				IAggregationResultRow row = this.populateResultSet( aggregationRsFromCube.getCurrentRow( ),
						targetDrill );
				drillResultSet.add( row );
			}
			else
			{
				aggregationRsFromCube.seek( k - 1 );
				break;
			}
		}

		return drillResultSet;
	}

	private IEdgeDrillFilter getTargetDrillOperation(
			IAggregationResultRow row, List<IEdgeDrillFilter[]> drillFilters )
	{
		for ( int i = 0; i < drillFilters.size( ); i++ )
		{
			IEdgeDrillFilter[] filters = drillFilters.get( i );
			for ( int t = 0; t < filters.length; t++ )
			{
				if ( isDrilledElement( row, filters[t] ) )
				{
					return filters[t];
				}
			}
		}
		return null;
	}

	private List<IEdgeDrillFilter[]> getRemainingDrillOperation(
			IEdgeDrillFilter targetDrill, List<IEdgeDrillFilter[]> drillFilters )
	{
		List list = new ArrayList( );
		for ( int i = 0; i < drillFilters.size( ); i++ )
		{
			IEdgeDrillFilter[] filters = drillFilters.get( i );
			for ( int t = 0; t < filters.length; t++ )
			{
				if ( !targetDrill.equals( filters[t] ) )
				{
					list.add( filters );
				}
			}
		}
		return list;
	}

	private boolean isDrilledElement( IAggregationResultRow row,
			IEdgeDrillFilter drill )
	{
		List comparableLevels = CubeQueryDefinitionUtil.getDrilledTargetLevels( drill );

		boolean matched = true;
		Object[] tuple = drill.getTuple( ).toArray( );
		for ( int j = 0; j < tuple.length; j++ )
		{
			if ( tuple[j] == null )
				continue;
			int levelIndex = -1;
			for ( int t = 0; t < this.dimLevel.length; t++ )
			{
				if ( this.dimLevel[t].equals( comparableLevels.get( j ) ) )
				{
					levelIndex = t;
					break;
				}
			}
			if ( levelIndex == -1 )
				return false;
			if ( !containMember( row.getLevelMembers( )[levelIndex].getKeyValues( ),
					(Object[]) tuple[j] ) )
			{
				matched = false;
				break;
			}
		}
		return matched;
	}

	private boolean containMember( Object[] levelkey, Object[] key )
	{
		Object[] memberKeys = levelkey;
		for ( Object obj : key )
		{
			if ( obj.toString( ).equals( memberKeys[0].toString( ) ) )
			{
				return true;
			}
		}
		return false;
	}

	public void clear( ) throws IOException
	{
		this.bufferedStructureArray.clear( );
	}

	public void close( ) throws IOException
	{
		this.bufferedStructureArray.close( );
	}

	public int getAggregationCount( )
	{
		return this.aggregationRsFromCube.getAggregationCount( );
	}

	public int getAggregationDataType( int aggregationIndex )
			throws IOException
	{
		return this.aggregationRsFromCube.getAggregationDataType( aggregationIndex );
	}

	public int[] getAggregationDataType( )
	{
		return this.aggregationRsFromCube.getAggregationDataType( );
	}

	public AggregationDefinition getAggregationDefinition( )
	{
		return this.aggregationRsFromCube.getAggregationDefinition( );
	}

	public int getAggregationIndex( String name ) throws IOException
	{
		return this.aggregationRsFromCube.getAggregationIndex( name );
	}

	public String getAggregationName( int index )
	{
		return this.aggregationRsFromCube.getAggregationName( index );
	}

	public Object getAggregationValue( int aggregationIndex )
			throws IOException
	{
		return this.resultObject.getAggregationValues( )[aggregationIndex];
	}

	public DimLevel[] getAllLevels( )
	{
		return this.dimLevel;
	}

	public String[][] getAttributeNames( )
	{
		return this.aggregationRsFromCube.getAttributeNames( );
	}

	public IAggregationResultRow getCurrentRow( ) throws IOException
	{
		return this.resultObject;
	}

	public String[][] getKeyNames( )
	{
		return this.aggregationRsFromCube.getKeyNames( );
	}

	public DimLevel getLevel( int levelIndex )
	{
		return this.aggregationRsFromCube.getLevel( levelIndex );
	}

	public Object getLevelAttribute( int levelIndex, int attributeIndex )
	{
		if ( resultObject.getLevelMembers( ) == null
				|| levelIndex < 0
				|| levelIndex > resultObject.getLevelMembers( ).length - 1
				|| resultObject.getLevelMembers( )[levelIndex] == null )
		{
			return null;
		}
		return resultObject.getLevelMembers( )[levelIndex].getAttributes( )[attributeIndex];
	}

	public int getLevelAttributeColCount( int levelIndex )
	{
		return this.aggregationRsFromCube.getLevelAttributeColCount( levelIndex );
	}

	public int getLevelAttributeDataType( DimLevel level, String attributeName )
	{
		return this.aggregationRsFromCube.getLevelAttributeDataType( level, attributeName );
	}

	public int getLevelAttributeDataType( int levelIndex, String attributeName )
	{
		return this.aggregationRsFromCube.getLevelAttributeDataType( levelIndex, attributeName );
	}

	public int[][] getLevelAttributeDataType( )
	{
		return this.aggregationRsFromCube.getLevelAttributeDataType( );
	}

	public int getLevelAttributeIndex( int levelIndex, String attributeName )
	{
		return this.aggregationRsFromCube.getLevelAttributeIndex( levelIndex, attributeName );
	}

	public int getLevelAttributeIndex( DimLevel level, String attributeName )
	{
		return this.aggregationRsFromCube.getLevelAttributeIndex( level, attributeName );
	}

	public String[] getLevelAttributes( int levelIndex )
	{
		return this.aggregationRsFromCube.getLevelAttributes( levelIndex );
	}

	public String[][] getLevelAttributes( )
	{
		return this.aggregationRsFromCube.getLevelAttributes( );
	}

	public Object[] getLevelAttributesValue( int levelIndex )
	{
		return this.aggregationRsFromCube.getLevelAttributesValue( levelIndex );
	}

	public int getLevelCount( )
	{
		return this.aggregationRsFromCube.getLevelCount( );
	}

	public int getLevelIndex( DimLevel level )
	{
		return this.aggregationRsFromCube.getLevelIndex( level );
	}

	public int getLevelKeyColCount( int levelIndex )
	{
		return this.aggregationRsFromCube.getLevelKeyColCount( levelIndex );
	}

	public int getLevelKeyDataType( DimLevel level, String keyName )
	{
		return this.aggregationRsFromCube.getLevelKeyDataType( level, keyName );
	}

	public int getLevelKeyDataType( int levelIndex, String keyName )
	{
		return this.aggregationRsFromCube.getLevelKeyDataType( levelIndex, keyName );
	}

	public int[][] getLevelKeyDataType( )
	{
		return this.aggregationRsFromCube.getLevelKeyDataType( );
	}

	public int getLevelKeyIndex( int levelIndex, String keyName )
	{
		return this.aggregationRsFromCube.getLevelKeyIndex( levelIndex, keyName );
	}

	public int getLevelKeyIndex( DimLevel level, String keyName )
	{
		return this.aggregationRsFromCube.getLevelKeyIndex( level, keyName );
	}

	public String getLevelKeyName( int levelIndex, int keyIndex )
	{
		return this.aggregationRsFromCube.getLevelKeyName( levelIndex, keyIndex );
	}

	public Object[] getLevelKeyValue( int levelIndex )
	{
		if ( resultObject.getLevelMembers( )[levelIndex] == null
				|| levelIndex < 0
				|| levelIndex > resultObject.getLevelMembers( ).length - 1 )
		{
			return null;
		}
		return resultObject.getLevelMembers()[levelIndex].getKeyValues();
	}

	public String[][] getLevelKeys( )
	{
		return this.aggregationRsFromCube.getLevelKeys( );
	}

	public int getPosition( )
	{
		return this.currentPosition;
	}

	public int getSortType( int levelIndex )
	{
		return this.aggregationRsFromCube.getSortType( levelIndex );
	}

	public int[] getSortType( )
	{
		return this.aggregationRsFromCube.getSortType( );
	}

	public int length( )
	{
		return bufferedStructureArray.size( );
	}

	public void seek( int index ) throws IOException
	{
		if ( index >= bufferedStructureArray.size( ) )
		{
			throw new IndexOutOfBoundsException( "Index: "
					+ index + ", Size: " + bufferedStructureArray.size( ) );
		}
		currentPosition = index;
		resultObject = (IAggregationResultRow) bufferedStructureArray.get( index );
	}

}
