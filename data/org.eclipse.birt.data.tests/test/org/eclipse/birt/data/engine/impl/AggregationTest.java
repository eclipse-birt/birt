/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.data.engine.impl;

import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.engine.api.APITestCase;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.data.engine.expression.AggregateExpression;

import com.ibm.icu.util.Calendar;

import testutil.ConfigText;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test case for aggregate JSExpression
 */
public class AggregationTest extends APITestCase
{
	
	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	protected DataSourceInfo getDataSourceInfo( )
	{
		return new DataSourceInfo( ConfigText.getString("Impl.TestData2.TableName"),
				ConfigText.getString( "Impl.TestData2.TableSQL" ),
				ConfigText.getString( "Impl.TestData2.TestDataFileName" ) );
	}
	
	// A test case with some mixed aggregate functions at different levels
	@Test
    public void test1( ) throws Exception
	{
		QueryDefinition query = newReportQuery( );
		
		Calendar c = Calendar.getInstance( );
		c.clear( );
		// 3 grouping levels: CITY, STORE, SALE_DATE(by month)
		GroupDefinition g1 = new GroupDefinition( "G1" );
		g1.setKeyExpression( "row.e1" );
		query.addGroup( g1 );

		GroupDefinition g2 = new GroupDefinition( "G2" );
		g2.setKeyExpression( "row.e2" );
		
		query.addGroup( g2 );

		GroupDefinition g3 = new GroupDefinition( "G3" );
		g3.setKeyExpression( "row.e3" );
		g3.setInterval( GroupDefinition.MONTH_INTERVAL );
		g3.setIntervalRange( 1 );
		
		c.set( 2004, 9, 1 );
		g3.setIntervalStart( c.getTime( ) );
		query.addGroup( g3 );
		
		SortDefinition sort = new SortDefinition( );
		sort.setExpression( "row.e3" );
		sort.setSortDirection( ISortDefinition.SORT_ASC );
		query.addSort( sort );

		ScriptExpression e1 = new ScriptExpression( "dataSetRow.CITY" );
		query.addResultSetExpression("e1", e1 );

		ScriptExpression e2 = new ScriptExpression( "dataSetRow.STORE" );
		query.addResultSetExpression("e2", e2 );
		
		ScriptExpression e3 = new ScriptExpression( "dataSetRow.SALE_DATE" );
		query.addResultSetExpression("e3", e3 );

		ScriptExpression e4 = new ScriptExpression( "dataSetRow.SKU" );
		query.addResultSetExpression("e4", e4 );

		ScriptExpression e10 = new ScriptExpression( "dataSetRow.PRICE" );
		query.addResultSetExpression("e10", e10 );

		ScriptExpression e11 = new ScriptExpression( "dataSetRow.QUANTITY" );
		query.addResultSetExpression("e11", e11 );

		// Aggregate: count at city level
		ScriptExpression e5 = new ScriptExpression( "Total.Count( )" );
		e5.setGroupName("G1");
		query.addResultSetExpression("e5", e5 );

		// Aggregate: count at city level but added to Store group
		ScriptExpression e6 = new ScriptExpression( "Total.Count( null, 1 )" );
		e6.setGroupName("G2");
		query.addResultSetExpression("e6", e6 );

		// Aggregate: day total sales
		ScriptExpression e7 = new ScriptExpression( "Total.Sum( dataSetRow.PRICE * dataSetRow.QUANTITY )" );
		e7.setGroupName("G3");
		query.addResultSetExpression("e7", e7 );

		// Aggregate: Percent of grand total
		ScriptExpression e8 = new ScriptExpression( "dataSetRow.PRICE * dataSetRow.QUANTITY / Total.Sum( dataSetRow.PRICE * dataSetRow.QUANTITY )" );
		query.addResultSetExpression("e8", e8 );

		// Aggregate: a moving ave with a filtering condition
		ScriptExpression e9 = new ScriptExpression( "Total.movingAve( dataSetRow.PRICE, 3, dataSetRow.QUANTITY > 1)" );
		query.addResultSetExpression("e9", e9 );

		String[] exprs = new String[]{
				"e1", "e2", "e3", "e4", "e10", "e11", "e5", "e6", "e7", "e8", "e9"
		};

		outputQueryResult( executeQuery( query ), exprs );
		checkOutputFile();
	}

