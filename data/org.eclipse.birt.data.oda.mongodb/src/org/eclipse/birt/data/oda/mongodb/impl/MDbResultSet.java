/*
 *************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.oda.mongodb.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Base64;
import org.bson.Document;
import org.bson.types.BSONTimestamp;
import org.eclipse.birt.data.oda.mongodb.internal.impl.DriverUtil;
import org.eclipse.birt.data.oda.mongodb.internal.impl.MDbMetaData.FieldMetaData;
import org.eclipse.birt.data.oda.mongodb.internal.impl.QueryProperties;
import org.eclipse.birt.data.oda.mongodb.internal.impl.ResultDataHandler;
import org.eclipse.birt.data.oda.mongodb.nls.Messages;
import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.impl.Blob;

import com.mongodb.BasicDBList;
import com.mongodb.DBCursor;

/**
 * Implementation class of IResultSet for the MongoDB ODA runtime driver.
 */
@SuppressWarnings("deprecation")
public class MDbResultSet implements IResultSet
{

	private Iterator<Document> m_resultsIterator;
	private DBCursor m_mongoCursor;
	private MDbResultSetMetaData m_metadata;
	private QueryProperties m_queryProps;

	private Document m_currentRow;
	private int m_currentRowId = 0; // 1-based index
	private int m_maxRows = 0; // no limit by default
	private boolean m_wasNull = true;
	private ResultDataHandler m_dataHandler;

	private static Logger sm_logger = DriverUtil.getLogger( );

