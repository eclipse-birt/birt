/*
 * Created on 2004-9-22
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package org.eclipse.birt.report.data.oda.jdbc;

import java.sql.SQLException;

import org.eclipse.birt.data.oda.IResultSetMetaData;
import org.eclipse.birt.data.oda.OdaException;

/**
 * 
 * This class implements the org.eclipse.birt.data.oda.IResultSetMetaData
 * interface.
 * 
 */
public class ResultSetMetaData implements IResultSetMetaData
{

	/** the JDBC ResultSetMetaData object */
	private java.sql.ResultSetMetaData rsMetadata;

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
			throw new DriverException(
					DriverException.ERRMSG_NO_RESULTSETMETADATA,
					DriverException.ERROR_NO_RESULTSETMETADATA );

		}
	}

	/**
	 * 
	 * Constructor ResultSetMetaData(java.sql.ResultSetMetaData rsMeta) use
	 * JDBC's ResultSetMetaData to construct it.
	 *  
	 */
	ResultSetMetaData( java.sql.ResultSetMetaData rsMeta ) throws OdaException
	{
		/* record down the JDBC ResultSetMetaData object */
		this.rsMetadata = rsMeta;

	}

	/*
	 * @see org.eclipse.birt.data.oda.IResultSetMetaData#getColumnCount()
	 */
	public int getColumnCount( ) throws OdaException
	{
		assertNotNull( rsMetadata );
		try
		{
			/* redirect the call to JDBC ResultSetMetaData.getColumnCount() */
			return rsMetadata.getColumnCount( );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}

	}

	/*
	 * @see org.eclipse.birt.data.oda.IResultSetMetaData#getColumnName(int)
	 */
	public String getColumnName( int index ) throws OdaException
	{
		assertNotNull( rsMetadata );
		try
		{
			/* redirect the call to JDBC ResultSetMetaData.getColumnName(int) */
			return rsMetadata.getColumnName( index );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}

	}

	/*
	 * @see org.eclipse.birt.data.oda.IResultSetMetaData#getColumnLabel(int)
	 */
	public String getColumnLabel( int index ) throws OdaException
	{
		assertNotNull( rsMetadata );
		try
		{
			/* redirect the call to JDBC ResultSetMetaData.getColumnLabel(int) */
			return rsMetadata.getColumnLabel( index );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}

	}

	/*
	 * @see org.eclipse.birt.data.oda.IResultSetMetaData#getColumnType(int)
	 */
	public int getColumnType( int index ) throws OdaException
	{
		assertNotNull( rsMetadata );
		try
		{
			/* redirect the call to JDBC ResultSetMetaData.getColumnType(int) */
			return rsMetadata.getColumnType( index );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}

	}

	/*
	 * @see org.eclipse.birt.data.oda.IResultSetMetaData#getColumnTypeName(int)
	 */
	public String getColumnTypeName( int index ) throws OdaException
	{
		assertNotNull( rsMetadata );
		try
		{
			/*
			 * redirect the call to JDBC
			 * ResultSetMetaData.getColumnTypeName(int)
			 */
			return rsMetadata.getColumnTypeName( index );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}

	}

	/*
	 * @see org.eclipse.birt.data.oda.IResultSetMetaData#getColumnDisplayLength(int)
	 */
	public int getColumnDisplayLength( int index ) throws OdaException
	{
		assertNotNull( rsMetadata );
		try
		{
			/*
			 * redirect the call to JDBC
			 * ResultSetMetaData.getColumnDisplaySize(int)
			 */
			return rsMetadata.getColumnDisplaySize( index );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}

	}

	/*
	 * @see org.eclipse.birt.data.oda.IResultSetMetaData#getPrecision(int)
	 */
	public int getPrecision( int index ) throws OdaException
	{
		assertNotNull( rsMetadata );
		try
		{
			/* redirect the call to JDBC ResultSetMetaData.getPrecision(int) */
			return rsMetadata.getPrecision( index );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}

	}

	/*
	 * @see org.eclipse.birt.data.oda.IResultSetMetaData#getScale(int)
	 */
	public int getScale( int index ) throws OdaException
	{
		assertNotNull( rsMetadata );
		try
		{
			/* redirect the call to JDBC ResultSetMetaData.getScale(int) */
			return rsMetadata.getScale( index );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}

	}

	/*
	 * @see org.eclipse.birt.data.oda.IResultSetMetaData#isNullable(int)
	 */
	public int isNullable( int index ) throws OdaException
	{
		assertNotNull( rsMetadata );
		try
		{
			/* redirect the call to JDBC ResultSetMetaData.isNullable(int) */
			return rsMetadata.isNullable( index );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}

	}

}