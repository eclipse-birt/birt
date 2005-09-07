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
import java.sql.CallableStatement;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.data.oda.IAdvancedQuery;
import org.eclipse.birt.data.oda.IParameterMetaData;
import org.eclipse.birt.data.oda.IParameterRowSet;
import org.eclipse.birt.data.oda.IResultSet;
import org.eclipse.birt.data.oda.IResultSetMetaData;
import org.eclipse.birt.data.oda.OdaException;
import org.eclipse.birt.data.oda.SortSpec;
import org.eclipse.birt.report.data.oda.i18n.ResourceConstants;

/**
 * 
 * The class implements the org.eclipse.birt.data.oda.IAdvancedQuery interface.
 *  
 */
 
public class CallStatement implements IAdvancedQuery
{

	/** the JDBC callableStatement object */
	private CallableStatement callStat;

	/** the JDBC Connection object */
	private java.sql.Connection conn;

	/** remember the max row value, default 0. */
	private int maxrows;

	/** indicates if need to call JDBC setMaxRows before execute statement */
	private boolean maxRowsUpToDate = false;

	/** Error message for ERRMSG_SET_PARAMETER */
	private final static String ERRMSG_SET_PARAMETER = "Error setting value for SQL parameter #";

	private static Logger logger = Logger.getLogger( CallStatement.class.getName( ) );

	private boolean isCallabeStatement = true;

	private String procedureName;

