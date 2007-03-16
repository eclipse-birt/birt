/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.data.oda.i18n.ResourceConstants;
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.SortSpec;
import org.eclipse.datatools.connectivity.oda.util.manifest.ConnectionProfileProperty;


/**
 * 
 * The class implements the org.eclipse.datatools.connectivity.IQuery interface.
 * 
 */
public class Statement implements IQuery
{

	/** the JDBC preparedStatement object */
	protected PreparedStatement preStat;

	/** the JDBC Connection object */
	protected java.sql.Connection conn;

	/** remember the max row value, default 0. */
	protected int maxrows;
	
	/** indicates if need to call JDBC setMaxRows before execute statement */
	protected boolean maxRowsUpToDate = false;

	/** Error message for ERRMSG_SET_PARAMETER */
	private final static String ERRMSG_SET_PARAMETER = "Error setting value for SQL parameter #";
	
	private static Logger logger = Logger.getLogger( Statement.class.getName( ) );	

	private IResultSetMetaData cachedResultMetaData;
	private IResultSet cachedResultSet;
	
	/**
	 * assertNull(Object o)
	 * 
	 * @param o
	 *            the object that need to be tested null or not. if null, throw
	 *            exception
	 */
	private void assertNotNull( Object o ) throws OdaException
	{
		if ( o == null )
		{
			throw new JDBCException( ResourceConstants.DRIVER_NO_STATEMENT,
					ResourceConstants.ERROR_NO_STATEMENT );

		}
	}

