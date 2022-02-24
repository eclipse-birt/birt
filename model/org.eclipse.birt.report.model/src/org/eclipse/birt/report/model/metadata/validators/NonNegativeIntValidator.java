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
 * Validates that a Integer value should be none-negative( larger or equal than
 * zero ).
 */

public class NonNegativeIntValidator extends SimpleValueValidator {

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

		assert value instanceof Integer;

		if (((Integer) value).intValue() < 0)
			throw new PropertyValueException(null, defn, value, PropertyValueException.DESIGN_EXCEPTION_NEGATIVE_VALUE);

	}

}
