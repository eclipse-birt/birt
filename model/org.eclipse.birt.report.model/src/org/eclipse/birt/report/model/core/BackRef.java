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

package org.eclipse.birt.report.model.core;

/**
 * Represents the back reference for the referencable element. The back
 * reference provides the capability that the referencable element knows what
 * element is referring it. The element referring it is called "client element".
 * It contains the client element and element reference property name.
 */

public final class BackRef {

	/**
	 * The client element that refers to one referencable element.
	 */

	private DesignElement element;

	/**
	 * The name of the property that refers to one referencable element.
	 */

	private String propName;

	/**
	 * 
	 */

	private Structure struct;

	/**
	 * Constructs the back reference with the client element and the element
	 * reference property name.
	 * 
	 * @param obj  client element
	 * @param prop name of the property which refers to another element
	 */

	public BackRef(DesignElement obj, String prop) {
		element = obj;
		propName = prop;
		struct = null;
	}

	/**
	 * Constructs the back reference with the client element and the element
	 * reference property name.
	 * 
	 * @param obj      client element
	 * @param propName member reference
	 * @param struct
	 */

	public BackRef(Structure struct, String propName) {
		this.struct = struct;
		this.propName = propName;
	}

	/**
	 * Gets the client element of the back reference.
	 * 
	 * @return the client element
	 */

	public DesignElement getElement() {
		if (element != null)
			return element;

		return struct.getElement();
	}

	/**
	 * Gets the property name that refers to one referencable element.
	 * 
	 * @return the property name of the back reference
	 */

	public String getPropertyName() {
		return this.propName;
	}

	/**
	 * Gets the structure.
	 * 
	 * @return structure
	 */

	public Structure getStructure() {
		return this.struct;
	}
}
