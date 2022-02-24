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

package org.eclipse.birt.report.model.metadata.validators;

/**
 * Specific property validator, the check rule can be applied to one specific
 * property.
 * 
 */

public abstract class SimpleValueValidator implements IValueValidator {

	/**
	 * The internal name of the validator.
	 */

	private String name = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.IMetaValidator#getName()
	 */

	public String getName() {
		return name;
	}

	/**
	 * Set the name of the validator, name is referenced by a property as key to the
	 * validator.
	 * 
	 * @param name name of the validator, can not be <code>null</code>.
	 */

	public void setName(String name) {
		assert name != null;

		this.name = name;
	}
}
