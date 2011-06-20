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

package org.eclipse.birt.data.engine.api;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.APITestCase.DataSourceInfo;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ColumnDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.ScriptDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;

import testutil.BaseTestCase;

/**
 * Test case for scripted data source/data set
 */

public class ProgressiveViewingTest extends BaseTestCase
{	
	/**
	 * No looking ahead at all.
	 * @throws BirtException
	 */
	public void testProgressiveViewing1() throws BirtException
	{
		DataEngineContext context = DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION, 
				null,null,null );
		context.setTmpdir( this.getTempDir( ) );
		DataEngine dataEngine = DataEngine.newDataEngine( context );
	
		ScriptDataSourceDesign dataSource = new ScriptDataSourceDesign( "ds" );
		dataSource.setOpenScript( "i = 0;" );
		ScriptDataSetDesign dataSet = new ScriptDataSetDesign( "test" );
		dataSet.setDataSource( "ds" );

		dataSet.addResultSetHint( new ColumnDefinition( "column1" ) );

		dataSet.setFetchScript( " i++; if ( i % 11 == 0 ) return false; row.column1 = i;" +
				"return true;" );

		dataEngine.defineDataSource( dataSource );
		dataEngine.defineDataSet( dataSet );
		
		QueryDefinition qd = new QueryDefinition();
	
		qd.addBinding( new Binding( "column1",
				new ScriptExpression( "i",
						DataType.INTEGER_TYPE ) ) );
		qd.setDataSetName( "test" );
		Map appContextMap = new HashMap( );
		IResultIterator ri1 = dataEngine.prepare( qd, appContextMap ).execute( null ).getResultIterator( );
		
