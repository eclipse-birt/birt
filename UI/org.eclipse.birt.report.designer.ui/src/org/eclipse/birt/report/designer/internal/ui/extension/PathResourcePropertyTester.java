/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.PathResourceEntry;
import org.eclipse.core.expressions.PropertyTester;

/**
 * PathResourcePropertyTester
 */
public class PathResourcePropertyTester extends PropertyTester {

	public PathResourcePropertyTester() {
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (receiver instanceof PathResourceEntry) {
			PathResourceEntry entry = (PathResourceEntry) receiver;

			if ("isFile".equals(property)) //$NON-NLS-1$
			{
				return entry.isFile() == Boolean.parseBoolean(String.valueOf(expectedValue));
			} else if ("pathEndsWith".equals(property)) //$NON-NLS-1$
			{
				String path = entry.getURL().toString().toLowerCase();

				return path.endsWith(String.valueOf(expectedValue));
			}
		}

		return false;
	}

}
