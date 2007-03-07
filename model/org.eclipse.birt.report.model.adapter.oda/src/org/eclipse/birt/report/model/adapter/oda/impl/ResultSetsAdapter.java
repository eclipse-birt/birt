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
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.adapter.oda.ODADesignFactory;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaResultSetColumnHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ColumnHint;
import org.eclipse.birt.report.model.api.elements.structures.OdaResultSetColumn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.datatools.connectivity.oda.design.ColumnDefinition;
import org.eclipse.datatools.connectivity.oda.design.DataElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.DataElementUIHints;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.OutputElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.ResultSetColumns;
import org.eclipse.datatools.connectivity.oda.design.ResultSetDefinition;
import org.eclipse.datatools.connectivity.oda.design.ResultSets;
import org.eclipse.datatools.connectivity.oda.design.ValueFormatHints;
import org.eclipse.emf.common.util.EList;

/**
 * The utility class that converts between ROM ResultSets and ODA ODA
 * ResultSetDefinition.
 * 
 * @see OdaDataSetHandle
 * @see ResultSetDefinition
 */

class ResultSetsAdapter
{

	/**
	 * Creates a list containing ROM ResultSetColumn according to given ODA
	 * ResultSetColumns.
	 * 
	 * @param setColumns
	 *            the ODA result set columns
	 * @return a list containing ROM ResultSetColumn.
	 */

	List newROMResultSetColumns( DataSetDesign setDesign,
			OdaDataSetHandle setHandle, ResultSetColumns cachedSetColumns )
	{
		if ( setDesign == null )
			return null;

		ResultSetDefinition resultDefn = setDesign.getPrimaryResultSet( );
		if ( resultDefn == null )
		{
			ResultSets resultSets = setDesign.getResultSets( );
			if ( resultSets != null
					&& !resultSets.getResultSetDefinitions( ).isEmpty( ) )
				resultDefn = (ResultSetDefinition) resultSets
						.getResultSetDefinitions( ).get( 0 );
		}

		if ( resultDefn == null )
			return null;

		ResultSetColumns setColumns = resultDefn.getResultSetColumns( );
		if ( setColumns == null )
			return null;

		EList odaSetColumns = setColumns.getResultColumnDefinitions( );
		if ( odaSetColumns.isEmpty( ) )
			return null;

		List retList = new ArrayList( );

		for ( int i = 0; i < odaSetColumns.size( ); i++ )
		{

			ColumnDefinition columnDefn = (ColumnDefinition) odaSetColumns
					.get( i );

			DataElementAttributes dataAttrs = columnDefn.getAttributes( );

			ColumnDefinition cachedColumnDefn = null;
			OdaResultSetColumnHandle oldColumn = null;
			if ( dataAttrs != null )
			{
				cachedColumnDefn = findColumnDefinition( cachedSetColumns,
						dataAttrs.getName( ), new Integer( dataAttrs
								.getPosition( ) ) );
				oldColumn = findOdaResultSetColumn( setHandle
						.resultSetIterator( ), dataAttrs.getName( ),
						new Integer( dataAttrs.getPosition( ) ), new Integer(
								dataAttrs.getNativeDataTypeCode( ) ) );

			}

			OdaResultSetColumn newColumn = null;

			// to use old values if applies

			if ( oldColumn == null )
			{
				// if the old column is not found, this means it can be removed.
				// Only update.

				newColumn = StructureFactory.createOdaResultSetColumn( );
				cachedColumnDefn = null;
			}
			else
				newColumn = (OdaResultSetColumn) oldColumn.getStructure( )
						.copy( );

			updateROMOdaResultSetColumnFromColumnDefinition( columnDefn,
					cachedColumnDefn, newColumn, setDesign
							.getOdaExtensionDataSourceId( ), setDesign
							.getOdaExtensionDataSetId( ), setHandle
							.resultSetIterator( ) );

			updateColumnNameIfAppliable( newColumn );

			assert newColumn.getColumnName( ) != null;

			ColumnHint oldHint = null;
			ColumnHintHandle oldHintHandle = findColumnHint( newColumn,
					setHandle.columnHintsIterator( ) );
			if ( oldHintHandle != null )
				oldHint = (ColumnHint) oldHintHandle.getStructure( );

			ColumnHint newHint = newROMColumnHintFromColumnDefinition(
					columnDefn, cachedColumnDefn, oldHint );

			ResultSetColumnInfo setInfo = new ResultSetColumnInfo( newColumn,
					newHint );
			retList.add( setInfo );
		}

		return retList;
	}

