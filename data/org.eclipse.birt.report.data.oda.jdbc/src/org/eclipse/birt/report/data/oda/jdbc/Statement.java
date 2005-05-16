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
import java.util.Properties;
import java.util.logging.Logger;

import org.eclipse.birt.data.oda.IParameterMetaData;
import org.eclipse.birt.data.oda.IResultSet;
import org.eclipse.birt.data.oda.IResultSetMetaData;
import org.eclipse.birt.data.oda.IStatement;
import org.eclipse.birt.data.oda.OdaException;
import org.eclipse.birt.data.oda.SortSpec;
import org.eclipse.birt.data.oda.util.logging.Level;
import org.eclipse.birt.report.data.oda.i18n.ResourceConstants;


/**
 * 
 * The class implements the org.eclipse.birt.data.oda.IStatement interface.
 * 
 */
public class Statement implements IStatement
{

	/** the JDBC preparedStatement object */
	private PreparedStatement preStat;

	/** the JDBC Connection object */
	private java.sql.Connection conn;

	/** remember the max row value, default 0. */
	private int maxrows;
	
	/** indicates if need to call JDBC setMaxRows before execute statement */
	private boolean maxRowsUpToDate = false;

	/** Error message for ERRMSG_SET_PARAMETER */
	private final static String ERRMSG_SET_PARAMETER = "Error setting value for SQL parameter #";
	
