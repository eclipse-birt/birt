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

import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.birt.data.engine.core.DataException;

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
		m_fields = fields;
	}
	
	public IResultClass getResultClass()
	{
		return m_resultClass;
	}

	public Object getFieldValue( String fieldName ) throws DataException
	{
		int fieldIndex = m_resultClass.getFieldIndex( fieldName );
		
		if( fieldIndex < 1 )
			throw new DataException( ResourceConstants.INVALID_FIELD_NAME, fieldName );
		
		return getFieldValue( fieldIndex );
	}

	public Object getFieldValue( int fieldIndex ) throws DataException
	{
		validateFieldIndex( fieldIndex );
		return m_fields[ fieldIndex - 1 ];
	}

	public void setCustomFieldValue( String fieldName, Object value ) throws DataException
	{
		int idx = m_resultClass.getFieldIndex( fieldName );
		setCustomFieldValue( idx, value );
	}

	// fieldIndex is 1-based
	public void setCustomFieldValue( int fieldIndex, Object value ) throws DataException
	{
		if ( m_resultClass.isCustomField( fieldIndex ) )
			m_fields[fieldIndex - 1] = value;
		else
			throw new DataException( ResourceConstants.INVALID_CUSTOM_FIELD_INDEX, new Integer(fieldIndex) );
	}
	
	private void validateFieldIndex( int index ) throws DataException
	{
		if( index < 1 || index > m_fields.length )
			throw new DataException( ResourceConstants.INVALID_FIELD_INDEX, new Integer(index) );
	}
	
	// To help with debugging and tracing
	public String toString()
	{
		StringBuffer buf = new StringBuffer( m_fields.length*10 );
		for (int i = 0; i < m_fields.length; i++)
		{
			if ( i > 0 ) buf.append( ',');
	     	buf.append( m_fields[i].toString());
		}
	      	return buf.toString();
	}
	
}
