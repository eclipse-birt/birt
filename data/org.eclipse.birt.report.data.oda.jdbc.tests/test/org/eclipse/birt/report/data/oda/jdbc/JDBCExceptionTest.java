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

package org.eclipse.birt.report.data.oda.jdbc;

import java.sql.SQLException;
import com.ibm.icu.util.ULocale;

import org.eclipse.birt.report.data.oda.i18n.JdbcResourceHandle;
import org.eclipse.birt.report.data.oda.i18n.ResourceConstants;



import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * The class implements the unit test for JDBCException
 * 
 */
public class JDBCExceptionTest {
	@Test
    public void testJDBCException( )
	{
		JdbcResourceHandle resourceHandle = new JdbcResourceHandle( ULocale.getDefault( ) );
		
		SQLException sqle = new SQLException( );
		/* use the SQLException to construct the JDBCException */
		String errorCode = "odajdbc.CannotCloseConn";
		JDBCException je = new JDBCException( errorCode , sqle );

		/* verify the error message is what's expected. */
		assertTrue( je.getMessage( ).startsWith( 
				resourceHandle.getMessage( ResourceConstants.CONN_CANNOT_CLOSE )) );

		/* verify the sqlState is inherited from the SQLException. */
		assertEquals( je.getSQLState( ), sqle.getSQLState( ) );

		/* verify the errorcode is what's definied. */
		assertEquals( je.getErrorCode( ), JDBCException.ERROR_JDBC );

		/* verify the initCause is the SQLException. */
		assertEquals( je.getCause( ), sqle );

		errorCode = "odajdbc.driver.DriverMissingProperties";
		je = new JDBCException( errorCode,
				  1111 );
		/* verify the error message is what's expected. */
		assertEquals( je.getMessage( ),
				resourceHandle.getMessage( ResourceConstants.DRIVER_MISSING_PROPERTIES ) );
		/* verify the sqlState is null. */
		assertNull( je.getSQLState( ) );
		/* verify the errorcode is what's definied. */
		assertEquals( je.getErrorCode( ), 1111 );
	}

}
