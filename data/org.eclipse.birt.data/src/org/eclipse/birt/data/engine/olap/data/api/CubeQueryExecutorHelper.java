
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.archive.FileArchiveReader;
import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
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
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.LevelFilter;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.TopBottomFilterDefn;
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
	private List topbottomFilters;
	
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
		
		this.rowSort = new ArrayList( );
		this.columnSort = new ArrayList( );
		this.topbottomFilters = new ArrayList( );
	}
	
	/**
	 * TODO: get the members according to the specified level.
	 * @param level
	 * @return
	 */
	public IDiskArray getLevelMembers( DimLevel level )
	{
		return null;
	}
	
	/**
	 * get the attribute reference name.
	 * @param dimName
	 * @param levelName
	 * @param attrName
	 * @return
	 */
	public static String getAttrReference( String dimName, String levelName, String attrName )
	{
		return dimName + '/' + levelName + '/' + attrName;
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
	 * Current we only support muti-top/bottom filters with AND predication.
	 * @param topbottomFilter
	 */
	public void addTopBottomFilter(TopBottomFilterDefn topbottomFilter)
	{
		this.topbottomFilters.add(topbottomFilter);
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
	 * @return sortDefinition list on row edge
	 */
	public List getRowSort( )
	{
		return this.rowSort;
	}
	
	/**
	 * 
	 * @return sortDefinition list on column edge
	 */
	public List getColumnSort( )
	{
		return this.columnSort;
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
	public void addFilter( LevelFilter levelFilter )
	{		
		filters.add( levelFilter );
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
		topbottomFilters.clear( );
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
		topbottomFilters = null;
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
		if ( topbottomFilters.size( ) > 0 )
		{
			selectTopBottomResultSet( aggregations, resultSet );
		}
		return resultSet;
	}
	
	 
	/**
	 * do the top N and bottom N filter work here.
	 * @param aggregations
	 * @param resultSet
	 * @return
	 * @throws IOException 
	 */
	private void selectTopBottomResultSet(
			AggregationDefinition[] aggregations,
			IAggregationResultSet[] resultSet ) throws IOException
	{
		for ( int i = 0; i < aggregations.length; i++ )
		{
			DimLevel[] levels = aggregations[i].getLevels( );
			Map levelRange = new HashMap( );
			for ( Iterator j = topbottomFilters.iterator( ); j.hasNext( ); )
			{
				TopBottomFilterDefn filter = (TopBottomFilterDefn) j.next( );
				if ( isEqualLevels( levels, filter.getAggrLevels( ) ) )
				{
					DimLevel target = filter.getTargerLevel( );
					IntRange range = (IntRange) levelRange.get( target );
					if ( range == null )
					{
						range = new IntRange( 0, resultSet[i].length( ) );
						levelRange.put( target, range );
					}
					if ( filter.isTop( ) )
					{// top N
						int end = filter.getN( );
						if ( end < range.end )
						{
							range.end = end;
						}
					}
					else
					{// bottom N
						int start = resultSet[i].length( ) - filter.getN( );
						if ( start > range.start )
						{
							range.start = start;
						}
					}
				}
			}
			for ( Iterator j = levelRange.keySet( ).iterator( ); j.hasNext( ); )
			{
				DimLevel level = (DimLevel) j.next( );
				IntRange range = (IntRange) levelRange.get( level );
				if ( range.start >= range.end )
				{// there is no intersection of the top/bottom filters, so
					// that the result set should be empty
					resultSet[i].clear( );
					continue;
				}
				resultSet[i].subset( level, range.start, range.end );
			}
		}
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
				if( source.getLevel( 0 ).equals( rSets[i].getLevel( 0 ) ))
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

		DimensionResultIterator[] dimensionResultIterator = populateDimensionResultIterator( dimPosition );

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
			DimLevel[] aggrLevels = filter.getAggrLevels( );
			for ( int j = 0; j < aggregations.length; j++ )
			{
				if ( isEqualLevels( aggregations[j].getLevels( ), aggrLevels ) )
				{
					// generate axis filter according to the cube filter definition
					DimLevel[] axisLevels = filter.getAxisQualifierLevels( );
					Object[] axisValues = filter.getAxisQualifierValues( );
					if ( ( axisLevels != null )
							&& ( axisValues != null )
							&& ( axisLevels.length == axisValues.length ) )
					{
						for ( int k = 0; k < axisLevels.length; k++ )
						{
							if ( !axisLevels[k].equals( filter.getTargetLevel( ) ) )
							{
								ISelection selection = SelectionFactory.createOneRowSelection( new Object[]{
									axisValues[k]
								} );

								LevelFilter axisFilter = new LevelFilter( axisLevels[k],
										new ISelection[]{
											selection
										} );
								levelFilters.add( axisFilter );
							}
						}
					}
					//-----------------------------------------------------------------------
					List selectionList = new ArrayList( );
					AggregationFunctionDefinition[] aggrFuncs = aggregations[j].getAggregationFunctions( );
					// TODO: currently we just support one level key
					// generate a row against levels and aggrNames
					String[] fields = getAllFieldNames( aggrLevels,
							resultSet[j] );
					String[] aggrNames = new String[aggrFuncs.length];
					for ( int k = 0; k < aggrFuncs.length; k++ )
					{
						aggrNames[k] = aggrFuncs[k].getName( );
					}
					for ( int k = 0; k < resultSet[j].length( ); k++ )
					{
						resultSet[j].seek( k );
						int fieldIndex = 0;
						Object[] fieldValues = new Object[fields.length];
						Object[] aggrValues = new Object[aggrFuncs.length];
						// fill field values
						for ( int m = 0; m < aggrLevels.length; m++ )
						{
							int levelIndex = resultSet[j].getLevelIndex( aggrLevels[m] );
							if ( levelIndex < 0
									|| levelIndex >= resultSet[j].getLevelCount( ) )
								continue;							
							fieldValues[fieldIndex++] = resultSet[j].getLevelKeyValue( levelIndex )[0];
							
						}
						// fill aggregation names and values
						for ( int m = 0; m < aggrFuncs.length; m++ )
						{
							int aggrIndex = resultSet[j].getAggregationIndex( aggrNames[m] );
							aggrValues[m] = resultSet[j].getAggregationValue( aggrIndex );
						}
						RowForFilter row = new RowForFilter( fields, aggrNames );
						row.setFieldValues( fieldValues );
						row.setAggrValues( aggrValues );
						boolean isSelect = filter.getAggrFilter( )
								.evaluateFilter( row );
						if ( isSelect )
						{// generate level filter here
							int levelIndex = resultSet[j].getLevelIndex( filter.getTargetLevel( ) );
							// select aggregation row
							ISelection selection = SelectionFactory.createOneRowSelection( resultSet[j].getLevelKeyValue( levelIndex ) );
							selectionList.add( selection );
						}
					}				
					//---------------------------------------------------------------------------------
					if ( selectionList.isEmpty( ) )
					{// this aggregation filter will filter out all
						// aggregation result set.
						noRecal[j] = true;
					}
					else
					{
						ISelection[] selections = new ISelection[selectionList.size( )];
						selectionList.toArray( selections );
						LevelFilter levelFilter = new LevelFilter( filter.getTargetLevel( ),
								selections );
						levelFilters.add( levelFilter );
					}
				}
			}
		}
		return levelFilters;
	}	
	
	
	/**
	 * get all field names of a level, including key column names and attribute column names.
	 * TODO: we just get all the field names, and will further support key names and attributes as field names.
	 * @param levels
	 * @param resultSet
	 * @return
	 */
	private String[] getAllFieldNames( DimLevel[] levels,
			IAggregationResultSet resultSet )
	{
		List fieldNameList = new ArrayList( );
		for ( int i = 0; i < levels.length; i++ )
		{
			int levelIndex = resultSet.getLevelIndex( levels[i] );
			if ( levelIndex < 0 || levelIndex >= resultSet.getLevelCount( ) )
				continue;
			fieldNameList.add( levels[i].getDimensionName( )
					+ '/' + levels[i].getLevelName( ));			
		}
		String[] fieldNames = new String[fieldNameList.size( )];
		fieldNameList.toArray( fieldNames );
		return fieldNames;
	}

	/**
	 * compare two level arrays to determine whether they are equal or not.
	 * @param levels1
	 * @param levels2
	 * @return
	 */
	private boolean isEqualLevels( DimLevel[] levels1, DimLevel[] levels2 )
	{
		if ( levels1 == null || levels2 == null )
			return false;

		if ( levels1.length != levels2.length )
			return false;
		for ( int i = 0; i < levels1.length; i++ )
		{
			if ( levels1[i].equals( levels2[i] ) == false )
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
	/*private DimLevel[][] getAllResultLevels( AggregationDefinition[] aggregation ) throws DataException
	{
		IDimension[] dimensions = cube.getDimesions( );
		List[] dimLevelList = new List[dimensions.length];
		
		for ( int i = 0; i < dimLevelList.length; i++ )
		{
			dimLevelList[i] = new ArrayList( );
		}
		List allAggregationLevel = new ArrayList( );
		for ( int i = 0; i < aggregation.length; i++ )
		{
			if ( aggregation[i].getLevels( ) != null )
			{
				for ( int j = 0; j < aggregation[i].getLevels( ).length; j++ )
				{
					allAggregationLevel.add( aggregation[i].getLevels( )[j] );
				}
				
			}
		}
		Object[] distinctAggregationLevel = distinct( allAggregationLevel.toArray( ) );
		for ( int i = 0; i < distinctAggregationLevel.length; i++ )
		{
			dimLevelList[getDimensionIndex( dimensions,
					(DimLevel)distinctAggregationLevel[i] )].add( distinctAggregationLevel[i] );
		}
		DimLevel[][] result = new DimLevel[dimensions.length][];
		for ( int i = 0; i < dimensions.length; i++ )
		{
			if( dimLevelList[i].size( ) == 0 )
			{
				continue;
			}
			result[i] = new DimLevel[dimLevelList[i].size( )];
			System.arraycopy( dimLevelList[i].toArray( ),
					0,
					result[i],
					0,
					dimLevelList[i].size( ) );
		}
		return result;
	}*/
	
	/**
	 * 
	 * @param objs
	 * @return
	 */
	/*private static Object[] distinct( Object[] objs )
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
	}*/
	
	/**
	 * 
	 * @param levelNameArray
	 * @param level
	 * @return
	 * @throws DataException 
	 */
/*	private static int getDimensionIndex( IDimension[] dimensions,
			DimLevel level ) throws DataException
	{
		for ( int i = 0; i < dimensions.length; i++ )
		{
			if ( dimensions[i].getName( ).equals( level.getDimensionName( ) ) )
			{
				ILevel[] levels = dimensions[i].getHierarchy( ).getLevels( );
				for ( int j = 0; j < levels.length; j++ )
				{
					if ( levels[j].getName( ).equals( level.getLevelName( ) ) )
					{
						return i;
					}
				}
			}
		}
		throw new DataException( ResourceConstants.MEASURE_NAME_NOT_FOUND,
				level );
	}*/
	
	/**
	 * 
	 * @param dimensions
	 * @return
	 */
	/*private static String[][] getAllLevelNames( IDimension[] dimensions )
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
	}*/
	
	/**
	 * 
	 * @param resultLevels
	 * @param position
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	private DimensionResultIterator[] populateDimensionResultIterator( IDiskArray[] position ) throws DataException, IOException
	{
		IDimension[] dimensions = cube.getDimesions( );
		DimensionResultIterator[] dimResultSet = new DimensionResultIterator[dimensions.length];
		int count = 0;
		for ( int i = 0; i < dimensions.length; i++ )
		{
			if ( position[i] == null )
				{
					dimResultSet[i] = new DimensionResultIterator( (Dimension) dimensions[i],
							dimensions[i].findAll( ));
				}
				else
				{
					dimResultSet[i] = new DimensionResultIterator( (Dimension) dimensions[i],
							position[i] );
				}
				count++;			
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
		rowForFilter.setFieldValues( fields.toArray( ) );
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
		List fieldNameList = new ArrayList( );
		for ( int i = 0; i < levels.length; i++ )
		{
			String[] keyNames = levels[i].getKeyNames( );
			if ( keyNames != null )
			{
				for ( int j = 0; j < keyNames.length; j++ )
				{
					fieldNameList.add( CubeQueryExecutorHelper.getAttrReference( dimension.getName( ),
							levels[i].getName( ),
							keyNames[j] ) );
				}
			}
			String[] attrNames = levels[i].getAttributeNames( );
			if ( attrNames != null )
			{
				for ( int j = 0; j < attrNames.length; j++ )
				{
					fieldNameList.add( CubeQueryExecutorHelper.getAttrReference( dimension.getName( ),
							levels[i].getName( ), attrNames[j] ) );
				}
			}
		}
		String[] fieldNames = new String[fieldNameList.size( )];
		fieldNameList.toArray( fieldNames );
		return fieldNames;
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
			if ( filter.getDimensionName( ).equals( dimension.getName( ) ) == false )
			{
				continue;
			}
			int index = getIndex( levels, filter.getLevelName( ) );
			if ( index >= 0 && selections[index] == null )
			{
				selections[index] = filter.getSelections( );
				filterCount ++;
			}
		}
		if(filterCount==0)
		{
			return null;
		}
		Level[] filterLevel = new Level[filterCount];
		ISelection[][] selects = new ISelection[filterCount][];
		int pos = 0;
		for( int i=0;i<selections.length;i++)
		{
			if ( selections[i] != null )
			{
				filterLevel[pos] = (Level)levels[i];
				selects[pos] = selections[i];
				pos++;
			}
		}
		return dimension.find( filterLevel, selects );
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
class IntRange
{
	int start;
	int end;
	IntRange( int start, int end )
	{
		this.start = start;
		this.end = end;
	}
}

/**
 * 
 *
 */
class AggrFilter
{
	private DimLevel[] aggrLevels;
	private IJsFilterHelper aggrFilterHelper;
	private DimLevel targetLevel;
	private DimLevel[] axisQualifierLevels;
	private Object[] axisQualifierValues;
	
	AggrFilter( DimensionFilterEvalHelper filterEvalHelper)
	{
		aggrFilterHelper = filterEvalHelper;
		ICubeFilterDefinition cubeFilter = filterEvalHelper.getCubeFiterDefinition( );
		targetLevel = new DimLevel(cubeFilter.getTargetLevel( ));
		aggrLevels = filterEvalHelper.getAggrLevels( );
		ILevelDefinition[] axisLevels = cubeFilter.getAxisQualifierLevels( );
		if ( axisLevels != null )
		{
			axisQualifierLevels = new DimLevel[axisLevels.length];
			for ( int i = 0; i < axisLevels.length; i++ )
			{
				axisQualifierLevels[i] = new DimLevel(axisLevels[i]);
			}
		}
		axisQualifierValues = cubeFilter.getAxisQualifierValues( );
	}

	
	/**
	 * @return the axisQualifierLevelNames
	 */
	DimLevel[] getAxisQualifierLevels( )
	{
		return axisQualifierLevels;
	}

	
	/**
	 * @return the axisQualifierLevelValues
	 */
	Object[] getAxisQualifierValues( )
	{
		return axisQualifierValues;
	}

	/**
	 * @return the aggrLevels
	 */
	DimLevel[] getAggrLevels( )
	{
		return aggrLevels;
	}
	
	/**
	 * @return the aggrFilter
	 */
	IJsFilterHelper getAggrFilter( )
	{
		return aggrFilterHelper;
	}
	
	/**
	 * @return the targetLevel
	 */
	DimLevel getTargetLevel( )
	{
		return targetLevel;
	}
}

/**
 * 
 */
class RowForFilter implements IResultRow
{

	private HashMap fieldMap = new HashMap( );
	private HashMap aggrMap = new HashMap( );
	private Object[] fieldValues;
	private Object[] aggrValues;

	/**
	 * 
	 * @param fieldNames
	 */
	RowForFilter( String[] fieldNames )
	{
		for ( int i = 0; i < fieldNames.length; i++ )
		{
			fieldMap.put( fieldNames[i].toString( ), new Integer( i ) );
		}
	}

	/**
	 * 
	 * @param fieldNames
	 * @param aggrNames
	 */
	RowForFilter( String[] fieldNames, String[] aggrNames )
	{
		this( fieldNames );
		for ( int i = 0; i < aggrNames.length; i++ )
		{
			aggrMap.put( aggrNames[i], new Integer( i ) );
		}
	}

	/**
	 * 
	 * @param levelValues
	 */
	void setFieldValues( Object[] levelValues )
	{
		this.fieldValues = levelValues;
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

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IResultRow#getFieldValue(java.lang.String)
	 */
	public Object getFieldValue( String field ) throws DataException
	{
		Object index = fieldMap.get( field );
		if ( index == null )
		{
			return null;
		}
		return fieldValues[( (Integer) index ).intValue( )];
	}
	
}