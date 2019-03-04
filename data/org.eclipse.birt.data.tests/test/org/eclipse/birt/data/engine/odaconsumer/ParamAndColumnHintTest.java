/*
 *************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer;

import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Properties;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IResultObject;

import testutil.JDBCOdaDataSource;

import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Tests the use of parameter hints and column hints.
 */
public class ParamAndColumnHintTest extends OdaconsumerTestCase
{
    private Connection m_connection;
    private PreparedStatement m_statement;
	@Before
    public void paramAndColumnHintSetUp() throws Exception
    {

/*        if( getName().equals( "testFetchwithTimeHint" ) )
            createTimeDataTypeTableData();
*/    }
	@After
    public void paramAndColumnHintTearDown() throws Exception
    {
/*        if( getName().equals( "testFetchwithTimeHint" ) )
            dropTimeDataTypeTableData();
*/        

		if( m_statement != null )
        	m_statement.close();
		if( m_connection != null )
        	m_connection.close( );
    }
    
    public final void testSetParamValueRetryWithParamHint( )
        throws DataException
    {
        // uses mySQL that does not provide runtime parameterMetaData,
        // but it also does not validate the data type on setting a param value.
        // can only do a negative test here to force a retry
        String queryText = 
            "select acInt from acdatatypes where acInt > ? ";

        // If connection fails, fix connection properties
        m_statement = getLocalMySqlConnection( ).prepareStatement( queryText,
                JDBCOdaDataSource.DATA_SET_TYPE );

        ParameterHint hint = new ParameterHint( "param1", true, false );
        hint.setPosition( 2 );  // use param index below to match hints during retry
        hint.setDataType( java.util.Date.class );
        hint.setNativeDataType( Types.INTEGER );
        m_statement.addParameterHint( hint );

        try
        {
            // use an invalid parameter index to force retry
            m_statement.setParameterValue( 2, "0" );
        }
        catch ( DataException ex )
        {
            // verify that retry attempts to use the native data type - int
            assertEquals( ex.getErrorCode(), ResourceConstants.CANNOT_SET_INT_PARAMETER );
        }
    }
    
    public final void testSetParamValueRetryWithTimestampParamHint( )
        throws DataException
    {
        // uses mySQL that does not provide runtime parameterMetaData,
        // but it also does not validate the data type on setting a param value.
        // can only do a negative test here to force a retry
        String queryText = 
            "select acTimestamp from acdatatypes where acTimestamp > ? ";

        // If connection fails, fix connection properties
        m_statement = getLocalMySqlConnection( ).prepareStatement( queryText,
                JDBCOdaDataSource.DATA_SET_TYPE );

        ParameterHint hint = new ParameterHint( "param1", true, false );
        hint.setPosition( 2 );  // use param index below to match hints during retry
        hint.setDataType( java.util.Date.class );
        hint.setNativeDataType( Types.TIMESTAMP );
        m_statement.addParameterHint( hint );

        try
        {
            // use an invalid parameter index to force retry
            m_statement.setParameterValue( 2, "1999-12-31 03:13:00.001" );
        }
        catch ( DataException ex )
        {
            // verify that retry attempts to use the native data type - int
            assertEquals( ex.getErrorCode(), ResourceConstants.CANNOT_SET_TIMESTAMP_PARAMETER );
        }
    }
    
    public final void testFetchWithTimestampHint( )
        throws DataException
    {
         String queryText = 
            "select acDate, acTimestamp from acdatatypes where acTimestamp > ? ";

        // If connection fails, fix connection properties
        m_statement = getLocalMySqlConnection( ).prepareStatement( queryText,
                JDBCOdaDataSource.DATA_SET_TYPE );

        ParameterHint hint = new ParameterHint( "param1", true, false );
        hint.setPosition( 1 );  // use param index below to match hints during retry
        hint.setDataType( java.util.Date.class );
        hint.setNativeDataType( Types.TIMESTAMP );
        m_statement.addParameterHint( hint );

        Timestamp ts = Timestamp.valueOf( "1999-12-31 03:13:00" );
        m_statement.setParameterValue( 1, ts );
        
        // since runtime metadata is available, hint is not used
        ColumnHint columnHint = new ColumnHint( "acTimestamp" );
        columnHint.setDataType( Integer.class );
        columnHint.setNativeDataType( Types.TIMESTAMP );
        m_statement.addColumnHint( columnHint );

        m_statement.execute( );       
        ResultSet resultSet = m_statement.getResultSet( );
        IResultObject resultObject = null;
        int numRows = 0;
        while ( ( resultObject = resultSet.fetch( ) ) != null )
        {
            numRows++;
            assertEquals( Timestamp.class,
                          resultObject.getResultClass( )
                                .getFieldValueClass( "acTimestamp" ));
            Object value = resultObject.getFieldValue( "acTimestamp" );
            assertTrue( value instanceof Timestamp );
        }
        assertEquals( 4, numRows );
    }
    