	/**
	 * Updates the oda result set column if it is <code>null</code> or empty
	 * values. The new column name is its native name.
	 * 
	 * @param column
	 *            the oda result set column
	 */

	private void updateColumnNameIfAppliable( OdaResultSetColumn column )
	{
		if ( !StringUtil.isBlank( column.getColumnName( ) ) )
			return;

		column.setColumnName( column.getNativeName( ) );

		// the result set native name still can be null. However, unique name
		// must be created when the column list is avaible. So, it will be
		// handled in ModelOdaAdapter.
		
	}

	/**
	 * Creates the column hint with given column definition and the old column
	 * hint.
	 * 
	 * @param columnDefn
	 *            the latest column definition
	 * @param cachedColumnDefn
	 *            the last (cached) column definition
	 * @param oldHint
	 *            the existing column hint in the data set handle
	 * @return the newly created column hint
	 */

	private ColumnHint newROMColumnHintFromColumnDefinition(
			ColumnDefinition columnDefn, ColumnDefinition cachedColumnDefn,
			ColumnHint oldHint )
	{
		if ( columnDefn == null )
			return null;

		DataElementAttributes dataAttrs = columnDefn.getAttributes( );
		if ( dataAttrs == null )
			return null;

		ColumnHint newHint = null;
		if ( oldHint == null )
		{
			newHint = StructureFactory.createColumnHint( );
			cachedColumnDefn = null;
		}
		else
			newHint = (ColumnHint) oldHint.copy( );

		DataElementUIHints dataUIHints = dataAttrs.getUiHints( );
		OutputElementAttributes outputAttrs = columnDefn.getUsageHints( );

		boolean isValueSet = isEmptyColumnHintValue( dataUIHints, outputAttrs );
		if ( !isValueSet )
		{
			if ( oldHint == null )
				return null;

			return newHint;
		}

		DataElementAttributes cachedDataAttrs = cachedColumnDefn == null
				? null
				: cachedColumnDefn.getAttributes( );

		updateColumnHintFromDataAttrs( columnDefn.getAttributes( ),
				cachedDataAttrs, newHint );
		updateColumnHintFromUsageHints( columnDefn.getUsageHints( ),
				cachedColumnDefn == null ? null : cachedColumnDefn
						.getUsageHints( ), newHint );

		if ( StringUtil.isBlank( (String) newHint.getProperty( null,
				ColumnHint.COLUMN_NAME_MEMBER ) ) )
		{
			newHint.setProperty( ColumnHint.COLUMN_NAME_MEMBER, newHint
					.getProperty( null, ColumnHint.COLUMN_NAME_MEMBER ) );
		}
		return newHint;
	}

	/**
	 * Checks whether there are values for newly created column hint.
	 * 
	 * @param dataUIHints
	 *            the latest data ui hints
	 * @param outputAttrs
	 *            the latest output element attributes
	 * @return <code>true</code> if no column hint value is set. Otherwise
	 *         <code>false</code>.
	 */

	private boolean isEmptyColumnHintValue( DataElementUIHints dataUIHints,
			OutputElementAttributes outputAttrs )
	{
		if ( dataUIHints == null && outputAttrs == null )
			return false;

		boolean isValueSet = false;
		if ( dataUIHints != null )
		{
			if ( dataUIHints.getDisplayName( ) != null )
				isValueSet = true;
		}

		if ( !isValueSet && outputAttrs != null )
		{
			if ( outputAttrs.getHelpText( ) != null )
				isValueSet = true;

			if ( !isValueSet )
			{
				ValueFormatHints formatHints = outputAttrs.getFormattingHints( );
				if ( formatHints != null
						&& formatHints.getDisplayFormat( ) != null )
					isValueSet = true;
			}

		}

		return isValueSet;
	}

	/**
	 * Updates column hint values by given data element attributes.
	 * 
	 * @param dataAttrs
	 *            the latest data element attributes
	 * @param cachedDataAttrs
	 *            the last(cached) data element attributes
	 * @param newHint
	 *            the column hint
	 */

