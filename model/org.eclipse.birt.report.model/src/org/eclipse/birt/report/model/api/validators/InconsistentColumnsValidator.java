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

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.SemanticError;
import org.eclipse.birt.report.model.elements.TableItem;
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

}