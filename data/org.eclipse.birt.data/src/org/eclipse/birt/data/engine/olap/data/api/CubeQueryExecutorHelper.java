
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
package org.eclipse.birt.data.engine.olap.data.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.archive.FileArchiveReader;
import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.api.cube.ICube;
import org.eclipse.birt.data.engine.olap.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.api.cube.StopSign;
import org.eclipse.birt.data.engine.olap.api.query.ICubeFilterDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationFunctionDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationResultSetSaveUtil;
import org.eclipse.birt.data.engine.olap.data.impl.Constants;
import org.eclipse.birt.data.engine.olap.data.impl.Cube;
import org.eclipse.birt.data.engine.olap.data.impl.SelectionFactory;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationExecutor;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.sort.AggrSortDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.sort.AggrSortHelper;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Dimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionResultIterator;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionRow;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Level;
import org.eclipse.birt.data.engine.olap.data.impl.facttable.FactTableRowIterator;
import org.eclipse.birt.data.engine.olap.data.util.BufferedPrimitiveDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.eclipse.birt.data.engine.olap.util.filter.DimensionFilterEvalHelper;
import org.eclipse.birt.data.engine.olap.util.filter.IJsFilterHelper;
import org.eclipse.birt.data.engine.olap.util.filter.IResultRow;

/**
 * 
 */

public class CubeQueryExecutorHelper implements ICubeQueryExcutorHelper
{
	private Cube cube;
	private List filters = null;
	private List aggrFilters = null;
	private Map dimJSFilterMap = null;
	private Map dimRowForFilterMap = null;
	
	private List rowSort = null;
	private List columnSort = null;
	
	private boolean[] noRecal; // to indicate whether an aggregation need
									// recalculate,whose length should be the
									// same as the length of aggregation
									// definisoin
	
	/**
	 * 
	 * @param cube
	 */
	public CubeQueryExecutorHelper( ICube cube )
	{
		this.cube = (Cube) cube;
		this.filters = new ArrayList( );
		this.aggrFilters = new ArrayList( );
		this.dimJSFilterMap = new HashMap( );
		this.dimRowForFilterMap = new HashMap( );
		
		this.rowSort = new ArrayList();
		this.columnSort = new ArrayList();
	}
	
	/**
	 * get the members according to the specified dimensionName and levelName.
	 * @param dimensionName
	 * @param levelName
	 * @return
	 */
	public IDiskArray getLevelMembers( String dimensionName, String levelName )
	{
		return null;
	}
	
	/**
	 * 
	 * @param cube
	 * @throws BirtException 
	 * @throws IOException 
	 */
	public static ICube loadCube( String cubeName,
			IDocumentManager documentManager, StopSign stopSign ) throws IOException, DataException
	{
		Cube cube = new Cube( cubeName, documentManager );
		cube.load( stopSign );
		return cube;
	}
	
	/**
	 * 
	 * @param name
	 * @param resultSets
	 * @param writer
	 * @throws IOException
	 */
	public static void saveAggregationResultSet( IDocArchiveWriter writer, String name, IAggregationResultSet[] resultSets ) throws IOException
	{
		AggregationResultSetSaveUtil.save( name, resultSets, writer );
	}
	
	/**
	 * 
	 * @param name
	 * @param resultSets
	 * @throws IOException
	 */
	public static void saveAggregationResultSet( String pathName ,String name, IAggregationResultSet[] resultSets ) throws IOException
	{
		IDocArchiveWriter writer = new FileArchiveWriter( getTmpFileName( pathName, name ) );
		AggregationResultSetSaveUtil.save( name, resultSets, writer );
		writer.flush( );
		writer.finish( );
	}
	
	
	/**
	 * 
	 * @param name
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public static IAggregationResultSet[] loadAggregationResultSet( IDocArchiveReader reader, String name ) throws IOException
	{
		return AggregationResultSetSaveUtil.load( name, reader );
	}
	
	/**
	 * 
	 * @param pathName
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public static IAggregationResultSet[] loadAggregationResultSet( String pathName, String name ) throws IOException
	{
		IDocArchiveReader reader = new FileArchiveReader( getTmpFileName( pathName, name ) );
		IAggregationResultSet[] result = AggregationResultSetSaveUtil.load( name, reader );
		reader.close( );
		return result;
	}
	
	/**
	 * 
	 * @param pathName
	 * @param name
	 * @return
	 */
	private static String getTmpFileName( String pathName, String name )
	{
		return pathName + File.separator + "cubequeryresult" +name;
	}
	
