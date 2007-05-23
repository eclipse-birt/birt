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
	public DataSetIterator( DataRequestSession session,
			TabularHierarchyHandle hierHandle, String timeLevelName, String leafLevelName ) throws BirtException
	{
		QueryDefinition query = new QueryDefinition( );
		query.setUsesDetails( false );
		
		query.setDataSetName( hierHandle.getDataSet( ).getQualifiedName( ) );

		List metaList = new ArrayList();
		this.prepareLevels( query,
				hierHandle, metaList );
		
		popualteFilter( session, hierHandle.filtersIterator( ), query );
		this.it = session.prepare( query ).execute( null ).getResultIterator( );
		
		this.metadata = new ResultMeta( metaList );
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
							metaList );
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
								String hierKey = joinCondition.getHierarchyKey( );
								metaList.add( new ColumnMeta( hierKey,
										true,
										null ) );
								query.addBinding( new Binding( hierKey,
										new ScriptExpression( ExpressionUtil.createJSDataSetRowExpression( cubeKey ) ) ) );

								GroupDefinition gd = new GroupDefinition( String.valueOf( query.getGroups( )
										.size( ) ) );
								gd.setKeyExpression( ExpressionUtil.createJSRowExpression( hierKey ) );
								query.addGroup( gd );
							}
						}
					}
				}
			}
		}

		prepareMeasure( cubeHandle, query, metaList );
		this.popualteFilter( session, cubeHandle.filtersIterator( ), query );
		this.it = session.prepare( query ).execute( null ).getResultIterator( );
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
							false,
							null );
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

	/**
	 * 
	 * @param query
	 * @param resultMetaList
	 * @param levelNameColumnNamePair
	 * @param hierHandle
	 * @throws AdapterException
	 */
	private void prepareLevels( QueryDefinition query,
			TabularHierarchyHandle hierHandle, List metaList )
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
				if ( isTimeType( type ) )
				{
					temp = new ColumnMeta( level.getName( ),
							true,
							new TimeValueProcessor( level.getDateTimeLevelType( ) ) );
					temp.setDataType( DataType.INTEGER_TYPE );
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
					temp = new ColumnMeta( level.getName( ), true, processor );
					temp.setDataType( type );
				}
				metaList.add( temp );
				Iterator it = level.attributesIterator( );
				while ( it.hasNext( ) )
				{
					LevelAttributeHandle levelAttr = (LevelAttributeHandle) it.next( );
					ColumnMeta meta = new ColumnMeta( OlapExpressionUtil.getAttributeColumnName( level.getName( ),
							levelAttr.getName( ) ),
							false,
							null );

					meta.setDataType( DataAdapterUtil.adaptModelDataType( levelAttr.getDataType( ) ) );
					metaList.add( meta );

					query.addBinding( new Binding( meta.getName( ),
							new ScriptExpression( ExpressionUtil.createJSDataSetRowExpression( levelAttr.getName( ) ) ) ) );
				}

				query.addBinding( new Binding(level.getName( ),
						new ScriptExpression( exprString ) ));

				GroupDefinition gd = new GroupDefinition( String.valueOf( query.getGroups( ).size( )));
				gd.setKeyExpression( ExpressionUtil.createJSRowExpression( level.getName( ) ) );

				if ( level.getLevelType( ) != null )
				{
					gd.setIntervalRange( level.getIntervalRange( ) );
					gd.setIntervalStart( level.getIntervalBase( ) );
					gd.setInterval( GroupAdapter.intervalFromModel( level.getInterval( ) ) );
				}
				if ( level.getDateTimeLevelType( ) != null )
				{
					gd.setIntervalRange( 1 );
					gd.setInterval( getTimeInterval( level.getDateTimeLevelType( ))  );
				}
				query.addGroup( gd );
			}
		}
		catch ( DataException e )
		{
			throw new AdapterException( e.getLocalizedMessage( ) );
		}
	}
	
	/**
	 * 
	 * @param timeType
	 * @return
	 * @throws AdapterException
	 */
	private int getTimeInterval( String timeType ) throws AdapterException
	{
		if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_MONTH.equals( timeType )
			 || DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_WEEK.equals( timeType )
			 || DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR.equals( timeType ))
		{
			return IGroupDefinition.DAY_INTERVAL;
		}
		else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_MONTH.equals( timeType )
				|| DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_YEAR.equals( timeType ))
		{
			return IGroupDefinition.WEEK_INTERVAL;
		}
		else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_QUARTER.equals( timeType ))
		{
			return IGroupDefinition.QUARTER_INTERVAL;
		}
		else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR.equals( timeType ))
		{
			return IGroupDefinition.YEAR_INTERVAL;
		}
		else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_HOUR.equals( timeType ))
		{
			return IGroupDefinition.HOUR_INTERVAL;
		}
		else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MINUTE.equals( timeType ))
		{
			return IGroupDefinition.MINUTE_INTERVAL;
		}
		else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_SECOND.equals( timeType ))
		{
			return IGroupDefinition.SECOND_INTERVAL;
		}
		else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH.equals( timeType ))
		{
			return IGroupDefinition.MONTH_INTERVAL;
		}
		else
			throw new AdapterException( ResourceConstants.INVALID_DATE_TIME_TYPE,
					timeType );
	}
	/**
	 * 
	 * @param type
	 * @return
	 */
	private boolean isTimeType( int type )
	{
		if ( type == DataType.DATE_TYPE 
			 || type == DataType.SQL_DATE_TYPE	)
		{
			return true;
		}	
		return false;
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
		
		return  this.metadata.getDataProcessor( fieldIndex ).process( value );
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
				if ( columnMeta.isLevelKey( ))
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
		private String name;
		private int type;
		private int index;
		private boolean isLevelKey;
		private IDataProcessor dataProcessor;
		/**
		 * 
		 * @param name
		 */
		ColumnMeta( String name, boolean isLevelKey, IDataProcessor processor )
		{
			this.name = name;
			this.isLevelKey = isLevelKey;
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
		
		/**
		 * 
		 * @return
		 */
		public boolean isLevelKey( )
		{
			return this.isLevelKey;
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
	
	private class YearProcessor implements IDataProcessor
	{
		public Object process( Object d )
		{
			return new Integer(getCalendar( d ).get( Calendar.YEAR ));
		}
		
	}
	
	private class QuarterProcessor implements IDataProcessor
	{
		public Object process( Object d )
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
			return new Integer(quarter);
		}
	}

	private class MonthProcessor implements IDataProcessor
	{
		public Object process( Object d )
		{
			int month = getCalendar( d ).get( Calendar.MONTH ) + 1;
			return new Integer(month);
		}
		
	}

	private class WeekOfMonthProcessor implements IDataProcessor
	{
		public Object process( Object d )
		{
			return new Integer(getCalendar( d ).get( Calendar.WEEK_OF_MONTH ));
		}
		
	}

	private class WeekOfYearProcessor implements IDataProcessor
	{
		public Object process( Object d )
		{
			return new Integer(getCalendar( d ).get( Calendar.WEEK_OF_YEAR ));
		}
		
	}
	
	
	private class DayOfWeekProcessor implements IDataProcessor
	{
		public Object process( Object d )
		{
			return new Integer(getCalendar( d ).get( Calendar.DAY_OF_WEEK ));
		}
		
	}
	
	private class DayOfMonthProcessor implements IDataProcessor
	{
		public Object process( Object d )
		{
			return new Integer(getCalendar( d ).get( Calendar.DAY_OF_MONTH ));
		}
		
	}
	
	private class DayOfYearProcessor implements IDataProcessor
	{
		public Object process( Object d )
		{
			return new Integer(getCalendar( d ).get( Calendar.DAY_OF_YEAR ));
		}
		
	}
	
	private class HourProcessor implements IDataProcessor
	{
		public Object process( Object d )
		{
			return new Integer(getCalendar( d ).get( Calendar.HOUR_OF_DAY ));
		}
	}
	
	private class MinuteProcessor implements IDataProcessor
	{
		public Object process( Object d )
		{
			return new Integer(getCalendar( d ).get( Calendar.MINUTE ));
		}
	}
	
	private class SecondProcessor implements IDataProcessor
	{
		public Object process( Object d )
		{
			return new Integer(getCalendar( d ).get( Calendar.SECOND ));
		}
	}
	
	private class TimeValueProcessor implements IDataProcessor
	{
		private IDataProcessor processor;
		TimeValueProcessor( String timeType ) throws AdapterException
		{
			if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_MONTH.equals( timeType ))
			{
				this.processor = new DayOfMonthProcessor();
			}
			else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_WEEK.equals( timeType ))
			{
				this.processor = new DayOfWeekProcessor();
			}
			else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR.equals( timeType ))
			{
				this.processor = new DayOfYearProcessor();
			}
			else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_MONTH.equals( timeType ))
			{
				this.processor = new WeekOfMonthProcessor();
			}
			else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_YEAR.equals( timeType ))
			{
				this.processor = new WeekOfYearProcessor();
			}
			else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH.equals( timeType ))
			{
				this.processor = new MonthProcessor();
			}
			else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_QUARTER.equals( timeType ))
			{
				this.processor = new QuarterProcessor();
			}
			else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR.equals( timeType ))
			{
				this.processor = new YearProcessor();
			}
			else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_HOUR.equals( timeType ))
			{
				this.processor = new HourProcessor();
			}
			else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MINUTE.equals( timeType ))
			{
				this.processor = new MinuteProcessor();
			}
			else if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_SECOND.equals( timeType ))
			{
				this.processor = new SecondProcessor();
			}
			else
				throw new AdapterException("Error");
		}
		
		public Object process( Object d ) throws AdapterException
		{
			return this.processor.process( d );
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
	
	private Calendar getCalendar( Object d )
	{
		if ( d == null )
			throw new java.lang.IllegalArgumentException( "date value is null!" );
		
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
			throw new java.lang.IllegalArgumentException( "date value is invalid");
		}
		
		
	}
	
}
