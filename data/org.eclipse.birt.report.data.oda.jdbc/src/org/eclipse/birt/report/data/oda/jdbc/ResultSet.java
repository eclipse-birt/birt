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
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.logging.Logger;

import org.eclipse.birt.data.oda.IResultSet;
import org.eclipse.birt.data.oda.IResultSetMetaData;
import org.eclipse.birt.data.oda.OdaException;
import org.eclipse.birt.data.oda.util.logging.Level;

/**
 * 
 * The class implements the org.eclipse.birt.data.oda.IResultSet interface.
 *  
 */
public class ResultSet implements IResultSet
{

	/** the JDBC ResultSet object */
	private java.sql.ResultSet rs;

	/** the variable to remember the max rows that the resultset can return */
	private int maxRows;

	/** the variable to indicate the current row number */
	private int currentRow;

	private static Logger logger = Logger.getLogger( ResultSet.class.getName( ) );	

	/**
	 * assertNotNull(Object o)
	 * 
	 * @param o
	 *            the object that need to be tested null or not. if null, throw
	 *            exception
	 */
	private void assertNotNull( Object o ) throws OdaException
	{
		if ( o == null )
		{
			throw new DriverException( DriverException.ERRMSG_NO_RESULTSET,
					DriverException.ERROR_NO_RESULTSET );

		}
	}

	/**
	 * 
	 * Constructor ResultSet(java.sql.ResultSet jrs) use JDBC's ResultSet to
	 * construct it.
	 *  
	 */
	ResultSet( java.sql.ResultSet jrs ) throws OdaException
	{

		/* record down the JDBC ResultSet object */
		this.rs = jrs;

		/* set the maxrows variable, default is 0 - no limit */
		maxRows = Integer.MAX_VALUE;

		currentRow = 0;

	}

	/*
	 * 
	 * @see org.eclipse.birt.data.oda.IResultSet#getMetaData()
	 */
	public IResultSetMetaData getMetaData( ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL, "ResultSet.getMetaData()" );
		logger.logp( java.util.logging.Level.FINE,
				ResultSet.class.getName( ),
				"getMetaData",
				"ResultSet.getMetaData( )" );
		assertNotNull( rs );

