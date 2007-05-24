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
		QueryDefinition query = new QueryDefinition( );
		query.setUsesDetails( false );
		
		query.setDataSetName( hierHandle.getDataSet( ).getQualifiedName( ) );

		List metaList = new ArrayList();
		this.prepareLevels( query,
				hierHandle, metaList );
		
		popualteFilter( session, hierHandle.filtersIterator( ), query );
		executeQuery( session, query );
		
		this.metadata = new ResultMeta( metaList );
	}

	private void executeQuery( DataRequestSessionImpl session, QueryDefinition query )
			throws AdapterException
	{
		try
		{
			
			Scriptable scope = session.getScope( );
			TempDateTransformer tt = new TempDateTransformer();
			ScriptableObject.putProperty( scope, tt.getClassName( ), tt );
			this.it = session.prepare( query ).execute( scope ).getResultIterator( );
		}
		catch ( BirtException e )
		{
			throw new AdapterException( e.getLocalizedMessage( ), e );
		}
	}

	/**
	 * Create DataSetIterator for fact table.
	 * 
	 * @param session
	 * @param cubeHandle
	 * @throws BirtException
	 */
	public DataSetIterator( DataRequestSessionImpl session,
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
		executeQuery( session, query );
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
				if ( level.getDateTimeLevelType( ) != null )
				{
					temp = new ColumnMeta( level.getName( ),
							true,
							new DataProcessorWrapper( GroupCalculatorFactory.getGroupCalculator( IGroupDefinition.NUMERIC_INTERVAL,
									DataType.INTEGER_TYPE,
									String.valueOf( getDefaultStartValue( level.getDateTimeLevelType( ),level.getIntervalBase( ))),
									level.getIntervalRange( )))) ;
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
				
				if( level.getDisplayColumnName( )!= null )
				{
					ColumnMeta meta = new ColumnMeta( OlapExpressionUtil.getDisplayColumnName( level.getName( )),
							false,
							null );
					meta.setDataType( DataType.STRING_TYPE );
					metaList.add( meta );
					query.addBinding( new Binding( meta.getName( ),
							new ScriptExpression( level.getDisplayColumnName( ) ) ) );
				}
				
				query.addBinding( new Binding(level.getName( ),
						new ScriptExpression( exprString ) ));

				GroupDefinition gd = new GroupDefinition( String.valueOf( query.getGroups( ).size( )));
				gd.setKeyExpression( ExpressionUtil.createJSRowExpression( level.getName( ) ) );

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
				throw new AdapterException( "Error" );
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
