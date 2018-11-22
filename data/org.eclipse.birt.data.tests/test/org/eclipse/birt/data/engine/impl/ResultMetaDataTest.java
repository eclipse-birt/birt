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


package org.eclipse.birt.data.engine.impl;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.APITestCase;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.querydefn.ColumnDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ComputedColumn;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;

import testutil.ConfigText;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

/**
 * Result metadata test case 1
 */
public class ResultMetaDataTest extends APITestCase
{
	
	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	protected DataSourceInfo getDataSourceInfo( )
	{
		return new DataSourceInfo( ConfigText.getString( "Impl.TestData3.TableName" ),
				ConfigText.getString( "Impl.TestData3.TableSQL" ),
				ConfigText.getString( "Impl.TestData3.TestDataFileName" ) );
	}

	// Basic test to get MD for all columns
	@Test
    public void test1_SelectAll( ) throws Exception
	{
		OdaDataSetDesign dset = newDataSet( "dset1", "Select * FROM "
				+ this.getTestTableName( ) );
		QueryDefinition query = this.newReportQuery( dset, true );

		IPreparedQuery pq = this.dataEngine.prepare( query );
		IQueryResults qr = pq.execute( null );

		IResultMetaData md = qr.getResultMetaData( );
		this.testPrint( Util.getMetaDadataInfo( md ) );
		checkOutputFile( );

		// Get Metadata from result iterator; it should be the same
		closeOutputFile( );
		openOutputFile( ); // This ought to clean up the output file

		md = qr.getResultIterator( ).getResultMetaData( );
		this.testPrint( Util.getMetaDadataInfo( md ) );
		checkOutputFile();
		
		// Test columnCount eliminating temp columns
		assertEquals( 17, md.getColumnCount( ) );

		// Test out of bound reads
		try
		{
			md.getColumnName( -1 );
			fail( "Exception expected" );
		}
		catch ( DataException e )
		{
			// We are OK
		}

		try
		{
			md.getColumnName( 18 );
			fail( "Exception expected" );
		}
		catch ( DataException e )
		{
			// We are OK
		}

	}

	// Test with setting column hints
	@Test
    public void test2_ResultHints( ) throws Exception
	{
		OdaDataSetDesign dset = newDataSet( "dset2", "Select * FROM "
				+ this.getTestTableName( ) );
		QueryDefinition query = this.newReportQuery( dset, true );

		// Add a column hint for column #1
		ColumnDefinition coldef = new ColumnDefinition( "name1" );
		coldef.setColumnPosition( 1 );
		coldef.setAlias( "colAlias1" );
		// Note: this hint should not change the actual data type returned in
		// result md
		coldef.setDataType( DataType.DOUBLE_TYPE );
		dset.addResultSetHint( coldef );

		// Add a column alias to textCol
		coldef = new ColumnDefinition( "textCol" );
		coldef.setAlias( "colAlias2" );
		dset.addResultSetHint( coldef );

		// Add an invalid result hint for non-existing named column (should be
		// ignored)
		coldef = new ColumnDefinition( "invalidCol1" );
		coldef.setAlias( "invalidColAlias1" );
		dset.addResultSetHint( coldef );

		// Add an invalid result hint for non-existing indexed column (should be
		// ignored)
		coldef = new ColumnDefinition( "invalidCol2" );
		coldef.setAlias( "invalidColAlias2" );
		coldef.setColumnPosition( 30 );
		dset.addResultSetHint( coldef );

		IPreparedQuery pq = this.dataEngine.prepare( query );
		IQueryResults qr = pq.execute( null );

		IResultMetaData md = qr.getResultMetaData( );
		this.testPrint( Util.getMetaDadataInfo( md ) );
		checkOutputFile();
	}

	// Test with computed column in SQL
/*	public void test3_ComputedCol( ) throws Exception
	{
		// Use a new data set with SQL containing computed columns
		// The 2 computed columns are unnamed (at least with MS SQL Server)
		OdaDataSetDesign dset = newDataSet( "dset3",
				"Select intCol, intCol + intCol, floatCol * intCol FROM "
						+ this.getTestTableName( ) );
		QueryDefinition query = this.newReportQuery( dset );

		// Add a BIRT computed column
		ComputedColumn compCol = new ComputedColumn( "ComputedCol1",
				"row.intCol * 2" );
		dset.addComputedColumn( compCol );

		// Add a column hint for column #2 (the unnamed computed col)
		ColumnDefinition coldef = new ColumnDefinition( "intCol_plus_intCol" );
		coldef.setColumnPosition( 2 );
		dset.addResultSetHint( coldef );

		// Give the BIRT computed column an alias
		coldef = new ColumnDefinition( "ComputedCol1" );
		coldef.setAlias( "ComputedCol1_alias" );
		dset.addResultSetHint( coldef );

		IPreparedQuery pq = this.dataEngine.prepare( query );
		IQueryResults qr = pq.execute( null );

		IResultMetaData md = qr.getResultMetaData( );
		this.printMetadadata( md );
		checkOutputFile();
	}
*/
	// Test column projection
	@Test
    public void test4_ProjectedCol( ) throws Exception
	{
		// Construct query that selects all columns
		OdaDataSetDesign dset = newDataSet( "dset4", "Select * FROM "
				+ this.getTestTableName( ) );
		QueryDefinition query = this.newReportQuery( dset, true );

		// Add a BIRT computed column
		ComputedColumn compCol = new ComputedColumn( "ComputedCol1",
				"row.intCol * 2",DataType.ANY_TYPE );
		dset.addComputedColumn( compCol );

		// Add an alias for intCol; this alias will be used
		// for column projection
		ColumnDefinition coldef = new ColumnDefinition( "intCol" );
		coldef.setAlias( "intCol_alias" );
		dset.addResultSetHint( coldef );

		query.setColumnProjection( new String[]{
				"floatCol", "varbinaryCol", "intCol_alias", "ComputedCol1"
		} );

		IPreparedQuery pq = this.dataEngine.prepare( query );
		IQueryResults qr = pq.execute( null );

		IResultMetaData md = qr.getResultMetaData( );
		this.testPrint( Util.getMetaDadataInfo( md ) );
		checkOutputFile();

		// Use an empty projection list; should return all columns
		query.setColumnProjection( new String[]{} );
		pq = this.dataEngine.prepare( query );
		qr = pq.execute( null );
		assertTrue( qr.getResultMetaData( ).getColumnCount( ) == 18 );
		assertTrue( qr.getResultIterator( )
				.getResultMetaData( )
				.getColumnCount( ) == 18 );

		// Test an invalid projection list
		query.setColumnProjection( new String[]{
			"invalid_col"
		} );
		pq = this.dataEngine.prepare( query );
		try
		{
			qr = pq.execute( null );
//			fail( "Invalid project column should generate exception." );
		}
		catch ( DataException e)
		{
			// TODO: check error code here
		}
	}
}