	private void updateColumnHintFromDataAttrs(
			DataElementAttributes dataAttrs,
			DataElementAttributes cachedDataAttrs, ColumnHint newHint )
	{
		if ( dataAttrs == null )
			return;

		Object oldValue = cachedDataAttrs == null ? null : cachedDataAttrs
				.getName( );
		Object newValue = dataAttrs.getName( );
		if ( oldValue == null || !oldValue.equals( newValue ) )
			newHint.setProperty( ColumnHint.COLUMN_NAME_MEMBER, newValue );

		DataElementUIHints dataUIHints = dataAttrs.getUiHints( );
		if ( dataUIHints == null )
			return;

		DataElementUIHints cachedDataUIHints = cachedDataAttrs == null
				? null
				: cachedDataAttrs.getUiHints( );
		oldValue = cachedDataUIHints == null ? null : cachedDataUIHints
				.getDisplayName( );
		newValue = dataUIHints.getDisplayName( );
		if ( oldValue == null || !oldValue.equals( newValue ) )
		{
			newHint.setProperty( ColumnHint.DISPLAY_NAME_MEMBER, newValue );
		}

	}

	/**
	 * Updates column hint values by given output element attributes.
	 * 
	 * @param outputAttrs
	 *            the latest output element attributes
	 * @param cachedOutputAttrs
	 *            the last(cached) output element attributes
	 * @param newHint
	 *            the column hint
	 */

	private void updateColumnHintFromUsageHints(
			OutputElementAttributes outputAttrs,
			OutputElementAttributes cachedOutputAttrs, ColumnHint newHint )
	{
		if ( outputAttrs == null )
			return;

		Object oldValue = cachedOutputAttrs == null ? null : cachedOutputAttrs
				.getHelpText( );
		Object newValue = outputAttrs.getHelpText( );
		if ( oldValue == null || !oldValue.equals( newValue ) )
		{
			newHint.setProperty( ColumnHint.HELP_TEXT_MEMBER, newValue );
		}

		ValueFormatHints formatHints = outputAttrs.getFormattingHints( );
		if ( formatHints == null )
			return;

		ValueFormatHints cachedFormatHints = cachedOutputAttrs == null
				? null
				: cachedOutputAttrs.getFormattingHints( );
		oldValue = cachedFormatHints == null ? null : cachedFormatHints
				.getDisplayFormat( );
		newValue = formatHints.getDisplayFormat( );
		if ( oldValue == null || !oldValue.equals( newValue ) )
		{
			newHint.setProperty( ColumnHint.FORMAT_MEMBER, newValue );
		}
	}

	/**
	 * Updates column hint values by given column definition.
	 * 
	 * @param columnDefn
	 *            the latest column definition
	 * @param cachedColumnDefn
	 *            the last(cached) column definition
	 * @param setColumn
	 *            the oda result set column
	 * @param dataSourceId
	 *            the data source id
	 * @param dataSetId
	 *            the data set id
	 * @param columns
	 *            the iterator that includes oda result set columns
	 */

	private void updateROMOdaResultSetColumnFromColumnDefinition(
			ColumnDefinition columnDefn, ColumnDefinition cachedColumnDefn,
			OdaResultSetColumn setColumn, String dataSourceId,
			String dataSetId, Iterator columns )
	{
		if ( columnDefn == null )
			return;

		updateResultSetColumnFromDataAttrs( columnDefn.getAttributes( ),
				cachedColumnDefn == null ? null : cachedColumnDefn
						.getAttributes( ), setColumn, dataSourceId, dataSetId,
				columns );
	}

	/**
	 * Updates result set column values by given data element attributes.
	 * 
	 * @param dataAttrs
	 *            the latest data element attributes
	 * @param cachedDataAttrs
	 *            the last (cached) data element attributes
	 * @param newColumn
	 *            the result set column
	 * @param dataSourceId
	 *            the data source id
	 * @param dataSetId
	 *            the data set id
	 * @param params
	 *            the iterator that includes oda result set columns
	 */

