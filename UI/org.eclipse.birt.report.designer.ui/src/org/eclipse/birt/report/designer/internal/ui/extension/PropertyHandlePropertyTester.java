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

package org.eclipse.birt.report.designer.internal.ui.extension;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.core.expressions.PropertyTester;

/**
 * PropertyHandlePropertyTester
 */
public class PropertyHandlePropertyTester extends PropertyTester {

	public PropertyHandlePropertyTester() {
	}

	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if ("name".equals(property)) //$NON-NLS-1$
		{
			if (receiver instanceof PropertyHandle) {
				String propertyName = expectedValue.toString();
				return propertyName.equals(((PropertyHandle) receiver).getPropertyDefn().getName());

			}
		} else if ("elementName".equals(property)) //$NON-NLS-1$
		{
			if (receiver instanceof PropertyHandle) {
				try {
					DesignElementHandle handle = ((PropertyHandle) receiver).getElementHandle();
					return handle.getDefn().getName().equals(expectedValue);
				} catch (NumberFormatException e) {
				}
			}
		}
		return false;
	}

}
