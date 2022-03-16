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

package org.eclipse.birt.chart.ui.swt;

import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.ui.swt.interfaces.IExpressionButton;

/**
 * The interface is used for subclass to manipulate query expression for
 * drag&drop and others operation.
 *
 * @since 2.3
 */
public interface IQueryExpressionManager {
	/**
	 * Return query object.
	 *
	 * @return
	 */
	Query getQuery();

	/**
	 * Update query with specified expression, which means to update the model of
	 * query using ui text).
	 *
	 * @param expression
	 */
	void updateQuery(String expression);

	/**
	 * Update the display text from a query definition string, which means to update
	 * the ui text using the model of query.
	 *
	 * @param expression
	 */
	void updateText(String expression);

	/**
	 * Returns display expression.
	 */
	String getDisplayExpression();

	/**
	 * Check if expression is valid to current query.
	 *
	 * @param expression
	 * @return
	 */
	boolean isValidExpression(String expression);

	/**
	 * Set tooltip for input control.
	 *
	 * @since 2.5
	 */
	void setTooltipForInputControl();

	/**
	 * Returns the ExpressionButton.
	 *
	 * @return The ExpressionButton.
	 */
	IExpressionButton getExpressionButton();
}
