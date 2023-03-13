/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.elements.SemanticError;

/**
 * Does table row copy operation.
 */

class RowBandCopyAction extends RowBandAction {
	/**
	 * Constructs a <code>RowBandAdapter</code> for the copy action.
	 *
	 * @param adapter the adapter to work on tables and grids.
	 *
	 */

	RowBandCopyAction(RowBandAdapter adapter) {
		super(adapter);
	}

	/**
	 * Checks if copy action can do with the given parameters .
	 *
	 * @param parameters parameters needed by insert operation.
	 * @return <code>true</code> returns if can do the action , otherwise return
	 *         <code>false</code>
	 */

	protected boolean canCopy(RowOperationParameters parameters) {
		int destIndex = parameters.getDestIndex();

		SlotHandle slotHandle = getSlotHandle(parameters);
		if (slotHandle == null || slotHandle.getCount() == 0 || destIndex < 0 || destIndex >= slotHandle.getCount()) {
			return false;
		}
		RowHandle rowHandle = (RowHandle) slotHandle.get(destIndex);

		if (!containsRowSpan(rowHandle) && isRectangleArea(rowHandle)) {
			return true;
		}

		return false;
	}

	/**
	 * Copies table row with the given parameters.
	 *
	 * @param parameters parameters needed by insert operation.
	 * @return the copied table row.
	 * @throws SemanticException
	 */

	protected IDesignElement doCopy(RowOperationParameters parameters) throws SemanticException {
		if (!canCopy(parameters)) {
			throw new SemanticError(adapter.getElementHandle().getElement(),
					new String[] { adapter.getElementHandle().getName() },
					SemanticError.DESIGN_EXCEPTION_ROW_COPY_FORBIDDEN);
		}
		int destIndex = parameters.getDestIndex();

		SlotHandle slotHandle = getSlotHandle(parameters);
		RowHandle rowHandle = (RowHandle) slotHandle.get(destIndex);
		return copyRow(rowHandle);
	}

}