	// This test is obsoleted.
	/*public void test2( ) throws Exception
	{
		QueryDefinition query = newReportQuery( );

		ScriptExpression e0 = new ScriptExpression( "dataSetRow[0]" );
		query.addResultSetExpression("e0", e0 );
		
		ScriptExpression e1 = new ScriptExpression( "Total.NewCount( )" );
		query.addResultSetExpression("e1", e1 );

		// Add a filter to JSExpression
		ScriptExpression e2 = new ScriptExpression( "Total.Count( dataSetRow.VOIDED != 0, \"OVERALL\" )" );
		query.addResultSetExpression("e2", e2 );

		IResultIterator resultIt = executeQuery( query );

		resultIt.next( );
		testPrintln( "row #"
				+ evalAsString( "e0", resultIt ) + ": "
				+ evalAsString( "e1", resultIt ) + "  "
				+ evalAsString( "e2", resultIt ) );
		resultIt.skipToEnd( 0 );
		testPrintln( "row #"
				+ evalAsString( "e0", resultIt ) + ": "
				+ evalAsString( "e1", resultIt ) + "  "
				+ evalAsString( "e2", resultIt ) );

		checkOutputFile();
	}*/

	// Test aggregates on empty result set
	@Test
    public void test3( ) throws Exception
	{
		QueryDefinition query = newReportQuery( );

		// Add a filter to filter out all rows
		query.addFilter( new FilterDefinition( new ScriptExpression( "false" ) ) );

		GroupDefinition g1 = new GroupDefinition( "G1" );
		g1.setKeyExpression( "row.e1" );
		query.addGroup( g1 );

		ScriptExpression e1 = new ScriptExpression( "dataSetRow.CITY" );
		query.addResultSetExpression("e1", e1 );
		
		// Aggregate: count at city level
		ScriptExpression e2 = new ScriptExpression( "Total.Count( )" );
		e2.setGroupName("G1");
		query.addResultSetExpression("e2", e2 );
		
		IResultIterator resultIt = executeQuery( query );
		assertFalse( resultIt.next() );
		// The Total.Count() against empty result set should return 0
		assertEquals( new Integer(0), resultIt.getValue( "e2" ));
	}
	
//	public void testCancel1( ) throws Exception
//	{
//		QueryDefinition query = newReportQuery( );
//		
//		Calendar c = Calendar.getInstance( );
//		c.clear( );
//		// 3 grouping levels: CITY, STORE, SALE_DATE(by month)
//		GroupDefinition g1 = new GroupDefinition( "G1" );
//		g1.setKeyExpression( "row.e1" );
//		query.addGroup( g1 );
//
//		GroupDefinition g2 = new GroupDefinition( "G2" );
//		g2.setKeyExpression( "row.e2" );
//		
//		query.addGroup( g2 );
//
//		GroupDefinition g3 = new GroupDefinition( "G3" );
//		g3.setKeyExpression( "row.e3" );
//		g3.setInterval( GroupDefinition.MONTH_INTERVAL );
//		g3.setIntervalRange( 1 );
//		
//		c.set( 2004, 9, 1 );
//		g3.setIntervalStart( c.getTime( ) );
//		query.addGroup( g3 );
//		
//		SortDefinition sort = new SortDefinition( );
//		sort.setExpression( "row.e3" );
//		sort.setSortDirection( ISortDefinition.SORT_ASC );
//		query.addSort( sort );
//
//		ScriptExpression e1 = new ScriptExpression( "dataSetRow.CITY" );
//		query.addResultSetExpression("e1", e1 );
//
//		ScriptExpression e2 = new ScriptExpression( "dataSetRow.STORE" );
//		query.addResultSetExpression("e2", e2 );
//		
//		ScriptExpression e3 = new ScriptExpression( "dataSetRow.SALE_DATE" );
//		query.addResultSetExpression("e3", e3 );
//
//		ScriptExpression e4 = new ScriptExpression( "dataSetRow.SKU" );
//		query.addResultSetExpression("e4", e4 );
//
//		ScriptExpression e10 = new ScriptExpression( "dataSetRow.PRICE" );
//		query.addResultSetExpression("e10", e10 );
//
//		ScriptExpression e11 = new ScriptExpression( "dataSetRow.QUANTITY" );
//		query.addResultSetExpression("e11", e11 );
//
//		// Aggregate: count at city level
//		ScriptExpression e5 = new ScriptExpression( "Total.Count( )" );
//		e5.setGroupName("G1");
//		query.addResultSetExpression("e5", e5 );
//
//		// Aggregate: count at city level but added to Store group
//		ScriptExpression e6 = new ScriptExpression( "Total.Count( null, 1 )" );
//		e6.setGroupName("G2");
//		query.addResultSetExpression("e6", e6 );
//
//		// Aggregate: day total sales
//		ScriptExpression e7 = new ScriptExpression( "Total.Sum( dataSetRow.PRICE * dataSetRow.QUANTITY )" );
//		e7.setGroupName("G3");
//		query.addResultSetExpression("e7", e7 );
//
//		// Aggregate: Percent of grand total
//		ScriptExpression e8 = new ScriptExpression( "dataSetRow.PRICE * dataSetRow.QUANTITY / Total.Sum( dataSetRow.PRICE * dataSetRow.QUANTITY )" );
//		query.addResultSetExpression("e8", e8 );
//
//		// Aggregate: a moving ave with a filtering condition
//		ScriptExpression e9 = new ScriptExpression( "Total.movingAve( dataSetRow.PRICE, 3, dataSetRow.QUANTITY > 1)" );
//		query.addResultSetExpression("e9", e9 );
//
//		IPreparedQuery preparedQuery = dataEngine.prepare( query, this.getAppContext( ) );
//		IQueryResults queryResults = preparedQuery.execute( null );
//		CancelDataEngineThread cancelThread = new CancelDataEngineThread( queryResults );
//		cancelThread.start( );
//		IResultIterator resultIt = queryResults.getResultIterator( );
//		assertFalse( resultIt.next() );
//		assertEquals( null, resultIt.getValue( "e2" ));
//	}
//	
//	public void testCancel5( ) throws Exception
//	{
//		QueryDefinition query = newReportQuery( );
//
//		GroupDefinition g1 = new GroupDefinition( "G1" );
//		g1.setKeyExpression( "row.e0" );
//		query.addGroup( g1 );
//
//		ScriptExpression e0 = new ScriptExpression( "dataSetRow.CITY" );
//		query.addResultSetExpression("e0", e0 );
//		// wrong filter
//		ScriptExpression e1 = new ScriptExpression( "Total.runningSum(dataSetRow.PRICE,abc,1)" );
//		e1.setGroupName("G1");
//		query.addResultSetExpression("e1", e1 );
//
//		// Aggregate: count at city level
//		ScriptExpression e2 = new ScriptExpression( "Total.Sum(dataSetRow.PRICE,null,1)" );
//		e2.setGroupName("G1");
//		query.addResultSetExpression("e2", e2 );
//
//		ScriptExpression e3 = new ScriptExpression( "Total.rank(dataSetRow.PRICE,true)" );
//		e3.setGroupName("G1");
//		query.addResultSetExpression("e3", e3 );
//		
//		// wrong expression
//		ScriptExpression e4 = new ScriptExpression( "Total.Sum(dataSetRow.PRICE,a,1)" );
//		e4.setGroupName("G1");
//		query.addResultSetExpression("e4", e4 );
//
//		ScriptExpression e5 = new ScriptExpression( "Total.runningSum(dataSetRow.PRICE)" );
//		e5.setGroupName("G1");
//		query.addResultSetExpression("e5", e5 );
//
//		IPreparedQuery preparedQuery = dataEngine.prepare( query, this.getAppContext( ) );
//		IQueryResults queryResults = preparedQuery.execute( null );
//		CancelDataEngineThread cancelThread = new CancelDataEngineThread( queryResults );
//		cancelThread.start( );
//		IResultIterator resultIt = queryResults.getResultIterator( );
//		assertFalse( resultIt.next() );
//		assertEquals( null, resultIt.getValue( "e2" ));
//	}
	
