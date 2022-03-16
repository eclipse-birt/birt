/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.expressions;

/**
 * IExpressionBuilder
 */
public interface IExpressionBuilder {

	/**
	 * @return Returns the dialog title
	 */
	String getTitle();

	/**
	 * Sets the dialog title
	 */
	void setTitle(String title);

	/**
	 * @return Returns the expression object
	 */
	Object getExpression();

	/**
	 * Sets the expression object
	 *
	 * @param exprObj
	 */
	void setExpression(Object exprObj);

	void setExpressionContext(IExpressionContext context);

	/**
	 * Opens the builder
	 *
	 * @return The return code. Refer to {@link org.eclipse.jface.window.Window} for
	 *         the possible values.
	 */
	int open();
}
