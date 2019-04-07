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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.birt.data.engine.api.querydefn.BaseDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ColumnDefinition;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.JoinCondition;
import org.eclipse.birt.data.engine.api.querydefn.JointDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.ScriptDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.DataSetCacheManager;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;

import testutil.ConfigText;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cache feature for oda result set.
 */
public class DataSetCacheTest extends APITestCase
{
	/** check value */
	private List expectedValue;
	
	private String[] rowBeNames;	
	private IBaseExpression[] rowBeArray;
	
	private String[] totalBeNames;
	private IBaseExpression[] totalBeArray;
		
	private DataEngineImpl dataEngine;
	Map appContextMap = new HashMap( );	
	
	/*
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
    public void dataSetCacheSetUp() throws Exception
	{
		
		expectedValue = new ArrayList( );
		appContextMap.put(DataEngine.DATASET_CACHE_OPTION, "true");
		dataEngine = newDataEngine( );
		dataEngine.clearCache( this.dataSource, this.dataSet );
	}
	
	/*
	 * @see junit.framework.TestCase#tearDown()
	 */
	@After
    public void dataSetCacheTearDown() throws Exception
	{
		getDataSetCacheManager( dataEngine ) .resetForTest( );
		dataEngine.shutdown( );
		if( myDataEngine!= null )
			myDataEngine.shutdown( );
	}
	
