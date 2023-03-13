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
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.util.CommandLabelFactory;

/**
 * Does table row insert operation.
 *
 */

public class RowBandInsertAction extends RowBandAction {

	/**
	 * Constructs a <code>RowBandAdapter</code> for the insert action.
	 *
	 * @param adapter the adapter to work on tables and grids.
	 *
	 */

	public RowBandInsertAction(RowBandAdapter adapter) {
		super(adapter);
	}

	/**
	 * Checks whether the insert operation can be done with the given slot id ,
	 * group id , index and the operation flag.
	 *
	 * @param copiedRow  source copied table row
	 * @param parameters parameters needed by insert operation.
	 * @return <code>true</code> indicates the insert operation can be done.
	 *         Otherwise <code>false</code>.
	 */

	protected boolean canInsert(RowOperationParameters parameters) {
		// if table has parent, its layout can't be changed. so can't do insert
		// operation.

		if (adapter.hasParent()) {
			return false;
		}

		int destIndex = parameters.getDestIndex();

		SlotHandle slotHandle = getSlotHandle(parameters);
		if ((slotHandle == null) || destIndex < 0 || destIndex > slotHandle.getCount()) {
			return false;
		}

		if (destIndex == 0) {
			return true;
		}

		// check the upper row of the target position is rectangle and hasn't
		// row span.

		RowHandle destHandle = (RowHandle) slotHandle.get(destIndex - 1);
		if (!containsRowSpan(destHandle) && isRectangleArea(destHandle)) {
			return true;
		}

		return false;
	}

	/**
	 * Inserts source table row below or above target table row. Table row can be
	 * inserted in position from zero to count of rows.
	 *
	 * @param copiedRow  the copied table row.
	 * @param parameters parameters needed by insert operation.
	 * @throws SemanticException
	 * @throws Exception
	 */

	protected void doInsert(RowOperationParameters parameters) throws SemanticException {
		// new empty table row.

		ElementFactory factory = new ElementFactory(adapter.getModule());
		RowHandle rowHandle = factory.newTableRow(adapter.getColumnCount());

		if (!canInsert(parameters)) {
			throw new SemanticError(adapter.getElementHandle().getElement(),
					new String[] { adapter.getElementHandle().getName() },
					SemanticError.DESIGN_EXCEPTION_ROW_INSERT_FORBIDDEN);
		}

		int destIndex = parameters.getDestIndex();

		SlotHandle slotHandle = getSlotHandle(parameters);
		ActivityStack stack = adapter.getModule().getActivityStack();
		try {
			stack.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.INSERT_ROW_MESSAGE));
			slotHandle.paste(rowHandle, destIndex);
		} catch (SemanticException e) {
			stack.rollback();
			throw e;
		}

		stack.commit();

	}
}
