/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
/**
 * 
 */

package org.eclipse.birt.report.designer.internal.ui.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DummyEditpart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.gef.EditPartViewer;

/**
 * @author Administrator
 *
 */
public class DeleteColumnHandler extends SelectionHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		super.execute(event);

		TableEditPart part = getTableEditPart();
		if (part != null) {
			EditPartViewer viewer = part.getViewer();
			part.deleteColumn(getColumnNumbers());
			viewer.select(part);
		}
		return Boolean.TRUE;
	}

	/**
	 * Gets column numbers of selected columns.. And sorts the array of ints into
	 * ascending numerical order.
	 */
	public int[] getColumnNumbers() {
		List columnHandles = getColumnHandles();
		if (columnHandles.isEmpty()) {
			return new int[0];
		}
		int size = columnHandles.size();
		int[] colNumbers = new int[size];

		for (int i = 0; i < size; i++) {
			colNumbers[i] = getColumnNumber(columnHandles.get(i));
		}

		// sorts array before returning.
		int[] a = colNumbers;
		Arrays.sort(a);
		return a;
	}

	/**
	 * Gets the current selected column objects.
	 * 
	 * @return The current column objects
	 */
	protected List getColumnHandles() {
		List list = getSelectedObjects();
		if (list.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		List columnHandles = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			if (obj instanceof DummyEditpart) {
				if (((DummyEditpart) obj).getModel() instanceof ColumnHandle) {
					columnHandles.add(((DummyEditpart) obj).getModel());
				}
			}
		}
		return columnHandles;
	}

	/**
	 * Gets column number given the column handle.
	 * 
	 * @return the column number
	 */
	public int getColumnNumber(Object columnHandle) {
		return HandleAdapterFactory.getInstance().getColumnHandleAdapter(columnHandle).getColumnNumber();
	}

}
