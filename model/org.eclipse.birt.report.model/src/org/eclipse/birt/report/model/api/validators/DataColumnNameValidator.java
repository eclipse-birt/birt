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

package org.eclipse.birt.report.model.api.validators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.DataItem;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.interfaces.IDataItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.util.DataBoundColumnUtil;
import org.eclipse.birt.report.model.validators.AbstractElementValidator;

/**
 * Validates the result set column of the data item.
 * 
 * <h3>Rule</h3>
 * The rule is that
 * <ul>
 * <li>If data item has a column name and this column name has no corresponding
 * column binding, semantic error is logged.
 * </ul>
 * 
 * <h3>Applicability</h3>
 * This validator is only applied to <code>DataItem</code>.
 */

public class DataColumnNameValidator extends AbstractElementValidator
{

	private static DataColumnNameValidator instance = new DataColumnNameValidator( );

	/**
	 * Returns the singleton validator instance.
	 * 
	 * @return the validator instance
	 */

	public static DataColumnNameValidator getInstance( )
	{
		return instance;
	}

	/**
	 * Validates whether the page size is invalid.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the master page to validate
	 * 
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List validate( Module module, DesignElement element )
	{
		if ( !( element instanceof DataItem ) )
			return Collections.EMPTY_LIST;

		return doValidate( module, (DataItem) element );
	}

	/**
	 * Validates the data item.
	 * 
	 * @param module
	 * @param toValidate
	 * @return the list containing semantic errors.
	 */

	private List doValidate( Module module, DataItem toValidate )
	{
		List list = new ArrayList( );

		// find the corresponding data column in the data binding. If not find
		// logs the error.

		String columnName = (String) toValidate.getLocalProperty( module,
				IDataItemModel.RESULT_SET_COLUMN_PROP );

		if ( columnName == null )
			return list;

		if ( !hasCorrespondingColumnBinding( module, toValidate, columnName ) )
		{
			list.add( new SemanticError( toValidate, new String[]{columnName},
					SemanticError.DESIGN_EXCEPTION_MISSING_COLUMN_BINDING ) );
		}

		return list;
	}

	/**
	 * @param columnBindingName
	 * @return <code>true</code> if the target has the column name.
	 *         <code>false</code> otherwise.
	 */

	private static boolean hasCorrespondingColumnBinding( Module module,
			DesignElement target, String columnBindingName )
	{
		DesignElement tmpElement = DataBoundColumnUtil
				.findTargetOfBoundColumns( target, module );

		if ( tmpElement instanceof GroupElement )
		{
			tmpElement = tmpElement.getContainer( );
			List columns = (List) tmpElement.getProperty( module,
					IReportItemModel.BOUND_DATA_COLUMNS_PROP );

			if ( exists( columns, columnBindingName ) )
				return true;
		}
		else
		{

			List columns = (List) tmpElement.getProperty( module,
					IReportItemModel.BOUND_DATA_COLUMNS_PROP );

			if ( exists( columns, columnBindingName ) )
				return true;

		}

		return false;
	}

	/**
	 * Tests whether the given expression has corresponding column binding in
	 * the given list.
	 * 
	 * @param columns
	 *            the binding columns
	 * @param columnName
	 *            the old value expression in BIRT 2.1M5
	 * @return <code>true</code> if the expression exists in the columns.
	 *         Otherwise, <code>false</code>.
	 */

	private static boolean exists( List columns, String columnName )
	{
		if ( getColumn( columns, columnName ) == null )
			return false;

		return true;
	}

	/**
	 * Gets the column with the given expression bound the given list.
	 * 
	 * @param columns
	 *            the binding columns
	 * @param name
	 *            the column binding name
	 * @return the bound column
	 */

	public static ComputedColumn getColumn( List columns, String name )
	{
		if ( ( columns == null ) || ( columns.size( ) == 0 ) || name == null )
			return null;

		for ( int i = 0; i < columns.size( ); i++ )
		{
			ComputedColumn column = (ComputedColumn) columns.get( i );
			if ( name.equals( column.getName( ) ) )
				return column;
		}
		return null;
	}
}