		assertFalse(((DataEngineImpl)dataEngine).getSession( ).getDataSetCacheManager( ).doesLoadFromCache( ) );
		//Please note here the progressive viewing feature is invoked.
		int i = 0;
		while ( ri1.next( ) )
		{
			assertEquals( ((Integer)ri1.getValue( "column1" )).intValue( ), ++i  );
		}
		dataEngine.shutdown( );
		
	}
	
	/**
	 * Looking ahead for 1 row.
	 * @throws BirtException
	 */
	public void testProgressiveViewing2() throws BirtException
	{
		DataEngineContext context = DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION, 
				null,null,null );
		context.setTmpdir( this.getTempDir( ) );
		DataEngine dataEngine = DataEngine.newDataEngine( context );
	
		ScriptDataSourceDesign dataSource = new ScriptDataSourceDesign( "ds" );
		dataSource.setOpenScript( "i = 0;" );
		ScriptDataSetDesign dataSet = new ScriptDataSetDesign( "test" );
		dataSet.setDataSource( "ds" );

		dataSet.addResultSetHint( new ColumnDefinition( "column1" ) );

		dataSet.setFetchScript( " i++; if ( i % 11 == 0 ) return false; row.column1 = i;" +
				"return true;" );

		dataEngine.defineDataSource( dataSource );
		dataEngine.defineDataSet( dataSet );
		
		QueryDefinition qd = new QueryDefinition();
		//Use the cache query results setting to ensure 1 row looking ahead
		qd.setCacheQueryResults( true );
		qd.addBinding( new Binding( "column1",
				new ScriptExpression( "i",
						DataType.INTEGER_TYPE ) ) );
		qd.setDataSetName( "test" );
		Map appContextMap = new HashMap( );
		IResultIterator ri1 = dataEngine.prepare( qd, appContextMap ).execute( null ).getResultIterator( );
		
		assertFalse(((DataEngineImpl)dataEngine).getSession( ).getDataSetCacheManager( ).doesLoadFromCache( ) );
		//Please note here the progressive viewing feature is invoked.
		int i = 0;
		while ( ri1.next( ) )
		{
			assertEquals( ((Integer)ri1.getValue( "column1" )).intValue( ), ++i + 1  );
		}
		dataEngine.shutdown( );
		
	}
	
	/**
	 * Looking ahead for all row because exist overall aggregation, and the aggregation value is fetched in the beginning.
	 * @throws BirtException
	 */
	public void testProgressiveViewing3() throws BirtException
	{
		DataEngineContext context = DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION, 
				null,null,null );
		context.setTmpdir( this.getTempDir( ) );
		DataEngine dataEngine = DataEngine.newDataEngine( context );
	
		ScriptDataSourceDesign dataSource = new ScriptDataSourceDesign( "ds" );
		dataSource.setOpenScript( "i = 0;" );
		ScriptDataSetDesign dataSet = new ScriptDataSetDesign( "test" );
		dataSet.setDataSource( "ds" );

		dataSet.addResultSetHint( new ColumnDefinition( "column1" ) );

		dataSet.setFetchScript( " i++; if ( i % 11 == 0 ) return false; row.column1 = i;" +
				"return true;" );

		dataEngine.defineDataSource( dataSource );
		dataEngine.defineDataSet( dataSet );
		
		QueryDefinition qd = new QueryDefinition();
		//Use the cache query results setting to ensure 1 row looking ahead
		qd.setCacheQueryResults( true );
		Binding aggregation = new Binding( "aggr", new ScriptExpression( "row[\"column1\"]"));
		aggregation.setAggrFunction( "count" );
		
		qd.addBinding( new Binding( "column1",
				new ScriptExpression( "i",
						DataType.INTEGER_TYPE ) ) );
		qd.addBinding( aggregation );
		qd.setDataSetName( "test" );
		Map appContextMap = new HashMap( );
		IResultIterator ri1 = dataEngine.prepare( qd, appContextMap ).execute( null ).getResultIterator( );
		
		assertFalse(((DataEngineImpl)dataEngine).getSession( ).getDataSetCacheManager( ).doesLoadFromCache( ) );
		//Please note here the progressive viewing feature is invoked.
		int i = 0;
		while ( ri1.next( ) )
		{
			assertEquals( ((Integer)ri1.getValue( "aggr" )).intValue( ), 10  );
			assertEquals( ((Integer)ri1.getValue( "column1" )).intValue( ), 11  );
		}
		dataEngine.shutdown( );
		
	}
	
	/**
	 * Looking ahead for 1 even there exist overall aggregation, and the aggregation value is fetched in the beginning.
	 * @throws BirtException
	 */
	public void testProgressiveViewing4() throws BirtException
	{
		DataEngineContext context = DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION, 
				null,null,null );
		context.setTmpdir( this.getTempDir( ) );
		DataEngine dataEngine = DataEngine.newDataEngine( context );
	
		ScriptDataSourceDesign dataSource = new ScriptDataSourceDesign( "ds" );
		dataSource.setOpenScript( "i = 0;" );
		ScriptDataSetDesign dataSet = new ScriptDataSetDesign( "test" );
		dataSet.setDataSource( "ds" );

		dataSet.addResultSetHint( new ColumnDefinition( "column1" ) );

		dataSet.setFetchScript( " i++; if ( i % 11 == 0 ) return false; row.column1 = i;" +
				"return true;" );

		dataEngine.defineDataSource( dataSource );
		dataEngine.defineDataSet( dataSet );
		
		QueryDefinition qd = new QueryDefinition();
		//Use the cache query results setting to ensure 1 row looking ahead
		qd.setCacheQueryResults( true );
		Binding aggregation = new Binding( "aggr", new ScriptExpression( "row[\"column1\"]"));
		aggregation.setAggrFunction( "count" );
		
		qd.addBinding( new Binding( "column1",
				new ScriptExpression( "i",
						DataType.INTEGER_TYPE ) ) );
		qd.addBinding( aggregation );
		qd.setDataSetName( "test" );
		Map appContextMap = new HashMap( );
		IResultIterator ri1 = dataEngine.prepare( qd, appContextMap ).execute( null ).getResultIterator( );
		
		assertFalse(((DataEngineImpl)dataEngine).getSession( ).getDataSetCacheManager( ).doesLoadFromCache( ) );
		//Please note here the progressive viewing feature is invoked.
		int i = 0;
		while ( ri1.next( ) )
		{
			assertEquals( ((Integer)ri1.getValue( "column1" )).intValue( ), 11  );
		}
		assertEquals( ((Integer)ri1.getValue( "aggr" )).intValue( ), 10  );
		dataEngine.shutdown( );
		
	}
	
	/**
	 * Looking ahead for 1 even there exist overall aggregation, and the aggregation value is fetched in the beginning.
	 * @throws BirtException
	 */
	public void testProgressiveViewing5() throws BirtException
	{
		DataEngineContext context = DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION, 
				null,null,null );
		context.setTmpdir( this.getTempDir( ) );
		DataEngine dataEngine = DataEngine.newDataEngine( context );
	
		ScriptDataSourceDesign dataSource = new ScriptDataSourceDesign( "ds" );
		dataSource.setOpenScript( "i = 0;" );
		ScriptDataSetDesign dataSet = new ScriptDataSetDesign( "test" );
		dataSet.setDataSource( "ds" );

		dataSet.addResultSetHint( new ColumnDefinition( "column1" ) );

		dataSet.setFetchScript( " i++; if ( i % 11 == 0 ) return false; row.column1 = i;" +
				"return true;" );

		dataEngine.defineDataSource( dataSource );
		dataEngine.defineDataSet( dataSet );
		
		QueryDefinition qd = new QueryDefinition();
		//Use the cache query results setting to ensure 1 row looking ahead
		qd.setCacheQueryResults( true );
		Binding aggregation = new Binding( "aggr", new ScriptExpression( "row[\"column1\"]"));
		aggregation.setAggrFunction( "runningcount" );
		
		qd.addBinding( new Binding( "column1",
				new ScriptExpression( "i",
						DataType.INTEGER_TYPE ) ) );
		qd.addBinding( aggregation );
		qd.setDataSetName( "test" );
		Map appContextMap = new HashMap( );
		IResultIterator ri1 = dataEngine.prepare( qd, appContextMap ).execute( null ).getResultIterator( );
		
		assertFalse(((DataEngineImpl)dataEngine).getSession( ).getDataSetCacheManager( ).doesLoadFromCache( ) );
		//Please note here the progressive viewing feature is invoked.
		int i = 1;
		while ( ri1.next( ) )
		{
			assertEquals( ((Integer)ri1.getValue( "aggr" )).intValue( ), i  );
			i++;
		}
		dataEngine.shutdown( );
	}

}
