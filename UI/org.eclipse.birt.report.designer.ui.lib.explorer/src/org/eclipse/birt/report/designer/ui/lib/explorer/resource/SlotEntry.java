/*******************************************************************************
 * Copyright (c) 2007, 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.lib.explorer.resource;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.SlotHandle;

/**
 * This class is a representation of resource entry for slot.
 */
public class SlotEntry extends ReportElementEntry {

	/**
	 * Constructs a resource entry for the specified slot.
	 * 
	 * @param slot   the specified slot.
	 * @param parent the parent entry.
	 */
	public SlotEntry(SlotHandle slot, ResourceEntry parent) {
		super(slot, parent);
	}

	@Override
	public boolean equals(Object object) {
		if (object == null || !object.getClass().equals(getClass())) {
			return false;
		}

		if (object == this) {
			return true;
		} else {
			SlotEntry temp = (SlotEntry) object;
			SlotHandle tempSlot = temp.getReportElement();
			SlotHandle thisSlot = getReportElement();

			if (tempSlot == thisSlot) {
				return true;
			}

			if (tempSlot != null && thisSlot != null && tempSlot.getSlotID() == thisSlot.getSlotID()
					&& tempSlot.getElement().getID() == thisSlot.getElement().getID()
					&& DEUtil.isSameString(tempSlot.getModule().getFileName(), thisSlot.getModule().getFileName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		SlotHandle slot = getReportElement();

		if (slot == null) {
			return super.hashCode();
		}

		String fileName = slot.getModule().getFileName();

		return (int) (slot.getElement().getID() * 7 + slot.getSlotID()) * 7
				+ (fileName == null ? 0 : fileName.hashCode());
	}

	@Override
	public SlotHandle getReportElement() {
		Object slot = super.getReportElement();

		return slot instanceof SlotHandle ? (SlotHandle) slot : null;
	}
}
