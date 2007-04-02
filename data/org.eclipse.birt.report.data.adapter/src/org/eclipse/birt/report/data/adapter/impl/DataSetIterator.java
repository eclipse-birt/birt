/*
 *************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.report.data.adapter.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.olap.api.cube.IDatasetIterator;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularDimensionHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;

/**
 * This is an implementation of IDatasetIterator interface.
 *
 */
public class DataSetIterator implements IDatasetIterator
{

	//
	private boolean started = false;
	private IResultIterator it;
	private ResultMeta metadata;

	/**
	 * Create DataSetIterator for a hierarchy.
	 * 
	 * @param session
	 * @param hierHandle
	 * @throws BirtException
	 */
	public DataSetIterator( DataRequestSession session,
			TabularHierarchyHandle hierHandle ) throws BirtException
	{
		QueryDefinition query = new QueryDefinition( );
		query.setUsesDetails( true );

		query.setDataSetName( hierHandle.getDataSet( ).getName( ) );

		List resultMetaList = new ArrayList( );
		Map levelNameColumnNamePair = new HashMap( );

		this.prepareLevels( query,
				resultMetaList,
				levelNameColumnNamePair,
				hierHandle );

		this.it = session.prepare( query ).execute( null ).getResultIterator( );
		this.populateMeta( levelNameColumnNamePair, resultMetaList );
	}

	/**
	 * Create DataSetIterator for fact table.
	 * 
	 * @param session
	 * @param cubeHandle
	 * @throws BirtException
	 */
	public DataSetIterator( DataRequestSession session,
			TabularCubeHandle cubeHandle ) throws BirtException
	{
		QueryDefinition query = new QueryDefinition( );

		query.setUsesDetails( true );
		query.setDataSetName( cubeHandle.getDataSet( ).getName( ) );

		List dimensions = cubeHandle.getContents( CubeHandle.DIMENSIONS_PROP );
		List resultMetaList = new ArrayList( );
		HashMap levelNameColumnNamePair = new HashMap( );
		if ( dimensions != null )
		{
			for ( int i = 0; i < dimensions.size( ); i++ )
			{
				TabularDimensionHandle dimension = (TabularDimensionHandle) dimensions.get( i );
				List hiers = dimension.getContents( DimensionHandle.HIERARCHIES_PROP );

				//By now we only support one hierarchy per dimension.
				assert hiers.size( ) == 1;

				TabularHierarchyHandle hierHandle = (TabularHierarchyHandle) hiers.get( 0 );

				if ( hierHandle.getDataSet( ) == null
						|| hierHandle.getDataSet( )
								.getName( )
								.equals( cubeHandle.getDataSet( ).getName( ) ) )
				{
					prepareLevels( query,
							resultMetaList,
							levelNameColumnNamePair,
							hierHandle );
				}
				else
				{
					//TODO need model support.
					/*//Use different data set
					 Iterator it = cubeHandle.joinConditionsIterator( );
					 DimensionConditionHandle dimCondHandle = (DimensionConditionHandle) it.next( );
					 
					 if ( dimCondHandle.getHierarchy( ).equals( hierHandle ))
					 {
					 dimCondHandle.getPrimaryKeys( )
					 }*/
				}

			}
		}

		prepareMeasure( cubeHandle, query, resultMetaList );

		this.it = session.prepare( query ).execute( null ).getResultIterator( );

		populateMeta( levelNameColumnNamePair, resultMetaList );

	}

	/**
	 * 
	 * @param cubeHandle
	 * @param query
	 * @param resultMetaList
	 */
	private void prepareMeasure( TabularCubeHandle cubeHandle,
			QueryDefinition query, List resultMetaList )
	{
		List measureGroups = cubeHandle.getContents( CubeHandle.MEASURE_GROUPS_PROP );
		for ( int i = 0; i < measureGroups.size( ); i++ )
		{
			MeasureGroupHandle mgh = (MeasureGroupHandle) measureGroups.get( i );
			List measures = mgh.getContents( MeasureGroupHandle.MEASURES_PROP );
			for ( int j = 0; j < measures.size( ); j++ )
			{
				MeasureHandle measure = (MeasureHandle) measures.get( j );
				String function = measure.getFunction( );
				if ( query.getGroups( ).size( ) > 0 )
				{
					ScriptExpression se = populateExpression( query,
							measure,
							function );

					query.addResultSetExpression( measure.getName( ), se );
				}
				else
				{
					query.addResultSetExpression( measure.getName( ),
							new ScriptExpression( measure.getMeasureExpression( ) ) );
				}

				ColumnMeta meta = new ColumnMeta( measure.getName( ) );
				//TODO after model finish support measure type, use data type defined in
				//measure handle.
				meta.setDataType( DataType.DOUBLE_TYPE );
				resultMetaList.add( meta );
			}
		}
	}