	private void updateResultSetColumnFromDataAttrs(
			DataElementAttributes dataAttrs,
			DataElementAttributes cachedDataAttrs,
			OdaResultSetColumn newColumn, String dataSourceId,
			String dataSetId, Iterator params )
	{
		if ( dataAttrs == null )
		{
			return;
		}

		Object oldValue = cachedDataAttrs == null ? null : cachedDataAttrs
				.getName( );
		Object newValue = dataAttrs.getName( );
		if ( oldValue == null || !oldValue.equals( newValue ) )
		{
			newColumn.setNativeName( (String) newValue );
		}

		oldValue = cachedDataAttrs == null ? null : new Integer(
				cachedDataAttrs.getPosition( ) );
		newValue = new Integer( dataAttrs.getPosition( ) );
		if ( oldValue == null || !oldValue.equals( newValue ) )
		{
			newColumn.setPosition( (Integer) newValue );
		}

		oldValue = cachedDataAttrs == null ? null : new Integer(
				cachedDataAttrs.getNativeDataTypeCode( ) );
		newValue = new Integer( dataAttrs.getNativeDataTypeCode( ) );
		if ( oldValue == null || !oldValue.equals( newValue )
				|| newColumn.getNativeDataType( ) == null )
		{
			newColumn.setNativeDataType( (Integer) newValue );
		}

		newColumn.setDataType( getROMDataType( dataSourceId, dataSetId,
				newColumn, params ) );
	}

	/**
	 * Returns the rom data type in string.
	 * 
	 * @param dataSourceId
	 *            the id of the data source
	 * @param dataSetId
	 *            the ide of the data set
	 * @param column
	 *            the rom data set parameter
	 * @param setHandleParams
	 *            params defined in data set handle
	 * @return the rom data type in string
	 */

	private String getROMDataType( String dataSourceId, String dataSetId,
			OdaResultSetColumn column, Iterator columns )
	{
		String name = column.getNativeName( );
		Integer position = column.getPosition( );
		Integer nativeDataType = column.getNativeDataType( );

		OdaResultSetColumnHandle tmpParam = findOdaResultSetColumn( columns,
				name, position, nativeDataType );

		if ( tmpParam == null )
			return convertNativeTypeToROMDataType( dataSourceId, dataSetId,
					column.getNativeDataType( ).intValue( ) );

		Integer tmpPosition = tmpParam.getPosition( );
		if ( tmpPosition == null )
			return convertNativeTypeToROMDataType( dataSourceId, dataSetId,
					column.getNativeDataType( ).intValue( ) );

		if ( !tmpPosition.equals( column.getPosition( ) ) )
			return convertNativeTypeToROMDataType( dataSourceId, dataSetId,
					column.getNativeDataType( ).intValue( ) );

		Integer tmpNativeCodeType = tmpParam.getNativeDataType( );
		if ( tmpNativeCodeType == null
				|| tmpNativeCodeType.equals( column.getNativeDataType( ) ) )
			return tmpParam.getDataType( );

		String oldDataType = tmpParam.getDataType( );
		return convertNativeTypeToROMDataType( dataSourceId, dataSetId, column
				.getNativeDataType( ).intValue( ), oldDataType );
	}

	/**
	 * Converts the ODA native data type code to rom data type.
	 * 
	 * @param dataSourceId
	 *            the id of the data source
	 * @param dataSetId
	 *            the ide of the data set
	 * @param nativeDataTypeCode
	 *            the oda data type code
	 * @return the rom data type in string
	 */

	String convertNativeTypeToROMDataType( String dataSourceId,
			String dataSetId, int nativeDataTypeCode )
	{
		return convertNativeTypeToROMDataType( dataSourceId, dataSetId,
				nativeDataTypeCode, null );
	}

	/**
	 * Converts the ODA native data type code to rom data type.
	 * 
	 * @param dataSourceId
	 *            the id of the data source
	 * @param dataSetId
	 *            the ide of the data set
	 * @param nativeDataTypeCode
	 *            the oda data type code
	 * @return the rom data type in string
	 */

	String convertNativeTypeToROMDataType( String dataSourceId,
			String dataSetId, int nativeDataTypeCode, String romDataType )
	{
		String newRomDataType = null;

		try
		{
			newRomDataType = NativeDataTypeUtil.getUpdatedDataType(
					dataSourceId, dataSetId, nativeDataTypeCode, romDataType,
					DesignChoiceConstants.CHOICE_COLUMN_DATA_TYPE );
		}
		catch ( BirtException e )
		{

		}

		return newRomDataType;
	}

