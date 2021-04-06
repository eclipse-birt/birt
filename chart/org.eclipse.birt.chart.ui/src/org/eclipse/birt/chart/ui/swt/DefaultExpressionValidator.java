/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
