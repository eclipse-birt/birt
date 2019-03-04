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

import org.eclipse.birt.data.engine.api.APITestCase;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;

import testutil.ConfigText;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 */

public class DefineDataSourceTest extends APITestCase
{
	
	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	protected DataSourceInfo getDataSourceInfo( )
	{
		return new DataSourceInfo( ConfigText.getString( "Binding.TestData.TableName" ),
				ConfigText.getString( "Binding.TestData.TableSQL" ),
				ConfigText.getString( "Binding.TestData.TestDataFileName" ) );
	}
	
	/**
	 * @throws Exception 
	 */
	@Test
    public void testDefineDataSource( ) throws Exception
	{
		DataEngine testEngine = new DataEngineImpl( DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				null,
				null,
				null ) );
		testEngine.defineDataSource( this.dataSource );
		testEngine.defineDataSet( this.dataSet );
		
		// column mapping
		IPreparedQuery preparedQuery = testEngine.prepare( newQueryDefn( ) );
		IQueryResults queryResults = preparedQuery.execute( null );
		IResultMetaData meta = queryResults.getResultMetaData( );
		assertTrue( meta != null );
		
		testEngine.defineDataSource( this.dataSource );
		testEngine.defineDataSet( this.dataSet );
		IResultIterator ri2 = testEngine.prepare( newQueryDefn( ) )
				.execute( null )
				.getResultIterator( );
		while ( ri2.next( ) )
		{
		}
		ri2.close( );
		
		IResultIterator ri = queryResults.getResultIterator( );
		ri.close( );
		
		testEngine.shutdown( );
	}
	
	/**
	 * @return
	 */
	private IQueryDefinition newQueryDefn( )
	{
		QueryDefinition queryDefn = new QueryDefinition( );
		queryDefn.setDataSetName( this.dataSet.getName( ) );

		String[] name = new String[]{
			"testColumn1"
		};
		ScriptExpression[] se = new ScriptExpression[name.length];
		se[0] = new ScriptExpression( "dataSetRow.COUNTRY" );
		for ( int i = 0; i < name.length; i++ )
			queryDefn.addResultSetExpression( name[i], se[i] );

		return queryDefn;
	}

}
