/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.item.crosstab.internal.ui.views.provider;

import org.eclipse.birt.report.designer.core.model.LibraryHandleAdapter;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.core.runtime.IAdaptable;

/**
 * 
 */

public class CrosstabPropertyHandleWrapper implements IAdaptable {

	private PropertyHandle handle;
	private String type;

	public Object getAdapter(Class adapter) {
		if (adapter == LibraryHandleAdapter.class) {
			DesignElementHandle element = handle.getElementHandle();
			if (element instanceof ExtendedItemHandle)
				return element;
		}
		return null;
	}

	public void setTestType(String testType) {
		this.type = testType;
	}

	public String getTestType() {
		if (type == null) {
			return getModel().getPropertyDefn().getName();
		}
		return type;
	}

	public CrosstabPropertyHandleWrapper(PropertyHandle handle) {
		this.handle = handle;
	}

	public PropertyHandle getModel() {
		return handle;
	}

	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof CrosstabPropertyHandleWrapper))
			return false;
		return ((CrosstabPropertyHandleWrapper) obj).getModel() == getModel();
	}

	public int hashCode() {
		if (getModel() != null)
			return getModel().hashCode();
		return super.hashCode();
	}
}
