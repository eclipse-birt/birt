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

package org.eclipse.birt.doc.legacy;

import java.util.ArrayList;

public class SpecElement extends SpecObject {
	public int type;
	public int isAbstract; // rom.def
	public String designObjName; // rom.def
	public String stateObjName; // rom.def
	public String xmlSummary; // Doc
	public String xmlElement; // rom.def
	public String styleNames; // rom.def
	public ArrayList properties = new ArrayList();
	public ArrayList methods = new ArrayList();
	public ArrayList slots = new ArrayList();
	public ArrayList inheritedProperties = new ArrayList();
	public static final int ELEMENT = 0;
	public static final int STRUCTURE = 1;

	public void addProperty(SpecProperty prop) {
		properties.add(prop);
	}

	public SpecProperty getProperty(String propName) {
		for (int i = 0; i < properties.size(); i++) {
			SpecProperty prop = (SpecProperty) properties.get(i);
			if (prop.name.equals(propName)) {
				return prop;
			}
		}
		return null;
	}

	/**
	 * @param method
	 */
	public void addMethod(SpecMethod method) {
		methods.add(method);
	}

	public SpecMethod getMethod(String methodName) {
		for (int i = 0; i < methods.size(); i++) {
			SpecMethod method = (SpecMethod) methods.get(i);
			if (method.name.equals(methodName)) {
				return method;
			}
		}
		return null;
	}

	public SpecSlot getSlot(String slotName) {
		for (int i = 0; i < slots.size(); i++) {
			SpecSlot slot = (SpecSlot) slots.get(i);
			if (slot.name.equals(slotName)) {
				return slot;
			}
		}
		return null;
	}

	public void addSlot(SpecSlot slot) {
		slots.add(slot);
	}

	/**
	 * @param prop
	 */
	public void addInheritedProperty(SpecInheritedProperty prop) {
		inheritedProperties.add(prop);
	}

	public String getTypeName() {
		if (type == ELEMENT) {
			return "Element";
		}
		return "Structure";
	}
}