	/**
	 * 
	 * @param sort
	 */
	public void addRowSort( AggrSortDefinition sort )
	{
		this.rowSort.add( sort );
	}
	
	/**
	 * 
	 * @param sort
	 */
	public void addColumnSort( AggrSortDefinition sort )
	{
		this.columnSort.add( sort );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.ICubeQueryExcutorHelper#addFilter(java.lang.String, org.eclipse.birt.data.olap.data.api.ISelection[])
	 */
	public void addFilter( String levelName, ISelection[] selections )
	{
		LevelFilter filter = new LevelFilter();
		filter.levelName = levelName;
		filter.selections = selections;
		filters.add( filter );
	}
	
	/**
	 * @param levelFilterList
	 */
	private void addLevelFilters( List levelFilterList )
	{
		this.filters.addAll( levelFilterList );
	}
	

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.ICubeQueryExcutorHelper#clear()
	 */
	public void clear( )
	{
		filters.clear( );
		aggrFilters.clear( );
		dimJSFilterMap.clear( );
		dimRowForFilterMap.clear( );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.ICubeQueryExcutorHelper#close()
	 */
	public void close( )
	{
		filters = null;
		aggrFilters = null;
		dimJSFilterMap = null;
		dimRowForFilterMap = null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.ICubeQueryExcutorHelper#excute(org.eclipse.birt.data.olap.data.impl.AggregationDefinition[], org.eclipse.birt.data.olap.data.impl.StopSign)
	 */
	public IAggregationResultSet[] execute(
			AggregationDefinition[] aggregations, StopSign stopSign )
			throws IOException, BirtException
	{
		IAggregationResultSet[] resultSet = onePassExecute( aggregations,
				stopSign );
		
		noRecal = new boolean[aggregations.length];// all aggregations will be
													// execuated again by
													// default
		if ( aggrFilters.isEmpty( ) == false )
		{// find level filters according to the specified aggregation filters
			List newAddFilters = generateLevelFilters( aggregations, resultSet );
			// add new filters for another aggregation computation
			addLevelFilters( newAddFilters );
			// generate AggregationDefinition array that need to be recalculated
			List aggrList = new ArrayList( );
			for ( int i = 0; i < noRecal.length; i++ )
			{
				if ( noRecal[i] == false )
				{// release all result set that will not be used later
					aggrList.add( aggregations[i] );
					resultSet[i].close( ); 
					resultSet[i] = null;
				}
				else
				{// the i-th aggregation do not need to recalculate, and the
					// coresponding result set should be empty
					resultSet[i].clear( );
				}
			}
			if ( aggrList.size( ) > 0 )
			{
				AggregationDefinition[] recalAggrs = new AggregationDefinition[aggrList.size( )];
				aggrList.toArray( recalAggrs );

				// recompute the aggregation according to new filters
				IAggregationResultSet[] recalResultSet = onePassExecute( recalAggrs,
						stopSign );
				// overwrite the result sets that have been recalculated
				for ( int i = 0, index = 0; i < noRecal.length; i++ )
				{
					if ( noRecal[i] == false )
					{
						resultSet[i] = recalResultSet[index++];
					}
				}
			}
			filters.removeAll( newAddFilters );//restore to original filter list to avoid conflict
		}
		
		if ( !this.columnSort.isEmpty( ) )
		{
			IAggregationResultSet column = AggrSortHelper.sort( this.columnSort,
					resultSet );
			resultSet[findMatchedResultSetIndex( resultSet, column )] = column;
		}
		if ( !this.rowSort.isEmpty( ) )
		{
			IAggregationResultSet row = AggrSortHelper.sort( this.rowSort,
					resultSet );
			resultSet[findMatchedResultSetIndex( resultSet, row )] = row;
		}
		return resultSet;
	}

	/**
	 * @param rSets
	 * @param source
	 * @return
	 * @throws DataException
	 */
	private int findMatchedResultSetIndex( IAggregationResultSet[] rSets, IAggregationResultSet source ) throws DataException
	{
		for( int i = 0; i < rSets.length; i++ )
		{
			if( AggrSortHelper.isEdgeResultSet( rSets[i] ))
			{
				if( source.getLevelName( 0 ).equals( rSets[i].getLevelName( 0 ) ))
					return i;
			}
		}
		throw new DataException("Invalid");
	}
	
	/**
	 * This method is responsible for computing the aggregation result according
	 * to the specified aggregation definitions.
	 * @param aggregations
	 * @param stopSign
	 * @return
	 * @throws DataException
	 * @throws IOException
	 * @throws BirtException
	 */
	private IAggregationResultSet[] onePassExecute(
			AggregationDefinition[] aggregations, StopSign stopSign )
			throws DataException, IOException, BirtException
	{
		String[][] allResultLevels = getAllResultLevels( aggregations );
		IDiskArray[] dimPosition = getFilterResult( );

		int count = 0;
		for ( int i = 0; i < dimPosition.length; i++ )
		{
			if ( dimPosition[i] != null )
			{
				count++;
			}
		}
		IDimension[] dimensions = cube.getDimesions( );
		String[] validDimensionName = new String[count];
		IDiskArray[] validDimPosition = new IDiskArray[count];
		int pos = 0;
		for ( int i = 0; i < dimPosition.length; i++ )
		{
			if ( dimPosition[i] != null )
			{
				validDimPosition[pos] = dimPosition[i];
				validDimensionName[pos] = dimensions[i].getName( );
				pos++;
			}
		}
		FactTableRowIterator facttableRowIterator = new FactTableRowIterator( cube.getFactTable( ),
				validDimensionName,
				validDimPosition,
				stopSign );

		DimensionResultIterator[] dimensionResultIterator = populateDimensionResultIterator( allResultLevels,
				dimPosition );

		AggregationExecutor aggregationCalculatorExecutor = new AggregationExecutor( dimensionResultIterator,
				facttableRowIterator,
				aggregations );
		return aggregationCalculatorExecutor.execute( stopSign );
	}
	
	/**
	 * generate level filters.
	 * @param aggregations
	 * @param resultSet
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	private List generateLevelFilters( AggregationDefinition[] aggregations,
			IAggregationResultSet[] resultSet ) throws IOException,
			DataException
	{
		List levelFilters = new ArrayList( );
		for ( Iterator i = aggrFilters.iterator( ); i.hasNext( ); )
		{
			AggrFilter filter = (AggrFilter) i.next( );
			String[] aggrLevelNames = filter.getAggrLevelNames( );
			for ( int j = 0; j < aggregations.length; j++ )
			{
				if ( isEqualLevels( aggregations[j].getLevelNames( ), aggrLevelNames ) )
				{
					// generate axis filter according to the cube filter definition
					String[] names = filter.getAxisQualifierNames( );
					Object[] values = filter.getAxisQualifierValues( );
					if ( ( names != null )
							&& ( values != null )
							&& ( names.length == values.length ) )
					{
						for ( int k = 0; k < names.length; k++ )
						{
							if ( names[k].equals( filter.getTargetLevelName( ) ) == false )
							{
								LevelFilter axisFilter = new LevelFilter( );
								axisFilter.levelName = names[k];
								ISelection selection = SelectionFactory.createOneRowSelection( new Object[]{
									values[k]
								} );
								axisFilter.selections = new ISelection[]{
									selection
								};
								levelFilters.add( axisFilter );
							}
						}
					}
					// 
					List rows = getResultRows( aggrLevelNames,
							aggregations[j],
							resultSet[j] );
					
					List selections = new ArrayList( );
					for ( Iterator k = rows.iterator( ); k.hasNext( ); )
					{//for any given row, if the aggregation result is true for the specified expression
					//this row should be selected. This is a filter-in filter.
						RowForFilter row = (RowForFilter) k.next( );
						boolean isSelect = filter.getAggrFilter( )
								.evaluateFilter( row );
						if ( isSelect )
						{// generate level filter here
							Object[] selectedKey = new Object[]{
								row.getLevelValue( filter.getTargetLevelName( ) )
							};
							// select aggregation row
							ISelection selection = SelectionFactory.createOneRowSelection( selectedKey );
							selections.add( selection );
						}
					}
					if ( selections.isEmpty( ) )
					{// this aggregation filter will filter out all
						// aggregation result set.
						noRecal[j] = true;
					}
					else
					{
						LevelFilter levelFilter = new LevelFilter( );
						levelFilter.levelName = filter.getTargetLevelName( );
						levelFilter.selections = new ISelection[selections.size( )];
						selections.toArray( levelFilter.selections );
						levelFilters.add( levelFilter );
					}
				}
			}
		}
		return levelFilters;
	}
	
	/**
	 * construct a row that contains all the level and aggregation for filtering.
	 * @param levels
	 * @param aggrDefn
	 * @param resultSet
	 * @return a row instance for filtering.
	 * @throws IOException
	 */
	private List getResultRows( String[] levels,
			AggregationDefinition aggrDefn, IAggregationResultSet resultSet )
			throws IOException
	{
		List rowList = new LinkedList( );
		AggregationFunctionDefinition[] aggrFuncs = aggrDefn.getAggregationFunctions( );
		String[] aggrNames = new String[aggrFuncs.length];
		for ( int i = 0; i < aggrFuncs.length; i++ )
		{
			aggrNames[i] = aggrFuncs[i].getName( );
		}
		for ( int i = 0; i < resultSet.length( ); i++ )
		{
			resultSet.seek( i );
			Object[] levelValues = new Object[levels.length];
			Object[] aggrValues = new Object[aggrFuncs.length];
			// fill level values
			for ( int j = 0; j < levels.length; j++ )
			{
				int levelIndex = resultSet.getLevelIndex( levels[j] );
				levelValues[j] = resultSet.getLevelKeyValue( levelIndex )[0];
			}
			// fill aggregation names and values
			for ( int j = 0; j < aggrFuncs.length; j++ )
			{
				int aggrIndex = resultSet.getAggregationIndex( aggrNames[j] );
				aggrValues[j] = resultSet.getAggregationValue( aggrIndex );
			}
			// generate a row against levels and aggrNames
			RowForFilter row = new RowForFilter( levels, aggrNames );
			row.setLevelValues( levelValues );
			row.setAggrValues( aggrValues );
			rowList.add( row );
		}
		return rowList;
	}
	
	/**
	 * compare two level arrays to determine whether they are equal or not.
	 * @param levelNames1
	 * @param levelNames2
	 * @return
	 */
	private boolean isEqualLevels( String[] levelNames1, String[] levelNames2 )
	{
		if ( levelNames1 == null || levelNames2 == null )
			return false;

		if ( levelNames1.length != levelNames2.length )
			return false;
		for ( int i = 0; i < levelNames1.length; i++ )
		{
			if ( levelNames1[i].equals( levelNames2[i] ) == false )
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param aggregation
	 * @return
	 * @throws DataException 
	 */
	private String[][] getAllResultLevels( AggregationDefinition[] aggregation ) throws DataException
	{
		IDimension[] dimensions = cube.getDimesions( );
		String[][] dimLevels = getAllLevelNames( dimensions );
		List[] dimLevelList = new List[dimensions.length];
		
		for ( int i = 0; i < dimLevelList.length; i++ )
		{
			dimLevelList[i] = new ArrayList( );
		}
		List allAggregationLevel = new ArrayList( );
		for ( int i = 0; i < aggregation.length; i++ )
		{
			if ( aggregation[i].getLevelNames( ) != null )
			{
				for ( int j = 0; j < aggregation[i].getLevelNames( ).length; j++ )
				{
					allAggregationLevel.add( aggregation[i].getLevelNames( )[j] );
				}
			}
		}
		Object[] distinctAggregationLevel = distinct( allAggregationLevel.toArray( ) );
		for ( int i = 0; i < distinctAggregationLevel.length; i++ )
		{
			dimLevelList[getLevelIndex( dimLevels,
					(String)distinctAggregationLevel[i] )].add( distinctAggregationLevel[i] );
		}
		String[][] result = new String[dimensions.length][];
		for ( int i = 0; i < dimensions.length; i++ )
		{
			if( dimLevelList[i].size( ) == 0 )
			{
				continue;
			}
			result[i] = new String[dimLevelList[i].size( )];
			System.arraycopy( dimLevelList[i].toArray( ),
					0,
					result[i],
					0,
					dimLevelList[i].size( ) );
		}
		return result;
	}
	
	/**
	 * 
	 * @param objs
	 * @return
	 */
	private static Object[] distinct( Object[] objs )
	{
		Arrays.sort( objs );
		List result = new ArrayList( );
		result.add( objs[0] );
		for ( int i = 1; i < objs.length; i++ )
		{
			if(((Comparable)objs[i]).compareTo( objs[i-1] )!=0)
			{
				result.add( objs[i] );
			}
		}
		return result.toArray( );
	}
	
	/**
	 * 
	 * @param levelNameArray
	 * @param levelName
	 * @return
	 * @throws DataException 
	 */
	private static int getLevelIndex( String[][] levelNameArray, String levelName ) throws DataException
	{
		for ( int i = 0; i < levelNameArray.length; i++ )
		{
			for ( int j = 0; j < levelNameArray[i].length; j++ )
			{
				if ( levelNameArray[i][j].equals( levelName ) )
				{
					return i;
				}
			}
		}
		throw new DataException( ResourceConstants.MEASURE_NAME_NOT_FOUND,
				levelName );
	}
	
	/**
	 * 
	 * @param dimensions
	 * @return
	 */
	private static String[][] getAllLevelNames( IDimension[] dimensions )
	{
		String[][] result = new String[dimensions.length][];
		for ( int i = 0; i < result.length; i++ )
		{
			ILevel[] levels = dimensions[i].getHierarchy( ).getLevels( );
			result[i] = new String[levels.length];
			for( int j=0;j<levels.length;j++)
			{
				result[i][j] = levels[j].getName( );
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @param resultLevel
	 * @param position
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	private DimensionResultIterator[] populateDimensionResultIterator( String[][] resultLevel,
			IDiskArray[] position ) throws DataException, IOException
	{
		IDimension[] dimensions = cube.getDimesions( );
		DimensionResultIterator[] dimResultSet = new DimensionResultIterator[dimensions.length];
		int count = 0;
		for ( int i = 0; i < dimensions.length; i++ )
		{
			if ( resultLevel[i] != null )
			{
				if ( position[i] == null )
				{
					dimResultSet[i] = new DimensionResultIterator( (Dimension) dimensions[i],
							dimensions[i].findAll( ),
							resultLevel[i] );
				}
				else
				{
					dimResultSet[i] = new DimensionResultIterator( (Dimension) dimensions[i],
							position[i],
							resultLevel[i] );
				}
				count++;
			}
		}
		
		DimensionResultIterator[] result = new DimensionResultIterator[count];
		int pos = 0;
		for( int i=0;i<dimResultSet.length;i++)
		{
			if( dimResultSet[i] != null )
			{
				result[pos] = dimResultSet[i];
				pos++;
			}
		}
		return result;
	}
	
	
	/**
	 * 
	 * @param dimension
	 * @param dimPosition
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	private IDiskArray getJSFilterResult( Dimension dimension, IDiskArray dimPosition ) throws DataException, IOException
	{
		if ( dimPosition == null )
		{
			if(getDimensionJSFilterList( dimension.getName( ) ).size() <= 0)
			{
				return null;
			}
			else
			{
				dimPosition = dimension.findAll( );
			}
		}
		IDiskArray result = new BufferedPrimitiveDiskArray( Constants.LIST_BUFFER_SIZE );
		for ( int i = 0; i < dimPosition.size( ); i++ )
		{
			Integer pos =(Integer) dimPosition.get( i );
			if ( isDimPositionSelected( dimension, pos.intValue( ) ) )
				result.add( pos );
		}
		return result;
	}

	/**
	 * 
	 * @param dimension
	 * @param pos
	 * @throws IOException
	 * @throws DataException
	 */
	private boolean isDimPositionSelected( Dimension dimension, int pos ) throws IOException, DataException
	{
		DimensionRow dimRow = dimension.getRowByPosition( pos );
		List filterList = getDimensionJSFilterList( dimension.getName( ) );
		RowForFilter rowForFilter = getRowForFilter( dimension, dimRow );		
		
		for ( int j = 0; j < filterList.size( ); j++ )
		{
			IJsFilterHelper filterHelper = (IJsFilterHelper) filterList.get( j );
			if( !filterHelper.evaluateFilter( rowForFilter ) )
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param dimensionName
	 * @return
	 */
	private List getDimensionJSFilterList( String dimensionName )
	{
		Object value = dimJSFilterMap.get( dimensionName );
		if( value != null )
		{
			return (List)value;
		}
		List list = new ArrayList();
		dimJSFilterMap.put( dimensionName, list );
		return list;
	}
	
	/**
	 * generate a RowForFilter instance for IJsFilter to evaluate.
	 * @param dimension
	 * @param dimRow
	 * @return
	 */
	private RowForFilter getRowForFilter( Dimension dimension, DimensionRow dimRow )
	{
		RowForFilter rowForFilter = (RowForFilter) dimRowForFilterMap.get( dimension.getName( ) );
		if ( rowForFilter == null )
		{
			String[] fieldNames = getAllFieldNames( dimension );
			rowForFilter = new RowForFilter( fieldNames );
			dimRowForFilterMap.put( dimension.getName( ), rowForFilter );
		}
		// fill values for this row
		List fields = new ArrayList( );
		for ( int i = 0; i < dimRow.getMembers( ).length; i++ )
		{
			if ( dimRow.getMembers( )[i].getKeyValues( ) != null )
			{
				for ( int j = 0; j < dimRow.getMembers( )[i].getKeyValues( ).length; j++ )
				{
					fields.add( dimRow.getMembers( )[i].getKeyValues( )[j] );
				}
			}
			if ( dimRow.getMembers( )[i].getAttributes( ) != null )
			{
				for ( int j = 0; j < dimRow.getMembers( )[i].getAttributes( ).length; j++ )
				{
					fields.add( dimRow.getMembers( )[i].getAttributes( )[j] );
				}
			}
		}
		rowForFilter.setLevelValues( fields.toArray( ) );
		return rowForFilter;
	}
	
	/**
	 * 
	 * @param dimension
	 * @return
	 */
	private static String[] getAllFieldNames( Dimension dimension )
	{
		ILevel[] levels = dimension.getHierarchy( ).getLevels( );
		List fieldNames = new ArrayList( );
		for ( int i = 0; i < levels.length; i++ )
		{
			String[] keyNames = levels[i].getKeyName( );
			if( keyNames!=null )
			{
				for ( int j = 0; j < keyNames.length; j++ )
				{
					fieldNames.add( keyNames[j] );
				}
			}
			String[] attrNames = levels[i].getAttributeNames( );
			if ( attrNames != null )
			{
				for ( int j = 0; j < attrNames.length; j++ )
				{
					fieldNames.add( attrNames[j] );
				}
			}
		}
		String[] result = new String[fieldNames.size( )];
		for( int i=0;i<result.length;i++)
		{
			result[i] = (String) fieldNames.get( i );
		}
		return result;
	}	
	
	
	/**
	 * 
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	private IDiskArray[] getFilterResult( ) throws DataException, IOException
	{
		IDimension[] dimensions = cube.getDimesions( );
		IDiskArray[] dimPosition = new IDiskArray[dimensions.length];
		for ( int i = 0; i < dimPosition.length; i++ )
		{
			dimPosition[i] = getJSFilterResult( (Dimension) dimensions[i],
					getSimpleFilterResult( (Dimension) dimensions[i] ) );
		}
		return dimPosition;
	}
	
	/**
	 * 
	 * @param dimension
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	private IDiskArray getSimpleFilterResult( Dimension dimension ) throws DataException, IOException
	{
		ILevel[] levels = dimension.getHierarchy( ).getLevels( );
		ISelection[][] selections = new ISelection[levels.length][];
		int filterCount = 0;
		for ( int i = 0; i < filters.size( ); i++ )
		{
			LevelFilter filter = (LevelFilter)filters.get( i );
			int index = getIndex( levels, filter.levelName );
			if ( index >= 0 && selections[index] == null )
			{
				selections[index] = filter.selections;
				filterCount ++;
			}
		}
		if(filterCount==0)
		{
			return null;
		}
		Level[] filterLevel = new Level[filterCount];
		ISelection[][] filters = new ISelection[filterCount][];
		int pos = 0;
		for( int i=0;i<selections.length;i++)
		{
			if ( selections[i] != null )
			{
				filterLevel[pos] = (Level)levels[i];
				filters[pos] = selections[i];
				pos++;
			}
		}
		return dimension.find( filterLevel, filters );
	}
	
	/**
	 * 
	 * @param levels
	 * @param levelName
	 * @return
	 */
	private int getIndex( ILevel[] levels, String levelName )
	{
		for( int i=0;i<levels.length;i++)
		{
			if( levels[i].getName( ).equals( levelName ))
			{
				return i;
			}
		}
		return -1;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.api.ICubeQueryExcutorHelper#addJSFilter(org.eclipse.birt.data.engine.olap.util.filter.DimensionFilterEvalHelper)
	 */
	public void addJSFilter( DimensionFilterEvalHelper filterEvalHelper )
	{
		if ( filterEvalHelper.isAggregationFilter( ) == false )
		{
			String dimesionName = filterEvalHelper.getDimensionName( );
			List filterList = getDimensionJSFilterList( dimesionName );
			filterList.add( filterEvalHelper );
		}
		else
		{
			aggrFilters.add( new AggrFilter( filterEvalHelper ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.api.ICubeQueryExcutorHelper#addJSFilter(java.util.List)
	 */
	public void addJSFilter( List filterEvalHelperList )
	{
		for ( int i = 0; i < filterEvalHelperList.size( ); i++ )
		{
			addJSFilter( (DimensionFilterEvalHelper) filterEvalHelperList.get( i ) );
		}
	}
}

/**
 * 
 */
class LevelFilter
{
	String dimensionName;
	String levelName;
	ISelection[] selections;
}

/**
 * 
 *
 */
class AggrFilter
{
	private String[] aggrLevelNames;
	private IJsFilterHelper aggrFilterHelper;
	private String targetLevelName;
	private String[] axisQualifierNames;
	private Object[] axisQualifierValues;
	
	public AggrFilter( DimensionFilterEvalHelper filterEvalHelper)
	{
		aggrFilterHelper = filterEvalHelper;
		ICubeFilterDefinition cubeFilter = filterEvalHelper.getCubeFiterDefinition( );
		targetLevelName = cubeFilter.getTargetLevel( ).getName( );
		aggrLevelNames = filterEvalHelper.getAggrLevelNames( );
		ILevelDefinition[] axisLevels = cubeFilter.getAxisQualifierLevels( );
		if ( axisLevels != null )
		{
			axisQualifierNames = new String[axisLevels.length];
			for ( int i = 0; i < axisLevels.length; i++ )
			{
				axisQualifierNames[i] = axisLevels[i].getName( );
			}
		}
		axisQualifierValues = cubeFilter.getAxisQualifierValues( );
	}

	
	/**
	 * @return the axisQualifierLevelNames
	 */
	public String[] getAxisQualifierNames( )
	{
		return axisQualifierNames;
	}

	
	/**
	 * @return the axisQualifierLevelValues
	 */
	public Object[] getAxisQualifierValues( )
	{
		return axisQualifierValues;
	}

	/**
	 * @return the aggrLevels
	 */
	public String[] getAggrLevelNames( )
	{
		return aggrLevelNames;
	}
	
	/**
	 * @return the aggrFilter
	 */
	public IJsFilterHelper getAggrFilter( )
	{
		return aggrFilterHelper;
	}
	
	/**
	 * @return the targetLevel
	 */
	public String getTargetLevelName( )
	{
		return targetLevelName;
	}
}

/**
 * 
 */
class RowForFilter implements IResultRow
{

	private HashMap levelMap = new HashMap( );
	private HashMap aggrMap = new HashMap( );
	private Object[] levelValues;
	private Object[] aggrValues;

	/**
	 * 
	 * @param levelNames
	 */
	RowForFilter( String[] levelNames )
	{
		for ( int i = 0; i < levelNames.length; i++ )
		{
			levelMap.put( levelNames[i], new Integer( i ) );
		}
	}

	/**
	 * 
	 * @param levelNames
	 * @param aggrNames
	 */
	RowForFilter( String[] levelNames, String[] aggrNames )
	{
		this( levelNames );
		for ( int i = 0; i < aggrNames.length; i++ )
		{
			aggrMap.put( aggrNames[i], new Integer( i ) );
		}
	}

	/**
	 * 
	 * @param levelValues
	 */
	void setLevelValues( Object[] levelValues )
	{
		this.levelValues = levelValues;
	}

	/**
	 * @param dataValues
	 */
	void setAggrValues( Object[] dataValues )
	{
		this.aggrValues = dataValues;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IResultRow#getLevelValue(java.lang.String)
	 */
	public Object getLevelValue( String attrName ) throws DataException
	{
		Object index = levelMap.get( attrName );
		if ( index == null )
		{
			return null;
		}
		return levelValues[( (Integer) index ).intValue( )];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IResultRow#getDataValue(java.lang.String)
	 */
	public Object getAggrValue( String aggrName ) throws DataException
	{
		Object index = aggrMap.get( aggrName );
		if ( index == null )
		{
			return null;
		}
		return aggrValues[( (Integer) index ).intValue( )];
	}
}