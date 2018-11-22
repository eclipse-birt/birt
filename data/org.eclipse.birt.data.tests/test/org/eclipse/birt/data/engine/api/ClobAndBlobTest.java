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
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;

import testutil.ConfigText;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;


/**
 *
 */
public class ClobAndBlobTest extends APITestCase
{
	// expression array
	private String[] beName;
	private IBaseExpression[] beArray;
	
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
	 * 
	 * @throws Exception
	 */
	@Test
    public void testClobAndBlob( ) throws Exception
	{
		QueryDefinition queryDefn = newReportQuery( );
		prepareExpression( queryDefn );
		
		
		IResultIterator ri = executeQuery( queryDefn );
		IResultMetaData md = ri.getResultMetaData();
		
		while ( ri.next( ) )
		{
			String str = "";
			for ( int i = 0; i < beArray.length; i++ )
			{
				if ( md.getColumnTypeName( i + 1 )
						.equals( DataType.BINARY_TYPE_NAME )||md.getColumnTypeName( i + 1 )
						.equals( DataType.BLOB_TYPE_NAME ) )
					str += new String( ri.getBytes( beName[i] ) );
				else
					str += ri.getValue( beName[i] );
				
				if ( i < beArray.length - 1 )
					str += ", ";
			}
			testPrintln( str );
		}
		
		checkOutputFile();
	}
	
	/**
	 * Add expression to query definition
	 * @param queryDefn
	 * @throws DataException 
	 */
	private void prepareExpression( QueryDefinition queryDefn ) throws DataException
	{
		beName = new String[3];
		beArray = new ScriptExpression[3];

		beName[0] = "_ID";
		ScriptExpression se = new ScriptExpression( "dataSetRow.ID" );
		se.setDataType( DataType.INTEGER_TYPE );
		beArray[0] = se;

		beName[1] = "_NAME";
		se = new ScriptExpression( "dataSetRow.NAME" );
		se.setDataType( DataType.STRING_TYPE );
		beArray[1] = se;

		beName[2] = "_INFO";
		se = new ScriptExpression( "dataSetRow.INFO" );
		se.setDataType( DataType.BLOB_TYPE );
		beArray[2] = se;

		for ( int i = 0; i < beName.length; i++ )
		{
			queryDefn.addBinding( new Binding( beName[i], beArray[i] ) );
		}
	}

}