	/**
	 * 
	 * @param query
	 * @param measure
	 * @param function
	 * @return
	 */
	private ScriptExpression populateExpression( QueryDefinition query,
			MeasureHandle measure, String function )
	{
		ScriptExpression se = null;
		
		if ( function == null || function.equals( "sum" ) )
		{
			se = new ScriptExpression( "Total.sum("
					+ measure.getMeasureExpression( ) + ",null,"
					+ query.getGroups( ).size( ) + ")" );

		}
		else if ( function.equals( "count" ) )
		{
			se = new ScriptExpression( "Total.count("
					+ "null," + query.getGroups( ).size( ) + ")" );
		}
		else if ( function.equals( "min" ) )
		{
			se = new ScriptExpression( "Total.min("
					+ measure.getMeasureExpression( ) + ",null,"
					+ query.getGroups( ).size( ) + ")" );
		}
		else if ( function.equals( "max" ) )
		{
			se = new ScriptExpression( "Total.max("
					+ measure.getMeasureExpression( ) + ",null,"
					+ query.getGroups( ).size( ) + ")" );
		}
		se.setDataType( DataType.DOUBLE_TYPE );
		return se;
	}

	/**
	 * 
	 * @param levelNameColumnNamePair
	 * @param resultMetaList
	 * @throws BirtException
	 */
	private void populateMeta( Map levelNameColumnNamePair, List resultMetaList )
			throws BirtException
	{
		IResultMetaData rm = this.it.getResultMetaData( );
		Iterator levelIt = levelNameColumnNamePair.keySet( ).iterator( );
		while ( levelIt.hasNext( ) )
		{
			String key = levelIt.next( ).toString( );
			for ( int i = 0; i < rm.getColumnCount( ); i++ )
			{
				if ( key.equals( rm.getColumnName( i + 1 ) ) )
				{
					ColumnMeta value = (ColumnMeta) levelNameColumnNamePair.get( key );
					value.setDataType( rm.getColumnType( i + 1 ) );
				}
			}
		}
		this.metadata = new ResultMeta( resultMetaList );
	}

