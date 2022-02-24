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

package org.eclipse.birt.report.model.api.validators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.CascadingParameterGroup;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.interfaces.IAbstractScalarParameterModel;
import org.eclipse.birt.report.model.validators.AbstractElementValidator;

/**
 * Validates that type of the parameters in a cascading parameter group should
 * be "dynamic".
 * 
 */

public class CascadingParameterTypeValidator extends AbstractElementValidator {

	/**
	 * Singleton instance.
	 */

	private final static CascadingParameterTypeValidator instance = new CascadingParameterTypeValidator();

	/**
	 * Returns the instance of this validator.
	 * 
	 * @return the instance of this validator.
	 */

	public static CascadingParameterTypeValidator getInstance() {
		return instance;
	}

	/**
	 * Validates whether the type of the parameter is valid.
	 * 
	 * @param module  the module
	 * @param element the parameter to
	 * 
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List<SemanticException> validate(Module module, DesignElement element) {
		if (!(element instanceof ScalarParameter)) {
			return Collections.emptyList();
		}

		List<SemanticException> list = new ArrayList<SemanticException>();

		// Cascading parameter should be typed "dynamic"

		ScalarParameter param = (ScalarParameter) element;
		if (param.getContainer() instanceof CascadingParameterGroup && !DesignChoiceConstants.PARAM_VALUE_TYPE_DYNAMIC
				.equalsIgnoreCase(param.getStringProperty(module, IAbstractScalarParameterModel.VALUE_TYPE_PROP))) {
			list.add(new SemanticError(element, SemanticError.DESIGN_EXCEPTION_INVALID_SCALAR_PARAMETER_TYPE));
		}

		return list;
	}

}
