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

package org.eclipse.birt.report.model.adapter.oda.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.adapter.oda.IAmbiguousAttribute;
import org.eclipse.birt.report.model.api.OdaResultSetColumnHandle;
import org.eclipse.birt.report.model.api.elements.structures.OdaResultSetColumn;
import org.eclipse.datatools.connectivity.oda.design.ColumnDefinition;
import org.eclipse.datatools.connectivity.oda.design.DataElementAttributes;

/**
 * Checks one column definition with oda result set column handle.
 */

class ResultSetColumnChecker
{

	List<IAmbiguousAttribute> ambiguousList;

	ColumnDefinition columnDefn = null;

	OdaResultSetColumnHandle columnHandle;

	/**
	 * @param columnDefn
	 * @param columnHandle
	 */

	ResultSetColumnChecker( ColumnDefinition columnDefn,
			OdaResultSetColumnHandle columnHandle )
	{
		if ( columnDefn == null || columnHandle == null )
			throw new IllegalArgumentException(
					"The parameter definition and oda data set parameter handle can not be null!" ); //$NON-NLS-1$
		this.columnDefn = columnDefn;
		this.columnHandle = columnHandle;
		this.ambiguousList = new ArrayList<IAmbiguousAttribute>( );
	}

	/**
	 * 
	 */

	List<IAmbiguousAttribute> process( )
	{
		DataElementAttributes dataAttrs = columnDefn.getAttributes( );
		processDataElementAttributes( dataAttrs );

		return this.ambiguousList;

	}

	/**
	 * 
	 */
	private void processDataElementAttributes( DataElementAttributes dataAttrs )
	{
		// check the native name

		if ( dataAttrs == null )
			return;

		// compare the name with the native name in oda result set column
		String newValue = dataAttrs.getName( );
		String oldValue = columnHandle.getNativeName( );
		if ( !CompareUtil.isEquals( newValue, oldValue ) )
		{
			ambiguousList.add( new AmbiguousAttribute(
					OdaResultSetColumn.NATIVE_NAME_MEMBER, oldValue, newValue,
					false ) );
		}

		// the position in the column definition and oda result set column is
		// equal
		assert dataAttrs.getPosition( ) == columnHandle.getPosition( )
				.intValue( );

		// compare native data type
		int newNativeDataType = dataAttrs.getNativeDataTypeCode( );
		Integer oldNativeDataType = columnHandle.getNativeDataType( );
		if ( !Integer.valueOf( newNativeDataType ).equals( oldNativeDataType ) )
		{
			ambiguousList.add( new AmbiguousAttribute(
					OdaResultSetColumn.NATIVE_DATA_TYPE_MEMBER,
					oldNativeDataType, newNativeDataType, false ) );
		}
	}

}