	// This test is obsoleted. We will throw exception when evaluating column binding.
	// When there is exception thrown by the calculator of aggregation,it
	// caused by "expression is invalid", "filter is invalid" etc, but it should
	// not affect latter aggregations caculation.
//	public void test5( ) throws Exception
//	{
//		QueryDefinition query = newReportQuery( );
//
//		GroupDefinition g1 = new GroupDefinition( "G1" );
//		g1.setKeyExpression( "row.e0" );
//		query.addGroup( g1 );
//
//		ScriptExpression e0 = new ScriptExpression( "dataSetRow.CITY" );
//		query.addResultSetExpression("e0", e0 );
//		// wrong filter
//		ScriptExpression e1 = new ScriptExpression( "Total.runningSum(dataSetRow.PRICE,abc,1)" );
//		e1.setGroupName("G1");
//		query.addResultSetExpression("e1", e1 );
//
//		// Aggregate: count at city level
//		ScriptExpression e2 = new ScriptExpression( "Total.Sum(dataSetRow.PRICE,null,1)" );
//		e2.setGroupName("G1");
//		query.addResultSetExpression("e2", e2 );
//
//		ScriptExpression e3 = new ScriptExpression( "Total.rank(dataSetRow.PRICE,true)" );
//		e3.setGroupName("G1");
//		query.addResultSetExpression("e3", e3 );
//		
//		// wrong expression
//		ScriptExpression e4 = new ScriptExpression( "Total.Sum(dataSetRow.PRICE,a,1)" );
//		e4.setGroupName("G1");
//		query.addResultSetExpression("e4", e4 );
//
//		ScriptExpression e5 = new ScriptExpression( "Total.runningSum(dataSetRow.PRICE)" );
//		e5.setGroupName("G1");
//		query.addResultSetExpression("e5", e5 );
//
//		try
//		{
//			IResultIterator resultIt = executeQuery( query );
//
//		}
//		catch ( DataException e )
//		{
//			assertTrue( e.getErrorCode( ) == ResourceConstants.WRAPPED_BIRT_EXCEPTION );
//		}
//		
////		IResultIterator resultIt = executeQuery( query );
////
////		String[] exprs = new String[]{
////				"e0", "e1", "e2", "e3", "e4", "e5" 
////		};
////
////		outputQueryResult( resultIt, exprs );
////		checkOutputFile( );
//	}

