/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api.validators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.validators.AbstractElementValidator;

/**
 * Validates all cells in one row don't overlap each other.
 *
 * <h3>Rule</h3> The rule is that all cells in the given row shouldn't overlap
 * each other.
 *
 * <h3>Applicability</h3> This validator is only applied to
 * <code>TableRow</code>.
 *
 */

public class CellOverlappingValidator extends AbstractElementValidator {

	private final static CellOverlappingValidator instance = new CellOverlappingValidator();

	/**
	 * Returns the singleton validator instance.
	 *
	 * @return the validator instance
	 */

	public static CellOverlappingValidator getInstance() {
		return instance;
	}

	/**
	 * Validates whether any cell in the given row overlaps others.
	 *
	 * @param module  the module
	 * @param element the row to validate
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	@Override
	public List<SemanticException> validate(Module module, DesignElement element) {
		if (!(element instanceof TableRow) || (element.getContainer() == null)) {
			return Collections.emptyList();
		}

		return doValidate(module, (TableRow) element);
	}

	private List<SemanticException> doValidate(Module module, TableRow toValidate) {
		List<SemanticException> list = new ArrayList<>();

		// Get the slot containing this row

		int slotId = toValidate.getContainer().findSlotOf(toValidate);
		ContainerSlot slot = toValidate.getContainer().getSlot(slotId);

		// Verify that no cells overlap.

		int colCount = toValidate.getColumnCount(module);

		// if the column count is zero or negative, it means that the
		// cells in the row may have some semantic errors. Since the check
		// of the cells is done before the check of the row, the semantic
		// errors are collected correctly. Therefore, we can jump it if
		// the column count is not positive.

		if (colCount <= 0) {
			return list;
		}

		boolean ok = true;
		boolean cols[] = new boolean[colCount];
		int rowPosn = slot.findPosn(toValidate);
		int rowCount = slot.getCount();
		int cellCount = toValidate.getContentsSlot().size();
		int impliedPosn = 0;
		for (int i = 0; i < cellCount; i++) {
			Cell cell = (Cell) toValidate.getContentsSlot().get(i);
			int colPosn = cell.getColumn(module);
			int colSpan = cell.getColSpan(module);
			int rowSpan = cell.getRowSpan(module);

			if (colPosn > 0) {
				colPosn--;
			} else {
				colPosn = impliedPosn;
			}

			// Check the horizontal and vertical cell span

			if (!checkColSpan(cols, colPosn, colSpan) || !checkRowSpan(rowCount, rowPosn, rowSpan)) {
				ok = false;
			}

			impliedPosn = colPosn + colSpan;
		}

		if (!ok) {
			DesignElement container = toValidate.getContainer();

			// The container of the row can be TableGroup, table and grid

			if (container instanceof TableGroup) {
				// get the table containing the table group

				container = container.getContainer();
			} else {
				assert container instanceof TableItem || container instanceof GridItem;
			}
			list.add(new SemanticError(toValidate, new String[] { container.getElementName(), container.getFullName() },
					SemanticError.DESIGN_EXCEPTION_OVERLAPPING_CELLS));
		}

		return list;
	}

	/**
	 * Checks whether the cell horizontal overlap exists.
	 *
	 * @param cols    column array which records the cell allocation
	 * @param colPosn column position of the cell
	 * @param colSpan column span of the cell
	 * @return whether the horizontal overlap exists
	 */

	private boolean checkColSpan(boolean cols[], int colPosn, int colSpan) {
		boolean ok = true;

		for (int j = 0; j < colSpan; j++) {
			if (cols[colPosn + j]) {
				ok = false;
			}
			cols[colPosn + j] = true;
		}

		return ok;
	}

	/**
	 * Checks whether the cell vertical overlap exists.
	 *
	 * @param rowCount row count of the band this cell belongs to
	 * @param rowPosn  row position of this cell in the band
	 * @param rowSpan  row span of the cell
	 * @return whether the vertical overlap exists
	 */

	private boolean checkRowSpan(int rowCount, int rowPosn, int rowSpan) {
		return (rowCount - rowPosn - rowSpan) >= 0;
	}

}
