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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * <code>ResultClass</code> contains the metadata about 
 * the projected columns in the result set.
 */
public class ResultClass implements IResultClass
{
	private List m_projectedColumns;
	private HashMap m_nameToIdMapping;
	private String[] m_fieldNames;
	private int[] m_fieldDriverPositions;
	
	public ResultClass( List projectedColumns ) throws DataException
	{	
		assert( projectedColumns != null );
		
		m_projectedColumns = new ArrayList( );
		m_projectedColumns.addAll( projectedColumns );
		m_nameToIdMapping = new HashMap( );
				
		for( int i = 0, n = projectedColumns.size(); i < n; i++ )
		{
			ResultFieldMetadata column = 
				(ResultFieldMetadata) projectedColumns.get( i );
			
			String upperCaseName = column.getName();
			if ( upperCaseName != null )
				upperCaseName = upperCaseName.toUpperCase();
			
			// need to add 1 to the 0-based array index, so we can put the 
			// 1-based index into the name-to-id mapping that will be used 
			// for the rest of the interfaces in this class
			Integer index = new Integer( i + 1 );
			
			// If the name is a duplicate of an existing column name or alias,
			// this entry is not put into the mapping table. This effectively
			// makes this entry inaccessible by name, which is the intended behavior
			
			if ( ! m_nameToIdMapping.containsKey(upperCaseName ) )
			{
				m_nameToIdMapping.put( upperCaseName, index );
			}
			
			String upperCaseAlias = column.getAlias();
			if ( upperCaseAlias != null )
				upperCaseAlias = upperCaseAlias.toUpperCase();
			if( upperCaseAlias != null && upperCaseAlias.length() > 0 && 
					! m_nameToIdMapping.containsKey( upperCaseAlias) )
			{
				m_nameToIdMapping.put( upperCaseAlias, index );
			}
		}
	}
	
	public int getFieldCount()
	{
		return m_projectedColumns.size();
	}

	// returns the field names in the projected order
	// or an empty array if no fields were projected
	public String[] getFieldNames()
	{
		return doGetFieldNames();
	}

	private String[] doGetFieldNames()
	{
		if( m_fieldNames == null )
		{
			int size = m_projectedColumns.size();
			m_fieldNames = new String[ size ];
			for( int i = 0; i < size; i++ )
			{
				ResultFieldMetadata column = 
					(ResultFieldMetadata) m_projectedColumns.get( i );
				String name = column.getName();
				m_fieldNames[i] = name;
			}
		}
		
		return m_fieldNames;
	}

	public int[] getFieldDriverPositions()
	{
		if( m_fieldDriverPositions == null )
		{
			int size = m_projectedColumns.size();
			m_fieldDriverPositions = new int[ size ];
			for( int i = 0; i < size; i++ )
			{
				ResultFieldMetadata column = 
					(ResultFieldMetadata) m_projectedColumns.get( i );
				m_fieldDriverPositions[i] = column.getDriverPosition();
			}
		}
		
		return m_fieldDriverPositions;
	}
	
	public String getFieldName( int index ) throws DataException
	{
		validateFieldIndex( index );
		ResultFieldMetadata column = 
			(ResultFieldMetadata) m_projectedColumns.get( index - 1 );
		return column.getName();
	}

	public String getFieldAlias( int index ) throws DataException
	{
		ResultFieldMetadata column = findColumn( index );
		return column.getAlias();
	}
	
	public int getFieldIndex( String fieldName )
	{
		Integer i = 
			(Integer) m_nameToIdMapping.get( fieldName.toUpperCase( ) );
		return ( i == null ) ? -1 : i.intValue();
	}
	
	private int doGetFieldIndex( String fieldName ) throws DataException
	{
		int index = getFieldIndex( fieldName );
		
		// TODO externalize message text
		if( index <= 0 )
			throw new DataException( ResourceConstants.INVALID_FIELD_NAME, fieldName );
		
		return index;
	}

	public Class getFieldValueClass( String fieldName ) throws DataException
	{
		int index = doGetFieldIndex( fieldName );
		return getFieldValueClass( index );
	}

	public Class getFieldValueClass( int index ) throws DataException
	{
		ResultFieldMetadata column = findColumn( index );
		return column.getDataType();
	}

	public boolean isCustomField( String fieldName ) throws DataException
	{
		int index = doGetFieldIndex( fieldName );
		return isCustomField( index );
	}

	public boolean isCustomField( int index ) throws DataException
	{
		ResultFieldMetadata column = findColumn( index );
		return column.isCustom();
	}

	public String getFieldLabel( int index ) throws DataException
	{
		ResultFieldMetadata column = findColumn( index );
		return column.getLabel();
	}
	
	private ResultFieldMetadata findColumn( int index ) throws DataException
	{
		validateFieldIndex( index );
		return (ResultFieldMetadata) m_projectedColumns.get( index - 1 );
	}
	
	// field indices are 1-based
    private void validateFieldIndex( int index ) throws DataException
    {
    	// TODO externalize message text
        if ( index < 1 || index > getFieldCount() )
            throw new DataException( ResourceConstants.INVALID_FIELD_INDEX, new Integer(index) );
    }
}
