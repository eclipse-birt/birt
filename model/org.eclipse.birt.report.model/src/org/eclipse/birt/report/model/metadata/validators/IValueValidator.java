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

	String getName();

	/**
	 * Validate a specific property.
	 *
	 * @param module the module
	 * @param defn   definition of the property.
	 * @param value  value to be validated.
	 * @throws PropertyValueException if the property has any semantic error.
	 */

	void validate(Module module, PropertyDefn defn, Object value) throws PropertyValueException;

}