	/**
	 * 
	 * Constructor Statement(java.sql.Connection connection) use JDBC's
	 * Connection to construct it.
	 *  
	 */
	public Statement( java.sql.Connection connection ) throws OdaException
	{
		if ( connection != null )

		{
			/* record down the JDBC Connection object */
			this.preStat = null;
			this.conn = connection;
			maxrows = -1;
		}
		else
		{
			throw new JDBCException( ResourceConstants.DRIVER_NO_CONNECTION,
					ResourceConstants.ERROR_NO_CONNECTION );
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IQuery#prepare(java.lang.String)
	 */
	public void prepare( String command ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				Statement.class.getName( ),
				"prepare",
				"Statement.prepare( \"" + command + "\" )" );

		try
		{
			if ( command == null ){
				logger.logp( java.util.logging.Level.FINE,
						Statement.class.getName( ),
						"prepare",
						"Query text can not be null." );
				throw new OdaException( "Query text can not be null." );
			}
			
			// Clear any cached result set or metadata
			this.cachedResultMetaData = null;
			this.cachedResultSet = null;
			
			/*
			 * call the JDBC Connection.prepareStatement(String) method to get
			 * the preparedStatement
			 */
			this.preStat = conn.prepareStatement( command );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.STATEMENT_CANNOT_PREPARE,
					e );
		}
	}
	
	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setAppContext(java.lang.Object)
	 */
	public void setAppContext( Object context ) throws OdaException
	{
	    // do nothing; no support for pass-through application context
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IQuery#setProperty(java.lang.String,
	 *      java.lang.String)
	 */
	public void setProperty( String name, String value ) throws OdaException
	{
		if ( name == null )
			throw new NullPointerException("name is null");
		
		if ( name.equals("queryTimeOut") )
		{
			// Ignore null or empty value
			if ( value != null && value.length() > 0 )
			{
				try
				{
					// Be forgiving if a floating point gets passed in - can happen 
					// when Javascript gets involved in calculating the property value
					double secs = Double.parseDouble( value );
					this.preStat.setQueryTimeout( (int) secs );
				}
				catch ( SQLException e )
				{
					// This is not an essential property; log and ignore error if driver doesn't
					// support query timeout
					logger.log( Level.FINE, "Statement.setQueryTimeout failed", e );
				}
			}
		}
		else if (name.equals(ConnectionProfileProperty.PROFILE_NAME_PROP_KEY)
				|| name.equals(ConnectionProfileProperty.PROFILE_STORE_FILE_PROP_KEY)
				|| name.equals( ConnectionProfileProperty.PROFILE_STORE_FILE_PATH_PROP_KEY))
		{
			//do nothing here. These are valid ODA properties. See Eclipse bug 176140
		}
		else
		{
			// unsupported query properties
			OdaException e = new OdaException( "Unsupported query property: " + name );
			logger.logp( java.util.logging.Level.FINE,
					Statement.class.getName( ),
					"setProperty",
					"Unsupported property", e);
			throw e;
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IQuery#close()
	 */
	public void close( ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				Statement.class.getName( ),
				"close",
				"Statement.close( )" );
		try
		{
			if ( preStat != null )
			{
				/* redirect the call to JDBC preparedStatement.close() */
				this.preStat.close( );
			}
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.PREPAREDSTATEMENT_CANNOT_CLOSE , e );
		}
		this.cachedResultMetaData = null;
		this.cachedResultSet = null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IQuery#setMaxRows(int)
	 */
	public void setMaxRows( int max )
	{
		logger.logp( java.util.logging.Level.FINE,
				Statement.class.getName( ),
				"setMaxRows",
				"Statement.setMaxRows( " + max + " )" );
		if ( max != maxrows && max >= 0 )
		{
			maxrows = max;
			maxRowsUpToDate = false;
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IQuery#getMaxRows()
	 */
	public int getMaxRows( )
	{
		logger.logp( java.util.logging.Level.FINE,
				Statement.class.getName( ),
				"getMaxRows",
				"Statement.getMaxRows( )" );
		return this.maxrows;

	}

	/*
	 * @see org.eclipse.datatools.connectivity.IQuery#getMetaData()
	 */
	public IResultSetMetaData getMetaData( ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				Statement.class.getName( ),
				"getMetaData",
				"Statement.getMetaData( )" );		
		assertNotNull( preStat );

		if ( this.cachedResultMetaData != null )
			return this.cachedResultMetaData;
		
	    java.sql.ResultSetMetaData resultmd = null;
		try
		{
			/* redirect the call to JDBC preparedStatement.getMetaData() */
			resultmd = preStat.getMetaData( );
		}
		catch ( Throwable e )
		{
			// Some data base will throw error, such as AbstractMethodError of
			// DB2 7.2, so Throwable should be used to catch all possible
			// exceptions or errors.
			// 
			// For some database, meta data of table can not be obtained
			// in prepared time. To solve this problem, query execution is
			// required to be executed first.
			resultmd = null;
		}
		if ( resultmd != null )
		{
			try
			{
				// in the case of oracle 8.1.7, even if ResultMetaData can be
				// gotten prepare time, the getColumnCount function is still
				// unavailable.
				resultmd.getColumnCount( );
				
				// in the case of sybase 4.9.2 using the sun jdbc-odbc
				// driver and freetds the database doesn't support ResultMetaData
				// at prepare time and get 0 as column count.
				// in the other case, having 0 column doesn't make sense neither and
				// it doesn't cost anything to try to get the columns from the query.
				if (resultmd.getColumnCount() <= 0) {
					resultmd = null;
				}
			}
			catch ( SQLException e )
			{
				resultmd = null;
			}

			if ( resultmd != null )
			{
				cachedResultMetaData = new ResultSetMetaData( resultmd );
			}
		}
		
		if ( cachedResultMetaData == null )
		{
			// If Jdbc driver throw an SQLexception or return null for
			// retrieving metadata from prepared statement, and then we have to
			// get metaData from ResultSet by executing query.
			this.cachedResultSet = executeQuery( );
			if ( this.cachedResultSet != null )
				cachedResultMetaData = this.cachedResultSet.getMetaData( );
		}
		
		return cachedResultMetaData;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IQuery#executeQuery()
	 */
	public IResultSet executeQuery( ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				Statement.class.getName( ),
				"executeQuery",
				"Statement.executeQuery( )" );
		assertNotNull( preStat );
		
		// If user has called getMetaData() and we executed the query as a result,
		// return the last result set to avoid unnecessary double execution
		if ( this.cachedResultSet != null )
		{
			IResultSet ret = this.cachedResultSet;
			this.cachedResultSet = null;	// Clear this so subsequent executeQuery should run it again
			return ret;
		}
		
		try
		{
			if ( maxrows >= 0 && !maxRowsUpToDate )
			{
				try
				{
					preStat.setMaxRows( maxrows );
				}
				catch ( SQLException e1 )
				{
					// assume this exception is caused by the drivers that do
					// not support "setMaxRows" method
				}
				maxRowsUpToDate = true;
			}
			/* redirect the call to JDBC preparedStatement.executeQuery() */
			return new ResultSet( this.preStat.executeQuery( ) );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.RESULTSET_CANNOT_RETURN , e );
		}
	}

	/*
	 * TODO: used by junit tests only;
	 * to be removed after update of tests
	 */
	public boolean execute( ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				Statement.class.getName( ),
				"execute",
				"Statement.execute( )" );
		assertNotNull( preStat );
		try
		{
			if ( maxrows >= 0 && !maxRowsUpToDate )
			{
				preStat.setMaxRows( maxrows );
				maxRowsUpToDate = true;
			}
			/* redirect the call to JDBC preparedStatement.execute() */
			return preStat.execute( );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.QUERY_EXECUTE_FAIL, e );
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IQuery#setInt(java.lang.String, int)
	 */
	public void setInt( String parameterName, int value ) throws OdaException
	{
		/* not supported */
		UnsupportedOperationException e = new UnsupportedOperationException( "No named Parameter supported." );
		logger.logp( java.util.logging.Level.FINE,
				Statement.class.getName( ),
				"setInt",
				"No named Parameter supported.",
				e );
		throw e;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IQuery#setInt(int, int)
	 */
	public void setInt( int parameterId, int value ) throws OdaException
	{
		assertNotNull( preStat );
		try
		{
			/* redirect the call to JDBC preparedStatement.setInt(int,int) */
			this.preStat.setInt( parameterId, value );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.PREPARESTATEMENT_CANNOT_SET_INT_VALUE,
					e );
		}
		catch ( RuntimeException e1 )
		{
			rethrowRunTimeException( e1, ERRMSG_SET_PARAMETER + parameterId );
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IQuery#setDouble(java.lang.String,
	 *      double)
	 */
	public void setDouble( String parameterName, double value )
			throws OdaException
	{
		/* not supported */
		UnsupportedOperationException e = new UnsupportedOperationException( "No named Parameter supported." );
		logger.logp( java.util.logging.Level.FINE,
				Statement.class.getName( ),
				"setDouble",
				"No named Parameter supported.",
				e );
		throw e;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IQuery#setDouble(int, double)
	 */
	public void setDouble( int parameterId, double value ) throws OdaException
	{
		assertNotNull( preStat );
		try
		{
			/* redirect the call to JDBC preparedStatement.setDouble(int,double) */
			this.preStat.setDouble( parameterId, value );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.PREPARESTATEMENT_CANNOT_SET_DUBLE_VALUE,
					e );
		}
		catch ( RuntimeException e1 )
		{
			rethrowRunTimeException( e1, ERRMSG_SET_PARAMETER + parameterId );
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IQuery#setBigDecimal(java.lang.String,
	 *      java.math.BigDecimal)
	 */
	public void setBigDecimal( String parameterName, BigDecimal value )
			throws OdaException
	{
		/* not supported */
		UnsupportedOperationException e = new UnsupportedOperationException( "No named Parameter supported." );
		logger.logp( java.util.logging.Level.FINE,
				Statement.class.getName( ),
				"setBigDecimal",
				"No named Parameter supported.",
				e );
		throw e;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IQuery#setBigDecimal(int,
	 *      java.math.BigDecimal)
	 */
	public void setBigDecimal( int parameterId, BigDecimal value )
			throws OdaException
	{
		assertNotNull( preStat );
		try
		{
			/*
			 * redirect the call to JDBC
			 * preparedStatement.setBigDecimal(int,BigDecimal)
			 */
			this.preStat.setBigDecimal( parameterId, value );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.PREPARESTATEMENT_CANNOT_SET_BIGDECIMAL_VALUE,
					e );
		}
		catch ( RuntimeException e1 )
		{
			rethrowRunTimeException( e1, ERRMSG_SET_PARAMETER + parameterId );
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IQuery#setString(java.lang.String,
	 *      java.lang.String)
	 */
	public void setString( String parameterName, String value )
			throws OdaException
	{
		/* not supported */
		UnsupportedOperationException e = new UnsupportedOperationException( "No named Parameter supported." );
		logger.logp( java.util.logging.Level.FINE,
				Statement.class.getName( ),
				"setString",
				"No named Parameter supported.",
				e );
		throw e;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IQuery#setString(int,
	 *      java.lang.String)
	 */
	public void setString( int parameterId, String value ) throws OdaException
	{
		assertNotNull( preStat );
		try
		{
			/* redirect the call to JDBC preparedStatement.setString(int,String) */
			this.preStat.setString( parameterId, value );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.PREPARESTATEMENT_CANNOT_SET_STRING_VALUE,
					e );
		}
		catch ( RuntimeException e1 )
		{
			rethrowRunTimeException( e1, ERRMSG_SET_PARAMETER + parameterId );
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IQuery#setDate(java.lang.String,
	 *      java.sql.Date)
	 */
	public void setDate( String parameterName, Date value ) throws OdaException
	{
		/* not supported */
		UnsupportedOperationException e = new UnsupportedOperationException( "No named Parameter supported." );
		logger.logp( java.util.logging.Level.FINE,
				Statement.class.getName( ),
				"setDate",
				"No named Parameter supported.",
				e );
		throw e;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IQuery#setDate(int, java.sql.Date)
	 */
	public void setDate( int parameterId, Date value ) throws OdaException
	{
		assertNotNull( preStat );
		try
		{
			/* redirect the call to JDBC preparedStatement.setDate(int,Date) */
			this.preStat.setDate( parameterId, value );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.PREPARESTATEMENT_CANNOT_SET_DATE_VALUE,
					e );
		}
		catch ( RuntimeException e1 )
		{
			rethrowRunTimeException( e1, ERRMSG_SET_PARAMETER + parameterId );
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IQuery#setTime(java.lang.String,
	 *      java.sql.Time)
	 */
	public void setTime( String parameterName, Time value ) throws OdaException
	{
		/* not supported */
		UnsupportedOperationException e = new UnsupportedOperationException( "No named Parameter supported." );
		logger.logp( java.util.logging.Level.FINE,
				Statement.class.getName( ),
				"setTime",
				"No named Parameter supported.",
				e );
		throw e;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IQuery#setTime(int, java.sql.Time)
	 */
	public void setTime( int parameterId, Time value ) throws OdaException
	{
		assertNotNull( preStat );
		try
		{
			/* redirect the call to JDBC preparedStatement.setTime(int,Time) */
			this.preStat.setTime( parameterId, value );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.PREPARESTATEMENT_CANNOT_SET_TIME_VALUE,
					e );
		}
		catch ( RuntimeException e1 )
		{
			rethrowRunTimeException( e1, ERRMSG_SET_PARAMETER + parameterId );
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IQuery#setTimestamp(java.lang.String,
	 *      java.sql.Timestamp)
	 */
	public void setTimestamp( String parameterName, Timestamp value )
			throws OdaException
	{
		/* not supported */
		UnsupportedOperationException e = new UnsupportedOperationException( "No named Parameter supported." );
		logger.logp( java.util.logging.Level.FINE,
				Statement.class.getName( ),
				"setTimestamp",
				"No named Parameter supported.",
				e );
		throw e;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IQuery#setTimestamp(int,
	 *      java.sql.Timestamp)
	 */
	public void setTimestamp( int parameterId, Timestamp value )
			throws OdaException
	{
		assertNotNull( preStat );
		try
		{
			/*
			 * redirect the call to JDBC
			 * preparedStatement.setTimestamp(int,Timestamp)
			 */
			this.preStat.setTimestamp( parameterId, value );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.PREPARESTATEMENT_CANNOT_SET_TIMESTAMP_VALUE,
					e );
		}
		catch ( RuntimeException e1 )
		{
			rethrowRunTimeException( e1, ERRMSG_SET_PARAMETER + parameterId );
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IQuery#findInParameter(java.lang.String)
	 */
	public int findInParameter( String parameterName ) throws OdaException
	{
		/* not supported */
		UnsupportedOperationException e = new UnsupportedOperationException( "No named Parameter supported." );
		logger.logp( java.util.logging.Level.FINE,
				Statement.class.getName( ),
				"findInParameter",
				"No named Parameter supported.",
				e );
		throw e;
	}


	/*
	 * @see org.eclipse.datatools.connectivity.IQuery#getParameterMetaData()
	 */
	public IParameterMetaData getParameterMetaData( ) throws OdaException
	{
		assertNotNull( preStat );
		try
		{
			/* redirect the call to JDBC preparedStatement.getParameterMetaData */
			return new ParameterMetaData( this.preStat.getParameterMetaData( ) );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.PREPARESTATEMENT_PARAMETER_METADATA_CANNOT_GET,
					e );
		}
		catch ( Throwable e )
		{
			// in the case of Oracle 8.1.7, AbstractMethodError will be thrown
			return null;
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IQuery#setSortSpec(org.eclipse.datatools.connectivity.SortSpec)
	 */
	public void setSortSpec( SortSpec sortBy ) throws OdaException
	{
		/* not supported */
		UnsupportedOperationException e = new UnsupportedOperationException( "setSortSpec is not supported." );
		logger.logp( java.util.logging.Level.FINE,
				Statement.class.getName( ),
				"setSortSpec",
				"setSortSpec is not supported.",
				e );
		throw e;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IQuery#getSortSpec()
	 */
	public SortSpec getSortSpec( ) throws OdaException
	{
		/* not supported */
		UnsupportedOperationException e = new UnsupportedOperationException( "setSortSpec is not supported." );
		logger.logp( java.util.logging.Level.FINE,
				Statement.class.getName( ),
				"getSortSpec",
				"getSortSpec is not supported.",
				e );
		throw e;	
	}

	public void clearInParameters() throws OdaException
	{
		assertNotNull( preStat );
		try
		{
			preStat.clearParameters();
		}
		catch( SQLException ex )
		{
			throw new JDBCException( ResourceConstants.PREPARESTATEMENT_CLEAR_PARAMETER_ERROR , ex );
		}
	}
	
	/**
	 * Converts a RuntimeException which occurred in the setting parameter value
	 * of a ROM script to an OdaException, and rethrows such exception. This
	 * method never returns.
	 */
	private static void rethrowRunTimeException( RuntimeException e, String msg )
			throws OdaException
	{
		OdaException odaException = new OdaException( msg );
		odaException.initCause( e );
		logger.logp( java.util.logging.Level.FINE,
				Statement.class.getName( ),
				"rethrowRunTimeException",
				msg,
				odaException );
		throw odaException;
	}

}
