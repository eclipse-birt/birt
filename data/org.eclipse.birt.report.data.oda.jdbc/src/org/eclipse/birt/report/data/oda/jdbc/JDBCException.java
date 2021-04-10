/*******************************************************************************
 * Copyright (c) 2004,2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc;

import com.ibm.icu.util.ULocale;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.SQLException;

import org.eclipse.birt.report.data.oda.i18n.JdbcResourceHandle;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * JDBCException is thrown when a JDBC call results in a java.sql.SQLException
 * being thrown. Error code and SQLState are copied from the SQLException, and
 * the caught SQLException is set as the initCause of the new exception.
 * 
 */
public class JDBCException extends OdaException {
	/** serial ID */
	private static final long serialVersionUID = -3923215393730764306L;

	/** Error code for all JDBCException instances. */
	public final static int ERROR_JDBC = 101;

	private static Logger logger = Logger.getLogger(Connection.class.getName());

	private String errorCode;
	private Object argv[];
	private static JdbcResourceHandle resourceHandle = new JdbcResourceHandle(ULocale.getDefault());

	/**
	 * 
	 * @param message
	 * @param vendorCode
	 */
	public JDBCException(String errorCode, int vendorCode) {
		super(errorCode, null, vendorCode);
		this.errorCode = errorCode;
		if (logger.isLoggable(Level.FINE)) {
			logger.logp(Level.FINE, JDBCException.class.getName(), "JDBCException", errorCode);
		}
	}

	/**
	 * 
	 * @param errorCode
	 * @param cause
	 */
	public JDBCException(String errorCode, SQLException cause) {
		super("JDBCException", // dummy message
				cause == null ? "" : cause.getSQLState(), ERROR_JDBC);
		initCause(cause);
		this.errorCode = errorCode;
		if (logger.isLoggable(Level.FINE)) {
			logger.logp(Level.FINE, JDBCException.class.getName(), "JDBCException", errorCode);
			logSQLException(logger, Level.FINE, cause);
		}
	}

	/**
	 * 
	 * @param errorCode
	 * @param cause
	 * @param argv
	 */
	public JDBCException(String errorCode, SQLException cause, Object argv) {
		super("JDBCException", // dummy message
				cause == null ? "" : cause.getSQLState(), ERROR_JDBC);
		initCause(cause);
		this.errorCode = errorCode;
		this.argv = new Object[] { argv };
		if (logger.isLoggable(Level.FINE)) {
			logger.logp(Level.FINE, JDBCException.class.getName(), "JDBCException", errorCode);
			logSQLException(logger, Level.FINE, cause);
		}
	}

	/**
	 * 
	 * @param errorCode
	 * @param cause
	 * @param argv
	 */
	public JDBCException(String errorCode, SQLException cause, Object argv[]) {
		super("JDBCException", // dummy message
				cause == null ? "" : cause.getSQLState(), ERROR_JDBC);
		initCause(cause);
		this.errorCode = errorCode;
		this.argv = argv;

		if (logger.isLoggable(Level.FINE)) {
			logger.logp(java.util.logging.Level.FINE, JDBCException.class.getName(), "JDBCException", errorCode);
			logSQLException(logger, Level.FINE, cause);
		}
	}

	/*
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		String msg;
		if (argv == null) {
			msg = resourceHandle.getMessage(errorCode);
		} else {
			msg = resourceHandle.getMessage(errorCode, argv);
		}

		Throwable cause = getCause();
		if (cause != null) {
			String extraMsg;
			if (cause instanceof SQLException) {
				extraMsg = getSQLExceptionMesssage((SQLException) cause);
			} else {
				extraMsg = cause.getLocalizedMessage();
			}
			if (extraMsg != null && extraMsg.length() > 0)
				msg += "\n" + extraMsg;
		}
		return msg;
	}

	public String getMessage(ULocale locale) {
		JdbcResourceHandle resourceHandle = new JdbcResourceHandle(locale);
		String msg;
		if (argv == null) {
			msg = resourceHandle.getMessage(errorCode);
		} else {
			msg = resourceHandle.getMessage(errorCode, argv);
		}

		Throwable cause = getCause();
		if (cause != null) {
			String extraMsg;
			if (cause instanceof SQLException) {
				extraMsg = getSQLExceptionMesssage((SQLException) cause);
			} else {
				extraMsg = cause.getLocalizedMessage();
			}
			if (extraMsg != null && extraMsg.length() > 0)
				msg += "\n" + extraMsg;
		}
		return msg;
	}

	/**
	 * Utility function to log a SQLException to provided logger.
	 */
	public static void logSQLException(Logger logger, Level logLevel, SQLException sqlException) {
		assert logger != null;
		// SQL Exception may be chained. Need to log all exceptions
		SQLException e = sqlException;
		int count = 1;
		while (e != null) {
			logger.log(logLevel, "SQL Exception #" + count, e);
			if (++count > 50) {
				// Programmer's paranoia; don't get stuck in a loop
				break;
			}
			e = e.getNextException();
		}
	}

	/**
	 * Utility function to concatenate all SQLException error messages
	 */
	public static String getSQLExceptionMesssage(SQLException sqlException) {
		assert sqlException != null;

		// SQL Exception may be chained. Need to get all exception messages
		StringBuffer msg = new StringBuffer();

		SQLException e = sqlException;
		int count = 1;
		while (e != null) {
			msg.append("SQL error #").append(count).append(":").append(e.getLocalizedMessage()).append("\n"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

			if (++count > 50) {
				// Programmer's paranoia; don't get stuck in a loop
				break;
			}
			e = e.getNextException();
		}
		return msg.toString();
	}
}