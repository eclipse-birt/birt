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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.structures.ExtendedProperty;

/**
 * Represents the handle of Extended property. The Extended property represents
 * an Extended public or private property. The property has two parts: a name
 * and a value.
 */

public class ExtendedPropertyHandle extends StructureHandle {

	/**
	 * Constructs the handle of Extended property.
	 * 
	 * @param valueHandle the value handle for Extended property list of one
	 *                    property
	 * @param index       the position of this Extended property in the list
	 */

	public ExtendedPropertyHandle(SimpleValueHandle valueHandle, int index) {
		super(valueHandle, index);
	}

	/**
	 * Returns the property name.
	 * 
	 * @return the property name
	 */

	public String getName() {
		ExtendedProperty property = (ExtendedProperty) getStructure();
		return property.getName();
	}

	/**
	 * Sets the property name.
	 * 
	 * @param name the property name to set
	 */

	public void setName(String name) {
		try {
			MemberHandle member;

			member = getMember(ExtendedProperty.NAME_MEMBER);
			member.setStringValue(name);
		} catch (NameException e) {
			// Should not fail

			assert false;
		} catch (SemanticException e) {
			// Should not fail

			assert false;
		}
	}

	/**
	 * Returns the property value.
	 * 
	 * @return the property value
	 */

	public String getValue() {
		ExtendedProperty property = (ExtendedProperty) getStructure();
		return property.getValue();
	}

	/**
	 * Sets the property value.
	 * 
	 * @param value the value to set
	 */

	public void setValue(String value) {
		try {
			MemberHandle member;

			member = getMember(ExtendedProperty.VALUE_MEMBER);
			member.setStringValue(value);
		} catch (NameException e) {
			// Should not fail

			assert false;
		} catch (SemanticException e) {
			// Should not fail

			assert false;
		}
	}
}
