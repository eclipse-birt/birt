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
import java.sql.ParameterMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.data.oda.i18n.ResourceConstants;
import org.eclipse.datatools.connectivity.oda.IAdvancedQuery;
import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IParameterRowSet;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.SortSpec;

/**
 * 
 * The class implements the org.eclipse.birt.data.oda.IAdvancedQuery interface.
 *  
 */
 
public class CallStatement implements IAdvancedQuery
{

	/** the JDBC callableStatement object */
	protected CallableStatement callStat;

	/** the JDBC Connection object */
	protected java.sql.Connection conn;

	/** remember the max row value, default 0. */
	protected int maxrows;

	/** indicates if need to call JDBC setMaxRows before execute statement */
	protected boolean maxRowsUpToDate = false;
	
	/** utility object to get position of parameter */
	private SPParameterPositionUtil paramUtil;
	
	/** Error message for ERRMSG_SET_PARAMETER */
	private final static String ERRMSG_SET_PARAMETER = "Error setting value for SQL parameter #";

	private static Logger logger = Logger.getLogger( CallStatement.class.getName( ) );

	//private boolean isCallabeStatement = true;

	protected String procedureName;

	protected String[] resultSetNames;

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
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#prepare(java.lang.String)
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
			String queryText = SQLFormatter.formatQueryText( command );
			this.callStat = conn.prepareCall( queryText );
			paramUtil = new SPParameterPositionUtil( queryText, '@' );
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
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setProperty(java.lang.String, java.lang.String)
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
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#close()
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
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setMaxRows(int)
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
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getMaxRows()
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
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getMetaData()
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
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#executeQuery()
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
			registerOutputParameter( );
		
