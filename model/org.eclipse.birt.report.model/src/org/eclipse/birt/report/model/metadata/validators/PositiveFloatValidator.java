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
 * Validates that a double-precision float value should be none-negative( larger
 * or equal than zero ).
 */

public class PositiveFloatValidator extends SimpleValueValidator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.IMetaValidator#validate(org.eclipse.
	 * birt.report.model.elements.ReportDesign,
	 * org.eclipse.birt.report.model.metadata.PropertyDefn, java.lang.Object)
	 */
	public void validate(Module module, PropertyDefn defn, Object value) throws PropertyValueException {
		if (value == null)
			return;

		assert value instanceof Double;

		if (((Double) value).doubleValue() <= 0.0d)
			throw new PropertyValueException(null, defn, value,
					PropertyValueException.DESIGN_EXCEPTION_NON_POSITIVE_VALUE);

	}

}