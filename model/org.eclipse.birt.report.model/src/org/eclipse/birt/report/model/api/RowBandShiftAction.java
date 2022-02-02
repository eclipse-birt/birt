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

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.util.CommandLabelFactory;

/**
 * Does table row shift operation. Shift is only allowed in the same slot
 * handle.
 */

public class RowBandShiftAction extends RowBandAction {

	/**
	 * Constructs a default <code>RowBandAdapter</code>.
	 * 
	 * @param adapter the adapter to work on tables and grids.
	 */

	public RowBandShiftAction(RowBandAdapter adapter) {
		super(adapter);
	}

	/**
	 * Checks whether the shift operation can be done with the given parameters
	 * 
	 * @param parameters parameters needed by insert operation.
	 * @return <code>true</code> indicates the shift operation can be done.
	 *         Otherwise <code>false</code>.
	 */

	protected boolean canShift(RowOperationParameters parameters) {
		// if table has parent, its layout can't be changed. so can't do insert
		// operation.

		if (adapter.hasParent())
			return false;

		int destIndex = parameters.getDestIndex();
		int sourceIndex = parameters.getSourceIndex();

		SlotHandle slotHandle = getSlotHandle(parameters);
		if (slotHandle == null)
			return false;
		if (sourceIndex < 0 || sourceIndex >= slotHandle.getCount())
			return false;
		if (destIndex < 0 || destIndex > slotHandle.getCount())
			return false;
		if (sourceIndex == destIndex)
			return false;

		RowHandle sourceHandle = (RowHandle) slotHandle.get(sourceIndex);
		if (destIndex > 0)
			--destIndex;

		// check source row and the upper of target row is rectangle and hasn't
		// row span.

		RowHandle destHandle = (RowHandle) slotHandle.get(destIndex);

		if (isRectangleArea(sourceHandle) && isRectangleArea(destHandle) && !containsRowSpan(sourceHandle)
				&& !containsRowSpan(destHandle)) {
			return true;
		}

		return false;

	}

	/**
	 * Does shift operation with the given parameters. Now only allow to shift table
	 * row in the same slot.
	 * 
	 * @param parameters parameters needed by insert operation.
	 * @throws SemanticException
	 */

	protected void doShift(RowOperationParameters parameters) throws SemanticException {
		if (!canShift(parameters))
			throw new SemanticError(adapter.getElementHandle().getElement(),
					new String[] { adapter.getElementHandle().getName() },
					SemanticError.DESIGN_EXCEPTION_ROW_SHIFT_FORBIDDEN);

		int destIndex = parameters.getDestIndex();
		int sourceIndex = parameters.getSourceIndex();
		SlotHandle slotHandle = getSlotHandle(parameters);
		ActivityStack stack = adapter.getModule().getActivityStack();
		try {
			stack.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.SHIFT_ROW_MESSAGE));

			// add source row to destination position.

			RowHandle rowHandle = (RowHandle) slotHandle.get(sourceIndex);
			IDesignElement copiedRow = copyRow(rowHandle);

			adapter.getModule().getModuleHandle().rename(copiedRow.getHandle(adapter.getModule()));

			// Shifting operation is seperated to droping and pasting operation.
			// So when shift table row from high to low position , should adjust
			// the position; else needn't do it.

			slotHandle.drop(sourceIndex);
			if ((sourceIndex < destIndex) && (destIndex > 0))
				--destIndex;
			slotHandle.paste(copiedRow.getHandle(slotHandle.getModule()), destIndex);
		} catch (SemanticException e) {
			stack.rollback();
			throw e;
		}
		stack.commit();

	}

}