    public final void testFetchWithTimeHint( )
        throws DataException
    {
        String queryText = 
            "select acDate, acTime, acTimestamp from acTimeDataTypes " +
            " where acTime > ? ";

        // If connection fails, fix connection properties
        m_statement = getLocalMySqlConnection( ).prepareStatement( queryText,
                JDBCOdaDataSource.DATA_SET_TYPE );

        ParameterHint hint = new ParameterHint( "param1", true, false );
        hint.setPosition( 1 );  // use param index below to match hints during retry
        hint.setDataType( Integer.class );  // use wrong one here to verify native type get used
        hint.setNativeDataType( Types.TIME );
        m_statement.addParameterHint( hint );

        Time timeValue = Time.valueOf( "03:13:00" );
        m_statement.setParameterValue( 1, timeValue );
        
        // since runtime metadata is available, hint is not used
        ColumnHint columnHint = new ColumnHint( "acTime" );
        columnHint.setDataType( Integer.class );
        columnHint.setNativeDataType( Types.TIME );
        m_statement.addColumnHint( columnHint );

        m_statement.execute( );       
        ResultSet resultSet = m_statement.getResultSet( );
        IResultObject resultObject = null;
        int numRows = 0;
        while ( ( resultObject = resultSet.fetch( ) ) != null )
        {
            numRows++;
            assertEquals( Time.class,
                          resultObject.getResultClass( )
                                .getFieldValueClass( "acTime" ));
            Object value = resultObject.getFieldValue( "acTime" );
            assertTrue( value instanceof Time );
        }
    }
    
    public final void testFetchWithDateHint( )
        throws DataException
    {
        String queryText = 
            "select acDate, acTime, acTimestamp from acTimeDataTypes " +
            " where acDate < ? ";

        // If connection fails, fix connection properties
        m_statement = getLocalMySqlConnection( ).prepareStatement( queryText,
                JDBCOdaDataSource.DATA_SET_TYPE );

        ParameterHint hint = new ParameterHint( "param1", true, false );
        hint.setPosition( 1 );  // use param index below to match hints during retry
        hint.setDataType( Integer.class );  // use wrong one here to verify native type get used
        hint.setNativeDataType( Types.DATE );
        m_statement.addParameterHint( hint );

        // use a parameter value whose time portion is greater than the test data,
        // but expects the time portion gets truncated when applied to a Date parameter
        Timestamp ts = Timestamp.valueOf( "2000-10-13 03:13:00" );
        m_statement.setParameterValue( 1, ts );
        
        // since runtime metadata is available, column hint is not used
        ColumnHint columnHint = new ColumnHint( "acDate" );
        columnHint.setDataType( Integer.class );
        m_statement.addColumnHint( columnHint );

        m_statement.execute( );       
        ResultSet resultSet = m_statement.getResultSet( );
        IResultObject resultObject = null;
        int numRows = 0;
        while ( ( resultObject = resultSet.fetch( ) ) != null )
        {
            numRows++;
            assertEquals( java.sql.Date.class,
                          resultObject.getResultClass( )
                                .getFieldValueClass( "acDate" ));
            Object value = resultObject.getFieldValue( "acDate" );
            assertTrue( value instanceof java.sql.Date );
        }
        
        assertEquals( 0, numRows );
    }

    private Properties getLocalMySqlConnProperties()
    {
        // Note: need to replace with own MySQL server's connection properties;
        // and the mysql jdbc driver jar must be in classpath
        Properties connProperties = new Properties( );
        connProperties.setProperty( "odaURL", "jdbc:mysql://birtdb2-w2k:3306/acTestDb" );
        connProperties.setProperty( "odaUser", "nnTest" );
        connProperties.setProperty( "odaPassword", "nTest" );
        connProperties.setProperty( "odaDriverClass", "com.mysql.jdbc.Driver" );
        return connProperties;
    }
    
    private Connection getLocalMySqlConnection( ) throws DataException
    {
        if( m_connection != null )
            return m_connection;
        
        m_connection = ConnectionManager.getInstance( )
                .openConnection( JDBCOdaDataSource.DATA_SOURCE_TYPE, 
                                getLocalMySqlConnProperties(), null );
        return m_connection;
    }

    private void createTimeDataTypeTableData() throws Exception
    {
        java.sql.Connection conn = openMySqlConnection();
        java.sql.Statement stmt = conn.createStatement( );

        // MySQL syntax
        stmt.execute( 
                "create table IF NOT EXISTS acTimeDataTypes" +
                " ( acDate date, acTime time, acTimestamp timestamp )" );
        stmt.execute(
                "insert into acTimeDataTypes values( " + 
                "'2000-10-13' , '06:30:18', '2000-10-13 06:30:18' )" );
        stmt.execute(
                "insert into acTimeDataTypes values( " + 
                "'2001-11-13' , '03:30:28', '2001-11-13 03:30:28' )" );
        
        stmt.close( );     
        conn.close( );
    }
    
    private void dropTimeDataTypeTableData() throws Exception
    {
        java.sql.Connection conn = openMySqlConnection();
        java.sql.Statement stmt = conn.createStatement( );

        stmt.execute( "drop table acTimeDataTypes" );

        stmt.close( );     
        conn.close( );
    }

    private java.sql.Connection openMySqlConnection() throws Exception
    {
        Properties connProp = getLocalMySqlConnProperties();
        // load the specified driver
        Class.forName( connProp.getProperty( "odaDriverClass" )); 
        return java.sql.DriverManager.getConnection( 
                connProp.getProperty( "odaURL" ),
                connProp.getProperty( "odaUser" ),
                connProp.getProperty( "odaPassword" ) );
    }
 
}
