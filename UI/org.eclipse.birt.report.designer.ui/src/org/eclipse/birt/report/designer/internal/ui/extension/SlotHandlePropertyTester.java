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
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.core.expressions.PropertyTester;

/**
 * SlotHandlePropertyTester
 */
public class SlotHandlePropertyTester extends PropertyTester {

	public SlotHandlePropertyTester() {
	}

	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if ("id".equals(property)) //$NON-NLS-1$
		{
			if (receiver instanceof SlotHandle) {
				try {
					int slotId = Integer.parseInt(expectedValue.toString());
					return slotId == ((SlotHandle) receiver).getSlotID();
				} catch (NumberFormatException e) {
				}
			}
		} else if ("elementName".equals(property)) //$NON-NLS-1$
		{
			if (receiver instanceof SlotHandle) {
				try {
					DesignElementHandle handle = ((SlotHandle) receiver).getElementHandle();
					return handle.getDefn().getName().equals(expectedValue);
				} catch (NumberFormatException e) {
				}
			}
		}
		return false;
	}

}
