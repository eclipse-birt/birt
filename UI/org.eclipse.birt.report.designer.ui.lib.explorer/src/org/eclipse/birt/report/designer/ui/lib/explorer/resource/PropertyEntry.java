/*******************************************************************************
 * Copyright (c) 2007, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.lib.explorer.resource;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.PropertyHandle;

/**
 * This class is a representation of resource entry for property.
 */
public class PropertyEntry extends ReportElementEntry {

	public PropertyEntry(PropertyHandle property, ResourceEntry parent) {
		super(property, parent);
	}

	@Override
	public boolean equals(Object object) {
		if (object == null || !object.getClass().equals(getClass())) {
			return false;
		}

		if (object == this) {
			return true;
		} else {
			PropertyEntry temp = (PropertyEntry) object;
			PropertyHandle tempProperty = temp.getReportElement();
			PropertyHandle thisProperty = getReportElement();

			if (tempProperty == thisProperty) {
				return true;
			}

			if (tempProperty != null && thisProperty != null
					&& tempProperty.getDefn().getName().equals(thisProperty.getDefn().getName())
					&& tempProperty.getElement().getID() == thisProperty.getElement().getID() && DEUtil.isSameString(
							tempProperty.getModule().getFileName(), thisProperty.getModule().getFileName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		PropertyHandle property = getReportElement();

		if (property == null) {
			return super.hashCode();
		}

		String fileName = property.getModule().getFileName();

		return (int) (property.getElement().getID() * 7 + property.getDefn().getName().hashCode()) * 7
				+ (fileName == null ? 0 : fileName.hashCode());
	}

	@Override
	public PropertyHandle getReportElement() {
		Object property = super.getReportElement();

		return property instanceof PropertyHandle ? (PropertyHandle) property : null;
	}
}
