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

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDatasetIterator;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.group.GroupCalculatorFactory;
import org.eclipse.birt.report.data.adapter.group.ICalculator;
import org.eclipse.birt.report.data.adapter.i18n.AdapterResourceHandle;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;
import org.eclipse.birt.report.data.adapter.internal.adapter.GroupAdapter;
import org.eclipse.birt.report.model.api.DimensionConditionHandle;
import org.eclipse.birt.report.model.api.DimensionJoinConditionHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.RuleHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularDimensionHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * This is an implementation of IDatasetIterator interface.
 *
 */
public class DataSetIterator implements IDatasetIterator
{

	private static final String DATE_TIME_ATTR_NAME = "DateTime";
	//
	private boolean started = false;
	private IResultIterator it;
	private ResultMeta metadata;

	private static long nullTime;
	
	static
	{
		Calendar c = Calendar.getInstance( );
		c.clear( );
		c.set( 0, 0, 1, 0, 0, 0 );
		nullTime = c.getTimeInMillis( );
		
	}
	
	/**
	 * Create DataSetIterator for a hierarchy.
	 * 
	 * @param session
	 * @param hierHandle
	 * @throws BirtException
	 */
	public DataSetIterator( DataRequestSessionImpl session,
			TabularHierarchyHandle hierHandle ) throws AdapterException
	{
		this( session, hierHandle, null );
	}
	
	/**
	 * Create DataSetIterator for a hierarchy.
	 * 
	 * @param session
	 * @param hierHandle
	 * @throws BirtException
	 */
	public DataSetIterator( DataRequestSessionImpl session,
			TabularHierarchyHandle hierHandle, Map appContext ) throws AdapterException
	{
		QueryDefinition query = new QueryDefinition( );
		query.setUsesDetails( false );
		
		query.setDataSetName( hierHandle.getDataSet( ).getQualifiedName( ) );

		List metaList = new ArrayList( );
		this.prepareLevels( query,
				hierHandle, metaList, null );
		
		popualteFilter( session, hierHandle.filtersIterator( ), query );
		executeQuery( session, query, appContext );
		
		this.metadata = new ResultMeta( metaList );
	}

	private void executeQuery( DataRequestSessionImpl session, QueryDefinition query, Map appContext )
			throws AdapterException
	{
		try
		{
			
			Scriptable scope = session.getScope( );
			TempDateTransformer tt = new TempDateTransformer();
			ScriptableObject.putProperty( scope, tt.getClassName( ), tt );
			this.it = session.prepare( query, appContext ).execute( scope ).getResultIterator( );
		}
		catch ( BirtException e )
		{
			throw new AdapterException( e.getLocalizedMessage( ), e );
		}
	}

	/**
	 * 
	 * @param session
	 * @param cubeHandle
	 * @param appContext
	 * @throws BirtException
	 */
	public DataSetIterator( DataRequestSessionImpl session,
			TabularCubeHandle cubeHandle ) throws BirtException
	{
		this( session, cubeHandle, null );
	}
	
