/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.model.api.elements.table;

import java.util.ArrayList;
import java.util.List;

/**
 * The slot for the header and the footer in groups.
 */

class LayoutGroupBand {

	/**
	 * Slots in groups.
	 */

	private List<LayoutSlot> slots = new ArrayList<LayoutSlot>();

	/**
	 * The container of the table.
	 */

	private LayoutTable tableContainer;

	/**
	 * Constructs a <code>LayoutGroupSlot</code> with the given table and the column
	 * count.
	 * 
	 * @param table    the table has this group slot.
	 * @param colCount the column count of the table
	 */

	LayoutGroupBand(LayoutTable table, int colCount) {
		tableContainer = table;
	}

	/**
	 * Adds a Group Header or Group Footer slot to the group slot.
	 * 
	 * @param groupLevel       the 1-based level of the group
	 * @param expectedColCount the column count
	 * @return the created slot
	 */

	public LayoutSlot addSlot(int groupLevel, int expectedColCount) {
		LayoutSlot slot = new LayoutSlot(tableContainer, groupLevel, expectedColCount);
		slots.add(slot);

		return slot;
	}

	/**
	 * Returns the number of the slots in the group.
	 * 
	 * @return the number of the slots in the group
	 */

	public int getGroupCount() {
		return slots.size();
	}

	/**
	 * Gets a slot with the given level.
	 * 
	 * @param groupLevel 1-based level of the group
	 * @return a slot with the given level
	 */

	public LayoutSlot getLayoutSlotWithGroupLevel(int groupLevel) {
		for (int i = 0; i < slots.size(); i++) {
			LayoutSlot slot = slots.get(i);
			if (groupLevel == slot.getGroupLevel())
				return slot;
		}

		return null;
	}

	/**
	 * Returns the slot at the position of <code>index</code>.
	 * 
	 * @param index the 0-based position
	 * @return the slot
	 */

	public LayoutSlot getLayoutSlot(int index) {
		if (index > slots.size() - 1)
			return null;

		return slots.get(index);
	}

	/**
	 * Returns the string that shows the layout. Mainly for the debug.
	 * 
	 * @return the string that shows the layout
	 */

	public String getLayoutString() {
		if (slots.isEmpty())
			return ""; //$NON-NLS-1$

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < slots.size(); i++) {
			LayoutSlot slot = slots.get(i);
			sb.append(slot.getLayoutString());
		}
		return sb.toString();
	}

	/**
	 * Returns the maximal count of columns in the group slot.
	 * 
	 * @return the maximal count of columns in the group slot
	 */

	protected int getColumnCount() {
		int colCount = 0;

		for (int i = 0; i < slots.size(); i++) {
			LayoutSlot obj = slots.get(i);
			int tmpCount = obj.getColumnCount();

			if (tmpCount > colCount)
				colCount = tmpCount;
		}

		return colCount;
	}
}
