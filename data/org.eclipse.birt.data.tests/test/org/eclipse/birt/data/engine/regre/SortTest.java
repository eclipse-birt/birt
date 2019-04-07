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
package org.eclipse.birt.data.engine.regre;

import org.eclipse.birt.data.engine.api.APITestCase;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;

import testutil.ConfigText;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * SCR#78081: Sorting on Date time column doesn't take effect
 */
public class SortTest extends APITestCase 
{
	
	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	protected DataSourceInfo getDataSourceInfo( )
	{
		return new DataSourceInfo( ConfigText.getString( "Api.TestDataCalls.TableName" ),
				ConfigText.getString( "Api.TestDataCalls.TableSQL" ),
				"testSort.txt" );
	}
	
	/**
	 * Test to check whether null value can be column data
	 */
	@Test
    public void testDateTime( )
	{
		/** sort definition */
		SortDefinition st = new SortDefinition();
		st.setExpression("dataSetRow.CALLTIME");
		
		/** expression */
		ScriptExpression expr = new ScriptExpression("dataSetRow.CALLTIME");
		
		/** query definition */
		QueryDefinition qd = new QueryDefinition();
		qd.setDataSetName(dataSet.getName());
		qd.addSort(st);
		
		qd.addResultSetExpression("NAME", expr);
		
		try
		{
			IResultIterator ri = executeQuery( qd );
			while ( ri.next( ) )
			{
				Object ob = ri.getValue( "NAME" );
				if ( ob == null )
					testPrintln( "null" );
				else
					testPrintln( ob.toString( ) );
			}
			ri.close( );

			checkOutputFile( );
		}
		catch ( Exception e )
		{
			fail( e.getMessage( ) );
		}
	}
	
}
