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
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.OdaResultSetColumnHandle;
import org.eclipse.birt.report.model.api.elements.structures.ColumnHint;
import org.eclipse.birt.report.model.api.elements.structures.OdaResultSetColumn;
import org.eclipse.datatools.connectivity.oda.design.AxisAttributes;
import org.eclipse.datatools.connectivity.oda.design.ColumnDefinition;
import org.eclipse.datatools.connectivity.oda.design.DataElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.DataElementUIHints;
import org.eclipse.datatools.connectivity.oda.design.OutputElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.ValueFormatHints;

/**
 * Checks one column definition with oda result set column handle.
 */

class ResultSetColumnChecker
{

	List<IAmbiguousAttribute> ambiguousList;

	ColumnDefinition columnDefn = null;

	OdaResultSetColumnHandle columnHandle;

	ColumnHintHandle columnHintHandle;

	/**
	 * @param columnDefn
	 * @param columnHandle
	 * @param existingColumnHintHandle
	 */

	ResultSetColumnChecker( ColumnDefinition columnDefn,
			OdaResultSetColumnHandle columnHandle,
			ColumnHintHandle columnHintHandle )
	{
		if ( columnDefn == null || columnHandle == null )
			throw new IllegalArgumentException(
					"The parameter definition and oda data set parameter handle can not be null!" ); //$NON-NLS-1$
		this.columnDefn = columnDefn;
		this.columnHandle = columnHandle;
		this.columnHintHandle = columnHintHandle;
		this.ambiguousList = new ArrayList<IAmbiguousAttribute>( );
	}

	/**
	 * 
	 */

	List<IAmbiguousAttribute> process( )
	{
		DataElementAttributes dataAttrs = columnDefn.getAttributes( );
		processDataElementAttributes( dataAttrs );
		processColumnHint( );

		return this.ambiguousList;

	}

	/**
	 * Checks column handle
	 */
	private void processDataElementAttributes( DataElementAttributes dataAttrs )
	{
		// check the native name

		if ( dataAttrs == null )
			return;

		// compare the name with the native name in oda result set column
		String newValue = dataAttrs.getName( );
		String oldValue = columnHandle.getNativeName( );
		checkProperty( OdaResultSetColumn.NATIVE_NAME_MEMBER, oldValue,
				newValue );

		// the position in the column definition and oda result set column is
		// equal
		assert dataAttrs.getPosition( ) == columnHandle.getPosition( )
				.intValue( );

		// compare native data type
		int newNativeDataType = dataAttrs.getNativeDataTypeCode( );
		Integer oldNativeDataType = columnHandle.getNativeDataType( );
		checkProperty( OdaResultSetColumn.NATIVE_DATA_TYPE_MEMBER,
				oldNativeDataType, newNativeDataType );
	}

	/**
	 * Checks column hints
	 */
	private void processColumnHint( )
	{
		if ( columnHintHandle == null )
			return;

		DataElementAttributes dataAttrs = columnDefn.getAttributes( );
		if ( dataAttrs != null )
		{
			checkProperty( ColumnHint.COLUMN_NAME_MEMBER, dataAttrs.getName( ),
					columnHintHandle.getColumnName( ) );
			DataElementUIHints uiHints = dataAttrs.getUiHints( );
			if ( uiHints != null )
			{
				checkProperty( ColumnHint.DISPLAY_NAME_MEMBER, uiHints
						.getDisplayName( ), columnHintHandle.getDisplayName( ) );
				checkProperty( ColumnHint.DISPLAY_NAME_ID_MEMBER, uiHints
						.getDisplayNameKey( ), columnHintHandle
						.getDisplayNameKey( ) );
				checkProperty( ColumnHint.DESCRIPTION_MEMBER, uiHints
						.getDescription( ), columnHintHandle.getDescription( ) );
				checkProperty( ColumnHint.DESCRIPTION_ID_MEMBER, uiHints
						.getDescriptionKey( ), columnHintHandle
						.getDescriptionKey( ) );
			}
		}
		AxisAttributes axisAttrs = columnDefn.getMultiDimensionAttributes( );
		if ( axisAttrs != null )
		{
			checkProperty( ColumnHint.ANALYSIS_MEMBER, ResultSetsAdapter
					.convertAxisTypeToAnalysisType( axisAttrs.getAxisType( ) ),
					columnHintHandle.getAnalysis( ) );
			checkProperty( ColumnHint.ON_COLUMN_LAYOUT_MEMBER, axisAttrs
					.isOnColumnLayout( ), columnHintHandle.isOnColumnLayout( ) );
		}
		OutputElementAttributes usageHints = columnDefn.getUsageHints( );
		if ( usageHints != null )
		{
			checkProperty( ColumnHint.HELP_TEXT_MEMBER, usageHints
					.getHelpText( ), columnHintHandle.getHelpText( ) );
			checkProperty( ColumnHint.HELP_TEXT_ID_MEMBER, usageHints
					.getHelpTextKey( ), columnHintHandle.getHelpTextKey( ) );
			checkProperty( ColumnHint.HEADING_MEMBER, usageHints.getLabel( ),
					columnHintHandle.getHeading( ) );
			checkProperty( ColumnHint.HEADING_ID_MEMBER, usageHints
					.getLabelKey( ), columnHintHandle.getHeadingKey( ) );
			ValueFormatHints formattingHints = usageHints.getFormattingHints( );
			if ( formattingHints != null )
			{
				checkProperty( ColumnHint.FORMAT_MEMBER, formattingHints
						.getDisplayFormat( ), columnHintHandle.getFormat( ) );
				checkProperty( ColumnHint.DISPLAY_LENGTH_MEMBER,
						formattingHints.getDisplaySize( ), columnHintHandle
								.getDisplayLength( ) );

				checkProperty(
						ColumnHint.HORIZONTAL_ALIGN_MEMBER,
						ResultSetsAdapter
								.convertToROMHorizontalAlignment( formattingHints
										.getHorizontalAlignment( ) ),
						columnHintHandle.getHorizontalAlign( ) );
				checkProperty( ColumnHint.WORD_WRAP_MEMBER, ResultSetsAdapter
						.convertToROMWordWrap( formattingHints
								.getTextWrapType( ) ), columnHintHandle
						.wordWrap( ) );
			}
		}
	}

	private void checkProperty( String propertyName, Object oldValue,
			Object newValue )
	{
		if ( !CompareUtil.isEquals( String.valueOf( newValue ), String
				.valueOf( oldValue ) ) )
			ambiguousList.add( new AmbiguousAttribute( propertyName, oldValue,
					newValue, false ) );
	}
}
