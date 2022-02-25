/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.model.command;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

/**
 * Changes the value of a property. The caller must have previously validated
 * that the property name is valid, that the new value is valid and so on.
 */

public class PropertyRecord extends PropertyRecordImpl {

	/**
	 * Constructor.
	 *
	 * @param propertyOwner the report element that has the property
	 * @param name          the name of the property to change
	 * @param value         the new value
	 */

	public PropertyRecord(DesignElement propertyOwner, String name, Object value) {
		super(propertyOwner, name, value);
	}

	/**
	 * Constructor.
	 *
	 * @param propertyOwner the element that has the property to set
	 * @param prop          the definition of the property to set
	 * @param value         the new value
	 */

	public PropertyRecord(DesignElement propertyOwner, ElementPropertyDefn prop, Object value) {
		super(propertyOwner, prop, value);
	}
}
