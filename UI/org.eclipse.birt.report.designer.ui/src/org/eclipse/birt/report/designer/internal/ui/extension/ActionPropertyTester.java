/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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
