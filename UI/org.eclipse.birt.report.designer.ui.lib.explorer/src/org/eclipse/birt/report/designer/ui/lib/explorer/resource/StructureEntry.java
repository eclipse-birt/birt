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
import org.eclipse.birt.report.model.api.StructureHandle;

/**
 * This class is a representation of resource entry for structure.
 */
public class StructureEntry extends ReportElementEntry {

	/** The index of structure. */
	private int index;

	/**
	 * Constructs a resource entry for the specified structure.
	 * 
	 * @param structure the specified structure.
	 * @param parent    the parent entry.
	 * @param index     the index of structure.
	 */
	public StructureEntry(StructureHandle structure, ResourceEntry parent, int index) {
		super(structure, parent);
		this.index = index;
	}

	@Override
	public boolean equals(Object object) {
		if (object == null || !object.getClass().equals(getClass())) {
			return false;
		}

		if (object == this) {
			return true;
		} else {
			StructureEntry temp = (StructureEntry) object;
			StructureHandle tempStructure = temp.getReportElement();
			StructureHandle thisStructure = getReportElement();

			if (tempStructure == thisStructure) {
				return true;
			}

			if (temp.index == this.index && tempStructure != null && thisStructure != null
					&& tempStructure.getElement().getID() == thisStructure.getElement().getID() && DEUtil.isSameString(
							tempStructure.getModule().getFileName(), thisStructure.getModule().getFileName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		StructureHandle structure = getReportElement();

		if (structure == null) {
			return super.hashCode();
		}

		String fileName = structure.getModule().getFileName();

		return (int) (structure.getElement().getID() * 7 + index) * 7 + (fileName == null ? 0 : fileName.hashCode());
	}

	@Override
	public StructureHandle getReportElement() {
		Object structure = super.getReportElement();

		return structure instanceof StructureHandle ? (StructureHandle) structure : null;
	}
}
