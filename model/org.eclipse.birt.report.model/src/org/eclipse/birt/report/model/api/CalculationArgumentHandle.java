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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.CalculationArgument;

/**
 * CalculationArgumentHandle.
 */
public class CalculationArgumentHandle extends StructureHandle {

	/**
	 * Constructs the handle of calculation argument.
	 * 
	 * @param valueHandle the value handle for calculation argument list of one
	 *                    property
	 * @param index       the position of this calculation argument in the list
	 */

	public CalculationArgumentHandle(SimpleValueHandle valueHandle, int index) {
		super(valueHandle, index);
	}

	/**
	 * Returns the argument name.
	 * 
	 * @return the argument name.
	 */

	public String getName() {
		return getStringProperty(CalculationArgument.NAME_MEMBER);
	}

	/**
	 * Sets the argument name.
	 * 
	 * @param argumentName the argument name to set
	 * @throws SemanticException
	 */

	public void setName(String argumentName) throws SemanticException {
		setProperty(CalculationArgument.NAME_MEMBER, argumentName);
	}

	/**
	 * Gets the expression handle for the value member. Then use the returned handle
	 * to do get/set action.
	 * 
	 * @return
	 */
	public ExpressionHandle getValue() {
		return getExpressionProperty(CalculationArgument.VALUE_MEMBER);
	}
}
