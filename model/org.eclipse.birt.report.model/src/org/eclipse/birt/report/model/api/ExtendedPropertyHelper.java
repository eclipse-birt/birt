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

package org.eclipse.birt.report.model.api;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ExtendedProperty;
import org.eclipse.birt.report.model.api.util.StringUtil;

/**
 * The helper class for <code>OdaDataSourceHandle</code> and
 * <code>OdaDataSetHandle</code>. It is used to set/get an extended property.
 */

final class ExtendedPropertyHelper {

	/**
	 * Returns the value of an extended property with the given name. If the
	 * extended property is not defined, <code>null</code> will be returned.
	 *
	 * @param element  the element that has extended property list
	 * @param propName the property name for structure lists that holds
	 *                 <code>ExtendedProperty</code>
	 * @param name     the name of an extended property
	 * @return the value of an extended property
	 *
	 */

	protected static String getExtendedProperty(DesignElementHandle element, String propName, String name) {
		if (StringUtil.isBlank(name)) {
			return null;
		}

		PropertyHandle propertyHandle = element.getPropertyHandle(propName);

		for (Iterator iter = propertyHandle.iterator(); iter.hasNext();) {
			ExtendedPropertyHandle prop = (ExtendedPropertyHandle) iter.next();

			if ((prop.getName() != null) && (prop.getName().equalsIgnoreCase(name))) {
				return prop.getValue();
			}
		}

		return null;
	}

	/**
	 * Sets an extended property with the given name and value. If the extended
	 * property does not exist, it will be added into the extended property list. If
	 * the extended property already exists, the value will be overwritten.
	 *
	 *
	 * @param element  the element that has extended property list
	 * @param propName the property name for structure lists that holds
	 *                 <code>ExtendedProperty</code>
	 * @param name     the name of an extended property
	 * @param value    the value of an extended property
	 * @throws SemanticException if <code>name</code> is <code>null</code> or an
	 *                           empty string after trimming.
	 */

	protected static void setExtendedProperty(DesignElementHandle element, String propName, String name, String value)
			throws SemanticException {
		PropertyHandle propertyHandle = element.getPropertyHandle(propName);

		for (Iterator iter = propertyHandle.iterator(); iter.hasNext();) {
			ExtendedPropertyHandle propHandle = (ExtendedPropertyHandle) iter.next();

			if ((propHandle.getName() != null) && (propHandle.getName().equalsIgnoreCase(name))) {
				propHandle.setValue(value);
				return;
			}
		}

		propertyHandle.addItem(new ExtendedProperty(name, value));
	}
}
