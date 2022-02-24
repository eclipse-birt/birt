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
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.gef.EditPartViewer;

/**
 * @author Administrator
 *
 */
public class DeleteRowHandler extends SelectionHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		super.execute(event);

		TableEditPart part = getTableEditPart();
		if (part != null) {
			EditPartViewer viewer = part.getViewer();
			part.deleteRow(getRowNumbers());
			viewer.select(part);
		}

		return Boolean.TRUE;
	}

	/**
	 * Gets the current selected row objects.
	 * 
	 * @return The current selected row objects.
	 */

	protected List getRowHandles() {

		List list = getSelectedObjects();
		if (list.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		List rowHandles = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			if (obj instanceof DummyEditpart) {
				if (((DummyEditpart) obj).getModel() instanceof RowHandle) {
					rowHandles.add(((DummyEditpart) obj).getModel());
				}
			}
		}
		return rowHandles;
	}

	/**
	 * Gets row numbers of selected rows. And sorts the array of ints into ascending
	 * numerical order.
	 */
	protected int[] getRowNumbers() {
		List rowHandles = getRowHandles();
		if (rowHandles.isEmpty()) {
			return new int[0];
		}
		int size = rowHandles.size();
		int[] rowNumbers = new int[size];

		for (int i = 0; i < size; i++) {
			rowNumbers[i] = getRowNumber(rowHandles.get(i));
		}

		// sorts array before returning.
		int[] a = rowNumbers;
		Arrays.sort(a);
		return a;
	}

	/**
	 * Gets row number given the row handle.
	 * 
	 * @return The row number of the selected row object.
	 */
	public int getRowNumber(Object rowHandle) {
		return HandleAdapterFactory.getInstance().getRowHandleAdapter(rowHandle).getRowNumber();
	}

}
