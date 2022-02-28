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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.schematic.CellHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableUtil;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Use the short cut to select the column.
 */

public class SelectColumnAction extends ContextSelectionAction {

	/** action ID */
	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.SelectColumnAction"; //$NON-NLS-1$

	/**
	 * Constructs new instance
	 *
	 * @param part current work bench part
	 */
	public SelectColumnAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	@Override
	protected boolean calculateEnabled() {
		int[] intValue = getSelectionColumnNumbers();
		if (intValue == null || intValue.length == 0) {
			return false;
		}
		return TableUtil.isContinue(intValue);
	}

	/**
	 * Runs action.
	 *
	 */
	@Override
	public void run() {
		int[] intValue = getSelectionColumnNumbers();
		Arrays.sort(intValue);
		TableEditPart part = getTableEditPart();
		if (part != null) {
			part.selectColumn(intValue);
		}
	}

	private int[] getSelectionColumnNumbers() {
		List list = getElementHandles();
		if (!TableUtil.isAllCell(list)) {
			return new int[] {};
		}
		int size = list.size();
		List temp = new ArrayList();
		for (int i = 0; i < size; i++) {
			Object obj = list.get(i);
			CellHandleAdapter adapt = HandleAdapterFactory.getInstance().getCellHandleAdapter(obj);
			int colNumber = adapt.getColumnNumber();
			int colSpan = adapt.getColumnSpan();
			for (int j = colNumber; j < colNumber + colSpan; j++) {
				Integer value = j;
				if (!temp.contains(value)) {
					temp.add(value);
				}
			}
		}
		size = temp.size();
		int[] retValue = new int[size];
		for (int i = 0; i < size; i++) {
			Integer value = (Integer) temp.get(i);
			retValue[i] = value.intValue();
		}
		return retValue;
	}
}