	// Test equality comparison
	@Test
    public void test4( ) throws Exception
	{
		QueryDefinition query = newReportQuery( );

		// 3 grouping levels: CITY, STORE, SALE_DATE(by month)
		GroupDefinition g1 = new GroupDefinition( "G1" );
		g1.setKeyExpression( "dataSetRow.CITY" );
		query.addGroup( g1 );

		GroupDefinition g2 = new GroupDefinition( "G2" );
		g2.setKeyExpression( "dataSetRow.STORE" );
		query.addGroup( g2 );

		GroupDefinition g3 = new GroupDefinition( "G3" );
		g3.setKeyExpression( "dataSetRow.SALE_DATE" );
		g3.setInterval( GroupDefinition.MONTH_INTERVAL );
		g3.setIntervalRange( 1 );
		query.addGroup( g3 );

		// Aggregate 1:
		ScriptExpression e1 = new ScriptExpression( "Total.Ave( dataSetRow.PRICE )" );
		e1.setGroupName("G1");
		query.addResultSetExpression("e1", e1 );
		
		// e2: should be same as e1
		ScriptExpression e2 = new ScriptExpression( "Total.Ave( dataSetRow.PRICE, null, \"G1\" )" );
		e2.setGroupName("G2");
		query.addResultSetExpression("e2", e2 );

		// e3: should be same as e1
		ScriptExpression e3 = new ScriptExpression( "Total.Ave( dataSetRow.PRICE, null, -2 )" );
		e3.setGroupName("G3");
		query.addResultSetExpression("e3", e3 );

		// e4: different from e1 since this is AFTER_LAST_ROW
		//After group is deprecated.
		ScriptExpression e4 = new ScriptExpression( "Total.Ave( dataSetRow.PRICE )" );
		//e4.setGroupName("G1");
		query.addResultSetExpression("e4", e4 );

		// e5: different from e1 since this is has a filter
		ScriptExpression e5 = new ScriptExpression( "Total.Ave( dataSetRow.PRICE, dataSetRow.VOIDED == 0 )" );
		e5.setGroupName("G1");
		query.addResultSetExpression("e5", e5 );

		dataEngine.prepare( query );
		AggregateExpression ae1 = (AggregateExpression) e1.getHandle( );
		AggregateExpression ae2 = (AggregateExpression) e2.getHandle( );
		AggregateExpression ae3 = (AggregateExpression) e3.getHandle( );
		AggregateExpression ae4 = (AggregateExpression) e4.getHandle( );
		AggregateExpression ae5 = (AggregateExpression) e5.getHandle( );

		assertTrue( ae1.getRegId( ) == ae2.getRegId( ) );
		assertTrue( ae1.getRegId( ) == ae3.getRegId( ) );
		assertTrue( ae1.getRegId( ) != ae4.getRegId( ) );
		assertTrue( ae1.getRegId( ) != ae5.getRegId( ) );
	}
	
	/**
	 * test sort on aggregation bindings add a aggregation binding, then sort it
	 * 
	 * @throws Exception
	 */
	@Test
    public void test5( ) throws Exception
	{
		QueryDefinition query = newReportQuery( );

		ScriptExpression e1 = new ScriptExpression( "dataSetRow.CITY" );
		query.addResultSetExpression( "e1", e1 );

		ScriptExpression e2 = new ScriptExpression( "dataSetRow.STORE" );
		query.addResultSetExpression( "e2", e2 );

		ScriptExpression e3 = new ScriptExpression( "dataSetRow.SALE_DATE" );
		query.addResultSetExpression( "e3", e3 );

		ScriptExpression e4 = new ScriptExpression( "dataSetRow.SKU" );
		query.addResultSetExpression( "e4", e4 );

		ScriptExpression e10 = new ScriptExpression( "dataSetRow.PRICE" );
		query.addResultSetExpression( "e10", e10 );

		ScriptExpression e11 = new ScriptExpression( "dataSetRow.QUANTITY" );
		query.addResultSetExpression( "e11", e11 );

		// grouping levels: CITY
		GroupDefinition g1 = new GroupDefinition( "G1" );
		g1.setKeyExpression( "dataSetRow.CITY" );
		query.addGroup( g1 );

		IBinding aggr1 = new Binding( "Rank",
				new ScriptExpression( "dataSetRow[\"PRICE\"]" ) );
		aggr1.setAggrFunction( "RANK" );
		aggr1.addAggregateOn( "G1" );
		query.addBinding( aggr1 );

		SortDefinition sort = new SortDefinition( );
		sort.setExpression( "row[\"Rank\"]" );
		sort.setSortDirection( ISortDefinition.SORT_ASC );
		query.addSort( sort );

		String[] exprs = new String[]{
				"e1", "e2", "e3", "e4", "e10", "e11", "Rank"
		};

		outputQueryResult( executeQuery( query ), exprs );
		checkOutputFile( );
	}

