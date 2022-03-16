/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.extension;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.core.expressions.PropertyTester;

/**
 * ElementContainerTester
 */
public class ElementContainerTester extends PropertyTester {

	public ElementContainerTester() {
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if ("containerName".equals(property)) //$NON-NLS-1$
		{
			if (receiver instanceof DesignElementHandle) {
				DesignElementHandle container = ((DesignElementHandle) receiver).getContainer();
				String containerName = expectedValue.toString();
				return container.getDefn().getDisplayName().equals(containerName);
			}
		}
		return false;
	}
}