	public MDbResultSet( Iterator<Document> resultsIterator,
			MDbResultSetMetaData rsmd, QueryProperties queryProps )
	{
		if ( resultsIterator == null || rsmd == null )
			throw new IllegalArgumentException( "null DBCursor" ); //$NON-NLS-1$

		m_resultsIterator = resultsIterator;
		// if( resultsIterator instanceof DBCursor )
		// m_mongoCursor = (DBCursor)resultsIterator;
		m_metadata = rsmd;
		m_queryProps = queryProps != null
				? queryProps
				: QueryProperties.defaultValues( );
		if ( projectsFlattenedRows( ) )
			m_dataHandler = new ResultDataHandler( m_metadata );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getMetaData()
	 */
	public IResultSetMetaData getMetaData( ) throws OdaException
	{
		return m_metadata;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#setMaxRows(int)
	 */
	public void setMaxRows( int max ) throws OdaException
	{
		m_maxRows = max;
		if ( m_maxRows > 0 && m_mongoCursor != null )
			m_mongoCursor.limit( m_maxRows );
	}

	/**
	 * Returns the maximum number of rows that can be fetched from this result
	 * set.
	 * 
	 * @return the maximum number of rows to fetch.
	 */
	protected int getMaxRows( )
	{
		return m_maxRows >= 0 ? m_maxRows : 0; // negative value is ignored
	}

	private boolean hasNoMaxLimit( )
	{
		return m_maxRows <= 0;
	}

	private boolean projectsFlattenedRows( )
	{
		return m_queryProps.isAutoFlattening( );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#next()
	 */
	public boolean next( ) throws OdaException
	{
		// handle automatic flattening of embedded objects - default is false
		if ( projectsFlattenedRows( ) )
		{
			if ( m_dataHandler != null && m_dataHandler.next( ) )
				return true;
		}

		// get next row from the iterator
		m_currentRow = null; // reset
		if ( m_resultsIterator == null || !m_resultsIterator.hasNext( ) )
			return false;

		// has next row; check the maximum rows limit
		if ( hasNoMaxLimit( ) || m_currentRowId < getMaxRows( ) )
		{
			m_currentRow = m_resultsIterator.next( );
			m_currentRowId++;
			return true;
		}

		return false;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#close()
	 */
	public void close( ) throws OdaException
	{
		m_currentRow = null;
		m_currentRowId = 0; // reset row counter
		m_resultsIterator = null;
		if ( m_mongoCursor != null )
		{
			m_mongoCursor.close( );
			m_mongoCursor = null;
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getRow()
	 */
	public int getRow( ) throws OdaException
	{
		return m_currentRowId;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getString(int)
	 */
	public String getString( int index ) throws OdaException
	{
		return getString( findFieldName( index ) );
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#getString(java.lang.
	 * String)
	 */
	public String getString( String columnName ) throws OdaException
	{
		Object columnValue = getFieldValue( columnName );
		if ( columnValue instanceof String )
			return (String) columnValue;

		if ( columnValue instanceof List
				&& !( columnValue instanceof BasicDBList ) )
		{
			// convert generic List to JSON-formattable list
			List<?> fromList = (List<?>) columnValue;
			if ( !fromList.isEmpty( ) )
			{
				BasicDBList fieldValuesList = new BasicDBList( );
				for ( int index = 0; index < fromList.size( ); index++ )
				{
					fieldValuesList.put( index, fromList.get( index ) );
				}
				fieldValuesList.markAsPartialObject( );
				return fieldValuesList.toString( ); // return JSON expr format
			}
		}

		if ( columnValue instanceof byte[] )
			return convertToString( (byte[]) columnValue );

		return columnValue != null ? columnValue.toString( ) : null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getInt(int)
	 */
	public int getInt( int index ) throws OdaException
	{
		return getInt( findFieldName( index ) );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getInt(java.lang.
	 * String)
	 */
	public int getInt( String columnName ) throws OdaException
	{
		Object columnValue = getFieldValue( columnName );
		columnValue = tryConvertToDataType( columnValue, Integer.class );

		if ( columnValue instanceof List )
			columnValue = getFirstFieldValue( (List<?>) columnValue,
					Integer.class, columnName );
		if ( columnValue instanceof Integer )
			return (Integer) columnValue;

		// not convertible
		if ( columnValue != null )
		{
			String errMsg = Messages.bind(
					Messages.mDbResultSet_cannotConvertFieldData,
					new Object[]{columnName, columnValue,
							columnValue.getClass( ).getSimpleName( ),
							"integer"} ); //$NON-NLS-1$
			getLogger( ).severe( errMsg );
			throw new OdaException( errMsg );
		}

		// null value
		return 0;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getDouble(int)
	 */
	public double getDouble( int index ) throws OdaException
	{
		return getDouble( findFieldName( index ) );
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#getDouble(java.lang.
	 * String)
	 */
	public double getDouble( String columnName ) throws OdaException
	{
		Object columnValue = getFieldValue( columnName );
		columnValue = tryConvertToDataType( columnValue, Double.class );

		if ( columnValue instanceof List )
			columnValue = getFirstFieldValue( (List<?>) columnValue,
					Double.class, columnName );
		if ( columnValue instanceof Double )
			return (Double) columnValue;

		// not convertible
		if ( columnValue != null )
		{
			String errMsg = Messages.bind(
					Messages.mDbResultSet_cannotConvertFieldData,
					new Object[]{columnName, columnValue,
							columnValue.getClass( ).getSimpleName( ),
							"double"} ); //$NON-NLS-1$
			getLogger( ).severe( errMsg );
			throw new OdaException( errMsg );
		}

		// null value
		return 0d;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBigDecimal(int)
	 */
	public BigDecimal getBigDecimal( int index ) throws OdaException
	{
		return getBigDecimal( findFieldName( index ) );
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#getBigDecimal(java.lang
	 * .String)
	 */
	public BigDecimal getBigDecimal( String columnName ) throws OdaException
	{
		Object columnValue = getFieldValue( columnName );
		columnValue = tryConvertToDataType( columnValue, BigDecimal.class );
		if ( columnValue instanceof BigDecimal )
			return (BigDecimal) columnValue;

		if ( columnValue instanceof List )
			return (BigDecimal) getFirstFieldValue( (List<?>) columnValue,
					BigDecimal.class, columnName );

		// not convertible
		if ( columnValue != null )
		{
			String errMsg = Messages.bind(
					Messages.mDbResultSet_cannotConvertFieldData,
					new Object[]{columnName, columnValue,
							columnValue.getClass( ).getSimpleName( ),
							"BigDecimal"} ); //$NON-NLS-1$
			getLogger( ).severe( errMsg );
			throw new OdaException( errMsg );
		}

		return null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getDate(int)
	 */
	public Date getDate( int index ) throws OdaException
	{
		return getDate( findFieldName( index ) );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getDate(java.lang.
	 * String)
	 */
	public Date getDate( String columnName ) throws OdaException
	{
		Object columnValue = getFieldValue( columnName );
		columnValue = tryConvertToDataType( columnValue, Date.class );
		if ( columnValue instanceof Date )
			return (Date) columnValue;

		if ( columnValue instanceof List )
			return (Date) getFirstFieldValue( (List<?>) columnValue, Date.class,
					columnName );

		// not convertible
		if ( columnValue != null )
		{
			String errMsg = Messages
					.bind( Messages.mDbResultSet_cannotConvertFieldData,
							new Object[]{columnName, columnValue,
									columnValue.getClass( ).getSimpleName( ),
									"Date"} ); //$NON-NLS-1$
			getLogger( ).severe( errMsg );
			throw new OdaException( errMsg );
		}

		return null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getTime(int)
	 */
	public Time getTime( int index ) throws OdaException
	{
		return getTime( findFieldName( index ) );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getTime(java.lang.
	 * String)
	 */
	public Time getTime( String columnName ) throws OdaException
	{
		Date dateValue = getDate( columnName );
		if ( dateValue == null )
			return null;
		return new Time( dateValue.getTime( ) );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getTimestamp(int)
	 */
	public Timestamp getTimestamp( int index ) throws OdaException
	{
		return getTimestamp( findFieldName( index ) );
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#getTimestamp(java.lang.
	 * String)
	 */
	public Timestamp getTimestamp( String columnName ) throws OdaException
	{
		Object columnValue = getFieldValue( columnName );
		columnValue = tryConvertToDataType( columnValue, Timestamp.class );

		if ( columnValue instanceof List )
			columnValue = getFirstFieldValue( (List<?>) columnValue,
					Timestamp.class, columnName );
		if ( columnValue instanceof Timestamp )
			return (Timestamp) columnValue;

		// not convertible
		if ( columnValue != null )
		{
			String errMsg = Messages.bind(
					Messages.mDbResultSet_cannotConvertFieldData,
					new Object[]{columnName, columnValue,
							columnValue.getClass( ).getSimpleName( ),
							"Timestamp"} ); //$NON-NLS-1$
			getLogger( ).severe( errMsg );
			throw new OdaException( errMsg );
		}

		return null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBlob(int)
	 */
	public IBlob getBlob( int index ) throws OdaException
	{
		return getBlob( findFieldName( index ) );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBlob(java.lang.
	 * String)
	 */
	public IBlob getBlob( String columnName ) throws OdaException
	{
		Object columnValue = getFieldValue( columnName );
		columnValue = tryConvertToDataType( columnValue, byte[].class );

		if ( columnValue instanceof List )
			columnValue = getFirstFieldValue( (List<?>) columnValue,
					byte[].class, columnName );
		if ( columnValue instanceof byte[] )
			return new Blob( (byte[]) columnValue );

		// not convertible
		if ( columnValue != null )
		{
			String errMsg = Messages
					.bind( Messages.mDbResultSet_cannotConvertFieldData,
							new Object[]{columnName, columnValue,
									columnValue.getClass( ).getSimpleName( ),
									"Blob"} ); //$NON-NLS-1$
			getLogger( ).severe( errMsg );
			throw new OdaException( errMsg );
		}

		return null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getClob(int)
	 */
	public IClob getClob( int index ) throws OdaException
	{
		throw MDbQuery.sm_unSupportedOpEx;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getClob(java.lang.
	 * String)
	 */
	public IClob getClob( String columnName ) throws OdaException
	{
		throw MDbQuery.sm_unSupportedOpEx;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBoolean(int)
	 */
	public boolean getBoolean( int index ) throws OdaException
	{
		return getBoolean( findFieldName( index ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#getBoolean(java.lang.
	 * String)
	 */
	public boolean getBoolean( String columnName ) throws OdaException
	{
		Object columnValue = getFieldValue( columnName );
		columnValue = tryConvertToDataType( columnValue, Boolean.class );

		if ( columnValue instanceof List )
			columnValue = getFirstFieldValue( (List<?>) columnValue,
					Boolean.class, columnName );
		if ( columnValue instanceof Boolean )
			return (Boolean) columnValue;

		// not convertible
		if ( columnValue != null )
		{
			String errMsg = Messages.bind(
					Messages.mDbResultSet_cannotConvertFieldData,
					new Object[]{columnName, columnValue,
							columnValue.getClass( ).getSimpleName( ),
							"boolean"} ); //$NON-NLS-1$
			getLogger( ).severe( errMsg );
			throw new OdaException( errMsg );
		}

		// null value
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getObject(int)
	 */
	public Object getObject( int index ) throws OdaException
	{
		return getObject( findFieldName( index ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#getObject(java.lang.
	 * String)
	 */
	public Object getObject( String columnName ) throws OdaException
	{
		return getFieldValue( columnName );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#wasNull()
	 */
	public boolean wasNull( ) throws OdaException
	{
		return m_wasNull;
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#findColumn(java.lang.
	 * String)
	 */
	public int findColumn( String columnName ) throws OdaException
	{
		return m_metadata.getColumnNumber( columnName );
	}

	private String findFieldName( int columnNumber ) throws OdaException
	{
		return getMetaData( ).getColumnName( columnNumber );
	}

	private Object getFieldValue( String columnName ) throws OdaException
	{
		Object fieldValue = doGetFieldValue( columnName );
		m_wasNull = ( fieldValue == null );
		return fieldValue;
	}

	private Object doGetFieldValue( String columnName ) throws OdaException
	{
		if ( !projectsFlattenedRows( ) )
		{
			FieldMetaData fieldMD = m_metadata.getColumnMetaData( columnName );
			return ResultDataHandler.fetchFieldValues( columnName, fieldMD,
					m_currentRow );
		}

		// flatten array of nested documents' field values, if exists, and
		// projects into multiple result rows
		return m_dataHandler.getFieldValue( columnName, m_currentRow );
	}

	/*
	 * Get specified type of value from the first element in an array; handles
	 * nested arrays. Returns a null value if not able to return value in the
	 * specified valueDataType.
	 */
	private Object getFirstFieldValue( List<?> valuesList,
			Class<?> valueDataType, String logColumnName ) throws OdaException
	{
		Object value = getFirstElementFromList( valuesList, logColumnName );
		value = tryConvertToDataType( value, valueDataType );
		if ( valueDataType.isInstance( value ) )
			return value;
		if ( value instanceof List ) // nested array
			return getFirstFieldValue( (List<?>) value, valueDataType,
					logColumnName );

		// not convertible
		if ( value != null && getLogger( ).isLoggable( Level.FINE ) )
			getLogger( ).fine( Messages.bind(
					"Unable to get the '{0}' field's first array value ({1}) in {2} data type as a {3} value.", //$NON-NLS-1$
					new Object[]{logColumnName, value,
							value.getClass( ).getSimpleName( ),
							valueDataType.getSimpleName( )} ) );

		m_wasNull = true;
		return null;
	}

	private static Object getFirstElementFromList( List<?> valuesList,
			String columnName )
	{
		if ( valuesList.size( ) == 0 )
			return null;
		Object firstValue = null;
		if ( valuesList instanceof BasicDBList )
			firstValue = ( (BasicDBList) valuesList )
					.get( String.valueOf( 0 ) );
		else
			firstValue = valuesList.get( 0 );

		// log that only first value in array is returned
		logFetchedFirstElementFromArray( columnName, valuesList.size( ) );
		return firstValue;
	}

	private static Object tryConvertToDataType( Object value,
			Class<?> toDataType ) throws OdaException
	{
		if ( value == null || toDataType.isInstance( value ) ) // already in
																// specified
																// data
																// type
			return value;

		try
		{
			if ( value instanceof String )
			{
				String stringValue = (String) value;
				if ( toDataType == Integer.class )
					return Integer.valueOf( stringValue );
				if ( toDataType == Double.class )
					return Double.valueOf( stringValue );
				if ( toDataType == BigDecimal.class )
					return new BigDecimal( stringValue );
				if ( toDataType == Boolean.class )
					return Boolean.valueOf( stringValue );
				if ( toDataType == Date.class )
					return Date.valueOf( stringValue );
				if ( toDataType == Timestamp.class )
					return Timestamp.valueOf( stringValue );
				if ( toDataType == byte[].class )
					return tryConvertToBytes( stringValue );
			}

			if ( value instanceof java.util.Date ) // the object type returned
													// by
													// MongoDB for a Date field
			{
				long msTime = ( (java.util.Date) value ).getTime( );
				if ( toDataType == Date.class )
					return new Date( msTime );
				if ( toDataType == Timestamp.class )
					return new Timestamp( msTime );
			}

			if ( value instanceof BSONTimestamp )
			{
				long msTime = ( (BSONTimestamp) value ).getTime( ) * 1000L;
				if ( toDataType == Date.class )
					return new Date( msTime );
				if ( toDataType == Timestamp.class )
					return new Timestamp( msTime );
			}

			if ( toDataType == Integer.class )
				return tryConvertToInteger( value );
			if ( toDataType == Double.class )
				return tryConvertToDouble( value );
			if ( toDataType == Boolean.class )
				return tryConvertToBoolean( value );
		}
		catch ( Exception ex )
		{
			String errMsg = Messages.bind(
					Messages.mDbResultSet_cannotConvertFieldData,
					new Object[]{DriverUtil.EMPTY_STRING, value,
							value.getClass( ).getSimpleName( ),
							toDataType.getSimpleName( )} );
			getLogger( ).severe( errMsg );

			OdaException odaEx = new OdaException( errMsg );
			odaEx.initCause( ex );
			throw odaEx;
		}

		// non-handled data type conversion; return value as is
		return value;
	}

	private static Object tryConvertToInteger( Object value )
	{
		if ( value instanceof Number )
			return Integer.valueOf( ( (Number) value ).intValue( ) );
		if ( value instanceof Boolean )
			return ( (Boolean) value )
					? Integer.valueOf( 1 )
					: Integer.valueOf( 0 );
		return value; // not able to convert; return value as is
	}

	private static Object tryConvertToDouble( Object value )
	{
		if ( value instanceof Number )
			return Double.valueOf( ( (Number) value ).doubleValue( ) );
		if ( value instanceof Boolean )
			return ( (Boolean) value )
					? Double.valueOf( 1d )
					: Double.valueOf( 0d );
		return value; // not able to convert; return value as is
	}

	private static Object tryConvertToBoolean( Object value )
	{
		if ( value instanceof Number )
			return ( (Number) value ).doubleValue( ) != 0
					? Boolean.TRUE
					: Boolean.FALSE;
		return value; // not able to convert; return value as is
	}

	private static Object tryConvertToBytes( String stringValue )
	{
		try
		{
			return DatatypeConverter.parseBase64Binary( stringValue );
		}
		catch ( Exception ex )
		{
			// DatatypeConverter could be un-initialized,
			// log and continue; note that Base64Codec#decode might be
			// unavailable in some versions
			if ( getLogger( ).isLoggable( Level.FINE ) )
				getLogger( ).fine( Messages.bind(
						"Unable to convert the String field value ({0}) to a bytes[] value.\n Cause: {1}", //$NON-NLS-1$
						stringValue, ex.getMessage( ) ) );
		}
		return stringValue; // not able to convert; return value as is
	}

	private static String convertToString( byte[] value )
	{
		try
		{
			return DatatypeConverter.printBase64Binary( value );
		}
		catch ( Exception ex )
		{
			// DatatypeConverter could be un-initialized; retry with Base64Codec
			return ( new String( Base64.encodeBase64( value ) ) );
		}
	}

	private static void logFetchedFirstElementFromArray( String columnName,
			int arraySize )
	{
		// log that only first value in set is returned
		if ( arraySize > 1 && getLogger( ).isLoggable( Level.FINER ) )
			getLogger( ).finer( Messages.bind(
					"Fetching only the first value out of {0} for the field {1}.", //$NON-NLS-1$
					Integer.valueOf( arraySize ), columnName ) );
	}

	private static Logger getLogger( )
	{
		return sm_logger;
	}

}
