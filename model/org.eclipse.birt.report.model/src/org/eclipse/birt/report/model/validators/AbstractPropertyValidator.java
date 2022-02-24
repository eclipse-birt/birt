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

package org.eclipse.birt.report.model.validators;

import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;

/**
 * The base abstract validator class to validate one specific property of
 * element in report.
 */

public abstract class AbstractPropertyValidator extends AbstractSemanticValidator {

	/**
	 * Validates the specific property of the given element in report.
	 * 
	 * @param module   the module
	 * @param element  the given element to validate
	 * @param propName name of the property to validate
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public abstract List<SemanticException> validate(Module module, DesignElement element, String propName);

}