	/**
	 * 
	 * @param dataEngine
	 * @return
	 */
	private DataSetCacheManager getDataSetCacheManager( DataEngineImpl dataEngine )
	{
		return dataEngine.getSession( ).getDataSetCacheManager( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	protected DataSourceInfo getDataSourceInfo( )
	{
		return new DataSourceInfo( ConfigText.getString( "Api.TestData.TableName" ),
				ConfigText.getString( "Api.TestData.TableSQL" ),
				ConfigText.getString( "Api.TestData.TestDataFileName" ) );
	}
	
	/**
	 * Test feature of whether cache will be used
	 * @throws BirtException
	 */
	@Test
    public void testUseCache( ) throws BirtException
	{
		this.dataSet.setCacheRowCount(4);
		
		DataEngineImpl myDataEngine = newDataEngine( );
		assertFalse( getDataSetCacheManager( myDataEngine ).doesLoadFromCache( ) );
		assertFalse( getDataSetCacheManager( myDataEngine ).doesSaveToCache( ) );
		QueryDefinition qd = newReportQuery( );
		rowBeArray = getRowExpr( );
		totalBeArray = getAggrExpr( );
		prepareExprNameAndQuery( rowBeArray, totalBeArray, qd );
		IQueryResults qr = myDataEngine.prepare( qd, appContextMap ).execute( null );
		qr.getResultIterator( ).next( );
		qr.close( );
	
		
		assertTrue(getDataSetCacheManager( myDataEngine ).doesLoadFromCache( ) );
		assertFalse( getDataSetCacheManager( myDataEngine ).doesSaveToCache( ) );
		myDataEngine.shutdown( );
	}
	
	/**
	 * Test feature of whether cache will be used
	 * @throws BirtException
	 * @throws IOException 
	 */
	@Test
    public void testUseCacheWithMemoryCache( ) throws BirtException, IOException
	{
		Map appContext = new HashMap( );
		appContext.put( DataEngine.MEMORY_DATA_SET_CACHE, 7 );
		
		DataEngineImpl myDataEngine = newDataEngine( );
		assertFalse( getDataSetCacheManager( myDataEngine ).doesLoadFromCache( ) );
		assertFalse( getDataSetCacheManager( myDataEngine ).doesSaveToCache( ) );
		QueryDefinition qd = newReportQuery( );
		rowBeArray = getRowExpr( );
		totalBeArray = getAggrExpr( );
		prepareExprNameAndQuery( rowBeArray, totalBeArray, qd );
		IQueryResults qr = myDataEngine.prepare( qd, appContext ).execute( null );
		
		IResultIterator ri = qr.getResultIterator( );
		while ( ri.next( ) )
		{
			String str = "";
			for ( int i = 0; i < rowBeNames.length; i++ )
			{
				str += ri.getValue( rowBeNames[i] );

				if ( i < rowBeNames.length - 1 )
					str += ", ";
			}
			testPrintln( str );
		}
		ri.close( );
		qr.close( );

		testPrintln( "##reset cache size##" );
		//setting its cache to 1
		appContext.put( DataEngine.MEMORY_DATA_SET_CACHE, 1 );
		newReportQuery( );
		rowBeArray = getRowExpr( );
		totalBeArray = getAggrExpr( );
		prepareExprNameAndQuery( rowBeArray, totalBeArray, qd );
		qr = myDataEngine.prepare( qd, appContext ).execute( null );
		assertTrue(getDataSetCacheManager( myDataEngine ).doesLoadFromCache( ) );
		assertFalse( getDataSetCacheManager( myDataEngine ).doesSaveToCache( ) );

		ri = qr.getResultIterator( );
		while ( ri.next( ) )
		{
			String str = "";
			for ( int i = 0; i < rowBeNames.length; i++ )
			{
				str += ri.getValue( rowBeNames[i] );

				if ( i < rowBeNames.length - 1 )
					str += ", ";
			}
			testPrintln( str );
		}
		
		checkOutputFile( );
		ri.close( );
		qr.close( );
		
		myDataEngine.shutdown( );
	}
	
	/**
	 * Test the feature of clear cache
	 * 
	 * @throws BirtException
	 */
	@Test
    public void testClearCache( ) throws BirtException
	{
		this.dataSet.setCacheRowCount(4);
		DataEngineImpl myDataEngine = newDataEngine( );
		assertFalse( getDataSetCacheManager( myDataEngine ).doesLoadFromCache( ) );
		assertFalse( getDataSetCacheManager( myDataEngine ).doesSaveToCache( ) );
		
		
		QueryDefinition qd = newReportQuery( );
		rowBeArray = getRowExpr( );
		totalBeArray = getAggrExpr( );
		prepareExprNameAndQuery( rowBeArray, totalBeArray, qd );
		IQueryResults qr = myDataEngine.prepare( qd, appContextMap ).execute( null );
		qr.getResultIterator( ).next( );
		qr.close( );
		
		
		assertTrue( getDataSetCacheManager( myDataEngine ).doesLoadFromCache( ) );	
		
		myDataEngine.clearCache( this.dataSource, this.dataSet );
		assertFalse( getDataSetCacheManager( myDataEngine ).doesLoadFromCache( ) );
		myDataEngine.shutdown( );
	}
	
	/**
	 * Test the feature of enable cache
	 * @throws Exception
	 */
	@Test
    public void testEnableCache( ) throws Exception
	{
		DataEngineImpl myDataEngine = newDataEngine( );
		getDataSetCacheManager( myDataEngine ).resetForTest( );
		
		this.dataSet.setCacheRowCount(4);
		
		assertFalse( getDataSetCacheManager( myDataEngine ).doesLoadFromCache( ) );
		assertFalse( getDataSetCacheManager( myDataEngine ).doesSaveToCache( ) );
		
		
		QueryDefinition qd = newReportQuery( );
		rowBeArray = getRowExpr( );
		totalBeArray = getAggrExpr( );
		prepareExprNameAndQuery( rowBeArray, totalBeArray, qd );
		
		IQueryResults qr = myDataEngine.prepare( qd ).execute( null );
		qr.getResultIterator( ).next( );
		qr.close( );
		
		
		assertFalse( getDataSetCacheManager( myDataEngine ).doesLoadFromCache( ) );
		assertFalse( getDataSetCacheManager( myDataEngine ).doesSaveToCache( ) );
		myDataEngine.shutdown( );
	}
	
	/**
	 * Test the feature of always cache.
	 * @throws BirtException
	 */
	@Test
    public void testAlwaysCache( ) throws BirtException
	{
		DataEngineContext context = DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				this.scriptContext,
				null,
				null,
				null );
		context.setCacheOption( DataEngineContext.CACHE_USE_ALWAYS, 4 );
		context.setTmpdir( this.getTempDir( ) );
		PlatformConfig platformConfig = new PlatformConfig();
		platformConfig.setTempDir( this.getTempDir() );
		DataEngineImpl myDataEngine2 = (DataEngineImpl) DataEngine.newDataEngine( platformConfig, context );
		myDataEngine2.defineDataSource( this.dataSource );
		myDataEngine2.defineDataSet( this.dataSet );
		QueryDefinition qd = newReportQuery( );
		IQueryResults qr = myDataEngine2.prepare( qd ).execute( null );

		assertFalse( getDataSetCacheManager( myDataEngine2 ).doesLoadFromCache( ) );

		qr.getResultIterator( );

		assertTrue( getDataSetCacheManager( myDataEngine2 ).doesLoadFromCache( ) );
		myDataEngine2.clearCache( dataSource, dataSet );
		qr.close( );
		myDataEngine2.shutdown( );
	}
	
	/**
	 * Test the feature of always cache.
	 * @throws BirtException
	 */
	@Test
    public void testSerializableJavaObjectCache( ) throws BirtException
	{
		DataEngineContext context = DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				this.scriptContext,
				null,
				null,
				null );
		context.setCacheOption( DataEngineContext.CACHE_USE_ALWAYS, 4 );
		context.setTmpdir( this.getTempDir( ) );
		PlatformConfig platformConfig = new PlatformConfig();
		platformConfig.setTempDir( this.getTempDir() );
		DataEngineImpl myDataEngine2 = (DataEngineImpl) DataEngine.newDataEngine( platformConfig, context );
		myDataEngine2.defineDataSource( this.dataSource );
		myDataEngine2.defineDataSet( this.dataSet );
		QueryDefinition qd = newReportQuery( );
		ScriptExpression se = new ScriptExpression( "new java.lang.StringBuffer(\"ss\")" );
		se.setDataType( DataType.JAVA_OBJECT_TYPE );
		IBinding b = new Binding("serializable", se );
		b.setDataType( DataType.JAVA_OBJECT_TYPE );
		qd.addBinding( b );
		IQueryResults qr = myDataEngine2.prepare( qd ).execute( null );

		assertFalse( getDataSetCacheManager( myDataEngine2 ).doesLoadFromCache( ) );

		qr.getResultIterator( );

		assertTrue( getDataSetCacheManager( myDataEngine2 ).doesLoadFromCache( ) );
		myDataEngine2.clearCache( dataSource, dataSet );
		qr.close( );
		myDataEngine2.shutdown( );
	}
	