	/**
	 * 
	 * @param query
	 * @param resultMetaList
	 * @param levelNameColumnNamePair
	 * @param hierHandle
	 */
	private void prepareLevels( QueryDefinition query, List resultMetaList,
			Map levelNameColumnNamePair, TabularHierarchyHandle hierHandle )
	{
		//Use same data set as cube fact table
		List levels = hierHandle.getContents( TabularHierarchyHandle.LEVELS_PROP );

		for ( int j = 0; j < levels.size( ); j++ )
		{

			TabularLevelHandle level = (TabularLevelHandle) levels.get( j );
			ColumnMeta temp = new ColumnMeta( level.getColumnName( ) );
			resultMetaList.add( temp );
			levelNameColumnNamePair.put( level.getColumnName( ), temp );
			Iterator it = level.attributesIterator( );
			while ( it.hasNext( ) )
			{
				LevelAttributeHandle levelAttr = (LevelAttributeHandle) it.next( );
				ColumnMeta meta = new ColumnMeta( level.getName( )
						+ "/" + levelAttr.getName( ) );

				meta.setDataType( ModelAdapter.adaptModelDataType( levelAttr.getDataType( ) ) );
				query.addResultSetExpression( meta.getName( ),
						new ScriptExpression( ExpressionUtil.createJSDataSetRowExpression( levelAttr.getName( ) ) ) );
				resultMetaList.add( meta );
			}
			
			query.addResultSetExpression( level.getColumnName( ),
					new ScriptExpression( ExpressionUtil.createJSDataSetRowExpression( level.getColumnName( ) ) ) );
			
			//The leaf level should serve as one of composit primary key of fact table
			if ( j == levels.size( ) - 1 )
			{
				GroupDefinition gd = new GroupDefinition( );
				gd.setKeyExpression( ExpressionUtil.createJSRowExpression( level.getColumnName( ) ) );
				query.addGroup( gd );
			}

		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.cube.IDatasetIterator#close()
	 */
	public void close( ) throws BirtException
	{
		it.close( );

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.cube.IDatasetIterator#getFieldIndex(java.lang.String)
	 */
	public int getFieldIndex( String name ) throws BirtException
	{
		if ( this.metadata == null )
		{
			for ( int i = 1; i <= it.getResultMetaData( ).getColumnCount( ); i++ )
			{
				if ( name.equals( it.getResultMetaData( ).getColumnName( i ) ) )
				{
					return i;
				}
			}
			return -1;
		}
		else
		{
			return this.metadata.getFieldIndex( name );
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.cube.IDatasetIterator#getFieldType(java.lang.String)
	 */
	public int getFieldType( String name ) throws BirtException
	{
		if ( this.metadata == null )
		{
			return it.getResultMetaData( )
					.getColumnType( this.getFieldIndex( name ) );
		}
		else
			return this.metadata.getFieldType( name );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.cube.IDatasetIterator#getValue(int)
	 */
	public Object getValue( int fieldIndex ) throws BirtException
	{
		return it.getValue( this.metadata == null ? it.getResultMetaData( )
				.getColumnName( fieldIndex )
				: this.metadata.getFieldName( fieldIndex ) );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.cube.IDatasetIterator#next()
	 */
	public boolean next( ) throws BirtException
	{
		if ( it.getQueryResults( )
				.getPreparedQuery( )
				.getReportQueryDefn( )
				.getGroups( )
				.size( ) == 0 )
			return it.next( );
		if ( !started )
		{
			started = true;
			return it.next( );
		}
		else
		{
			it.skipToEnd( it.getQueryResults( )
					.getPreparedQuery( )
					.getReportQueryDefn( )
					.getGroups( )
					.size( ) );
			return it.next( );
		}
	}

	/**
	 * 
	 *
	 */
	private class ResultMeta
	{
		//
		private HashMap columnMetaMap;
		private HashMap indexMap;

		/**
		 * Constructor.
		 * @param columnMetas
		 */
		ResultMeta( List columnMetas )
		{
			this.columnMetaMap = new HashMap( );
			this.indexMap = new HashMap( );
			for ( int i = 0; i < columnMetas.size( ); i++ )
			{
				ColumnMeta columnMeta = (ColumnMeta) columnMetas.get( i );
				columnMeta.setIndex( i + 1 );
				this.columnMetaMap.put( columnMeta.getName( ), columnMeta );
				this.indexMap.put( new Integer( i + 1 ), columnMeta );
			}
		}

		/**
		 * 
		 * @param fieldName
		 * @return
		 */
		public int getFieldIndex( String fieldName )
		{
			return ( (ColumnMeta) this.columnMetaMap.get( fieldName ) ).getIndex( );
		}

		/**
		 * 
		 * @param fieldName
		 * @return
		 */
		public int getFieldType( String fieldName )
		{
			return ( (ColumnMeta) this.columnMetaMap.get( fieldName ) ).getType( );
		}

		/**
		 * 
		 * @param index
		 * @return
		 */
		public String getFieldName( int index )
		{
			return ( (ColumnMeta) this.indexMap.get( new Integer( index ) ) ).getName( );
		}
	}

	/**
	 * 
	 *
	 */
	private class ColumnMeta
	{
		//
		private String name;
		private int type;
		private int index;

		/**
		 * 
		 * @param name
		 */
		ColumnMeta( String name )
		{
			this.name = name;
		}

		/**
		 * 
		 * @return
		 */
		public int getIndex( )
		{
			return this.index;
		}

		/**
		 * 
		 * @return
		 */
		public int getType( )
		{
			return this.type;
		}

		/**
		 * 
		 * @return
		 */
		public String getName( )
		{
			return this.name;
		}

		/**
		 * 
		 * @param index
		 */
		public void setIndex( int index )
		{
			this.index = index;
		}

		/**
		 * 
		 * @param type
		 */
		public void setDataType( int type )
		{
			this.type = type;
		}
	}
}
