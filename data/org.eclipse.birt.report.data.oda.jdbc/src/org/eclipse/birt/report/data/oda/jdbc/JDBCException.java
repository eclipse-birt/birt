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

import org.eclipse.birt.data.oda.OdaException;
import org.eclipse.birt.data.oda.util.logging.Level;

/**
 * JDBCException is thrown when a JDBC call results in a java.sql.SQLException
 * being thrown. Error code and SQLState are copied from the SQLException, and
 * the caught SQLException is set as the initCause of the new exception.
 * 
 */
public class JDBCException extends OdaException
{

	/** Error code for all JDBCException instances. */
	public final static int ERROR_JDBC = 101;

	/**
	 * Constructor
	 * 
	 * @param sqlException
	 *            the SQLException caused by JDBC call.
	 */
	public JDBCException( SQLException sqlException )
	{
		super( "A JDBC Exception occured: "
				+ sqlException.getLocalizedMessage( ), sqlException
				.getSQLState( ), ERROR_JDBC );
		initCause(sqlException);
		JDBCConnectionFactory.log(Level.SEVERE_LEVEL, sqlException);		
	}
}