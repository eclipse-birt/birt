
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.executor.cache;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.querydefn.BaseDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.ColumnDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.binding.APITestCase;

import testutil.ConfigText;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;



public class ScopedCacheTest extends APITestCase
{
	private String tableName;
	@Before
    public void scopedCacheSetUp() throws Exception
	{
		tableName = ConfigText.getString( "Api.TestData.TableName" );
	}
	@After
    public void scopedCacheTearDown() throws Exception
	{
		this.dataEngine.clearCache( "12345" );
	}
	
	@Override
	protected DataSourceInfo getDataSourceInfo( )
	{
		//Api.TestData.TableSQL=CREATE TABLE #dte_test_table#(COUNTRY varchar(10), CITY varchar(10), SALE_DATE timestamp, AMOUNT int, ORDERED int, NULL_COLUMN varchar(10))
		return new DataSourceInfo( ConfigText.getString( "Api.TestData.TableName" ),
				ConfigText.getString( "Api.TestData.TableSQL" ),
				ConfigText.getString( "Api.TestData.TestDataFileName" ) );
	}

	//Basic, without display name
	@Test
    public void test1( ) throws Exception
	{
		BaseDataSetDesign design = this.newDataSet( "testCache", "select COUNTRY from "+ tableName );
		design.addResultSetHint( new ColumnDefinition( "COUNTRY" ) );
		
		this.dataEngine.defineDataSet( design );
		QueryDefinition query = this.newReportQuery( design, true );
		this.executeQuery( query, new String[]{"COUNTRY"} );
		
		this.testPrintln( "Cache Complete" );
		
		design = this.newDataSet( "testCache", "select COUNTRY, CITY from "+ tableName );
		design.addResultSetHint( new ColumnDefinition( "COUNTRY" ) );
		design.addResultSetHint( new ColumnDefinition( "CITY" ) );

		this.dataEngine.defineDataSet( design );
		query = this.newReportQuery( design, true );

		this.executeQuery( query, new String[]{"COUNTRY", "CITY"} );
		checkOutputFile( );
	}
	
	//Basic with display name
	@Test
    public void test2( ) throws Exception
	{
		BaseDataSetDesign design = this.newDataSet( "testCache", "select COUNTRY from "+ tableName );
		design.addResultSetHint( new ColumnDefinition( "COUNTRY" ) );
		
		this.dataEngine.defineDataSet( design );
		QueryDefinition query = this.newReportQuery( design, true );
		this.executeQuery( query, new String[]{"COUNTRY"} );
		
		this.testPrintln( "Cache Complete" );
		
		design = this.newDataSet( "testCache", "select COUNTRY, CITY from "+ tableName );
		design.addResultSetHint( new ColumnDefinition( "COUNTRY" ) );
		ColumnDefinition city = new ColumnDefinition( "CITY" );
		city.setDisplayName( "City Display" );
		design.addResultSetHint( city );

		this.dataEngine.defineDataSet( design );
		query = this.newReportQuery( design, true );

		this.executeQuery( query, new String[]{"COUNTRY", "CITY"} );
		checkOutputFile( );
	}
	
	//Basic clear cache
	@Test
    public void test3( ) throws Exception
	{
		BaseDataSetDesign design = this.newDataSet( "testCache", "select COUNTRY from "+ tableName );
		design.addResultSetHint( new ColumnDefinition( "COUNTRY" ) );
		
		this.dataEngine.defineDataSet( design );
		QueryDefinition query = this.newReportQuery( design, true );
		this.executeQuery( query, new String[]{"COUNTRY"} );
		
		this.testPrintln( "Cache Complete" );
		
		design = this.newDataSet( "testCache", "select COUNTRY, CITY from "+ tableName );
		design.addResultSetHint( new ColumnDefinition( "COUNTRY" ) );
		ColumnDefinition city = new ColumnDefinition( "CITY" );
		city.setDisplayName( "City Display" );
		design.addResultSetHint( city );

		this.dataEngine.defineDataSet( design );
		query = this.newReportQuery( design, true );

		this.executeQuery( query, new String[]{"COUNTRY", "CITY"} );
		
		this.testPrintln( "Clear Cache" );
		// clearCache(ID) leads to NPE during executeQuery() later on
		// even though this issue requires further investigation,
		// this method is not currently used in API
//		this.dataEngine.clearCache( "12345" );
		this.dataEngine.clearCache( this.dataSource, design );
		this.executeQuery( query, new String[]{"COUNTRY", "CITY"} );

		checkOutputFile( );
	}
	