	/**
	 * Returns the matched oda result set column with the specified name and
	 * position.
	 * 
	 * @param columns
	 *            the iterator that includes oda result set columns
	 * @param paramName
	 *            the result set column name
	 * @param position
	 *            the position
	 * @return the matched oda result set column
	 */

	private static OdaResultSetColumnHandle findOdaResultSetColumn(
			Iterator columns, String paramName, Integer position,
			Integer nativeDataType )
	{
		if ( position == null || nativeDataType == null )
			return null;

		while ( columns.hasNext( ) )
		{
			OdaResultSetColumnHandle column = (OdaResultSetColumnHandle) columns
					.next( );

			Integer tmpNativeDataType = column.getNativeDataType( );

			if ( ( StringUtil.isBlank( paramName ) || paramName.equals( column
					.getNativeName( ) ) )
					&& position.equals( column.getPosition( ) )
					&& ( tmpNativeDataType == null || nativeDataType
							.equals( tmpNativeDataType ) ) )
				return column;

		}
		return null;

	}

	/**
	 * Returns the matched column definition with the specified name and
	 * position.
	 * 
	 * @param columns
	 *            the ODA defined result set column definitions
	 * @param paramName
	 *            the result set column name
	 * @param position
	 *            the position
	 * @return the matched oda result set column
	 */

	private static ColumnDefinition findColumnDefinition(
			ResultSetColumns columns, String columnName, Integer position )
	{
		if ( columns == null || columnName == null )
			return null;

		EList odaColumns = columns.getResultColumnDefinitions( );
		if ( odaColumns == null || odaColumns.isEmpty( ) )
			return null;

		for ( int i = 0; i < odaColumns.size( ); i++ )
		{
			ColumnDefinition columnDefn = (ColumnDefinition) odaColumns.get( i );

			DataElementAttributes dataAttrs = columnDefn.getAttributes( );
			if ( dataAttrs == null )
				continue;

			if ( columnName.equals( dataAttrs.getName( ) )
					&& ( position == null || position.intValue( ) == dataAttrs
							.getPosition( ) ) )
				return columnDefn;
		}

		return null;
	}

	/**
	 * Creates a list containing ROM ResultSetColumn according to given ODA
	 * ResultSets.
	 * 
	 * @param setDefn
	 *            the ODA result set.
	 * @return a list containing ROM ResultSetColumn.
	 */

	List newROMResultSets( DataSetDesign setDesign, OdaDataSetHandle setHandle,
			ResultSetDefinition cachedSetDefn ) throws SemanticException
	{
		ResultSetColumns cachedSetColumns = cachedSetDefn == null
				? null
				: cachedSetDefn.getResultSetColumns( );

		List retList = newROMResultSetColumns( setDesign, setHandle,
				cachedSetColumns );

		return retList;
	}

	/**
	 * Returns the matched column hint with the given result set column.
	 * 
	 * @param setColumn
	 *            the result set column
	 * @param columnHints
	 *            the iterator that includes column hints
	 * @return the matched column hint
	 */

	private static ColumnHintHandle findColumnHint(
			OdaResultSetColumn setColumn, Iterator columnHints )
	{
		assert setColumn != null;

		return findColumnHint( setColumn.getColumnName( ), columnHints );
	}

	/**
	 * Returns the matched column hint with the given result set column.
	 * 
	 * @param name
	 *            the name of the column hint
	 * @param columnHints
	 *            the iterator that includes column hints
	 * @return the matched column hint
	 */

	static ColumnHintHandle findColumnHint( String name, Iterator columnHints )
	{
		if ( name == null )
			return null;

		while ( columnHints.hasNext( ) )
		{
			ColumnHintHandle hint = (ColumnHintHandle) columnHints.next( );
			if ( name.equals( hint.getColumnName( ) ) )
				return hint;
		}

		return null;
	}

	/**
	 * Creates a ResultSetDefinition with the given ROM ResultSet columns.
	 * 
	 * @param romResultSet
	 *            the ROM result set columns.
	 * @return the created ResultSetDefinition
	 */

