/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.tests.table;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FactoryPropertyHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.extension.IReportItemFactory;
import org.eclipse.birt.report.model.api.extension.ReportItem;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

/**
 * Implements <code>IReportItem</code> for testing
 */

public class ReportItemImpl extends ReportItem implements IReportItem, Cloneable {

	private ModuleHandle moduleHandle = null;
	private DesignElementHandle extItemHandle = null;

	/**
	 * Constructs an element.
	 * 
	 * @param extDefn
	 * @param elementHandle
	 */

	public ReportItemImpl(IReportItemFactory extDefn, DesignElementHandle elementHandle) {
		assert elementHandle != null;
		this.moduleHandle = elementHandle.getModuleHandle();
		extItemHandle = elementHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IExtendedElement#getProperty(java.lang.
	 * String)
	 */
	public Object getProperty(String propName) {
		IPropertyDefn defn = extItemHandle.getPropertyDefn(propName);
		if (defn == null)
			return null;
		assert ((ElementPropertyDefn) defn).isStyleProperty();

		FactoryPropertyHandle factoryHandle = extItemHandle.getFactoryPropertyHandle(propName);
		Object value = factoryHandle == null ? null : factoryHandle.getValue();
		if (value != null)
			return value;

		DesignElementHandle container = extItemHandle.getContainer();
		return container == null ? null : container.getProperty(propName);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.extension.ReportItem#canExport()
	 */
	public boolean canExport() {
		if ("TestingTable1".equals(extItemHandle.getStringProperty(ExtendedItemHandle.EXTENSION_NAME_PROP))) //$NON-NLS-1$
			return false;
		return true;
	}
}
