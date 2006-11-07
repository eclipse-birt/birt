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

import org.eclipse.birt.data.engine.api.APITestCase;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.data.engine.expression.AggregateExpression;

import com.ibm.icu.util.Calendar;

import testutil.ConfigText;

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
	
	// When there is exception thrown by the calculator of aggregation,it
	// caused by "expression is invalid", "filter is invalid" etc, but it should
	// not affect latter aggregations caculation.
	public void test5( ) throws Exception
	{
		QueryDefinition query = newReportQuery( );

		GroupDefinition g1 = new GroupDefinition( "G1" );
		g1.setKeyExpression( "row.e0" );
		query.addGroup( g1 );

		ScriptExpression e0 = new ScriptExpression( "dataSetRow.CITY" );
		query.addResultSetExpression("e0", e0 );
		// wrong filter
		ScriptExpression e1 = new ScriptExpression( "Total.runningSum(dataSetRow.PRICE,abc,1)" );
		e1.setGroupName("G1");
		query.addResultSetExpression("e1", e1 );

		// Aggregate: count at city level
		ScriptExpression e2 = new ScriptExpression( "Total.Sum(dataSetRow.PRICE,null,1)" );
		e2.setGroupName("G1");
		query.addResultSetExpression("e2", e2 );

		ScriptExpression e3 = new ScriptExpression( "Total.rank(dataSetRow.PRICE,true)" );
		e3.setGroupName("G1");
		query.addResultSetExpression("e3", e3 );
		
		// wrong expression
		ScriptExpression e4 = new ScriptExpression( "Total.Sum(dataSetRow.PRICEs,null,1)" );
		e4.setGroupName("G1");
		query.addResultSetExpression("e4", e4 );

		ScriptExpression e5 = new ScriptExpression( "Total.runningSum(dataSetRow.PRICE)" );
		e5.setGroupName("G1");
		query.addResultSetExpression("e5", e5 );

		IResultIterator resultIt = executeQuery( query );

		String[] exprs = new String[]{
				"e0", "e1", "e2", "e3", "e4", "e5" 
		};

		outputQueryResult( resultIt, exprs );
		checkOutputFile( );
	}

	// Test equality comparison
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
	
	/*
	 * Please refer to SCR #74988
	 * The value of static property "Total.OVERALL" has not been set up correctly.  
	 */
	public void testSCR74988( ) throws Exception
	{
		this.setUp();
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
}