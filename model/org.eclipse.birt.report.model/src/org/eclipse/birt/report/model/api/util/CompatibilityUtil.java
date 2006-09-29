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

package org.eclipse.birt.report.model.api.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.CachedMetaDataHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.CachedMetaData;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * Provides the backward compatibility for the user. Now support:
 * 
 * <ul>
 * <li>Updated CachedMetaData.resultSet.
 * </ul>
 * 
 * <ul>
 * <li>Added DataSet.resultSet column.
 * </ul>
 * 
 * <p>
 * Backward compatibilty is different from other operations. It does not support
 * undo/redo and won't send out events.
 * 
 */

public class CompatibilityUtil
{

	/**
	 * Updates CachedMetaData.resultSet property with the given data set and the
	 * list of result set columns.
	 * 
	 * @param setHandle
	 *            the data set
	 * @param resultColumns
	 *            a list containing result set columns. Each item in the list is
	 *            {@link ResultSetColumn}
	 * @throws SemanticException
	 *             if any result set column in the list has invalid values.
	 */

	public static void updateResultSetinCachedMetaData(
			DataSetHandle setHandle, List resultColumns )
			throws SemanticException
	{
		if ( setHandle == null )
			return;

		if ( resultColumns == null || resultColumns.isEmpty( ) )
			return;

		CachedMetaDataHandle originalMetaData = setHandle
				.getCachedMetaDataHandle( );

		// uses the tmporary metadata value.

		CachedMetaData metaData = StructureFactory.createCachedMetaData( );
		metaData.setProperty( CachedMetaData.RESULT_SET_MEMBER, resultColumns );

		PropertyValueValidationUtil.validateProperty( setHandle,
				DataSetHandle.CACHED_METADATA_PROP, metaData );

		if ( originalMetaData == null )
		{
			setHandle.getElement( ).setProperty(
					DataSetHandle.CACHED_METADATA_PROP, metaData );
		}
		else
		{
			// clear metaData values.

			metaData.setProperty( CachedMetaData.RESULT_SET_MEMBER, null );
			metaData = null;

			metaData = (CachedMetaData) originalMetaData.getStructure( );
			metaData.setProperty( CachedMetaData.RESULT_SET_MEMBER,
					resultColumns );
		}
	}

	/**
	 * Adds the given structures to the corresponding property values. For
	 * example, adds result set columns to DataSet.resultSets.
	 * 
	 * @param propHandle
	 *            the property handle
	 * @param structures
	 *            the list containing structures
	 * @throws SemanticException
	 *             if any structure in the list has invalid values.
	 */

	public static void addStructures( PropertyHandle propHandle, List structures )
			throws SemanticException
	{
		if ( structures == null || structures.isEmpty( ) )
			return;

		if ( propHandle == null )
			return;

		DesignElementHandle element = propHandle.getElementHandle( );

		IPropertyDefn propDefn = propHandle.getDefn( );
		PropertyValueValidationUtil.validateProperty( element, propDefn
				.getName( ), structures );

		List oldList = element.getListProperty( propDefn.getName( ) );
		List newList = null;
		if ( !propHandle.isLocal( ) )
		{
			if ( oldList != null )
				newList = (List) ModelUtil.copyValue( propDefn, structures );
			else
				newList = new ArrayList( );

			element.getElement( ).setProperty( (ElementPropertyDefn) propDefn,
					newList );
		}
		else
			newList = oldList;

		newList.addAll( structures );
	}

	/**
	 * Adds ResultSetColumn without sending out event.
	 * 
	 * @param dataSetHandle
	 *            data set handle
	 * @param columns
	 *            list contains OdaResultSetColumn
	 * @throws SemanticException
	 *             if any result set column in the list has invalid values.
	 */

	public static void addResultSetColumn( DataSetHandle dataSetHandle,
			List columns ) throws SemanticException
	{
		if ( dataSetHandle == null )
			return;

		addStructures( dataSetHandle
				.getPropertyHandle( DataSetHandle.RESULT_SET_PROP ), columns );
	}
}
