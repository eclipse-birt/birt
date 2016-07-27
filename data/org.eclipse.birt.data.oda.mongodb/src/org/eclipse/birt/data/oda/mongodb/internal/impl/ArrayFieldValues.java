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

package org.eclipse.birt.data.oda.mongodb.internal.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.eclipse.birt.data.oda.mongodb.internal.impl.MDbMetaData.FieldMetaData;

/**
 * Internal class to cache and handle a logical row's multiple values. E.g. a
 * field that contains an array of scalar values, or multiple documents fetched
 * from a nested collection. It servers to handle projection of additional
 * result set sub-rows, flattening collection of nested documents.
 */
public class ArrayFieldValues
{

	// Map key is a fully-qualified field name;
	// and Map value may be a list of field values, such as an array of scalar
	// values,
	// or values extracted from each and every document in a nested collection
	private Map<String, Object> m_fieldValues;
	private String m_containingDocName;

	private int m_subRowIndex = 0; // initialize to first subrow; 0-based index
	private int m_maxSubRows = 0;

	ArrayFieldValues( String containingDocName )
	{
		m_fieldValues = new LinkedHashMap<String, Object>( 5 );
		m_containingDocName = containingDocName;
	}

	boolean hasFieldValue( String fieldName )
	{
		return m_fieldValues.containsKey( fieldName );
	}

	Object getFieldValue( String fieldName )
	{
		return m_fieldValues.get( fieldName );
	}

	void addFieldValue( FieldMetaData fieldMD, List<?> value )
	{
		String fieldName = fieldMD.getFullName( );
		addFieldValue( fieldName, value, true ); // default is to iterate over
													// each element in value
													// list
	}

	void addFieldValue( String fieldName, Object value,
			boolean iterateListElements )
	{
		m_fieldValues.put( fieldName, value );

		int valueRowCount = 1;
		if ( iterateListElements && value instanceof List<?> )
			valueRowCount = ( (List<?>) value ).size( );
		if ( m_maxSubRows < valueRowCount )
			m_maxSubRows = valueRowCount;
	}

	boolean hasContainerDocs( )
	{
		return hasFieldValue( m_containingDocName );
	}

	void addContainerDocs( Object value )
	{
		addFieldValue( m_containingDocName, value, true );
	}

	void clearContainerDocs( )
	{
		// defer clearing of top-level container cache till the end of
		// iterating top-level row in ResultDataHandler#next()
		if ( ResultDataHandler.TOP_LEVEL_PARENT.equals( m_containingDocName ) )
			return;

		if ( !m_fieldValues.isEmpty( ) )
			m_fieldValues.remove( m_containingDocName );
		resetRowCount( );
	}

	Object getCurrentValue( String fieldName )
	{
		Object value = getFieldValue( fieldName );
		if ( value == null )
			return null;

		if ( !( value instanceof List<?> ) )
			return value;

		List<?> fieldValuesList = (List<?>) value;
		if ( m_subRowIndex < 0 || m_subRowIndex >= fieldValuesList.size( ) )
			return null;

		return fieldValuesList.get( m_subRowIndex );
	}

	Document getCurrentContainerDoc( )
	{
		Object currentDoc = getCurrentValue( m_containingDocName );
		return currentDoc instanceof Document ? (Document) currentDoc : null;
	}

	boolean next( )
	{
		// iterate over this cache
		if ( m_subRowIndex + 1 < m_maxSubRows ) // 0-based index
		{
			++m_subRowIndex;
			return true;
		}

		resetIterator( );
		return false;
	}

	private void resetIterator( )
	{
		m_subRowIndex = 0; // re-initialize to first subrow
	}

	void clear( )
	{
		if ( !m_fieldValues.isEmpty( ) )
			m_fieldValues.clear( );
		resetRowCount( );
	}

	private void resetRowCount( )
	{
		if ( m_fieldValues.isEmpty( ) )
		{
			m_maxSubRows = 0;
			resetIterator( );
			return;
		}

		// reset count based on remaining field values
		for ( Object fieldValue : m_fieldValues.values( ) )
		{
			int valueRowCount = fieldValue instanceof List<?>
					? ( (List<?>) fieldValue ).size( )
					: 1;
			if ( m_maxSubRows < valueRowCount )
				m_maxSubRows = valueRowCount;
		}
	}

}
