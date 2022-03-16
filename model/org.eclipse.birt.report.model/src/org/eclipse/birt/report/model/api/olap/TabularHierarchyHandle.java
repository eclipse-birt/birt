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

package org.eclipse.birt.report.model.api.olap;

import java.util.List;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.ITabularHierarchyModel;

/**
 * Represents a Hierarchy.
 *
 * @see org.eclipse.birt.report.model.elements.olap.Hierarchy
 */

public class TabularHierarchyHandle extends HierarchyHandle implements ITabularHierarchyModel {

	/**
	 * Constructs a handle for the given design and design element. The application
	 * generally does not create handles directly. Instead, it uses one of the
	 * navigation methods available on other element handles.
	 *
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public TabularHierarchyHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Returns the data set of this hierarchy.
	 *
	 * @return the handle to the data set
	 */

	public DataSetHandle getDataSet() {
		return (DataSetHandle) getElementProperty(DATA_SET_PROP);
	}

	/**
	 * Sets the data set of this hierarchy.
	 *
	 * @param handle the handle of the data set
	 *
	 * @throws SemanticException if the property is locked, or the data-set is
	 *                           invalid.
	 */

	public void setDataSet(DataSetHandle handle) throws SemanticException {
		if (handle == null) {
			setStringProperty(DATA_SET_PROP, null);
		} else {
			ModuleHandle moduleHandle = handle.getRoot();
			String valueToSet = handle.getElement().getFullName();
			if (moduleHandle instanceof LibraryHandle) {
				String namespace = ((LibraryHandle) moduleHandle).getNamespace();
				valueToSet = StringUtil.buildQualifiedReference(namespace, valueToSet);
			}
			setStringProperty(DATA_SET_PROP, valueToSet);
		}
	}

	/**
	 * Returns the list of primary keys. The element in the list is a
	 * <code>String</code>.
	 *
	 * @return a list of primary keys if set, otherwise null
	 */

	public List getPrimaryKeys() {
		return getListProperty(PRIMARY_KEYS_PROP);
	}
}
