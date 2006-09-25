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

package org.eclipse.birt.report.data.oda.xml.impl;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.eclipse.birt.report.data.oda.xml.i18n.Messages;
import org.eclipse.birt.report.data.oda.xml.util.RelationInformation;
import org.eclipse.birt.report.data.oda.xml.util.SaxParserConsumer;
import org.eclipse.birt.report.data.oda.xml.util.XMLDataInputStream;
import org.eclipse.birt.report.data.oda.xml.util.date.DateUtil;
import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * This class implement IResultSet class 
 */
public class ResultSet implements IResultSet
{
	//The ResultSetMetaData of this resultSet.
	private ResultSetMetaData rsMetaData;
	
	//the max number of rows can be fetched from this result set.
	private int maxRows;
	
	//indicate whether the last getX() returns null.
	private boolean wasNull;
	
	//indicate whether the result set has been closed.
	private boolean isClosed;
	
	//The ISaxParserConsumer class used to help populating the data.
	private SaxParserConsumer spConsumer;
	
	/**
	 * 
	 * @param fileName
	 * @param ri
	 * @param tableName
	 * @throws OdaException
	 */
	public ResultSet( XMLDataInputStream is, RelationInformation ri, String tableName, int maxRows )
			throws OdaException
	{
		this.rsMetaData = new ResultSetMetaData( ri, tableName );

		this.maxRows = maxRows;
		
		isClosed = false;
		
		spConsumer = new SaxParserConsumer( this, ri, is, tableName );
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getMetaData()
	 */
	public IResultSetMetaData getMetaData( ) throws OdaException
	{
		testClosed();
		return rsMetaData;
	}

	/**
	 * If the result set is closed then throw an OdaException. This method is invoked
	 * before an method defined in IResultSet is called.
	 * 
	 * @throws OdaException
	 */
	private void testClosed() throws OdaException
	{
		if( isClosed )
			throw new OdaException( Messages.getString("ResultSet.ResultSetClosed")); //$NON-NLS-1$
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#close()
	 */
	public void close( ) throws OdaException
	{
		this.spConsumer.close();
		this.rsMetaData = null;
		
		this.isClosed = true;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#setMaxRows(int)
	 */
	public void setMaxRows( int max ) throws OdaException
	{
		testClosed();
		this.maxRows = max;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#next()
	 */
	public boolean next( ) throws OdaException
	{
		testClosed();
		//If the row number exceeds the defined maxRows then return false;
		if ( spConsumer.getCurrentRowNo() >= maxRows && maxRows != 0 )
		{
			return false;
		}
		return spConsumer.next();
	}

	

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getRow()
	 */
	public int getRow( ) throws OdaException
	{
		testClosed();
		return spConsumer.getCurrentRowNo();
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getString(int)
	 */
	public String getString( int index ) throws OdaException
	{
		testClosed();
		String result = spConsumer.getResultSet()[getRowPosition( )][getColumnPosition( index )];
		this.wasNull = result == null ? true : false;
		return result;
	}

	/**
	 * Transform 1-based column index to 0-based column position in the array.
	 * @param index
	 * @return
	 */
	private int getColumnPosition( int index )
	{
		return index - 1;
	}

	/**
	 * Transform the 1-based row number to 0-based row position in the array 
	 * @return
	 */
	private int getRowPosition( )
	{
		return spConsumer.getRowPosition();
	}
	
	/**
	 * Return the index of a column
	 * 
	 * @param columnName
	 * @return
	 * @throws OdaException
	 */
	private int getColumnIndex( String columnName ) throws OdaException
	{
		for ( int i = 1; i <= rsMetaData.getColumnCount( ); i++ )
		{
			if ( rsMetaData.getColumnName( i ).equals( columnName ) )
				return i;
		}
		throw new OdaException( );
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getString(java.lang.String)
	 */
	public String getString( String name ) throws OdaException
	{
		testClosed();
		return this.getString(this.getColumnIndex( name));
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getInt(int)
	 */
	public int getInt( int index ) throws OdaException
	{
		return stringToInt ( getString(index) );
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getInt(java.lang.String)
	 */
	public int getInt( String name ) throws OdaException
	{
		return stringToInt( getString( name));
	}

	 /**
     * Transform a String value to an int value.
     * 
     * @param stringValue String value
     * @return Corresponding int value
	 * @throws OdaException 
     */
    private int stringToInt( String stringValue ) throws OdaException
    {
    	testClosed();
    	if( stringValue != null )
        {
            try
            {
                return new Integer( stringValue ).intValue();
            }
            catch( NumberFormatException e )
            {
                this.wasNull = true;
            }
        }
        return 0;
    }

    /*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getDouble(int)
	 */
	public double getDouble( int index ) throws OdaException
	{
		return stringToDouble( getString(index));
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getDouble(java.lang.String)
	 */
	public double getDouble( String name ) throws OdaException
	{
		return stringToDouble( getString( name));
	}

	/**
     * Transform a String value to a double value
     * 
     * @param stringValue String value
     * @return Corresponding double value
	 * @throws OdaException 
     */
    private double stringToDouble( String stringValue ) throws OdaException
    {
    	testClosed();
    	if( stringValue != null )
        {
            try
            {
                return new Double( stringValue ).doubleValue();
            }
            catch( NumberFormatException e )
            {
                this.wasNull = true;
            }
        }
        return 0;
    }

    /*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBigDecimal(int)
	 */
	public BigDecimal getBigDecimal( int index ) throws OdaException
	{
		return stringToBigDecimal( getString( index ));
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBigDecimal(java.lang.String)
	 */
	public BigDecimal getBigDecimal( String name ) throws OdaException
	{
		return stringToBigDecimal( getString( name ));
	}

	/**
     * Transform a String value to a big decimal value
     * 
     * @param stringValue String value
     * @return Corresponding BigDecimal value
	 * @throws OdaException 
     */
    private BigDecimal stringToBigDecimal( String stringValue ) throws OdaException
    {
    	testClosed( );
    	if( stringValue != null )
        {
            try
            {
                return new BigDecimal( stringValue );
            }
            catch( NumberFormatException e )
            {
                this.wasNull = true;
            }
        }
        return null;
    }

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getDate(int)
	 */
	public Date getDate( int index ) throws OdaException
	{
		return stringToDate( getString( index ));
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getDate(java.lang.String)
	 */
	public Date getDate( String columnName ) throws OdaException
	{
		return stringToDate( getString( columnName ) );
	}

	/**
     * Transform a String value to a date value
     * 
     * @param stringValue String value
     * @return Corresponding date value
	 * @throws OdaException 
     */

    private Date stringToDate( String stringValue ) throws OdaException
    {
    	testClosed();
    	if ( stringValue != null )
		{
			try
			{
				java.util.Date date = DateUtil.toDate( stringValue );
				return new Date( date.getTime( ) );
			}
			catch ( OdaException oe )
			{
				this.wasNull = true;
				return null;
			}
		}
        return null;
    }
	
	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getTime(int)
	 */
	public Time getTime( int index ) throws OdaException
	{
		// TODO Auto-generated method stub
		return stringToTime( this.getString( index ));
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getTime(java.lang.String)
	 */
	public Time getTime( String columnName ) throws OdaException
	{
		return stringToTime( this.getString( columnName ));
	}

	 /**
     * Transform a String value to a Time value
     * @param stringValue String value
     * @return Corresponding Time value
	 * @throws OdaException 
     */
    private Time stringToTime( String stringValue ) throws OdaException
    {
    	testClosed();
    	if ( stringValue != null )
		{
			try
			{
				java.util.Date date = DateUtil.toDate( stringValue );
				return new Time( date.getTime( ) );
			}
			catch ( OdaException oe )
			{
				this.wasNull = true;
				return null;
			}
		}
		this.wasNull = true;
		return null;
	}
	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getTimestamp(int)
	 */
	public Timestamp getTimestamp( int index ) throws OdaException
	{
		return stringToTimestamp( this.getString( index ) );
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getTimestamp(java.lang.String)
	 */
	public Timestamp getTimestamp( String columnName ) throws OdaException
	{
		return stringToTimestamp( this.getString( columnName) );
	}

	/**
     * Transform a String value to a Timestamp value
     * @param stringValue String value
     * @return Corresponding Timestamp value
	 * @throws OdaException 
     */
    private Timestamp stringToTimestamp( String stringValue ) throws OdaException
    {
    	testClosed();
    	if ( stringValue != null )
		{
			try
			{
				long timeMills = new Long( stringValue ).longValue( );
				return new Timestamp( timeMills );
			}
			catch ( NumberFormatException e1 )
			{
				try
				{
					java.util.Date date = DateUtil.toDate( stringValue );
					Timestamp timeStamp = new Timestamp( date.getTime( ) );

					return timeStamp;
				}
				catch ( OdaException oe )
				{
					this.wasNull = true;
					return null;
				}
			}
		}
    	this.wasNull = true;
        return null;
    }
	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#wasNull()
	 */
	public boolean wasNull( ) throws OdaException
	{
		testClosed();
		return this.wasNull;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#findColumn(java.lang.String)
	 */
	public int findColumn( String columnName ) throws OdaException
	{
		testClosed();
		return this.getColumnIndex( columnName );
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBlob(int)
	 */
	public IBlob getBlob( int index ) throws OdaException
	{
		throw new UnsupportedOperationException ();	
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBlob(java.lang.String)
	 */
	public IBlob getBlob( String columnName ) throws OdaException
	{
		throw new UnsupportedOperationException ();	
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getClob(int)
	 */
	public IClob getClob( int index ) throws OdaException
	{
		throw new UnsupportedOperationException ();	
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getClob(java.lang.String)
	 */
	public IClob getClob( String columnName ) throws OdaException
	{
		throw new UnsupportedOperationException ();	
	}
}
