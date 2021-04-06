/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.core.model.schematic.GridHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.TableHandleAdapter;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.TableHandle;

/**
 * Check the table and grid layout infomation
 */

public class TableHandleLayoutCheck extends AbstractLayoutCheck {

	@Override
	public boolean layoutCheck(Object model) {
		TableHandleAdapter adapter;
		if (model instanceof TableHandle) {
			adapter = new TableHandleAdapter((TableHandle) model, null);
		} else if (model instanceof GridHandle) {
			adapter = new GridHandleAdapter((GridHandle) model, null);
		} else {
			return true;
		}

		// TableHandleAdapter adapter = new
		// TableHandleAdapter((TableHandle)model, null);
		try {
			adapter.reload();
		} catch (Exception e) {
			return false;
		}

		int columnCount = adapter.getColumnCount();

		List rows = adapter.getRows();
		if (!getTrueColumnNumber(rows, columnCount)) {
			return false;// the column number is not correct
		}
		return true;
	}

	private boolean getTrueColumnNumber(List rows, int modelColumnCount) {
		Map<Integer, Integer> addRowMap = new HashMap<Integer, Integer>();
		List list = new ArrayList();
		for (int i = 0; i < rows.size(); i++) {
			int count = 0;
			RowHandle rowHandle = (RowHandle) rows.get(i);
			List children = rowHandle.getCells().getContents();
			for (int j = 0; j < children.size(); j++) {
				CellHandle cellHandle = (CellHandle) children.get(j);

				int rowSpan = cellHandle.getRowSpan();
				int columnSpan = cellHandle.getColumnSpan();
				if (!list.contains(cellHandle)) {
					for (int k = 2; k <= rowSpan; k++) {
						if (i + k > rows.size()) {
							return false;// The row span over
						}
						addColumnCounts(i + k, columnSpan, addRowMap);
					}
				}
				count = count + 1 + columnSpan - 1;
				list.add(cellHandle);
			}
			Integer addSize = addRowMap.get(i + 1);
			if (addSize != null) {
				count = addSize + count;
			}
			if (count != modelColumnCount) {
				return false;
			}
		}

		return true;
	}

	private void addColumnCounts(int rowNumber, int size, Map<Integer, Integer> addRowMap) {
		Integer ori = addRowMap.get(rowNumber);
		if (ori == null) {
			addRowMap.put(rowNumber, size);
		} else {
			addRowMap.put(rowNumber, ori + size);
		}
	}
}
