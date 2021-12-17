/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  Others: See git history
 *******************************************************************************/

package org.eclipse.birt.report.debug.internal.core.vm.js;

import org.eclipse.birt.report.debug.internal.core.vm.VMConstants;
import org.eclipse.birt.report.debug.internal.core.vm.VMValue;
import org.eclipse.birt.report.debug.internal.core.vm.VMVariable;

/**
 * JsVariable
 */
public class JsVariable implements VMVariable, VMConstants, Comparable {

	private String name;
	private String typeName;
	private JsValue value;

	public JsVariable(Object value, String name) {
		this.name = name;
		this.value = new JsValue(value);
	}

	JsVariable(Object value, String name, String reservedTypeName) {
		this.name = name;
		this.value = new JsValue(value, reservedTypeName);
	}

	JsVariable(Object value, String name, boolean isPrimitive) {
		this.name = name;
		this.value = new JsValue(value, isPrimitive);
	}

	public String getName() {
		return name;
	}

	public VMValue getValue() {
		return value;
	}

	public String getTypeName() {
		if (typeName != null) {
			return typeName;
		}

		if (value != null) {
			return value.getTypeName();
		}

		return "null"; //$NON-NLS-1$
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public int compareTo(Object arg0) {
		if (arg0 instanceof VMVariable) {
			VMVariable that = (VMVariable) arg0;

			if (name != null && !name.equals("this")) //$NON-NLS-1$
			{
				int idx1 = parseArrayElement(name);

				if (idx1 != -1) {
					int idx2 = parseArrayElement(that.getName());

					if (idx2 != -1) {
						return idx1 - idx2;
					}
				}

				return name.compareToIgnoreCase(that.getName());
			}
		}
		return -1;
	}

	private static int parseArrayElement(String name) {
		if (name != null && name.length() > 2 && name.charAt(0) == '[' && name.charAt(name.length() - 1) == ']') {
			try {
				return Integer.parseInt(name.substring(1, name.length() - 1));
			} catch (NumberFormatException e) {
			}
		}

		return -1;
	}
}
