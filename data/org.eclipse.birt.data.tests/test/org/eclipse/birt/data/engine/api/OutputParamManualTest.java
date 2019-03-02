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

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.ParameterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;

import testutil.BaseTestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.*;

/**
 * Derby does not support DatabaseMetaData in Connection, and then
 * registerOutputParameter can not be successfully done. Because of it, the test
 * has to be done on other database and then it is marked as manual test.
 */
public class OutputParamManualTest extends BaseTestCase
{
	// connection property
	private String url = "jdbc:jtds:sqlserver://linzhu:1433/pubs";
	private String driverClass = "net.sourceforge.jtds.jdbc.Driver";
	private String user = "sa";
	private String password = "sa";
	private String queryText = "{call sumTitle(?,?)}";

	private OdaDataSourceDesign odaDataSource;
	private OdaDataSetDesign odaDataSet;
	private QueryDefinition queryDefinition;
	private String[] expressionName;
	private IBaseExpression[] expressionArray;

	/** JDBC data source and data set info */
	private static final String JDBC_DATA_SOURCE_TYPE = "org.eclipse.birt.report.data.oda.jdbc";
	private static final String JDBC_DATA_SET_TYPE = "org.eclipse.birt.report.data.oda.jdbc.SPSelectDataSet";

	/**
	 * @return IBaseDataSourceDesign
	 * @throws Exception
	 */
	private IBaseDataSourceDesign getDataSource( ) throws Exception
	{
		if ( odaDataSource != null )
			return odaDataSource;

		odaDataSource = new OdaDataSourceDesign( "Test Data Source" );
		odaDataSource.setExtensionID( JDBC_DATA_SOURCE_TYPE );
		odaDataSource.addPublicProperty( "odaURL", url );
		odaDataSource.addPublicProperty( "odaDriverClass", driverClass );
		odaDataSource.addPublicProperty( "odaUser", user );
		odaDataSource.addPublicProperty( "odaPassword", password );

		return odaDataSource;
	}

	/**
	 * Add info of input and output parameter.
	 * 
	 * @return IBaseDataSetDesign
	 * @throws Exception
	 */
	private IBaseDataSetDesign getDataSet( ) throws Exception
	{
		if ( odaDataSet != null )
			return odaDataSet;

		odaDataSet = new OdaDataSetDesign( "Test Data Set" );
		odaDataSet.setDataSource( getDataSource( ).getName( ) );
		odaDataSet.setExtensionID( JDBC_DATA_SET_TYPE );
		odaDataSet.setQueryText( queryText );

		ParameterDefinition pd1 = new ParameterDefinition( "param1",
				DataType.STRING_TYPE,
				true,
				false );
		pd1.setPosition( 1 );

		ParameterDefinition pd2 = new ParameterDefinition( "@@param2",
				DataType.DECIMAL_TYPE,
				true,
				true );
		pd2.setPosition( 2 );

		InputParameterBinding pb1 = new InputParameterBinding( "param1",
				new ScriptExpression( "'%'" ) );

		InputParameterBinding pb2 = new InputParameterBinding( "@@param2",
				new ScriptExpression( "1" ) );

		odaDataSet.addParameter( pd1 );
		odaDataSet.addParameter( pd2 );
		odaDataSet.addInputParamBinding( pb1 );
		odaDataSet.addInputParamBinding( pb2 );

		return odaDataSet;
	}

	/**
	 * @return query defintion with output parameter expression
	 * @throws Exception
	 */
	private QueryDefinition getQueryDefn( ) throws Exception
	{
		if ( queryDefinition != null )
			return queryDefinition;

		queryDefinition = new QueryDefinition( );
		queryDefinition.setDataSetName( getDataSet( ).getName( ) );

		// add expression based on group defintion
		expressionArray = new IBaseExpression[2];
		expressionName = new String[2];

		expressionName[0] = "_outputParams1";
		ScriptExpression expr = new ScriptExpression( "outputParams[1]" );
		expressionArray[0] = expr;

		expressionName[1] = "_outputParams2";
		expr = new ScriptExpression( "outputParams[\"@@param2\"]" );
		expressionArray[1] = expr;

		for ( int i = 0; i < expressionArray.length; i++ )
			queryDefinition.addResultSetExpression( expressionName[i], expressionArray[i] );

		return queryDefinition;
	}

	/**
	 * @return defined array of expression
	 */
	private IBaseExpression[] getExprArray( )
	{
		return expressionArray;
	}

	/*
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
    public void outputParamManualSetUp() throws Exception
	{


		System.setProperty( "BIRT_HOME", "./test" );
		System.setProperty( "PROPERTY_RUN_UNDER_ECLIPSE", "false" );
		Platform.startup( null );
	}

	/**
	 * Test disk based feature.
	 * This test is using net.sourceforge.jtds.jdbc.Driver to access SQL server which is not
	 * included in default package. The JDBC driver has to installed in order for the test
	 * to pass. 
	 */
	@Ignore("Test must be run manually")
	@Test
    public void testOutputParameter( ) throws BirtException, Exception
	{
		// prepare
		DataEngineContext context = DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				this.scriptContext,
				null,
				null,
				null );
		context.setTmpdir( this.getTempDir( ) );
		PlatformConfig platformConfig = new PlatformConfig();
		platformConfig.setTempDir( this.getTempDir() );
		DataEngine de = DataEngine.newDataEngine( platformConfig, context );
		de.defineDataSource( this.getDataSource( ) );
		de.defineDataSet( this.getDataSet( ) );

		// execute
		IPreparedQuery pq = de.prepare( this.getQueryDefn( ) );
		IQueryResults qr = pq.execute( null );

		// get value
		IBaseExpression[] bes = getExprArray( );

		IResultIterator ri = qr.getResultIterator( );
		ri.next( );
		
		for ( int i = 0; i < bes.length; i++ )
		{
			Object ob = ri.getValue( expressionName[i] );
			assertTrue( ob != null );
			System.out.println( ob );
		}

		// clean
		ri.close( );
		qr.close( );
		de.shutdown( );
	}

}
