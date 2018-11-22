/*
 * ****************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *  Actuate Corporation - initial API and implementation
 * 
 * *****************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer;

import java.sql.Types;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;

import testutil.JDBCOdaDataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

public class ParameterHintTest extends ConnectionTest
{

	private DataResourceHandle resourceHandle = DataResourceHandle.getInstance( );
@Test
    public void testNameToPositionInSingleParameterHint( ) throws Exception
	{
		PreparedStatement statement = getConnection( ).prepareStatement( "select \"intColumn\" from \"testtable\" where \"intColumn\" = ?",
				JDBCOdaDataSource.DATA_SET_TYPE );
		assertNotNull( statement );

		ParameterHint hint = new ParameterHint( "ParamName", true, false );
		hint.setPosition( 1 );
		statement.addParameterHint( hint );

		statement.setParameterValue( "ParamName", new Integer( 4 ) );
		assertTrue( statement.execute( ) );
		ResultSet resultset = statement.getResultSet( );

		int count = 0;
		while ( resultset.fetch( ) != null )
			count++;

		assertEquals( 1, count );
	}
	@Test
    public void testNameToPositionInParameterHints( ) throws Exception
	{
		String command = "select \"intColumn\" from \"testtable\" where \"intColumn\" = ? OR \"stringColumn\" = ?";
		PreparedStatement statement = getConnection( ).prepareStatement( command,
				JDBCOdaDataSource.DATA_SET_TYPE );
		assertNotNull( statement );

		ParameterHint hint = new ParameterHint( "ParamName1", true, false );
		hint.setPosition( 1 );
		statement.addParameterHint( hint );

		hint = new ParameterHint( "ParamName2", true, false );
		hint.setPosition( 2 );
		statement.addParameterHint( hint );

		statement.setParameterValue( "ParamName1", new Integer( 0 ) );
		statement.setParameterValue( "ParamName2", "blah blah blah" );
		assertTrue( statement.execute( ) );
		ResultSet resultset = statement.getResultSet( );

		int count = 0;
		while ( resultset.fetch( ) != null )
			count++;

		assertEquals( 2, count );
	}
	@Test
    public void testValidateParameterHints1( ) throws Exception
	{
		try
		{
			PreparedStatement statement = getConnection( ).prepareStatement( "select \"intColumn\" from \"testtable\" where \"intColumn\" = ?",
					JDBCOdaDataSource.DATA_SET_TYPE );
			assertNotNull( statement );

			ParameterHint hint = new ParameterHint( "ParamName1", true, false );
			hint.setPosition( 1 );
			statement.addParameterHint( hint );

			// conflicting hint on same parameter name
			hint = new ParameterHint( "ParamName1", true, false );
			hint.setPosition( 2 );
			statement.addParameterHint( hint );

			fail( );
		}
		catch ( DataException ex )
		{
			String msg = resourceHandle.getMessage( ResourceConstants.SAME_PARAM_NAME_FOR_DIFFERENT_HINTS,
					new Object[]{
						"ParamName1"
					} );
			assertEquals( msg, ex.getMessage( ) );
		}
	}
	@Test
    public void testValidateInputParameterHints2( ) throws Exception
	{
		PreparedStatement statement = getConnection( ).prepareStatement( "select \"intColumn\" from \"testtable\" where \"intColumn\" = ?",
				JDBCOdaDataSource.DATA_SET_TYPE );
		assertNotNull( statement );

		ParameterHint hint = new ParameterHint( "ParamName1", true, false );
		hint.setPosition( 1 );
		statement.addParameterHint( hint );

		hint = new ParameterHint( "PName1", true, false );
		hint.setPosition( 1 );

		try
		{
			statement.addParameterHint( hint );
			fail( );     // should have an exception
		}
		catch ( DataException ex )
		{
			String msg = resourceHandle.getMessage( ResourceConstants.DIFFERENT_PARAM_NAME_FOR_SAME_POSITION,
					new Object[]{
							"ParamName1", new Integer( 1 )
					} );
			assertEquals( msg, ex.getMessage( ) );
		}
	}
	@Test
    public void testValidateOutputParameterHints( ) throws Exception
	{
		PreparedStatement statement = getConnection( ).prepareStatement( "select \"intColumn\" from \"testtable\" where \"intColumn\" = ?",
				JDBCOdaDataSource.DATA_SET_TYPE );
		assertNotNull( statement );

		ParameterHint outputHint = new ParameterHint( "ParamName1", false, true );
		outputHint.setPosition( 2 );
		statement.addParameterHint( outputHint );

		ParameterHint hint = new ParameterHint( "PName1", true, false );
		hint.setPosition( 2 );

		try
		{
			statement.addParameterHint( hint );
			fail( );
		}
		catch ( DataException ex )
		{
			String msg = resourceHandle.getMessage( ResourceConstants.DIFFERENT_PARAM_NAME_FOR_SAME_POSITION,
					new Object[]{
							"ParamName1", new Integer( 2 )
					} );
			assertEquals( msg, ex.getMessage( ) );
		}
	}
	@Test
    public void testValidateInputParameterHintsSucceed( ) throws Exception
    {
        PreparedStatement statement = getConnection( ).prepareStatement( "select \"intColumn\" from \"testtable\" where \"intColumn\" = ?",
                JDBCOdaDataSource.DATA_SET_TYPE );
        assertNotNull( statement );

        // 2 hints with different model names and positions, but same native name
        ParameterHint hint = new ParameterHint( "ParamName1", true, false );
        hint.setPosition( 1 );
        hint.setNativeName( "sameNativeName" );
        statement.addParameterHint( hint );

        hint = new ParameterHint( "ParamName2", true, false );
        hint.setPosition( 2 );
        hint.setNativeName( "sameNativeName" );

        try
        {
            statement.addParameterHint( hint );
            assertTrue( true );     // no exception, test succeeded
        }
        catch ( DataException ex )
        {
            fail( );
        }
    }
	@Test
    public void testGetParameterMetaData1( ) throws Exception
	{
		PreparedStatement statement = getConnection( ).prepareStatement( "select \"intColumn\" from \"testtable\" where \"intColumn\" = ?",
				JDBCOdaDataSource.DATA_SET_TYPE );
		assertNotNull( statement );

		Collection parameterMetaData = statement.getParameterMetaData( );
		assertNotNull( parameterMetaData );
		assertEquals( 1, parameterMetaData.size( ) );

		Iterator iter = parameterMetaData.iterator( );
		while ( iter.hasNext( ) )
		{
			ParameterMetaData metadata = (ParameterMetaData) iter.next( );
			checkDefaultMetaData( metadata, 1 );
		}
	}
	@Test
    public void testGetParameterMetaData2( ) throws Exception
	{
		PreparedStatement statement = getConnection( ).prepareStatement( "select \"intColumn\" from \"testtable\" where \"intColumn\" < ? AND \"intColumn\" > ?",
				JDBCOdaDataSource.DATA_SET_TYPE );
		assertNotNull( statement );

		Collection parameterMetaData = statement.getParameterMetaData( );
		assertNotNull( parameterMetaData );
		assertEquals( 2, parameterMetaData.size( ) );

		Iterator iter = parameterMetaData.iterator( );
		int count = 1;
		while ( iter.hasNext( ) )
		{
			ParameterMetaData metadata = (ParameterMetaData) iter.next( );
			checkDefaultMetaData( metadata, count++ );
		}
	}

	private void checkDefaultMetaData( ParameterMetaData metadata, int index )
	{
		//the following code specified for derby database JDBC driver returned
		// metadata
		assertEquals( index, metadata.getPosition( ) );
		assertEquals( Types.INTEGER, metadata.getDataType() );
		assertEquals( null, metadata.getName( ) );
		assertEquals( null, metadata.getDefaultValue( ) );
		assertEquals( "INTEGER", metadata.getNativeTypeName() );
		assertEquals( 0, metadata.getScale( ) );
		assertEquals( 10, metadata.getPrecision() );
		assertEquals( Boolean.TRUE, metadata.isInputMode( ) );
		assertEquals( Boolean.FALSE, metadata.isOutputMode( ) );
		assertEquals( null, metadata.isOptional( ) );
		assertEquals( Boolean.TRUE, metadata.isNullable( ) );
	}
	@Test
    public void testGetParameterMetaData3( ) throws Exception
	{
		PreparedStatement statement = getConnection( ).prepareStatement( "select \"intColumn\" from \"testtable\" where \"intColumn\" = ?",
				JDBCOdaDataSource.DATA_SET_TYPE );
		assertNotNull( statement );

		Collection parameterMetaData = statement.getParameterMetaData( );
		assertNotNull( parameterMetaData );

		ParameterHint hint = new ParameterHint( "ParamName1", true, false );
		hint.setPosition( 1 );
		hint.setNativeName( "paramNativeName" );
		hint.setDataType( Integer.class );
		hint.setIsInputOptional( false );
		statement.addParameterHint( hint );

		Collection parameterMetaData1 = statement.getParameterMetaData( );
		assertNotNull( parameterMetaData1 );
		assertNotSame( parameterMetaData, parameterMetaData1 );

		Iterator iter = parameterMetaData1.iterator( );
		while ( iter.hasNext( ) )
		{
			ParameterMetaData metadata = (ParameterMetaData) iter.next( );
			assertEquals( 1, metadata.getPosition( ) );
			//This expected value only suitable for Derby Database
			assertEquals( Types.INTEGER, metadata.getDataType( ) );
			assertEquals( "ParamName1", metadata.getName( ) );
            assertEquals( "paramNativeName", metadata.getNativeName( ) );
			assertEquals( null, metadata.getDefaultValue( ) );
			//This expected value only suitable for Derby Database
			assertEquals( "INTEGER", metadata.getNativeTypeName( ) );
			assertEquals( 0, metadata.getScale( ) );
			//This expected value only suitable for Derby Database
			assertEquals( 10, metadata.getPrecision( ) );
			assertEquals( Boolean.TRUE, metadata.isInputMode( ) );
			assertEquals( Boolean.FALSE, metadata.isOutputMode( ) );
			assertEquals( Boolean.FALSE, metadata.isOptional( ) );
			assertEquals( Boolean.TRUE, metadata.isNullable( ) );
		}
	}
	@Ignore("Ignore tests that require manual setup")
	@Test
    public void testUnsupportedRuntimeParameterMetaData( ) throws Exception
	{
        // uses mySQL that does not provide runtime parameterMetaData
		Connection connection = getMySqlConnection( );
		PreparedStatement statement = connection.prepareStatement( "SELECT \"intColumn\", \"doubleColumn\" FROM \"testtable\" WHERE \"intColumn\" > ?",
				JDBCOdaDataSource.DATA_SET_TYPE );
		assertNotNull( statement );

		Collection parameterMetaData = null;
        try
        {
            parameterMetaData = statement.getParameterMetaData( );
        }
        catch( DataException e )
        {
            // ignore
        }
        assertNull( parameterMetaData );

		ParameterHint hint = new ParameterHint( "ParamName1", true, false );
		hint.setPosition( 1 );
		hint.setDataType( Integer.class );
		hint.setIsInputOptional( false );
		statement.addParameterHint( hint );

		ParameterHint hint2 = new ParameterHint( "ParamName2", true, false );
		hint2.setDataType( Double.class );
		statement.addParameterHint( hint2 );

		Collection parameterMetaData1 = statement.getParameterMetaData( );
		assertNotNull( parameterMetaData1 );

		Iterator iter = parameterMetaData1.iterator( );
		int paramNumInCollection = 1;
		while ( iter.hasNext( ) )
		{
			ParameterMetaData metadata = (ParameterMetaData) iter.next( );
			if ( paramNumInCollection++ == 1 )
			{
				assertEquals( 1, metadata.getPosition( ) );
				assertEquals( Types.INTEGER, metadata.getDataType( ) );
				assertEquals( "ParamName1", metadata.getName( ) );
				assertEquals( null, metadata.getDefaultValue( ) );
				assertEquals( null, metadata.getNativeTypeName( ) );
				assertEquals( -1, metadata.getScale( ) );
				assertEquals( -1, metadata.getPrecision( ) );
				assertEquals( Boolean.TRUE, metadata.isInputMode( ) );
				assertEquals( Boolean.FALSE, metadata.isOutputMode( ) );
				assertEquals( Boolean.FALSE, metadata.isOptional( ) );
				assertEquals( Boolean.TRUE, metadata.isNullable( ) );
			}
			else
			{
				assertEquals( -1, metadata.getPosition( ) );
				assertEquals( Types.DOUBLE, metadata.getDataType( ) );
				assertEquals( "ParamName2", metadata.getName( ) );
				assertEquals( null, metadata.getDefaultValue( ) );
				assertEquals( null, metadata.getNativeTypeName( ) );
				assertEquals( -1, metadata.getScale( ) );
				assertEquals( -1, metadata.getPrecision( ) );
				assertEquals( Boolean.TRUE, metadata.isInputMode( ) );
				assertEquals( Boolean.FALSE, metadata.isOutputMode( ) );
				assertEquals( Boolean.TRUE, metadata.isOptional( ) );
				assertEquals( Boolean.TRUE, metadata.isNullable( ) );
			}
		}
	}
	@Ignore("Ignore tests that require manual setup")
	@Test
    public void testMultipleHintsOnSameParameterName( ) throws Exception
	{
		Connection connection = getMySqlConnection( );
		PreparedStatement statement = connection.prepareStatement( "SELECT \"intColumn\", \"doubleColumn\" FROM \"testtable\" WHERE \"intColumn\" > ?",
				JDBCOdaDataSource.DATA_SET_TYPE );
		assertNotNull( statement );

		Collection parameterMetaData = null;
        try
        {
            parameterMetaData = statement.getParameterMetaData( );
        }
        catch( DataException e )
        {
            // ignore
        }
        assertNull( parameterMetaData );

		ParameterHint hint = new ParameterHint( "ParamName1", true, false );
		hint.setPosition( 1 );
		hint.setNativeName( "paramNativeName1" );
		hint.setDataType( Integer.class );
		hint.setIsInputOptional( false );
		statement.addParameterHint( hint );

		ParameterHint hint2 = new ParameterHint( "ParamName2", false, true );
		hint2.setDataType( Double.class );
		statement.addParameterHint( hint2 );

		// another hint to change the design metadata of the first parameter
		ParameterHint hint3 = new ParameterHint( "ParamName1", false, true );
        hint3.setNativeName( "paramNativeName3" );
		statement.addParameterHint( hint3 );

		Collection parameterMetaData1 = statement.getParameterMetaData( );
		assertNotNull( parameterMetaData1 );

		Iterator iter = parameterMetaData1.iterator( );
		int paramNumInCollection = 1;
		while ( iter.hasNext( ) )
		{
			ParameterMetaData metadata = (ParameterMetaData) iter.next( );
			if ( paramNumInCollection++ == 1 )
			{
				assertEquals( 1, metadata.getPosition( ) );
				assertEquals( Types.INTEGER, metadata.getDataType( ) );
				assertEquals( "ParamName1", metadata.getName( ) );
                // last hint's nativeName is effective
				assertEquals( "paramNativeName3", metadata.getNativeName() );   
				assertEquals( null, metadata.getDefaultValue( ) );
				assertEquals( null, metadata.getNativeTypeName( ) );
				assertEquals( -1, metadata.getScale( ) );
				assertEquals( -1, metadata.getPrecision( ) );
				
				// should have md from the third hint
				assertEquals( Boolean.FALSE, metadata.isInputMode( ) );
				assertEquals( Boolean.TRUE, metadata.isOutputMode( ) );
				
				assertEquals( Boolean.TRUE, metadata.isOptional( ) );
				assertEquals( Boolean.TRUE, metadata.isNullable( ) );
			}
			else
			{
				assertEquals( -1, metadata.getPosition( ) );
				assertEquals( Types.DOUBLE, metadata.getDataType( ) );
				assertEquals( "ParamName2", metadata.getName( ) );
				assertEquals( null, metadata.getDefaultValue( ) );
				assertEquals( null, metadata.getNativeTypeName( ) );
				assertEquals( -1, metadata.getScale( ) );
				assertEquals( -1, metadata.getPrecision( ) );
				assertEquals( Boolean.FALSE, metadata.isInputMode( ) );
				assertEquals( Boolean.TRUE, metadata.isOutputMode( ) );
				assertEquals( Boolean.TRUE, metadata.isOptional( ) );
				assertEquals( Boolean.TRUE, metadata.isNullable( ) );
			}
		}
	}
	@Ignore("Ignore tests that require manual setup")
	@Test
    public void testUnsupportedRuntimeParameterMetaData2( ) throws Exception
	{
        // uses mySQL that does not provide runtime parameterMetaData
		Connection connection = getMySqlConnection( );
		PreparedStatement statement = connection.prepareStatement( "SELECT \"intColumn\", \"doubleColumn\" FROM \"testtable\" WHERE \"intColumn\" > ?",
				JDBCOdaDataSource.DATA_SET_TYPE );
		assertNotNull( statement );

		Collection parameterMetaData = null;
        try
        {
            parameterMetaData = statement.getParameterMetaData( );
        }
        catch( DataException e )
        {
            // ignore
        }
        assertNull( parameterMetaData );

		ParameterHint hint = new ParameterHint( "ParamName1", true, false );
		hint.setPosition( 1 );
		hint.setDataType( Integer.class );
		hint.setIsInputOptional( false );
		hint.setDefaultInputValue( "123" );
		statement.addParameterHint( hint );

		ParameterHint hint2 = new ParameterHint( "ParamName2", true, false );
		hint2.setDataType( Double.class );
		hint2.setDefaultInputValue( "456" );
		statement.addParameterHint( hint2 );

		Collection parameterMetaData1 = statement.getParameterMetaData( );
		assertNotNull( parameterMetaData1 );

		Iterator iter = parameterMetaData1.iterator( );
		int paramNumInCollection = 1;
		while ( iter.hasNext( ) )
		{
			ParameterMetaData metadata = (ParameterMetaData) iter.next( );
			if ( paramNumInCollection++ == 1 )
			{
				assertEquals( 1, metadata.getPosition( ) );
				assertEquals( Types.INTEGER, metadata.getDataType( ) );
				assertEquals( "ParamName1", metadata.getName( ) );
				assertEquals( "123", metadata.getDefaultValue( ) );
				assertEquals( null, metadata.getNativeTypeName( ) );
				assertEquals( -1, metadata.getScale( ) );
				assertEquals( -1, metadata.getPrecision( ) );
				assertEquals( Boolean.TRUE, metadata.isInputMode( ) );
				assertEquals( Boolean.FALSE, metadata.isOutputMode( ) );
				assertEquals( Boolean.FALSE, metadata.isOptional( ) );
				assertEquals( Boolean.TRUE, metadata.isNullable( ) );
			}
			else
			{
				assertEquals( -1, metadata.getPosition( ) );
				assertEquals( Types.DOUBLE, metadata.getDataType( ) );
				assertEquals( "ParamName2", metadata.getName( ) );
				assertEquals( "456", metadata.getDefaultValue( ) );
				assertEquals( null, metadata.getNativeTypeName( ) );
				assertEquals( -1, metadata.getScale( ) );
				assertEquals( -1, metadata.getPrecision( ) );
				assertEquals( Boolean.TRUE, metadata.isInputMode( ) );
				assertEquals( Boolean.FALSE, metadata.isOutputMode( ) );
				assertEquals( Boolean.TRUE, metadata.isOptional( ) );
				assertEquals( Boolean.TRUE, metadata.isNullable( ) );
			}
		}
	}
	@Test
    public void testUnsupportedParameterDataTypes( ) throws Exception
	{
		ParameterHint inputHint = new ParameterHint( "InputParameter", true, true );

	    boolean isErrorCaught = false;
		try
        {
            inputHint.setDataType( IBlob.class );
        }
        catch( IllegalArgumentException e )
        {
            isErrorCaught = true;
        }
	    assertTrue( isErrorCaught );

	    isErrorCaught = false;
	    try
        {
            inputHint.setDataType( IClob.class );
        }
        catch( IllegalArgumentException e )
        {
            isErrorCaught = true;
        }
	    assertTrue( isErrorCaught );
	}
	@Ignore("Ignore tests that require manual setup")
	@Test
    public void testMergeParamHints( ) throws Exception
	{
        // uses mySQL that does not provide runtime parameterMetaData
		Connection connection = getMySqlConnection( );
		PreparedStatement statement = connection.prepareStatement( "SELECT \"intColumn\", \"doubleColumn\" FROM \"testtable\" WHERE \"intColumn\" > ?",
				JDBCOdaDataSource.DATA_SET_TYPE );

		ParameterHint hint = new ParameterHint( "ParamName1", true, false );
		hint.setPosition( 1 );
		hint.setDataType( Integer.class );
		hint.setIsInputOptional( false );
		statement.addParameterHint( hint );

		// another hint on same parameter name, but as output parameter mode
		ParameterHint outputHint = new ParameterHint( "ParamName1", false, true );
		outputHint.setPosition( 1 );
		outputHint.setDataType( Integer.class );
		statement.addParameterHint( outputHint );

		Collection parameterMetaData = statement.getParameterMetaData( );
		assertTrue( parameterMetaData != null && parameterMetaData.size( ) == 1 );
		Iterator iter = parameterMetaData.iterator( );
		while ( iter.hasNext( ) )
		{
			ParameterMetaData metadata = (ParameterMetaData) iter.next( );
			assertEquals( 1, metadata.getPosition( ) );
			assertEquals( Types.INTEGER, metadata.getDataType( ) );
			assertEquals( "ParamName1", metadata.getName( ) );
			assertEquals( null, metadata.getDefaultValue( ) );
			assertEquals( null, metadata.getNativeTypeName( ) );
			assertEquals( -1, metadata.getScale( ) );
			assertEquals( -1, metadata.getPrecision( ) );
			assertEquals( Boolean.FALSE, metadata.isInputMode( ) );
			assertEquals( Boolean.TRUE, metadata.isOutputMode( ) );
			assertEquals( Boolean.TRUE, metadata.isOptional( ) );
			assertEquals( Boolean.TRUE, metadata.isNullable( ) );
		}
	}
	@Ignore("Ignore tests that require manual setup")
	@Test
    public void testMergeParamHints1( ) throws Exception
	{
        // uses mySQL that does not provide runtime parameterMetaData
		Connection connection = getMySqlConnection( );
		PreparedStatement statement = connection.prepareStatement( "SELECT \"intColumn\", \"doubleColumn\" FROM \"testtable\" WHERE \"intColumn\" > ?",
				JDBCOdaDataSource.DATA_SET_TYPE );

		ParameterHint hint = new ParameterHint( "ParamName1", true, false );
		hint.setDataType( Integer.class );
		hint.setIsInputOptional( false );
		statement.addParameterHint( hint );

        // another hint on same parameter name, with additional metadata attributes
		hint = new ParameterHint( "ParamName1", true, false );
		hint.setPosition( 1 );
		hint.setNativeName( "paramNativeName" );
		hint.setDataType( Integer.class );
		statement.addParameterHint( hint );

		Collection parameterMetaData = statement.getParameterMetaData( );
		assertTrue( parameterMetaData != null && parameterMetaData.size( ) == 1 );
		Iterator iter = parameterMetaData.iterator( );
		while ( iter.hasNext( ) )
		{
			ParameterMetaData metadata = (ParameterMetaData) iter.next( );
			assertEquals( 1, metadata.getPosition( ) );
			assertEquals( Types.INTEGER, metadata.getDataType( ) );
			assertEquals( "ParamName1", metadata.getName( ) );
            assertEquals( "paramNativeName", metadata.getNativeName( ) );
			assertEquals( null, metadata.getDefaultValue( ) );
			assertEquals( null, metadata.getNativeTypeName( ) );
			assertEquals( -1, metadata.getScale( ) );
			assertEquals( -1, metadata.getPrecision( ) );
			assertEquals( Boolean.TRUE, metadata.isInputMode( ) );
			assertEquals( Boolean.FALSE, metadata.isOutputMode( ) );
			assertEquals( Boolean.TRUE, metadata.isOptional( ) );
			assertEquals( Boolean.TRUE, metadata.isNullable( ) );
		}
	}
	@Ignore("Ignore tests that require manual setup")
	@Test
    public void testConflictingParamHints( ) throws Exception
	{
		try
		{
	        // uses mySQL that does not provide runtime parameterMetaData
			Connection connection = getMySqlConnection( );
			PreparedStatement statement = connection.prepareStatement( "SELECT \"intColumn\", \"doubleColumn\" FROM \"testtable\" WHERE \"intColumn\" > ?",
					JDBCOdaDataSource.DATA_SET_TYPE );

			ParameterHint hint = new ParameterHint( "ParamName1", true, false );
			hint.setPosition( 1 );
			hint.setDataType( Integer.class );
			hint.setIsInputOptional( false );
			statement.addParameterHint( hint );

			ParameterHint outputHint = new ParameterHint( "ParamName1", false, true );
			outputHint.setPosition( 2 );
			outputHint.setDataType( Integer.class );
			statement.addParameterHint( outputHint );
			fail( );
		}
		catch ( DataException ex )
		{
			String msg = resourceHandle.getMessage( ResourceConstants.SAME_PARAM_NAME_FOR_DIFFERENT_HINTS,
					new Object[]{
						"ParamName1"
					} );
			assertEquals( msg, ex.getMessage( ) );
		}
	}
	@Ignore("Ignore tests that require manual setup")
	@Test
    public void testMergeParamHints3( ) throws Exception
	{
        // uses mySQL that does not provide runtime parameterMetaData
		Connection connection = getMySqlConnection( );
		PreparedStatement statement = connection.prepareStatement( "SELECT \"intColumn\", \"doubleColumn\" FROM \"testtable\" WHERE \"intColumn\" > ?",
				JDBCOdaDataSource.DATA_SET_TYPE );

		ParameterHint hint = new ParameterHint( "ParamName1", true, false );
		hint.setPosition( 1 );
		hint.setDataType( Integer.class );
		hint.setIsInputOptional( false );
		statement.addParameterHint( hint );

		ParameterHint outputHint = new ParameterHint( "ParamName1", false, true );
		outputHint.setDataType( Double.class );
		statement.addParameterHint( outputHint );

		Collection parameterMetaData = statement.getParameterMetaData( );
		assertTrue( parameterMetaData != null && parameterMetaData.size( ) == 1 );
		Iterator iter = parameterMetaData.iterator( );
		while ( iter.hasNext( ) )
		{
			ParameterMetaData metadata = (ParameterMetaData) iter.next( );
			assertEquals( 1, metadata.getPosition( ) );
			assertEquals( Types.DOUBLE, metadata.getDataType( ) );
			assertEquals( "ParamName1", metadata.getName( ) );
			assertEquals( null, metadata.getDefaultValue( ) );
			assertEquals( null, metadata.getNativeTypeName( ) );
			assertEquals( -1, metadata.getScale( ) );
			assertEquals( -1, metadata.getPrecision( ) );
			assertEquals( Boolean.FALSE, metadata.isInputMode( ) );
			assertEquals( Boolean.TRUE, metadata.isOutputMode( ) );
			assertEquals( Boolean.TRUE, metadata.isOptional( ) );
			assertEquals( Boolean.TRUE, metadata.isNullable( ) );
		}
	}
	@Test
    public void testMergeParamHintsWithRuntimeMd() throws Exception 
	{
        PreparedStatement statement = 
            getConnection().prepareStatement( "select \"intColumn\" from \"testtable\" where \"intColumn\" = ?", 
                                              JDBCOdaDataSource.DATA_SET_TYPE ); 
        assertNotNull( statement );
      
        Collection parameterMetaData = statement.getParameterMetaData();
        assertNotNull( parameterMetaData );
      
        ParameterHint hint = new ParameterHint( "ParamName1", true, false );
        hint.setPosition( 1 );
        hint.setNativeName( "myParam1" );
        hint.setDataType( Integer.class ); 
        hint.setIsInputOptional( false );
        statement.addParameterHint( hint );

        Collection parameterMetaData1 = statement.getParameterMetaData();
        assertNotNull( parameterMetaData1 ); 
        assertNotSame( parameterMetaData, parameterMetaData1 );
      
        Iterator iter = parameterMetaData1.iterator(); 
        while( iter.hasNext() ) 
        {
            ParameterMetaData metadata = (ParameterMetaData) iter.next();
            assertEquals( 1, metadata.getPosition() ); 
            assertEquals( Types.INTEGER, metadata.getDataType() ); 
            assertEquals( "ParamName1", metadata.getName() ); 
            assertEquals( "myParam1", metadata.getNativeName() ); 
            assertEquals( null, metadata.getDefaultValue() ); //
            assertEquals( "INTEGER", metadata.getNativeTypeName() ); 
            assertEquals( 0, metadata.getScale() ); 
            assertEquals( 10, metadata.getPrecision() );
            assertEquals( Boolean.TRUE, metadata.isInputMode() ); 
            assertEquals( Boolean.FALSE, metadata.isOutputMode() ); 
            assertEquals( Boolean.FALSE, metadata.isOptional() ); 
            assertEquals( Boolean.TRUE, metadata.isNullable() ); 
        } 
    }
	@Test
    public void testMergeParamHintsWithDefaultValue() throws Exception {
		PreparedStatement statement = 
			getConnection().prepareStatement( "select \"intColumn\" from \"testtable\" where \"intColumn\" = ?",
											  JDBCOdaDataSource.DATA_SET_TYPE ); 
		assertNotNull( statement );
	  
		Collection parameterMetaData = statement.getParameterMetaData();
		assertNotNull( parameterMetaData );
	  
		ParameterHint hint = new ParameterHint( "ParamName1", true, false );
		hint.setPosition( 1 );
		hint.setDataType( Integer.class ); 
		hint.setIsInputOptional( false );
		hint.setDefaultInputValue( "123" ); 
		statement.addParameterHint( hint );

		Collection parameterMetaData1 = statement.getParameterMetaData();
		assertNotNull( parameterMetaData1 ); 
		assertNotSame( parameterMetaData, parameterMetaData1 );
	  
		Iterator iter = parameterMetaData1.iterator(); 
		while( iter.hasNext() ) 
		{
			ParameterMetaData metadata = (ParameterMetaData) iter.next();
			assertEquals( 1, metadata.getPosition() ); 
			assertEquals( Types.INTEGER, metadata.getDataType() ); 
			assertEquals( "ParamName1", metadata.getName() ); 
			assertEquals( "123", metadata.getDefaultValue() ); 
			assertEquals( "INTEGER", metadata.getNativeTypeName() ); 
			assertEquals( 0, metadata.getScale() );
			assertEquals( 10, metadata.getPrecision() ); 
			assertEquals( Boolean.TRUE, metadata.isInputMode() ); 
			assertEquals( Boolean.FALSE, metadata.isOutputMode() ); 
			assertEquals( Boolean.FALSE, metadata.isOptional() ); 
			assertEquals( Boolean.TRUE, metadata.isNullable() ); 
		} 
	}

	// Test that the LOB data type specified in a ParameterHint
	// for an output parameter gets merged with the
	// runtime parameter metadata.
	@Test
    public void testMergeParamHintOnLOB( ) throws Exception
	{
        String queryText = "select * from \"testtable_lob\" where \"clob1\" like ? ";
		PreparedStatement statement = getConnection( ).prepareStatement( queryText,
				JDBCOdaDataSource.DATA_SET_TYPE );
		assertNotNull( statement );

		Collection parameterMetaData = statement.getParameterMetaData( );
		assertNotNull( parameterMetaData );

		ParameterHint hint = new ParameterHint( "OutParam", false, true );
		hint.setPosition( 1 );
		hint.setDataType( IClob.class );
		statement.addParameterHint( hint );

		Collection parameterMetaData1 = statement.getParameterMetaData( );
		assertNotNull( parameterMetaData1 );
		assertNotSame( parameterMetaData, parameterMetaData1 );

		Iterator iter = parameterMetaData1.iterator( );
		while ( iter.hasNext( ) )
		{
			ParameterMetaData metadata = (ParameterMetaData) iter.next( );
			assertEquals( 1, metadata.getPosition( ) );
			assertEquals( "OutParam", metadata.getName( ) );
			assertEquals( Types.CLOB, metadata.getDataType( ) );
		}
		statement.close();
	}

}
