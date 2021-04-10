/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.model.api;

import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.elements.TableColumn;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.util.CommandLabelFactory;

/**
 * Provides the paste operation to the column band in the grid/table.
 * 
 */

class ColumnBandPasteAction extends ColumnBandCopyAction {

	/**
	 * Constructs a <code>ColumnBandPasteAction</code> for the paste action.
	 * 
	 * @param adapter the adapter to work on tables and grids.
	 * 
	 */

	public ColumnBandPasteAction(ColumnBandAdapter adapter) {
		super(adapter);
	}

	/**
	 * Checks whether the paste operation can be done with the given copied column
	 * band data, the column index and the operation flag.
	 * 
	 * @param columnIndex the column index
	 * @param inForce     <code>true</code> indicates to paste the column regardless
	 *                    of the different layout of cells. <code>false</code>
	 *                    indicates not.
	 * @param data        the copied column band data
	 * @return <code>true</code> indicates the paste operation can be done.
	 *         Otherwise <code>false</code>.
	 */

	protected boolean canPaste(int columnIndex, boolean inForce, ColumnBandData data) {
		// if table has parent, its layout can't be changed. so can't do insert
		// operation.

		if (adapter.hasParent())
			return false;

		List cells = data.getCells();

		List originalCells = getCellsContextInfo(adapter.getCellsUnderColumn(columnIndex));

		if (!isRectangleArea(originalCells, 1))
			return false;

		boolean isSameLayout = false;

		try {
			isSameLayout = isSameLayout(cells, originalCells);
		} catch (SemanticException e) {
			return false;
		}

		if (!inForce && !isSameLayout)
			return false;

		return true;
	}

	/**
	 * Pastes a column to the given <code>target</code>.
	 * 
	 * @param columnIndex the column number
	 * @param inForce     <code>true</code> if paste regardless of the difference of
	 *                    cell layouts, otherwise <code>false</code>.
	 * @param data        the copied column band data
	 * @return a list containing post-parsing errors. Each element in the list is
	 *         <code>ErrorDetail</code>.
	 * @throws SemanticException if layouts of slots are different. Or,
	 *                           <code>inForce</code> is <code>false</code> and the
	 *                           layout of cells are different.
	 */

	protected List pasteColumnBand(int columnIndex, boolean inForce, ColumnBandData data) throws SemanticException {
		boolean canDone = canPaste(columnIndex, inForce, data);

		if (inForce && !canDone)
			throw new SemanticError(adapter.getElementHandle().getElement(),
					new String[] { adapter.getElementHandle().getName() },
					SemanticError.DESIGN_EXCEPTION_COLUMN_PASTE_FORBIDDEN);

		if (!inForce && !canDone)
			throw new SemanticError(adapter.getElementHandle().getElement(),
					SemanticError.DESIGN_EXCEPTION_COLUMN_PASTE_DIFFERENT_LAYOUT);

		TableColumn column = data.getColumn();
		List cells = data.getCells();
		List originalCells = getCellsContextInfo(adapter.getCellsUnderColumn(columnIndex));

		ActivityStack as = adapter.getModule().getActivityStack();
		try {
			if (adapter instanceof TableColumnBandAdapter)
				as.startSilentTrans(CommandLabelFactory.getCommandLabel(MessageConstants.PASTE_COLUMN_BAND_MESSAGE));
			else
				as.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.PASTE_COLUMN_BAND_MESSAGE));

			pasteColumn(column, columnIndex, false);
			pasteCells(cells, originalCells, columnIndex, false);
		} catch (SemanticException e) {
			as.rollback();
			throw e;
		}
		as.commit();

		return doPostPasteCheck(column, cells);
	}

}
