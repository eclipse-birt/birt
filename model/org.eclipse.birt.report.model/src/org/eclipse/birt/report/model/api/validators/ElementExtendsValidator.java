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
import java.util.List;

import org.eclipse.birt.report.model.api.command.InvalidParentException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.validators.AbstractElementValidator;

/**
 * Validates the element extends property. If the value can refer to an actual
 * element, it will be resolved after validation.
 * <h3>Rule</h3> The rule is that the element extends value should refer to an
 * actual element in the same report or included libraries.
 * <h3>Applicability</h3> This validator is only applied to the element extends
 * property.
 */

public class ElementExtendsValidator extends AbstractElementValidator {

	/**
	 * Name of this validator.
	 */

	public final static String NAME = "ElementExtendsValidator"; //$NON-NLS-1$

	/**
	 * The singleton instance of the validator.
	 */

	protected final static ElementExtendsValidator instance = new ElementExtendsValidator();

	/**
	 * Returns the singleton validator instance.
	 *
	 * @return the validator instance
	 */

	public static ElementExtendsValidator getInstance() {
		return instance;
	}

	/**
	 * Validates the element reference value can refer to an actual element.
	 *
	 * @param module  the module
	 * @param element the element to validate
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	@Override
	public List validate(Module module, DesignElement element) {
		List list = new ArrayList();

		if (!StringUtil.isEmpty(element.getExtendsName()) && element.getExtendsElement() == null) {
			list.add(new InvalidParentException(element, element.getExtendsName(),
					InvalidParentException.DESIGN_EXCEPTION_PARENT_NOT_FOUND));
		}

		return list;
	}

}