	/**
	 * test sort on aggregation bindings add tow aggregation bingdings, then
	 * sort it
	 * 
	 * @throws Exception
	 */
	@Test
    public void test6( ) throws Exception
	{
		QueryDefinition query = newReportQuery( );

		ScriptExpression e1 = new ScriptExpression( "dataSetRow.CITY" );
		query.addResultSetExpression( "e1", e1 );

		ScriptExpression e2 = new ScriptExpression( "dataSetRow.STORE" );
		query.addResultSetExpression( "e2", e2 );

		ScriptExpression e3 = new ScriptExpression( "dataSetRow.SALE_DATE" );
		query.addResultSetExpression( "e3", e3 );

		ScriptExpression e4 = new ScriptExpression( "dataSetRow.SKU" );
		query.addResultSetExpression( "e4", e4 );

		ScriptExpression e10 = new ScriptExpression( "dataSetRow.PRICE" );
		query.addResultSetExpression( "e10", e10 );

		ScriptExpression e11 = new ScriptExpression( "dataSetRow.QUANTITY" );
		query.addResultSetExpression( "e11", e11 );

		// grouping levels: CITY
		GroupDefinition g1 = new GroupDefinition( "G1" );
		g1.setKeyExpression( "dataSetRow.CITY" );
		query.addGroup( g1 );

		// grouping levels: store
		GroupDefinition g2 = new GroupDefinition( "G2" );
		g2.setKeyExpression( "dataSetRow.STORE" );
		query.addGroup( g2 );

		IBinding aggr1 = new Binding( "Rank",
				new ScriptExpression( "dataSetRow[\"PRICE\"]" ) );
		aggr1.setAggrFunction( "RANK" );
		aggr1.addAggregateOn( "G1" );
		query.addBinding( aggr1 );

		SortDefinition sort = new SortDefinition( );
		sort.setExpression( "row[\"Rank\"]" );
		sort.setSortDirection( ISortDefinition.SORT_ASC );
		query.addSort( sort );

		IBinding aggr2 = new Binding( "Runningsum",
				new ScriptExpression( "dataSetRow[\"PRICE\"]" ) );
		aggr2.setAggrFunction( IBuildInAggregation.TOTAL_RUNNINGSUM_FUNC );
		aggr2.addAggregateOn( "G2" );
		query.addBinding( aggr2 );

		sort = new SortDefinition( );
		sort.setExpression( "row[\"Runningsum\"]" );
		sort.setSortDirection( ISortDefinition.SORT_ASC );
		query.addSort( sort );

		String[] exprs = new String[]{
				"e1", "e2", "e3", "e4", "e10", "e11", "Rank", "Runningsum"
		};

		outputQueryResult( executeQuery( query ), exprs );
		checkOutputFile( );
	}

	/**
	 * test sort on aggregation bindings add a binding, which use aggregation
	 * bindings.
	 * 
	 * @throws Exception
	 */
	@Test
    public void test7( ) throws Exception
	{
		QueryDefinition query = newReportQuery( );

		ScriptExpression e1 = new ScriptExpression( "dataSetRow.CITY" );
		query.addResultSetExpression( "e1", e1 );

		ScriptExpression e2 = new ScriptExpression( "dataSetRow.STORE" );
		query.addResultSetExpression( "e2", e2 );

		ScriptExpression e3 = new ScriptExpression( "dataSetRow.SALE_DATE" );
		query.addResultSetExpression( "e3", e3 );

		ScriptExpression e4 = new ScriptExpression( "dataSetRow.SKU" );
		query.addResultSetExpression( "e4", e4 );

		ScriptExpression e10 = new ScriptExpression( "dataSetRow.PRICE" );
		query.addResultSetExpression( "e10", e10 );

		ScriptExpression e11 = new ScriptExpression( "dataSetRow.QUANTITY" );
		query.addResultSetExpression( "e11", e11 );

		// grouping levels: CITY
		GroupDefinition g1 = new GroupDefinition( "G1" );
		g1.setKeyExpression( "dataSetRow.CITY" );
		query.addGroup( g1 );

		// grouping levels: store
		GroupDefinition g2 = new GroupDefinition( "G2" );
		g2.setKeyExpression( "dataSetRow.STORE" );
		query.addGroup( g2 );

		IBinding aggr2 = new Binding( "Runningsum",
				new ScriptExpression( "dataSetRow[\"PRICE\"]" ) );
		aggr2.setAggrFunction( IBuildInAggregation.TOTAL_RUNNINGSUM_FUNC );
		aggr2.addAggregateOn( "G2" );
		query.addBinding( aggr2 );

		IBinding binding1 = new Binding( "sqrtSum",
				new ScriptExpression( "Math.sqrt(row[\"Runningsum\"])" ) );
		query.addBinding( binding1 );

		SortDefinition sort = new SortDefinition( );
		sort.setExpression( "row[\"sqrtSum\"]" );
		sort.setSortDirection( ISortDefinition.SORT_ASC );
		query.addSort( sort );

		String[] exprs = new String[]{
				"e1", "e2", "e3", "e4", "e10", "e11", "sqrtSum"
		};

		outputQueryResult( executeQuery( query ), exprs );
		checkOutputFile( );
	}