	/**
	 * Test the feature of always cache.
	 * @throws BirtException
	 */
	@Test
    public void testUnserializableJavaObjectCache( ) throws BirtException
	{
		DataEngineContext context = DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				this.scriptContext,
				null,
				null,
				null );
		context.setCacheOption( DataEngineContext.CACHE_USE_ALWAYS, 4 );
		context.setTmpdir( this.getTempDir( ) );
		PlatformConfig platformConfig = new PlatformConfig();
		platformConfig.setTempDir( this.getTempDir() );
		DataEngineImpl myDataEngine2 = (DataEngineImpl) DataEngine.newDataEngine( platformConfig, context );
		myDataEngine2.defineDataSource( this.dataSource );
		myDataEngine2.defineDataSet( this.dataSet );
		QueryDefinition qd = newReportQuery( );
		ScriptExpression se = new ScriptExpression( "new java.lang.ThreadGroup(\"ss\")" );
		se.setDataType( DataType.JAVA_OBJECT_TYPE );
		IBinding b = new Binding("unserializable", se );
		b.setDataType( DataType.JAVA_OBJECT_TYPE );
		qd.addBinding( b );
		IQueryResults qr = myDataEngine2.prepare( qd ).execute( null );

		assertFalse( getDataSetCacheManager( myDataEngine2 ).doesLoadFromCache( ) );

		qr.getResultIterator( );

