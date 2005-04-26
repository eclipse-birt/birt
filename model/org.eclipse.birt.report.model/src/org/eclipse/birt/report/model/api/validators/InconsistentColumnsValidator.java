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

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.validators.AbstractElementValidator;

/**
 * Validates the column number in columns slot and the maximum column number in
 * rows should be consistent.
 * 
 * <h3>Rule</h3>
 * The rule is that the column number in columns slot should be same with the
 * maximum column number in rows of other slots.
 * 
 * <h3>Applicability</h3>
 * This validator is only applied to <code>GridItem</code> and
 * <code>TableItem</code>.
 * 
 */

public class InconsistentColumnsValidator extends AbstractElementValidator
{

	private static InconsistentColumnsValidator instance = new InconsistentColumnsValidator( );

	/**
	 * Returns the singleton validator instance.
	 * 
	 * @return the validator instance
	 */

	public static InconsistentColumnsValidator getInstance( )
	{
		return instance;
	}

	/**
	 * Validates whether the page size is invalid.
	 * 
	 * @param design
	 *            the report design
	 * @param element
	 *            the master page to validate
	 * 
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List validate( ReportDesign design, DesignElement element )
	{
		DesignElement toValidate = element;

		if ( !( toValidate instanceof GridItem )
				&& !( toValidate instanceof TableItem ) )
			return Collections.EMPTY_LIST;

		return doValidate( design, toValidate );
	}

	private List doValidate( ReportDesign design, DesignElement element )
	{
		List list = new ArrayList( );

		// If column definitions are defined, then they must describe the
		// number of columns actually used by the table. It is legal to
		// have a table with zero columns.

		int colDefnCount = getColDefnCount( design, element );
		int maxCols = findMaxCols( design, element );
		if ( colDefnCount != maxCols && colDefnCount != 0 )
		{
			String errorCode = SemanticError.DESIGN_EXCEPTION_INCONSITENT_TABLE_COL_COUNT;
			if ( hasDroppingCell( element ) )
				errorCode = SemanticError.DESIGN_EXCEPTION_INCONSITENT_TABLE_COL_COUNT_WITH_DROP;

			if ( element instanceof GridItem )
				errorCode = SemanticError.DESIGN_EXCEPTION_INCONSITENT_GRID_COL_COUNT;

			list.add( new SemanticError( element, errorCode ) );
		}
		return list;
	}

	/**
	 * Gets the number of columns described in the column definition section.
	 * 
	 * @param design
	 *            the report design
	 * @param element
	 *            grid or table
	 * @return the number of columns described by column definitions
	 */

	private int getColDefnCount( ReportDesign design, DesignElement element )
	{
		if ( element instanceof GridItem )
			return ( (GridItem) element ).getColDefnCount( design );

		return ( (TableItem) element ).getColDefnCount( design );
	}

	/**
	 * Finds the maximum column width for this grid/table.
	 * 
	 * @param design
	 *            the report design
	 * @param element
	 *            grid or table
	 * @return the maximum number of columns
	 */

	private int findMaxCols( ReportDesign design, DesignElement element )
	{
		if ( element instanceof GridItem )
			return ( (GridItem) element ).findMaxCols( design );

		return ( (TableItem) element ).findMaxCols( design );
	}

	/**
	 * Checks whether there is any cell that has "drop" property.
	 * 
	 * @param element
	 *            a grid or table element
	 * @return <code>true</code> if any cell has the "drop" property,
	 *         otherwise <code>false</code>.
	 */

	private boolean hasDroppingCell( DesignElement element )
	{
		if ( element instanceof GridItem )
			return false;

		ContainerSlot groups = element.getSlot( ListingElement.GROUP_SLOT );
		int groupCount = groups.getCount( );

		// check on group header by group header. From the outer to the
		// inner-most.

		for ( int groupIndex = 0; groupIndex < groupCount; groupIndex++ )
		{
			TableGroup group = (TableGroup) groups.getContent( groupIndex );
			ContainerSlot header = group.getSlot( TableGroup.HEADER_SLOT );

			// only gets the last row.

			TableRow row = (TableRow) header
					.getContent( header.getCount( ) - 1 );
			ContainerSlot cells = row.getSlot( TableRow.CONTENT_SLOT );

			for ( int cellIndex = 0; cellIndex < cells.getCount( ); cellIndex++ )
			{
				Cell cell = (Cell) cells.getContent( cellIndex );
				String drop = (String) cell.getLocalProperty( null,
						Cell.DROP_PROP );

				if ( DesignChoiceConstants.DROP_TYPE_ALL
						.equalsIgnoreCase( drop )
						|| DesignChoiceConstants.DROP_TYPE_DETAIL
								.equalsIgnoreCase( drop ) )
					return true;
			}
		}
		return false;
	}
}