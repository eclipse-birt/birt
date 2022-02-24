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

package org.eclipse.birt.report.model.api.elements.table;

import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;

/**
 * The table group model for a <code>TableGroup</code> element.
 */

public class LayoutGroup {

	/**
	 * The 1-based group level.
	 */

	private int groupLevel;

	/**
	 * The container of the layout table group.
	 */

	private LayoutTable table;

	/**
	 * Constructs a <code>LayoutGroup</code> with the given table and the group
	 * level.
	 * 
	 * @param table      the layout table
	 * @param groupLevel the level of the group
	 */

	protected LayoutGroup(LayoutTable table, int groupLevel) {
		this.table = table;
		this.groupLevel = groupLevel;

		assert groupLevel <= table.getGroupCount();
	}

	/**
	 * Returns the corresponding handle of the table group.
	 * 
	 * @return the corresponding handle of the table group
	 */

	public TableGroupHandle getGroup() {
		SlotHandle slots = table.getTable().getGroups();
		return (TableGroupHandle) slots.get(groupLevel - 1);
	}

	/**
	 * Returns the layout slot of the HEADER_SLOT.
	 * 
	 * @return the layout slot of the HEADER_SLOT
	 */

	public LayoutSlot getLayoutSlotHeader() {
		LayoutGroupBand groups = table.getGroupHeaders();
		return groups.getLayoutSlot(groupLevel);
	}

	/**
	 * Returns the layout slot of the HEADER_FOOTER.
	 * 
	 * @return the layout slot of the HEADER_FOOTER
	 */

	public LayoutSlot getLayoutSlotFooter() {
		LayoutGroupBand groups = table.getGroupFooters();
		return groups.getLayoutSlot(groupLevel);
	}
}
