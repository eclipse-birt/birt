/*
 * ****************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 * 
 * *****************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer;

import java.math.BigDecimal;
import java.sql.Date;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

import testutil.JDBCOdaDataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

public class PreparedStatementTest extends ConnectionTest
{

	private PreparedStatement m_statement;
	@Before
    public void preparedStatementSetUp() throws Exception
	{
		String command = "select * from \"testtable\" where \"intColumn\" > ?"; //$NON-NLS-1$
		reprepareStatement( command );
	}
	@After
    public void preparedStatementTearDown() throws Exception
	{
		m_statement.close( );
	}

	public final void testFindInParameter( ) throws DataException
	{
		try
		{
			// ODA-JDBC doesn't support named parameters
			m_statement.findInParameter( "someParamName" ); //$NON-NLS-1$
			assertTrue( false );
		}
		catch ( DataException ex )
		{
			assertEquals( ex.getErrorCode(), ResourceConstants.CANNOT_FIND_IN_PARAMETER );
			assertEquals( "No named Parameter supported.", ex.getCause( ) //$NON-NLS-1$
					.getMessage( ) );
		}
	}

	public final void testSetParameterValueintObject0( ) throws DataException
	{
		m_statement.setParameterValue( 1, new Integer( 4 ) );
		testParamExecute( 2 );
	}

	public final void testSetParameterValueintObject1( )
	{
		try
		{
			m_statement.setParameterValue( 2, new Integer( 4 ) );
		}
		catch ( DataException ex )
		{
			assertEquals( ex.getErrorCode(), ResourceConstants.CANNOT_SET_STRING_PARAMETER );
			//assertEquals( "A JDBC Exception occured: Invalid parameter index
			// 2.",
			//			  ex.getCause().getMessage() );
		}
	}

    public final void testSetParameterValueBooleanTrue( ) throws DataException
    {
        // assign boolean to an intColumn, which should convert to value 1
        m_statement.setParameterValue( 1, new Boolean( "true" ) ); //$NON-NLS-1$
        testParamExecute( 3 );
    }

    public final void testSetParameterValueBooleanFalse( ) throws DataException
    {
        // assign boolean to an intColumn, which should convert to value 0
        m_statement.setParameterValue( 1, new Boolean( "false" ) ); //$NON-NLS-1$
        testParamExecute( 3 );
    }

	public final void testSetParameterValueDoubleObject( ) throws DataException
	{
		String command = "select * from \"testtable\" where \"doubleColumn\" < ?"; //$NON-NLS-1$
		reprepareStatement( command );
		m_statement.setParameterValue( 1, new Double( 12.3636 ) );
		testParamExecute( 3 );
	}

	public final void testSetParameterValueStringObject( ) throws DataException
	{
		String command = "select * from \"testtable\" where \"stringColumn\" " //$NON-NLS-1$
				+ "between ? and ?"; //$NON-NLS-1$
		reprepareStatement( command );
		m_statement.setParameterValue( 1, "blah blah blah" ); //$NON-NLS-1$
		m_statement.setParameterValue( 2, "seven zero six" ); //$NON-NLS-1$
		testParamExecute( 4 );
	}

	public final void testSetParameterValueDateObject( ) throws DataException
	{
		String command = "select * from \"testtable\" where \"dateColumn\" < ?"; //$NON-NLS-1$
		reprepareStatement( command );
		m_statement.setParameterValue( 1, Date.valueOf( "1999-01-01" ) ); //$NON-NLS-1$
		testParamExecute( 2 );
	}

	public final void testSetParameterValueDecimalObject( ) throws DataException
	{
		String command = "select * from \"testtable\" where \"decimalColumn\" = ? OR " //$NON-NLS-1$
				+ "\"decimalColumn\" = ?"; //$NON-NLS-1$
		reprepareStatement( command );
		m_statement.setParameterValue( 1, new BigDecimal( 10 ) );
		m_statement.setParameterValue( 2, new BigDecimal( 10000 ) );
		testParamExecute( 3 );
	}

    public final void testSetParameterNullValue( ) throws DataException
    {
        String command = "select * from \"testtable\" where \"stringColumn\" " //$NON-NLS-1$
            + "like ? "; //$NON-NLS-1$
        reprepareStatement( command );
                
        boolean hasError = false;
        try
        {
            m_statement.setParameterValue( 1, null );
            m_statement.execute( );
        }
        catch( Exception e )
        {
            hasError = true;
        }
        assertFalse( hasError );
    }    

    public final void testSetParameterNullValueForPrimitiveType( ) throws DataException
    {
        String command =  
                "select * from \"testtable\" where \"doubleColumn\" < ?"; //$NON-NLS-1$
        PreparedStatement myStmt = getConnection().prepareStatement( command,
                JDBCOdaDataSource.DATA_SET_TYPE );

        boolean hasError = false;
        try
        {
            myStmt.setParameterValue( 1, null );
        }
        catch( Exception e )
        {
        	// expects odaconsumer to not able to retry for a primitive type
            hasError = true;
        }
        
        myStmt.close();
        // derby jdbc driver supports setNull
        assertFalse( hasError );
    }

	private void testParamExecute( int rowsExpected ) throws DataException
	{
		assertTrue( m_statement.execute( ) );
		ResultSet result = m_statement.getResultSet( 1 );
		assertNotNull( result );
		int count = 0;
		while ( result.fetch( ) != null )
			count++;
		assertEquals( rowsExpected, count );
	}

	public final void testSetParameterValueInvalidParam( ) throws DataException
	{
		try
		{
			// ODA-JDBC doesn't support named parameters
			m_statement.setParameterValue( "someParamName", new Integer( 1 ) ); //$NON-NLS-1$
			assertTrue( false );
		}
		catch ( DataException ex )
		{
			assertEquals( ex.getErrorCode(), ResourceConstants.CANNOT_SET_STRING_PARAMETER );
		}
	}

	public final void testClearParameterValues1( ) throws DataException
	{
		prepareForClearParameterTests( );
		try
		{
			m_statement.execute( );
			fail( );
		}
		catch ( DataException ex )
		{
			assertEquals( ex.getErrorCode(), ResourceConstants.CANNOT_EXECUTE_STATEMENT );

			//assertEquals( "A JDBC Exception occured: Parameter #1 has not
			// been set.",
			//              ex.getCause().getMessage() );
		}
	}

	public final void testClearParameterValues2( ) throws DataException
	{
		prepareForClearParameterTests( );
		m_statement.setParameterValue( 1, new BigDecimal( 600 ) );
		m_statement.setParameterValue( 2, new BigDecimal( 10000 ) );
		testParamExecute( 2 );
	}

	private void prepareForClearParameterTests( ) throws DataException
	{
		String command = "select * from \"testtable\" where \"decimalColumn\" = ? OR " //$NON-NLS-1$
				+ "\"decimalColumn\" = ?"; //$NON-NLS-1$
		reprepareStatement( command );
		m_statement.setParameterValue( 1, new BigDecimal( 10 ) );
		m_statement.setParameterValue( 2, new BigDecimal( 10000 ) );
		testParamExecute( 3 );
		m_statement.clearParameterValues( );
	}

    private void reprepareStatement( String queryText ) throws DataException
    {
        if( m_statement != null )
            m_statement.close();
        m_statement = getConnection( ).prepareStatement( queryText,
                JDBCOdaDataSource.DATA_SET_TYPE );
    }
    
}
