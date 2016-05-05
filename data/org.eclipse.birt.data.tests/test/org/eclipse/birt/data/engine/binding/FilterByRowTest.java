/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.binding;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.BaseDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.ColumnDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ComputedColumn;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.ScriptDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpressionUtil;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

import testutil.ConfigText;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

/**
 * Test for FilterByRow
 */
public class FilterByRowTest extends APITestCase
{
	
	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	protected DataSourceInfo getDataSourceInfo( )
	{
		return new DataSourceInfo( ConfigText.getString( "Impl.TestData1.TableName" ),
				ConfigText.getString( "Impl.TestData1.TableSQL" ),
				ConfigText.getString( "Impl.TestData1.TestDataFileName" ) );
	}

	/**
	 * Test FilterByRow#testAccept case 1
	 * @throws Exception
	 */
	@Test
    public void testAccept1( ) throws Exception
	{

		String[] bindingNameFilter = new String[3];
		bindingNameFilter[0] = "FILTER_COL0";
		bindingNameFilter[1] = "FILTER_COL1";
		bindingNameFilter[2] = "FILTER_COL2";
		IBaseExpression[] bindingExprFilter = new IBaseExpression[3];
		bindingExprFilter[0] = new ScriptExpression( "dataSetRow.COL0" );
		bindingExprFilter[1] = new ScriptExpression( "dataSetRow.COL1" );
		bindingExprFilter[2] = new ScriptExpression( "dataSetRow.COL2" );

		FilterDefinition[] filterDefn = new FilterDefinition[]{
			new FilterDefinition( new ScriptExpression( "row.FILTER_COL0 > 0" ) ),
			new FilterDefinition( new ScriptExpression( "row.FILTER_COL1 > 1" ) ),
			new FilterDefinition( new ScriptExpression( "row.FILTER_COL2 > 0" ) )
		};

		IResultIterator resultIterator = getResultIterator( filterDefn,
				bindingNameFilter,
				bindingExprFilter,
				false );

		while ( resultIterator.next( ) )
		{
			Integer value0 = resultIterator.getInteger( getBindingExpressionName( )[0] );
			Integer value1 = resultIterator.getInteger( getBindingExpressionName( )[1] );
			Integer value2 = resultIterator.getInteger( getBindingExpressionName( )[2] );

			assertTrue( value0.intValue( ) > 0 );
			assertTrue( value1.intValue( ) > 1 );
			assertTrue( value2.intValue( ) > 0 );
		}

		resultIterator.close( );
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
    public void testAccept1WithCache( ) throws Exception
	{

		String[] bindingNameFilter = new String[3];
		bindingNameFilter[0] = "FILTER_COL0";
		bindingNameFilter[1] = "FILTER_COL1";
		bindingNameFilter[2] = "FILTER_COL2";
		IBaseExpression[] bindingExprFilter = new IBaseExpression[3];
		bindingExprFilter[0] = new ScriptExpression( "dataSetRow.COL0" );
		bindingExprFilter[1] = new ScriptExpression( "dataSetRow.COL1" );
		bindingExprFilter[2] = new ScriptExpression( "dataSetRow.COL2" );
		
		FilterDefinition[] filterDefn = new FilterDefinition[]{
			new FilterDefinition( new ScriptExpression( "row.FILTER_COL0 > 0" ) ),
			new FilterDefinition( new ScriptExpression( "row.FILTER_COL1 > 1" ) ),
			new FilterDefinition( new ScriptExpression( "row.FILTER_COL2 > 0" ) )
		};

		IResultIterator resultIterator = getResultIterator( filterDefn,
				bindingNameFilter,
				bindingExprFilter,
				true );
		String queryResultID = resultIterator.getQueryResults( ).getID( );
		resultIterator.close();
		resultIterator = getResultIterator( filterDefn,
				bindingNameFilter,
				bindingExprFilter,
				true, queryResultID );
		while ( resultIterator.next( ) )
		{
			Integer value0 = resultIterator.getInteger( getBindingExpressionName( )[0] );
			Integer value1 = resultIterator.getInteger( getBindingExpressionName( )[1] );
			Integer value2 = resultIterator.getInteger( getBindingExpressionName( )[2] );

			assertTrue( value0.intValue( ) > 0 );
			assertTrue( value1.intValue( ) > 1 );
			assertTrue( value2.intValue( ) > 0 );
		}

		
		resultIterator.close( );
	}
	
	/**
	 * Test FilterByRow#testAccept case 2
	 * @throws Exception
	 */
	@Test
    public void testAccept2( ) throws Exception
	{
		FilterDefinition[] filterDefn = new FilterDefinition[]{
			new FilterDefinition( new ScriptExpression( "row.ROW_COL0 + row.ROW_COL1 > row.ROW_COL2" ) )
		};
		
		IResultIterator resultIterator = getResultIterator( filterDefn, null , null, false );
		
		while ( resultIterator.next( ) )
		{
			Integer value0 = resultIterator.getInteger( getBindingExpressionName()[0] );
			Integer value1 = resultIterator.getInteger( getBindingExpressionName()[1] );
			Integer value2 = resultIterator.getInteger( getBindingExpressionName()[2] );
			assertTrue( value0.intValue( ) + value1.intValue( ) > value2.intValue( ) );
		}
		
		resultIterator.close();
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
    public void testAccept2WithCache( ) throws Exception
	{
		FilterDefinition[] filterDefn = new FilterDefinition[]{
			new FilterDefinition( new ScriptExpression( "row.ROW_COL0 + row.ROW_COL1 > row.ROW_COL2" ) )
		};
		
		IResultIterator resultIterator = getResultIterator( filterDefn, null , null, true );
		String queryResultID = resultIterator.getQueryResults( ).getID( );
		resultIterator.close();
		resultIterator = getResultIterator( filterDefn,
				null,
				null,
				true, queryResultID );
		while ( resultIterator.next( ) )
		{
			Integer value0 = resultIterator.getInteger( getBindingExpressionName()[0] );
			Integer value1 = resultIterator.getInteger( getBindingExpressionName()[1] );
			Integer value2 = resultIterator.getInteger( getBindingExpressionName()[2] );
			assertTrue( value0.intValue( ) + value1.intValue( ) > value2.intValue( ) );
		}
		
		resultIterator.close();
	}
	
	/**
	 * Test FilterByRow#testAccept case 3
	 * @throws Exception
	 */
	@Test
    public void testAccept3( ) throws Exception
	{
		FilterDefinition[] filterDefn = new FilterDefinition[]{
			new FilterDefinition( new ScriptExpression( "row.ROW_COL0 * row.ROW_COL1 > row.ROW_COL2" ) )
		};

		IResultIterator resultIterator = getResultIterator( filterDefn,
				null,
				null,
				false);
		while ( resultIterator.next( ) )
		{
			Integer value0 = resultIterator.getInteger( getBindingExpressionName( )[0] );
			Integer value1 = resultIterator.getInteger( getBindingExpressionName( )[1] );
			Integer value2 = resultIterator.getInteger( getBindingExpressionName( )[2] );
			assertTrue( value0.intValue( ) * value1.intValue( ) > value2.intValue( ) );
		}
		
		resultIterator.close();
	}
	
	/**
	 * Test FilterByRow#testAccept case 3
	 * @throws Exception
	 */
	@Test
    public void testAccept3WithCache( ) throws Exception
	{
		FilterDefinition[] filterDefn = new FilterDefinition[]{
			new FilterDefinition( new ScriptExpression( "row.ROW_COL0 * row.ROW_COL1 > row.ROW_COL2" ) )
		};

		IResultIterator resultIterator = getResultIterator( filterDefn,
				null,
				null,
				true);
		String queryResultID = resultIterator.getQueryResults( ).getID( );
		resultIterator.close();
		resultIterator = getResultIterator( filterDefn,
				null,
				null,
				true, queryResultID );	
		while ( resultIterator.next( ) )
		{
			Integer value0 = resultIterator.getInteger( getBindingExpressionName( )[0] );
			Integer value1 = resultIterator.getInteger( getBindingExpressionName( )[1] );
			Integer value2 = resultIterator.getInteger( getBindingExpressionName( )[2] );
			assertTrue( value0.intValue( ) * value1.intValue( ) > value2.intValue( ) );
		}
		
		resultIterator.close();
	}
	
	/**
	 * Test FilterByRow#testAccept case 4
	 * @throws Exception
	 */
	@Test
    public void testAccept4( ) throws Exception
	{
	
		FilterDefinition[] filterDefn = new FilterDefinition[]{
			new FilterDefinition( new ConditionalExpression( "row.ROW_COL0 * row.ROW_COL1",
					ConditionalExpression.OP_GT,
					"row.ROW_COL2" ) )
		};
		
		IResultIterator resultIterator = getResultIterator( filterDefn, null, null, false );
		
		while ( resultIterator.next( ) )
		{
			Integer value0 = resultIterator.getInteger( getBindingExpressionName()[0] );
			Integer value1 = resultIterator.getInteger( getBindingExpressionName()[1] );
			Integer value2 = resultIterator.getInteger( getBindingExpressionName()[2] );
			assertTrue( value0.intValue( ) * value1.intValue( ) > value2.intValue( ) );
		}
		
		resultIterator.close();
	}
	
	/**
	 * Test FilterByRow#testAccept case 4
	 * @throws Exception
	 */
	@Test
    public void testAccept4WithCache( ) throws Exception
	{
	
		FilterDefinition[] filterDefn = new FilterDefinition[]{
			new FilterDefinition( new ConditionalExpression( "row.ROW_COL0 * row.ROW_COL1",
					ConditionalExpression.OP_GT,
					"row.ROW_COL2" ) )
		};
		IResultIterator resultIterator = getResultIterator( filterDefn, null, null, true );
		String queryResultID = resultIterator.getQueryResults( ).getID( );
		resultIterator.close();
		resultIterator = getResultIterator( filterDefn,
				null,
				null,
				true, queryResultID );
		while ( resultIterator.next( ) )
		{
			Integer value0 = resultIterator.getInteger( getBindingExpressionName()[0] );
			Integer value1 = resultIterator.getInteger( getBindingExpressionName()[1] );
			Integer value2 = resultIterator.getInteger( getBindingExpressionName()[2] );
			assertTrue( value0.intValue( ) * value1.intValue( ) > value2.intValue( ) );
		}
		
		resultIterator.close();
	}
	
	/**
	 * Test FilterByRow#test JS filter case 
	 * 
	 * @throws Exception
	 */
	@Test
    public void testJSFilter( ) throws Exception
	{
		String[] bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_COL0";
		bindingNameRow[1] = "ROW_COL1";
		bindingNameRow[2] = "ROW_COL2";
		bindingNameRow[3] = "ROW_COL3";
		ScriptExpression[] bindingExprRow = new ScriptExpression[]{
				new ScriptExpression( "dataSetRow.COL0" ),
				new ScriptExpression( "dataSetRow.COL1" ),
				new ScriptExpression( "dataSetRow.COL2" ),
				new ScriptExpression( "dataSetRow.COL3" ),
		};
			
		FilterDefinition[] filterDefn = new FilterDefinition[]{
				new FilterDefinition( new ConditionalExpression( "Math.log( row[\"ROW_COL0\"])",
						ConditionalExpression.OP_GE,
						"Math.log(1)" ) ),
				new FilterDefinition( new ConditionalExpression( " row[\"ROW_COL0\"].toString() ",
						ConditionalExpression.OP_EQ,
						"2" ) )
		};
				
		QueryDefinition queryDefn1 = this.createQuery( null, null, null, null, null, null, null, null, filterDefn, bindingNameRow, bindingExprRow );

		IResultIterator resultIt = executeQuery( queryDefn1 );

		outputQueryResult( resultIt, bindingNameRow );
		// assert
		checkOutputFile( );
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
    public void testJSFilterWithCache( ) throws Exception
	{
		String[] bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_COL0";
		bindingNameRow[1] = "ROW_COL1";
		bindingNameRow[2] = "ROW_COL2";
		bindingNameRow[3] = "ROW_COL3";
		ScriptExpression[] bindingExprRow = new ScriptExpression[]{
				new ScriptExpression( "dataSetRow.COL0" ),
				new ScriptExpression( "dataSetRow.COL1" ),
				new ScriptExpression( "dataSetRow.COL2" ),
				new ScriptExpression( "dataSetRow.COL3" ),
		};
			
		FilterDefinition[] filterDefn = new FilterDefinition[]{
				new FilterDefinition( new ConditionalExpression( "Math.log( row[\"ROW_COL0\"])",
						ConditionalExpression.OP_GE,
						"Math.log(1)" ) ),
				new FilterDefinition( new ConditionalExpression( " row[\"ROW_COL0\"].toString() ",
						ConditionalExpression.OP_EQ,
						"2" ) )
		};
				
		QueryDefinition queryDefn1 = this.createQuery( null, null, null, null, null, null, null, null, filterDefn, bindingNameRow, bindingExprRow );
		queryDefn1.setCacheQueryResults( true );
		IResultIterator resultIt = executeQuery( queryDefn1 );
		String queryResultID = resultIt.getQueryResults( ).getID( );
		resultIt.close();
		resultIt = getResultIterator( filterDefn,
				null,
				null,
				true, queryResultID );outputQueryResult( resultIt, bindingNameRow );
		// assert
		checkOutputFile( );
	}
	
    /**
	 * Test the Query when there is boolean filter existing
	 * 
	 * @throws Exception
	 */
	@Test
    public void testBooleanFilterOnDataRows( ) throws Exception
	{

		String[] ccName = new String[]{
				"ccc", "ddd"
		};
		String[] ccExpr = new String[]{
				"true", "false"
		};
		for ( int i = 0; i < ccName.length; i++ )
		{
			ComputedColumn computedColumn = new ComputedColumn( ccName[i],
					ccExpr[i],
					DataType.BOOLEAN_TYPE );
			( (BaseDataSetDesign) this.dataSet ).addComputedColumn( computedColumn );
		}

		FilterDefinition[] filterDefn = new FilterDefinition[]{
			new FilterDefinition( new ConditionalExpression( "row.ROW_ccc",
					ConditionalExpression.OP_EQ,
					"true" ) )
		};

		String[] bindingNameRow = new String[2];
		bindingNameRow[0] = "ROW_ccc";
		bindingNameRow[1] = "ROW_ddd";

		ScriptExpression[] bindingExprRow = new ScriptExpression[]{
				new ScriptExpression( "dataSetRow." + ccName[0], 0 ),
				new ScriptExpression( "dataSetRow." + ccName[1], 0 ),
		};

		IResultIterator resultIt = this.executeQuery( this.createQuery( null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				filterDefn,
				bindingNameRow,
				bindingExprRow ) );

		outputQueryResult( resultIt, bindingNameRow );
		// assert
		checkOutputFile( );
	}
	
	/**
	 * test top N filter and common filter on query, the filter's order is
	 * independent on the result set.
	 * 
	 * @throws Exception
	 */
	@Test
    public void testTopNFilter( ) throws Exception
	{
		String[] bindingNameRow = new String[2];
		bindingNameRow[0] = "ROW_COL0";
		bindingNameRow[1] = "ROW_COL1";

		ScriptExpression[] bindingExprRow = new ScriptExpression[]{
				new ScriptExpression( "dataSetRow.COL0", 0 ),
				new ScriptExpression( "dataSetRow.COL1", 0 ),
		};

		FilterDefinition[] filterDefn1 = new FilterDefinition[]{
				new FilterDefinition( new ConditionalExpression( "row.ROW_COL0",
						ConditionalExpression.OP_TOP_N,
						"10" ) ),
				new FilterDefinition( new ConditionalExpression( "row.ROW_COL1",
						ConditionalExpression.OP_GE,
						"0" ) )
		};

		IResultIterator resultIt1 = executeQuery( createQuery( null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				filterDefn1,
				bindingNameRow,
				bindingExprRow ) );
		outputQueryResult( resultIt1, bindingNameRow );
		

		FilterDefinition[] filterDefn2 = new FilterDefinition[]{
				new FilterDefinition( new ConditionalExpression( "row.ROW_COL1",
						ConditionalExpression.OP_GE,
						"0" ) ),
				new FilterDefinition( new ConditionalExpression( "row.ROW_COL0",
						ConditionalExpression.OP_TOP_N,
						"10" ) )
		};

		IResultIterator resultIt2 = executeQuery( createQuery( null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				filterDefn2,
				bindingNameRow,
				bindingExprRow ) );
		outputQueryResult( resultIt2, bindingNameRow );
		checkQueryResult( resultIt1, resultIt2, bindingNameRow );
	}
	
	/**
	 * test invalid filter definition and it's error code
	 * 
	 * @throws Exception
	 */
	@Test
    public void testInvalidFilterDefinition( ) throws Exception
	{
		String[] bindingNameRow = new String[2];
		bindingNameRow[0] = "ROW_COL0";
		bindingNameRow[1] = "ROW_COL1";

		ScriptExpression[] bindingExprRow = new ScriptExpression[]{
				new ScriptExpression( "dataSetRow.COL0", 0 ),
				new ScriptExpression( "dataSetRow.COL1", 0 ),
		};

		FilterDefinition[] filterDefn1 = new FilterDefinition[]{
			new FilterDefinition( new ConditionalExpression( "row.ROW_COL0",
					ConditionalExpression.OP_EQ,
					"abc" ) )
		};

		try
		{
			executeQuery( createQuery( null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					filterDefn1,
					bindingNameRow,
					bindingExprRow ) );

			fail( "exception expected" );
		}
		catch ( Exception e )
		{
			
		}
	}
	
	/**
	 * test invalid filter definition and it's error code
	 * 
	 * @throws Exception
	 */
	@Test
    public void testInvalidFilterDefinition2( ) throws Exception
	{
		String[] bindingNameRow = new String[2];
		bindingNameRow[0] = "ROW_COL0";
		bindingNameRow[1] = "ROW_COL1";

		ScriptExpression[] bindingExprRow = new ScriptExpression[]{
				new ScriptExpression( "dataSetRow.COL0", 0 ),
				new ScriptExpression( "dataSetRow.COL1", 0 ),
		};

		FilterDefinition[] filterDefn1 = new FilterDefinition[]{
			new FilterDefinition( new ConditionalExpression( "Total.sum(row.ROW_COL0)",
					ConditionalExpression.OP_EQ,
					"100" ) )
		};

		try
		{
			executeQuery( createQuery( null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					filterDefn1,
					bindingNameRow,
					bindingExprRow ) );

			fail( "exception expected" );
		}
		catch ( DataException e )
		{
			assertEquals( ResourceConstants.INVALID_DEFINITION_IN_FILTER,
					e.getErrorCode( ) );
		}
	}
	
	/**
	 * compare the two result sets
	 * @param resultIt1
	 * @param resultIt2
	 * @param expressions
	 * @throws Exception
	 */
	protected void checkQueryResult( IResultIterator resultIt1, IResultIterator resultIt2,
			String[] expressions ) throws Exception
	{
		while ( resultIt1.next( ) && resultIt2.next( ) )
		{
			for ( int i = 0; i < expressions.length; i++ )
			{
				assertTrue( evalAsString( expressions[i], resultIt1 ).equals( evalAsString( expressions[i],
						resultIt2 ) ) );
			}
		}
	}
	
	private IResultIterator getResultIterator(
			FilterDefinition[] filterDefn, String[] bindingNameFilter,
			IBaseExpression[] bindingExprFilter, boolean needCache, String queryResultID ) throws Exception
	{
		QueryDefinition queryDefn = (QueryDefinition) getDefaultQueryDefn( this.dataSet.getName( ) );
		queryDefn.setCacheQueryResults(needCache);
		queryDefn.setQueryResultsID( queryResultID );
		if ( filterDefn != null )
		{
			if ( bindingNameFilter != null )
				for ( int i = 0; i < bindingNameFilter.length; i++ )
					queryDefn.addResultSetExpression( bindingNameFilter[i],
							bindingExprFilter[i] );
			for ( int i = 0; i < filterDefn.length; i++ )
				queryDefn.addFilter( filterDefn[i] );
		}
		return executeQuery( queryDefn );
	}

	/**
	 * Execute Query
	 * 
	 * @return IResultIterator
	 * @throws Exception
	 */
	private IResultIterator getResultIterator(
			FilterDefinition[] filterDefn, String[] bindingNameFilter,
			IBaseExpression[] bindingExprFilter, boolean needCache ) throws Exception
	{
		return this.getResultIterator( filterDefn,
				bindingNameFilter,
				bindingExprFilter,
				needCache,
				null );
	}
	
	/*
	 * Please refer to SCR #77518
	 * Filter Expression does not allow "null" value  
	 */
	@Test
    public void testSCR77518() throws BirtException
	{
		// Create script data set and data source
		ScriptDataSourceDesign dsource = new ScriptDataSourceDesign( "JUST as place folder" );
		ScriptDataSetDesign dset = newDataSet( );
		dset.setDataSource( dsource.getName( ) );

		dataEngine.defineDataSource( dsource );
		dataEngine.defineDataSet( dset );
		
		QueryDefinition rqDefn = new QueryDefinition();
		rqDefn.setDataSetName("test");
		
		String bindingNameFilter = "ROW_CH";
		ScriptExpression bindingExprFilter = new ScriptExpression( "dataSetRow.CH",
				0 );
		FilterDefinition filterDefn = new FilterDefinition( new ScriptExpression( "row.ROW_CH == null" ) );

		rqDefn.addResultSetExpression( bindingNameFilter, bindingExprFilter );
		rqDefn.getFilters( ).add( filterDefn );		
		IPreparedQuery pq = dataEngine.prepare( rqDefn );
		IQueryResults qr = pq.execute( jsScope );
		IResultIterator ri = qr.getResultIterator( );

		int rowIndex = 0;
		while ( ri.next( ) )
		{
			rowIndex++;
		}
		
		assertEquals( rowIndex, 2 );
		
		rqDefn.getFilters().clear();
		rqDefn.getFilters().add( new FilterDefinition ( new ScriptExpression("row.ROW_CH != null")));
		
		pq = dataEngine.prepare( rqDefn );
		qr = pq.execute( jsScope );
		ri = qr.getResultIterator( );

		rowIndex = 0;
		while ( ri.next( ) )
		{
			rowIndex++;
		}
		
		assertEquals( rowIndex, 8);
		ri.close();
		qr.close();
	}
	@Test
    public void testConstantWithNull() throws BirtException
	{
		// Create script data set and data source
		ScriptDataSourceDesign dsource = new ScriptDataSourceDesign( "JUST as place folder" );
		ScriptDataSetDesign dset = newDataSet( );
		dset.setDataSource( dsource.getName( ) );

		dataEngine.defineDataSource( dsource );
		dataEngine.defineDataSet( dset );
		
		QueryDefinition rqDefn = new QueryDefinition();
		rqDefn.setDataSetName("test");
		
		String bindingNameFilter = "ROW_CH";
		ScriptExpression bindingExprFilter = new ScriptExpression( "dataSetRow.CH",
				0 );
		ConditionalExpression expr = new ConditionalExpression( ScriptExpressionUtil.createJavaScriptExpression( "row.ROW_CH" ),
				IConditionalExpression.OP_EQ,
				ScriptExpressionUtil.createConstantExpression( null ),
				null );
		FilterDefinition filterDefn = new FilterDefinition( expr );

		rqDefn.addResultSetExpression( bindingNameFilter, bindingExprFilter );
		rqDefn.getFilters( ).add( filterDefn );		
		IPreparedQuery pq = dataEngine.prepare( rqDefn );
		IQueryResults qr = pq.execute( jsScope );
		IResultIterator ri = qr.getResultIterator( );

		int rowIndex = 0;
		while ( ri.next( ) )
		{
			rowIndex++;
		}
		
		assertEquals( rowIndex, 2 );
		
		rqDefn.getFilters().clear();
		
		List expression = new ArrayList();
		expression.add( ScriptExpressionUtil.createConstantExpression( null ) );
		expr = new ConditionalExpression( ScriptExpressionUtil.createJavaScriptExpression( "row.ROW_CH" ),
				IConditionalExpression.OP_IN,
				expression );
		FilterDefinition filterDefn2 = new FilterDefinition( expr );
		rqDefn.getFilters( ).add( filterDefn2 );
		
		pq = dataEngine.prepare( rqDefn );
		qr = pq.execute( jsScope );
		ri = qr.getResultIterator( );

		rowIndex = 0;
		while ( ri.next( ) )
		{
			rowIndex++;
		}

		assertEquals( rowIndex, 2 );
		ri.close( );
		qr.close( );
	}
	
	private ScriptDataSetDesign newDataSet( )
	{
		ScriptDataSetDesign dataSet = new ScriptDataSetDesign( "test" );

		// 1: add scripts
		dataSet.setOpenScript(	"count=11;");
		
		dataSet.setFetchScript( 
			"count--;" +
			"if (count==0) {return false; } else " +
			"{ if(count > 5 && count < 8)row.CH=null;else row.CH = \"HELLO\"; return true; }");
		
		// 2: add column hints
		dataSet.getResultSetHints( ).add( new ColumnDefinition("CH") );
		return dataSet;
	}
	
}