	/**
	 * test sort on aggregation bindings add two bindings, one is aggregation,
	 * another is not aggregation.
	 * 
	 * @throws Exception
	 */
	@Test
    public void test8( ) throws Exception
	{
		QueryDefinition query = newReportQuery( );

		ScriptExpression e1 = new ScriptExpression( "dataSetRow.CITY" );
		query.addResultSetExpression( "e1", e1 );

		ScriptExpression e2 = new ScriptExpression( "dataSetRow.STORE" );
		query.addResultSetExpression( "e2", e2 );

		ScriptExpression e3 = new ScriptExpression( "dataSetRow.SALE_DATE" );
		query.addResultSetExpression( "e3", e3 );

		ScriptExpression e4 = new ScriptExpression( "dataSetRow.SKU" );
		query.addResultSetExpression( "e4", e4 );

		ScriptExpression e10 = new ScriptExpression( "dataSetRow.PRICE" );
		query.addResultSetExpression( "e10", e10 );

		ScriptExpression e11 = new ScriptExpression( "dataSetRow.QUANTITY" );
		query.addResultSetExpression( "e11", e11 );

		// grouping levels: CITY
		GroupDefinition g1 = new GroupDefinition( "G1" );
		g1.setKeyExpression( "dataSetRow.CITY" );
		query.addGroup( g1 );

		IBinding aggr1 = new Binding( "Rank",
				new ScriptExpression( "dataSetRow[\"PRICE\"]" ) );
		aggr1.setAggrFunction( "RANK" );
		aggr1.addAggregateOn( "G1" );
		query.addBinding( aggr1 );

		SortDefinition sort = new SortDefinition( );
		sort.setExpression( "row[\"Rank\"]" );
		sort.setSortDirection( ISortDefinition.SORT_ASC );
		query.addSort( sort );

		// grouping levels: store
		GroupDefinition g2 = new GroupDefinition( "G2" );
		g2.setKeyExpression( "dataSetRow.STORE" );
		query.addGroup( g2 );

		IBinding aggr2 = new Binding( "Runningsum",
				new ScriptExpression( "dataSetRow[\"PRICE\"]" ) );
		aggr2.setAggrFunction( IBuildInAggregation.TOTAL_RUNNINGSUM_FUNC );
		aggr2.addAggregateOn( "G2" );
		query.addBinding( aggr2 );

		IBinding binding1 = new Binding( "sqrtSum",
				new ScriptExpression( "Math.sqrt(row[\"Runningsum\"])" ) );
		query.addBinding( binding1 );

		sort = new SortDefinition( );
		sort.setExpression( "row[\"Runningsum\"]" );
		sort.setSortDirection( ISortDefinition.SORT_ASC );
		query.addSort( sort );

		String[] exprs = new String[]{
				"e1", "e2", "e3", "e4", "e10", "e11", "Rank", "sqrtSum"
		};

		outputQueryResult( executeQuery( query ), exprs );
		checkOutputFile( );
	}

