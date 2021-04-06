/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public boolean isReservedString(String expression);

	/**
	 * Checks if specified expression is valid.
	 * 
	 * @param expression
	 * @return
	 */
	public boolean isValidExpression(String expression);
}
