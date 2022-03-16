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
import org.eclipse.birt.report.model.api.DesignElementHandle;

/**
 * This class is a representation of resource entry for design element.
 */
public class DesignElementEntry extends ReportElementEntry {

	/**
	 * Constructs a resource entry for the specified design element.
	 *
	 * @param element the specified design element.
	 * @param parent  the parent entry.
	 */
	public DesignElementEntry(DesignElementHandle element, ResourceEntry parent) {
		super(element, parent);
	}

	@Override
	public boolean equals(Object object) {
		if (object == null || !object.getClass().equals(getClass())) {
			return false;
		}

		if (object == this) {
			return true;
		} else {
			DesignElementEntry temp = (DesignElementEntry) object;
			DesignElementHandle tempElement = temp.getReportElement();
			DesignElementHandle thisElement = getReportElement();

			if (tempElement == thisElement) {
				return true;
			}

			if (tempElement != null && thisElement != null
					&& tempElement.getElement().getID() == thisElement.getElement().getID()
					&& DEUtil.isSameString(tempElement.getModule().getFileName(), thisElement.getModule().getFileName())
					&& (tempElement.getElement().getName() == null ? true
							: (tempElement.getElement().getName().equals(thisElement.getElement().getName())))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		DesignElementHandle element = getReportElement();

		if (element == null) {
			return super.hashCode();
		}

		String fileName = element.getModule().getFileName();

		return (int) (element.getElement().getID() * 7
				+ (element.getElement().getName() == null ? 0 : (element.getElement().getName().hashCode()))) * 7
				+ (fileName == null ? 0 : fileName.hashCode());
	}

	@Override
	public DesignElementHandle getReportElement() {
		Object element = super.getReportElement();

		return element instanceof DesignElementHandle ? (DesignElementHandle) element : null;
	}
}