	/**
	 * test sort on aggregation bindings add a aggregation binding, add filter
	 * 
	 * @throws Exception
	 */
	@Test
    public void test9( ) throws Exception
	{
		QueryDefinition query = newReportQuery( );

		ScriptExpression e1 = new ScriptExpression( "dataSetRow.CITY" );
		query.addResultSetExpression( "e1", e1 );

		ScriptExpression e2 = new ScriptExpression( "dataSetRow.STORE" );
		query.addResultSetExpression( "e2", e2 );

		ScriptExpression e3 = new ScriptExpression( "dataSetRow.SALE_DATE" );
		query.addResultSetExpression( "e3", e3 );

		ScriptExpression e4 = new ScriptExpression( "dataSetRow.SKU" );
		query.addResultSetExpression( "e4", e4 );

		ScriptExpression e10 = new ScriptExpression( "dataSetRow.PRICE" );
		query.addResultSetExpression( "e10", e10 );

		ScriptExpression e11 = new ScriptExpression( "dataSetRow.QUANTITY" );
		query.addResultSetExpression( "e11", e11 );

		// grouping levels: CITY
		GroupDefinition g1 = new GroupDefinition( "G1" );
		g1.setKeyExpression( "dataSetRow.CITY" );
		query.addGroup( g1 );

		IBinding aggr1 = new Binding( "Rank",
				new ScriptExpression( "dataSetRow[\"PRICE\"]" ) );
		aggr1.setAggrFunction( "RANK" );
		aggr1.addAggregateOn( "G1" );
		query.addBinding( aggr1 );

		SortDefinition sort = new SortDefinition( );
		sort.setExpression( "row[\"Rank\"]" );
		sort.setSortDirection( ISortDefinition.SORT_ASC );
		query.addSort( sort );

		IConditionalExpression filter = new ConditionalExpression( "dataSetRow.CITY",
				IConditionalExpression.OP_EQ,
				"\"LONDON\"" );

		FilterDefinition filterDefn = new FilterDefinition( filter );
		query.addFilter( filterDefn );

		String[] exprs = new String[]{
				"e1", "e2", "e3", "e4", "e10", "e11", "Rank"
		};

		outputQueryResult( executeQuery( query ), exprs );
		checkOutputFile( );
	}

	/**
	 * test sort on aggregation bindings add a computed column, the binding
	 * which use bind with a aggregation. e,g, aggr is a aggregation, then
	 * define a binding bind(aggr), then add a computed column sqrt(aggr),
	 * 
	 * @throws Exception
	 */
	@Test
    public void test10( ) throws Exception
	{
		QueryDefinition query = newReportQuery( );

		ScriptExpression e1 = new ScriptExpression( "dataSetRow.CITY" );
		query.addResultSetExpression( "e1", e1 );

		ScriptExpression e2 = new ScriptExpression( "dataSetRow.STORE" );
		query.addResultSetExpression( "e2", e2 );

		ScriptExpression e3 = new ScriptExpression( "dataSetRow.SALE_DATE" );
		query.addResultSetExpression( "e3", e3 );

		ScriptExpression e4 = new ScriptExpression( "dataSetRow.SKU" );
		query.addResultSetExpression( "e4", e4 );

		ScriptExpression e10 = new ScriptExpression( "dataSetRow.PRICE" );
		query.addResultSetExpression( "e10", e10 );

		ScriptExpression e11 = new ScriptExpression( "dataSetRow.QUANTITY" );
		query.addResultSetExpression( "e11", e11 );

		// grouping levels: CITY
		GroupDefinition g1 = new GroupDefinition( "G1" );
		g1.setKeyExpression( "dataSetRow.CITY" );
		query.addGroup( g1 );

		IBinding aggr2 = new Binding( "Runningsum",
				new ScriptExpression( "dataSetRow[\"PRICE\"]" ) );
		aggr2.setAggrFunction( IBuildInAggregation.TOTAL_RUNNINGSUM_FUNC );
		aggr2.addAggregateOn( "G1" );
		query.addBinding( aggr2 );

		IBinding bind1 = new Binding( "bind1",
				new ScriptExpression( "row[\"Runningsum\"]" ) );
		query.addBinding( bind1 );

		IBinding binding2 = new Binding( "sqrtBind1",
				new ScriptExpression( "Math.sqrt(row[\"bind1\"])" ) );
		query.addBinding( binding2 );

		SortDefinition sort = new SortDefinition( );
		sort.setExpression( "row[\"sqrtBind1\"]" );
		sort.setSortDirection( ISortDefinition.SORT_ASC );
		query.addSort( sort );

		String[] exprs = new String[]{
				"e1", "e2", "e3", "e4", "e10", "e11", "sqrtBind1"
		};

		outputQueryResult( executeQuery( query ), exprs );
		checkOutputFile( );
	}
	
