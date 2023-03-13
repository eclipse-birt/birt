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

import org.eclipse.birt.report.designer.core.model.views.outline.LibraryNode;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.util.DEUtil;

/**
 * This class is a representation of resource entry for library node.
 */
public class LibraryNodeEntry extends ReportElementEntry {

	/**
	 * Constructs a resource entry for the specified library node.
	 *
	 * @param library the specified library node.
	 * @param parent  the parent entry.
	 */
	public LibraryNodeEntry(LibraryNode library, ResourceEntry parent) {
		super(library, parent);
	}

	@Override
	public boolean equals(Object object) {
		if (object == null || !object.getClass().equals(getClass())) {
			return false;
		}

		if (object == this) {
			return true;
		} else {
			LibraryNodeEntry temp = (LibraryNodeEntry) object;
			LibraryNode tempLibrary = temp.getReportElement();
			LibraryNode thisLibrary = getReportElement();

			if (tempLibrary == thisLibrary) {
				return true;
			}

			if (tempLibrary != null && thisLibrary != null
					&& tempLibrary.getReportDesignHandle().getID() == thisLibrary.getReportDesignHandle().getID()
					&& DEUtil.isSameString(tempLibrary.getReportDesignHandle().getModule().getFileName(),
							thisLibrary.getReportDesignHandle().getModule().getFileName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		LibraryNode library = getReportElement();

		if (library == null) {
			return super.hashCode();
		}

		String fileName = library.getReportDesignHandle().getModule().getFileName();

		return (int) library.getReportDesignHandle().getID() * 7 + (fileName == null ? 0 : fileName.hashCode());
	}

	@Override
	public LibraryNode getReportElement() {
		Object library = super.getReportElement();

		return library instanceof LibraryNode ? (LibraryNode) library : null;
	}
}