		assertTrue( getDataSetCacheManager( myDataEngine2 ).doesLoadFromCache( ) );
		myDataEngine2.clearCache( dataSource, dataSet );
		qr.close( );
		myDataEngine2.shutdown( );
	}
	
	/**
	 * Test the feature of disable cache.
	 * @throws BirtException
	 */
	@Test
    public void testDisableCache( ) throws BirtException
	{
		DataEngineContext context = DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				this.scriptContext,
				null,
				null,
				null );
		context.setCacheOption( DataEngineContext.CACHE_USE_DISABLE, 4 );
		context.setTmpdir( this.getTempDir( ) );
		PlatformConfig platformConfig = new PlatformConfig();
		platformConfig.setTempDir( this.getTempDir() );
		DataEngineImpl myDataEngine2 = (DataEngineImpl) DataEngine.newDataEngine( platformConfig, context );
		myDataEngine2.defineDataSource( this.dataSource );
		myDataEngine2.defineDataSet( this.dataSet );
		QueryDefinition qd = newReportQuery( );
		IQueryResults qr = myDataEngine2.prepare( qd ).execute( null );

		assertFalse( getDataSetCacheManager( myDataEngine2 ).doesLoadFromCache( ) );

		qr.getResultIterator( );

		assertFalse( getDataSetCacheManager( myDataEngine2 ).doesLoadFromCache( ) );
		
		qr.close( );
		myDataEngine2.shutdown( );
	}
	
	/**
	 * Test the feature of cache. Check the data in between the cache use is corret.
	 *@throws BirtException
	 */
	@Test
    public void testOdaCache( ) throws BirtException
	{
		genCache( );
		useCache( );
	}
	
	/**
	 * @throws BirtException
	 */
	private void genCache( ) throws BirtException
	{
		this.dataSet.setCacheRowCount( 4 );

		DataEngine myDataEngine = newDataEngine( );
		IResultIterator ri = getResultIterator1( myDataEngine );

		while ( ri.next( ) )
		{
			for ( int i = 0; i < rowBeArray.length; i++ )
				expectedValue.add( ri.getValue( rowBeNames[i] ) );

			for ( int i = 0; i < totalBeArray.length; i++ )
				expectedValue.add( ri.getValue( totalBeNames[i] ) );
		}

		ri.close( );
		myDataEngine.shutdown( );
	}
	
	/**
	 * @throws BirtException
	 */
	private void useCache() throws BirtException
	{
		DataEngine myDataEngine = newDataEngine( );
		IResultIterator ri = getResultIterator1( myDataEngine );
		
		checkResult( ri );

		ri.close( );
		myDataEngine.shutdown( );
	}
	
	/**
	 * Test the feature of cache. Check the data in between the cache use is corret.
	 *@throws BirtException
	 */
	@Test
    public void testOdaCache2( ) throws BirtException
	{
		genCache( );
		useCache3( );
	}

	/**
	 * @throws BirtException
	 */
	private void useCache3( ) throws BirtException
	{
		DataEngine myDataEngine = newDataEngine2( );

		QueryDefinition qd = newReportQuery( );
		GroupDefinition groupDefn = new GroupDefinition( );
		groupDefn.setKeyExpression( "row.COUNTRY1" );
		qd.addGroup( groupDefn );

		groupDefn = new GroupDefinition( );
		groupDefn.setKeyColumn( "CITY1" );
		qd.addGroup( groupDefn );

		// prepare
		rowBeArray = getRowExpr( );
		totalBeArray = getAggrExpr( );
		prepareExprNameAndQuery( rowBeArray, totalBeArray, qd );

		// generation
		IQueryResults qr = myDataEngine.prepare( qd, appContextMap )
				.execute( null );
		assertTrue( qr.getResultMetaData( ) != null );
		IResultIterator ri = qr.getResultIterator( );
		
		checkResult( ri );

		ri.close( );
		myDataEngine.shutdown( );
	}
	
	/**
	 * @param myDataEngine
	 * @return
	 * @throws BirtException
	 */
	private IResultIterator getResultIterator1( DataEngine myDataEngine )
			throws BirtException
	{
		QueryDefinition qd = newReportQuery( );
		GroupDefinition groupDefn = new GroupDefinition( );
		groupDefn.setKeyExpression( "row.COUNTRY1" );
		qd.addGroup( groupDefn );

		groupDefn = new GroupDefinition( );
		groupDefn.setKeyColumn( "CITY1" );
		qd.addGroup( groupDefn );

		// prepare
		rowBeArray = getRowExpr( );
		totalBeArray = getAggrExpr( );
		prepareExprNameAndQuery( rowBeArray, totalBeArray, qd );

		// generation
		IQueryResults qr = myDataEngine.prepare( qd, appContextMap )
				.execute( null );
		assertTrue( qr.getResultMetaData( ) != null );

		return qr.getResultIterator( );
	}
	
	//--------data engine for joint data set
	private DataEngineImpl myDataEngine;
	
	/**
	 * @throws Exception
	 */
	@Test
	@Ignore
    public void testJointDataSetCache( ) throws Exception
	{
		QueryDefinition queryDefn = prepareForJointDataSet( false );

		prepareExprNameAndQuery( rowBeArray, totalBeArray, queryDefn );
		IPreparedQuery preparedQuery = myDataEngine.prepare( queryDefn );
		IQueryResults qr = preparedQuery.execute( null );
		IResultIterator ri = qr.getResultIterator( );

		while ( ri.next( ) )
		{
			String s = "";
			for ( int i = 0; i < rowBeArray.length; i++ )
			{
				s += ri.getValue( rowBeNames[i] );
				if ( i != rowBeArray.length - 1 )
					s += ", ";
			}

			testPrintln( s );
		}
		qr.close( );
		myDataEngine.shutdown( );

		assertTrue( getDataSetCacheManager( myDataEngine ).doesLoadFromCache( ) );
		assertFalse( getDataSetCacheManager( myDataEngine ).doesSaveToCache( ) );
		
		this.checkOutputFile( );
	}
	
	/**
	 * @throws Exception 
	 */
	@Test
    public void testJointDataSetCacheWithFilter( ) throws Exception
	{
		QueryDefinition queryDefn = prepareForJointDataSet( false);

		prepareExprNameAndQuery( rowBeArray, totalBeArray, queryDefn );
		IPreparedQuery preparedQuery = myDataEngine.prepare( queryDefn );
		IQueryResults qr = preparedQuery.execute( null );
		IResultIterator ri = qr.getResultIterator( );

		while ( ri.next( ) )
		{
			for ( int i = 0; i < rowBeArray.length; i++ )
				ri.getValue( rowBeNames[i] );
		}

		FilterDefinition filterDefn = new FilterDefinition( new ScriptExpression( "dataSetRow[\""
				+ this.dataSet.getName( ) + "::COUNTRY\"]" + "==" + "\"CHINA\"" ) );
		queryDefn.addFilter( filterDefn );

		preparedQuery = myDataEngine.prepare( queryDefn );
		qr = preparedQuery.execute( null );
		ri = qr.getResultIterator( );

		while ( ri.next( ) )
		{
			String s = "";
			for ( int i = 0; i < rowBeArray.length; i++ )
			{
				s += ri.getValue( rowBeNames[i] );
				if ( i != rowBeArray.length - 1 )
					s += ", ";
			}

			testPrintln( s );
		}		
		myDataEngine.shutdown( );
		
		this.checkOutputFile( );
	}
	
	/**
	 * @throws Exception 
	 */
	@Test
    public void testJointDataSetCacheWithGroup( ) throws Exception
	{
		QueryDefinition queryDefn = prepareForJointDataSet( true );

		prepareExprNameAndQuery( rowBeArray, totalBeArray, queryDefn );
		IPreparedQuery preparedQuery = myDataEngine.prepare( queryDefn );
		IQueryResults qr = preparedQuery.execute( null );
		IResultIterator ri = qr.getResultIterator( );

		while ( ri.next( ) )
		{
			for ( int i = 0; i < rowBeArray.length; i++ )
				ri.getValue( rowBeNames[i] );
		}

		GroupDefinition gd = new GroupDefinition( "G1" );
		gd.setKeyExpression( "row[\""
				+ this.dataSet.getName( ) + "::COUNTRY\"]" );
		queryDefn.addGroup( gd );
		preparedQuery = myDataEngine.prepare( queryDefn );
		qr = preparedQuery.execute( null );
		ri = qr.getResultIterator( );

		while ( ri.next( ) )
		{
			String s = "";
			for ( int i = 0; i < rowBeArray.length; i++ )
			{
				s += ri.getValue( rowBeNames[i] );
				if ( i != rowBeArray.length - 1 )
					s += ", ";
			}

			testPrintln( s );
		}		
		
		gd = new GroupDefinition( "G2" );
		gd.setKeyExpression( "row[\""
				+ this.dataSet.getName( ) + "::CITY\"]" );
		queryDefn.addGroup( gd );
		preparedQuery = myDataEngine.prepare( queryDefn );
		qr = preparedQuery.execute( null );
		ri = qr.getResultIterator( );

		while ( ri.next( ) )
		{
			String s = "";
			for ( int i = 0; i < rowBeArray.length; i++ )
			{
				s += ri.getValue( rowBeNames[i] );
				if ( i != rowBeArray.length - 1 )
					s += ", ";
			}

			testPrintln( s );
		}		
		myDataEngine.shutdown( );
		
		this.checkOutputFile( );
	}
	
	/**
	 * @throws BirtException
	 */
	public QueryDefinition prepareForJointDataSet( boolean autoBinding ) throws BirtException
	{
		BaseDataSetDesign dataSet2 = newDateSet( (OdaDataSetDesign) ( this.dataSet ) );

		DataEngineContext context = DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				this.scriptContext,
				null,
				null,
				null );
		context.setCacheOption( DataEngineContext.CACHE_USE_ALWAYS, 400 );
		context.setTmpdir( this.getTempDir( ) );
		PlatformConfig platformConfig = new PlatformConfig();
		platformConfig.setTempDir( this.getTempDir() );
		myDataEngine = (DataEngineImpl) DataEngine.newDataEngine( platformConfig, context );
		myDataEngine.defineDataSource( this.dataSource );
		myDataEngine.defineDataSet( this.dataSet );
		myDataEngine.defineDataSet( dataSet2 );

		List a = new ArrayList( );
		a.add( new JoinCondition( new ScriptExpression( "dataSetRow.CITY" ),
				new ScriptExpression( "dataSetRow.CITY1" ),
				IJoinCondition.OP_EQ ) );

		JointDataSetDesign dset3 = new JointDataSetDesign( "dset13",
				this.dataSet.getName( ),
				dataSet2.getName( ),
				IJointDataSetDesign.LEFT_OUTER_JOIN,
				a );
		myDataEngine.defineDataSet( dset3 );

		QueryDefinition queryDefn = new QueryDefinition( autoBinding );
		queryDefn.setDataSetName( dset3.getName( ) );
		int num = 4;
		rowBeArray = new IBaseExpression[num];
		rowBeArray[0] = new ScriptExpression( "dataSetRow[\""
				+ this.dataSet.getName( ) + "::COUNTRY\"]" );
		rowBeArray[1] = new ScriptExpression( "dataSetRow[\""
				+ this.dataSet.getName( ) + "::CITY\"]" );
		rowBeArray[2] = new ScriptExpression( "dataSetRow[\""
				+ dataSet2.getName( ) + "::COUNTRY\"]" );
		rowBeArray[3] = new ScriptExpression( "dataSetRow[\""
				+ dataSet2.getName( ) + "::CITY1\"]" );
		rowBeNames = new String[4];
		rowBeNames[0] = "COUNTRY3";
		rowBeNames[1] = "CITY3";
		rowBeNames[2] = "COUNTRY4";
		rowBeNames[3] = "CITY4";
		
		totalBeArray = new IBaseExpression[0];
		
		return queryDefn;
	}
	
	/**
	 * @param BaseDataSetDesign
	 *            sDataSet
	 * @return BaseDataSetDesign
	 */
	protected OdaDataSetDesign newDateSet( OdaDataSetDesign sDataSet )
	{
		OdaDataSetDesign copyDataSet = new OdaDataSetDesign( sDataSet.getName( )
				+ "1", sDataSet.getDataSourceName( ) );
		copyDataSet.setEventHandler( sDataSet.getEventHandler( ) );
		copyDataSet.setExtensionID( sDataSet.getExtensionID( ) );
		// test the cache is closed when joint data set is running
		copyDataSet.setQueryText( "select COUNTRY, CITY as CITY1 "
				+ sDataSet.getQueryText( ).substring( 9 ) );
		return copyDataSet;
	}
	
	/**
	 * @throws BirtException 
	 * 
	 */
	@Test
    public void testScriptedCache( ) throws BirtException
	{
		genCache2( );
		useCache2( );
	}
	
	/**
	 * @throws BirtException
	 */
	private void genCache2( ) throws BirtException
	{
		DataEngineContext context = DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				this.scriptContext,
				null,
				null,
				null );
		context.setCacheOption( DataEngineContext.CACHE_USE_ALWAYS, 4 );
		context.setTmpdir( this.getTempDir( ) );
		PlatformConfig platformConfig = new PlatformConfig();
		platformConfig.setTempDir( this.getTempDir() );
		DataEngine myDataEngine2 = DataEngine.newDataEngine( platformConfig, context );
		IResultIterator ri = getResultIterator2( myDataEngine2 );
		while ( ri.next( ) )
		{
			for ( int i = 0; i < rowBeArray.length; i++ )
				expectedValue.add( ri.getValue( rowBeNames[i] ) );
		}
		ri.close( );
		myDataEngine2.shutdown( );
	}

	/**
	 * @throws BirtException
	 */
	private void useCache2( ) throws BirtException
	{
		DataEngineContext context = DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				this.scriptContext,
				null,
				null,
				null );
		context.setCacheOption( DataEngineContext.CACHE_USE_ALWAYS, 4 );
		context.setTmpdir( this.getTempDir( ) );
		PlatformConfig platformConfig = new PlatformConfig();
		platformConfig.setTempDir( this.getTempDir() );
		DataEngine myDataEngine2 = DataEngine.newDataEngine( platformConfig, context );
		IResultIterator ri = getResultIterator2( myDataEngine2 );
		
		checkResult( ri );

		ri.close( );
		myDataEngine2.shutdown( );
	}
	
	/**
	 * 
	 * @param myDataEngine
	 * @return
	 * @throws BirtException
	 */
	private IResultIterator getResultIterator2( DataEngine myDataEngine2 )
			throws BirtException
	{
		ScriptDataSourceDesign odaDataSource = new ScriptDataSourceDesign( "JUST as place folder" );

		ScriptDataSetDesign odaDataSet = new ScriptDataSetDesign( "ScriptedDataSet" );
		odaDataSet.setDataSource( odaDataSource.getName( ) );
		odaDataSet.setOpenScript( "count=100;" );
		odaDataSet.setFetchScript( "if (count==0) "
				+ "{" + "return false; " + "} " + "else " + "{ "
				+ "row.NUM=count; " + "row.SQUARE=count*count; "
				+ "row.STR=\"row#\" + count; " + "--count; " + "return true; "
				+ "}" );

		// set column defintion for data set
		String[] scriptColumnNames = new String[]{
				"NUM", "SQUARE", "STR"
		};
		int[] scriptColumnTypes = new int[]{
				DataType.INTEGER_TYPE,
				DataType.DOUBLE_TYPE,
				DataType.STRING_TYPE
		};
		for ( int i = 0; i < scriptColumnNames.length; i++ )
		{
			ColumnDefinition colInfo = new ColumnDefinition( scriptColumnNames[i] );
			colInfo.setDataType( scriptColumnTypes[i] );
			odaDataSet.getResultSetHints( ).add( colInfo );
		}

		QueryDefinition queryDefinition = this.newReportQuery( );
		queryDefinition.setDataSetName( odaDataSet.getName( ) );

		rowBeArray = new IBaseExpression[3];
		ScriptExpression expr = new ScriptExpression( "dataSetRow.NUM" );
		rowBeArray[0] = expr;
		expr = new ScriptExpression( "dataSetRow.SQUARE" );
		rowBeArray[1] = expr;
		expr = new ScriptExpression( "dataSetRow.STR" );
		rowBeArray[2] = expr;
		
		rowBeNames = new String[3];
		rowBeNames[0] = "NUM1";
		rowBeNames[1] = "SQUARE1";
		rowBeNames[2] = "STR1";
		
		for ( int i = 0; i < rowBeArray.length; i++ )
			queryDefinition.addResultSetExpression( rowBeNames[i],
					rowBeArray[i] );
		
		myDataEngine2.defineDataSource( odaDataSource );
		myDataEngine2.defineDataSet( odaDataSet );

		IQueryResults qr = myDataEngine2.prepare( queryDefinition )
				.execute( null );
		return qr.getResultIterator( );
	}
	
	/**
	 * @return
	 * @throws BirtException
	 */
	private DataEngineImpl newDataEngine( ) throws BirtException
	{
		DataEngineContext context = DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				this.scriptContext,
				null,
				null,
				null );
		context.setTmpdir( this.getTempDir( ) );
		PlatformConfig platformConfig = new PlatformConfig();
		platformConfig.setTempDir( this.getTempDir() );
		DataEngineImpl myDataEngine = (DataEngineImpl)DataEngine.newDataEngine( platformConfig, context );
		
		OdaDataSetDesign odaDesign = new OdaDataSetDesign( "Test DataSet2" );
		odaDesign.setExtensionID( ( (OdaDataSetDesign) this.dataSet ).getExtensionID( ) );
		odaDesign.setQueryText( ( (OdaDataSetDesign) this.dataSet ).getQueryText( ) );
		
		myDataEngine.defineDataSource( this.dataSource );
		myDataEngine.defineDataSet( this.dataSet );
		
		return myDataEngine;
	}
	
	/**
	 * @return
	 * @throws BirtException
	 */
	private DataEngine newDataEngine2( ) throws BirtException
	{
		DataEngineContext context = DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				this.scriptContext,
				null,
				null,
				null );
		
		context.setTmpdir( this.getTempDir() );
		PlatformConfig platformConfig = new PlatformConfig();
		platformConfig.setTempDir( this.getTempDir() );
		DataEngine myDataEngine = DataEngine.newDataEngine( platformConfig, context );
		
		myDataEngine.defineDataSource( this.dataSource );
		
		OdaDataSetDesign odaDesign = new OdaDataSetDesign( this.dataSet.getName( ) );		
		odaDesign.setExtensionID( ( (OdaDataSetDesign) this.dataSet ).getExtensionID( ) );
		odaDesign.setQueryText( ( (OdaDataSetDesign) this.dataSet ).getQueryText( ) );
		odaDesign.setDataSource( this.dataSource.getName( ) );
		odaDesign.setCacheRowCount( this.dataSet.getCacheRowCount( ) );
		
		myDataEngine.defineDataSet( odaDesign );
		
		return myDataEngine;
	}
	
	/**
	 * @return
	 */
	private IBaseExpression[] getRowExpr( )
	{
		// row test
		int num = 4;
		IBaseExpression[] _rowBeArray = new IBaseExpression[num];
		_rowBeArray[0] = new ScriptExpression( "dataSetRow.COUNTRY" );
		_rowBeArray[1] = new ScriptExpression( "dataSetRow.CITY" );
		_rowBeArray[2] = new ScriptExpression( "dataSetRow.SALE_DATE" );
		_rowBeArray[3] = new ScriptExpression( "dataSetRow.AMOUNT" );
		
		rowBeNames = new String[num];
		rowBeNames[0] = "COUNTRY1";
		rowBeNames[1] = "CITY1";
		rowBeNames[2] = "SALE_DATE1";
		rowBeNames[3] = "AMOUNT1";

		return _rowBeArray;
	}
	
	/**
	 * @return aggregation expression array
	 */
	private IBaseExpression[] getAggrExpr( )
	{
		int num2 = 2;
		IBaseExpression[] _totalBeArray = new IBaseExpression[num2];
		_totalBeArray[0] = new ScriptExpression( "Total.Count( )" );
		_totalBeArray[1] = new ScriptExpression( "Total.Sum( dataSetRow.AMOUNT )" );

		totalBeNames = new String[num2];
		totalBeNames[0] = "Count( )";
		totalBeNames[1] = "Sum( )";
		
		return _totalBeArray;
	}
	
	/**
	 * @param rowBeArray
	 * @param totalBeArray
	 * @param qd
	 */
	private void prepareExprNameAndQuery( IBaseExpression[] rowBeArray,
			IBaseExpression[] totalBeArray, BaseQueryDefinition qd )
	{
		int num = rowBeArray.length;
		int num2 = totalBeArray.length;

		for ( int i = 0; i < num; i++ )
			qd.addResultSetExpression( rowBeNames[i], rowBeArray[i]);

		for ( int i = 0; i < num2; i++ )
			qd.addResultSetExpression( totalBeNames[i], totalBeArray[i]);
	}
	
	/**
	 * Only check the result of the expectedValue of the result set
	 * 
	 * @param data.it
	 * @param ri
	 * @throws DataException
	 * @throws BirtException
	 */
	private void checkResult( IResultIterator ri )
			throws BirtException
	{
		Iterator it = this.expectedValue.iterator( );

		while ( ri.next( ) )
		{
			String str = "";
			
			for ( int i = 0; i < rowBeArray.length; i++ )
			{
				Object ob1 = it.next( );
				Object ob2 = ri.getValue( rowBeNames[i] );
				assertEquals( ob1, ob2 );
				str += " " + ob2.toString( );
			}

			if ( totalBeArray != null )
			{
				for ( int i = 0; i < totalBeArray.length; i++ )
				{
					Object ob1 = it.next( );
					Object ob2 = ri.getValue( totalBeNames[i] );
					assertEquals( ob1, ob2 );
					str += " " + ob2.toString( );
				}
			}
			
			System.out.println( "row result set: " + str );
		}
	}
	
}