		try
		{
			/* redirect the call to JDBC ResultSet.getMetaData() */
			ResultSetMetaData rsMeta = new ResultSetMetaData( rs.getMetaData( ) );
			return rsMeta;
		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}

	}

	/*
	 * 
	 * @see org.eclipse.birt.data.oda.IResultSet#close()
	 */
	public void close( ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.INFO_LEVEL, "ResultSet.close()" );
		logger.logp( java.util.logging.Level.FINE,
				ResultSet.class.getName( ),
				"close",
				"ResultSet.close()" );		
		assertNotNull( rs );
		try
		{
			/* redirect the call to JDBC ResultSet.close() */
			rs.close( );

		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}

	}

	/*
	 * 
	 * @see org.eclipse.birt.data.oda.IResultSet#setMaxRows(int)
	 */
	public void setMaxRows( int max )
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL, "ResultSet.setMaxRows( "
				+ max + " )" );
		logger.logp( java.util.logging.Level.FINE,
				ResultSet.class.getName( ),
				"setMaxRows",
				"ResultSet.setMaxRows( " + max + " )" );
		if ( max > 0 )
			maxRows = max;
		else
			maxRows = Integer.MAX_VALUE;
		//if the max is positive, reset it,
		// otherwise, ignore this operation and keep the
		// previous value.

	}

	/*
	 * 
	 * @see org.eclipse.birt.data.oda.IResultSet#next()
	 */
	public boolean next( ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL, "ResultSet.next()" );
		logger.logp( java.util.logging.Level.FINE,
				ResultSet.class.getName( ),
				"next",
				"ResultSet.next( )" );

		assertNotNull( rs );

		try
		{
			/* redirect the call to JDBC ResultSet.next() */
			if ( currentRow < maxRows && rs.next( ) )
			{
				currentRow++;
				return true;
			}
			return false;
		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}
	}

	/*
	 * 
	 * @see org.eclipse.birt.data.oda.IResultSet#getRow()
	 */
	public int getRow( ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL, "ResultSet.getRow()" );
		logger.logp( java.util.logging.Level.FINE,
				ResultSet.class.getName( ),
				"getRow",
				"ResultSet.getRow( )" );
		assertNotNull( rs );
		return this.currentRow;
	}

	/*
	 * 
	 * @see org.eclipse.birt.data.oda.IResultSet#getString(int)
	 */
	public String getString( int index ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL, "ResultSet.getString( "
				+ index + " )" );
		assertNotNull( rs );
		try
		{
			/* redirect the call to JDBC ResultSet.getString(int) */
			return rs.getString( index );

		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}
	}

	/*
	 * 
	 * @see org.eclipse.birt.data.oda.IResultSet#getString(java.lang.String)
	 */
	public String getString( String columnName ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL, "ResultSet.getString( \""
				+ columnName + "\" )" );
		assertNotNull( rs );
		try
		{
			/* redirect the call to JDBC ResultSet.getString(string) */
			return rs.getString( columnName );

		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}
	}

	/*
	 * 
	 * @see org.eclipse.birt.data.oda.IResultSet#getInt(int)
	 */
	public int getInt( int index ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL, "ResultSet.getInt( "
				+ index + " )" );
		assertNotNull( rs );
		try
		{
			/* redirect the call to JDBC ResultSet.getInt(int) */
			return rs.getInt( index );

		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}
	}

	/*
	 * 
	 * @see org.eclipse.birt.data.oda.IResultSet#getInt(java.lang.String)
	 */
	public int getInt( String columnName ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL, "ResultSet.getInt( \""
				+ columnName + "\" )" );
		assertNotNull( rs );
		try
		{
			/* redirect the call to JDBC ResultSet.getInt(String) */
			return rs.getInt( columnName );

		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}
	}

	/*
	 * 
	 * @see org.eclipse.birt.data.oda.IResultSet#getDouble(int)
	 */
	public double getDouble( int index ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL, "ResultSet.getDouble( "
				+ index + " )" );
		assertNotNull( rs );
		try
		{
			/* redirect the call to JDBC ResultSet.getDouble(int) */
			return rs.getDouble( index );

		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}
	}

	/*
	 * 
	 * @see org.eclipse.birt.data.oda.IResultSet#getDouble(java.lang.String)
	 */
	public double getDouble( String columnName ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL, "ResultSet.getDouble( \""
				+ columnName + "\" )" );
		assertNotNull( rs );
		try
		{
			/* redirect the call to JDBC ResultSet.getDouble(String) */
			return rs.getDouble( columnName );

		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}
	}

	/*
	 * 
	 * @see org.eclipse.birt.data.oda.IResultSet#getBigDecimal(int)
	 */
	public BigDecimal getBigDecimal( int index ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL,
				"ResultSet.getBigDecimal( " + index + " )" );
		assertNotNull( rs );
		try
		{
			/* redirect the call to JDBC ResultSet.getBigDecimal(int) */
			return rs.getBigDecimal( index );

		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}
	}

	/*
	 * 
	 * @see org.eclipse.birt.data.oda.IResultSet#getBigDecimal(java.lang.String)
	 */
	public BigDecimal getBigDecimal( String columnName ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL,
				"ResultSet.getBigDecimal( \"" + columnName + "\" )" );
		assertNotNull( rs );
		try
		{
			/* redirect the call to JDBC ResultSet.getBigDecimal(String) */
			return rs.getBigDecimal( columnName );

		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}
	}

	/*
	 * 
	 * @see org.eclipse.birt.data.oda.IResultSet#getDate(int)
	 */
	public Date getDate( int index ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL, "ResultSet.getDate( "
				+ index + " )" );
		assertNotNull( rs );

		try
		{
			/* redirect the call to JDBC ResultSet.getDate(int) */
			return rs.getDate( index );

		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}
	}

	/*
	 * 
	 * @see org.eclipse.birt.data.oda.IResultSet#getDate(java.lang.String)
	 */
	public Date getDate( String columnName ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL, "ResultSet.getDate( \""
				+ columnName + "\" )" );
		assertNotNull( rs );
		try
		{
			/* redirect the call to JDBC ResultSet.getDate(String) */
			return rs.getDate( columnName );

		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}
	}

	/*
	 * 
	 * @see org.eclipse.birt.data.oda.IResultSet#getTime(int)
	 */
	public Time getTime( int index ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL, "ResultSet.getTime( "
				+ index + " )" );
		assertNotNull( rs );
		try
		{
			/* redirect the call to JDBC ResultSet.getTime(int) */
			return rs.getTime( index );

		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}
	}

	/*
	 * 
	 * @see org.eclipse.birt.data.oda.IResultSet#getTime(java.lang.String)
	 */
	public Time getTime( String columnName ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL, "ResultSet.getTime( \""
				+ columnName + "\" )" );
		assertNotNull( rs );
		try
		{
			/* redirect the call to JDBC ResultSet.getTime(String) */
			return rs.getTime( columnName );

		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}
	}

	/*
	 * 
	 * @see org.eclipse.birt.data.oda.IResultSet#getTimestamp(int)
	 */
	public Timestamp getTimestamp( int index ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL, "ResultSet.getTimestamp( "
				+ index + " )" );
		assertNotNull( rs );
		try
		{
			/* redirect the call to JDBC ResultSet.getTimestamp(int) */
			return rs.getTimestamp( index );

		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}
	}

	/*
	 * 
	 * @see org.eclipse.birt.data.oda.IResultSet#getTimestamp(java.lang.String)
	 */
	public Timestamp getTimestamp( String columnName ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL,
				"ResultSet.getTimestamp( \"" + columnName + "\" )" );
		assertNotNull( rs );
		try
		{
			/* redirect the call to JDBC ResultSet.getTimestamp(String) */
			return rs.getTimestamp( columnName );

		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}
	}

	/*
	 * 
	 * @see org.eclipse.birt.data.oda.IResultSet#wasNull()
	 */
	public boolean wasNull( ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL, "ResultSet.wasNull()" );
		logger.logp( java.util.logging.Level.FINE,
				ResultSet.class.getName( ),
				"getMetaData",
				"ResultSet.wasNull( )" );
		assertNotNull( rs );

		try
		{
			/* redirect the call to JDBC ResultSet.wasNull() */
			return rs.wasNull( );

		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}
	}

	/*
	 * 
	 * @see org.eclipse.birt.data.oda.IResultSet#findColumn(java.lang.String)
	 */
	public int findColumn( String columnName ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL, "ResultSet.findColumn( \""
				+ columnName + "\" )" );
		logger.logp( java.util.logging.Level.FINE,
				ResultSet.class.getName( ),
				"findColumn",
				"ResultSet.findColumn( \"" + columnName + "\" )" );
		assertNotNull( rs );
		try
		{
			/* redirect the call to JDBC ResultSet.findColumn(String) */
			return rs.findColumn( columnName );

		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}
	}
}