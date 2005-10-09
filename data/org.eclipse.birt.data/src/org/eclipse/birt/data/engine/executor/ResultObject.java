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

package org.eclipse.birt.data.engine.executor;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * <code>ResultObject</code> contains the field values 
 * for a given row in the result set.
 */
public class ResultObject implements IResultObject
{
	protected IResultClass m_resultClass;
	protected Object[] m_fields;
	
	public ResultObject( IResultClass resultClass, 
						 Object[] fields )
	{
		// TODO externalize message text
		if( resultClass == null || fields == null )
			throw new NullPointerException( "ResultClass and/or fields" + 
											" should not be null." );
		
		assert( resultClass.getFieldCount() == fields.length );
		
		m_resultClass = resultClass;
		
		try
		{
			initFieldValue( fields );
		}
		catch ( DataException e )
		{
			throw new IllegalStateException( e.getMessage( ) );
		}
	}
	
	/**
	 * @param fields
	 * @throws DataException
	 */
	private void initFieldValue( Object[] fields ) throws DataException
	{
		int length = fields.length;
		m_fields = new Object[length];
		
		for ( int i = 0; i < length; i++ )
		{
			if ( fields[i] != null )
			{
				Object value = fields[i];

				// computed column has no information of field native type,
				// so a safe approach is by judging the value class.
				Class valueClass = m_resultClass.getFieldValueClass( i + 1 );
				assert valueClass != null;
				if ( valueClass!=null && valueClass.isAssignableFrom( IClob.class ) )
					value = getClobValue( (IClob) fields[i] );
				else if ( valueClass!=null && valueClass.isAssignableFrom( IBlob.class ) )
					value = getBlobValue( (IBlob) fields[i] );

				m_fields[i] = value;
			}
		}
	}
	
	/**
	 * Retrieve the String value for Clob type
	 * 
	 * @param clob
	 * @return String value of Clob type
	 * @throws DataException
	 */
	private String getClobValue( IClob clob ) throws DataException
	{
		BufferedReader in = null;
		try
		{
			in = new BufferedReader( clob.getCharacterStream( ) );
		}
		catch ( OdaException e )
		{
			throw new DataException( ResourceConstants.CLOB_OPEN_ERROR, e );
		}

		StringBuffer buffer = new StringBuffer( );
		try
		{
			String str;
			while ( ( str = in.readLine( ) ) != null )
			{
				buffer.append( str );
			}
			in.close( );
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.CLOB_READ_ERROR, e );
		}

		return buffer.toString( );
	}

	/**
	 * Retrieve the byte array value for Blob type
	 * 
	 * @param blob
	 * @return byte array value of Blob type
	 * @throws DataException
	 */
	private byte[] getBlobValue( IBlob blob ) throws DataException
	{
		BufferedInputStream inputStream = null;
		byte[] bytes = null;
		try
		{
			inputStream = new BufferedInputStream( blob.getBinaryStream( ) );
			bytes = new byte[ (int) blob.length( )]; // a potential bug ???
		}
		catch ( OdaException e )
		{
			throw new DataException( ResourceConstants.BLOB_OPEN_ERROR, e );
		}

		try
		{
			inputStream.read( bytes );
			inputStream.close( );
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.BLOB_READ_ERROR, e );
		}

		return bytes;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.odi.IResultObject#getResultClass()
	 */
	public IResultClass getResultClass( )
	{
		return m_resultClass;
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IResultObject#getFieldValue(java.lang.String)
	 */
	public Object getFieldValue( String fieldName ) throws DataException
	{
		int fieldIndex = m_resultClass.getFieldIndex( fieldName );

		if ( fieldIndex < 1 )
			throw new DataException( ResourceConstants.INVALID_FIELD_NAME,
					fieldName );

		return getFieldValue( fieldIndex );
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IResultObject#getFieldValue(int)
	 */
	public Object getFieldValue( int fieldIndex ) throws DataException
	{
		validateFieldIndex( fieldIndex );
		return m_fields[fieldIndex - 1];
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IResultObject#setCustomFieldValue(java.lang.String, java.lang.Object)
	 */
	public void setCustomFieldValue( String fieldName, Object value )
			throws DataException
	{
		int idx = m_resultClass.getFieldIndex( fieldName );
		setCustomFieldValue( idx, value );
	}

	/*
	 * fieldIndex is 1-based
	 * @see org.eclipse.birt.data.engine.odi.IResultObject#setCustomFieldValue(int, java.lang.Object)
	 */
	public void setCustomFieldValue( int fieldIndex, Object value )
			throws DataException
	{
		if ( m_resultClass.isCustomField( fieldIndex ) )
			m_fields[fieldIndex - 1] = value;
		else
			throw new DataException( ResourceConstants.INVALID_CUSTOM_FIELD_INDEX,
					new Integer( fieldIndex ) );
	}
	
	/**
	 * 
	 * @param index
	 * @throws DataException
	 */
	private void validateFieldIndex( int index ) throws DataException
	{
		if ( index < 1 || index > m_fields.length )
			throw new DataException( ResourceConstants.INVALID_FIELD_INDEX,
					new Integer( index ) );
	}
	
	/*
	 * To help with debugging and tracing
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString( )
	{
		StringBuffer buf = new StringBuffer( m_fields.length * 10 );
		for ( int i = 0; i < m_fields.length; i++ )
		{
			if ( i > 0 )
				buf.append( ',' );
			buf.append( m_fields[i].toString( ) );
		}
		return buf.toString( );
	}
	
}
