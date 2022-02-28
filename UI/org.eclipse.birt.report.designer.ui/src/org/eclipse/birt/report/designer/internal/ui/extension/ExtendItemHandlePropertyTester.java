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
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.core.expressions.PropertyTester;

/**
 * ExtendItemHandlePropertyTester
 */
public class ExtendItemHandlePropertyTester extends PropertyTester {

	public ExtendItemHandlePropertyTester() {
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if ("extensionName".equals(property)) //$NON-NLS-1$
		{
			if (receiver instanceof ExtendedItemHandle) {
				String extensionName = expectedValue.toString();
				return extensionName.equals(((ExtendedItemHandle) receiver).getExtensionName());

			}
		} else if ("containerName".equals(property)) //$NON-NLS-1$
		{
			if (receiver instanceof ExtendedItemHandle) {
				DesignElementHandle container = ((ExtendedItemHandle) receiver).getContainer();
				if (container == null) {
					return false;
				}
				String containerName = expectedValue.toString();
				return container.getDefn().getName().equals(containerName);
			}
		} else if ("containerPropertyName".equals(property)) //$NON-NLS-1$
		{
			if (receiver instanceof ExtendedItemHandle) {
				PropertyHandle container = ((ExtendedItemHandle) receiver).getContainerPropertyHandle();
				if (container == null) {
					return false;
				}
				String containerName = expectedValue.toString();
				return container.getDefn().getName().equals(containerName);
			}
		}
		return false;
	}

}