			/* redirect the call to JDBC callableStatement.executeQuery() */
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
	 * get paremeter metadata for callableStatement, if metadata is null or data
	 * mode is unknown or SQLException is thrown, register output parameter on
	 * DatabaseMetadata, else register output paremeter on statement's metadata.
	 * 
	 * @throws OdaException
	 */
	private void registerOutputParameter( ) throws OdaException
	{
		ParameterMetaData metaData = null;
		try
		{
			metaData = this.callStat.getParameterMetaData( );
		}
		catch ( SQLException e )
		{
			metaData = null;
		}
		try
		{
			if ( metaData != null )
			{
				for ( int index = 1; index <= metaData.getParameterCount( ); index++ )
				{
					if ( metaData.getParameterMode( index ) == ParameterMetaData.parameterModeUnknown )
					{
						registerOnDbMetaData( );
						break;
					}
					else if ( metaData.getParameterMode( index ) == java.sql.ParameterMetaData.parameterModeOut
							|| metaData.getParameterMode( index ) == java.sql.ParameterMetaData.parameterModeInOut )
						registerOutParameter( index,
								metaData.getParameterType( index ) );
				}
			}
			else
				registerOnDbMetaData( );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.QUERY_EXECUTE_FAIL, e );
		}
	}

	/**
	 * register output parameter based on database metadata
	 * 
	 * @throws OdaException
	 */
	private void registerOnDbMetaData( ) throws OdaException
	{
		IParameterMetaData paramInfo = getParameterMetaData( );
		if ( !paramUtil.hasNonDefaultParameter( ) )
		{

			int count = paramInfo.getParameterCount( );
			for ( int i = 1; i <= count; i++ )
			{
				if ( paramInfo.getParameterMode( i ) == IParameterMetaData.parameterModeInOut
						|| paramInfo.getParameterMode( i ) == IParameterMetaData.parameterModeOut )
					registerOutParameter( i, paramInfo.getParameterType( i ) );
			}
		}
		else
		{
			int[] positionArray = paramUtil.getParameterPositions( );
			for ( int index = 0; index < positionArray.length; index++ )
			{
				int position = positionArray[index];
				if ( position <= paramInfo.getParameterCount( )
						&& ( paramInfo.getParameterMode( position ) == IParameterMetaData.parameterModeInOut || paramInfo.getParameterMode( position ) == IParameterMetaData.parameterModeOut ) )
					registerOutParameter( index + 1,
							paramInfo.getParameterType( position ) );
			}
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
		assertNotNull( callStat );
		try
		{
			callStat.registerOutParameter( position, type );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.QUERY_EXECUTE_FAIL, e );
		}
	}
	
	/**
	 * 
	 * @param position
	 * @param type
	 * @throws OdaException
	 */
	void registerOutParameter( String name, int type ) throws OdaException
	{
		assertNotNull( callStat );
		try
		{
			callStat.registerOutParameter( name, type );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.QUERY_EXECUTE_FAIL, e );
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#execute()
	 */
	public boolean execute( ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				CallStatement.class.getName( ),
				"execute",
				"CallableStatement.execute( )" );
		assertNotNull( callStat );
		try
		{
			{
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
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setInt(java.lang.String, int)
	 */
	public void setInt( String parameterName, int value ) throws OdaException
	{
		assertNotNull( callStat );
		try
		{
			/* redirect the call to JDBC callableStatement.setInt(int,int) */
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
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setInt(int, int)
	 */
	public void setInt( int parameterId, int value ) throws OdaException
	{
		assertNotNull( callStat );
		try
		{
			/* redirect the call to JDBC callableStatement.setInt(int,int) */
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
	 *  @see org.eclipse.datatools.connectivity.oda.IQuery#setDouble(java.lang.String, double)
	 */
	public void setDouble( String parameterName, double value )
			throws OdaException
	{
		assertNotNull( callStat );
		try
		{
			/* redirect the call to JDBC callableStatement.setDouble(int,double) */
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
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDouble(int, double)
	 */
	public void setDouble( int parameterId, double value ) throws OdaException
	{
		assertNotNull( callStat );
		try
		{
			/* redirect the call to JDBC callableStatement.setDouble(int,double) */
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
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setBigDecimal(java.lang.String, java.math.BigDecimal)
	 */
	public void setBigDecimal( String parameterName, BigDecimal value )
			throws OdaException
	{
		assertNotNull( callStat );
		try
		{
			/*
			 * redirect the call to JDBC
			 * callableStatement.setBigDecimal(int,BigDecimal)
			 */
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
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setBigDecimal(int, java.math.BigDecimal)
	 */
	public void setBigDecimal( int parameterId, BigDecimal value )
			throws OdaException
	{
		assertNotNull( callStat );
		try
		{
			/*
			 * redirect the call to JDBC
			 * callableStatement.setBigDecimal(int,BigDecimal)
			 */
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
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setString(java.lang.String, java.lang.String)
	 */
	public void setString( String parameterName, String value )
			throws OdaException
	{
		assertNotNull( callStat );
		try
		{
			/* redirect the call to JDBC CallStatement.setString(int,String) */
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
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setString(int, java.lang.String)
	 */
	public void setString( int parameterId, String value ) throws OdaException
	{
		assertNotNull( callStat );
		try
		{
			/* redirect the call to JDBC CallStatement.setString(int,String) */
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
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDate(java.lang.String, java.sql.Date)
	 */
	public void setDate( String parameterName, Date value ) throws OdaException
	{
		assertNotNull( callStat );
		try
		{
			/* redirect the call to JDBC callableStatement.setDate(int,Date) */
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
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDate(int, java.sql.Date)
	 */
	public void setDate( int parameterId, Date value ) throws OdaException
	{
		assertNotNull( callStat );
		try
		{
			/* redirect the call to JDBC callableStatement.setDate(int,Date) */
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
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTime(java.lang.String, java.sql.Time)
	 */
	public void setTime( String parameterName, Time value ) throws OdaException
	{
		assertNotNull( callStat );
		try
		{
			/* redirect the call to JDBC callableStatement.setTime(int,Time) */
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
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTime(int, java.sql.Time)
	 */
	public void setTime( int parameterId, Time value ) throws OdaException
	{
		assertNotNull( callStat );
		try
		{
			/* redirect the call to JDBC callableStatement.setTime(int,Time) */
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
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTimestamp(java.lang.String, java.sql.Timestamp)
	 */
	public void setTimestamp( String parameterName, Timestamp value )
			throws OdaException
	{
		assertNotNull( callStat );
		try
		{
			/*
			 * redirect the call to JDBC
			 * callableStatement.setTimestamp(int,Timestamp)
			 */
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
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTimestamp(int, java.sql.Timestamp)
	 */
	public void setTimestamp( int parameterId, Timestamp value )
			throws OdaException
	{
		assertNotNull( callStat );
		try
		{
			/*
			 * redirect the call to JDBC
			 * callableStatement.setTimestamp(int,Timestamp)
			 */
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
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#setNewRow(java.lang.String)
	 */
	public IParameterRowSet setNewRow( String parameterName )
			throws OdaException
	{
		return null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#setNewRow(int)
	 */
	public IParameterRowSet setNewRow( int parameterId ) throws OdaException
	{
		return null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#setNewRowSet(java.lang.String)
	 */
	public IParameterRowSet setNewRowSet( String parameterName )
			throws OdaException
	{
		return null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#setNewRowSet(int)
	 */
	public IParameterRowSet setNewRowSet( int parameterId ) throws OdaException
	{
		return null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getInt(java.lang.String)
	 */
	public int getInt( String parameterName ) throws OdaException
	{
		assertNotNull( callStat );
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
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getInt(int)
	 */
	public int getInt( int parameterId ) throws OdaException
	{
		assertNotNull( callStat );
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
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getDouble(java.lang.String)
	 */
	public double getDouble( String parameterName ) throws OdaException
	{
		assertNotNull( callStat );
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
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getDouble(int)
	 */
	public double getDouble( int parameterId ) throws OdaException
	{
		assertNotNull( callStat );
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
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getBigDecimal(java.lang.String)
	 */
	public BigDecimal getBigDecimal( String parameterName ) throws OdaException
	{
		assertNotNull( callStat );
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
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getBigDecimal(int)
	 */
	public BigDecimal getBigDecimal( int parameterId ) throws OdaException
	{
		assertNotNull( callStat );
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
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getString(java.lang.String)
	 */
	public String getString( String parameterName ) throws OdaException
	{
		assertNotNull( callStat );
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
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getString(int)
	 */
	public String getString( int parameterId ) throws OdaException
	{
		assertNotNull( callStat );
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
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getDate(java.lang.String)
	 */
	public Date getDate( String parameterName ) throws OdaException
	{
		assertNotNull( callStat );
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
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getDate(int)
	 */
	public Date getDate( int parameterId ) throws OdaException
	{
		assertNotNull( callStat );
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
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getTime(java.lang.String)
	 */
	public Time getTime( String parameterName ) throws OdaException
	{
		assertNotNull( callStat );
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
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getTime(int)
	 */
	public Time getTime( int parameterId ) throws OdaException
	{
		assertNotNull( callStat );
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
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getTimestamp(java.lang.String)
	 */
	public Timestamp getTimestamp( String parameterName ) throws OdaException
	{
		assertNotNull( callStat );
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
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getTimestamp(int)
	 */
	public Timestamp getTimestamp( int parameterId ) throws OdaException
	{
		assertNotNull( callStat );
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

	/* (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getBlob(java.lang.String)
	 */
	public IBlob getBlob( String parameterName ) throws OdaException
	{
		assertNotNull( callStat );
		try
		{
			java.sql.Blob blob = callStat.getBlob( parameterName );
			return new Blob( blob );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.RESULTSET_CANNOT_GET_BLOB_VALUE,
					e );
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getBlob(int)
	 */
	public IBlob getBlob( int parameterId ) throws OdaException
	{
		assertNotNull( callStat );
		try
		{
			java.sql.Blob blob = callStat.getBlob( parameterId );
			return new Blob( blob );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.RESULTSET_CANNOT_GET_BLOB_VALUE,
					e );
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getClob(java.lang.String)
	 */
	public IClob getClob( String parameterName ) throws OdaException
	{
		assertNotNull( callStat );
		try
		{
			java.sql.Clob clob = callStat.getClob( parameterName );
			return new Clob( clob );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.RESULTSET_CANNOT_GET_BLOB_VALUE,
					e );
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getClob(int)
	 */
	public IClob getClob( int parameterId ) throws OdaException
	{
		assertNotNull( callStat );
		try
		{
			java.sql.Clob clob = callStat.getClob( parameterId );
			return new Clob( clob );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.RESULTSET_CANNOT_GET_BLOB_VALUE,
					e );
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getRow(java.lang.String)
	 */
	public IParameterRowSet getRow( String parameterName ) throws OdaException
	{
		return null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getResultSet()
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
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getResultSet(java.lang.String)
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
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getMoreResults()
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
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getRow(int)
	 */
	public IParameterRowSet getRow( int parameterId ) throws OdaException
	{
		return null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getMetaDataOf(java.lang.String)
	 */
	public IResultSetMetaData getMetaDataOf( String resultSetName )
			throws OdaException
	{
		return null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#findInParameter(java.lang.String)
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
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#findOutParameter(java.lang.String)
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
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getParameterMetaData()
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
			String schemaPattern = null;
			ArrayList schemaList = null;
			String columnNamePattern = null;
			String procedureNamePattern = procedureName;
			
			if ( procedureName.indexOf( "." ) > 0 )
			{
				schemaPattern = procedureName.substring( 0,
						procedureName.indexOf( "." ) );
				procedureNamePattern = procedureName.substring( procedureName.indexOf( "." ) + 1 );
			}
			
			if ( schemaPattern != null )
			{
				schemaList = new ArrayList( );
				schemaList.add( schemaPattern );
			}
			else
			{
				schemaList = createSchemaList( metaData.getSchemas( ) );
			}
			
			if ( schemaList == null || schemaList.size( ) == 0 )
			{
				if ( schemaList == null )
					schemaList = new ArrayList( );
				
				schemaList.add( "" );
				columnNamePattern = "";
			}

			for ( int i = 0; i < schemaList.size( ); i++ )
			{
				java.sql.ResultSet rs = metaData.getProcedureColumns( cataLog,
						schemaList.get( i ).toString( ),
						procedureNamePattern,
						columnNamePattern );
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
					if ( p.getParamType( ) == Types.OTHER )
						correctParamType( p );
					if ( p.getParamInOutType( ) != 5 )
						paramMetaDataList.add( p );
				}
			}
		}
		catch ( SQLException e )
		{
		}
		return paramMetaDataList;
	}
	
	/**
	 * temporary solution for driver specific problem: Oracle
	 * @param parameterDefn
	 */
	private void correctParamType( ParameterDefn parameterDefn )
	{
		String parameterName = parameterDefn.getParamTypeName( ).toUpperCase( );
		if ( parameterName.equals( "FLOAT" ) )
			parameterDefn.setParamType( Types.FLOAT );
		else if ( parameterName.equals( "NCHAR" )
				|| parameterName.equals( "NVARCHAR2" ) )
			parameterDefn.setParamType( Types.VARCHAR );
		else
			parameterDefn.setParamType( Types.VARCHAR );
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

			//ResultSet rs = null;
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
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setSortSpec(org.eclipse.datatools.connectivity.oda.SortSpec)
	 */
	public void setSortSpec( SortSpec sortBy ) throws OdaException
	{
		setSortSpec( null, sortBy );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#setSortSpec(java.lang.String, org.eclipse.datatools.connectivity.oda.SortSpec)
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
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getSortSpec()
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
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getSortSpec(java.lang.String)
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
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#clearInParameters()
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
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#wasNull()
	 */
	public boolean wasNull( ) throws OdaException
	{
		return false;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getResultSetNames()
	 */
	public String[] getResultSetNames( ) throws OdaException
	{
		return resultSetNames;
	}

}