	/*
	 * Please refer to SCR #74988
	 * The value of static property "Total.OVERALL" has not been set up correctly.  
	 */
	@Test
    public void testSCR74988( ) throws Exception
	{
		QueryDefinition query = newReportQuery( );

		ScriptExpression e1 = new ScriptExpression( "dataSetRow.CITY" );
		query.addResultSetExpression("e1", e1 );

		ScriptExpression e2 = new ScriptExpression( "dataSetRow.STORE" );
		query.addResultSetExpression("e2", e2 );

		ScriptExpression e3 = new ScriptExpression( "dataSetRow.SALE_DATE" );
		query.addResultSetExpression("e3", e3 );

		ScriptExpression e4 = new ScriptExpression( "dataSetRow.SKU" );
		query.addResultSetExpression("e4", e4 );

		ScriptExpression e5 = new ScriptExpression( "dataSetRow.PRICE" );
		query.addResultSetExpression("e5", e5 );

		ScriptExpression e6 = new ScriptExpression( "dataSetRow.QUANTITY" );
		query.addResultSetExpression("e6", e6 );
		
		// 3 grouping levels: CITY, STORE, SALE_DATE(by month)
		GroupDefinition g1 = new GroupDefinition( "G1" );
		g1.setKeyExpression( "row.e1" );
		query.addGroup( g1 );

		GroupDefinition g2 = new GroupDefinition( "G2" );
		g2.setKeyExpression( "row.e2" );
		query.addGroup( g2 );

		GroupDefinition g3 = new GroupDefinition( "G3" );
		g3.setKeyExpression( "row.e3" );
		g3.setInterval( GroupDefinition.MONTH_INTERVAL );
		g3.setIntervalRange( 1 );
		query.addGroup( g3 );
		
		ScriptExpression expr0 = new ScriptExpression( "Total.Sum( row.e5 * row.e6 )" );
		ScriptExpression expr1 = new ScriptExpression( "Total.Sum( row.e5 * row.e6 , null, 0)" );
		expr1.setGroupName("G1");
		ScriptExpression expr2 = new ScriptExpression( "Total.Sum( row.e5 * row.e6 , null, Total.OVERALL)" );
		expr2.setGroupName("G2");
		ScriptExpression expr3 = new ScriptExpression( "Total.Sum( row.e5 * row.e6 , null, \"overall\")" );
		expr3.setGroupName("G3");
		query.addResultSetExpression("expr0", expr0);
		query.addResultSetExpression("expr1", expr1);
		query.addResultSetExpression("expr2", expr2);
		query.addResultSetExpression("expr3", expr3);
		
		IResultIterator resultIterator = executeQuery( query );
		while ( resultIterator.next() )
		{	
			String resultValue0 = evalAsString( "expr0", resultIterator );
			String resultValue1 = evalAsString( "expr1", resultIterator );
			String resultValue2 = evalAsString( "expr2", resultIterator );
			String resultValue3 = evalAsString( "expr3", resultIterator );
			assertEquals( resultValue0, resultValue1);
			assertEquals( resultValue0, resultValue2);
			assertEquals( resultValue0, resultValue3);
			assertEquals( resultValue0, resultValue3);
		}
	}
	
	/**
	 * Test sort on aggregation and filtering on a none-sort key column.
	 * 
	 * @throws Exception
	 */
	@Test
    public void testTed49051( ) throws Exception
	{
		QueryDefinition query = newReportQuery( );

		Binding b = new Binding( "b1",
				new ScriptExpression( "dataSetRow[\"CITY\"]" ) );
		query.addBinding( b );

		b = new Binding( "b2", new ScriptExpression( "dataSetRow[\"STORE\"]" ) );
		query.addBinding( b );

		b = new Binding( "b3",
				new ScriptExpression( "dataSetRow[\"SALE_DATE\"]" ) );
		query.addBinding( b );

		b = new Binding( "b4", new ScriptExpression( "dataSetRow[\"SKU\"]" ) );
		query.addBinding( b );

		b = new Binding( "b5", new ScriptExpression( "dataSetRow[\"PRICE\"]" ) );
		query.addBinding( b );

		b = new Binding( "b6",
				new ScriptExpression( "dataSetRow[\"QUANTITY\"]" ) );
		query.addBinding( b );

		b = new Binding( "aggr1", null );
		b.setAggrFunction( "RANK" );
		b.addArgument( new ScriptExpression( "row[\"b5\"]" ) );
		query.addBinding( b );

		SortDefinition sort = new SortDefinition( );
		sort.setExpression( "row[\"aggr1\"]" );
		sort.setSortDirection( ISortDefinition.SORT_ASC );
		query.addSort( sort );

		FilterDefinition filter = new FilterDefinition( new ConditionalExpression( "row[\"b5\"]",
				IConditionalExpression.OP_TOP_N,
				"3" ) );
		query.addFilter( filter );

		String[] cols = new String[]{
				"b1", "b2", "b3", "b4", "b5", "b6", "aggr1"
		};

		outputQueryResult( executeQuery( query ), cols );
		checkOutputFile( );
	}
}

class CancelDataEngineThread extends Thread 
{
	IQueryResults queryResults;
	
	CancelDataEngineThread( IQueryResults queryResults )
	{
		this.queryResults = queryResults;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run( )
	{
		while ( true )
		{
			try
			{
//				Thread.sleep( 100 );
				queryResults.cancel( );
			}
			catch ( Exception e )
			{
				System.out.println( e );
			}
		}
	}
}
