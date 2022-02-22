/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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

package org.eclipse.birt.chart.ui.swt.interfaces;

/**
 * This class provides functions to validate if specified expression is valid or
 * legal.
 *
 * @since 2.6.2
 */

public interface IExpressionValidator {
	/**
	 * Checks if specified expression is a reserved string, not a valid expression.
	 *
	 * @param expression
	 * @return
	 */
	boolean isReservedString(String expression);

	/**
	 * Checks if specified expression is valid.
	 *
	 * @param expression
	 * @return
	 */
	boolean isValidExpression(String expression);
}
