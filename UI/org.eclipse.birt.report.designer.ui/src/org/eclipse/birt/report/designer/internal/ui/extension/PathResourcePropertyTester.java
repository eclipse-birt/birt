/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
