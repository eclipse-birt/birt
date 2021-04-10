/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.metadata.validators;

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * Validator used to validate a property using a specific rule.
 * 
 */

public interface IValueValidator {

	/**
	 * Return the name of the validator.
	 * 
	 * @return name of the validator.
	 */

	public String getName();

	/**
	 * Validate a specific property.
	 * 
	 * @param module the module
	 * @param defn   definition of the property.
	 * @param value  value to be validated.
	 * @throws PropertyValueException if the property has any semantic error.
	 */

	public void validate(Module module, PropertyDefn defn, Object value) throws PropertyValueException;

}