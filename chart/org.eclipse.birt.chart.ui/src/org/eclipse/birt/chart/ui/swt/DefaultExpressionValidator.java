/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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

package org.eclipse.birt.chart.ui.swt;

import org.eclipse.birt.chart.ui.swt.interfaces.IExpressionValidator;

/**
 * The default implementation, don't do any validation.
 * 
 * @since 2.6.2
 */

public class DefaultExpressionValidator implements IExpressionValidator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IExpressionValidator#
	 * isReservedString(java.lang.String)
	 */
	public boolean isReservedString(String expression) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IExpressionValidator#
	 * isValidExpression(java.lang.String)
	 */
	public boolean isValidExpression(String expression) {
		return true;
	}

}
