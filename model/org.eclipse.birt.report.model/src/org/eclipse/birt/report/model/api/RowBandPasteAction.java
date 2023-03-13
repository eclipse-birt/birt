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
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.util.CommandLabelFactory;

/**
 * Does table row paste operation.
 *
 */
public class RowBandPasteAction extends RowBandAction {

	/**
	 * Constructs a <code>RowBandAdapter</code> for the paste action.
	 *
	 * @param adapter the adapter to work on tables and grids.
	 *
	 */

	public RowBandPasteAction(RowBandAdapter adapter) {
		super(adapter);
	}

	/**
	 * Checks whether the paste operation can be done with the given copied column
	 * band data, the column index and the operation flag.
	 *
	 * @param clonedRow  the copied table row.
	 * @param parameters parameters needed by insert operation.
	 * @return <code>true</code> indicates the paste operation can be done.
	 *         Otherwise <code>false</code>.
	 */

	protected boolean canPaste(TableRow clonedRow, RowOperationParameters parameters) {
		// if table has parent, its layout can't be changed. so can't do insert
		// operation.

		if (adapter.hasParent()) {
			return false;
		}

		int destIndex = parameters.getDestIndex();

		int desColumnCount = adapter.getColumnCount();
		SlotHandle slotHandle = getSlotHandle(parameters);
		if ((slotHandle == null) || destIndex < 0 || destIndex >= slotHandle.getCount()) {
			return false;
		}

		RowHandle destHandle = (RowHandle) slotHandle.get(destIndex);

		int count = adapter.computeRowCount(clonedRow);
		if ((count == desColumnCount) && isRectangleArea(destHandle) && !containsRowSpan(destHandle)) {
			return true;
		}

		return false;
	}

	/**
	 * Pastes the given table row to target row with the given slot id , group id
	 * and destination index.
	 *
	 * @param copiedRow  the copied table row.
	 * @param parameters parameters needed by insert operation.
	 * @throws SemanticException
	 *
	 */

	protected void doPaste(TableRow copiedRow, RowOperationParameters parameters) throws SemanticException {

		if (!canPaste(copiedRow, parameters)) {
			throw new SemanticError(adapter.getElementHandle().getElement(),
					new String[] { adapter.getElementHandle().getName() },
					SemanticError.DESIGN_EXCEPTION_ROW_PASTE_FORBIDDEN);
		}

		int destIndex = parameters.getDestIndex();
		SlotHandle slotHandle = getSlotHandle(parameters);

		ActivityStack stack = adapter.getModule().getActivityStack();
		try {
			stack.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.PASTE_ROW_MESSAGE));
			adapter.getModule().getModuleHandle().rename(copiedRow.getHandle(slotHandle.getModule()));

			// when paste , there is no need to adjust the position

			slotHandle.drop(destIndex);

			// add table row and do semantic check

			slotHandle.paste(copiedRow.getHandle(slotHandle.getModule()), destIndex);
		} catch (SemanticException e) {
			stack.rollback();
			throw e;
		}
		stack.commit();

	}

}