	//Test Double
	@Test
    public void test4( ) throws Exception
	{
		BaseDataSetDesign design = this.newDataSet( "testCache", "select COUNTRY from "+ tableName );
		design.addResultSetHint( new ColumnDefinition( "COUNTRY" ) );
		
		this.dataEngine.defineDataSet( design );
		QueryDefinition query = this.newReportQuery( design, true );
		this.executeQuery( query, new String[]{"COUNTRY"} );
		
		this.testPrintln( "Cache Complete" );
		
		design = this.newDataSet( "testCache", "select COUNTRY, AMOUNT from "+ tableName );
		design.addResultSetHint( new ColumnDefinition( "COUNTRY" ) );
		ColumnDefinition amount = new ColumnDefinition( "AMOUNT" );
		
		amount.setDisplayName( "AMOUNT DISPLAY" );
		amount.setDataType( DataType.DOUBLE_TYPE );
		design.addResultSetHint( amount );

		this.dataEngine.defineDataSet( design );
		query = this.newReportQuery( design, true );

		this.executeQuery( query, new String[]{"COUNTRY", "AMOUNT"} );
		
		this.testPrintln( "Clear Cache" );
		// clearCache(ID) leads to NPE during executeQuery() later on
		// even though this issue requires further investigation,
		// this method is not currently used in API
//		this.dataEngine.clearCache( "12345" );
		this.dataEngine.clearCache( this.dataSource, design );
		this.executeQuery( query, new String[]{"COUNTRY", "AMOUNT"} );

		checkOutputFile( );
	}
	
	//Test Double
	@Test
    public void test5( ) throws Exception
	{
		BaseDataSetDesign design = this.newDataSet( "testCache", "select COUNTRY from "+ tableName );
		design.addResultSetHint( new ColumnDefinition( "COUNTRY" ) );
		
		this.dataEngine.defineDataSet( design );
		QueryDefinition query = this.newReportQuery( design, true );
		this.executeQuery( query, new String[]{"COUNTRY"} );
		
		this.testPrintln( "Cache Complete" );
		
		design = this.newDataSet( "testCache", "select COUNTRY, AMOUNT from "+ tableName );
		design.addResultSetHint( new ColumnDefinition( "COUNTRY" ) );
		ColumnDefinition amount = new ColumnDefinition( "AMOUNT" );
		
		amount.setDisplayName( "AMOUNT DISPLAY" );
		amount.setDataType( DataType.DECIMAL_TYPE );
		design.addResultSetHint( amount );

		this.dataEngine.defineDataSet( design );
		query = this.newReportQuery( design, true );

		this.executeQuery( query, new String[]{"COUNTRY", "AMOUNT"} );
		
		this.testPrintln( "Clear Cache" );
		// clearCache(ID) leads to NPE during executeQuery() later on
		// even though this issue requires further investigation,
		// this method is not currently used in API
//		this.dataEngine.clearCache( "12345" );
		this.dataEngine.clearCache( this.dataSource, design );
		this.executeQuery( query, new String[]{"COUNTRY", "AMOUNT"} );

		checkOutputFile( );
	}
	protected Map getAppContext( )
	{
		Map appContext = new HashMap();
		appContext.put( DataEngine.MEMORY_DATA_SET_CACHE, 5 );
		appContext.put( DataEngine.QUERY_EXECUTION_SESSION_ID, "12345" );
		return appContext;
	}
}