	private static Logger logger = Logger.getLogger( Statement.class.getName( ) );	

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
			maxrows = 0;
		}
		else
		{
			throw new JDBCException( ResourceConstants.DRIVER_NO_CONNECTION,
					ResourceConstants.ERROR_NO_CONNECTION );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IStatement#prepare(java.lang.String)
	 */
	public void prepare( String command ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.INFO_LEVEL, "Statement.prepare( \""
				+ command + "\" )" );
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
			/*
			 * call the JDBC Connection.prepareStatement(String) method to get
			 * the preparedStatement
			 */
			this.preStat = conn.prepareStatement( formatQueryText( command ) );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.STATEMENT_CANNOT_PREPARE,
					e );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IStatement#setProperty(java.lang.String,
	 *      java.lang.String)
	 */
	public void setProperty( String name, String value ) throws OdaException
	{
		/* not supported */
		UnsupportedOperationException e = new UnsupportedOperationException(
				"setProperty is not supported." );
		logger.logp( java.util.logging.Level.FINE,
				Statement.class.getName( ),
				"setProperty",
				"setProperty is not supported.",
				e );
		throw e;
	}

	/*
	 * @see org.eclipse.birt.data.oda.IStatement#setPropertyInfo(java.util.Properties)
	 */
	public void setPropertyInfo( Properties info ) throws OdaException
	{
		/* not supported */
		UnsupportedOperationException e = new UnsupportedOperationException( "setPropertyInfo is not supported." );
		logger.logp( java.util.logging.Level.FINE,
				Statement.class.getName( ),
				"setPropertyInfo",
				"setPropertyInfo is not supported.",
				e );
		throw e;
	}

	/*
	 * @see org.eclipse.birt.data.oda.IStatement#close()
	 */
	public void close( ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.INFO_LEVEL, "Statement.close( )" );
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
	}

	/*
	 * @see org.eclipse.birt.data.oda.IStatement#setMaxRows(int)
	 */
	public void setMaxRows( int max )
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL, "Statement.setMaxRows( "
				+ max + " )" );
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
	 * @see org.eclipse.birt.data.oda.IStatement#getMaxRows()
	 */
	public int getMaxRows( )
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL, "Statement.getMaxRows( )" );
		logger.logp( java.util.logging.Level.FINE,
				Statement.class.getName( ),
				"getMaxRows",
				"Statement.getMaxRows( )" );
		return this.maxrows;

	}

	/*
	 * @see org.eclipse.birt.data.oda.IStatement#getMetaData()
	 */
	public IResultSetMetaData getMetaData( ) throws OdaException
	{
		JDBCConnectionFactory
				.log( Level.FINE_LEVEL, "Statement.getMetaData( )" );
		logger.logp( java.util.logging.Level.FINE,
				Statement.class.getName( ),
				"getMetaData",
				"Statement.getMetaData( )" );		
		assertNotNull( preStat );

	    java.sql.ResultSetMetaData resultmd = null;
		try
		{
			/* redirect the call to JDBC preparedStatement.getMetaData() */
			resultmd = preStat.getMetaData( );
		}
		catch (NullPointerException e )
		{
		  resultmd=null;
		}
		catch(SQLException e)
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
			IResultSet mdRs = executeQuery( );
			if ( mdRs != null )
			    pstmtResultMetaData = mdRs.getMetaData( );
		}
		return pstmtResultMetaData;
	}

	/*
	 * @see org.eclipse.birt.data.oda.IStatement#executeQuery()
	 */
	public IResultSet executeQuery( ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.INFO_LEVEL,
				"Statement.executeQuery( )" );
		logger.logp( java.util.logging.Level.FINE,
				Statement.class.getName( ),
				"executeQuery",
				"Statement.executeQuery( )" );
		assertNotNull( preStat );
		try
		{
			if (!maxRowsUpToDate)
			{
				try {
					preStat.setMaxRows( maxrows );
				}catch ( SQLException e1)
				{
					//assume this exception is caused by the drivers that do
					//not support "setMaxRows" method
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
	boolean execute( ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL, "Statement.execute( )" );
		logger.logp( java.util.logging.Level.FINE,
				Statement.class.getName( ),
				"execute",
				"Statement.execute( )" );
		assertNotNull( preStat );
		try
		{
			if (!maxRowsUpToDate)
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
	 * @see org.eclipse.birt.data.oda.IStatement#setInt(java.lang.String, int)
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
	 * @see org.eclipse.birt.data.oda.IStatement#setInt(int, int)
	 */
	public void setInt( int parameterId, int value ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL, "Statement.setInt( "
				+ parameterId + " , " + value + " )" );
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
	 * @see org.eclipse.birt.data.oda.IStatement#setDouble(java.lang.String,
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
	 * @see org.eclipse.birt.data.oda.IStatement#setDouble(int, double)
	 */
	public void setDouble( int parameterId, double value ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL, "Statement.setDouble( "
				+ parameterId + " , " + value + " )" );
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
	 * @see org.eclipse.birt.data.oda.IStatement#setBigDecimal(java.lang.String,
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
	 * @see org.eclipse.birt.data.oda.IStatement#setBigDecimal(int,
	 *      java.math.BigDecimal)
	 */
	public void setBigDecimal( int parameterId, BigDecimal value )
			throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL,
				"Statement.setBigDecimal( " + parameterId + " , " + value
						+ " )" );
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
	 * @see org.eclipse.birt.data.oda.IStatement#setString(java.lang.String,
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
	 * @see org.eclipse.birt.data.oda.IStatement#setString(int,
	 *      java.lang.String)
	 */
	public void setString( int parameterId, String value ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL, "Statement.setString( "
				+ parameterId + " , \"" + value + "\" )" );
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
	 * @see org.eclipse.birt.data.oda.IStatement#setDate(java.lang.String,
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
	 * @see org.eclipse.birt.data.oda.IStatement#setDate(int, java.sql.Date)
	 */
	public void setDate( int parameterId, Date value ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL, "Statement.setDate( "
				+ parameterId + " , " + value + " )" );
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
	 * @see org.eclipse.birt.data.oda.IStatement#setTime(java.lang.String,
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
	 * @see org.eclipse.birt.data.oda.IStatement#setTime(int, java.sql.Time)
	 */
	public void setTime( int parameterId, Time value ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL, "Statement.setTime( "
				+ parameterId + " , " + value + " )" );
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
	 * @see org.eclipse.birt.data.oda.IStatement#setTimestamp(java.lang.String,
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
	 * @see org.eclipse.birt.data.oda.IStatement#setTimestamp(int,
	 *      java.sql.Timestamp)
	 */
	public void setTimestamp( int parameterId, Timestamp value )
			throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL, "Statement.setTimestamp( "
				+ parameterId + " , " + value + " )" );
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
	 * @see org.eclipse.birt.data.oda.IStatement#findInParameter(java.lang.String)
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
	 * @see org.eclipse.birt.data.oda.IStatement#getParameterType(java.lang.String)
	 */
	public int getParameterType( String parameterName ) throws OdaException
	{
		/* not supported */
		UnsupportedOperationException e = new UnsupportedOperationException( "No named Parameter supported." );
		logger.logp( java.util.logging.Level.FINE,
				Statement.class.getName( ),
				"getParameterType",
				"No named Parameter supported.",
				e );
		throw e;
	}

	/*
	 * @see org.eclipse.birt.data.oda.IStatement#getParameterType(int)
	 */
	public int getParameterType( int parameterId ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL,
				"Statement.getParameterType( " + parameterId + " )" );
		assertNotNull( preStat );

		try
		{
			/*
			 * redirect the call to JDBC preparedStatement.getParameterMetaData(
			 * ).getParameterType(int)
			 */
			return this.preStat.getParameterMetaData( ).getParameterType(
					parameterId );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.PREPARESTATEMENT_PARAMETER_TYPE_CANNOT_GET,
					e );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IStatement#getParameterMetaData()
	 */
	public IParameterMetaData getParameterMetaData( ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL,
				"Statement.getParameterMetaData( )" );
		assertNotNull( preStat );
		try
		{
			/* redirect the call to JDBC preparedStatement.getParameterMetaData */
			return new ParameterMetaData( this.preStat.getParameterMetaData( ) );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.PREPARESTATEMENT_PARAMETER_METADATA_CANNOT_GET , e );
		}
	}

	/*
	 * @see org.eclipse.birt.data.oda.IStatement#setSortSpec(org.eclipse.birt.data.oda.SortSpec)
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
	 * @see org.eclipse.birt.data.oda.IStatement#getSortSpec()
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
		JDBCConnectionFactory.log( Level.INFO_LEVEL, "Statement.clearInParameters( )" );
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
	 * Format Query Text
	 * Simply replace all LF(10) with " "
	 * @param source
	 * @return
	 */
	private String formatQueryText(String source)
	{
		return source.replaceAll("\n"," ");
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