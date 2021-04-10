/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.extension;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jface.action.IAction;

/**
 * ActionPropertyTester
 */
public class ActionPropertyTester extends PropertyTester {

	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if ("id".equals(property)) //$NON-NLS-1$
		{
			if (receiver instanceof IAction) {
				String id = ((IAction) receiver).getId();

				return (id == null) ? (expectedValue == null) : (id.equals(expectedValue));
			}
		}
		return false;
	}

}