	/**
	 * Create DataSetIterator for fact table.
	 * 
	 * @param session
	 * @param cubeHandle
	 * @throws BirtException
	 */
	public DataSetIterator( DataRequestSessionImpl session,
			TabularCubeHandle cubeHandle, Map appContext ) throws BirtException
	{
		QueryDefinition query = new QueryDefinition( );

		query.setUsesDetails( false );
		query.setDataSetName( cubeHandle.getDataSet( ).getQualifiedName( ) );

		List dimensions = cubeHandle.getContents( CubeHandle.DIMENSIONS_PROP );

		List metaList = new ArrayList();
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
								.getQualifiedName( )
								.equals( cubeHandle.getDataSet( ).getQualifiedName( ) ) )
				{
					prepareLevels( query,
							hierHandle,
							metaList,
							dimension.getName());
				}
				else
				{
					Iterator it = cubeHandle.joinConditionsIterator( );
					while ( it.hasNext( ) )
					{
						DimensionConditionHandle dimCondHandle = (DimensionConditionHandle) it.next( );

						if ( dimCondHandle.getHierarchy( ).equals( hierHandle ) )
						{
							Iterator conditionIt = dimCondHandle.getJoinConditions( )
									.iterator( );
							while ( conditionIt.hasNext( ) )
							{
								DimensionJoinConditionHandle joinCondition = (DimensionJoinConditionHandle) conditionIt.next( );
								String cubeKey = joinCondition.getCubeKey( );
								String cubeKeyWithDimIdentifier = OlapExpressionUtil.getQualifiedLevelName( dimension.getName( ),
										cubeKey );
								metaList.add( new ColumnMeta( cubeKeyWithDimIdentifier,
										null,
										ColumnMeta.LEVEL_KEY_TYPE ) );
								query.addBinding( new Binding( cubeKeyWithDimIdentifier,
										new ScriptExpression( ExpressionUtil.createJSDataSetRowExpression( cubeKey ) ) ) );

								GroupDefinition gd = new GroupDefinition( String.valueOf( query.getGroups( )
										.size( ) ) );
								gd.setKeyExpression( ExpressionUtil.createJSRowExpression( cubeKeyWithDimIdentifier ) );
								query.addGroup( gd );
							}
						}
					}
				}
			}
		}

		prepareMeasure( cubeHandle, query, metaList );
		this.popualteFilter( session, cubeHandle.filtersIterator( ), query );
		executeQuery( session, query, appContext );
		this.metadata = new ResultMeta( metaList );

	}

	/**
	 * 
	 * @param session
	 * @param filterIterator
	 * @param query
	 */
	private void popualteFilter( DataRequestSession session,
			Iterator filterIterator, QueryDefinition query )
	{
		while( filterIterator.hasNext( ) )
		{
			FilterConditionHandle filter = (FilterConditionHandle) filterIterator.next( );
			query.addFilter( session.getModelAdaptor( ).adaptFilter( filter ) );
		}
	}

	/**
	 * 
	 * @param cubeHandle
	 * @param query
	 * @param resultMetaList
	 * @throws DataException 
	 * @throws AdapterException 
	 */
	private void prepareMeasure( TabularCubeHandle cubeHandle,
			QueryDefinition query, List metaList ) throws AdapterException
	{
		try
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
						Binding binding = new Binding( measure.getName( ),
								new ScriptExpression( measure.getMeasureExpression( ) ) );
						binding.setAggrFunction( DataAdapterUtil.adaptModelAggregationType( function ) );
						IGroupDefinition group = (IGroupDefinition) query.getGroups( )
								.get( query.getGroups( ).size( ) - 1 );
						binding.addAggregateOn( group.getName( ) );

						query.addBinding( binding );
					}
					else
					{
						query.addBinding( new Binding( measure.getName( ),
								new ScriptExpression( measure.getMeasureExpression( ) ) ) );
					}

					ColumnMeta meta = new ColumnMeta( measure.getName( ),
							null,
							ColumnMeta.MEASURE_TYPE );
					meta.setDataType( DataAdapterUtil.adaptModelDataType( measure.getDataType( ) ) );
					metaList.add( meta );
				}
			}
		}
		catch ( DataException e )
		{
			throw new AdapterException( e.getLocalizedMessage( ) );
		}
	}

	private int getDefaultStartValue( String timeType, String value ) throws AdapterException
	{
		if( value != null && Double.valueOf( value ).doubleValue( )!= 0 )
			return Integer.valueOf( value ).intValue( );
		if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_MONTH.equals( timeType ) )
		{
			return 1;
		}
		else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_WEEK.equals( timeType ) )
		{
			return 1;
		}
		else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR.equals( timeType ) )
		{
			return 1;
		}
		else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_MONTH.equals( timeType ) )
		{
			return 1;
		}
		else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_YEAR.equals( timeType ) )
		{
			return 1;
		}
		else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH.equals( timeType ) )
		{
			return 1;
		}
		else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_QUARTER.equals( timeType ) )
		{
			return 1;
		}
		else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR.equals( timeType ) )
		{
			return 1;
		}
		else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_HOUR.equals( timeType ) )
		{
			return 0;
		}
		else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MINUTE.equals( timeType ) )
		{
			return 0;
		}
		else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_SECOND.equals( timeType ) )
		{
			return 0;
		}
		else
			throw new AdapterException( "Error" );
	}
	
	/**
	 * 
	 * @param query
	 * @param resultMetaList
	 * @param levelNameColumnNamePair
	 * @param hierHandle
	 * @throws AdapterException
	 */
	private void prepareLevels( QueryDefinition query,
			TabularHierarchyHandle hierHandle, List metaList, String dimName )
			throws AdapterException
	{
		try
		{
			// Use same data set as cube fact table
			List levels = hierHandle.getContents( TabularHierarchyHandle.LEVELS_PROP );

			for ( int j = 0; j < levels.size( ); j++ )
			{

				TabularLevelHandle level = (TabularLevelHandle) levels.get( j );

				ColumnMeta temp = null;

				String exprString = ExpressionUtil.createJSDataSetRowExpression( level.getColumnName( ) );

				int type = DataAdapterUtil.adaptModelDataType( level.getDataType( ) );
				if ( type == DataType.UNKNOWN_TYPE || type == DataType.ANY_TYPE )
					type = DataType.STRING_TYPE;
				if ( level.getDateTimeLevelType( ) != null )
				{
					temp = new ColumnMeta( createLevelName( dimName, level.getName( )),
							new DataProcessorWrapper( GroupCalculatorFactory.getGroupCalculator( IGroupDefinition.NUMERIC_INTERVAL,
									DataType.INTEGER_TYPE,
									String.valueOf( getDefaultStartValue( level.getDateTimeLevelType( ),
											level.getIntervalBase( ) ) ),
									level.getIntervalRange( ) ) ),
							ColumnMeta.LEVEL_KEY_TYPE );
					temp.setDataType( DataType.INTEGER_TYPE );
					exprString = this.createDateTransformerExpr( level.getDateTimeLevelType( ), exprString );
				}
				else
				{
					IDataProcessor processor = null;
					if ( DesignChoiceConstants.LEVEL_TYPE_DYNAMIC.equals( level.getLevelType( ) ) )
					{
						int interval = GroupAdapter.intervalFromModel( level.getInterval( ) );
						if ( interval != IGroupDefinition.NO_INTERVAL )
							processor = new DataProcessorWrapper( GroupCalculatorFactory.getGroupCalculator( interval,
									type,
									level.getIntervalBase( ),
									level.getIntervalRange( ) ) );
					}
					else if ( DesignChoiceConstants.LEVEL_TYPE_MIRRORED.equals( level.getLevelType( ) ) )
					{
						Iterator it = level.staticValuesIterator( );
						List dispExpr = new ArrayList( );
						List filterExpr = new ArrayList( );
						while ( it.hasNext( ) )
						{
							RuleHandle o = (RuleHandle) it.next( );
							dispExpr.add( o.getDisplayExpression( ) );
							filterExpr.add( o.getRuleExpression( ) );

						}

						// When use mirrored level type, we would change the
						exprString = "";
						for ( int i = 0; i < dispExpr.size( ); i++ )
						{
							String disp = "\""
									+ JavascriptEvalUtil.transformToJsConstants( String.valueOf( dispExpr.get( i ) ) )
									+ "\"";
							String filter = String.valueOf( filterExpr.get( i ) );
							exprString += "if(" + filter + ")" + disp + ";";
						}
					}
					temp = new ColumnMeta( createLevelName( dimName, level.getName( )),
							processor,
							ColumnMeta.LEVEL_KEY_TYPE );
					temp.setDataType( type );
				}
				
				metaList.add( temp );
				Iterator it = level.attributesIterator( );
				while ( it.hasNext( ) )
				{
					LevelAttributeHandle levelAttr = (LevelAttributeHandle) it.next( );
					
					IDataProcessor processor = null;
					String bindingExpr = null;
					if( level.getDateTimeLevelType( ) != null && DATE_TIME_ATTR_NAME.equals( levelAttr.getName()))
					{
						processor = new DateTimeAttributeProcessor( level.getDateTimeLevelType());
						bindingExpr = ExpressionUtil.createJSDataSetRowExpression( level.getColumnName() ) ;
					}else
					{
						bindingExpr = ExpressionUtil.createJSDataSetRowExpression( levelAttr.getName() ) ;
					}
					ColumnMeta meta = new ColumnMeta( createLevelName( dimName, OlapExpressionUtil.getAttributeColumnName( level.getName( ),
							levelAttr.getName( ) )),
							processor,
							ColumnMeta.UNKNOWN_TYPE );

					meta.setDataType( DataAdapterUtil.adaptModelDataType( levelAttr.getDataType( ) ) );
					metaList.add( meta );

					query.addBinding( new Binding( meta.getName( ),
							new ScriptExpression( bindingExpr ) ));
				}
				
				if( level.getDisplayColumnName( )!= null )
				{
					ColumnMeta meta = new ColumnMeta( createLevelName( dimName, OlapExpressionUtil.getDisplayColumnName( level.getName( ) )),
							null,
							ColumnMeta.UNKNOWN_TYPE );
					meta.setDataType( DataType.STRING_TYPE );
					metaList.add( meta );
					query.addBinding( new Binding( meta.getName( ),
							new ScriptExpression( level.getDisplayColumnName( ) ) ) );
				}
				
				String levelName = createLevelName( dimName, level.getName( ));
				query.addBinding( new Binding( levelName ,
						new ScriptExpression( exprString ) ));

				GroupDefinition gd = new GroupDefinition( String.valueOf( query.getGroups( ).size( )));
				gd.setKeyExpression( ExpressionUtil.createJSRowExpression( levelName ) );

				if ( level.getLevelType( ) != null && level.getDateTimeLevelType( ) == null )
				{
					gd.setIntervalRange( level.getIntervalRange( ) );
					gd.setIntervalStart( level.getIntervalBase( ) );
					gd.setInterval( GroupAdapter.intervalFromModel( level.getInterval( ) ) );
				}
				if ( level.getDateTimeLevelType( ) != null )
				{
					gd.setIntervalRange( level.getIntervalRange( ) == 0 ? 1
							: level.getIntervalRange( ) );
					gd.setIntervalStart( String.valueOf( getDefaultStartValue( level.getDateTimeLevelType( ),level.getIntervalBase( ))) );
					gd.setInterval( IGroupDefinition.NUMERIC_INTERVAL  );
				}
				query.addGroup( gd );
			}
		}
		catch ( DataException e )
		{
			throw new AdapterException( e.getLocalizedMessage( ) );
		}
	}
	
	private String createLevelName( String dimName, String levelName )
	{
		if( dimName!= null )
			return dimName + "/" + levelName;
		else
			return levelName;
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
		return this.metadata.getFieldIndex( name );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.cube.IDatasetIterator#getFieldType(java.lang.String)
	 */
	public int getFieldType( String name ) throws BirtException
	{
		return this.metadata.getFieldType( name );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.cube.IDatasetIterator#getValue(int)
	 */
	public Object getValue( int fieldIndex ) throws BirtException
	{
		Object value = it.getValue( this.metadata.getFieldName( fieldIndex ) );
		if ( value == null )
		{
			return this.metadata.getNullValueReplacer( fieldIndex );
		} 
		
		return DataTypeUtil.convert( this.metadata.getDataProcessor( fieldIndex )
				.process( value ),
				value.getClass( ) );
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

	private Calendar getCalendar( Object d )
	{
		assert d != null;
		
		Date date;
		try
		{
			date = DataTypeUtil.toDate( d );
			Calendar c = Calendar.getInstance( );
			c.setTime( date );
			return c;
		}
		catch ( BirtException e )
		{
			throw new java.lang.IllegalArgumentException( AdapterResourceHandle.getInstance( )
					.getMessage( ResourceConstants.INVALID_DATETIME_VALUE ) );
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
		private Object[] nullValueReplacer;
		
		
		/**
		 * Constructor.
		 * @param columnMetas
		 */
		ResultMeta( List columnMetas )
		{
			this.columnMetaMap = new HashMap( );
			this.indexMap = new HashMap( );
			this.nullValueReplacer = new Object[columnMetas.size( )];
			for ( int i = 0; i < columnMetas.size( ); i++ )
			{
				ColumnMeta columnMeta = (ColumnMeta) columnMetas.get( i );
				columnMeta.setIndex( i + 1 );
				this.columnMetaMap.put( columnMeta.getName( ), columnMeta );
				this.indexMap.put( new Integer( i + 1 ), columnMeta );
				if ( columnMeta.isLevelKey( ) || columnMeta.isMeasure( ) )
				{
					this.nullValueReplacer[i] = createNullValueReplacer( columnMeta.getType( ));
				}
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
		
		/**
		 * 
		 * @param index
		 * @return
		 */
		public Object getNullValueReplacer( int index )
		{
			return this.nullValueReplacer[index - 1];
		}
		
		public IDataProcessor getDataProcessor( int index )
		{
			 return ( (ColumnMeta) this.indexMap.get( new Integer( index ) ) ).getDataProcessor( );
		}
		
		/**
		 * 
		 * @param fieldType
		 * @return
		 */
		private Object createNullValueReplacer( int fieldType )
		{
			
			switch ( fieldType )
			{
				case DataType.DATE_TYPE :
					return new java.util.Date( nullTime );
				case DataType.SQL_DATE_TYPE :
					return new java.sql.Date( nullTime );
				case DataType.SQL_TIME_TYPE :
					return new Time( nullTime );
				case DataType.BOOLEAN_TYPE :
					return new Boolean( false );
				case DataType.DECIMAL_TYPE :
					return new Double( 0 );
				case DataType.DOUBLE_TYPE :
					return new Double( 0 );
				case DataType.INTEGER_TYPE :
					return new Integer( 0 );
				case DataType.STRING_TYPE :
					return "";
				default :
					return "";
			}
		}
	}

	/**
	 * 
	 *
	 */
	private class ColumnMeta
	{
		//
		static final int LEVEL_KEY_TYPE = 1;
		static final int MEASURE_TYPE = 2;
		static final int UNKNOWN_TYPE = 3;
		
		private String name;
		private int dataType, type;
		private int index;
		private IDataProcessor dataProcessor;
		/**
		 * 
		 * @param name
		 */
		ColumnMeta( String name, IDataProcessor processor, int type )
		{
			this.name = name;
			this.type = type;
			this.dataProcessor = ( processor == null )
					? (IDataProcessor) new DummyDataProcessor( ) : processor;
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
			return this.dataType;
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
			this.dataType = type;
		}
		
		/**
		 * 
		 * @return
		 */
		public boolean isLevelKey( )
		{
			return this.type == LEVEL_KEY_TYPE;
		}
		
		/**
		 * 
		 * @return
		 */
		public boolean isMeasure( )
		{
			return this.type == MEASURE_TYPE;
		}
		
		/**
		 * 
		 * @return
		 */
		public IDataProcessor getDataProcessor()
		{
			return this.dataProcessor;
		}
	}
	private interface IDataProcessor
	{
		public Object process( Object d ) throws AdapterException;
	}
	
	private class DummyDataProcessor implements IDataProcessor
	{
		public Object process( Object d )
		{
			return d;
		}
	}
	
	/**
	 * For all Time level, there is by default an "DateTime" attribute which contains the corresponding
	 * DateTime value of that time level.
	 */
	private class DateTimeAttributeProcessor implements IDataProcessor
	{
		private String timeType;
		
		DateTimeAttributeProcessor( String timeType )
		{
			this.timeType = timeType;
		}
		
		public Object process( Object d ) throws AdapterException
		{
			if( d == null )
				return null;
			
			Calendar cal = getCalendar( d );
			if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_MONTH.equals( timeType ) 
				 || DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_WEEK.equals( timeType )
				 || DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR.equals( timeType )
				 || DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_MONTH.equals( timeType )
				 || DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_YEAR.equals( timeType ))
			{
				cleanTimePortion( cal );
				return cal.getTime();
			}
			else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH.equals( timeType ) )
			{
				//For all month, clear time portion and set DayOfMonth to 1.
				cleanTimePortion( cal );
				cal.set( Calendar.DATE, 1 );
				return cal.getTime();
			}
			else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_QUARTER.equals( timeType ) )
			{
				//For all quarter, clear time portion and set month to first month of 
				//that quarter and set day to first day of that month.
				cleanTimePortion( cal );
				cal.set( Calendar.DATE, 1 );
				int month = cal.get( Calendar.MONTH );
				switch ( month )
				{
					case Calendar.JANUARY :
					case Calendar.FEBRUARY :
					case Calendar.MARCH :
						cal.set(Calendar.MONTH, 0);
						break;
					case Calendar.APRIL :
					case Calendar.MAY :
					case Calendar.JUNE :
						cal.set(Calendar.MONTH, 3);
						break;
					case Calendar.JULY :
					case Calendar.AUGUST :
					case Calendar.SEPTEMBER :
						cal.set(Calendar.MONTH, 6);
						break;
					case Calendar.OCTOBER :
					case Calendar.NOVEMBER :
					case Calendar.DECEMBER :
						cal.set(Calendar.MONTH, 9);
						break;
				}
				return cal.getTime();
			}
			else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR.equals( timeType ) )
			{
				//For year, clear all time portion and set Date to Jan 1st.
				cleanTimePortion( cal );
				cal.set(Calendar.MONTH, 0);
				cal.set(Calendar.DATE, 1);
				return cal.getTime();
			}
			else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_HOUR.equals( timeType ) )
			{
				//For hour, set minute to 0 and second to 1.
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 1);
				cal.set(Calendar.MILLISECOND, 0);
				return cal.getTime();
			}
			else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MINUTE.equals( timeType ) )
			{
				//For minute, set second to 1.
				cal.set(Calendar.SECOND, 1);
				cal.set(Calendar.MILLISECOND, 0);
				return cal.getTime();
			}
			else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_SECOND.equals( timeType ) )
			{
				//For second, set millisecond to 0.
				cal.set(Calendar.MILLISECOND, 0);
				return cal.getTime();
			}
			else
				throw new AdapterException( ResourceConstants.INVALID_DATE_TIME_TYPE, timeType );
	
		}
	}
	
	/**
	 * Clear time portion of a date value
	 * @param d
	 */
	private void cleanTimePortion( Calendar d )
	{
		d.set(Calendar.HOUR_OF_DAY, 0);
		d.set(Calendar.MINUTE, 0);
		d.set(Calendar.SECOND, 1);
		d.set(Calendar.MILLISECOND,0 );
	}
	
	private String createDateTransformerExpr( String timeType, String value )
	{
		return "TempDateTransformer.transform(\""
				+ timeType + "\"," + value
				+ ")";
		
	}
	
	private class TempDateTransformer extends ScriptableObject
	{

		public TempDateTransformer( )
		{
			this.defineProperty( "transform", new Function_Transform( ), 0 );
		}

		/**
		 * 
		 */
		public String getClassName( )
		{
			return "TempDateTransformer";
		}

	}

	private class Function_Transform extends Function_temp
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.data.adapter.impl.DataSetIterator.TimeValueProcessor.Function_temp#getValue(java.lang.Object[])
		 */
		protected Object getValue( Object[] args ) throws BirtException
		{
			assert args.length == 2;
			String timeType = args[0].toString( );
			Object d = args[1];
			if ( args[1] == null )
				return new Integer( 0 );
			if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_MONTH.equals( timeType ) )
			{
				return new Integer( getCalendar( d ).get( Calendar.DAY_OF_MONTH ) );
			}
			else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_WEEK.equals( timeType ) )
			{
				return new Integer( getCalendar( d ).get( Calendar.DAY_OF_WEEK ) );
			}
			else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR.equals( timeType ) )
			{
				return new Integer( getCalendar( d ).get( Calendar.DAY_OF_YEAR ) );
			}
			else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_MONTH.equals( timeType ) )
			{
				return new Integer( getCalendar( d ).get( Calendar.WEEK_OF_MONTH ) );
			}
			else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_YEAR.equals( timeType ) )
			{
				return new Integer( getCalendar( d ).get( Calendar.WEEK_OF_YEAR ) );
			}
			else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH.equals( timeType ) )
			{
				return new Integer( getCalendar( d ).get( Calendar.MONTH ) + 1 );
			}
			else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_QUARTER.equals( timeType ) )
			{
				int month = getCalendar( d ).get( Calendar.MONTH );
				int quarter = -1;
				switch ( month )
				{
					case Calendar.JANUARY :
					case Calendar.FEBRUARY :
					case Calendar.MARCH :
						quarter = 1;
						break;
					case Calendar.APRIL :
					case Calendar.MAY :
					case Calendar.JUNE :
						quarter = 2;
						break;
					case Calendar.JULY :
					case Calendar.AUGUST :
					case Calendar.SEPTEMBER :
						quarter = 3;
						break;
					case Calendar.OCTOBER :
					case Calendar.NOVEMBER :
					case Calendar.DECEMBER :
						quarter = 4;
						break;
					default :
						quarter = -1;
				}
				return new Integer( quarter );
			}
			else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR.equals( timeType ) )
			{
				return new Integer( getCalendar( d ).get( Calendar.YEAR ) );
			}
			else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_HOUR.equals( timeType ) )
			{
				return new Integer( getCalendar( d ).get( Calendar.HOUR_OF_DAY ) );
			}
			else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MINUTE.equals( timeType ) )
			{
				return new Integer( getCalendar( d ).get( Calendar.MINUTE ) );
			}
			else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_SECOND.equals( timeType ) )
			{
				return new Integer( getCalendar( d ).get( Calendar.SECOND ) );
			}
			else
				throw new AdapterException( ResourceConstants.INVALID_DATE_TIME_TYPE, timeType );
		}
	}

	abstract class Function_temp extends BaseFunction implements Function
	{

		public Object call( Context cx, Scriptable scope, Scriptable thisObj,
				java.lang.Object[] args )
		{
			args = convertToJavaObjects( args );

			try
			{
				return getValue( args );
			}
			catch ( BirtException e )
			{
				throw new IllegalArgumentException( "The type of arguement is incorrect." );
			}
		}

		protected abstract Object getValue( Object[] args )
				throws BirtException;

		private Object[] convertToJavaObjects( Object[] args )
		{
			for ( int i = 0; i < args.length; i++ )
			{
				args[i] = JavascriptEvalUtil.convertJavascriptValue( args[i] );
			}
			return args;
		}

	}
	
	private class DataProcessorWrapper implements IDataProcessor
	{
		private ICalculator calculator;
		
		DataProcessorWrapper ( ICalculator calculator )
		{
			this.calculator = calculator;
		}
		
		public Object process( Object d ) throws AdapterException
		{
			try
			{
				return this.calculator.calculate( d );
			}
			catch ( BirtException e )
			{
				throw new AdapterException( e.getLocalizedMessage( ));
			}
		}
		
	}
}
