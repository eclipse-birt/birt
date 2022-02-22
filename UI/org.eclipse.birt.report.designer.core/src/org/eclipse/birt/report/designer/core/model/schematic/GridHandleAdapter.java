/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
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

package org.eclipse.birt.report.designer.core.model.schematic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.IModelAdapterHelper;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * Adapter class to adapt model handle. This adapter provides convenience.
 * methods to GUI requirement GridHandleAdapter responds to model GridHandle
 *
 */

public class GridHandleAdapter extends TableHandleAdapter {

	/**
	 * Constructor
	 *
	 * @param table
	 * @param mark
	 */
	public GridHandleAdapter(GridHandle grid, IModelAdapterHelper mark) {
		super(grid, mark);
	}

	/**
	 * Gets the Children iterator. This children relationship is determined by GUI
	 * requirement. This is not the model children relationship.
	 *
	 * @return Children iterator
	 */
	@Override
	public List getChildren() {
		List children = new ArrayList();

		SlotHandle rows = getGridHandle().getRows();

		for (Iterator it = rows.iterator(); it.hasNext();) {
			children.addAll(((RowHandle) it.next()).getCells().getContents());
		}
		removePhantomCells(children);
		return children;
	}

	/**
	 * Gets the all columns list
	 *
	 * @return
	 */
	@Override
	public List getColumns() {
		return getGridHandle().getColumns().getContents();
	}

	private GridHandle getGridHandle() {
		return (GridHandle) getHandle();
	}

	/**
	 * Gets all rows list.
	 *
	 * @return The rows list.
	 */
	@Override
	protected void buildRowInfo() {
		insertRowInfo(getGridHandle().getRows(), TableHandleAdapter.RowUIInfomation.GRID_ROW,
				TableHandleAdapter.RowUIInfomation.GRID_ROW);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.core.model.schematic.TableHandleAdapter#
	 * canMerge(java.util.List)
	 */
	@Override
	public boolean canMerge(List list) {
		return list != null && list.size() > 1;
	}

	/**
	 * return false for Grid Item for grid doesn't have slot
	 */
	@Override
	public boolean hasSlotHandleRow(int id) {
		return false;
	}

	/**
	 * Returns the defined height in model in Pixel.
	 *
	 * @return
	 */
	public String getDefinedHeight() {
		DimensionHandle handle = ((ReportItemHandle) getHandle()).getHeight();

		if (handle.getUnits() == null || handle.getUnits().length() == 0) {
			return null;
		} else if (DesignChoiceConstants.UNITS_PERCENTAGE.equals(handle.getUnits())) {
			return null;
		} else {
			int px = (int) DEUtil.convertoToPixel(handle);

			if (DEUtil.isFixLayout(getHandle())) {
				if (px == 0 && handle.isSet()) {
					px = 1;
				}
			}

			if (px <= 0) {
				return null;
			}

			return String.valueOf(px);
		}
	}

	@Override
	public boolean isSupportHeight() {
		return true;
	}
}
