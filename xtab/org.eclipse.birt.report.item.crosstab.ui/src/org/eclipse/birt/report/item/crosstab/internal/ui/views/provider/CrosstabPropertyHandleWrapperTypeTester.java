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

package org.eclipse.birt.report.item.crosstab.internal.ui.views.provider;

import org.eclipse.core.expressions.PropertyTester;

/**
 * CrosstabPropertyHandleWrapperTypeTester
 */
public class CrosstabPropertyHandleWrapperTypeTester extends PropertyTester {

	public CrosstabPropertyHandleWrapperTypeTester() {
	}

	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if ("type".equals(property)) //$NON-NLS-1$
		{
			if (receiver instanceof CrosstabPropertyHandleWrapper) {
				String propertyName = expectedValue.toString();
				return propertyName.equals((((CrosstabPropertyHandleWrapper) receiver).getTestType()));

			}
		}
		return false;
	}

}
