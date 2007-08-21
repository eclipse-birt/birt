
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
package org.eclipse.birt.data.engine.binding;

import org.eclipse.birt.data.engine.api.APITestCase;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;

import testutil.ConfigText;
/**
 * 
 */

public class QueryCacheTest extends APITestCase
{
	protected DataSourceInfo getDataSourceInfo( )
	{
		return new DataSourceInfo( ConfigText.getString( "Binding.TestData.TableName" ),
				ConfigText.getString( "Binding.TestData.TableSQL" ),
				ConfigText.getString( "Binding.TestData.TestDataFileName" ) );
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void testBasicCache( ) throws Exception
	{
		QueryDefinition query = new QueryDefinition();
		query.setDataSetName( this.dataSet.getName( ) );
		query.setAutoBinding( true );
		query.setCacheQueryResults( true );
		IQueryResults queryResults = this.dataEngine.prepare( query ).execute( null );
		String id = queryResults.getID( );
		IResultIterator it = queryResults.getResultIterator( );
		
		IQueryResults result = this.dataEngine.getQueryResults( id );
		it = result.getResultIterator( );
		this.outputQueryResult( it, new String[]{"CITY", "AMOUNT"} );
		this.checkOutputFile( );
	}
}
