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

import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.SemanticError;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.validators.AbstractElementValidator;

/**
 * Validates all cells in one row don't overlap each other.
 * 
 * <h3>Rule</h3>
 * The rule is that all cells in the given row shouldn't overlap each other.
 * 
 * <h3>Applicability</h3>
 * This validator is only applied to <code>TableRow</code>.
 *  
 */

public class CellOverlappingValidator extends AbstractElementValidator
{

	private final static CellOverlappingValidator instance = new CellOverlappingValidator( );

	/**
	 * Returns the singleton validator instance.
	 * 
	 * @return the validator instance
	 */

	public static CellOverlappingValidator getInstance( )
	{
		return instance;
	}

	/**
	 * Validates whether any cell in the given row overlaps others.
	 * 
	 * @param design
	 *            the report design
	 * @param element
	 *            the row to validate
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List validate( ReportDesign design, DesignElement element )
	{
		if ( !( element instanceof TableRow ) )
			return Collections.EMPTY_LIST;

		if ( element.getContainer( ) == null )
			return Collections.EMPTY_LIST;

		return doValidate( design, (TableRow) element );
	}

	private List doValidate( ReportDesign design, TableRow toValidate )
	{
		List list = new ArrayList( );

		// Get the slot containing this row

		int slotId = toValidate.getContainer( ).findSlotOf( toValidate );
		ContainerSlot slot = toValidate.getContainer( ).getSlot( slotId );

		// Verify that no cells overlap.

		int colCount = toValidate.getColumnCount( design );

		// if the column count is zero or negative, it means that the
		// cells in the row may have some semantic errors. Since the check
		// of the cells is done before the check of the row, the semantic
		// errors are collected correctly. Therefore, we can jump it if
		// the column count is not positive.

		if ( colCount <= 0 )
			return list;

		boolean ok = true;
		boolean cols[] = new boolean[colCount];
		int rowPosn = slot.findPosn( toValidate );
		int rowCount = slot.getCount( );
		int cellCount = toValidate.getContentsSlot( ).size( );
		int impliedPosn = 0;
		for ( int i = 0; i < cellCount; i++ )
		{
			Cell cell = (Cell) toValidate.getContentsSlot( ).get( i );
			int colPosn = cell.getColumn( design );
			int colSpan = cell.getColSpan( design );
			int rowSpan = cell.getRowSpan( design );

			if ( colPosn > 0 )
				colPosn--;
			else
				colPosn = impliedPosn;

			// Check the horizontal and vertical cell span

			if ( !checkColSpan( cols, cell, colPosn, colSpan )
					|| !checkRowSpan( rowCount, rowPosn, rowSpan ) )
				ok = false;

			impliedPosn = colPosn + colSpan;
		}

		if ( !ok )
			list.add( new SemanticError( toValidate,
					SemanticError.DESIGN_EXCEPTION_OVERLAPPING_TABLE_CELLS ) );

		return list;
	}

	/**
	 * Checks whether the cell horizontal overlap exists.
	 * 
	 * @param cols
	 *            column array which records the cell allocation
	 * @param cell
	 *            cell element to check
	 * @param colPosn
	 *            column position of the cell
	 * @param colSpan
	 *            column span of the cell
	 * @return whether the horizontal overlap exists
	 */

	private boolean checkColSpan( boolean cols[], Cell cell, int colPosn,
			int colSpan )
	{
		boolean ok = true;

		for ( int j = 0; j < colSpan; j++ )
		{
			if ( cols[colPosn + j] )
				ok = false;
			cols[colPosn + j] = true;
		}

		return ok;
	}

	/**
	 * Checks whether the cell vertical overlap exists.
	 * 
	 * @param rowCount
	 *            row count of the band this cell belongs to
	 * @param rowPosn
	 *            row position of this cell in the band
	 * @param rowSpan
	 *            row span of the cell
	 * @return whether the vertical overlap exists
	 */

	private boolean checkRowSpan( int rowCount, int rowPosn, int rowSpan )
	{
		return ( rowCount - rowPosn - rowSpan ) >= 0;
	}

}