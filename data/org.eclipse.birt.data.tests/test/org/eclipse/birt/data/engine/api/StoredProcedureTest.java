/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.ParameterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;

import testutil.ConfigText;
import testutil.JDBCDataSource;
import testutil.JDBCOdaDataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

/**
 * to test stored procedure in derby database
 */
@Ignore("Ignore tests that require manual setup")
public class StoredProcedureTest extends APITestCase
{

	/*
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
    public void storedProcedureSetUp() throws Exception
	{
		createTestProcedure( );
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	protected DataSourceInfo getDataSourceInfo( )
	{
		return new DataSourceInfo( ConfigText.getString( "Api.TestBlobAndClob.TableName" ),
				ConfigText.getString( "Api.TestBlobAndClob.TableSQL" ),
				ConfigText.getString( "Api.TestBlobAndClob.TestDataFileName" ) );
	}

	/**
	 * Creates a test procedure
	 */
	protected void createTestProcedure( ) throws Exception
	{
		if ( dataSourceInstance == null )
			dataSourceInstance = JDBCDataSource.newInstance( );

		this.dataSourceInstance.createStoredProcedure( ConfigText.getString( "Api.TestProcedure.ProcedureName" ),
				ConfigText.getString( "Api.TestProcedure.ProcedureSQL" ),
				true );
	}

	/**
	 * execute the procedure with inputparameter
	 *
	 */
	@Test
    public void test1( )
	{
		try
		{
			IBaseDataSetDesign baseDataset = newDataSet( "newDataSet",
					getCallableSQL( ) );
			( (OdaDataSetDesign) baseDataset ).setQueryText( getCallableSQL( ) );
			Collection inputParamDefns = new ArrayList( );
			ParameterDefinition inputParamDefn = new ParameterDefinition( "param1",
					DataType.INTEGER_TYPE );
			inputParamDefn.setInputMode( true );
			inputParamDefn.setPosition( 1 );
			inputParamDefn.setDefaultInputValue( "0" );
			inputParamDefns.add( inputParamDefn );

			InputParameterBinding paramBinding = new InputParameterBinding( "param1",
					new ScriptExpression( "100" ) );

			// important notice:
			// derby does not support returning parameter metadata, so
			// output parameter feature can not be supported.
			if ( inputParamDefns != null )
			{
				Iterator iterator = inputParamDefns.iterator( );
				while ( iterator.hasNext( ) )
				{
					ParameterDefinition paramDefn = (ParameterDefinition) iterator.next( );
					if ( paramDefn.isInputMode( ) )
						( (OdaDataSetDesign) baseDataset ).addParameter( paramDefn );
				}
			}
			QueryDefinition queryDefn = new QueryDefinition( );
			queryDefn.setDataSetName( baseDataset.getName( ) );
			queryDefn.addInputParamBinding( paramBinding );

			IPreparedQuery preparedQuery = dataEngine.prepare( queryDefn );
			IQueryResults queryResults = preparedQuery.execute( null );
			queryResults.getResultIterator( );
		}
		catch ( Exception e )
		{
			fail( e.getMessage( ) );
		}
	}

	/**
	 * new a JDBC dataset with specified datasetname and querytext 
	 * 
	 * @param datasetName
	 * @param queryText
	 * @return dataset
	 * @throws Exception
	 */
	protected OdaDataSetDesign newDataSet( String datasetName, String queryText )
			throws Exception
	{
		OdaDataSetDesign dset = new OdaDataSetDesign( datasetName );
		dset.setDataSource( this.dataSource.getName( ) );
		dset.setQueryText( queryText );
		dset.setExtensionID( JDBCOdaDataSource.SP_DATA_SET_TYPE );
		dataEngine.defineDataSet( dset );
		return dset;
	}

	/**
	 * Used to create query text for data set
	 * 
	 * @return query text of data set
	 */
	protected String getCallableSQL( )
	{
		// Default SQL: call procedureName(?)
		return "call App.testProc(?) ";
	}

	/**
	 * the procedure "testProc"
	 * @param inputParam
	 */
	public static void selectData( int inputParam )
	{
		assert inputParam == 100;
	}

}