	ResultSetDefinition newOdaResultSetDefinition( OdaDataSetHandle setHandle )
	{
		Iterator romSets = setHandle.resultSetIterator( );
		String name = setHandle.getResultSetName( );

		if ( !romSets.hasNext( ) && StringUtil.isBlank( name ) )
			return null;

		ResultSetDefinition odaSetDefn = null;
		ResultSetColumns odaSetColumns = null;

		if ( !StringUtil.isBlank( name ) )
		{
			odaSetDefn = ODADesignFactory.getFactory().createResultSetDefinition( );
			odaSetDefn.setName( name );
		}

		while ( romSets.hasNext( ) )
		{
			if ( odaSetDefn == null )
				odaSetDefn = ODADesignFactory.getFactory()
						.createResultSetDefinition( );

			if ( odaSetColumns == null )
				odaSetColumns = ODADesignFactory.getFactory()
						.createResultSetColumns( );

			OdaResultSetColumnHandle setColumn = (OdaResultSetColumnHandle) romSets
					.next( );

			// get the colum hint

			ColumnHintHandle hint = findColumnHint(
					(OdaResultSetColumn) setColumn.getStructure( ), setHandle
							.columnHintsIterator( ) );

			ColumnDefinition columnDefn = ODADesignFactory.getFactory()
					.createColumnDefinition( );

			String newName = setColumn.getNativeName( );
			if ( StringUtil.isBlank( newName ) )
				newName = setColumn.getColumnName( );
			DataElementAttributes dataAttrs = ODADesignFactory.getFactory()
					.createDataElementAttributes( );
			dataAttrs.setName( newName );

			Integer position = setColumn.getPosition( );
			if ( position != null )
				dataAttrs.setPosition( setColumn.getPosition( ).intValue( ) );

			Integer nativeDataType = setColumn.getNativeDataType( );
			if ( nativeDataType != null )
				dataAttrs.setNativeDataTypeCode( nativeDataType.intValue( ) );

			columnDefn.setAttributes( dataAttrs );
			odaSetColumns.getResultColumnDefinitions( ).add( columnDefn );

			if ( hint == null )
				continue;

			// update display name

			String displayName = hint.getDisplayName( );
			if ( displayName != null )
			{
				DataElementUIHints uiHints = ODADesignFactory.getFactory()
						.createDataElementUIHints( );
				uiHints.setDisplayName( displayName );
				dataAttrs.setUiHints( uiHints );
			}
			else
				dataAttrs.setUiHints( null );

			// update usage hints.

			OutputElementAttributes outputAttrs = null;

			String helpText = hint.getHelpText( );
			String format = hint.getFormat( );
			if ( helpText != null || format != null )
			{
				outputAttrs = ODADesignFactory.getFactory()
						.createOutputElementAttributes( );
				outputAttrs.setHelpText( helpText );
				if ( format != null )
				{
					ValueFormatHints formatHint = ODADesignFactory.getFactory()
							.createValueFormatHints( );
					formatHint.setDisplayFormat( format );
					outputAttrs.setFormattingHints( formatHint );
				}
			}
			columnDefn.setUsageHints( outputAttrs );
		}

		odaSetDefn.setResultSetColumns( odaSetColumns );
		return odaSetDefn;
	}

	/**
	 * The data strcuture to hold a result set column and its column hint.
	 * 
	 */

	static class ResultSetColumnInfo
	{

		private OdaResultSetColumn column;
		private ColumnHint hint;

		ResultSetColumnInfo( OdaResultSetColumn column, ColumnHint hint )
		{
			this.column = column;
			this.hint = hint;
		}

		/**
		 * Distributes result set columns and column hints to different lists.
		 * 
		 * @param infos
		 *            the list containing result set column info
		 * @param columns
		 *            the list containing result set column
		 * @param hints
		 *            the list containing column hint
		 */

		static void updateResultSetColumnList( List infos, List columns,
				List hints )
		{
			if ( infos == null || infos.isEmpty( ) )
				return;

			if ( columns == null || hints == null )
				throw new IllegalArgumentException( "input list cannot be null" ); //$NON-NLS-1$
			for ( int i = 0; i < infos.size( ); i++ )
			{
				ResultSetColumnInfo info = (ResultSetColumnInfo) infos.get( i );
				columns.add( info.column );

				if ( info.hint != null )
					hints.add( info.hint );
			}
		}
	}
}