	private String[] resultSetNames;

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
	 * Constructor CallableStatement(java.sql.Connection connection) use JDBC's
	 * Connection to construct it.
	 *  
	 */
	public CallStatement( java.sql.Connection connection ) throws OdaException
	{
		if ( connection != null )

		{
			/* record down the JDBC Connection object */
			this.callStat = null;
			this.conn = connection;
			maxrows = 0;
		}
		else
		{
			throw new JDBCException( ResourceConstants.DRIVER_NO_CONNECTION,
					ResourceConstants.ERROR_NO_CONNECTION );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#prepare(java.lang.String)
	 */
	public void prepare( String command ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				CallStatement.class.getName( ),
				"prepare",
				"CallableStatement.prepare( \"" + command + "\" )" );

		try
		{
			if ( command == null )
			{
				logger.logp( java.util.logging.Level.FINE,
						CallStatement.class.getName( ),
						"prepare",
						"Query text can not be null." );
				throw new OdaException( "Query text can not be null." );
			}
			/*
			 * call the JDBC Connection.prepareCall(String) method to get the
			 * callableStatement
			 */
			procedureName = getProcedureName( command );
			command = "{" + command + "}";
			this.callStat = conn.prepareCall( formatQueryText( command ) );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.STATEMENT_CANNOT_PREPARE,
					e );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#setProperty(java.lang.String,
	 *      java.lang.String)
	 */
	public void setProperty( String name, String value ) throws OdaException
	{
		if ( name == null )
			throw new NullPointerException( "name is null" );

		if ( name.equals( "queryTimeOut" ) )
		{
			// Ignore null or empty value
			if ( value != null && value.length( ) > 0 )
			{
				try
				{
					// Be forgiving if a floating point gets passed in - can
					// happen
					// when Javascript gets involved in calculating the property
					// value
					double secs = Double.parseDouble( value );
					this.callStat.setQueryTimeout( (int) secs );
				}
				catch ( SQLException e )
				{
					// This is not an essential property; log and ignore error
					// if driver doesn't
					// support query timeout
					logger.log( Level.FINE,
							"CallStatement.setQueryTimeout failed",
							e );
				}
			}
		}
		else
		{
			// unsupported query properties
			OdaException e = new OdaException( "Unsupported query property: "
					+ name );
			logger.logp( java.util.logging.Level.FINE,
					CallStatement.class.getName( ),
					"setProperty",
					"Unsupported property",
					e );
			throw e;
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#close()
	 */
	public void close( ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				CallStatement.class.getName( ),
				"close",
				"CallStatement.close( )" );
		try
		{
			if ( callStat != null )
			{
				this.callStat.close( );
			}
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.PREPAREDSTATEMENT_CANNOT_CLOSE,
					e );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#setMaxRows(int)
	 */
	public void setMaxRows( int max )
	{
		logger.logp( java.util.logging.Level.FINE,
				CallStatement.class.getName( ),
				"setMaxRows",
				"CallStatement.setMaxRows( " + max + " )" );
		if ( max != maxrows && max >= 0 )
		{
			maxrows = max;
			maxRowsUpToDate = false;
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#getMaxRows()
	 */
	public int getMaxRows( )
	{
		logger.logp( java.util.logging.Level.FINE,
				CallStatement.class.getName( ),
				"getMaxRows",
				"CallStatement.getMaxRows( )" );
		return this.maxrows;

	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#getMetaData()
	 */
	public IResultSetMetaData getMetaData( ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				CallStatement.class.getName( ),
				"getMetaData",
				"CallableStatement.getMetaData( )" );

		java.sql.ResultSetMetaData resultmd = null;
		try
		{
			assertNotNull( callStat );
			resultmd = callStat.getMetaData( );
		}
		catch ( NullPointerException e )
		{
			resultmd = null;
		}
		catch ( SQLException e )
		{
			// For some database, meta data of table can not be obtained
			// in prepared time. To solve this problem, query execution is
			// required to be executed first.
		}
		IResultSetMetaData pstmtResultMetaData = null;
		if ( resultmd != null )
		{
			pstmtResultMetaData = new ResultSetMetaData( resultmd );
		}
		else
		{
			// If Jdbc driver throw an SQLexception or return null, when we get
			// MetaData from ResultSet
			IResultSet mdRs = null;
			try
			{
				 mdRs = executeQuery( );
			}
			catch ( OdaException e )
			{
				 mdRs = null;
			}
			
			try
			{
				if ( mdRs != null )
					pstmtResultMetaData = mdRs.getMetaData( );
				else
					pstmtResultMetaData = new SPResultSetMetaData( null );
			}
			catch ( OdaException e )
			{
				//					throw new JDBCException(
				// ResourceConstants.DRIVER_NO_RESULTSET,
				//							ResourceConstants.ERROR_NO_RESULTSET );
			}
		}
		return pstmtResultMetaData;
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#executeQuery()
	 */
	public IResultSet executeQuery( ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				CallStatement.class.getName( ),
				"executeQuery",
				"CallableStatement.executeQuery( )" );
		try
		{
			if ( !maxRowsUpToDate )
			{
				try
				{
					assertNotNull( callStat );
					callStat.setMaxRows( maxrows );
				}
				catch ( SQLException e1 )
				{
					//assume this exception is caused by the drivers that do
					//not support "setMaxRows" method
				}
				maxRowsUpToDate = true;
			}
			/* redirect the call to JDBC callableStatement.executeQuery() */
			//get procedure parameter metadata and register the output
			// parameter
			IParameterMetaData paramInfo = getParameterMetaData( );
			int count = paramInfo.getParameterCount( );
			for ( int i = 1; i <= count; i++ )
			{
				if ( paramInfo.getParameterMode( i ) == IParameterMetaData.parameterModeInOut
						|| paramInfo.getParameterMode( i ) == IParameterMetaData.parameterModeOut )
					registerOutParameter( i, paramInfo.getParameterType( i ) );
			}
			this.callStat.execute( );
			if ( this.callStat.getResultSet( ) != null )
				return new ResultSet( this.callStat.getResultSet( ) );
			else
				return new SPResultSet( null );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.RESULTSET_CANNOT_RETURN,
					e );
		}
	}

	/**
	 * 
	 * @param position
	 * @param type
	 * @throws OdaException
	 */
	void registerOutParameter( int position, int type ) throws OdaException
	{
		try
		{
			assertNotNull( callStat );
			callStat.registerOutParameter( position, type );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.QUERY_EXECUTE_FAIL, e );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#execute()
	 */
	public boolean execute( ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				CallStatement.class.getName( ),
				"execute",
				"CallableStatement.execute( )" );
		try
		{
			{
				assertNotNull( callStat );
				if ( !maxRowsUpToDate )
				{
					callStat.setMaxRows( maxrows );
					maxRowsUpToDate = true;
				}
				/* redirect the call to JDBC callableStatement.execute() */
				return callStat.execute( );
			}
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.QUERY_EXECUTE_FAIL, e );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#setInt(java.lang.String,
	 *      int)
	 */
	public void setInt( String parameterName, int value ) throws OdaException
	{
		try
		{
			/* redirect the call to JDBC callableStatement.setInt(int,int) */
			assertNotNull( callStat );
			this.callStat.setInt( parameterName, value );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.PREPARESTATEMENT_CANNOT_SET_INT_VALUE,
					e );
		}
		catch ( RuntimeException e1 )
		{
			rethrowRunTimeException( e1, ERRMSG_SET_PARAMETER + parameterName );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#setInt(int, int)
	 */
	public void setInt( int parameterId, int value ) throws OdaException
	{
		try
		{
			/* redirect the call to JDBC callableStatement.setInt(int,int) */
			assertNotNull( callStat );
			this.callStat.setInt( parameterId, value );
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
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#setDouble(java.lang.String,
	 *      double)
	 */
	public void setDouble( String parameterName, double value )
			throws OdaException
	{
		try
		{
			/* redirect the call to JDBC callableStatement.setDouble(int,double) */
			assertNotNull( callStat );
			this.callStat.setDouble( parameterName, value );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.PREPARESTATEMENT_CANNOT_SET_DUBLE_VALUE,
					e );
		}
		catch ( RuntimeException e1 )
		{
			rethrowRunTimeException( e1, ERRMSG_SET_PARAMETER + parameterName );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#setDouble(int, double)
	 */
	public void setDouble( int parameterId, double value ) throws OdaException
	{
		try
		{
			/* redirect the call to JDBC callableStatement.setDouble(int,double) */
			assertNotNull( callStat );
			this.callStat.setDouble( parameterId, value );
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
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#setBigDecimal(java.lang.String,
	 *      java.math.BigDecimal)
	 */
	public void setBigDecimal( String parameterName, BigDecimal value )
			throws OdaException
	{
		try
		{
			/*
			 * redirect the call to JDBC
			 * callableStatement.setBigDecimal(int,BigDecimal)
			 */
			assertNotNull( callStat );
			this.callStat.setBigDecimal( parameterName, value );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.PREPARESTATEMENT_CANNOT_SET_BIGDECIMAL_VALUE,
					e );
		}
		catch ( RuntimeException e1 )
		{
			rethrowRunTimeException( e1, ERRMSG_SET_PARAMETER + parameterName );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#setBigDecimal(int,
	 *      java.math.BigDecimal)
	 */
	public void setBigDecimal( int parameterId, BigDecimal value )
			throws OdaException
	{
		try
		{
			/*
			 * redirect the call to JDBC
			 * callableStatement.setBigDecimal(int,BigDecimal)
			 */
			assertNotNull( callStat );
			this.callStat.setBigDecimal( parameterId, value );
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
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#setString(java.lang.String,
	 *      java.lang.String)
	 */
	public void setString( String parameterName, String value )
			throws OdaException
	{
		try
		{
			/* redirect the call to JDBC CallStatement.setString(int,String) */
			assertNotNull( callStat );
			this.callStat.setString( parameterName, value );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.PREPARESTATEMENT_CANNOT_SET_STRING_VALUE,
					e );
		}
		catch ( RuntimeException e1 )
		{
			rethrowRunTimeException( e1, ERRMSG_SET_PARAMETER + parameterName );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#setString(int,
	 *      java.lang.String)
	 */
	public void setString( int parameterId, String value ) throws OdaException
	{
		try
		{
			/* redirect the call to JDBC CallStatement.setString(int,String) */
			assertNotNull( callStat );
			this.callStat.setString( parameterId, value );
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
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#setDate(java.lang.String,
	 *      java.sql.Date)
	 */
	public void setDate( String parameterName, Date value ) throws OdaException
	{
		try
		{
			/* redirect the call to JDBC callableStatement.setDate(int,Date) */
			assertNotNull( callStat );
			this.callStat.setDate( parameterName, value );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.PREPARESTATEMENT_CANNOT_SET_DATE_VALUE,
					e );
		}
		catch ( RuntimeException e1 )
		{
			rethrowRunTimeException( e1, ERRMSG_SET_PARAMETER + parameterName );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#setDate(int, java.sql.Date)
	 */
	public void setDate( int parameterId, Date value ) throws OdaException
	{
		try
		{
			/* redirect the call to JDBC callableStatement.setDate(int,Date) */
			assertNotNull( callStat );
			this.callStat.setDate( parameterId, value );
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
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#setTime(java.lang.String,
	 *      java.sql.Time)
	 */
	public void setTime( String parameterName, Time value ) throws OdaException
	{
		try
		{
			/* redirect the call to JDBC callableStatement.setTime(int,Time) */
			assertNotNull( callStat );
			this.callStat.setTime( parameterName, value );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.PREPARESTATEMENT_CANNOT_SET_TIME_VALUE,
					e );
		}
		catch ( RuntimeException e1 )
		{
			rethrowRunTimeException( e1, ERRMSG_SET_PARAMETER + parameterName );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#setTime(int, java.sql.Time)
	 */
	public void setTime( int parameterId, Time value ) throws OdaException
	{
		try
		{
			/* redirect the call to JDBC callableStatement.setTime(int,Time) */
			assertNotNull( callStat );
			this.callStat.setTime( parameterId, value );
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
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#setTimestamp(java.lang.String,
	 *      java.sql.Timestamp)
	 */
	public void setTimestamp( String parameterName, Timestamp value )
			throws OdaException
	{
		try
		{
			/*
			 * redirect the call to JDBC
			 * callableStatement.setTimestamp(int,Timestamp)
			 */
			assertNotNull( callStat );
			this.callStat.setTimestamp( parameterName, value );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.PREPARESTATEMENT_CANNOT_SET_TIMESTAMP_VALUE,
					e );
		}
		catch ( RuntimeException e1 )
		{
			rethrowRunTimeException( e1, ERRMSG_SET_PARAMETER + parameterName );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#setTimestamp(int,
	 *      java.sql.Timestamp)
	 */
	public void setTimestamp( int parameterId, Timestamp value )
			throws OdaException
	{
		try
		{
			/*
			 * redirect the call to JDBC
			 * callableStatement.setTimestamp(int,Timestamp)
			 */
			assertNotNull( callStat );
			this.callStat.setTimestamp( parameterId, value );
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
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#setNewRow(String)
	 */
	public IParameterRowSet setNewRow( String parameterName )
			throws OdaException
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#setNewRow(int)
	 */
	public IParameterRowSet setNewRow( int parameterId ) throws OdaException
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#setNewRowSet(String)
	 */
	public IParameterRowSet setNewRowSet( String parameterName )
			throws OdaException
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#setNewRowSet(int)
	 */
	public IParameterRowSet setNewRowSet( int parameterId ) throws OdaException
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#getInt(String)
	 */
	public int getInt( String parameterName ) throws OdaException
	{
		try
		{
			return callStat.getInt( parameterName );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.RESULTSET_CANNOT_GET_INT_VALUE,
					e );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#getInt(int)
	 */
	public int getInt( int parameterId ) throws OdaException
	{
		try
		{
			return callStat.getInt( parameterId );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.RESULTSET_CANNOT_GET_INT_VALUE,
					e );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#getDouble(java.lang.String)
	 */
	public double getDouble( String parameterName ) throws OdaException
	{
		try
		{
			return callStat.getDouble( parameterName );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.RESULTSET_CANNOT_GET_DOUBLE_VALUE,
					e );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#getDouble(int)
	 */
	public double getDouble( int parameterId ) throws OdaException
	{
		try
		{
			return callStat.getDouble( parameterId );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.RESULTSET_CANNOT_GET_DOUBLE_VALUE,
					e );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#getBigDecimal(java.lang.String)
	 */
	public BigDecimal getBigDecimal( String parameterName ) throws OdaException
	{
		try
		{
			return callStat.getBigDecimal( parameterName );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.RESULTSET_CANNOT_GET_BIGDECIMAL_VALUE,
					e );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#getBigDecimal(int)
	 */
	public BigDecimal getBigDecimal( int parameterId ) throws OdaException
	{
		try
		{
			return callStat.getBigDecimal( parameterId );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.RESULTSET_CANNOT_GET_BIGDECIMAL_VALUE,
					e );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#getString(java.lang.String)
	 */
	public String getString( String parameterName ) throws OdaException
	{
		try
		{
			return callStat.getString( parameterName );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.RESULTSET_CANNOT_GET_STRING_VALUE,
					e );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#getString(int)
	 */
	public String getString( int parameterId ) throws OdaException
	{
		try
		{
			return callStat.getString( parameterId );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.RESULTSET_CANNOT_GET_STRING_VALUE,
					e );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#getDate(java.lang.String)
	 */
	public Date getDate( String parameterName ) throws OdaException
	{
		try
		{
			return callStat.getDate( parameterName );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.RESULTSET_CANNOT_GET_DATE_VALUE,
					e );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#getDate(int)
	 */
	public Date getDate( int parameterId ) throws OdaException
	{
		try
		{
			return callStat.getDate( parameterId );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.RESULTSET_CANNOT_GET_DATE_VALUE,
					e );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#getTime(java.lang.String)
	 */
	public Time getTime( String parameterName ) throws OdaException
	{
		try
		{
			return callStat.getTime( parameterName );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.RESULTSET_CANNOT_GET_TIME_VALUE,
					e );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#getTime(int)
	 */
	public Time getTime( int parameterId ) throws OdaException
	{
		try
		{
			return callStat.getTime( parameterId );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.RESULTSET_CANNOT_GET_TIME_VALUE,
					e );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#getTimestamp(java.lang.String)
	 */
	public Timestamp getTimestamp( String parameterName ) throws OdaException
	{
		try
		{
			return callStat.getTimestamp( parameterName );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.RESULTSET_CANNOT_GET_TIMESTAMP_VALUE,
					e );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#getTimestamp(int)
	 */
	public Timestamp getTimestamp( int parameterId ) throws OdaException
	{
		try
		{
			return callStat.getTimestamp( parameterId );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.RESULTSET_CANNOT_GET_TIMESTAMP_VALUE,
					e );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#getRow(java.lang.String)
	 */
	public IParameterRowSet getRow( String parameterName ) throws OdaException
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#getResultSet()
	 */
	public IResultSet getResultSet( ) throws OdaException
	{
		try
		{
			return new ResultSet( callStat.getResultSet( ) );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.RESULTSET_CANNOT_GET, e );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#getResultSet(String)
	 */
	public IResultSet getResultSet( String resultSetName ) throws OdaException
	{
		try
		{
			return new ResultSet( callStat.getResultSet( ) );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.RESULTSET_CANNOT_GET, e );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#getMoreResults()
	 */
	public boolean getMoreResults( ) throws OdaException
	{
		try
		{
			return callStat.getMoreResults( );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.RESULTSET_CANNOT_GET, e );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#getRow(int)
	 */
	public IParameterRowSet getRow( int parameterId ) throws OdaException
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#getMetaDataOf(java.lang.String)
	 */
	public IResultSetMetaData getMetaDataOf( String resultSetName )
			throws OdaException
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.oda.IQuery#findInParameter(java.lang.String)
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
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#findOutParameter(java.lang.String)
	 */
	public int findOutParameter( String parameterName ) throws OdaException
	{
		/* not supported */
		UnsupportedOperationException e = new UnsupportedOperationException( "No named Parameter supported." );
		logger.logp( java.util.logging.Level.FINE,
				Statement.class.getName( ),
				"findOutParameter",
				"No named Parameter supported.",
				e );
		throw e;
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#getParameterMetaData()
	 */
	public IParameterMetaData getParameterMetaData( ) throws OdaException
	{
		/* redirect the call to JDBC callableStatement.getParameterMetaData */
		assertNotNull( callStat );
		return new SPParameterMetaData( getCallableParamMetaData( ) );
	}

	/**
	 * get parameter metadata from database matadata
	 */
	private java.util.List getCallableParamMetaData( )
	{
		java.util.List paramMetaDataList = new ArrayList( );
		try
		{
			DatabaseMetaData metaData = conn.getMetaData( );
			String cataLog = conn.getCatalog( );
			ArrayList schemaList = createSchemaList( metaData.getSchemas( ) );

			if ( schemaList != null && schemaList.size( ) > 0 )
			{
				for ( int i = 0; i < schemaList.size( ); i++ )
				{
					java.sql.ResultSet rs = metaData.getProcedureColumns( cataLog,
							schemaList.get( i ).toString( ),
							procedureName,
							null );
					while ( rs.next( ) )
					{
						ParameterDefn p = new ParameterDefn( );
						p.setParamName( rs.getString( "COLUMN_NAME" ) );
						p.setParamInOutType( rs.getInt( "COLUMN_TYPE" ) );
						p.setParamType( rs.getInt( "DATA_TYPE" ) );
						p.setParamTypeName( rs.getString( "TYPE_NAME" ) );
						p.setPrecision( rs.getInt( "PRECISION" ) );
						p.setScale( rs.getInt( "SCALE" ) );
						p.setIsNullable( rs.getInt( "NULLABLE" ) );
						if ( p.getParamInOutType( ) != 5 )
							paramMetaDataList.add( p );
					}
				}
			}
		}
		catch ( SQLException e )
		{
		}
		return paramMetaDataList;
	}

	/**
	 * @param schemaRs:
	 *            The ResultSet containing the List of schema
	 * @return A List of schema names
	 */
	private ArrayList createSchemaList( java.sql.ResultSet schemaRs )
	{
		if ( schemaRs == null )
		{
			return null;
		}

		ArrayList schemas = new ArrayList( );
		ArrayList allSchemas = new ArrayList( );
		try
		{
			while ( schemaRs.next( ) )
			{
				allSchemas.add( schemaRs.getString( "TABLE_SCHEM" ) );
			}

			ResultSet rs = null;
			Iterator it = allSchemas.iterator( );

			while ( it.hasNext( ) )
			{
				String schema = it.next( ).toString( );
				schemas.add( schema );//$NON-NLS-1$					
			}
		}
		catch ( SQLException e )
		{
			e.printStackTrace( );
		}

		return schemas;

	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#setSortSpec(org.eclipse.birt.data.oda.SortSpec)
	 */
	public void setSortSpec( SortSpec sortBy ) throws OdaException
	{
		setSortSpec( null, sortBy );
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#setSortSpec(java.lang.String,
	 *      org.eclipse.birt.data.oda.SortSpec)
	 */
	public void setSortSpec( String resultSetName, SortSpec sortBy )
			throws OdaException
	{
		/* not supported */
		UnsupportedOperationException e = new UnsupportedOperationException( "setSortSpec is not supported." );
		logger.logp( java.util.logging.Level.FINE,
				CallStatement.class.getName( ),
				"setSortSpec",
				"setSortSpec is not supported.",
				e );
		throw e;
	}

	/*
	 * @see org.eclipse.birt.data.oda.IQuery#getSortSpec()
	 */
	public SortSpec getSortSpec( ) throws OdaException
	{
		UnsupportedOperationException e = new UnsupportedOperationException( "setSortSpec is not supported." );
		logger.logp( java.util.logging.Level.FINE,
				CallStatement.class.getName( ),
				"getSortSpec",
				"getSortSpec is not supported.",
				e );
		throw e;
	}

	/*
	 * @see org.eclipse.birt.data.oda.IQuery#getSortSpec()
	 */
	public SortSpec getSortSpec( String resultSetName ) throws OdaException
	{
		/* not supported */
		UnsupportedOperationException e = new UnsupportedOperationException( "setSortSpec is not supported." );
		logger.logp( java.util.logging.Level.FINE,
				CallStatement.class.getName( ),
				"getSortSpec",
				"getSortSpec is not supported.",
				e );
		throw e;
	}

	/*
	 * @see org.eclipse.birt.data.oda.IQuery#clearInParameters()
	 */
	public void clearInParameters( ) throws OdaException
	{
		try
		{
			assertNotNull( callStat );
			callStat.clearParameters( );
		}
		catch ( SQLException ex )
		{
			throw new JDBCException( ResourceConstants.PREPARESTATEMENT_CLEAR_PARAMETER_ERROR,
					ex );
		}
	}

	/**
	 * Format Query Text Simply replace all LF(10) with " "
	 * 
	 * @param source
	 * @return
	 */
	private String formatQueryText( String source )
	{
		return source.replaceAll( "\n", " " );
	}

	/**
	 * get procedureName
	 * 
	 * @param text
	 * @return
	 */
	private String getProcedureName( String text )
	{
		assert text != null;
		String name;
		int start = text.toLowerCase( ).indexOf( "call " ) + 4;
		int end = text.indexOf( "(", start );
		if ( end < start )
			end = text.length( );
		name = text.substring( start, end ).trim( );
		name = escapeIdentifier( name );
		if ( name.indexOf( ";" ) > 0 )
			name = name.substring( 0, name.indexOf( ";" ) );
		return name;
	}

	/**
	 * escape the double quote & bracket
	 * @param text
	 * @return
	 */
	private String escapeIdentifier( String text )
	{
		if ( ( text.startsWith( "\"" ) && text.endsWith( "\"" ) )
				|| ( text.startsWith( "[" ) && text.endsWith( "]" ) ) )
			return text.substring( 1, text.length( ) - 1 );
		else
			return text;
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
				CallStatement.class.getName( ),
				"rethrowRunTimeException",
				msg,
				odaException );
		throw odaException;
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#wasNull()
	 */
	public boolean wasNull( ) throws OdaException
	{
		return false;
	}

	/*
	 * @see org.eclipse.birt.data.oda.IAdvancedQuery#getResultSetNames()
	 */
	public String[] getResultSetNames( ) throws OdaException
	{
		return resultSetNames;
	}
}